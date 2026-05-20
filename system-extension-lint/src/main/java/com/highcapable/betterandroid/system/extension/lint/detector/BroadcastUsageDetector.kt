/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019 HighCapable
 * https://github.com/BetterAndroid/BetterAndroid
 *
 * Apache License Version 2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is created by fankes on 2026/5/13.
 */
package com.highcapable.betterandroid.system.extension.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.system.extension.lint.DeclaredSymbol
import com.highcapable.betterandroid.system.extension.lint.detector.extension.asCall
import com.highcapable.betterandroid.system.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiLocalVariable
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.toUElementOfType

class BroadcastUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val SEND_BROADCAST = "sendBroadcast"
        private const val REGISTER_RECEIVER = "registerReceiver"

        private val setPackageRegex = """^setPackage\((.+)\)$""".toRegex()
        private val applyIntentRegex = Regex("""^(.*)\.apply\s*\{(.*)}$""", setOf(RegexOption.DOT_MATCHES_ALL))
        private val setPackageIntentRegex = Regex("""^(.*)\.setPackage\((.+)\)$""", setOf(RegexOption.DOT_MATCHES_ALL))
        private val intentConstructorRegex = Regex("""^Intent\((.*)\)$""", setOf(RegexOption.DOT_MATCHES_ALL))

        val ISSUE = Issue.create(
            id = "ReplaceWithBroadcastExtension",
            briefDescription = "Use system-extension's broadcast extensions instead.",
            explanation = """
                Using `sendBroadcast(Intent(...))` with a clearly constructed `Intent`, or manually \
                creating an anonymous `BroadcastReceiver` for `registerReceiver(...)`, can be \
                simplified by using the broadcast extensions from BetterAndroid system-extension \
                library.

                The `Broadcast.kt` provides:
                - A shorter builder-style `sendBroadcast(...)` API
                - Direct support for package name, receiver permission and options
                - Simpler anonymous receiver registration through `registerReceiver(...)`
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                context.sendBroadcast(Intent("com.example.app.action.DEMO").apply {
                    setPackage("com.example.app")
                    putExtra("message", "Hello")
                })

                context.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        // Handle the broadcast.
                    }
                }, IntentFilter("com.example.app.action.DEMO"))

                // After
                context.sendBroadcast("com.example.app") {
                    action = "com.example.app.action.DEMO"
                    putExtra("message", "Hello")
                }

                context.registerReceiver(IntentFilter("com.example.app.action.DEMO")) { context, intent ->
                    // Handle the broadcast.
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                BroadcastUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java as Class<out UElement>)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            reportSendBroadcast(node)
            reportRegisterReceiver(node)
        }

        private fun reportSendBroadcast(node: UCallExpression) {
            if (node.methodName != SEND_BROADCAST) return

            val intentSpec = resolveIntentSpec(node.valueArguments.firstOrNull()) ?: return
            val receiverPermission = node.valueArguments.getOrNull(1).takeUnlessNullLiteral()?.asSourceString()
            val options = node.valueArguments.getOrNull(2).takeUnlessNullLiteral()?.asSourceString()

            val arguments = buildExtensionArguments(intentSpec.packageName, receiverPermission, options)
            val replacement = buildReplacement(node.receiverPrefix(), arguments, intentSpec.bodyStatements)

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$SEND_BROADCAST'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$SEND_BROADCAST")
                )
            )
        }

        private fun reportRegisterReceiver(node: UCallExpression) {
            if (node.methodName != REGISTER_RECEIVER) return

            val receiverExpression = node.valueArguments.firstOrNull()?.unwrapParenthesized() as? UObjectLiteralExpression ?: return
            if (!receiverExpression.asSourceString().contains("BroadcastReceiver")) return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Anonymous `BroadcastReceiver` bodies can be simplified with `$REGISTER_RECEIVER(...)`."
            )
        }

        private fun resolveIntentSpec(expression: UExpression?): IntentSpec? {
            val source = expression.resolveInitializerSource()?.asSourceString() ?: return null
            return parseIntentSource(source)
        }

        private fun parseIntentSource(source: String): IntentSpec? {
            val normalized = source.trim()

            applyIntentRegex.matchEntire(normalized)?.let {
                val base = parseIntentSource(it.groupValues[1].trim()) ?: return null
                val (packageName, bodyStatements) = parseLambdaStatements(it.groupValues[2])

                return base.copy(
                    packageName = base.packageName ?: packageName,
                    bodyStatements = base.bodyStatements + bodyStatements
                )
            }

            setPackageIntentRegex.matchEntire(normalized)?.let {
                val base = parseIntentSource(it.groupValues[1].trim()) ?: return null
                val packageName = it.groupValues[2].trim()
                return base.copy(packageName = base.packageName ?: packageName)
            }

            val constructorMatch = intentConstructorRegex.matchEntire(normalized) ?: return null
            val constructorArguments = constructorMatch.groupValues[1].trim()
            if (constructorArguments.isEmpty()) return IntentSpec()
            if (constructorArguments.contains(',')) return null

            return IntentSpec(action = constructorArguments)
        }

        private fun parseLambdaStatements(source: String): Pair<String?, List<String>> {
            val bodySource = source.trim()
            if (bodySource.isBlank()) return null to emptyList()

            var packageName: String? = null
            val statements = mutableListOf<String>()

            bodySource.lines()
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .forEach { line ->
                    val statement = line.removeSuffix(";")
                    val packageMatch = setPackageRegex.matchEntire(statement)

                    if (packageName == null && packageMatch != null)
                        packageName = packageMatch.groupValues[1].trim()
                    else statements += statement
                }
            return packageName to statements
        }

        private fun buildExtensionArguments(packageName: String?, receiverPermission: String?, options: String?): String {
            val arguments = mutableListOf<String>()

            if (packageName != null) {
                arguments += packageName
                when {
                    receiverPermission != null && options != null -> {
                        arguments += receiverPermission
                        arguments += options
                    }
                    receiverPermission != null -> arguments += receiverPermission
                    options != null -> arguments += "options = $options"
                }
            } else {
                if (receiverPermission != null) arguments += "receiverPermission = $receiverPermission"
                if (options != null) arguments += "options = $options"
            }

            return arguments.joinToString(", ")
        }

        private fun buildReplacement(receiverPrefix: String, arguments: String, bodyStatements: List<String>): String {
            val header = buildString {
                append(receiverPrefix)
                append(SEND_BROADCAST)
                append('(')
                append(arguments)
                append(')')
            }
            if (bodyStatements.isEmpty()) return header

            return buildString {
                append(header)
                append(" {\n")
                bodyStatements.forEach { append("    ").append(it).append('\n') }
                append('}')
            }
        }

        private fun UExpression?.resolveInitializerSource(): UElement? {
            val target = unwrapParenthesized()
            if (target is org.jetbrains.uast.UQualifiedReferenceExpression) return target

            val directCall = target.asCall()
            if (directCall != null) return directCall

            val reference = target as? USimpleNameReferenceExpression ?: return null
            val localVariable = when (val resolved = reference.resolve()) {
                is ULocalVariable -> resolved
                is PsiLocalVariable -> resolved.toUElementOfType<ULocalVariable>()
                else -> null
            } ?: return null

            return localVariable.uastInitializer.unwrapParenthesized()
        }

        private fun UExpression?.takeUnlessNullLiteral() =
            this?.takeUnless { it.asSourceString() == "null" }
    }

    private data class IntentSpec(
        val action: String? = null,
        val packageName: String? = null,
        val bodyStatements: List<String> = action?.let { listOf("action = $it") } ?: emptyList()
    )
}
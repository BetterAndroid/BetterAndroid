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
import com.highcapable.betterandroid.system.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.system.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiLocalVariable
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UReturnExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.toUElementOfType

class BroadcastUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val SEND_BROADCAST = "sendBroadcast"
        private const val REGISTER_RECEIVER = "registerReceiver"
        private const val APPLY_METHOD = "apply"
        private const val SET_PACKAGE_METHOD = "setPackage"
        private const val INTENT_CLASS = "android.content.Intent"
        private const val BROADCAST_RECEIVER_CLASS = "android.content.BroadcastReceiver"

        private const val ACTION_PROPERTY = "action"
        private const val OPTIONS_ARGUMENT = "options"
        private const val RECEIVER_PERMISSION_ARGUMENT = "receiverPermission"
        private const val NULL_LITERAL = "null"

        val ISSUE = Issue.create(
            id = "ReplaceWithBroadcastExtension",
            briefDescription = "Use system-extension's broadcast extensions instead.",
            explanation = """
                Using `sendBroadcast(Intent(...))` with a clearly constructed `Intent`, or manually \
                creating an anonymous `BroadcastReceiver` for `registerReceiver(...)`, can be \
                simplified by using the broadcast extensions from BetterAndroid system-extension \
                library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/system-extension#broadcast-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/system-extension#broadcast-extension

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

            // This is the `context.sendBroadcast(Intent(...))` pattern.
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
            val receiverType = receiverExpression.getExpressionType()

            // Validation is BroadcastReceiver class.
            if (!receiverType.extendsClass(context, BROADCAST_RECEIVER_CLASS)) return

            // This is the `registerReceiver(object : BroadcastReceiver() { ... }, ...)` pattern.
            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Anonymous `BroadcastReceiver` bodies can be simplified with `$REGISTER_RECEIVER(...)`."
            )
        }

        private fun resolveIntentSpec(expression: UExpression?) = expression.resolveInitializerExpression()?.let(::resolveIntentSpec)

        private fun resolveIntentSpec(element: UElement?) = when (val target = element.unwrapParenthesized()) {
            is UQualifiedReferenceExpression -> resolveQualifiedIntentSpec(target)
            else -> resolveIntentCall(target.asCall())
        }

        private fun resolveQualifiedIntentSpec(node: UQualifiedReferenceExpression): IntentSpec? {
            val selectorCall = node.selector.asCall() ?: return resolveIntentCall(node.asCall())

            return when {
                selectorCall.methodName == APPLY_METHOD -> {
                    val base = resolveIntentSpec(node.receiver) ?: return null
                    val (packageName, bodyStatements) = parseApplyStatements(selectorCall.valueArguments.firstOrNull())

                    base.copy(
                        packageName = base.packageName ?: packageName,
                        bodyStatements = base.bodyStatements + bodyStatements
                    )
                }
                isIntentSetPackageCall(selectorCall) -> {
                    val base = resolveIntentSpec(node.receiver) ?: return null
                    val packageName = selectorCall.valueArguments.firstOrNull()?.asSourceString()?.trim() ?: return null
                    base.copy(packageName = base.packageName ?: packageName)
                }
                else -> resolveIntentCall(selectorCall)
            }
        }

        private fun resolveIntentCall(node: UCallExpression?): IntentSpec? {
            val call = node ?: return null

            return when {
                isIntentConstructorCall(call) -> resolveIntentConstructorSpec(call)
                isIntentSetPackageCall(call) -> {
                    val base = resolveIntentSpec(call.receiver) ?: return null
                    val packageName = call.valueArguments.firstOrNull()?.asSourceString()?.trim() ?: return null
                    base.copy(packageName = base.packageName ?: packageName)
                }
                else -> null
            }
        }

        private fun resolveIntentConstructorSpec(node: UCallExpression): IntentSpec? {
            if (node.valueArguments.isEmpty()) return IntentSpec()
            if (node.valueArguments.size != 1) return null

            return IntentSpec(action = node.valueArguments.firstOrNull()?.asSourceString()?.trim() ?: return null)
        }

        private fun parseApplyStatements(argument: UExpression?): Pair<String?, List<String>> {
            val body = (argument.unwrapParenthesized() as? ULambdaExpression)?.body ?: return null to emptyList()
            val expressions = when (body) {
                is UBlockExpression -> body.expressions
                else -> listOf(body)
            }

            var packageName: String? = null
            val statements = mutableListOf<String>()

            expressions.mapNotNull { expression ->
                val content = expression.unwrapReturnedExpression()
                val source = content.asSourceString().trim().removeSuffix(";")
                source.takeIf { it.isNotBlank() }?.let { content to it }
            }.forEach { (content, source) ->
                val call = content.asCall()
                if (packageName == null && call != null && isIntentSetPackageCall(call))
                    packageName = call.valueArguments.firstOrNull()?.asSourceString()?.trim()
                else statements += source
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
                    options != null -> arguments += "$OPTIONS_ARGUMENT = $options"
                }
            } else {
                if (receiverPermission != null) arguments += "$RECEIVER_PERMISSION_ARGUMENT = $receiverPermission"
                if (options != null) arguments += "$OPTIONS_ARGUMENT = $options"
            }

            return arguments.joinToString(", ")
        }

        private fun buildReplacement(receiverPrefix: String, arguments: String, bodyStatements: List<String>): String {
            val header = buildString {
                append(receiverPrefix)
                append(SEND_BROADCAST)

                if (arguments.isNotEmpty() || bodyStatements.isEmpty()) {
                    append('(')
                    append(arguments)
                    append(')')
                }
            }
            if (bodyStatements.isEmpty()) return header

            return buildString {
                append(header)
                append(" {\n")
                bodyStatements.forEach { append("    ").append(it).append('\n') }
                append('}')
            }
        }

        private fun isIntentConstructorCall(node: UCallExpression): Boolean {
            val method = node.resolve() ?: return false

            // Validation is Intent constructor.
            return method.isConstructor && method.containingClass?.qualifiedName == INTENT_CLASS
        }

        private fun isIntentSetPackageCall(node: UCallExpression): Boolean {
            if (node.methodName != SET_PACKAGE_METHOD) return false
            val method = node.resolve() ?: return false

            return context.evaluator.isMemberInClass(method, INTENT_CLASS)
        }

        private fun UExpression?.resolveInitializerExpression(): UElement? {
            val target = unwrapParenthesized()
            if (target is UQualifiedReferenceExpression) return target

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

        private fun UExpression.unwrapReturnedExpression(): UExpression = when (this) {
            is UReturnExpression -> returnExpression?.unwrapParenthesized() as? UExpression ?: this
            else -> this
        }

        private fun UExpression?.takeUnlessNullLiteral() =
            this?.takeUnless { it.asSourceString() == NULL_LITERAL }
    }

    private data class IntentSpec(
        val action: String? = null,
        val packageName: String? = null,
        val bodyStatements: List<String> = action?.let { listOf("$ACTION_PROPERTY = $it") } ?: emptyList()
    )
}
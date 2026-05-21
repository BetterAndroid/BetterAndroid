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
import com.highcapable.betterandroid.system.extension.lint.detector.extension.displayShortName
import com.highcapable.betterandroid.system.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.resolveStaticClassLiteralType
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapParenthesized
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapReturnedExpression
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UQualifiedReferenceExpression

class ServiceUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val CONTEXT_CLASS = "android.content.Context"
        private const val INTENT_CLASS = "android.content.Intent"
        private const val INTENT_UTILS_CLASS = "${DeclaredSymbol.COMPONENT_PACKAGE}.IntentUtils"

        private const val START_SERVICE_METHOD = "startService"
        private const val START_FOREGROUND_SERVICE_METHOD = "startForegroundService"
        private const val APPLY_METHOD = "apply"
        private const val ALSO_METHOD = "also"
        private const val INTENT_HELPER = "Intent"

        val ISSUE = Issue.create(
            id = "ReplaceWithServiceExtension",
            briefDescription = "Use system-extension's service extensions instead.",
            explanation = """
                Using `startService(Intent(...))` or `startForegroundService(Intent(...))` with an \
                explicit target service class can be simplified by using service extensions from \
                BetterAndroid system-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/system-extension#service-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/system-extension#service-extension

                The `Service.kt` provides:
                - Generic service start helpers
                - Matching foreground service start helpers
                - Less explicit `Intent(context, Service::class.java)` boilerplate
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                context.startService(Intent(context, DemoService::class.java))
                context.startForegroundService(Intent(context, DemoService::class.java))

                // After
                context.startService<DemoService>()
                context.startForegroundService<DemoService>()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ServiceUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            val methodName = node.methodName ?: return
            if (methodName != START_SERVICE_METHOD && methodName != START_FOREGROUND_SERVICE_METHOD) return

            val method = node.resolve() ?: return
            val containingClass = method.containingClass ?: return
            if (!context.evaluator.extendsClass(containingClass, CONTEXT_CLASS, false)) return

            val intentSpec = resolveIntentSpec(node.valueArguments.firstOrNull()) ?: return
            val receiverPrefix = node.receiverPrefix()
            val replacement = buildReplacement(receiverPrefix, methodName, intentSpec)

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${replacement.displayReplacement}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$methodName'",
                    replacement = replacement.replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$methodName")
                )
            )
        }

        private fun resolveIntentSpec(expression: UExpression?) = when (val target = expression.unwrapParenthesized()) {
            is UQualifiedReferenceExpression -> resolveQualifiedIntentSpec(target)
            else -> resolveIntentCall(target.asCall())
        }

        private fun resolveQualifiedIntentSpec(node: UQualifiedReferenceExpression): IntentSpec? {
            val selectorCall = node.selector.asCall() ?: return resolveIntentCall(node.asCall())

            return when (selectorCall.methodName) {
                APPLY_METHOD, ALSO_METHOD -> {
                    val base = resolveIntentSpec(node.receiver) ?: return null
                    val bodyStatements = parseIntentBuilderStatements(
                        selectorCall.valueArguments.firstOrNull(),
                        selectorCall.methodName == ALSO_METHOD
                    )
                    base.copy(bodyStatements = base.bodyStatements + bodyStatements)
                }
                else -> resolveIntentCall(selectorCall)
            }
        }

        private fun resolveIntentCall(node: UCallExpression?): IntentSpec? {
            val call = node ?: return null
            val method = call.resolve() ?: return null

            return when {
                method.isConstructor && method.containingClass?.qualifiedName == INTENT_CLASS -> {
                    val targetClass = resolveTargetServiceClass(call) ?: return null
                    IntentSpec(targetClass)
                }
                call.methodName == INTENT_HELPER &&
                    method.containingClass?.qualifiedName == INTENT_UTILS_CLASS &&
                    call.returnType?.canonicalText == INTENT_CLASS -> {
                    val targetClass = call.typeArguments.firstOrNull()?.canonicalText ?: return null
                    IntentSpec(targetClass)
                }
                else -> null
            }
        }

        private fun buildReplacement(receiverPrefix: String, methodName: String, intentSpec: IntentSpec): ReplacementSpec {
            val displayTargetClass = intentSpec.targetClass.displayShortName()
            if (intentSpec.bodyStatements.isEmpty()) {
                val replacement = "$receiverPrefix$methodName<${intentSpec.targetClass}>()"
                val displayReplacement = "$receiverPrefix$methodName<$displayTargetClass>()"
                return ReplacementSpec(replacement, displayReplacement)
            }

            return ReplacementSpec(
                replacement = buildString {
                    append("$receiverPrefix$methodName<${intentSpec.targetClass}> {\n")
                    intentSpec.bodyStatements.forEach { append("    ").append(it).append('\n') }
                    append('}')
                },
                displayReplacement = buildString {
                    append("$receiverPrefix$methodName<$displayTargetClass> {\n")
                    intentSpec.bodyStatements.forEach { append("    ").append(it).append('\n') }
                    append('}')
                }
            )
        }

        private fun resolveTargetServiceClass(intentCall: UCallExpression) =
            intentCall.valueArguments.getOrNull(1).resolveStaticClassLiteralType()

        private fun parseIntentBuilderStatements(argument: UExpression?, isAlso: Boolean): List<String> =
            argument.resolveIntentBuilderStatements(isAlso)

        private fun UExpression?.resolveIntentBuilderStatements(isAlso: Boolean): List<String> {
            val lambda = unwrapParenthesized() as? ULambdaExpression ?: return emptyList()
            val expressions = when (val body = lambda.body) {
                is UBlockExpression -> body.expressions
                else -> listOf(body)
            }
            val parameterName = lambda.valueParameters.firstOrNull()?.name ?: if (isAlso) "it" else null

            return expressions.mapNotNull { expression ->
                val source = expression.unwrapReturnedExpression().asSourceString().trim().removeSuffix(";")
                    .removeIntentLambdaPrefix(parameterName)
                source.takeIf { it.isNotBlank() }
            }
        }

        private fun String.removeIntentLambdaPrefix(parameterName: String?) =
            parameterName?.takeIf { startsWith("$it.") }?.let { removePrefix("$it.") } ?: this
    }

    private data class IntentSpec(
        val targetClass: String,
        val bodyStatements: List<String> = emptyList()
    )

    private data class ReplacementSpec(
        val replacement: String,
        val displayReplacement: String
    )
}
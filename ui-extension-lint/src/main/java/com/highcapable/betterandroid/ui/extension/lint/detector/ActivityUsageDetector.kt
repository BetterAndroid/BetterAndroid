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
 * This file is created by fankes on 2026/5/12.
 */
package com.highcapable.betterandroid.ui.extension.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.ui.extension.lint.DeclaredSymbol
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.asCall
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.displayShortName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveStaticClassLiteralType
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapReturnedExpression
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UQualifiedReferenceExpression

class ActivityUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val CONTEXT_CLASS = "android.content.Context"
        private const val FRAGMENT_CLASS = "androidx.fragment.app.Fragment"
        private const val INTENT_CLASS = "android.content.Intent"
        private const val INTENT_UTILS_CLASS = "IntentUtils"

        private const val START_ACTIVITY_METHOD = "startActivity"
        private const val APPLY_METHOD = "apply"
        private const val ALSO_METHOD = "also"
        private const val INTENT_HELPER = "Intent"
        private const val DEFAULT_ALSO_PARAMETER = "it"
        private const val STATEMENT_SEPARATOR = ";"

        val ISSUE = Issue.create(
            id = "ReplaceWithActivityExtension",
            briefDescription = "Use ui-extension's `startActivity<T>(...)` instead.",
            explanation = """
                Using `startActivity(Intent(...))` with an explicit target activity class can be \
                simplified by using `startActivity<T>(...)` from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#activity-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#activity-extension

                The `Activity.kt` provides:
                - A shorter generic activity launch API
                - Consistent usage on both `Context` and `Fragment`
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                context.startActivity(Intent(context, DemoActivity::class.java))
                fragment.startActivity(Intent(context, DemoActivity::class.java))

                // After
                context.startActivity<DemoActivity>()
                fragment.startActivity<DemoActivity>()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ActivityUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != START_ACTIVITY_METHOD) return

            // Validation is Context or Fragment `startActivity(...)`.
            val method = node.resolve() ?: return
            val containingClass = method.containingClass
            val isContext = containingClass?.let { context.evaluator.extendsClass(it, CONTEXT_CLASS, false) } == true
            val isFragment = containingClass?.let { context.evaluator.extendsClass(it, FRAGMENT_CLASS, false) } == true
            if (!isContext && !isFragment) return

            // This is the `startActivity(Intent(context, TargetActivity::class.java))` pattern.
            val intentSpec = resolveIntentSpec(node.valueArguments.firstOrNull()) ?: return
            val receiverPrefix = node.receiverPrefix()
            val replacement = buildReplacement(receiverPrefix, intentSpec)

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${replacement.displayReplacement}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$START_ACTIVITY_METHOD'",
                    replacement = replacement.replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$START_ACTIVITY_METHOD")
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
                    val targetClass = resolveTargetActivityClass(call) ?: return null
                    IntentSpec(targetClass)
                }
                call.methodName == INTENT_HELPER &&
                    method.containingClass?.name == INTENT_UTILS_CLASS &&
                    call.returnType?.canonicalText == INTENT_CLASS -> {
                    val targetClass = call.typeArguments.firstOrNull()?.canonicalText ?: return null
                    IntentSpec(targetClass)
                }
                else -> null
            }
        }

        private fun buildReplacement(receiverPrefix: String, intentSpec: IntentSpec): ReplacementSpec {
            val displayTargetClass = intentSpec.targetClass.displayShortName()
            if (intentSpec.bodyStatements.isEmpty()) {
                val replacement = "$receiverPrefix$START_ACTIVITY_METHOD<${intentSpec.targetClass}>()"
                val displayReplacement = "$receiverPrefix$START_ACTIVITY_METHOD<$displayTargetClass>()"
                return ReplacementSpec(replacement, displayReplacement)
            }

            return ReplacementSpec(
                replacement = buildString {
                    append("$receiverPrefix$START_ACTIVITY_METHOD<${intentSpec.targetClass}> {\n")
                    intentSpec.bodyStatements.forEach { append("    ").append(it).append('\n') }
                    append('}')
                },
                displayReplacement = buildString {
                    append("$receiverPrefix$START_ACTIVITY_METHOD<$displayTargetClass> {\n")
                    intentSpec.bodyStatements.forEach { append("    ").append(it).append('\n') }
                    append('}')
                }
            )
        }

        private fun resolveTargetActivityClass(intentCall: UCallExpression) =
            intentCall.valueArguments.getOrNull(1).resolveStaticClassLiteralType()

        private fun parseIntentBuilderStatements(argument: UExpression?, isAlso: Boolean): List<String> =
            argument.resolveIntentBuilderStatements(isAlso)

        private fun UExpression?.resolveIntentBuilderStatements(isAlso: Boolean): List<String> {
            val lambda = unwrapParenthesized() as? ULambdaExpression ?: return emptyList()
            val expressions = when (val body = lambda.body) {
                is UBlockExpression -> body.expressions
                else -> listOf(body)
            }
            val parameterName = lambda.valueParameters.firstOrNull()?.name ?: if (isAlso) DEFAULT_ALSO_PARAMETER else null

            return expressions.mapNotNull { expression ->
                val source = expression.unwrapReturnedExpression().asSourceString().trim().removeSuffix(STATEMENT_SEPARATOR)
                    .removeIntentLambdaPrefix(parameterName)
                source.takeIf { it.isNotBlank() }
            }
        }

        private fun String.removeIntentLambdaPrefix(parameterName: String?) =
            parameterName?.takeIf { startsWith("$it.") }?.let { removePrefix("$it.") } ?: this
    })

    private data class IntentSpec(
        val targetClass: String,
        val bodyStatements: List<String> = emptyList()
    )

    private data class ReplacementSpec(
        val replacement: String,
        val displayReplacement: String
    )
}
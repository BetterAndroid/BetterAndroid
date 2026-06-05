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
 * This file is created by fankes on 2026/5/27.
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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.getContainingPsiClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.UThisExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind

class ContextUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val ACTIVITY_CLASS = "android.app.Activity"
        private const val CONTEXT_CLASS = "android.content.Context"
        private const val CONTEXT_WRAPPER_CLASS = "android.content.ContextWrapper"
        private const val CONTEXT_THEME_WRAPPER_CLASS = "android.view.ContextThemeWrapper"

        private const val HOST_ACTIVITY_PROPERTY = "hostActivity"
        private const val REQUIRE_HOST_ACTIVITY_FUNCTION = "requireHostActivity"

        private const val AS_OPERATOR = "as"
        private const val SAFE_AS_OPERATOR = "as?"
        private const val NULLABLE_SUFFIX = "?"
        private const val PLATFORM_SUFFIX = "!"

        val ISSUE = Issue.create(
            id = "ReplaceWithContextExtension",
            briefDescription = "Use ui-extension's context host activity extensions instead.",
            explanation = """
                Using `context as Activity`, `context as? Activity`, `context as YourActivity` or \
                `context as? YourActivity` on `Context`, `ContextWrapper` or `ContextThemeWrapper` \
                can be simplified by using context host activity extensions from BetterAndroid \
                ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#context-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#context-extension

                The `Context.kt` provides:
                - A direct `hostActivity` property for nullable access
                - Matching `requireHostActivity()` style APIs
                - Generic host activity lookup helpers
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                context as Activity
                context as? Activity
                context as YourActivity
                context as? YourActivity

                // After
                context.requireHostActivity()
                context.hostActivity
                context.requireHostActivity<YourActivity>()
                context.hostActivity<YourActivity>()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ContextUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UBinaryExpressionWithType::class.java as Class<out UElement>)

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return

            val operandNode = node.operand.unwrapParenthesized() as? UExpression ?: return
            if (!operandNode.isExplicitContextTarget()) return

            val isNullableCast = node.operationKind.name == SAFE_AS_OPERATOR
            if (!isNullableCast && node.operationKind.name != AS_OPERATOR) return

            val replacement = createActivityReplacement(node, operandNode.asSourceString(), isNullableCast) ?: return
            val fixName = if (isNullableCast) HOST_ACTIVITY_PROPERTY else REQUIRE_HOST_ACTIVITY_FUNCTION

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${replacement.first}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = replacement.first,
                    imports = replacement.second.toTypedArray()
                )
            )
        }

        private fun createActivityReplacement(
            node: UBinaryExpressionWithType,
            operand: String,
            isNullableCast: Boolean
        ): Pair<String, List<String>>? {
            val castType = node.type
            if (!castType.extendsClass(context, ACTIVITY_CLASS)) return null

            val targetType = node.typeReference?.type?.canonicalText
                ?.removeSuffix(NULLABLE_SUFFIX)
                ?.removeSuffix(PLATFORM_SUFFIX)
            val importName = "${DeclaredSymbol.COMPONENT_PACKAGE}.${if (isNullableCast) HOST_ACTIVITY_PROPERTY else REQUIRE_HOST_ACTIVITY_FUNCTION}"

            return when {
                targetType == null || targetType == ACTIVITY_CLASS -> {
                    val replacement = if (isNullableCast) "$operand.$HOST_ACTIVITY_PROPERTY" else "$operand.$REQUIRE_HOST_ACTIVITY_FUNCTION()"
                    replacement to listOf(importName)
                }
                else -> {
                    val functionName = if (isNullableCast) HOST_ACTIVITY_PROPERTY else REQUIRE_HOST_ACTIVITY_FUNCTION
                    "$operand.$functionName<${node.typeReference?.asSourceString().orEmpty().trim().removeSuffix(NULLABLE_SUFFIX)}>()" to
                        listOf(importName)
                }
            }
        }

        private fun UExpression.isExplicitContextTarget(): Boolean {
            if (this is UThisExpression) {
                val ownerClass = getContainingPsiClass() ?: return false
                return context.evaluator.extendsClass(ownerClass, CONTEXT_CLASS, false) ||
                    context.evaluator.extendsClass(ownerClass, CONTEXT_WRAPPER_CLASS, false) ||
                    context.evaluator.extendsClass(ownerClass, CONTEXT_THEME_WRAPPER_CLASS, false)
            }

            val resolvedVariableType = ((this as? UResolvable)?.resolve() as? PsiVariable)?.type
                ?.canonicalText
                ?.removeSuffix(NULLABLE_SUFFIX)
                ?.removeSuffix(PLATFORM_SUFFIX)
            if (resolvedVariableType == CONTEXT_CLASS ||
                resolvedVariableType == CONTEXT_WRAPPER_CLASS ||
                resolvedVariableType == CONTEXT_THEME_WRAPPER_CLASS
            ) return true

            val expressionType = getExpressionType() as? PsiClassType ?: return false
            val canonicalText = expressionType.canonicalText.removeSuffix(NULLABLE_SUFFIX).removeSuffix(PLATFORM_SUFFIX)
            return canonicalText == CONTEXT_CLASS ||
                canonicalText == CONTEXT_WRAPPER_CLASS ||
                canonicalText == CONTEXT_THEME_WRAPPER_CLASS
        }
    })
}
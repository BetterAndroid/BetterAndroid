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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiVariable
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.UThisExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind

class LifecycleOwnerUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val ACTIVITY_CLASS = "android.app.Activity"
        private const val CONTEXT_CLASS = "android.content.Context"
        private const val LIFECYCLE_PACKAGE = "androidx.lifecycle"
        private const val LIFECYCLE_OWNER_CLASS = "androidx.lifecycle.LifecycleOwner"
        private const val LIFECYCLE_VIEWTREE_OWNER_CLASS = "androidx.lifecycle.ViewTreeLifecycleOwner"
        private const val VIEW_CLASS = "android.view.View"
        private const val FIND_VIEW_TREE_LIFECYCLE_OWNER_METHOD = "findViewTreeLifecycleOwner"
        private const val LIFECYCLE_OWNER_PROPERTY = "lifecycleOwner"
        private const val ACTIVITY_PROPERTY = "activity"
        private const val CONTEXT_PROPERTY = "context"
        private const val REQUIRE_ACTIVITY_FUNCTION = "requireActivity"
        private const val REQUIRE_CONTEXT_FUNCTION = "requireContext"

        private val FIND_VIEW_TREE_LIFECYCLE_OWNER_IMPORT_REGEX =
            Regex("""^\s*import\s+androidx\.lifecycle\.findViewTreeLifecycleOwner(?:\s+as\s+(\w+))?\s*$""", RegexOption.MULTILINE)

        val ISSUE = Issue.create(
            id = "ReplaceWithLifecycleOwnerExtension",
            briefDescription = "Use ui-extension's lifecycle owner extensions instead.",
            explanation = """
                Using `findViewTreeLifecycleOwner()`, `owner as Activity`, `owner as? Activity`, \
                `owner as Context` or `owner as? Context` can be simplified by using lifecycle \
                owner extensions from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#lifecycleowner-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#lifecycleowner-extension

                The `LifecycleOwner.kt` provides:
                - A direct `View.lifecycleOwner` property
                - Safer `activity` and `context` access helpers
                - Matching `requireActivity()` and `requireContext()` style APIs
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                view.findViewTreeLifecycleOwner()
                owner as Activity
                owner as? Activity
                owner as Context
                owner as? Context

                // After
                view.lifecycleOwner
                owner.requireActivity()
                owner.activity
                owner.requireContext()
                owner.context
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                LifecycleOwnerUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UBinaryExpressionWithType::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (!node.isFindViewTreeLifecycleOwner(context) &&
                !node.isImportedFindViewTreeLifecycleOwnerFallback(context)
            ) return

            val replacement = node.receiver?.asSourceString()?.let { "$it.$LIFECYCLE_OWNER_PROPERTY" }
                ?: resolveImplicitViewLifecycleOwnerReplacement(node)
                ?: return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$LIFECYCLE_OWNER_PROPERTY'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$LIFECYCLE_OWNER_PROPERTY")
                )
            )
        }

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return

            val operandNode = node.operand.unwrapParenthesized() ?: return
            if (!operandNode.isLifecycleOwnerTarget()) return

            val operand = operandNode.asSourceString()
            val castType = node.type
            val isNullableCast = node.operationKind.name == "as?"
            if (!isNullableCast && node.operationKind.name != "as") return

            val replacement = when {
                castType.extendsClass(context, ACTIVITY_CLASS) -> createActivityReplacement(node, operand, isNullableCast)
                castType.extendsClass(context, CONTEXT_CLASS) -> createContextReplacement(operand, isNullableCast)
                else -> null
            } ?: return
            val fixName = when {
                replacement.first.contains(".$ACTIVITY_PROPERTY") -> ACTIVITY_PROPERTY
                replacement.first.contains(".$CONTEXT_PROPERTY") -> CONTEXT_PROPERTY
                replacement.first.contains(".$REQUIRE_ACTIVITY_FUNCTION") -> REQUIRE_ACTIVITY_FUNCTION
                else -> REQUIRE_CONTEXT_FUNCTION
            }

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
            val targetType = node.typeReference?.type?.canonicalText
            return when {
                targetType == null || targetType == ACTIVITY_CLASS -> {
                    val functionName = if (isNullableCast) ACTIVITY_PROPERTY else REQUIRE_ACTIVITY_FUNCTION
                    "$operand.$functionName${if (functionName == ACTIVITY_PROPERTY) "" else "()"}" to
                        listOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$functionName")
                }
                else -> {
                    val functionName = if (isNullableCast) ACTIVITY_PROPERTY else REQUIRE_ACTIVITY_FUNCTION
                    "$operand.$functionName<${node.typeReference?.asSourceString().orEmpty().trim().removeSuffix("?")}>()" to
                        listOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$functionName")
                }
            }
        }

        private fun resolveImplicitViewLifecycleOwnerReplacement(node: UCallExpression): String? {
            val psiClass = node.findContainingUClass()?.javaPsi ?: return null
            if (!context.evaluator.extendsClass(psiClass, VIEW_CLASS, false)) return null

            return LIFECYCLE_OWNER_PROPERTY
        }

        private fun createContextReplacement(
            operand: String,
            isNullableCast: Boolean
        ) = (if (isNullableCast) "$operand.$CONTEXT_PROPERTY" else "$operand.$REQUIRE_CONTEXT_FUNCTION()") to
            listOf("${DeclaredSymbol.COMPONENT_PACKAGE}.${if (isNullableCast) CONTEXT_PROPERTY else REQUIRE_CONTEXT_FUNCTION}")

        private fun UElement.isLifecycleOwnerTarget(): Boolean {
            val resolvedVariableType = ((this as? UResolvable?)?.resolve() as? PsiVariable?)?.type
                ?.canonicalText
                ?.removeSuffix("?")
                ?.removeSuffix("!")
            if (resolvedVariableType == LIFECYCLE_OWNER_CLASS) return true

            val expressionType = (this as? UExpression?)?.getExpressionType() as? PsiClassType ?: return false
            val canonicalText = expressionType.canonicalText.removeSuffix("?").removeSuffix("!")
            return expressionType.resolve()?.qualifiedName == LIFECYCLE_OWNER_CLASS || canonicalText == LIFECYCLE_OWNER_CLASS
        }

        private fun UCallExpression.isFindViewTreeLifecycleOwner(context: JavaContext): Boolean {
            val writtenCallName = writtenCallName() ?: return false
            if (receiver == null && hasContainingClassMethod(writtenCallName)) return false

            val resolved = resolve() ?: return false
            return resolved.name == FIND_VIEW_TREE_LIFECYCLE_OWNER_METHOD &&
                (context.evaluator.isMemberInClass(resolved, LIFECYCLE_VIEWTREE_OWNER_CLASS) ||
                    resolved.containingClass?.qualifiedName == LIFECYCLE_PACKAGE)
        }

        private fun UCallExpression.isImportedFindViewTreeLifecycleOwnerFallback(context: JavaContext): Boolean {
            val importedNames = sourcePsi?.containingFile?.text
                ?.let {
                    FIND_VIEW_TREE_LIFECYCLE_OWNER_IMPORT_REGEX.findAll(it).map { match ->
                        match.groupValues.getOrNull(1).orEmpty().ifBlank { FIND_VIEW_TREE_LIFECYCLE_OWNER_METHOD }
                    }.toSet()
                }
                .orEmpty()
            if (importedNames.isEmpty()) return false

            val callName = writtenCallName() ?: return false
            if (callName !in importedNames) return false
            if (hasContainingClassMethod(callName) && isImplicitOrThisReceiver()) return false

            val receiverType = receiver?.getExpressionType()
            if (receiverType != null) return receiverType.extendsClass(context, VIEW_CLASS)

            val uClass = findContainingUClass() ?: return false
            if (!context.evaluator.extendsClass(uClass.javaPsi, VIEW_CLASS, false)) return false
            if (hasContainingClassMethod(callName)) return false

            return true
        }

        private fun UCallExpression.findContainingUClass(): UClass? {
            var parent = uastParent
            while (parent != null) {
                if (parent is UClass) return parent
                parent = parent.uastParent
            }
            return null
        }

        private fun UCallExpression.hasContainingClassMethod(name: String) =
            findContainingUClass()?.methods?.any { it.name == name } == true

        private fun UCallExpression.isImplicitOrThisReceiver() =
            receiver == null || receiver is UThisExpression || receiver?.asSourceString() == "this"

        private fun UCallExpression.writtenCallName() = sourcePsi?.text
            ?.substringBefore('(')
            ?.trim()
            ?.substringAfterLast('.')
            ?.takeIf { it.isNotBlank() }
            ?: methodName
    }
}
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
 * This file is created by fankes on 2026/5/26.
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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.findContainingStatement
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.getContainingPsiClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesizedOrNotNull
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UBinaryExpression
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind
import org.jetbrains.uast.UastBinaryOperator

class ViewUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val VIEW_CLASS = "android.view.View"
        private const val VIEW_SIMPLE_NAME = "View"
        private const val VIEW_GROUP_CLASS = "android.view.ViewGroup"
        private const val VIEW_GROUP_SIMPLE_NAME = "ViewGroup"
        private const val VIEW_PARENT_CLASS = "android.view.ViewParent"
        private const val VIEW_PARENT_SIMPLE_NAME = "ViewParent"

        private const val GET_PARENT_METHOD = "getParent"
        private const val GET_CHILD_AT_METHOD = "getChildAt"
        private const val REMOVE_VIEW_METHOD = "removeView"
        private const val REMOVE_VIEW_IN_LAYOUT_METHOD = "removeViewInLayout"
        private const val INDEX_OF_CHILD_METHOD = "indexOfChild"

        private const val PARENT_PROPERTY = "parent"
        private const val PARENT_FUNCTION = "parent"
        private const val PARENT_OR_NULL_FUNCTION = "parentOrNull"
        private const val CHILD_FUNCTION = "child"
        private const val CHILD_OR_NULL_FUNCTION = "childOrNull"
        private const val FIRST_CHILD_PROPERTY = "firstChild"
        private const val LAST_CHILD_PROPERTY = "lastChild"
        private const val FIRST_CHILD_OR_NULL_PROPERTY = "firstChildOrNull"
        private const val LAST_CHILD_OR_NULL_PROPERTY = "lastChildOrNull"
        private const val REMOVE_SELF_FUNCTION = "removeSelf"
        private const val REMOVE_SELF_IN_LAYOUT_FUNCTION = "removeSelfInLayout"
        private const val INDEX_OF_IN_PARENT_FUNCTION = "indexOfInParent"

        private const val THIS_RECEIVER = "this"
        private const val NEGATIVE_ONE = "-1"
        private const val DOT = "."
        private const val NULLABLE_CAST_OPERATOR = "as?"
        private const val CAST_OPERATOR = "as"
        private const val SAFE_CALL_OPERATOR = "?."
        private const val NORMAL_CALL_OPERATOR = "."

        private const val VIEW_EXTENSION_PACKAGE = "${DeclaredSymbol.BASE_PACKAGE}.view"

        val ISSUE = Issue.create(
            id = "ReplaceWithViewExtension",
            briefDescription = "Use ui-extension's `View.kt` instead.",
            explanation = """
                Using raw parent-child access and self-removal APIs can be simplified by using functions from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#view-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#view-扩展

                The `View.kt` provides:
                - Safer parent and child access helpers
                - Direct self-removal helpers
                - A clearer API for querying the index in parent

                Examples:
                ```kotlin
                // Before
                view.parent as? ViewGroup
                viewGroup.getChildAt(0)
                (view.parent as? ViewGroup)?.removeView(view)
                (view.parent as? ViewGroup)?.indexOfChild(view) ?: -1

                // After
                view.parentOrNull()
                viewGroup.firstChild
                view.removeSelf()
                view.indexOfInParent()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ViewUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UBinaryExpressionWithType::class.java as Class<out UElement>,
        UCallExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        private val visitedBinaryExpressionsWithType = hashSetOf<Any>()

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            node.sourcePsi?.let { if (!visitedBinaryExpressionsWithType.add(it)) return }
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return

            reportParentCast(context, node)
            reportChildCast(context, node)
        }

        override fun visitCallExpression(node: UCallExpression) {
            reportChildAccess(context, node)
            reportRemoveSelf(context, node, REMOVE_VIEW_METHOD, REMOVE_SELF_FUNCTION)
            reportRemoveSelf(context, node, REMOVE_VIEW_IN_LAYOUT_METHOD, REMOVE_SELF_IN_LAYOUT_FUNCTION)
            reportIndexOfInParent(context, node)
        }

        private fun reportParentCast(context: JavaContext, node: UBinaryExpressionWithType) {
            val castOperator = node.operationKind.name
            val isNullableCast = castOperator == NULLABLE_CAST_OPERATOR
            if (!isNullableCast && castOperator != CAST_OPERATOR) return

            val leftSource = node.operand.unwrapParenthesized()?.asSourceString() ?: return
            val receiverPrefix = leftSource.parentReceiverPrefix() ?: return
            if (!node.operand.isViewParentAccess(context)) return

            val targetPsiType = node.typeReference?.type as? PsiClassType ?: return
            if (!targetPsiType.extendsClass(context, VIEW_PARENT_CLASS)) return
            val targetType = node.typeReference?.asSourceString()?.removeSuffix("?") ?: return
            if (targetType == VIEW_PARENT_SIMPLE_NAME) return

            val replacement = createParentLintFix(receiverPrefix, targetType, isNullableCast)
            reportAndFix(context, node, replacement)
        }

        private fun reportChildCast(context: JavaContext, node: UBinaryExpressionWithType) {
            val castOperator = node.operationKind.name
            val isNullableCast = castOperator == NULLABLE_CAST_OPERATOR
            if (!isNullableCast && castOperator != CAST_OPERATOR) return

            val call = node.operand.unwrapParenthesized().asCall() ?: return
            if (!call.isViewGroupChildAtCall(context)) return

            val targetType = node.typeReference?.asSourceString()?.removeSuffix("?") ?: return
            val receiver = call.receiverPrefix()
            val indexArg = call.valueArguments.firstOrNull() ?: return

            val replacement = createChildLintFix(receiver, indexArg, targetType, isNullableCast)
            reportAndFix(context, node, replacement)
        }

        private fun reportChildAccess(context: JavaContext, node: UCallExpression) {
            if (!node.isViewGroupChildAtCall(context)) return

            // Typed child access is handled by the surrounding cast detector.
            val parent = node.uastParent
            if (parent is UBinaryExpressionWithType && (parent.operationKind.name == CAST_OPERATOR ||
                    parent.operationKind.name == NULLABLE_CAST_OPERATOR)) return
            val qualifiedParent = parent as? UQualifiedReferenceExpression
            val outerParent = qualifiedParent?.uastParent
            if (outerParent is UBinaryExpressionWithType && (outerParent.operationKind.name == CAST_OPERATOR ||
                    outerParent.operationKind.name == NULLABLE_CAST_OPERATOR)) return

            val receiver = node.receiverPrefix()
            val indexArg = node.valueArguments.firstOrNull() ?: return

            val replacement = createPlainChildLintFix(receiver, indexArg)
            reportAndFix(context, node, replacement)
        }

        private fun reportRemoveSelf(context: JavaContext, node: UCallExpression, methodName: String, targetFunction: String) {
            val resolved = node.resolve() ?: return
            if (resolved.name != methodName || !context.evaluator.isMemberInClass(resolved, VIEW_GROUP_CLASS)) return

            // Match both raw parent access and BetterAndroid's parent accessors as the intermediate step.
            val safeReceiver = node.uastParent as? UQualifiedReferenceExpression ?: return
            val viewReceiver = node.valueArguments.singleOrNull()?.asSourceString() ?: return
            if (!safeReceiver.receiver.isParentAccessOf(context, viewReceiver)) return
            if (!safeReceiver.isSafeOrNormalCall(methodName)) return

            val replacement = createReceiverReplacement(viewReceiver, targetFunction)
            reportAndFix(context, safeReceiver, replacement)
        }

        private fun reportIndexOfInParent(context: JavaContext, node: UCallExpression) {
            val resolved = node.resolve() ?: return
            if (resolved.name != INDEX_OF_CHILD_METHOD || !context.evaluator.isMemberInClass(resolved, VIEW_GROUP_CLASS)) return

            val statementNode = node.findContainingStatement() ?: return
            // Keep the match narrow to the direct parent -> indexOfChild handoff we can replace safely.
            val statement = statementNode.asSourceString()
            val viewReceiver = node.valueArguments.singleOrNull()?.asSourceString() ?: return
            val isFallbackPattern = statement.contains("$SAFE_CALL_OPERATOR$INDEX_OF_CHILD_METHOD(") && statement.endsWith("?: $NEGATIVE_ONE")
            val isDirectPattern = statement.endsWith("$NORMAL_CALL_OPERATOR$INDEX_OF_CHILD_METHOD($viewReceiver)")
            if (!isFallbackPattern && !isDirectPattern) return
            if (!node.receiver.isParentAccessOf(context, expectedReceiver = viewReceiver)) return

            val replacement = createReceiverReplacement(viewReceiver, INDEX_OF_IN_PARENT_FUNCTION)
            reportAndFix(context, statementNode, replacement)
        }

        private fun UElement?.isViewParentAccess(context: JavaContext): Boolean = when (val target = unwrapParenthesized()) {
            is USimpleNameReferenceExpression -> {
                val resolved = target.resolve() as? PsiMethod ?: return false
                resolved.name == GET_PARENT_METHOD && context.evaluator.isMemberInClass(resolved, VIEW_CLASS)
            }
            is UQualifiedReferenceExpression -> {
                val resolved = (target.selector as? UResolvable)?.resolve() as? PsiMethod ?: return false
                resolved.name == GET_PARENT_METHOD && context.evaluator.isMemberInClass(resolved, VIEW_CLASS)
            }
            else -> false
        }

        private fun UCallExpression.isViewGroupChildAtCall(context: JavaContext): Boolean {
            val resolved = resolve() ?: return false
            if (resolved.name != GET_CHILD_AT_METHOD || !context.evaluator.isMemberInClass(resolved, VIEW_GROUP_CLASS))
                return false
            return receiver?.getExpressionType().extendsClass(context, VIEW_GROUP_CLASS) || 
                getContainingPsiClass()?.let { context.evaluator.extendsClass(it, VIEW_GROUP_CLASS, false) } == true
        }

        private fun createParentLintFix(receiverPrefix: String, targetType: String, isNullableCast: Boolean): Pair<String, String> {
            val functionName = if (isNullableCast) PARENT_OR_NULL_FUNCTION else PARENT_FUNCTION
            val useNonGeneric = targetType == VIEW_GROUP_SIMPLE_NAME
            val replacement = when {
                useNonGeneric -> "$receiverPrefix$functionName()"
                else -> "$receiverPrefix$functionName<$targetType>()"
            }
            return replacement to functionName
        }

        private fun createChildLintFix(receiver: String, indexArg: UExpression, targetType: String, isNullableCast: Boolean): Pair<String, String> {
            // Prefer non-generic overloads when the target is plain `View`, and keep generics for concrete subclasses.
            val useNonGeneric = targetType == VIEW_SIMPLE_NAME
            val indexSource = indexArg.asSourceString()
            return when {
                indexArg.isZeroLiteral() && isNullableCast && useNonGeneric -> createPropertyLintFix(receiver, FIRST_CHILD_OR_NULL_PROPERTY)
                indexArg.isZeroLiteral() && !isNullableCast && useNonGeneric -> createPropertyLintFix(receiver, FIRST_CHILD_PROPERTY)
                indexArg.isZeroLiteral() && isNullableCast -> createGenericPropertyLintFix(receiver, FIRST_CHILD_OR_NULL_PROPERTY, targetType)
                indexArg.isZeroLiteral() -> createGenericPropertyLintFix(receiver, FIRST_CHILD_PROPERTY, targetType)
                indexArg.isLastChildIndex() && isNullableCast && useNonGeneric -> createPropertyLintFix(receiver, LAST_CHILD_OR_NULL_PROPERTY)
                indexArg.isLastChildIndex() && !isNullableCast && useNonGeneric -> createPropertyLintFix(receiver, LAST_CHILD_PROPERTY)
                indexArg.isLastChildIndex() && isNullableCast -> createGenericPropertyLintFix(receiver, LAST_CHILD_OR_NULL_PROPERTY, targetType)
                indexArg.isLastChildIndex() -> createGenericPropertyLintFix(receiver, LAST_CHILD_PROPERTY, targetType)
                isNullableCast && useNonGeneric -> createFunctionLintFix(receiver, CHILD_OR_NULL_FUNCTION, "($indexSource)")
                !isNullableCast && useNonGeneric -> createFunctionLintFix(receiver, CHILD_FUNCTION, "($indexSource)")
                isNullableCast -> createFunctionLintFix(receiver, CHILD_OR_NULL_FUNCTION, "<$targetType>($indexSource)")
                else -> createFunctionLintFix(receiver, CHILD_FUNCTION, "<$targetType>($indexSource)")
            }
        }

        private fun createPlainChildLintFix(receiver: String, indexArg: UExpression): Pair<String, String> {
            val indexSource = indexArg.asSourceString()
            return when {
                indexArg.isZeroLiteral() -> createPropertyLintFix(receiver, FIRST_CHILD_PROPERTY)
                indexArg.isLastChildIndex() -> createPropertyLintFix(receiver, LAST_CHILD_PROPERTY)
                else -> createFunctionLintFix(receiver, CHILD_FUNCTION, "($indexSource)")
            }
        }

        private fun createPropertyLintFix(receiver: String, propertyName: String) =
            "$receiver$propertyName" to propertyName

        private fun createGenericPropertyLintFix(receiver: String, propertyName: String, targetType: String) =
            "$receiver$propertyName<$targetType>()" to propertyName

        private fun createFunctionLintFix(receiver: String, functionName: String, arguments: String = "()") =
            "$receiver$functionName$arguments" to functionName

        private fun createReceiverReplacement(receiver: String, functionName: String) =
            createFunctionLintFix(receiver.functionReceiverPrefix(), functionName)

        private fun String.parentReceiverPrefix() = when {
            this == PARENT_PROPERTY -> ""
            this == "$THIS_RECEIVER.$PARENT_PROPERTY" -> "$THIS_RECEIVER$DOT"
            endsWith("$DOT$PARENT_PROPERTY") -> "${removeSuffix("$DOT$PARENT_PROPERTY")}$DOT"
            else -> null
        }

        private fun UElement?.matchesParentAccessOf(receiver: String): Boolean {
            val source = unwrapParenthesizedOrNotNull()?.asSourceString() ?: return false
            return source == PARENT_PROPERTY && receiver == THIS_RECEIVER ||
                source == "$receiver$DOT$PARENT_PROPERTY"
        }

        private fun UElement?.isParentAccessOf(context: JavaContext, expectedReceiver: String): Boolean {
            val target = unwrapParenthesizedOrNotNull() ?: return false
            val parentCast = target as? UBinaryExpressionWithType
            if (parentCast != null) {
                return (parentCast.operationKind.name == NULLABLE_CAST_OPERATOR || parentCast.operationKind.name == CAST_OPERATOR) &&
                    parentCast.operand.matchesParentAccessOf(expectedReceiver) &&
                    parentCast.operand.isViewParentAccess(context)
            }

            val parentCall = (target as? UExpression)?.asCall() ?: return false
            if (parentCall.methodName != PARENT_OR_NULL_FUNCTION && parentCall.methodName != PARENT_FUNCTION) return false
            val receiverPrefix = parentCall.receiverPrefix()
            val receiverSource = receiverPrefix.removeSuffix(DOT)
            if (receiverSource.isEmpty()) return expectedReceiver == THIS_RECEIVER

            return receiverSource == expectedReceiver
        }

        private fun UQualifiedReferenceExpression.isSafeOrNormalCall(methodName: String): Boolean {
            val source = asSourceString()
            return source.contains("$SAFE_CALL_OPERATOR$methodName(") ||
                source.contains("$NORMAL_CALL_OPERATOR$methodName(")
        }

        private fun String.functionReceiverPrefix() = if (this == THIS_RECEIVER) "" else "$this$DOT"

        private fun UExpression.isZeroLiteral() = (unwrapParenthesizedOrNotNull() as? ULiteralExpression)?.value == 0

        private fun UExpression.isLastChildIndex(): Boolean {
            val target = unwrapParenthesizedOrNotNull() as? UBinaryExpression ?: return false
            if (target.operator != UastBinaryOperator.MINUS) return false
            if ((target.rightOperand.unwrapParenthesizedOrNotNull() as? ULiteralExpression)?.value != 1) return false

            return when (val left = target.leftOperand.unwrapParenthesizedOrNotNull()) {
                is USimpleNameReferenceExpression -> left.identifier == "childCount"
                is UQualifiedReferenceExpression -> {
                    val selector = left.selector as? USimpleNameReferenceExpression ?: return false
                    selector.identifier == "childCount"
                }
                else -> false
            }
        }

        private fun reportAndFix(context: JavaContext, node: UElement, replacement: Pair<String, String>) =
            reportAndFix(context, node, replacement.first, replacement.second)

        private fun reportAndFix(context: JavaContext, node: UElement, replacement: String, fixName: String) =
            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = replacement,
                    imports = arrayOf("$VIEW_EXTENSION_PACKAGE.$fixName")
                )
            )
    })
}
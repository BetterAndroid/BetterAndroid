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
 * This file is created by fankes on 2026/5/15.
 */
package com.highcapable.betterandroid.ui.component.adapter.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.ui.component.adapter.lint.DeclaredSymbol
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.resolveLocalVariableInitializer
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiClass
import org.jetbrains.uast.UBinaryExpression
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UDeclarationsExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UIfExpression
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UUnaryExpression
import org.jetbrains.uast.UastBinaryOperator
import org.jetbrains.uast.getContainingUClass

class RecyclerViewUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val RECYCLER_FACTORY_PACKAGE = DeclaredSymbol.RECYCLER_FACTORY_PACKAGE
        private const val RECYCLER_VIEW_CLASS = "androidx.recyclerview.widget.RecyclerView"

        private const val SCROLL_TO_POSITION_METHOD = "scrollToPosition"
        private const val SMOOTH_SCROLL_TO_POSITION_METHOD = "smoothScrollToPosition"
        private const val SCROLL_TO_FIRST_POSITION_FUNCTION = "scrollToFirstPosition"
        private const val SCROLL_TO_LAST_POSITION_FUNCTION = "scrollToLastPosition"
        private const val SMOOTH_SCROLL_TO_FIRST_POSITION_FUNCTION = "smoothScrollToFirstPosition"
        private const val SMOOTH_SCROLL_TO_LAST_POSITION_FUNCTION = "smoothScrollToLastPosition"
        private const val ADAPTER_PROPERTY = "adapter"
        private const val ITEM_COUNT_PROPERTY = "itemCount"
        private const val MINUS_METHOD = "minus"
        private const val ZERO_ARGUMENT = "0"
        private const val ONE_ARGUMENT = "1"
        private const val THIS_RECEIVER = "this"
        private const val ELVIS_OPERATOR = "?:"

        val ISSUE = Issue.create(
            id = "ReplaceWithRecyclerViewExtension",
            briefDescription = "Use ui-component-adapter's RecyclerView extensions instead.",
            explanation = """
                Using direct first or last position scrolling on `RecyclerView` can be simplified \
                by using RecyclerView extensions from BetterAndroid ui-component-adapter library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-component-adapter#adapter-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-component-adapter#adapter-extension

                The `RecyclerView.kt` provides:
                - `scrollToFirstPosition()` and `scrollToLastPosition()`
                - `smoothScrollToFirstPosition()` and `smoothScrollToLastPosition()`
                - Better readability when scrolling to fixed edge positions

                Examples:
                ```kotlin
                // Before
                recyclerView.scrollToPosition(0)
                recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)

                // After
                recyclerView.scrollToFirstPosition()
                recyclerView.smoothScrollToLastPosition()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                RecyclerViewUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java as Class<out UElement>)

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            val replacement = when (node.methodName) {
                SCROLL_TO_POSITION_METHOD -> resolveRecyclerViewScrollReplacement(
                    context = context,
                    node = node,
                    firstFunction = SCROLL_TO_FIRST_POSITION_FUNCTION,
                    lastFunction = SCROLL_TO_LAST_POSITION_FUNCTION
                )
                SMOOTH_SCROLL_TO_POSITION_METHOD -> resolveRecyclerViewScrollReplacement(
                    context = context,
                    node = node,
                    firstFunction = SMOOTH_SCROLL_TO_FIRST_POSITION_FUNCTION,
                    lastFunction = SMOOTH_SCROLL_TO_LAST_POSITION_FUNCTION
                )
                else -> null
            } ?: return

            // This is the first or last position scrolling pattern.
            val fixName = replacement.substringAfterLast('.').substringBefore('(')
            val importTarget = "$RECYCLER_FACTORY_PACKAGE.$fixName"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = replacement,
                    imports = arrayOf(importTarget)
                )
            )
        }
    })

    private fun resolveRecyclerViewScrollReplacement(
        context: JavaContext,
        node: UCallExpression,
        firstFunction: String,
        lastFunction: String
    ): String? {
        val method = node.resolve() ?: return null
        val containingClass = method.containingClass ?: return null

        // Validation is `RecyclerView.scrollToPosition(...)` or `RecyclerView.smoothScrollToPosition(...)`.
        if (containingClass.qualifiedName != RECYCLER_VIEW_CLASS &&
            !context.evaluator.extendsClass(containingClass, RECYCLER_VIEW_CLASS, false)
        ) return null

        val receiver = node.recyclerViewReceiver(context) ?: return null
        val position = node.valueArguments.firstOrNull()?.unwrapParenthesized() ?: return null
        val replacementReceiver = node.receiverPrefix()

        return when {
            position.isZeroExpression() -> "$replacementReceiver$firstFunction()"
            isLastPositionExpression(receiver, position) -> "$replacementReceiver$lastFunction()"
            else -> null
        }
    }

    private fun isLastPositionExpression(receiver: String, expression: UElement): Boolean {
        val target = expression.resolveLocalVariableInitializer() ?: expression
        return target.isAdapterItemCountMinusOne(receiver)
    }

    private fun UElement.isAdapterItemCountMinusOne(receiver: String): Boolean {
        val target = resolveLocalVariableInitializer() ?: unwrapParenthesized() ?: return false

        return when (target) {
            is UIfExpression -> target.elseExpression?.isZeroExpression() == true &&
                (target.thenExpression?.isAdapterItemCountMinusOne(receiver) == true ||
                    target.thenExpression?.resolveLocalVariableInitializer()
                        ?.isAdapterItemCountMinusOne(receiver) == true ||
                    target.thenExpression?.resolveLocalVariableInitializerFromBlock()
                        ?.isAdapterItemCountMinusOne(receiver) == true)
            is UBinaryExpression -> when (target.operator) {
                UastBinaryOperator.MINUS -> target.leftOperand.isAdapterItemCountOrZero(receiver) &&
                    target.rightOperand.isOneExpression()
                UastBinaryOperator.OTHER -> target.rightOperand.isZeroExpression() &&
                    (target.leftOperand.isAdapterItemCountMinusOne(receiver) ||
                        target.leftOperand.resolveLocalVariableInitializer()
                            ?.isAdapterItemCountMinusOne(receiver) == true ||
                        target.leftOperand.resolveLocalVariableInitializerFromBlock()
                            ?.isAdapterItemCountMinusOne(receiver) == true ||
                        target.isElvisWrappedLastPosition(receiver))
                else -> false
            }
            is UCallExpression -> target.methodName == MINUS_METHOD &&
                target.valueArguments.singleOrNull()?.isOneExpression() == true &&
                target.receiver.isAdapterItemCountOrZero(receiver)
            is UQualifiedReferenceExpression -> {
                val call = target.selector as? UCallExpression
                call != null &&
                    call.methodName == MINUS_METHOD &&
                    call.valueArguments.singleOrNull()?.isOneExpression() == true &&
                    target.receiver.isAdapterItemCountOrZero(receiver)
            }
            else -> target.isElvisWrappedLastPosition(receiver)
        }
    }

    private fun UElement.isElvisWrappedLastPosition(receiver: String): Boolean {
        val source = asSourceString()
        if (!source.endsWith(" $ELVIS_OPERATOR $ZERO_ARGUMENT")) return false

        val leftSource = source.removeSuffix(" $ELVIS_OPERATOR $ZERO_ARGUMENT")
        return leftSource.isLastPositionSource(receiver) ||
            resolveLocalVariableInitializerFromBlock(leftSource)
                ?.isAdapterItemCountMinusOne(receiver) == true
    }

    private fun UElement?.isAdapterItemCountOrZero(receiver: String): Boolean {
        val target = resolveLocalVariableInitializer() ?: unwrapParenthesized() ?: return false

        return when (target) {
            is UIfExpression -> target.elseExpression?.isZeroExpression() == true &&
                target.thenExpression.isAdapterItemCount(receiver)
            else -> target.isAdapterItemCount(receiver) || target.asSourceString().isAdapterItemCountSource(receiver)
        }
    }

    private fun UElement?.isAdapterItemCount(receiver: String): Boolean {
        val target = resolveLocalVariableInitializer() ?: unwrapParenthesized() ?: return false
        if (target !is UQualifiedReferenceExpression) return false
        if (target.selector.asSourceString() != ITEM_COUNT_PROPERTY) return false

        return target.receiver.isRecyclerViewAdapter(receiver)
    }

    private fun UElement?.isRecyclerViewAdapter(receiver: String): Boolean {
        val target = unwrapParenthesized() ?: return false

        return when (target) {
            is UQualifiedReferenceExpression -> {
                val receiverText = target.receiver.asSourceString()
                val selectorText = target.selector.asSourceString()
                receiverText == receiver && selectorText == ADAPTER_PROPERTY ||
                    receiverText == "$receiver.$ADAPTER_PROPERTY" && selectorText == ITEM_COUNT_PROPERTY
            }
            is UUnaryExpression -> target.operand.isRecyclerViewAdapter(receiver)
            else -> false
        }
    }

    private fun UElement?.isZeroExpression() = (resolveLocalVariableInitializer() ?: unwrapParenthesized())
        ?.asSourceString() == ZERO_ARGUMENT

    private fun UElement?.isOneExpression() = (resolveLocalVariableInitializer() ?: unwrapParenthesized())
        ?.asSourceString() == ONE_ARGUMENT

    private fun UCallExpression.recyclerViewReceiver(context: JavaContext) = receiver?.asSourceString()
        ?: THIS_RECEIVER.takeIf { getContainingUClass()?.javaPsi.isRecyclerViewClass(context) }

    private fun UElement?.resolveLocalVariableInitializerFromBlock(): UElement? {
        val reference = this.unwrapParenthesized() as? USimpleNameReferenceExpression ?: return null
        return reference.resolveLocalVariableInitializerFromBlock(reference.identifier)
    }

    private fun UElement.resolveLocalVariableInitializerFromBlock(name: String): UElement? {
        var parent = uastParent

        while (parent != null) {
            if (parent is UBlockExpression) {
                return parent.expressions
                    .flatMap { expression ->
                        when (expression) {
                            is ULocalVariable -> listOf(expression)
                            is UDeclarationsExpression -> expression.declarations.filterIsInstance<ULocalVariable>()
                            else -> emptyList()
                        }
                    }
                    .firstOrNull { it.name == name }
                    ?.uastInitializer
                    ?.unwrapParenthesized()
            }
            parent = parent.uastParent
        }

        return null
    }

    private fun String.isLastPositionSource(receiver: String) =
        startsWith("$receiver.$ADAPTER_PROPERTY") &&
            containsItemCountAccess() &&
            containsMinusOneCall()

    private fun String.isAdapterItemCountSource(receiver: String) =
        startsWith("$receiver.$ADAPTER_PROPERTY") &&
            containsItemCountAccess()

    private fun String.containsItemCountAccess() =
        contains(".$ITEM_COUNT_PROPERTY") || contains("?.$ITEM_COUNT_PROPERTY")

    private fun String.containsMinusOneCall() =
        contains(".$MINUS_METHOD($ONE_ARGUMENT)") || contains("?.$MINUS_METHOD($ONE_ARGUMENT)")

    private fun PsiClass?.isRecyclerViewClass(context: JavaContext): Boolean {
        val psiClass = this ?: return false
        return psiClass.qualifiedName == RECYCLER_VIEW_CLASS ||
            context.evaluator.extendsClass(psiClass, RECYCLER_VIEW_CLASS, false)
    }
}
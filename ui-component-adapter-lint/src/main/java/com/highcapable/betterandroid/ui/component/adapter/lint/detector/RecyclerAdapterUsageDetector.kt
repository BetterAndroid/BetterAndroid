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
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.ui.component.adapter.lint.DeclaredSymbol
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.asCall
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.findContainingBlock
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.findMethodExpressionSource
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.isObjectLiteralOf
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTypesUtil
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.getContainingUClass

class RecyclerAdapterUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val DIFF_CALLBACK_CLASS = "androidx.recyclerview.widget.DiffUtil.Callback"
        private const val LIST_UPDATE_CALLBACK_CLASS = "androidx.recyclerview.widget.ListUpdateCallback"
        private const val ADAPTER_CLASS_NAME = "androidx.recyclerview.widget.RecyclerView.Adapter"
        private const val COLLECTION_CLASS = "java.util.Collection"
        private const val DIFF_UTIL_CALLBACK_FUNCTION = "DiffUtilCallback"
        private const val LIST_UPDATE_CALLBACK_FUNCTION = "ListUpdateCallback"
        private const val DIFF_UTIL_CLASS = "androidx.recyclerview.widget.DiffUtil"
        private const val DIFF_RESULT_CLASS = "androidx.recyclerview.widget.DiffUtil.DiffResult"
        private const val ADAPTER_CLASS = "androidx.recyclerview.widget.RecyclerView.Adapter"
        private const val NOTIFY_ITEM_RANGE_INSERTED_METHOD = "notifyItemRangeInserted"
        private const val NOTIFY_ITEM_RANGE_CHANGED_METHOD = "notifyItemRangeChanged"
        private const val NOTIFY_ITEM_RANGE_REMOVED_METHOD = "notifyItemRangeRemoved"
        private const val NOTIFY_ALL_ITEMS_INSERTED_FUNCTION = "notifyAllItemsInserted"
        private const val NOTIFY_ALL_ITEMS_CHANGED_FUNCTION = "notifyAllItemsChanged"
        private const val NOTIFY_BY_DIFF_FUNCTION = "notifyByDiff"
        private const val CLEAR_AND_NOTIFY_FUNCTION = "clearAndNotify"
        private const val DISPATCH_UPDATES_TO_METHOD = "dispatchUpdatesTo"
        private const val CALCULATE_DIFF_METHOD = "calculateDiff"
        private const val GET_OLD_LIST_SIZE_METHOD = "getOldListSize"
        private const val GET_NEW_LIST_SIZE_METHOD = "getNewListSize"
        private const val ARE_ITEMS_THE_SAME_METHOD = "areItemsTheSame"
        private const val ARE_CONTENTS_THE_SAME_METHOD = "areContentsTheSame"
        private const val GET_CHANGE_PAYLOAD_METHOD = "getChangePayload"
        private const val ON_INSERTED_METHOD = "onInserted"
        private const val ON_REMOVED_METHOD = "onRemoved"
        private const val ON_MOVED_METHOD = "onMoved"
        private const val ON_CHANGED_METHOD = "onChanged"
        private const val CLEAR_METHOD = "clear"
        private const val ITEM_COUNT_PROPERTY = "itemCount"
        private const val SIZE_PROPERTY = "size"
        private const val OLD_SIZE_VARIABLE = "oldSize"
        private const val ZERO_ARGUMENT = "0"
        private const val TRUE_ARGUMENT = "true"
        private const val UNRESOLVED_REPLACEMENT = "..."
        private const val THIS_RECEIVER = "this"
        private const val OLD_ITEM_POSITION_PARAMETER = "oldItemPosition"
        private const val NEW_ITEM_POSITION_PARAMETER = "newItemPosition"
        private const val OLD_ITEM_PARAMETER = "oldItem"
        private const val NEW_ITEM_PARAMETER = "newItem"
        private const val RECYCLER_FACTORY_PACKAGE = DeclaredSymbol.RECYCLER_FACTORY_PACKAGE

        val ISSUE = Issue.create(
            id = "ReplaceWithRecyclerAdapterExtension",
            briefDescription = "Use ui-component-adapter's RecyclerAdapter extensions instead.",
            explanation = """
                Using `DiffUtil.Callback`, `ListUpdateCallback`, \
                `DiffUtil.calculateDiff(...).dispatchUpdatesTo(...)`, full-range notify calls, \
                or manual clear notifications can be simplified by using RecyclerAdapter extensions \
                from BetterAndroid ui-component-adapter library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-component-adapter#adapter-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-component-adapter#adapter-extension

                The `RecyclerAdapter.kt` provides:
                - `DiffUtilCallback(...)` for creating reusable `DiffUtil.Callback`
                - `ListUpdateCallback(...)` for creating reusable `ListUpdateCallback`
                - `notifyByDiff(...)` for clearer DiffUtil updates
                - `notifyAllItemsInserted(...)` and `notifyAllItemsChanged(...)` for full-range updates
                - `clearAndNotify(...)` for clearing a data set and notifying in one step

                Examples:
                ```kotlin
                // Before
                val callback = object : DiffUtil.Callback() { ... }
                val listUpdateCallback = object : ListUpdateCallback { ... }
                DiffUtil.calculateDiff(callback).dispatchUpdatesTo(adapter)
                adapter.notifyItemRangeInserted(0, adapter.itemCount)
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
                dataSet.clear()
                adapter.notifyItemRangeRemoved(0, oldSize)

                // After
                val callback = DiffUtilCallback(...)
                val listUpdateCallback = ListUpdateCallback(...)
                adapter.notifyByDiff(callback)
                adapter.notifyAllItemsInserted()
                adapter.notifyAllItemsChanged()
                adapter.clearAndNotify(dataSet)
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                RecyclerAdapterUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UObjectLiteralExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitObjectLiteralExpression(node: UObjectLiteralExpression) {
            reportDiffUtilCallback(node)
            reportListUpdateCallback(node)
        }

        override fun visitCallExpression(node: UCallExpression) {
            when {
                reportDiffDispatch(node) -> Unit
                reportNotifyAllItemsInserted(node) -> Unit
                reportNotifyAllItemsChanged(node) -> Unit
                reportClearAndNotify(node) -> Unit
            }
        }

        private fun reportDiffDispatch(node: UCallExpression): Boolean {
            if (node.methodName != DISPATCH_UPDATES_TO_METHOD) return false

            // Validation is `DiffUtil.DiffResult.dispatchUpdatesTo(...)`.
            val method = node.resolve() ?: return false
            if (method.containingClass?.qualifiedName != DIFF_RESULT_CLASS &&
                !context.evaluator.extendsClass(method.containingClass, DIFF_RESULT_CLASS, false)
            ) return false
            if (node.valueArgumentCount != 1) return false

            // This is the `DiffUtil.calculateDiff(...).dispatchUpdatesTo(adapter)` pattern.
            val receiver = node.receiver.asCall() ?: return false
            if (receiver.methodName != CALCULATE_DIFF_METHOD) return false
            val receiverMethod = receiver.resolve() ?: return false
            if (receiverMethod.containingClass?.qualifiedName != DIFF_UTIL_CLASS) return false

            val targetArgument = node.valueArguments.firstOrNull()?.unwrapParenthesized() as? UExpression ?: return false
            val replacement = resolveNotifyByDiffReplacement(targetArgument, receiver)
                ?: resolveNotifyByDiffCallbackReplacement(node, receiver)
                ?: return false

            val quickFix = if (replacement.contains(UNRESOLVED_REPLACEMENT)) null else buildReplaceFix(
                name = "Replace with '$NOTIFY_BY_DIFF_FUNCTION'",
                replacement = replacement,
                imports = arrayOf("$RECYCLER_FACTORY_PACKAGE.$NOTIFY_BY_DIFF_FUNCTION")
            )

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = quickFix
            )
            return true
        }

        private fun reportDiffUtilCallback(node: UObjectLiteralExpression): Boolean {
            if (!node.isObjectLiteralOf(DIFF_CALLBACK_CLASS)) return false

            // This is the `object : DiffUtil.Callback()` pattern.
            val replacement = resolveDiffUtilCallbackReplacement(node) ?: return false

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$DIFF_UTIL_CALLBACK_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("$RECYCLER_FACTORY_PACKAGE.$DIFF_UTIL_CALLBACK_FUNCTION")
                )
            )
            return true
        }

        private fun reportListUpdateCallback(node: UObjectLiteralExpression): Boolean {
            if (!node.isObjectLiteralOf(LIST_UPDATE_CALLBACK_CLASS)) return false

            // This is the `object : ListUpdateCallback` pattern.
            val replacement = resolveListUpdateCallbackReplacement(node) ?: return false

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$LIST_UPDATE_CALLBACK_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("$RECYCLER_FACTORY_PACKAGE.$LIST_UPDATE_CALLBACK_FUNCTION")
                )
            )
            return true
        }

        private fun reportNotifyAllItemsInserted(node: UCallExpression): Boolean {
            if (node.methodName != NOTIFY_ITEM_RANGE_INSERTED_METHOD) return false

            // Validation is `RecyclerView.Adapter.notifyItemRangeInserted(...)`.
            if (!isRecyclerViewAdapterMethod(node)) return false
            if (!isZeroArgument(node.valueArguments.getOrNull(0))) return false

            // This is the full-range inserted notification pattern.
            val countArgument = node.valueArguments.getOrNull(1)?.unwrapParenthesized() ?: return false
            val receiver = node.adapterReceiver() ?: return false
            val replacementReceiver = node.receiverPrefix()
            when {
                countArgument.isAdapterItemCount(receiver) -> Unit
                countArgument.isCollectionSizeAccess() -> Unit
                else -> return false
            }
            val replacement = "$replacementReceiver$NOTIFY_ALL_ITEMS_INSERTED_FUNCTION()"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$NOTIFY_ALL_ITEMS_INSERTED_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("$RECYCLER_FACTORY_PACKAGE.$NOTIFY_ALL_ITEMS_INSERTED_FUNCTION")
                )
            )
            return true
        }

        private fun reportNotifyAllItemsChanged(node: UCallExpression): Boolean {
            if (node.methodName != NOTIFY_ITEM_RANGE_CHANGED_METHOD) return false

            // Validation is `RecyclerView.Adapter.notifyItemRangeChanged(...)`.
            if (!isRecyclerViewAdapterMethod(node)) return false
            if (!isZeroArgument(node.valueArguments.getOrNull(0))) return false

            // This is the full-range changed notification pattern.
            val countArgument = node.valueArguments.getOrNull(1)?.unwrapParenthesized() ?: return false
            val receiver = node.adapterReceiver() ?: return false
            val replacementReceiver = node.receiverPrefix()
            val payloadArgument = node.valueArguments.getOrNull(2)?.unwrapParenthesized()?.asSourceString()
            when {
                payloadArgument != null -> return false
                countArgument.isAdapterItemCount(receiver) -> Unit
                countArgument.isCollectionSizeAccess() -> Unit
                else -> return false
            }
            val replacement = "$replacementReceiver$NOTIFY_ALL_ITEMS_CHANGED_FUNCTION()"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$NOTIFY_ALL_ITEMS_CHANGED_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("$RECYCLER_FACTORY_PACKAGE.$NOTIFY_ALL_ITEMS_CHANGED_FUNCTION")
                )
            )
            return true
        }

        private fun reportClearAndNotify(node: UCallExpression): Boolean {
            if (node.methodName != NOTIFY_ITEM_RANGE_REMOVED_METHOD) return false

            // Validation is `RecyclerView.Adapter.notifyItemRangeRemoved(...)`.
            if (!isRecyclerViewAdapterMethod(node)) return false
            if (!isZeroArgument(node.valueArguments.getOrNull(0))) return false

            // This is the `dataSet.clear()` + `notifyItemRangeRemoved(...)` pattern.
            val block = node.findContainingBlock() ?: return false
            val expressions = block.expressions
            val index = expressions.indexOfFirst { expression ->
                val current = expression.asCall()
                current == node
            }
            if (index <= 0) return false

            val clearExpression = expressions.getOrNull(index - 1)?.unwrapParenthesized() ?: return false
            val notifyExpression = expressions.getOrNull(index)?.unwrapParenthesized() ?: return false
            val clearCall = clearExpression.asCall() ?: return false
            if (clearCall.methodName != CLEAR_METHOD) return false

            val dataSet = clearCall.receiver?.asSourceString() ?: return false
            val countArgument = node.valueArguments.getOrNull(1)?.unwrapParenthesized()?.asSourceString() ?: return false
            if (countArgument != "$dataSet.$SIZE_PROPERTY" && countArgument != OLD_SIZE_VARIABLE) return false

            node.adapterReceiver() ?: return false
            val replacement = "${node.receiverPrefix()}$CLEAR_AND_NOTIFY_FUNCTION($dataSet)"

            context.report(
                issue = ISSUE,
                scope = notifyExpression,
                location = context.getRangeLocation(clearExpression, 0, notifyExpression, 0),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildClearAndNotifyFix(context, clearExpression, notifyExpression, replacement)
            )
            return true
        }

        private fun isRecyclerViewAdapterMethod(node: UCallExpression): Boolean {
            val method = node.resolve() ?: return false
            val containingClass = method.containingClass ?: return false

            return containingClass.qualifiedName == ADAPTER_CLASS ||
                context.evaluator.extendsClass(containingClass, ADAPTER_CLASS, false)
        }

        private fun UCallExpression.adapterReceiver() = receiver?.asSourceString()
            ?: THIS_RECEIVER.takeIf { getContainingUClass()?.javaPsi.isRecyclerViewAdapterClass() }

        private fun UCallExpression.receiverPrefix() = receiver?.asSourceString()?.let { "$it." }.orEmpty()

        private fun isZeroArgument(element: UElement?) = element?.unwrapParenthesized()?.asSourceString() == ZERO_ARGUMENT

        private fun UElement.isAdapterItemCount(adapterReceiver: String): Boolean {
            val source = asSourceString()
            if (source == "$adapterReceiver.$ITEM_COUNT_PROPERTY") return true

            return adapterReceiver == THIS_RECEIVER && source == ITEM_COUNT_PROPERTY
        }

        private fun UElement.isCollectionSizeAccess(): Boolean {
            val target = unwrapParenthesized() ?: return false
            return when (target) {
                is UQualifiedReferenceExpression -> {
                    val selector = target.selector as? USimpleNameReferenceExpression ?: return false
                    selector.identifier == SIZE_PROPERTY && target.receiver.isCollectionExpression()
                }
                else -> false
            }
        }

        private fun UExpression.isCollectionExpression(): Boolean {
            val psiClass = PsiTypesUtil.getPsiClass(getExpressionType()) ?: return false
            return psiClass.qualifiedName == COLLECTION_CLASS ||
                context.evaluator.extendsClass(psiClass, COLLECTION_CLASS, false)
        }

        private fun resolveNotifyByDiffReplacement(
            dispatchTarget: UExpression,
            calculateDiffNode: UCallExpression
        ): String? {
            if (!dispatchTarget.isRecyclerViewAdapter()) return null

            val callbackArgument = calculateDiffNode.valueArguments.firstOrNull()?.unwrapParenthesized() ?: return null
            val callbackExpression = callbackArgument as? UObjectLiteralExpression ?: return null
            val adapter = dispatchTarget.asSourceString()
            val oldList = callbackExpression.findMethodExpressionSource(GET_OLD_LIST_SIZE_METHOD, SIZE_PROPERTY) ?: return null
            val newList = callbackExpression.findMethodExpressionSource(GET_NEW_LIST_SIZE_METHOD, SIZE_PROPERTY) ?: return null
            val areItemsTheSame = callbackExpression.findMethodExpressionSource(ARE_ITEMS_THE_SAME_METHOD)
                ?.normalizeDiffItemExpression(oldList, newList) ?: return null
            val areContentsTheSame = callbackExpression.findMethodExpressionSource(ARE_CONTENTS_THE_SAME_METHOD)
                ?.normalizeDiffItemExpression(oldList, newList) ?: return null
            val getChangePayload = callbackExpression.findMethodExpressionSource(GET_CHANGE_PAYLOAD_METHOD)
                ?.normalizeDiffItemExpression(oldList, newList)
            val detectMoves = calculateDiffNode.valueArguments.getOrNull(1)?.unwrapParenthesized()?.asSourceString()

            return buildString {
                append("$adapter.$NOTIFY_BY_DIFF_FUNCTION(")
                append("oldList = $oldList, ")
                append("newList = $newList, ")
                append("areItemsTheSame = { $OLD_ITEM_PARAMETER, $NEW_ITEM_PARAMETER -> $areItemsTheSame }, ")
                append("areContentsTheSame = { $OLD_ITEM_PARAMETER, $NEW_ITEM_PARAMETER -> $areContentsTheSame }")
                if (getChangePayload != null) append(", getChangePayload = { $OLD_ITEM_PARAMETER, $NEW_ITEM_PARAMETER -> $getChangePayload }")
                if (detectMoves != null && detectMoves != TRUE_ARGUMENT) append(", detectMoves = $detectMoves")
                append(")")
            }
        }

        private fun resolveNotifyByDiffCallbackReplacement(
            dispatchNode: UCallExpression,
            calculateDiffNode: UCallExpression
        ): String? {
            val adapterArgument = dispatchNode.valueArguments.firstOrNull()?.unwrapParenthesized() as? UExpression ?: return null
            if (!adapterArgument.isRecyclerViewAdapter()) return null
            if (calculateDiffNode.valueArguments.firstOrNull()?.unwrapParenthesized() is UObjectLiteralExpression) return null

            val callback = calculateDiffNode.valueArguments.firstOrNull()?.unwrapParenthesized()?.asSourceString() ?: return null
            val detectMoves = calculateDiffNode.valueArguments.getOrNull(1)?.unwrapParenthesized()?.asSourceString()
            val adapter = adapterArgument.asSourceString()
                .takeUnless { it == THIS_RECEIVER }
                ?.let { "$it." }
                .orEmpty()

            return buildString {
                append("$adapter$NOTIFY_BY_DIFF_FUNCTION($callback")
                if (detectMoves != null && detectMoves != TRUE_ARGUMENT) append(", detectMoves = $detectMoves")
                append(")")
            }
        }
    }

    private fun UExpression.isRecyclerViewAdapter(): Boolean {
        val psiClass = PsiTypesUtil.getPsiClass(getExpressionType()) ?: return false
        return psiClass.isRecyclerViewAdapterClass()
    }

    private fun resolveDiffUtilCallbackReplacement(node: UObjectLiteralExpression): String? {
        val oldList = node.findMethodExpressionSource(GET_OLD_LIST_SIZE_METHOD, SIZE_PROPERTY) ?: return null
        val newList = node.findMethodExpressionSource(GET_NEW_LIST_SIZE_METHOD, SIZE_PROPERTY) ?: return null
        val areItemsTheSame = node.findMethodExpressionSource(ARE_ITEMS_THE_SAME_METHOD)
            ?.normalizeDiffItemExpression(oldList, newList) ?: return null
        val areContentsTheSame = node.findMethodExpressionSource(ARE_CONTENTS_THE_SAME_METHOD)
            ?.normalizeDiffItemExpression(oldList, newList) ?: return null
        val getChangePayload = node.findMethodExpressionSource(GET_CHANGE_PAYLOAD_METHOD)
            ?.normalizeDiffItemExpression(oldList, newList)

        return buildString {
            append("$DIFF_UTIL_CALLBACK_FUNCTION(")
            append("oldList = $oldList, ")
            append("newList = $newList, ")
            append("areItemsTheSame = { $OLD_ITEM_PARAMETER, $NEW_ITEM_PARAMETER -> $areItemsTheSame }, ")
            append("areContentsTheSame = { $OLD_ITEM_PARAMETER, $NEW_ITEM_PARAMETER -> $areContentsTheSame }")
            if (getChangePayload != null) append(", getChangePayload = { $OLD_ITEM_PARAMETER, $NEW_ITEM_PARAMETER -> $getChangePayload }")
            append(")")
        }
    }

    private fun String.normalizeDiffItemExpression(oldList: String, newList: String) =
        replace("$oldList[$OLD_ITEM_POSITION_PARAMETER]", OLD_ITEM_PARAMETER)
            .replace("$newList[$NEW_ITEM_POSITION_PARAMETER]", NEW_ITEM_PARAMETER)

    private fun PsiClass?.isRecyclerViewAdapterClass(): Boolean {
        val psiClass = this ?: return false
        return psiClass.qualifiedName == ADAPTER_CLASS_NAME ||
            psiClass.supers.any { it.qualifiedName == ADAPTER_CLASS_NAME }
    }

    private fun resolveListUpdateCallbackReplacement(node: UObjectLiteralExpression): String? {
        val onInserted = node.findMethodExpressionSource(ON_INSERTED_METHOD) ?: return null
        val onRemoved = node.findMethodExpressionSource(ON_REMOVED_METHOD) ?: return null
        val onMoved = node.findMethodExpressionSource(ON_MOVED_METHOD) ?: return null
        val onChanged = node.findMethodExpressionSource(ON_CHANGED_METHOD) ?: return null

        return buildString {
            append("$LIST_UPDATE_CALLBACK_FUNCTION(")
            append("onInserted = { position, count -> $onInserted }, ")
            append("onRemoved = { position, count -> $onRemoved }, ")
            append("onMoved = { fromPosition, toPosition -> $onMoved }, ")
            append("onChanged = { position, count, payload -> $onChanged }")
            append(")")
        }
    }

    private fun buildClearAndNotifyFix(
        context: JavaContext,
        clearExpression: UElement,
        notifyExpression: UElement,
        replacement: String
    ): LintFix {
        val clearSourcePsi = clearExpression.sourcePsi
        val notifySourcePsi = notifyExpression.sourcePsi
        if (clearSourcePsi == null || notifySourcePsi == null) return buildReplaceFix(
            name = "Replace with 'clearAndNotify'",
            replacement = replacement,
            imports = arrayOf("$RECYCLER_FACTORY_PACKAGE.$CLEAR_AND_NOTIFY_FUNCTION")
        )

        val deleteClearFix = LintFix.create()
            .replace()
            .range(context.getLocation(clearSourcePsi))
            .with("")
            .reformat(true)
            .build()

        val replaceNotifyFix = LintFix.create()
            .replace()
            .range(context.getLocation(notifySourcePsi))
            .with(replacement)
            .reformat(true)
            .shortenNames()
            .imports("$RECYCLER_FACTORY_PACKAGE.$CLEAR_AND_NOTIFY_FUNCTION")
            .build()

        return LintFix.create()
            .name("Replace with 'clearAndNotify'")
            .composite(deleteClearFix, replaceNotifyFix)
    }
}
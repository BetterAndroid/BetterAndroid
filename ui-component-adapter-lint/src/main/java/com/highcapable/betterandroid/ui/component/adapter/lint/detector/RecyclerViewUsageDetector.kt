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
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.resolveLocalVariableInitializer
import com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension.unwrapParenthesized
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

class RecyclerViewUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val RECYCLER_VIEW_CLASS = "androidx.recyclerview.widget.RecyclerView"
        private const val SCROLL_TO_POSITION_METHOD = "scrollToPosition"
        private const val SMOOTH_SCROLL_TO_POSITION_METHOD = "smoothScrollToPosition"
        private const val SCROLL_TO_FIRST_POSITION_FUNCTION = "scrollToFirstPosition"
        private const val SCROLL_TO_LAST_POSITION_FUNCTION = "scrollToLastPosition"
        private const val SMOOTH_SCROLL_TO_FIRST_POSITION_FUNCTION = "smoothScrollToFirstPosition"
        private const val SMOOTH_SCROLL_TO_LAST_POSITION_FUNCTION = "smoothScrollToLastPosition"

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

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

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

            val fixName = replacement.substringAfterLast('.').substringBefore('(')
            val importTarget = "${DeclaredSymbol.RECYCLER_FACTORY_PACKAGE}.$fixName"

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
    }

    private fun resolveRecyclerViewScrollReplacement(
        context: JavaContext,
        node: UCallExpression,
        firstFunction: String,
        lastFunction: String
    ): String? {
        val method = node.resolve() ?: return null
        val containingClass = method.containingClass ?: return null
        if (containingClass.qualifiedName != RECYCLER_VIEW_CLASS &&
            !context.evaluator.extendsClass(containingClass, RECYCLER_VIEW_CLASS, false)
        ) return null

        val receiver = node.receiver?.asSourceString() ?: return null
        val position = node.valueArguments.firstOrNull()?.unwrapParenthesized() ?: return null

        return when {
            position.asSourceString() == "0" -> "$receiver.$firstFunction()"
            isLastPositionExpression(receiver, position) -> "$receiver.$lastFunction()"
            else -> null
        }
    }

    private fun isLastPositionExpression(receiver: String, expression: UElement): Boolean {
        val target = expression.resolveLocalVariableInitializer() ?: expression
        val source = target.asSourceString()

        return source == "$receiver.adapter!!.itemCount - 1" ||
            source == "($receiver.adapter!!.itemCount - 1)" ||
            source == "($receiver.adapter?.itemCount ?: 0) - 1" ||
            source == "(($receiver.adapter?.itemCount ?: 0) - 1)" ||
            source == "($receiver.adapter?.itemCount ?: 0).minus(1)" ||
            source == "$receiver.adapter?.itemCount?.minus(1)" ||
            source == "($receiver.adapter?.itemCount?.minus(1))"
    }
}
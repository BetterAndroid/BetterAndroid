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
import org.jetbrains.uast.UCallExpression

class ViewTooltipTextUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val TOOLTIP_COMPAT_CLASS = "androidx.appcompat.widget.TooltipCompat"
        private const val VIEW_COMPAT_CLASS = "androidx.core.view.ViewCompat"
        private const val SET_TOOLTIP_TEXT_METHOD = "setTooltipText"

        private const val TOOLTIP_TEXT_COMPAT_PROPERTY = "tooltipTextCompat"
        private const val TOOLTIP_TEXT_COMPAT_FULL_NAME = "${DeclaredSymbol.VIEW_PACKAGE}.$TOOLTIP_TEXT_COMPAT_PROPERTY"

        val ISSUE = Issue.create(
            id = "ReplaceWithViewTooltipTextCompatExtension",
            briefDescription = "Use ui-extension's `tooltipTextCompat` instead of `TooltipCompat.setTooltipText(...)` or `ViewCompat.setTooltipText(...)`.",
            explanation = """
                Using `TooltipCompat.setTooltipText(...)` or `ViewCompat.setTooltipText(...)` can \
                be simplified by using `tooltipTextCompat` from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#view-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#view-extension

                The `View.kt` provides:
                - A direct tooltip compatibility property
                - Less static compat API code
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                TooltipCompat.setTooltipText(view, "Tooltip")
                ViewCompat.setTooltipText(view, "Tooltip")

                // After
                view.tooltipTextCompat = "Tooltip"
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ViewTooltipTextUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != SET_TOOLTIP_TEXT_METHOD) return

            // Validation is TooltipCompat or ViewCompat class.
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, TOOLTIP_COMPAT_CLASS) &&
                !context.evaluator.isMemberInClass(method, VIEW_COMPAT_CLASS)
            ) return

            val arguments = node.valueArguments
            if (arguments.size < 2) return

            // This is the `TooltipCompat/ViewCompat.setTooltipText(view, text)` pattern.
            val viewText = arguments[0].asSourceString()
            val tooltipText = arguments[1].asSourceString()
            val replacement = "$viewText.$TOOLTIP_TEXT_COMPAT_PROPERTY = $tooltipText"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$TOOLTIP_TEXT_COMPAT_PROPERTY'",
                    replacement = replacement,
                    imports = arrayOf(TOOLTIP_TEXT_COMPAT_FULL_NAME)
                )
            )
        }
    }
}
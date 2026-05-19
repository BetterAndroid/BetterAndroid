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
import org.jetbrains.uast.UCallExpression

class WindowInsetsListenerUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val VIEW_COMPAT_CLASS = "androidx.core.view.ViewCompat"
        private const val VIEW_CLASS = "android.view.View"
        private const val SET_ON_APPLY_WINDOW_INSETS_LISTENER = "setOnApplyWindowInsetsListener"
        private const val HANDLE_ON_WINDOW_INSETS_CHANGED = "handleOnWindowInsetsChanged"

        val ISSUE = Issue.create(
            id = "ReplaceWithHandleOnWindowInsetsChanged",
            briefDescription = "Use ui-extension's `handleOnWindowInsetsChanged(...)` instead.",
            explanation = """
                Using `setOnApplyWindowInsetsListener(...)` can be simplified by using \
                `handleOnWindowInsetsChanged(...)` from BetterAndroid ui-extension library.

                The `WindowInsets.kt` provides:
                - Direct `WindowInsetsWrapper` callback instead of manually converting `WindowInsetsCompat`
                - Optional `animated` parameter to handle window insets animation changes together
                - Optional `consumed` and `requestApplyOnLayout` parameters for common insets handling scenarios
                - Matching `removeWindowInsetsListener()` to clear both listener and animation callback together

                Examples:
                ```kotlin
                // Before
                ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                    val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
                    view.updatePadding(bottom = imeInsets.bottom)
                    insets
                }

                // After
                view.handleOnWindowInsetsChanged { currentView, insetsWrapper ->
                    currentView.updateInsetsPadding(insetsWrapper.ime, bottom = true)
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                WindowInsetsListenerUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != SET_ON_APPLY_WINDOW_INSETS_LISTENER) return

            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, VIEW_COMPAT_CLASS) &&
                !context.evaluator.isMemberInClass(method, VIEW_CLASS)
            ) return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$HANDLE_ON_WINDOW_INSETS_CHANGED(...)`."
            )
        }
    }
}
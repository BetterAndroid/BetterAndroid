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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.receiverPrefix
import org.jetbrains.uast.UCallExpression

class DrawableUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val DRAWABLE_CLASS = "android.graphics.drawable.Drawable"
        private const val SET_PADDING_METHOD = "setPadding"

        val ISSUE = Issue.create(
            id = "ReplaceWithDrawableExtension",
            briefDescription = "Use ui-extension's `Drawable.setPadding(size)` instead.",
            explanation = """
                Using `Drawable.setPadding(left, top, right, bottom)` with the same value on all \
                sides can be simplified by using `setPadding(size)` from BetterAndroid ui-extension \
                library.

                The `Drawable.kt` provides:
                - A shorter API for uniform drawable padding
                - Less duplicated padding arguments
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                drawable.setPadding(20, 20, 20, 20)

                // After
                drawable.setPadding(20)
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                DrawableUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != SET_PADDING_METHOD) return

            val method = node.resolve() ?: return
            val containingClass = method.containingClass ?: return
            if (containingClass.qualifiedName != DRAWABLE_CLASS &&
                !context.evaluator.extendsClass(containingClass, DRAWABLE_CLASS, false)
            ) return

            val arguments = node.valueArguments
            if (arguments.size != 4) return

            val values = arguments.map { it.asSourceString() }
            if (values.distinct().size != 1) return

            val replacement = "${node.receiverPrefix()}$SET_PADDING_METHOD(${values.first()})"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$SET_PADDING_METHOD'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$SET_PADDING_METHOD")
                )
            )
        }
    }
}
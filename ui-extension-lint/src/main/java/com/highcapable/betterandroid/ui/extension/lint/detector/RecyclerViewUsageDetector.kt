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
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind

class RecyclerViewUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val LAYOUT_MANAGER_FUNCTION = "layoutManager"

        val ISSUE = Issue.create(
            id = "ReplaceWithRecyclerViewExtension",
            briefDescription = "Use ui-extension's `RecyclerView.layoutManager()` instead.",
            explanation = """
                Using `recyclerView.layoutManager as ...` or `as? ...` can be simplified by using \
                `layoutManager()` from BetterAndroid ui-extension library.

                The `RecyclerView.kt` provides:
                - A generic layout manager access API
                - Less explicit casting code
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                recyclerView.layoutManager as LinearLayoutManager
                recyclerView.layoutManager as? LinearLayoutManager

                // After
                recyclerView.layoutManager<LinearLayoutManager>()
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

    override fun getApplicableUastTypes() = listOf(UBinaryExpressionWithType::class.java as Class<out UElement>)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return

            val operand = node.operand.asSourceString()
            val targetType = node.typeReference?.asSourceString() ?: return
            val replacement = when {
                operand == "layoutManager" -> "$LAYOUT_MANAGER_FUNCTION<$targetType>()"
                operand == "this.layoutManager" -> "this.$LAYOUT_MANAGER_FUNCTION<$targetType>()"
                operand.endsWith(".layoutManager") -> "${operand.removeSuffix(".layoutManager")}.$LAYOUT_MANAGER_FUNCTION<$targetType>()"
                else -> return
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$LAYOUT_MANAGER_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.VIEW_PACKAGE}.$LAYOUT_MANAGER_FUNCTION")
                )
            )
        }
    }
}
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
 * This file is created by fankes on 2026/5/22.
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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.findMethod
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.isObjectLiteralOf
import org.jetbrains.uast.UBinaryExpression
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UastBinaryOperator

class ViewOutlineProviderUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val VIEW_OUTLINE_PROVIDER_CLASS = "android.view.ViewOutlineProvider"

        private const val VIEW_FILE_NAME = "View.kt"
        private const val OUTLINE_PROVIDER_PROPERTY = "outlineProvider"
        private const val OUTLINE_PROVIDER_FUNCTION = "outlineProvider"
        private const val GET_OUTLINE_METHOD = "getOutline"

        val ISSUE = Issue.create(
            id = "ReplaceWithViewOutlineProviderExtension",
            briefDescription = "Use ui-extension's `outlineProvider(...)` instead.",
            explanation = """
                Using `outlineProvider = object : ViewOutlineProvider()` can be simplified by using \
                `outlineProvider(...)` from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#view-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#view-extension

                The `View.kt` provides:
                - A direct outline provider DSL for `View`
                - Less anonymous class boilerplate
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                view.outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: Outline) {
                        // Do something with view and outline.
                    }
                }

                // After
                view.outlineProvider { view, outline ->
                    // Do something with view and outline.
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ViewOutlineProviderUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UBinaryExpression::class.java as Class<out UElement>)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitBinaryExpression(node: UBinaryExpression) {
            if (node.operator != UastBinaryOperator.ASSIGN) return
            if (node.sourcePsi?.containingFile?.name == VIEW_FILE_NAME) return

            val receiver = resolveOutlineProviderReceiver(node.leftOperand) ?: return
            val objectLiteral = node.rightOperand as? UObjectLiteralExpression ?: return
            if (!objectLiteral.isObjectLiteralOf(VIEW_OUTLINE_PROVIDER_CLASS)) return

            val replacement = buildReplacement(receiver, objectLiteral) ?: return
            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${replacement.display}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$OUTLINE_PROVIDER_FUNCTION'",
                    replacement = replacement.source,
                    imports = arrayOf("${DeclaredSymbol.VIEW_PACKAGE}.$OUTLINE_PROVIDER_FUNCTION")
                )
            )
        }

        private fun resolveOutlineProviderReceiver(expression: UExpression): String? = when (expression) {
            is UQualifiedReferenceExpression -> {
                val selector = expression.selector as? USimpleNameReferenceExpression ?: return null
                if (selector.identifier != OUTLINE_PROVIDER_PROPERTY) return null
                expression.receiver.asSourceString()
            }
            is USimpleNameReferenceExpression -> if (expression.identifier == OUTLINE_PROVIDER_PROPERTY) "" else null
            else -> null
        }

        private fun buildReplacement(receiver: String, objectLiteral: UObjectLiteralExpression): ReplacementSpec? {
            val method = objectLiteral.declaration.findMethod(GET_OUTLINE_METHOD) ?: return null
            val bodyExpressions = (method.uastBody as? UBlockExpression)?.expressions.orEmpty()
            val body = bodyExpressions.joinToString("\n") { it.asSourceString().trimEnd() }

            return ReplacementSpec(
                source = buildString {
                    if (receiver.isNotEmpty()) append(receiver).append('.')
                    append("$OUTLINE_PROVIDER_FUNCTION { view, outline ->")
                    if (body.isNotBlank()) append('\n').append(body).append('\n')
                    append('}')
                },
                display = buildString {
                    if (receiver.isNotEmpty()) append(receiver).append('.')
                    append("$OUTLINE_PROVIDER_FUNCTION { view, outline ->")
                    if (body.isNotBlank()) append('\n').append(body).append('\n')
                    append('}')
                }
            )
        }
    }

    private data class ReplacementSpec(
        val source: String,
        val display: String
    )
}
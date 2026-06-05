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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveName
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.USimpleNameReferenceExpression

class TextViewUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val TEXT_VIEW_CLASS = "android.widget.TextView"
        private const val SET_TEXT_COLOR_METHOD = "setTextColor"
        private const val TO_STRING_METHOD = "toString"
        private const val INT_TYPE = "int"

        private const val GET_TEXT_PROPERTY = "text"
        private const val GET_HINT_PROPERTY = "hint"
        private const val GET_TEXT_METHOD = "getText"
        private const val GET_HINT_METHOD = "getHint"
        private const val GET_PREFIX = "get"

        private const val TEXT_COLOR_PROPERTY = "textColor"
        private const val TEXT_TO_STRING_FUNCTION = "textToString"
        private const val HINT_TO_STRING_FUNCTION = "hintToString"

        val ISSUE = Issue.create(
            id = "ReplaceWithTextViewExtension",
            briefDescription = "Use ui-extension's TextView extensions instead.",
            explanation = """
                Using `TextView` text and color related APIs can be simplified by using TextView \
                extensions from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#textview-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#textview-extension

                The `TextView.kt` provides:
                - A direct `textColor` property
                - `textToString()` and `hintToString()` string helpers
                - Less repeated `toString()` conversion code
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                textView.setTextColor(color)
                textView.text.toString()
                textView.text?.toString()
                textView.hint.toString()
                textView.hint?.toString()

                // After
                textView.textColor = color
                textView.textToString()
                textView.hintToString()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                TextViewUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UQualifiedReferenceExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            when (node.methodName) {
                SET_TEXT_COLOR_METHOD -> reportSetTextColor(node)
                TO_STRING_METHOD -> reportToString(node)
            }
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            val selector = node.selector as? UCallExpression ?: return
            if (selector.methodName != TO_STRING_METHOD) return

            reportQualifiedToString(node)
        }

        private fun reportSetTextColor(node: UCallExpression) {
            // Validation is TextView class.
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, TEXT_VIEW_CLASS)) return

            // This is the `textView.setTextColor(@ColorInt int color)` pattern.
            if (method.parameterList.parameters.firstOrNull()?.type?.canonicalText != INT_TYPE) return
            val valueArg = node.valueArguments.firstOrNull() ?: return
            val replacement = "${node.receiverPrefix()}$TEXT_COLOR_PROPERTY = ${valueArg.asSourceString()}"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$TEXT_COLOR_PROPERTY'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.VIEW_PACKAGE}.$TEXT_COLOR_PROPERTY")
                )
            )
        }

        private fun reportToString(node: UCallExpression) {
            reportQualifiedToString(node.receiver as? UQualifiedReferenceExpression ?: return, node)
        }

        private fun reportQualifiedToString(node: UQualifiedReferenceExpression) {
            reportQualifiedToString(node, node)
        }

        private fun reportQualifiedToString(qualified: UQualifiedReferenceExpression, reportNode: Any) {
            val toStringCall = qualified.selector as? UCallExpression ?: return
            if (toStringCall.methodName != TO_STRING_METHOD) return
            val target = qualified.receiver
            val (memberName, receiverPrefix) = when (target) {
                is UQualifiedReferenceExpression -> {
                    val resolvedName = target.selector.resolveName() ?: return
                    resolvedName to target.receiverPrefix()
                }
                is UCallExpression -> {
                    val resolvedName = target.resolveName() ?: return
                    val memberName = when (resolvedName) {
                        GET_TEXT_METHOD -> GET_TEXT_PROPERTY
                        GET_HINT_METHOD -> GET_HINT_PROPERTY
                        else -> return
                    }
                    memberName to target.receiverPrefix()
                }
                is USimpleNameReferenceExpression -> target.identifier to ""
                else -> return
            }
            if (memberName != GET_TEXT_PROPERTY && memberName != GET_HINT_PROPERTY) return
            if (!target.isResolvedTextViewMember(memberName)) return

            // This is the `textView.text/hint.toString()` pattern.
            val replacement = if (memberName == GET_TEXT_PROPERTY)
                "$receiverPrefix$TEXT_TO_STRING_FUNCTION()"
            else "$receiverPrefix$HINT_TO_STRING_FUNCTION()"
            val importTarget = if (memberName == GET_TEXT_PROPERTY) {
                "${DeclaredSymbol.VIEW_PACKAGE}.$TEXT_TO_STRING_FUNCTION"
            } else "${DeclaredSymbol.VIEW_PACKAGE}.$HINT_TO_STRING_FUNCTION"
            val fixName = if (memberName == GET_TEXT_PROPERTY) TEXT_TO_STRING_FUNCTION else HINT_TO_STRING_FUNCTION

            context.report(
                issue = ISSUE,
                location = context.getLocation(reportNode),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = replacement,
                    imports = arrayOf(importTarget)
                )
            )
        }

        private fun UElement.isResolvedTextViewMember(name: String): Boolean {
            val resolved = when (this) {
                is UCallExpression -> resolve()
                is UQualifiedReferenceExpression -> (selector as? UResolvable)?.resolve()
                is USimpleNameReferenceExpression -> resolve()
                else -> null
            } as? PsiMember ?: return false

            return when (resolved) {
                is PsiMethod -> resolved.name == "$GET_PREFIX${name.replaceFirstChar { it.titlecase() }}" &&
                    resolved.isDeclaredInTextViewHierarchy()
                is PsiField -> resolved.name == name &&
                    resolved.isDeclaredInTextViewHierarchy()
                else -> false
            }
        }

        private fun PsiMember.isDeclaredInTextViewHierarchy() =
            containingClass?.let {
                it.qualifiedName == TEXT_VIEW_CLASS || context.evaluator.extendsClass(it, TEXT_VIEW_CLASS, false)
            } == true
    })
}
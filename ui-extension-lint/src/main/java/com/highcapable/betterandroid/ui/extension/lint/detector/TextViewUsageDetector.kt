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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.displayShortName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveName
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression

class TextViewUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val TEXT_VIEW_CLASS = "android.widget.TextView"
        private const val SET_TEXT_COLOR_METHOD = "setTextColor"
        private const val TO_STRING_METHOD = "toString"

        private const val TEXT_COLOR_PROPERTY = "textColor"
        private const val TEXT_TO_STRING_FUNCTION = "textToString"
        private const val HINT_TO_STRING_FUNCTION = "hintToString"

        val ISSUE = Issue.create(
            id = "ReplaceWithTextViewExtension",
            briefDescription = "Use ui-extension's TextView extensions instead.",
            explanation = """
                Using `TextView` text and color related APIs can be simplified by using the \
                `textColor`, `textToString()` and `hintToString()` extensions from BetterAndroid \
                ui-extension library.
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

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

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
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, TEXT_VIEW_CLASS)) return

            val receiver = node.receiver?.asSourceString() ?: return
            val valueArg = node.valueArguments.firstOrNull() ?: return
            val replacement = "$receiver.$TEXT_COLOR_PROPERTY = ${valueArg.asSourceString()}"

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
            val target = qualified.receiver as? UQualifiedReferenceExpression ?: return

            val memberName = target.selector.resolveName() ?: return
            if (memberName != "text" && memberName != "hint") return

            val source = target.receiver.asSourceString()
            val replacement = if (memberName == "text") {
                "$source.$TEXT_TO_STRING_FUNCTION()"
            } else "$source.$HINT_TO_STRING_FUNCTION()"
            val importTarget = if (memberName == "text") {
                "${DeclaredSymbol.VIEW_PACKAGE}.$TEXT_TO_STRING_FUNCTION"
            } else "${DeclaredSymbol.VIEW_PACKAGE}.$HINT_TO_STRING_FUNCTION"

            context.report(
                issue = ISSUE,
                location = context.getLocation(reportNode),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '${replacement.displayShortName()}'",
                    replacement = replacement,
                    imports = arrayOf(importTarget)
                )
            )
        }
    }
}
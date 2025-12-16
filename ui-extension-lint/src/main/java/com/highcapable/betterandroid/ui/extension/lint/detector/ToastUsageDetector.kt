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
 * This file is created by fankes on 2025/12/14.
 */
package com.highcapable.betterandroid.ui.extension.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.ui.extension.lint.DeclaredSymbol
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UQualifiedReferenceExpression

class ToastUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val TOAST_CLASS = "android.widget.Toast"
        private const val TOAST_MAKE_TEXT_METHOD = "makeText"
        private const val TOAST_SHOW_METHOD = "show"

        private const val TOAST_EXTENSION_PACKAGE = "${DeclaredSymbol.BASE_PACKAGE}.view"
        private const val TOAST_EXTENSION_FULL_FUNCTION_NAME = "$TOAST_EXTENSION_PACKAGE.toast"

        val ISSUE = Issue.create(
            id = "ReplaceWithToastExtension",
            briefDescription = "Use ui-extension's `toast(...)` function instead of `Toast.makeText(...).show()`.",
            explanation = "Recommended to use `context.toast(\"Your text\")` instead of `Toast.makeText(context, \"Your text\", Toast.LENGTH_SHORT).show()`.",
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ToastUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != TOAST_SHOW_METHOD) return

            val makeTextCall = when (val receiver = node.receiver) {
                is UCallExpression -> receiver
                is UQualifiedReferenceExpression -> receiver.selector as? UCallExpression
                else -> null
            } ?: return

            if (makeTextCall.methodName != TOAST_MAKE_TEXT_METHOD) return

            // Validation is Toast class.
            val method = node.resolve() ?: return
            val containingClass = method.containingClass ?: return
            if (containingClass.qualifiedName != TOAST_CLASS) return

            val receiverMethod = makeTextCall.resolve() ?: return
            val receiverClass = receiverMethod.containingClass ?: return
            if (receiverClass.qualifiedName != TOAST_CLASS) return

            // This is the `Toast.makeText(...).show()` pattern.
            val arguments = makeTextCall.valueArguments
            if (arguments.size < 2) return

            val contextArg = arguments[0]
            val messageArg = arguments[1]
            val durationArg = arguments.getOrNull(2)

            val contextText = contextArg.asSourceString()
            val messageText = messageArg.asSourceString()
            val durationText = durationArg?.asSourceString()

            val fix = createLintFix(contextText, messageText, durationText)
            val replaceSuggestion = fix.first
            val message = "Can be replaced with `$replaceSuggestion`."

            val location = context.getLocation(node)
            context.report(
                issue = ISSUE,
                location = location,
                message = message,
                quickfixData = fix.second
            )
        }
    }

    private fun createLintFix(contextText: String, messageText: String, durationText: String?): Pair<String, LintFix> {
        // Determine whether to use `context.toast(...)` or direct `toast(...)`.
        val replacement = when {
            contextText == "this" -> {
                // If context is `this`, can use `toast(...)` directly.
                if (durationText == null || durationText == "Toast.LENGTH_SHORT") 
                    "toast($messageText)"
                else "toast($messageText, $durationText)"
            }
            else -> {
                // Otherwise use `context.toast(...)`.
                if (durationText == null || durationText == "Toast.LENGTH_SHORT")
                    "$contextText.toast($messageText)"
                else "$contextText.toast($messageText, $durationText)"
            }
        }

        return replacement to LintFix.create()
            .name("Replace with 'toast'")
            .replace()
            .all()
            .with(replacement)
            .reformat(true)
            .shortenNames()
            .imports(TOAST_EXTENSION_FULL_FUNCTION_NAME)
            .build()
    }
}
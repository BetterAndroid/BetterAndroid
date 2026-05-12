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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveName
import com.intellij.psi.util.PsiTypesUtil
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UQualifiedReferenceExpression

class ViewImeVisibilityUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val VIEW_CLASS = "android.view.View"

        private const val INPUT_METHOD_MANAGER_CLASS = "android.view.inputmethod.InputMethodManager"
        private const val WINDOW_INSETS_CONTROLLER_CLASS = "android.view.WindowInsetsController"
        private const val WINDOW_INSETS_CONTROLLER_COMPAT_CLASS = "androidx.core.view.WindowInsetsControllerCompat"

        private const val SHOW_SOFT_INPUT_METHOD = "showSoftInput"
        private const val SHOW_SOFT_INPUT_FROM_INPUT_METHOD = "showSoftInputFromInputMethod"
        private const val HIDE_SOFT_INPUT_FROM_WINDOW_METHOD = "hideSoftInputFromWindow"
        private const val HIDE_SOFT_INPUT_FROM_INPUT_METHOD = "hideSoftInputFromInputMethod"
        private const val SHOW_METHOD = "show"
        private const val HIDE_METHOD = "hide"
        private const val GET_WINDOW_INSETS_CONTROLLER_METHOD = "getWindowInsetsController"
        private const val IME_METHOD = "ime"
        private const val WINDOW_TOKEN_PROPERTY = "windowToken"

        private const val SHOW_IME_FUNCTION = "showIme"
        private const val HIDE_IME_FUNCTION = "hideIme"

        private const val SHOW_IME_FULL_NAME = "${DeclaredSymbol.VIEW_PACKAGE}.$SHOW_IME_FUNCTION"
        private const val HIDE_IME_FULL_NAME = "${DeclaredSymbol.VIEW_PACKAGE}.$HIDE_IME_FUNCTION"

        val ISSUE = Issue.create(
            id = "ReplaceWithViewImeExtension",
            briefDescription = "Use ui-extension's `showIme()` or `hideIme()` instead.",
            explanation = """
                Using official IME show and hide APIs can be simplified by using `showIme()` and \
                `hideIme()` from BetterAndroid ui-extension library.

                The `View.kt` provides:
                - Direct IME control APIs on `View`
                - A single entry for `InputMethodManager`, `WindowInsetsController` and compat usage
                - Less window token and controller plumbing
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                inputMethodManager.showSoftInput(view, 0)
                inputMethodManager.showSoftInputFromInputMethod(view.windowToken, 0)
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                inputMethodManager.hideSoftInputFromInputMethod(view.windowToken, 0)
                windowInsetsController.show(WindowInsets.Type.ime())
                windowInsetsController.hide(WindowInsets.Type.ime())

                // After
                view.showIme()
                view.hideIme()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ViewImeVisibilityUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            val methodName = node.methodName ?: return
            if (methodName != SHOW_SOFT_INPUT_METHOD &&
                methodName != SHOW_SOFT_INPUT_FROM_INPUT_METHOD &&
                methodName != HIDE_SOFT_INPUT_FROM_WINDOW_METHOD &&
                methodName != HIDE_SOFT_INPUT_FROM_INPUT_METHOD &&
                methodName != SHOW_METHOD &&
                methodName != HIDE_METHOD
            ) return

            val method = node.resolve() ?: return
            val isInputMethodManager = context.evaluator.isMemberInClass(method, INPUT_METHOD_MANAGER_CLASS)
            val isWindowInsetsController = context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_CLASS)
            val isWindowInsetsControllerCompat = context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_COMPAT_CLASS)
            if (!isInputMethodManager && !isWindowInsetsController && !isWindowInsetsControllerCompat) return

            val replacement = when {
                isInputMethodManager && methodName == SHOW_SOFT_INPUT_METHOD ->
                    resolveTargetViewText(node)?.let { "$it.$SHOW_IME_FUNCTION()" }
                isInputMethodManager && methodName == SHOW_SOFT_INPUT_FROM_INPUT_METHOD ->
                    resolveTargetViewFromWindowTokenText(node)?.let { "$it.$SHOW_IME_FUNCTION()" }
                isInputMethodManager && methodName == HIDE_SOFT_INPUT_FROM_WINDOW_METHOD ->
                    resolveTargetViewText(node)?.let { "$it.$HIDE_IME_FUNCTION()" }
                isInputMethodManager && methodName == HIDE_SOFT_INPUT_FROM_INPUT_METHOD ->
                    resolveTargetViewFromWindowTokenText(node)?.let { "$it.$HIDE_IME_FUNCTION()" }
                (isWindowInsetsController || isWindowInsetsControllerCompat) && methodName == SHOW_METHOD ->
                    resolveReceiverViewText(node)?.let { "$it.$SHOW_IME_FUNCTION()" }
                (isWindowInsetsController || isWindowInsetsControllerCompat) && methodName == HIDE_METHOD ->
                    resolveReceiverViewText(node)?.let { "$it.$HIDE_IME_FUNCTION()" }
                else -> null
            }

            val message = when (replacement) {
                null -> "Can be replaced with `$SHOW_IME_FUNCTION(...)` or `$HIDE_IME_FUNCTION(...)`."
                else -> "Can be replaced with `$replacement`."
            }

            val quickFix = replacement?.let {
                val importTarget = if (it.endsWith("$SHOW_IME_FUNCTION()")) SHOW_IME_FULL_NAME else HIDE_IME_FULL_NAME
                val fixName = if (it.endsWith("$SHOW_IME_FUNCTION()")) SHOW_IME_FUNCTION else HIDE_IME_FUNCTION

                buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = it,
                    imports = arrayOf(importTarget)
                )
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = message,
                quickfixData = quickFix
            )
        }

        private fun resolveTargetViewText(node: UCallExpression): String? {
            val firstArg = node.valueArguments.firstOrNull() ?: return null
            val psiClass = PsiTypesUtil.getPsiClass(firstArg.getExpressionType()) ?: return null
            return firstArg.asSourceString().takeIf { context.evaluator.extendsClass(psiClass, VIEW_CLASS, false) }
        }

        private fun resolveTargetViewFromWindowTokenText(node: UCallExpression): String? {
            val firstArg = node.valueArguments.firstOrNull() as? UQualifiedReferenceExpression ?: return null
            val selectorName = firstArg.selector.resolveName()
            if (selectorName != WINDOW_TOKEN_PROPERTY) return null

            return firstArg.receiver.asSourceString()
        }

        private fun resolveReceiverViewText(node: UCallExpression): String? {
            val receiver = node.receiver ?: return null
            val resolvedReceiver = receiver as? UQualifiedReferenceExpression

            if (resolvedReceiver != null) {
                val selectorName = resolvedReceiver.selector.resolveName()
                if (selectorName == IME_METHOD) return resolvedReceiver.receiver.asSourceString()

                val selectorCall = resolvedReceiver.selector as? UCallExpression
                if (selectorCall?.methodName == GET_WINDOW_INSETS_CONTROLLER_METHOD)
                    return selectorCall.valueArguments.firstOrNull()?.asSourceString()
            }

            val firstArg = node.valueArguments.firstOrNull() ?: return null
            val psiClass = PsiTypesUtil.getPsiClass(firstArg.getExpressionType())
            if (psiClass != null && context.evaluator.extendsClass(psiClass, VIEW_CLASS, false))
                return firstArg.asSourceString()

            if (node.methodName != SHOW_METHOD && node.methodName != HIDE_METHOD) return null
            if (firstArg !is UCallExpression || firstArg.methodName != IME_METHOD) return null

            return firstArg.receiver?.asSourceString()
        }
    }
}
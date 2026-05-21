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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.asCall
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.displayShortName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiLocalVariable
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.toUElementOfType

class ActivityUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val CONTEXT_CLASS = "android.content.Context"
        private const val FRAGMENT_CLASS = "androidx.fragment.app.Fragment"
        private const val INTENT_CLASS = "android.content.Intent"

        private const val START_ACTIVITY_METHOD = "startActivity"

        val ISSUE = Issue.create(
            id = "ReplaceWithActivityExtension",
            briefDescription = "Use ui-extension's `startActivity<T>(...)` instead.",
            explanation = """
                Using `startActivity(Intent(...))` with an explicit target activity class can be \
                simplified by using `startActivity<T>(...)` from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#activity-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#activity-extension

                The `Activity.kt` provides:
                - A shorter generic activity launch API
                - Consistent usage on both `Context` and `Fragment`
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                context.startActivity(Intent(context, DemoActivity::class.java))
                fragment.startActivity(Intent(context, DemoActivity::class.java))

                // After
                context.startActivity<DemoActivity>()
                fragment.startActivity<DemoActivity>()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ActivityUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != START_ACTIVITY_METHOD) return

            val method = node.resolve() ?: return
            val containingClass = method.containingClass
            val isContext = containingClass?.let { context.evaluator.extendsClass(it, CONTEXT_CLASS, false) } == true
            val isFragment = containingClass?.let { context.evaluator.extendsClass(it, FRAGMENT_CLASS, false) } == true
            if (!isContext && !isFragment) return

            val intentCall = node.valueArguments.firstOrNull().unwrapParenthesized().asCall() ?: return
            if (intentCall.returnType?.canonicalText != INTENT_CLASS) {
                val intentMethod = intentCall.resolve() ?: return
                if (intentMethod.containingClass?.qualifiedName != INTENT_CLASS) return
            }

            val targetClass = resolveTargetActivityClass(intentCall) ?: return
            val receiverPrefix = node.receiverPrefix()
            val replacement = "$receiverPrefix$START_ACTIVITY_METHOD<$targetClass>()"
            val displayTargetClass = targetClass.displayShortName()
            val displayReplacement = "$receiverPrefix$START_ACTIVITY_METHOD<$displayTargetClass>()"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$displayReplacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$START_ACTIVITY_METHOD'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$START_ACTIVITY_METHOD")
                )
            )
        }

        private fun resolveTargetActivityClass(intentCall: UCallExpression) =
            when (val targetArg = intentCall.valueArguments.getOrNull(1).unwrapParenthesized()) {
                is UClassLiteralExpression -> targetArg.type?.canonicalText
                is UQualifiedReferenceExpression -> (targetArg.receiver as? UClassLiteralExpression)?.type?.canonicalText
                is USimpleNameReferenceExpression -> resolveReferencedClassLiteralType(targetArg)
                else -> null
            }

        private fun resolveReferencedClassLiteralType(expression: USimpleNameReferenceExpression): String? {
            val localVariable = when (val resolved = expression.resolve()) {
                is ULocalVariable -> resolved
                is PsiLocalVariable -> resolved.toUElementOfType<ULocalVariable>()
                else -> null
            } ?: return null

            return when (val initializer = localVariable.uastInitializer.unwrapParenthesized()) {
                is UClassLiteralExpression -> initializer.type?.canonicalText
                is UQualifiedReferenceExpression -> (initializer.receiver as? UClassLiteralExpression)?.type?.canonicalText
                else -> null
            }
        }
    }
}
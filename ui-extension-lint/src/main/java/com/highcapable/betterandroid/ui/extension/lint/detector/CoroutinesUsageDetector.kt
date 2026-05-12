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
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

class CoroutinesUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val HANDLER_CLASS = "android.os.Handler"
        private const val POST_DELAYED_METHOD = "postDelayed"
        private const val LAUNCH_METHOD = "launch"
        private const val ASYNC_METHOD = "async"
        private const val LIFECYCLE_SCOPE_PROPERTY = "lifecycleScope"

        private const val LAUNCH_EXTENSION = "launch"
        private const val ASYNC_EXTENSION = "async"
        private const val RUN_DELAYED_EXTENSION = "runDelayed"

        val ISSUE = Issue.create(
            id = "ReplaceWithCoroutinesExtension",
            briefDescription = "Use ui-extension's coroutine extensions instead.",
            explanation = """
                Using `LifecycleOwner.lifecycleScope.launch`, `LifecycleOwner.lifecycleScope.async` \
                or `Handler(...).postDelayed(...)` can be simplified by using coroutine helpers from \
                BetterAndroid ui-extension library.

                The `Coroutines.kt` provides:
                - A shorter coroutine-based delayed task API
                - Direct support for `LifecycleOwner` and `CoroutineScope`
                - Optional coroutine context and dispatcher parameters
                - Better consistency with `launch(...)` and `async(...)` helpers

                Examples:
                ```kotlin
                // Before
                Handler().postDelayed({
                    // Do something.
                }, 500)

                owner.lifecycleScope.launch {
                    // Do something.
                }

                // After
                owner.runDelayed(500) {
                    // Do something.
                }

                owner.launch {
                    // Do something.
                }

                owner.async {
                    // Do something.
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                CoroutinesUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            when (node.methodName) {
                LAUNCH_METHOD, ASYNC_METHOD -> reportLifecycleScopeCall(node)
                POST_DELAYED_METHOD -> reportPostDelayed(node)
            }
        }

        private fun reportLifecycleScopeCall(node: UCallExpression) {
            val receiver = node.receiver ?: return
            val source = node.asSourceString()
            val receiverSource = receiver.asSourceString()
            val methodName = node.methodName ?: return

            val replacementPrefix = when (receiver) {
                is UQualifiedReferenceExpression -> {
                    val selectorName = receiver.selector.resolveName() ?: return
                    if (selectorName != LIFECYCLE_SCOPE_PROPERTY) return
                    "${receiver.receiver.asSourceString()}.$methodName"
                }
                is USimpleNameReferenceExpression -> {
                    if (receiver.identifier != LIFECYCLE_SCOPE_PROPERTY) return
                    methodName
                }
                else -> {
                    val selectorName = receiver.resolveName() ?: return
                    if (selectorName != LIFECYCLE_SCOPE_PROPERTY) return
                    methodName
                }
            }

            val replacement = source.replaceFirst("$receiverSource.$methodName", replacementPrefix)
            val importTarget = if (methodName == LAUNCH_METHOD) {
                "${DeclaredSymbol.COMPONENT_PACKAGE}.$LAUNCH_EXTENSION"
            } else "${DeclaredSymbol.COMPONENT_PACKAGE}.$ASYNC_EXTENSION"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$methodName'",
                    replacement = replacement,
                    imports = arrayOf(importTarget)
                )
            )
        }

        private fun reportPostDelayed(node: UCallExpression) {
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, HANDLER_CLASS)) return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$RUN_DELAYED_EXTENSION(...)`.",
                quickfixData = null
            )
        }
    }
}
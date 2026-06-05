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
 * This file is created by fankes on 2026/5/18.
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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.asCall
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.findContainingStatement
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.findMethod
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.getContainingPsiClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.isObjectLiteralOf
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression

class BackPressedUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val VIEW_CLASS = "android.view.View"
        private const val FRAGMENT_CLASS = "androidx.fragment.app.Fragment"
        private const val COMPONENT_ACTIVITY_CLASS = "androidx.activity.ComponentActivity"
        private const val ON_BACK_PRESSED_CALLBACK_CLASS = "androidx.activity.OnBackPressedCallback"
        private const val ON_BACK_PRESSED_DISPATCHER_CLASS = "androidx.activity.OnBackPressedDispatcher"

        private const val ON_BACK_PRESSED_METHOD = "onBackPressed"
        private const val ON_BACK_PRESSED_DISPATCHER_PROPERTY = "onBackPressedDispatcher"
        private const val ON_BACK_PRESSED_CALLBACK_FUNCTION = "OnBackPressedCallback"
        private const val HANDLE_ON_BACK_PRESSED_METHOD = "handleOnBackPressed"
        private const val ADD_CALLBACK_METHOD = "addCallback"
        private const val BACK_PRESSED_FILE_NAME = "BackPressed.kt"
        private const val TRIGGER_METHOD = "trigger"
        private const val REMOVE_METHOD = "remove"
        private const val IS_ENABLED_PROPERTY = "isEnabled"
        private const val OBJECT_LITERAL_PREFIX = "object : $ON_BACK_PRESSED_CALLBACK_FUNCTION"
        private const val ACTIVITY_PROPERTY = "activity"
        private const val REQUIRE_ACTIVITY_CALL = "requireActivity()"
        private const val ACTIVITY_NOT_NULL_RECEIVER = "$ACTIVITY_PROPERTY!!"
        private const val ACTIVITY_NULLABLE_RECEIVER = "$ACTIVITY_PROPERTY?"
        private const val CONTEXT_PROPERTY = "context"
        private const val THIS_RECEIVER = "this"
        private const val TRUE_VALUE = "true"
        private const val FALSE_VALUE = "false"
        private const val REMOVED_PARAMETER = "removed"
        private const val SAFE_CALL_OPERATOR = "?."
        private const val NULLABLE_SUFFIX = "?"

        val ISSUE = Issue.create(
            id = "ReplaceWithBackPressedExtension",
            briefDescription = "Use ui-extension's back pressed extensions instead.",
            explanation = """
                Using raw `onBackPressedDispatcher` access from `Fragment` or `View`, manual \
                `object : OnBackPressedCallback(...)` creation, or manual back pressed dispatch \
                forwarding can be simplified by using back pressed extensions from BetterAndroid \
                ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#backpressed-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#backpressed-extension

                The `BackPressed.kt` provides:
                - A direct `Fragment.onBackPressedDispatcher` property
                - A direct `View.onBackPressedDispatcher` property
                - A direct `OnBackPressedCallback(...)` function
                - A direct `trigger(dispatcher)` function
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                requireActivity().onBackPressedDispatcher
                activity!!.onBackPressedDispatcher
                activity?.onBackPressedDispatcher
                (context as ComponentActivity).onBackPressedDispatcher
                (context as? FragmentActivity)?.onBackPressedDispatcher
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                        isEnabled = true
                    }
                }
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                        remove()
                    }
                }

                // After
                onBackPressedDispatcher
                OnBackPressedCallback {
                    trigger(onBackPressedDispatcher)
                }
                OnBackPressedCallback {
                    trigger(onBackPressedDispatcher, removed = true)
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                BackPressedUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UQualifiedReferenceExpression::class.java as Class<out UElement>,
        UObjectLiteralExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            reportTriggerLambda(node)
            reportTriggerStatements(node)
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            if (node.selector.resolveName() != ON_BACK_PRESSED_DISPATCHER_PROPERTY) return

            val replacement = when {
                node.isFragmentDispatcherAccess(context) -> ON_BACK_PRESSED_DISPATCHER_PROPERTY
                node.isViewDispatcherAccess(context) -> ON_BACK_PRESSED_DISPATCHER_PROPERTY
                else -> null
            } ?: return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$ON_BACK_PRESSED_DISPATCHER_PROPERTY'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$ON_BACK_PRESSED_DISPATCHER_PROPERTY")
                )
            )
        }

        override fun visitObjectLiteralExpression(node: UObjectLiteralExpression) {
            if (!node.isObjectLiteralOf(ON_BACK_PRESSED_CALLBACK_CLASS)) return
            if (node.sourcePsi?.containingFile?.name == BACK_PRESSED_FILE_NAME) return

            val replacement = resolveOnBackPressedCallbackReplacement(node) ?: return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$ON_BACK_PRESSED_CALLBACK_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$ON_BACK_PRESSED_CALLBACK_FUNCTION")
                )
            )
        }

        private fun reportTriggerLambda(node: UCallExpression) {
            if (node.methodName != ADD_CALLBACK_METHOD && node.methodName != ON_BACK_PRESSED_CALLBACK_FUNCTION) return
            if (node.methodName == ADD_CALLBACK_METHOD &&
                node.valueArguments.any { it.unwrapParenthesized().asCall()?.methodName == ON_BACK_PRESSED_CALLBACK_FUNCTION }
            ) return

            // This is the `isEnabled = false; dispatcher.onBackPressed(); isEnabled = true/remove()` lambda pattern.
            val lambda = node.valueArguments
                .mapNotNull { it.unwrapParenthesized() as? ULambdaExpression }
                .lastOrNull()
                ?: return
            val expressions = when (val body = lambda.body) {
                is UBlockExpression -> body.expressions
                else -> listOf(body)
            }
            if (expressions.size != 3) return

            val previous = expressions[0]
            val statement = expressions[1]
            val next = expressions[2]
            if (!previous.isDisableCurrentBackPressedCallback()) return

            val onBackPressedCall = statement.unwrapParenthesized().asCall() ?: return
            if (onBackPressedCall.methodName != ON_BACK_PRESSED_METHOD) return
            val method = onBackPressedCall.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, ON_BACK_PRESSED_DISPATCHER_CLASS)) return

            val replacement = when {
                next.isEnableCurrentBackPressedCallback() -> onBackPressedCall.resolveTriggerReplacement(context)
                next.isRemoveCurrentBackPressedCallback() -> onBackPressedCall.resolveTriggerReplacement(context, removed = true)
                else -> null
            } ?: return
            val location = context.getRangeLocation(previous, 0, next, 0)

            context.report(
                issue = ISSUE,
                scope = node,
                location = location,
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildTriggerFix(
                    context = context,
                    previous = previous,
                    statement = statement,
                    next = next,
                    replacement = replacement,
                    imports = onBackPressedCall.resolveTriggerImports(context)
                )
            )
        }

        private fun reportTriggerStatements(node: UCallExpression) {
            if (node.methodName != ON_BACK_PRESSED_METHOD) return
            if (node.isInsideOnBackPressedCallbackFactory()) return

            // Validation is OnBackPressedDispatcher class.
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, ON_BACK_PRESSED_DISPATCHER_CLASS)) return

            // This is the `isEnabled = false; dispatcher.onBackPressed(); isEnabled = true/remove()` statement pattern.
            val statement = node.findContainingStatement() ?: return
            val block = statement.uastParent as? UBlockExpression ?: return
            val expressions = block.expressions
            val index = expressions.indexOf(statement)
            if (index <= 0 || index >= expressions.lastIndex) return

            val previous = expressions[index - 1]
            val next = expressions[index + 1]
            if (!previous.isDisableCurrentBackPressedCallback()) return

            val replacement = when {
                next.isEnableCurrentBackPressedCallback() -> node.resolveTriggerReplacement(context)
                next.isRemoveCurrentBackPressedCallback() -> node.resolveTriggerReplacement(context, removed = true)
                else -> null
            } ?: return

            context.report(
                issue = ISSUE,
                scope = statement,
                location = context.getRangeLocation(previous, 0, next, 0),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildTriggerFix(
                    context = context,
                    previous = previous,
                    statement = statement,
                    next = next,
                    replacement = replacement,
                    imports = node.resolveTriggerImports(context)
                )
            )
        }

        private fun resolveOnBackPressedCallbackReplacement(node: UObjectLiteralExpression): String? {
            val source = node.sourcePsi?.text ?: return null
            val header = OBJECT_LITERAL_PREFIX
            if (!source.trimStart().startsWith(header)) return null

            val enabled = source
                .substringAfter(header, "")
                .substringBefore("{", "")
                .substringAfter("(", "")
                .substringBefore(")", "")
                .trim()
                .takeUnless { it.isBlank() || it == TRUE_VALUE }
            val method = node.declaration.findMethod(HANDLE_ON_BACK_PRESSED_METHOD) ?: return null
            val body = method.uastBody?.sourcePsi?.text
                ?.removePrefix("{")
                ?.removeSuffix("}")
                ?.trimIndent()
                ?.trim()
                ?: return null

            return buildString {
                append(ON_BACK_PRESSED_CALLBACK_FUNCTION)
                if (enabled != null) append("($enabled)")
                append(" {")
                if (body.isNotEmpty()) append('\n').append(body).append('\n')
                append('}')
            }
        }

        private fun UQualifiedReferenceExpression.isFragmentDispatcherAccess(context: JavaContext): Boolean {
            if (!isInsideClass(context, FRAGMENT_CLASS)) return false
            return fragmentDispatcherReceiverSource() != null
        }

        private fun UQualifiedReferenceExpression.isViewDispatcherAccess(context: JavaContext): Boolean {
            if (!isInsideClass(context, VIEW_CLASS)) return false

            val castExpression = receiver.unwrapParenthesized() as? UBinaryExpressionWithType ?: return false
            if (castExpression.operand.asSourceString() != CONTEXT_PROPERTY) return false

            return castExpression.typeReference?.type.extendsClass(context, COMPONENT_ACTIVITY_CLASS)
        }

        private fun UExpression.isDisableCurrentBackPressedCallback() =
            isCurrentBackPressedExpression("$IS_ENABLED_PROPERTY = $FALSE_VALUE")

        private fun UExpression.isEnableCurrentBackPressedCallback() =
            isCurrentBackPressedExpression("$IS_ENABLED_PROPERTY = $TRUE_VALUE")

        private fun UExpression.isRemoveCurrentBackPressedCallback() =
            isCurrentBackPressedExpression("$REMOVE_METHOD()")

        private fun UElement.isInsideClass(context: JavaContext, qualifiedName: String) =
            getContainingPsiClass()?.let { context.evaluator.extendsClass(it, qualifiedName, false) } == true

        private fun UElement.isInsideFragment(context: JavaContext) = isInsideClass(context, FRAGMENT_CLASS)

        private fun UCallExpression.resolveTriggerReplacement(context: JavaContext, removed: Boolean = false): String? {
            val dispatcher = normalizeDispatcherSource(context) ?: return null
            return if (removed) "$TRIGGER_METHOD($dispatcher, $REMOVED_PARAMETER = $TRUE_VALUE)" else "$TRIGGER_METHOD($dispatcher)"
        }

        private fun UCallExpression.resolveTriggerImports(context: JavaContext): Set<String> = buildSet {
            add("${DeclaredSymbol.COMPONENT_PACKAGE}.$TRIGGER_METHOD")
            if (normalizeDispatcherSource(context) == ON_BACK_PRESSED_DISPATCHER_PROPERTY &&
                isInsideFragment(context)
            ) add("${DeclaredSymbol.COMPONENT_PACKAGE}.$ON_BACK_PRESSED_DISPATCHER_PROPERTY")
        }

        private fun UCallExpression.normalizeDispatcherSource(context: JavaContext): String? {
            val receiver = receiver?.asSourceString()?.trim().orEmpty()
            return normalizeDispatcherSource(context, receiver)
        }

        private fun UElement.normalizeDispatcherSource(context: JavaContext, dispatcherSource: String): String? {
            val receiver = dispatcherSource.trim()
            if (receiver.isBlank() || receiver.contains(SAFE_CALL_OPERATOR)) return null
            if (receiver == ON_BACK_PRESSED_DISPATCHER_PROPERTY) return ON_BACK_PRESSED_DISPATCHER_PROPERTY

            if (isInsideFragment(context) && receiver in setOf(REQUIRE_ACTIVITY_CALL, ACTIVITY_NOT_NULL_RECEIVER))
                return ON_BACK_PRESSED_DISPATCHER_PROPERTY

            return receiver
        }

        private fun UQualifiedReferenceExpression.fragmentDispatcherReceiverSource(): String? {
            val source = asSourceString().trim()
            when {
                source.startsWith("$REQUIRE_ACTIVITY_CALL.$ON_BACK_PRESSED_DISPATCHER_PROPERTY") -> return REQUIRE_ACTIVITY_CALL
                source.startsWith("$ACTIVITY_NOT_NULL_RECEIVER.$ON_BACK_PRESSED_DISPATCHER_PROPERTY") -> return ACTIVITY_NOT_NULL_RECEIVER
                source.startsWith("$ACTIVITY_NULLABLE_RECEIVER.$ON_BACK_PRESSED_DISPATCHER_PROPERTY") -> return ACTIVITY_NULLABLE_RECEIVER
            }

            val receiverSource = receiver.unwrapParenthesized()?.asSourceString()?.trim()?.removeSuffix(NULLABLE_SUFFIX) ?: return null
            return receiverSource.takeIf {
                it == REQUIRE_ACTIVITY_CALL || it == ACTIVITY_NOT_NULL_RECEIVER || it == ACTIVITY_NULLABLE_RECEIVER
            }
        }

        private fun UExpression.isCurrentBackPressedExpression(expected: String): Boolean {
            val source = unwrapParenthesized()?.asSourceString()?.trim() ?: return false
            return source == expected || source == "$THIS_RECEIVER.$expected"
        }

        private fun UElement.isInsideOnBackPressedCallbackFactory(): Boolean {
            var current = uastParent
            while (current != null) {
                if (current is UCallExpression && current.methodName == ON_BACK_PRESSED_CALLBACK_FUNCTION) return true
                current = current.uastParent
            }
            return false
        }

        private fun buildTriggerFix(
            context: JavaContext,
            previous: UExpression,
            statement: UExpression,
            next: UExpression,
            replacement: String,
            imports: Set<String>
        ): LintFix {
            val previousPsi = previous.sourcePsi
            val statementPsi = statement.sourcePsi
            val nextPsi = next.sourcePsi

            if (previousPsi == null || statementPsi == null || nextPsi == null)
                return LintFix.create()
                    .name("Replace with '$TRIGGER_METHOD'")
                    .replace()
                    .all()
                    .with(replacement)
                    .reformat(true)
                    .shortenNames()
                    .imports(*imports.toTypedArray())
                    .build()

            val deletePreviousFix = LintFix.create()
                .replace()
                .range(context.getLocation(previousPsi))
                .with("")
                .reformat(true)
                .build()

            val replaceStatementFix = LintFix.create()
                .replace()
                .range(context.getLocation(statementPsi))
                .with(replacement)
                .reformat(true)
                .shortenNames()
                .imports(*imports.toTypedArray())
                .build()

            val deleteNextFix = LintFix.create()
                .replace()
                .range(context.getLocation(nextPsi))
                .with("")
                .reformat(true)
                .build()

            return LintFix.create()
                .name("Replace with '$TRIGGER_METHOD'")
                .composite(deletePreviousFix, replaceStatementFix, deleteNextFix)
        }
    })
}
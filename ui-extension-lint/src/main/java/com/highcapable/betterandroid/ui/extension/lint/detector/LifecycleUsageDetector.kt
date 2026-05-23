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
 * This file is created by fankes on 2026/5/23.
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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.findMethod
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.isObjectLiteralOf
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UNamedExpression
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.toUElementOfType

class LifecycleUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val LIFECYCLE_CLASS = "androidx.lifecycle.Lifecycle"
        private const val DEFAULT_LIFECYCLE_OBSERVER_CLASS = "androidx.lifecycle.DefaultLifecycleObserver"
        private const val LIFECYCLE_EVENT_OBSERVER_CLASS = "androidx.lifecycle.LifecycleEventObserver"

        private const val LIFECYCLE_FILE_NAME = "Lifecycle.kt"
        private const val LIFECYCLE_UTILS_CLASS = "${DeclaredSymbol.COMPONENT_PACKAGE}.LifecycleUtils"
        private const val ADD_OBSERVER_METHOD = "addObserver"
        private const val DEFAULT_LIFECYCLE_OBSERVER_FUNCTION = "DefaultLifecycleObserver"
        private const val LIFECYCLE_EVENT_OBSERVER_FUNCTION = "LifecycleEventObserver"
        private const val ON_STATE_CHANGED_METHOD = "onStateChanged"
        private const val SOURCE_PARAMETER = "source"
        private const val EVENT_PARAMETER = "event"
        private const val OWNER_PARAMETER = "owner"

        private val DEFAULT_EVENTS = listOf("onCreate", "onStart", "onResume", "onPause", "onStop", "onDestroy")

        val ISSUE = Issue.create(
            id = "ReplaceWithLifecycleExtension",
            briefDescription = "Use ui-extension's lifecycle observer extensions instead.",
            explanation = """
                Using raw `Lifecycle.addObserver(...)`, `object : DefaultLifecycleObserver`, or \
                `object : LifecycleEventObserver` can be simplified by using lifecycle observer \
                extensions from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#lifecycle-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#lifecycle-extension

                The `Lifecycle.kt` provides:
                - A direct `DefaultLifecycleObserver(...)` function
                - A direct `LifecycleEventObserver(...)` function
                - Direct `Lifecycle.addObserver(...)` lambda overloads
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onCreate(owner: LifecycleOwner) {
                        // Some code here.
                    }
                })

                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        // Some code here.
                    }
                })

                // After
                lifecycle.addObserver(
                    onCreate = { owner ->
                        // Some code here.
                    }
                )

                lifecycle.addObserver { source, event ->
                    // Some code here.
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                LifecycleUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UObjectLiteralExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != ADD_OBSERVER_METHOD) return
            if (!node.isLifecycleAddObserverCall(context)) return

            // This is the `lifecycle.addObserver(...)` pattern.
            val observer = node.valueArguments.firstOrNull()?.unwrapParenthesized() ?: return
            val replacement = when (observer) {
                is UObjectLiteralExpression -> observer.resolveLifecycleObserverObjectReplacement(node)
                is UCallExpression -> observer.resolveLifecycleObserverFactoryReplacement(node)
                is UQualifiedReferenceExpression -> observer.asCall()?.resolveLifecycleObserverFactoryReplacement(node)
                else -> null
            } ?: observer.resolveLifecycleObserverByTypeReplacement(node, context) ?: return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${replacement.display}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$ADD_OBSERVER_METHOD'",
                    replacement = replacement.source,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$ADD_OBSERVER_METHOD")
                )
            )
        }

        private fun UCallExpression.isLifecycleAddObserverCall(context: JavaContext): Boolean {
            val method = resolve() ?: return false
            if (context.evaluator.isMemberInClass(method, LIFECYCLE_CLASS)) return true
            return receiver?.getExpressionType().extendsClass(context, LIFECYCLE_CLASS)
        }

        override fun visitObjectLiteralExpression(node: UObjectLiteralExpression) {
            if (node.sourcePsi?.containingFile?.name == LIFECYCLE_FILE_NAME) return

            val replacement = node.resolveLifecycleObserverFactoryReplacement() ?: return
            node.reportEnclosingAddObserverReplacement(context)

            // This is the `object : DefaultLifecycleObserver/LifecycleEventObserver` pattern.
            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '${replacement.substringBefore('(')}'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.${replacement.substringBefore('(')}")
                )
            )
        }

        private fun UObjectLiteralExpression.reportEnclosingAddObserverReplacement(context: JavaContext) {
            if (!isObjectLiteralOf(DEFAULT_LIFECYCLE_OBSERVER_CLASS)) return
            val call = findEnclosingAddObserverCall() ?: return
            if (!call.isLifecycleAddObserverCall(context)) return
            val replacement = resolveLifecycleObserverObjectReplacement(call) ?: return

            context.report(
                issue = ISSUE,
                location = context.getLocation(call),
                message = "Can be replaced with `${replacement.display}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$ADD_OBSERVER_METHOD'",
                    replacement = replacement.source,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$ADD_OBSERVER_METHOD")
                )
            )
        }

        private fun UElement.findEnclosingAddObserverCall(): UCallExpression? {
            var current = uastParent
            while (current != null) {
                if (current is UCallExpression && current.methodName == ADD_OBSERVER_METHOD) return current
                current = current.uastParent
            }
            return null
        }

        private fun UCallExpression.resolveLifecycleObserverFactoryReplacement(addObserverCall: UCallExpression): ReplacementSpec? {
            val functionName = methodName ?: return null
            val method = resolve() ?: return null
            val isLibraryFactory = method.containingClass?.qualifiedName == LIFECYCLE_UTILS_CLASS

            return when (functionName) {
                DEFAULT_LIFECYCLE_OBSERVER_FUNCTION -> {
                    if (!isLibraryFactory) return null
                    buildDefaultLifecycleAddObserverReplacement(
                        addObserverCall,
                        valueArguments.mapIndexed { index, argument ->
                            argument.asNamedArgumentSource(DEFAULT_EVENTS.getOrNull(index))
                        }
                    )
                }
                LIFECYCLE_EVENT_OBSERVER_FUNCTION -> {
                    if (!isLibraryFactory && !returnType.extendsClass(context, LIFECYCLE_EVENT_OBSERVER_CLASS)) return null
                    buildEventLifecycleAddObserverReplacement(
                        addObserverCall,
                        valueArguments.firstOrNull()?.asLambdaSource() ?: return null
                    )
                }
                else -> null
            }
        }

        private fun UElement.resolveLifecycleObserverByTypeReplacement(
            addObserverCall: UCallExpression,
            context: JavaContext
        ): ReplacementSpec? {
            val type = (this as? UExpression)?.getExpressionType()
            return when {
                type.extendsClass(context, DEFAULT_LIFECYCLE_OBSERVER_CLASS) ->
                    buildDefaultLifecycleAddObserverReplacement(addObserverCall, emptyList())
                type.extendsClass(context, LIFECYCLE_EVENT_OBSERVER_CLASS) ->
                    buildEventLifecycleAddObserverReplacement(addObserverCall, "{ $SOURCE_PARAMETER, $EVENT_PARAMETER ->\n}")
                else -> null
            }
        }

        private fun UObjectLiteralExpression.resolveLifecycleObserverObjectReplacement(addObserverCall: UCallExpression): ReplacementSpec? {
            val factory = resolveLifecycleObserverFactoryReplacement() ?: return null
            return when {
                factory.startsWith(DEFAULT_LIFECYCLE_OBSERVER_FUNCTION) -> {
                    val arguments = factory.substringAfter("(", "").substringBeforeLast(")")
                        .takeIf { it.isNotBlank() }
                        ?.splitTopLevelArguments()
                        ?: emptyList()
                    if (arguments.isEmpty()) return null
                    buildDefaultLifecycleAddObserverReplacement(addObserverCall, arguments)
                }
                factory.startsWith(LIFECYCLE_EVENT_OBSERVER_FUNCTION) -> buildEventLifecycleAddObserverReplacement(
                    addObserverCall,
                    factory.removePrefix(LIFECYCLE_EVENT_OBSERVER_FUNCTION).trim()
                )
                else -> null
            }
        }

        private fun UObjectLiteralExpression.resolveLifecycleObserverFactoryReplacement(): String? {
            if (isObjectLiteralOf(DEFAULT_LIFECYCLE_OBSERVER_CLASS)) return resolveDefaultLifecycleObserverFactory()
            if (isObjectLiteralOf(LIFECYCLE_EVENT_OBSERVER_CLASS)) return resolveEventLifecycleObserverFactory()
            return null
        }

        private fun UObjectLiteralExpression.resolveDefaultLifecycleObserverFactory(): String? {
            val eventArguments = DEFAULT_EVENTS.mapNotNull { eventName ->
                val method = declaration.findMethod(eventName) ?: return@mapNotNull null
                val body = method.resolveLifecycleMethodBody(eventName)
                "$eventName = { $OWNER_PARAMETER ->${body.asLambdaBlock()}}"
            }
            if (eventArguments.isEmpty()) return null

            return buildString {
                append(DEFAULT_LIFECYCLE_OBSERVER_FUNCTION)
                append("(\n")
                eventArguments.forEachIndexed { index, argument ->
                    append("    ").append(argument)
                    if (index != eventArguments.lastIndex) append(',')
                    append('\n')
                }
                append(')')
            }
        }

        private fun UObjectLiteralExpression.resolveEventLifecycleObserverFactory(): String? {
            val method = declaration.findMethod(ON_STATE_CHANGED_METHOD) ?: return null
            val body = method.resolveLifecycleMethodBody(ON_STATE_CHANGED_METHOD)
            return "$LIFECYCLE_EVENT_OBSERVER_FUNCTION { $SOURCE_PARAMETER, $EVENT_PARAMETER ->${body.asLambdaBlock()}}"
        }

        private fun buildDefaultLifecycleAddObserverReplacement(
            addObserverCall: UCallExpression,
            arguments: List<String>
        ): ReplacementSpec? {
            val receiver = addObserverCall.receiverPrefix()
            if (arguments.isEmpty()) return null

            val source = buildString {
                append(receiver).append(ADD_OBSERVER_METHOD).append("(\n")
                arguments.forEachIndexed { index, argument ->
                    append("    ").append(argument.trim())
                    if (index != arguments.lastIndex) append(',')
                    append('\n')
                }
                append(')')
            }
            return ReplacementSpec(source, source)
        }

        private fun buildEventLifecycleAddObserverReplacement(
            addObserverCall: UCallExpression,
            lambda: String
        ): ReplacementSpec {
            val receiver = addObserverCall.receiverPrefix()
            val source = "$receiver$ADD_OBSERVER_METHOD $lambda"
            return ReplacementSpec(source, source)
        }

        private fun UElement.asNamedArgumentSource(fallbackName: String?) = when (this) {
            is UNamedExpression -> "${name ?: fallbackName} = ${expression.asSourceString()}"
            else -> fallbackName?.let { "$it = ${asSourceString()}" } ?: asSourceString()
        }

        private fun UElement.asLambdaSource() = when (this) {
            is ULambdaExpression -> asSourceString()
            else -> asSourceString()
        }

        private fun PsiMethod.resolveLifecycleMethodBody(methodName: String): String {
            val expressions = (toUElementOfType<UMethod>()?.uastBody as? UBlockExpression)?.expressions ?: return ""
            return expressions.resolveLifecycleMethodBody(methodName)
        }

        private fun List<UExpression>.resolveLifecycleMethodBody(methodName: String): String {
            val superCallText = "super.$methodName"
            return filterNot { it.asSourceString().trimStart().startsWith(superCallText) }
                .joinToString("\n") { it.asSourceString().trimEnd() }
        }

        private fun String.asLambdaBlock() = if (isBlank()) "\n" else "\n$this\n"

        private fun String.splitTopLevelArguments(): List<String> {
            val result = mutableListOf<String>()
            var depth = 0
            var start = 0
            forEachIndexed { index, char ->
                when (char) {
                    '(', '{', '[' -> depth++
                    ')', '}', ']' -> depth--
                    ',' -> if (depth == 0) {
                        result += substring(start, index).trim()
                        start = index + 1
                    }
                }
            }
            result += substring(start).trim()
            return result.filter { it.isNotBlank() }
        }
    }

    private data class ReplacementSpec(val source: String, val display: String)
}
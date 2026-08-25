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
 * This file is created by fankes on 2026/8/25.
 */
package com.highcapable.betterandroid.ui.component.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.ui.component.lint.DeclaredSymbol
import com.highcapable.betterandroid.ui.component.lint.detector.extension.asCall
import com.highcapable.betterandroid.ui.component.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.component.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.component.lint.detector.extension.unwrapParenthesized
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UReturnExpression

class NotificationActionUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val NOTIFICATION_BUILDER_CLASS =
            "${DeclaredSymbol.NOTIFICATION_PACKAGE}.NotificationBuilder"
        private const val NOTIFICATION_ACTION_UTILS_CLASS =
            "${DeclaredSymbol.NOTIFICATION_FACTORY_PACKAGE}.NotificationActionUtils"
        private const val NOTIFICATION_ACTION_BUILDER_CLASS = "androidx.core.app.NotificationCompat.Action.Builder"
        private const val REMOTE_INPUT_BUILDER_CLASS = "androidx.core.app.RemoteInput.Builder"

        private const val ADD_ACTION_METHOD = "addAction"
        private const val ADD_INVISIBLE_ACTION_METHOD = "addInvisibleAction"
        private const val ADD_REMOTE_INPUT_METHOD = "addRemoteInput"
        private const val APPLY_METHOD = "apply"
        private const val BUILD_METHOD = "build"

        private const val NOTIFICATION_ACTION_FUNCTION = "NotificationAction"
        private const val NOTIFICATION_REMOTE_INPUT_FUNCTION = "NotificationRemoteInput"
        private const val NOTIFICATION_ACTION_BUILDER_FUNCTION = "NotificationCompat.Action.Builder(...)"
        private const val REMOTE_INPUT_BUILDER_FUNCTION = "RemoteInput.Builder(...)"
        private const val ADD_REMOTE_INPUT_IMPORT =
            "${DeclaredSymbol.NOTIFICATION_FACTORY_PACKAGE}.$ADD_REMOTE_INPUT_METHOD"

        val ISSUE = Issue.create(
            id = "ReplaceWithNotificationAction",
            briefDescription = "Use ui-component's notification action APIs instead.",
            explanation = """
                Using `NotificationCompat.Action.Builder(...)`, `RemoteInput.Builder(...)`, or wrapping a notification \
                action DSL inside another supported DSL can be simplified by using notification action APIs from \
                BetterAndroid ui-component library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-component#notification
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-component#notification

                The `NotificationAction.kt` provides:
                - `NotificationAction(...)`
                - `NotificationRemoteInput(...)`
                - `NotificationCompat.Action.Builder.addRemoteInput(...)`

                The `NotificationBuilder.kt` provides:
                - `NotificationBuilder.addAction(...)`
                - `NotificationBuilder.addInvisibleAction(...)`

                Examples:
                ```kotlin
                // Before
                val remoteInput = RemoteInput.Builder("reply")
                    .setLabel("Reply")
                    .build()
                val action = NotificationCompat.Action.Builder(icon, "Reply", pendingIntent)
                    .addRemoteInput(remoteInput)
                    .build()
                addAction(
                    NotificationAction(icon, "Reply", pendingIntent) {
                        addRemoteInput(
                            NotificationRemoteInput("reply") {
                                setLabel("Reply")
                            }
                        )
                    }
                )

                // After
                val remoteInput = NotificationRemoteInput("reply") {
                    setLabel("Reply")
                }
                val action = NotificationAction(icon, "Reply", pendingIntent) {
                    addRemoteInput(remoteInput)
                }
                addAction(icon, "Reply", pendingIntent) {
                    addRemoteInput("reply") {
                        setLabel("Reply")
                    }
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                NotificationActionUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (reportWrappedDsl(node)) return

            when {
                node.methodName == BUILD_METHOD -> reportBuiltBuilder(node)
                node.resolve()?.isConstructor == true -> reportUnbuiltBuilder(node)
            }
        }

        private fun reportWrappedDsl(node: UCallExpression): Boolean {
            val target = resolveWrappedDslTarget(node) ?: return false
            val factoryCall = node.valueArguments.singleOrNull()?.unwrapParenthesized()?.asCall() ?: return false
            val factory = factoryCall.resolve() ?: return false

            // Validation is a BetterAndroid factory directly wrapped by its corresponding receiver DSL.
            if (factoryCall.methodName != target.factoryFunction ||
                factory.containingClass?.qualifiedName != NOTIFICATION_ACTION_UTILS_CLASS
            ) return false

            val replacement = buildWrappedDslReplacement(target.targetFunction, factoryCall)
            val imports = target.targetImport?.let { arrayOf(it) } ?: emptyArray()

            context.report(
                issue = ISSUE,
                location = context.getCallLocation(node, includeReceiver = false, includeArguments = true),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '${target.targetFunction}'",
                    replacement = replacement,
                    imports = imports
                )
            )
            return true
        }

        private fun resolveWrappedDslTarget(node: UCallExpression): WrappedDslTarget? {
            val method = node.resolve() ?: return null
            val containingClass = method.containingClass?.qualifiedName ?: return null

            return when (containingClass) {
                NOTIFICATION_BUILDER_CLASS if node.methodName == ADD_ACTION_METHOD ->
                    WrappedDslTarget(NOTIFICATION_ACTION_FUNCTION, ADD_ACTION_METHOD)
                NOTIFICATION_BUILDER_CLASS if node.methodName == ADD_INVISIBLE_ACTION_METHOD ->
                    WrappedDslTarget(NOTIFICATION_ACTION_FUNCTION, ADD_INVISIBLE_ACTION_METHOD)
                NOTIFICATION_ACTION_BUILDER_CLASS if node.methodName == ADD_REMOTE_INPUT_METHOD ->
                    WrappedDslTarget(
                        factoryFunction = NOTIFICATION_REMOTE_INPUT_FUNCTION,
                        targetFunction = ADD_REMOTE_INPUT_METHOD,
                        targetImport = ADD_REMOTE_INPUT_IMPORT
                    )
                else -> null
            }
        }

        private fun reportBuiltBuilder(node: UCallExpression) {
            val method = node.resolve() ?: return
            val builderClass = method.containingClass?.qualifiedName ?: return

            // Validation is NotificationCompat.Action.Builder or RemoteInput.Builder class.
            val builderTarget = resolveBuilderTarget(builderClass) ?: return

            // This is the `Builder(...).build()` pattern, including chained setters and `apply` blocks.
            val builderSpec = resolveBuilderSpec(node.receiver, builderClass) ?: return
            val replacement = buildReplacement(builderTarget.targetFunction, builderSpec)

            context.report(
                issue = ISSUE,
                location = context.getCallLocation(node, includeReceiver = true, includeArguments = true),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '${builderTarget.targetFunction}'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.NOTIFICATION_FACTORY_PACKAGE}.${builderTarget.targetFunction}")
                )
            )
        }

        private fun reportUnbuiltBuilder(node: UCallExpression) {
            val constructor = node.resolve() ?: return
            val builderClass = constructor.containingClass?.qualifiedName ?: return

            // Validation is NotificationCompat.Action.Builder or RemoteInput.Builder constructor.
            val builderTarget = resolveBuilderTarget(builderClass) ?: return
            val builderChain = resolveBuilderChain(node, builderClass)
            if (builderChain.hasBuildCall) return

            context.report(
                issue = ISSUE,
                location = builderChain.expression.sourcePsi?.let { context.getLocation(it) }
                    ?: context.getCallLocation(node, includeReceiver = true, includeArguments = true),
                message = "Consider handing `${builderTarget.sourceFunction}` over to BetterAndroid's " +
                    "`${builderTarget.targetFunction}(...)`."
            )
        }

        private fun resolveBuilderTarget(builderClass: String) = when (builderClass) {
            NOTIFICATION_ACTION_BUILDER_CLASS -> BuilderTarget(
                sourceFunction = NOTIFICATION_ACTION_BUILDER_FUNCTION,
                targetFunction = NOTIFICATION_ACTION_FUNCTION
            )
            REMOTE_INPUT_BUILDER_CLASS -> BuilderTarget(
                sourceFunction = REMOTE_INPUT_BUILDER_FUNCTION,
                targetFunction = NOTIFICATION_REMOTE_INPUT_FUNCTION
            )
            else -> null
        }

        private fun resolveBuilderChain(constructor: UCallExpression, builderClass: String): BuilderChain {
            var current: UElement = constructor
            var hasBuildCall = false

            while (true) {
                current = when (val parent = current.uastParent) {
                    is UQualifiedReferenceExpression -> {
                        val selectorCall = parent.selector.asCall()
                        if (selectorCall?.methodName == BUILD_METHOD &&
                            selectorCall.resolve()?.containingClass?.qualifiedName == builderClass
                        ) hasBuildCall = true
                        parent
                    }
                    is UParenthesizedExpression -> parent
                    else -> return BuilderChain(current, hasBuildCall)
                }
            }
        }

        private fun resolveBuilderSpec(expression: UExpression?, builderClass: String): BuilderSpec? {
            val target = expression.unwrapParenthesized() ?: return null
            val call = target.asCall()
            val method = call?.resolve()
            if (method?.isConstructor == true && method.containingClass?.qualifiedName == builderClass)
                return BuilderSpec(call.valueArguments.map { it.asSourceString().trim() })

            if (target is UQualifiedReferenceExpression) {
                val selectorCall = target.selector.asCall() ?: return null
                val base = resolveBuilderSpec(target.receiver, builderClass) ?: return null
                return appendBuilderCall(base, selectorCall, builderClass)
            }

            val receiver = call?.receiver ?: return null
            val base = resolveBuilderSpec(receiver, builderClass) ?: return null
            return appendBuilderCall(base, call, builderClass)
        }

        private fun appendBuilderCall(base: BuilderSpec, call: UCallExpression, builderClass: String): BuilderSpec? {
            if (call.methodName == APPLY_METHOD) {
                val statements = parseBuilderStatements(call.valueArguments.lastOrNull()) ?: return null
                return base.copy(bodyStatements = base.bodyStatements + statements)
            }

            val method = call.resolve() ?: return null
            if (method.containingClass?.qualifiedName != builderClass) return null

            val statement = call.asSourceString().normalizeBuilderStatement()
            return base.copy(bodyStatements = base.bodyStatements + statement)
        }

        private fun parseBuilderStatements(argument: UExpression?): List<String>? {
            val lambda = argument.unwrapParenthesized() as? ULambdaExpression ?: return null
            val expressions = when (val body = lambda.body) {
                is UBlockExpression -> body.expressions
                else -> listOf(body)
            }

            return expressions.mapNotNull { expression ->
                val content = when (expression) {
                    is UReturnExpression -> expression.returnExpression ?: return@mapNotNull null
                    else -> expression
                }
                content.asSourceString().normalizeBuilderStatement().takeIf { it.isNotBlank() }
            }
        }

        private fun String.normalizeBuilderStatement(): String {
            val lines = trim().removeSuffix(";").lines()
            val continuationIndent = lines.drop(1)
                .filter { it.isNotBlank() }
                .minOfOrNull { it.length - it.trimStart().length } ?: 0

            return lines.mapIndexed { index, line ->
                if (index == 0) line.trimStart() else line.drop(continuationIndent)
            }.joinToString("\n").trimEnd()
        }

        private fun buildReplacement(targetFunction: String, builderSpec: BuilderSpec): String {
            val invocation = "$targetFunction(${builderSpec.arguments.joinToString(", ")})"
            if (builderSpec.bodyStatements.isEmpty()) return invocation

            return buildString {
                append(invocation)
                append(" {\n")
                builderSpec.bodyStatements.forEach { append(it.prependIndent("    ")).append('\n') }
                append('}')
            }
        }

        private fun buildWrappedDslReplacement(targetFunction: String, factoryCall: UCallExpression): String {
            val builder = factoryCall.valueArguments.lastOrNull()?.unwrapParenthesized() as? ULambdaExpression
            val arguments = if (builder == null) factoryCall.valueArguments else factoryCall.valueArguments.dropLast(1)
            val invocation = "$targetFunction(${arguments.joinToString(", ") { it.asSourceString().trim() }})"

            return if (builder == null) invocation
            else "$invocation ${builder.asSourceString().normalizeBuilderStatement()}"
        }
    })

    private data class BuilderSpec(
        val arguments: List<String>,
        val bodyStatements: List<String> = emptyList()
    )

    private data class BuilderTarget(
        val sourceFunction: String,
        val targetFunction: String
    )

    private data class WrappedDslTarget(
        val factoryFunction: String,
        val targetFunction: String,
        val targetImport: String? = null
    )

    private data class BuilderChain(
        val expression: UElement,
        val hasBuildCall: Boolean
    )
}
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
import com.highcapable.betterandroid.ui.component.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.component.lint.detector.extension.extendsClass
import org.jetbrains.uast.UCallExpression

class NotificationUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val NOTIFICATION_MANAGER_PROPERTY_IMPORT = "${DeclaredSymbol.NOTIFICATION_FACTORY_PACKAGE}.notificationManager"

        private const val CONTEXT_CLASS = "android.content.Context"
        private const val NOTIFICATION_MANAGER_COMPAT_CLASS = "androidx.core.app.NotificationManagerCompat"
        private const val NOTIFICATION_COMPAT_BUILDER_CLASS = "androidx.core.app.NotificationCompat.Builder"
        private const val NOTIFICATION_BUILDER_CLASS = "android.app.Notification.Builder"
        private const val NOTIFICATION_CHANNEL_COMPAT_BUILDER_CLASS = "androidx.core.app.NotificationChannelCompat.Builder"
        private const val NOTIFICATION_CHANNEL_CLASS = "android.app.NotificationChannel"
        private const val NOTIFICATION_CHANNEL_GROUP_COMPAT_BUILDER_CLASS = "androidx.core.app.NotificationChannelGroupCompat.Builder"
        private const val NOTIFICATION_CHANNEL_GROUP_CLASS = "android.app.NotificationChannelGroup"

        private const val FROM_METHOD = "from"
        private const val NOTIFICATION_MANAGER_PROPERTY = "notificationManager"
        private const val THIS_RECEIVER = "this"

        private const val CREATE_NOTIFICATION_FUNCTION = "createNotification(...)"
        private const val NOTIFICATION_FUNCTION = "Notification(...)"
        private const val NOTIFICATION_CHANNEL_FUNCTION = "NotificationChannel(...)"
        private const val NOTIFICATION_CHANNEL_GROUP_FUNCTION = "NotificationChannelGroup(...)"
        private const val NOTIFICATION_COMPAT_BUILDER_FUNCTION = "NotificationCompat.Builder(...)"
        private const val NOTIFICATION_BUILDER_FUNCTION = "Notification.Builder(...)"
        private const val NOTIFICATION_CHANNEL_COMPAT_BUILDER_FUNCTION = "NotificationChannelCompat.Builder(...)"
        private const val NOTIFICATION_CHANNEL_COMPAT_FUNCTION = "NotificationChannel(...)"
        private const val NOTIFICATION_CHANNEL_GROUP_COMPAT_BUILDER_FUNCTION = "NotificationChannelGroupCompat.Builder(...)"
        private const val NOTIFICATION_CHANNEL_GROUP_COMPAT_FUNCTION = "NotificationChannelGroup(...)"

        val ISSUE = Issue.create(
            id = "ReplaceWithNotificationComponent",
            briefDescription = "Use ui-component's notification APIs instead.",
            explanation = """
                Using `NotificationManagerCompat.from(...)`, `NotificationCompat.Builder`, \
                `Notification.Builder`, `NotificationChannelCompat.Builder`, \
                `NotificationChannel`, `NotificationChannelGroupCompat.Builder`, or \
                `NotificationChannelGroup` can be simplified by using notification APIs from \
                BetterAndroid ui-component library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-component#notification
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-component#notification

                The `Notification.kt` provides:
                - `Context.notificationManager`
                - `Context.createNotification(...)`
                - `Notification(...)`
                - `NotificationChannel(...)`
                - `NotificationChannelGroup(...)`

                Examples:
                ```kotlin
                // Before
                val manager = NotificationManagerCompat.from(context)
                val notification = NotificationCompat.Builder(context, "channel_id")
                val channel = NotificationChannelCompat.Builder(
                    "channel_id",
                    NotificationManagerCompat.IMPORTANCE_DEFAULT
                )
                val channelGroup = NotificationChannelGroupCompat.Builder("group_id")

                // After
                val manager = context.notificationManager
                val notification = context.createNotification(
                    channel = NotificationChannel("channel_id") {
                        name = "My Channel"
                    }
                ) {
                    contentTitle = "Hello"
                }
                val manualNotification = Notification(
                    context = context,
                    channel = NotificationChannel("channel_id") {
                        name = "My Channel"
                    }
                ) {
                    contentTitle = "Hello"
                }
                val channel = NotificationChannel("channel_id") {
                    name = "My Channel"
                }
                val channelGroup = NotificationChannelGroup("group_id") {
                    name = "My Group"
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                NotificationUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            when {
                reportNotificationManager(node) -> Unit
                reportHandOverNotificationBuilder(node) -> Unit
                reportHandOverNotificationChannelBuilder(node) -> Unit
                reportHandOverNotificationChannelGroupBuilder(node) -> Unit
            }
        }

        private fun reportNotificationManager(node: UCallExpression): Boolean {
            if (node.methodName != FROM_METHOD) return false

            // Validation is NotificationManagerCompat class.
            val method = node.resolve() ?: return false
            if (!context.evaluator.isMemberInClass(method, NOTIFICATION_MANAGER_COMPAT_CLASS)) return false

            // This is the `NotificationManagerCompat.from(context)` pattern.
            val receiver = node.valueArguments.singleOrNull() ?: return false
            if (!receiver.getExpressionType().extendsClass(context, CONTEXT_CLASS)) return false

            val receiverText = receiver.asSourceString().trim()
            val fix = createNotificationManagerLintFix(receiverText)
            val replaceSuggestion = fix.first
            val message = "Can be replaced with `$replaceSuggestion`."

            val location = context.getLocation(node)
            context.report(
                issue = ISSUE,
                location = location,
                message = message,
                quickfixData = fix.second
            )
            return true
        }

        private fun reportHandOverNotificationBuilder(node: UCallExpression): Boolean {
            val constructor = node.resolve() ?: return false

            // Validation is NotificationCompat.Builder or Notification.Builder class.
            return when {
                context.evaluator.isMemberInClass(constructor, NOTIFICATION_COMPAT_BUILDER_CLASS) -> {
                    reportHandOverNotificationComponent(
                        node = node,
                        source = NOTIFICATION_COMPAT_BUILDER_FUNCTION,
                        target = "$CREATE_NOTIFICATION_FUNCTION or $NOTIFICATION_FUNCTION"
                    )
                    true
                }
                context.evaluator.isMemberInClass(constructor, NOTIFICATION_BUILDER_CLASS) -> {
                    reportHandOverNotificationComponent(
                        node = node,
                        source = NOTIFICATION_BUILDER_FUNCTION,
                        target = "$CREATE_NOTIFICATION_FUNCTION or $NOTIFICATION_FUNCTION"
                    )
                    true
                }
                else -> false
            }
        }

        private fun reportHandOverNotificationChannelBuilder(node: UCallExpression): Boolean {
            val constructor = node.resolve() ?: return false

            // Validation is NotificationChannelCompat.Builder or NotificationChannel class.
            return when {
                context.evaluator.isMemberInClass(constructor, NOTIFICATION_CHANNEL_COMPAT_BUILDER_CLASS) -> {
                    reportHandOverNotificationComponent(
                        node = node,
                        source = NOTIFICATION_CHANNEL_COMPAT_BUILDER_FUNCTION,
                        target = NOTIFICATION_CHANNEL_FUNCTION
                    )
                    true
                }
                context.evaluator.isMemberInClass(constructor, NOTIFICATION_CHANNEL_CLASS) -> {
                    reportHandOverNotificationComponent(
                        node = node,
                        source = NOTIFICATION_CHANNEL_COMPAT_FUNCTION,
                        target = NOTIFICATION_CHANNEL_FUNCTION
                    )
                    true
                }
                else -> false
            }
        }

        private fun reportHandOverNotificationChannelGroupBuilder(node: UCallExpression): Boolean {
            val constructor = node.resolve() ?: return false

            // Validation is NotificationChannelGroupCompat.Builder or NotificationChannelGroup class.
            return when {
                context.evaluator.isMemberInClass(constructor, NOTIFICATION_CHANNEL_GROUP_COMPAT_BUILDER_CLASS) -> {
                    reportHandOverNotificationComponent(
                        node = node,
                        source = NOTIFICATION_CHANNEL_GROUP_COMPAT_BUILDER_FUNCTION,
                        target = NOTIFICATION_CHANNEL_GROUP_FUNCTION
                    )
                    true
                }
                context.evaluator.isMemberInClass(constructor, NOTIFICATION_CHANNEL_GROUP_CLASS) -> {
                    reportHandOverNotificationComponent(
                        node = node,
                        source = NOTIFICATION_CHANNEL_GROUP_COMPAT_FUNCTION,
                        target = NOTIFICATION_CHANNEL_GROUP_FUNCTION
                    )
                    true
                }
                else -> false
            }
        }

        private fun reportHandOverNotificationComponent(node: UCallExpression, source: String, target: String) {
            val message = "Consider handing `$source` over to BetterAndroid's `$target`."
            val location = context.getLocation(node)

            context.report(
                issue = ISSUE,
                location = location,
                message = message
            )
        }
    }

    private fun createNotificationManagerLintFix(receiverText: String) = run {
        // Determine whether to use `notificationManager` or `context.notificationManager`.
        val replacement = if (receiverText == THIS_RECEIVER)
            NOTIFICATION_MANAGER_PROPERTY
        else "$receiverText.$NOTIFICATION_MANAGER_PROPERTY"

        replacement to buildReplaceFix(
            name = "Replace with '$NOTIFICATION_MANAGER_PROPERTY'",
            replacement = replacement,
            imports = arrayOf(NOTIFICATION_MANAGER_PROPERTY_IMPORT)
        )
    }
}
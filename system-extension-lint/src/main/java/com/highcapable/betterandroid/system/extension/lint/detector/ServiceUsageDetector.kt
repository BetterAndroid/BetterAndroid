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
 * This file is created by fankes on 2026/5/13.
 */
package com.highcapable.betterandroid.system.extension.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.system.extension.lint.DeclaredSymbol
import com.highcapable.betterandroid.system.extension.lint.detector.extension.asCall
import com.highcapable.betterandroid.system.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.displayShortName
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapParenthesized
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.UQualifiedReferenceExpression

class ServiceUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val CONTEXT_CLASS = "android.content.Context"
        private const val INTENT_CLASS = "android.content.Intent"

        private const val START_SERVICE_METHOD = "startService"
        private const val START_FOREGROUND_SERVICE_METHOD = "startForegroundService"

        val ISSUE = Issue.create(
            id = "ReplaceWithServiceExtension",
            briefDescription = "Use system-extension's service extensions instead.",
            explanation = """
                Using `startService(Intent(...))` or `startForegroundService(Intent(...))` with an \
                explicit target service class can be simplified by using service extensions from \
                BetterAndroid system-extension library.

                The `Service.kt` provides:
                - Generic service start helpers
                - Matching foreground service start helpers
                - Less explicit `Intent(context, Service::class.java)` boilerplate
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                context.startService(Intent(context, DemoService::class.java))
                context.startForegroundService(Intent(context, DemoService::class.java))

                // After
                context.startService<DemoService>()
                context.startForegroundService<DemoService>()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ServiceUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            val methodName = node.methodName ?: return
            if (methodName != START_SERVICE_METHOD && methodName != START_FOREGROUND_SERVICE_METHOD) return

            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, CONTEXT_CLASS)) return

            val intentCall = node.valueArguments.firstOrNull().unwrapParenthesized().asCall() ?: return
            if (intentCall.returnType?.canonicalText != INTENT_CLASS) {
                val intentMethod = intentCall.resolve() ?: return
                if (intentMethod.containingClass?.qualifiedName != INTENT_CLASS) return
            }

            val receiver = node.receiver?.asSourceString() ?: "this"
            val targetClass = resolveTargetServiceClass(intentCall) ?: return
            val replacement = "$receiver.$methodName<$targetClass>()"
            val displayReplacement = "$receiver.$methodName<${targetClass.displayShortName()}>()"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$displayReplacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$methodName'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$methodName")
                )
            )
        }

        private fun resolveTargetServiceClass(intentCall: UCallExpression) =
            when (val target = intentCall.valueArguments.getOrNull(1).unwrapParenthesized()) {
                is UClassLiteralExpression -> target.type?.canonicalText
                is UQualifiedReferenceExpression -> (target.receiver as? UClassLiteralExpression)?.type?.canonicalText
                else -> null
            }
    }
}
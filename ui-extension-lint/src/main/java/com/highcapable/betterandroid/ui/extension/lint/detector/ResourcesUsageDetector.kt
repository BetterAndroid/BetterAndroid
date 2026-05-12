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
import org.jetbrains.uast.UCallExpression

class ResourcesUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val CONTEXT_COMPAT_CLASS = "androidx.core.content.ContextCompat"
        private const val RESOURCES_COMPAT_CLASS = "androidx.core.content.res.ResourcesCompat"

        private const val GET_DRAWABLE_METHOD = "getDrawable"
        private const val GET_COLOR_METHOD = "getColor"
        private const val GET_COLOR_STATE_LIST_METHOD = "getColorStateList"
        private const val GET_FLOAT_METHOD = "getFloat"
        private const val GET_FONT_METHOD = "getFont"

        private const val GET_DRAWABLE_COMPAT = "getDrawableCompat"
        private const val GET_COLOR_COMPAT = "getColorCompat"
        private const val GET_COLOR_STATE_LIST_COMPAT = "getColorStateListCompat"
        private const val GET_FLOAT_COMPAT = "getFloatCompat"
        private const val GET_FONT_COMPAT = "getFontCompat"

        val ISSUE = Issue.create(
            id = "ReplaceWithResourcesExtension",
            briefDescription = "Use ui-extension's resources compat extensions instead.",
            explanation = """
                Using `ContextCompat` or `ResourcesCompat` resource access APIs can be simplified by \
                using resource compatibility extensions from BetterAndroid ui-extension library.

                The `Resources.kt` provides:
                - Context and Resources based compat access APIs
                - Direct replacements for drawable, color, color state list, float and font lookups
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                ContextCompat.getDrawable(context, drawableId)
                ContextCompat.getColor(context, colorId)
                ContextCompat.getColorStateList(context, colorId)
                ResourcesCompat.getDrawable(resources, drawableId, theme)
                ResourcesCompat.getColor(resources, colorId, theme)
                ResourcesCompat.getColorStateList(resources, colorId, theme)
                ResourcesCompat.getFloat(resources, dimenId)
                ResourcesCompat.getFont(context, fontId)

                // After
                context.getDrawableCompat(drawableId)
                context.getColorCompat(colorId)
                context.getColorStateListCompat(colorId)
                resources.getDrawableCompat(drawableId, theme)
                resources.getColorCompat(colorId, theme)
                resources.getColorStateListCompat(colorId, theme)
                resources.getFloatCompat(dimenId)
                context.getFontCompat(fontId)
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ResourcesUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            val methodName = node.methodName ?: return
            val method = node.resolve() ?: return

            val replacement = when {
                context.evaluator.isMemberInClass(method, CONTEXT_COMPAT_CLASS) ->
                    createContextCompatReplacement(node, methodName)
                context.evaluator.isMemberInClass(method, RESOURCES_COMPAT_CLASS) ->
                    createResourcesCompatReplacement(node, methodName)
                else -> null
            } ?: return
            val fixName = when (methodName) {
                GET_DRAWABLE_METHOD -> GET_DRAWABLE_COMPAT
                GET_COLOR_METHOD -> GET_COLOR_COMPAT
                GET_COLOR_STATE_LIST_METHOD -> GET_COLOR_STATE_LIST_COMPAT
                GET_FLOAT_METHOD -> GET_FLOAT_COMPAT
                GET_FONT_METHOD -> GET_FONT_COMPAT
                else -> return
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${replacement.first}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = replacement.first,
                    imports = replacement.second.toTypedArray()
                )
            )
        }

        private fun createContextCompatReplacement(node: UCallExpression, methodName: String): Pair<String, List<String>>? {
            val arguments = node.valueArguments
            return when (methodName) {
                GET_DRAWABLE_METHOD -> {
                    if (arguments.size < 2) null else
                        "${arguments[0].asSourceString()}.$GET_DRAWABLE_COMPAT(${arguments[1].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_DRAWABLE_COMPAT")
                }
                GET_COLOR_METHOD -> {
                    if (arguments.size < 2) null else
                        "${arguments[0].asSourceString()}.$GET_COLOR_COMPAT(${arguments[1].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_COLOR_COMPAT")
                }
                GET_COLOR_STATE_LIST_METHOD -> {
                    if (arguments.size < 2) null else
                        "${arguments[0].asSourceString()}.$GET_COLOR_STATE_LIST_COMPAT(${arguments[1].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_COLOR_STATE_LIST_COMPAT")
                }
                else -> null
            }
        }

        private fun createResourcesCompatReplacement(node: UCallExpression, methodName: String): Pair<String, List<String>>? {
            val arguments = node.valueArguments
            return when (methodName) {
                GET_DRAWABLE_METHOD -> {
                    if (arguments.size < 3) null else
                        "${arguments[0].asSourceString()}.$GET_DRAWABLE_COMPAT(${arguments[1].asSourceString()}, ${arguments[2].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_DRAWABLE_COMPAT")
                }
                GET_COLOR_METHOD -> {
                    if (arguments.size < 3) null else
                        "${arguments[0].asSourceString()}.$GET_COLOR_COMPAT(${arguments[1].asSourceString()}, ${arguments[2].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_COLOR_COMPAT")
                }
                GET_COLOR_STATE_LIST_METHOD -> {
                    if (arguments.size < 3) null else
                        "${arguments[0].asSourceString()}.$GET_COLOR_STATE_LIST_COMPAT(${arguments[1].asSourceString()}, ${arguments[2].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_COLOR_STATE_LIST_COMPAT")
                }
                GET_FLOAT_METHOD -> {
                    if (arguments.size < 2) null else
                        "${arguments[0].asSourceString()}.$GET_FLOAT_COMPAT(${arguments[1].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_FLOAT_COMPAT")
                }
                GET_FONT_METHOD -> {
                    if (arguments.size < 2) null else
                        "${arguments[0].asSourceString()}.$GET_FONT_COMPAT(${arguments[1].asSourceString()})" to
                            listOf("${DeclaredSymbol.COMPONENT_BASE_PACKAGE}.$GET_FONT_COMPAT")
                }
                else -> null
            }
        }
    }
}
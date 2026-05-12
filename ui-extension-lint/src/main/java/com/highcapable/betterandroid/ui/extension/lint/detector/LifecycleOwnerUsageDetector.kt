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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.displayShortName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind

class LifecycleOwnerUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val ACTIVITY_CLASS = "android.app.Activity"
        private const val CONTEXT_CLASS = "android.content.Context"
        private const val FIND_VIEW_TREE_LIFECYCLE_OWNER_METHOD = "findViewTreeLifecycleOwner"
        private const val LIFECYCLE_OWNER_PROPERTY = "lifecycleOwner"
        private const val ACTIVITY_PROPERTY = "activity"
        private const val CONTEXT_PROPERTY = "context"
        private const val REQUIRE_ACTIVITY_FUNCTION = "requireActivity"
        private const val REQUIRE_CONTEXT_FUNCTION = "requireContext"

        val ISSUE = Issue.create(
            id = "ReplaceWithLifecycleOwnerExtension",
            briefDescription = "Use ui-extension's lifecycle owner extensions instead.",
            explanation = """
                Using `findViewTreeLifecycleOwner()`, `owner as Activity`, `owner as? Activity`,
                `owner as Context` or `owner as? Context` can be simplified by using the lifecycle \
                owner extensions from BetterAndroid ui-extension library.
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                LifecycleOwnerUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UBinaryExpressionWithType::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != FIND_VIEW_TREE_LIFECYCLE_OWNER_METHOD) return

            val receiver = node.receiver?.asSourceString() ?: return
            val replacement = "$receiver.$LIFECYCLE_OWNER_PROPERTY"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$LIFECYCLE_OWNER_PROPERTY'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$LIFECYCLE_OWNER_PROPERTY")
                )
            )
        }

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return

            val operand = node.operand.unwrapParenthesized()?.asSourceString() ?: return
            val castType = node.type
            val isNullableCast = node.operationKind.name == "as?"

            val replacement = when {
                castType.extendsClass(context, ACTIVITY_CLASS) -> createActivityReplacement(node, operand, isNullableCast)
                castType.extendsClass(context, CONTEXT_CLASS) -> createContextReplacement(operand, isNullableCast)
                else -> null
            } ?: return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${replacement.first}`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '${replacement.first.displayShortName()}'",
                    replacement = replacement.first,
                    imports = replacement.second.toTypedArray()
                )
            )
        }

        private fun createActivityReplacement(
            node: UBinaryExpressionWithType,
            operand: String,
            isNullableCast: Boolean
        ): Pair<String, List<String>>? {
            val targetType = node.typeReference?.type?.canonicalText
            return when {
                targetType == null || targetType == ACTIVITY_CLASS -> {
                    val functionName = if (isNullableCast) ACTIVITY_PROPERTY else REQUIRE_ACTIVITY_FUNCTION
                    "$operand.$functionName${if (functionName == ACTIVITY_PROPERTY) "" else "()"}" to
                        listOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$functionName")
                }
                else -> {
                    val functionName = if (isNullableCast) ACTIVITY_PROPERTY else REQUIRE_ACTIVITY_FUNCTION
                    "$operand.$functionName<${node.typeReference?.asSourceString()}>()" to
                        listOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$functionName")
                }
            }
        }

        private fun createContextReplacement(
            operand: String,
            isNullableCast: Boolean
        ) = (if (isNullableCast) "$operand.$CONTEXT_PROPERTY" else "$operand.$REQUIRE_CONTEXT_FUNCTION()") to
            listOf("${DeclaredSymbol.COMPONENT_PACKAGE}.${if (isNullableCast) CONTEXT_PROPERTY else REQUIRE_CONTEXT_FUNCTION}")
    }
}
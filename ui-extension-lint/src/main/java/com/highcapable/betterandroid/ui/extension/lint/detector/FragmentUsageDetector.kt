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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind

class FragmentUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val SUPPORT_FRAGMENT_MANAGER = "supportFragmentManager"
        private const val PARENT_FRAGMENT_MANAGER = "parentFragmentManager"
        private const val CHILD_FRAGMENT_MANAGER = "childFragmentManager"
        private const val FIND_FRAGMENT_BY_ID_METHOD = "findFragmentById"
        private const val FIND_FRAGMENT_BY_TAG_METHOD = "findFragmentByTag"

        private const val FRAGMENT_MANAGER_FUNCTION = "fragmentManager"
        private const val FIND_FRAGMENT_FUNCTION = "findFragment"

        val ISSUE = Issue.create(
            id = "ReplaceWithFragmentExtension",
            briefDescription = "Use ui-extension's fragment manager extensions instead.",
            explanation = """
                Using `supportFragmentManager`, `parentFragmentManager`, `childFragmentManager`,
                `findFragmentById(...) as? T` or `findFragmentByTag(...) as? T` can be simplified by \
                using the fragment manager extensions from BetterAndroid ui-extension library.
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                FragmentUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UQualifiedReferenceExpression::class.java as Class<out UElement>,
        USimpleNameReferenceExpression::class.java as Class<out UElement>,
        UBinaryExpressionWithType::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            val selectorName = node.selector.resolveName() ?: return
            val receiver = node.receiver.asSourceString()
            val replacement = when (selectorName) {
                SUPPORT_FRAGMENT_MANAGER -> "$receiver.$FRAGMENT_MANAGER_FUNCTION()"
                PARENT_FRAGMENT_MANAGER -> "$receiver.$FRAGMENT_MANAGER_FUNCTION(parent = true)"
                CHILD_FRAGMENT_MANAGER -> "$receiver.$FRAGMENT_MANAGER_FUNCTION()"
                else -> return
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '${replacement.displayShortName()}'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$FRAGMENT_MANAGER_FUNCTION")
                )
            )
        }

        override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
            val selectorName = node.resolveName() ?: return
            val replacement = when (selectorName) {
                SUPPORT_FRAGMENT_MANAGER -> "$FRAGMENT_MANAGER_FUNCTION()"
                else -> return
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$replacement'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$FRAGMENT_MANAGER_FUNCTION")
                )
            )
        }

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return

            val isNullableCast = node.operationKind.name == "as?"
            if (!isNullableCast && node.operationKind.name != "as") return

            val call = node.operand.unwrapParenthesized().asCall() ?: return

            val methodName = call.methodName ?: return
            if (methodName != FIND_FRAGMENT_BY_ID_METHOD && methodName != FIND_FRAGMENT_BY_TAG_METHOD) return

            val receiver = call.receiver?.asSourceString() ?: return
            val arg = call.valueArguments.firstOrNull()?.asSourceString() ?: return
            val targetType = node.typeReference?.asSourceString() ?: return

            val replacement = "$receiver.$FIND_FRAGMENT_FUNCTION<$targetType>($arg)${if (isNullableCast) "" else "!!"}"

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$replacement'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$FIND_FRAGMENT_FUNCTION")
                )
            )
        }
    }
}
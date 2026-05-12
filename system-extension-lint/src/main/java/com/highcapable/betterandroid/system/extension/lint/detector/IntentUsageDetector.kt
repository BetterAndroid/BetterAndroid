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
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind

class IntentUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val INTENT_CLASS = "android.content.Intent"
        private const val BUNDLE_CLASS = "android.os.Bundle"

        private const val GET_PARCELABLE_EXTRA_METHOD = "getParcelableExtra"
        private const val GET_PARCELABLE_METHOD = "getParcelable"
        private const val GET_SERIALIZABLE_EXTRA_METHOD = "getSerializableExtra"
        private const val GET_SERIALIZABLE_METHOD = "getSerializable"

        private const val GET_PARCELABLE_EXTRA_COMPAT = "getParcelableExtraCompat"
        private const val GET_PARCELABLE_COMPAT = "getParcelableCompat"
        private const val GET_SERIALIZABLE_EXTRA_COMPAT = "getSerializableExtraCompat"
        private const val GET_SERIALIZABLE_COMPAT = "getSerializableCompat"

        val ISSUE = Issue.create(
            id = "ReplaceWithIntentExtension",
            briefDescription = "Use system-extension's intent extensions instead.",
            explanation = """
                Using official `Intent` and `Bundle` parcelable or serializable access APIs can be
                simplified by using the compat extensions from BetterAndroid system-extension library.
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                IntentUsageDetector::class.java,
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
            reportExplicitTypedCompat(node)
            reportGenericParcelable(node)
        }

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return
            if (node.operationKind.name != "as" && node.operationKind.name != "as?") return

            val call = node.operand.unwrapParenthesized().asCall() ?: return
            val compat = resolveCompat(call) ?: return
            if (call.valueArguments.size != 1) return

            val receiver = call.receiver?.asSourceString() ?: return
            val key = call.valueArguments[0].asSourceString()
            val type = node.typeReference?.asSourceString() ?: return
            val isNullableCast = node.operationKind.name == "as?"

            val replacement = "$receiver.${compat.functionName}<$type>($key)${if (isNullableCast) "" else "!!"}"
            report(node, replacement, compat.importTarget)
        }

        private fun reportExplicitTypedCompat(node: UCallExpression) {
            if (node.valueArguments.size != 2) return
            val compat = resolveCompat(node) ?: return
            val receiver = node.receiver?.asSourceString() ?: return
            val key = node.valueArguments[0].asSourceString()
            val type = resolveClassLiteralType(node.valueArguments[1]) ?: return

            val replacement = "$receiver.${compat.functionName}<$type>($key)"
            val displayReplacement = "$receiver.${compat.functionName}<${type.displayShortName()}>($key)"
            report(node, replacement, compat.importTarget, displayReplacement)
        }

        private fun reportGenericParcelable(node: UCallExpression) {
            if (node.isInsideTypeCast()) return
            val compat = resolveCompat(node) ?: return
            if (compat.functionName != GET_PARCELABLE_EXTRA_COMPAT && compat.functionName != GET_PARCELABLE_COMPAT) return
            if (node.valueArguments.size != 1) return

            val receiver = node.receiver?.asSourceString() ?: return
            val key = node.valueArguments[0].asSourceString()
            val type = node.typeArguments.firstOrNull()?.canonicalText ?: return

            val replacement = "$receiver.${compat.functionName}<$type>($key)"
            val displayReplacement = "$receiver.${compat.functionName}<${type.displayShortName()}>($key)"
            report(node, replacement, compat.importTarget, displayReplacement)
        }

        private fun resolveCompat(node: UCallExpression): CompatTarget? {
            val method = node.resolve() ?: return null
            return when {
                context.evaluator.isMemberInClass(method, INTENT_CLASS) && node.methodName == GET_PARCELABLE_EXTRA_METHOD ->
                    CompatTarget(GET_PARCELABLE_EXTRA_COMPAT)
                context.evaluator.isMemberInClass(method, BUNDLE_CLASS) && node.methodName == GET_PARCELABLE_METHOD ->
                    CompatTarget(GET_PARCELABLE_COMPAT)
                context.evaluator.isMemberInClass(method, INTENT_CLASS) && node.methodName == GET_SERIALIZABLE_EXTRA_METHOD ->
                    CompatTarget(GET_SERIALIZABLE_EXTRA_COMPAT)
                context.evaluator.isMemberInClass(method, BUNDLE_CLASS) && node.methodName == GET_SERIALIZABLE_METHOD ->
                    CompatTarget(GET_SERIALIZABLE_COMPAT)
                else -> null
            }
        }

        private fun resolveClassLiteralType(expression: UElement?) = when (val target = expression.unwrapParenthesized()) {
            is UClassLiteralExpression -> target.type?.canonicalText
            is UQualifiedReferenceExpression -> (target.receiver as? UClassLiteralExpression)?.type?.canonicalText
            else -> null
        }

        private fun UCallExpression.isInsideTypeCast(): Boolean {
            var parent = uastParent
            while (parent is UParenthesizedExpression) parent = parent.uastParent

            return parent is UBinaryExpressionWithType
        }

        private fun report(
            node: UElement,
            replacement: String,
            importTarget: String,
            displayReplacement: String = replacement
        ) = context.report(
            issue = ISSUE,
            location = context.getLocation(node),
            message = "Can be replaced with `$displayReplacement`.",
            quickfixData = buildReplaceFix(
                name = "Replace with '${displayReplacement.displayShortName()}'",
                replacement = replacement,
                imports = arrayOf(importTarget)
            )
        )
    }

    private data class CompatTarget(val functionName: String) {
        val importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$functionName"
    }
}
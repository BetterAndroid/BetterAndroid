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
import com.highcapable.betterandroid.system.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.system.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiLocalVariable
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind
import org.jetbrains.uast.toUElementOfType

class IntentUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val INTENT_CLASS = "android.content.Intent"
        private const val BUNDLE_CLASS = "android.os.Bundle"
        private const val PARCELABLE_CLASS = "android.os.Parcelable"
        private const val SERIALIZABLE_CLASS = "java.io.Serializable"

        private const val GET_PARCELABLE_EXTRA_METHOD = "getParcelableExtra"
        private const val GET_PARCELABLE_METHOD = "getParcelable"
        private const val GET_SERIALIZABLE_EXTRA_METHOD = "getSerializableExtra"
        private const val GET_SERIALIZABLE_METHOD = "getSerializable"

        private const val GET_PARCELABLE_EXTRA_COMPAT = "getParcelableExtraCompat"
        private const val GET_PARCELABLE_COMPAT = "getParcelableCompat"
        private const val GET_SERIALIZABLE_EXTRA_COMPAT = "getSerializableExtraCompat"
        private const val GET_SERIALIZABLE_COMPAT = "getSerializableCompat"

        private const val INTENT_HELPER = "Intent"

        private const val AS_CAST = "as"
        private const val AS_SAFE_CAST = "as?"
        private const val NOT_NULL_ASSERTION = "!!"
        private const val NULLABLE_MARK = "?"

        val ISSUE = Issue.create(
            id = "ReplaceWithIntentExtension",
            briefDescription = "Use system-extension's intent extensions instead.",
            explanation = """
                Using official `Intent` constructors for component targets, or `Intent` and \
                `Bundle` parcelable or serializable access APIs, can be simplified by using \
                extensions from BetterAndroid system-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/system-extension#intent-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/system-extension#intent-extension

                The `Intent.kt` provides:
                - Generic component-targeted `Intent(...)` helpers
                - Generic compat access APIs for `Parcelable` and `Serializable`
                - Consistent usage across both `Intent` and `Bundle`
                - Less version-specific API branching in call sites
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                Intent(context, DemoActivity::class.java)
                Intent(Intent.ACTION_VIEW, uri, context, DemoActivity::class.java)
                intent.getParcelableExtra("parcelable", DemoParcelable::class.java)
                bundle.getParcelable("parcelable", DemoParcelable::class.java)
                intent.getSerializableExtra("serializable", DemoSerializable::class.java)
                bundle.getSerializable("serializable", DemoSerializable::class.java)
                intent.getParcelableExtra("parcelable") as? DemoParcelable
                bundle.getSerializable("serializable") as DemoSerializable

                // After
                Intent<DemoActivity>(context)
                Intent<DemoActivity>(Intent.ACTION_VIEW, uri, context)
                intent.getParcelableExtraCompat<DemoParcelable>("parcelable")
                bundle.getParcelableCompat<DemoParcelable>("parcelable")
                intent.getSerializableExtraCompat<DemoSerializable>("serializable")
                bundle.getSerializableCompat<DemoSerializable>("serializable")
                intent.getParcelableExtraCompat<DemoParcelable>("parcelable")
                bundle.getSerializableCompat<DemoSerializable>("serializable")!!
                ```
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

        private val visitedBinaryExpressionsWithType = hashSetOf<Any>()

        override fun visitCallExpression(node: UCallExpression) {
            reportIntentConstructor(node)
            reportExplicitTypedCompat(node)
            reportGenericParcelable(node)
        }

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            node.sourcePsi?.let { if (!visitedBinaryExpressionsWithType.add(it)) return }
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return
            if (node.operationKind.name != AS_CAST && node.operationKind.name != AS_SAFE_CAST) return

            val call = node.operand.unwrapParenthesized().asCall() ?: return
            val compat = resolveCompat(call) ?: return
            if (call.valueArguments.size != 1) return

            // This is the `getParcelableExtra(...) as/as? Type` pattern.
            val key = call.valueArguments[0].asSourceString()
            if (!node.typeReference?.type.extendsClass(context, compat.requiredClass)) return
            val castType = node.resolveCastType() ?: return
            val isNullableCast = node.operationKind.name == AS_SAFE_CAST || castType.isNullable

            val replacement = "${call.receiverPrefix()}${compat.functionName}<${castType.name}>($key)${if (isNullableCast) "" else NOT_NULL_ASSERTION}"
            reportAndFix(node, replacement, compat.importTarget, fixName = compat.functionName)
        }

        private fun reportExplicitTypedCompat(node: UCallExpression) {
            if (node.valueArguments.size != 2) return
            val compat = resolveCompat(node) ?: return

            // This is the `getParcelableExtra(name, Type::class.java)` pattern.
            val key = node.valueArguments[0].asSourceString()
            val type = resolveClassLiteralType(node.valueArguments[1]) ?: return

            val receiverPrefix = node.receiverPrefix()
            val replacement = "$receiverPrefix${compat.functionName}<$type>($key)"
            val displayReplacement = "$receiverPrefix${compat.functionName}<${type.displayShortName()}>($key)"
            reportAndFix(node, replacement, compat.importTarget, displayReplacement, compat.functionName)
        }

        private fun reportIntentConstructor(node: UCallExpression) {
            val method = node.resolve() ?: return

            // Validation is Intent constructor.
            if (!method.isConstructor || method.containingClass?.qualifiedName != INTENT_CLASS) return

            // This is the `Intent(context, Target::class.java)` or `Intent(action, uri, context, Target::class.java)` pattern.
            val replacement = when (node.valueArguments.size) {
                2 -> {
                    val context = node.valueArguments[0].asSourceString()
                    val type = resolveClassLiteralType(node.valueArguments[1]) ?: return
                    IntentReplacement(
                        replacement = "$INTENT_HELPER<$type>($context)",
                        displayReplacement = "$INTENT_HELPER<${type.displayShortName()}>($context)"
                    )
                }
                4 -> {
                    val action = node.valueArguments[0].asSourceString()
                    val uri = node.valueArguments[1].asSourceString()
                    val context = node.valueArguments[2].asSourceString()
                    val type = resolveClassLiteralType(node.valueArguments[3]) ?: return
                    IntentReplacement(
                        replacement = "$INTENT_HELPER<$type>($action, $uri, $context)",
                        displayReplacement = "$INTENT_HELPER<${type.displayShortName()}>($action, $uri, $context)"
                    )
                }
                else -> return
            }

            reportAndFix(
                node = node,
                replacement = replacement.replacement,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$INTENT_HELPER",
                displayReplacement = replacement.displayReplacement,
                fixName = INTENT_HELPER
            )
        }

        private fun reportGenericParcelable(node: UCallExpression) {
            if (node.isInsideTypeCast()) return
            val compat = resolveCompat(node) ?: return
            if (compat.functionName != GET_PARCELABLE_EXTRA_COMPAT && compat.functionName != GET_PARCELABLE_COMPAT) return
            if (node.valueArguments.size != 1) return

            // This is the legacy generic `getParcelableExtra<Type>(name)` pattern.
            val key = node.valueArguments[0].asSourceString()
            val type = node.typeArguments.firstOrNull()?.canonicalText ?: return

            val receiverPrefix = node.receiverPrefix()
            val replacement = "$receiverPrefix${compat.functionName}<$type>($key)"
            val displayReplacement = "$receiverPrefix${compat.functionName}<${type.displayShortName()}>($key)"
            reportAndFix(node, replacement, compat.importTarget, displayReplacement, compat.functionName)
        }

        private fun resolveCompat(node: UCallExpression): CompatTarget? {
            val method = node.resolve() ?: return null

            // Validation is Intent or Bundle class.
            return when {
                context.evaluator.isMemberInClass(method, INTENT_CLASS) && node.methodName == GET_PARCELABLE_EXTRA_METHOD ->
                    CompatTarget(GET_PARCELABLE_EXTRA_COMPAT, PARCELABLE_CLASS)
                context.evaluator.isMemberInClass(method, BUNDLE_CLASS) && node.methodName == GET_PARCELABLE_METHOD ->
                    CompatTarget(GET_PARCELABLE_COMPAT, PARCELABLE_CLASS)
                context.evaluator.isMemberInClass(method, INTENT_CLASS) && node.methodName == GET_SERIALIZABLE_EXTRA_METHOD ->
                    CompatTarget(GET_SERIALIZABLE_EXTRA_COMPAT, SERIALIZABLE_CLASS)
                context.evaluator.isMemberInClass(method, BUNDLE_CLASS) && node.methodName == GET_SERIALIZABLE_METHOD ->
                    CompatTarget(GET_SERIALIZABLE_COMPAT, SERIALIZABLE_CLASS)
                else -> null
            }
        }

        private fun resolveClassLiteralType(expression: UElement?) = when (val target = expression.unwrapParenthesized()) {
            is UClassLiteralExpression -> target.type?.canonicalText
            is UQualifiedReferenceExpression -> (target.receiver as? UClassLiteralExpression)?.type?.canonicalText
            is USimpleNameReferenceExpression -> resolveReferencedClassLiteralType(target)
            else -> null
        }

        private fun resolveReferencedClassLiteralType(expression: USimpleNameReferenceExpression): String? {
            val localVariable = when (val resolved = expression.resolve()) {
                is ULocalVariable -> resolved
                is PsiLocalVariable -> resolved.toUElementOfType<ULocalVariable>()
                else -> null
            } ?: return null

            return resolveClassLiteralType(localVariable.uastInitializer)
        }

        private fun UCallExpression.isInsideTypeCast(): Boolean {
            var parent = uastParent
            while (parent is UParenthesizedExpression) parent = parent.uastParent

            return parent is UBinaryExpressionWithType
        }

        private fun UBinaryExpressionWithType.resolveCastType(): CastType? {
            val type = typeReference?.asSourceString()?.trim() ?: return null
            val isNullable = type.endsWith(NULLABLE_MARK)

            return CastType(
                name = if (isNullable) type.dropLast(1).trimEnd() else type,
                isNullable = isNullable
            )
        }

        private fun reportAndFix(
            node: UElement,
            replacement: String,
            importTarget: String,
            displayReplacement: String = replacement,
            fixName: String
        ) = context.report(
            issue = ISSUE,
            location = context.getLocation(node),
            message = "Can be replaced with `$displayReplacement`.",
            quickfixData = buildReplaceFix(
                name = "Replace with '$fixName'",
                replacement = replacement,
                imports = arrayOf(importTarget)
            )
        )
    }

    private data class CompatTarget(
        val functionName: String,
        val requiredClass: String
    ) {
        val importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$functionName"
    }

    private data class IntentReplacement(
        val replacement: String,
        val displayReplacement: String
    )

    private data class CastType(
        val name: String,
        val isNullable: Boolean
    )
}
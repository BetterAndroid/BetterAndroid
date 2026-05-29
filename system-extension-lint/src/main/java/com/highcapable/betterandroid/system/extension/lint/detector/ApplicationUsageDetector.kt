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
import com.highcapable.betterandroid.system.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.resolveName
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapParenthesized
import org.jetbrains.uast.UBinaryExpression
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UIfExpression
import org.jetbrains.uast.UPostfixExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UastBinaryOperator
import org.jetbrains.uast.visitor.AbstractUastVisitor

class ApplicationUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val COMPONENT_NAME_CLASS = "android.content.ComponentName"
        private const val PACKAGE_MANAGER_CLASS = "android.content.pm.PackageManager"
        private const val PACKAGE_INFO_COMPAT_CLASS = "androidx.core.content.pm.PackageInfoCompat"

        private const val GET_COMPONENT_NAME = "getComponentName"
        private const val HAS_PACKAGE = "hasPackage"
        private const val HAS_LAUNCH_ACTIVITY = "hasLaunchActivity"
        private const val IS_COMPONENT_ENABLED = "isComponentEnabled"
        private const val ENABLE_COMPONENT = "enableComponent"
        private const val DISABLE_COMPONENT = "disableComponent"
        private const val RESET_COMPONENT = "resetComponent"
        private const val VERSION_CODE_COMPAT = "versionCodeCompat"

        private const val GET_PACKAGE_INFO_METHOD = "getPackageInfo"
        private const val GET_OR_NULL_METHOD = "getOrNull"
        private const val GET_LAUNCH_INTENT_FOR_PACKAGE_METHOD = "getLaunchIntentForPackage"
        private const val GET_COMPONENT_ENABLED_SETTING_METHOD = "getComponentEnabledSetting"
        private const val SET_COMPONENT_ENABLED_SETTING_METHOD = "setComponentEnabledSetting"
        private const val GET_LONG_VERSION_CODE_METHOD = "getLongVersionCode"
        private const val IS_NOT_EMPTY_METHOD = "isNotEmpty"
        private const val QUERY_INTENT_ACTIVITIES_METHOD = "queryIntentActivities"
        private const val RUN_CATCHING_METHOD = "runCatching"
        private const val SIZE_PROPERTY = "size"
        private const val VERSION_CODE_LONG_PROPERTY = "longVersionCode"
        private const val VERSION_CODE_PROPERTY = "versionCode"
        private const val VERSION_CODE_TO_LONG_CALL = "versionCode.toLong()"
        private const val VERSION_CODE_COMPAT_LONG = "long"
        private const val VERSION_CODE_COMPAT_VERSION = "version"
        private const val VERSION_CODE_COMPAT_28 = "28"

        private const val COMPONENT_ENABLED_STATE_DISABLED = "COMPONENT_ENABLED_STATE_DISABLED"
        private const val COMPONENT_ENABLED_STATE_ENABLED = "COMPONENT_ENABLED_STATE_ENABLED"
        private const val COMPONENT_ENABLED_STATE_DEFAULT = "COMPONENT_ENABLED_STATE_DEFAULT"

        private const val BUILD_VERSION_NAME = "Build.VERSION"
        private const val BUILD_SDK_INT_NAME = "$BUILD_VERSION_NAME.SDK_INT"
        private const val BUILD_VERSION_CODES_NAME = "Build.VERSION_CODES"
        private const val ANDROID_VERSION_NAME = "AndroidVersion"
        private const val ANDROID_VERSION_CLASS = "${DeclaredSymbol.UTILS_PACKAGE}.$ANDROID_VERSION_NAME"
        private const val P_FIELD = "P"
        private const val IS_AT_LEAST_METHOD = "isAtLeast"
        private const val IS_GREATER_THAN_METHOD = "isGreaterThan"
        private const val VERSION_CODES_P_QUALIFIED = "$BUILD_VERSION_CODES_NAME.$P_FIELD"
        private const val ANDROID_VERSION_P_QUALIFIED = "$ANDROID_VERSION_NAME.$P_FIELD"

        private const val THIS_EXPRESSION = "this"
        private const val NULL_LITERAL = "null"
        private const val ZERO_LITERAL = "0"
        private const val NOT_NULL_ASSERTION = "!!"

        val ISSUE = Issue.create(
            id = "ReplaceWithApplicationExtension",
            briefDescription = "Use system-extension's application extensions instead.",
            explanation = """
                Using `ComponentName(context, ...)`, `PackageManager` package checks, component \
                state operations, or package version compat wrappers can be simplified by using \
                application extensions from BetterAndroid system-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/system-extension#application-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/system-extension#application-extension

                The `Application.kt` provides:
                - Generic component name creation helpers
                - Package existence and launch activity checks
                - Component state query and update helpers
                - A direct `versionCodeCompat` access API
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                ComponentName(context, DemoActivity::class.java)
                packageManager.getPackageInfo(packageName, 0) != null
                packageManager.getLaunchIntentForPackage(packageName) != null
                packageManager.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
                PackageInfoCompat.getLongVersionCode(packageInfo)

                // After
                context.getComponentName<DemoActivity>()
                packageManager.hasPackage(packageName)
                packageManager.hasLaunchActivity(packageName)
                packageManager.isComponentEnabled(componentName)
                packageManager.disableComponent(componentName, PackageManager.DONT_KILL_APP)
                packageInfo.versionCodeCompat
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ApplicationUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UBinaryExpression::class.java as Class<out UElement>,
        UIfExpression::class.java as Class<out UElement>,
        UQualifiedReferenceExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        private val visitedBinaryExpressions = hashSetOf<Any>()

        override fun visitCallExpression(node: UCallExpression) {
            reportComponentName(node)
            reportSetComponentEnabledSetting(node)
            reportPackageInfoCompat(node)
        }

        override fun visitBinaryExpression(node: UBinaryExpression) {
            node.sourcePsi?.let { if (!visitedBinaryExpressions.add(it)) return }
            reportHasPackage(node)
            reportHasLaunchActivity(node)
            reportIsComponentEnabled(node)
        }

        override fun visitIfExpression(node: UIfExpression) {
            reportVersionCodeCompat(node)
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            reportQueryLaunchActivities(node)
        }

        private fun reportComponentName(node: UCallExpression) {
            val method = node.resolve() ?: return

            // Validation is ComponentName constructor.
            if (method.containingClass?.qualifiedName != COMPONENT_NAME_CLASS) return
            if (node.valueArguments.size < 2) return

            // This is the `ComponentName(context, Target::class.java)` pattern.
            val receiver = node.valueArguments[0].asSourceString().trim()
            val targetClass = resolveClassLiteralType(node.valueArguments[1]) ?: return
            val receiverPrefix = if (receiver == THIS_EXPRESSION) "" else "$receiver."
            val replacement = "$receiverPrefix$GET_COMPONENT_NAME<$targetClass>()"
            val displayReplacement = "$receiverPrefix$GET_COMPONENT_NAME<${targetClass.displayShortName()}>()"

            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                displayReplacement = displayReplacement,
                fixName = GET_COMPONENT_NAME,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$GET_COMPONENT_NAME"
            )
        }

        private fun reportHasPackage(node: UBinaryExpression) {
            if (node.operator != UastBinaryOperator.NOT_EQUALS) return
            if (node.rightOperand.asSourceString() != NULL_LITERAL) return

            val directCall = node.leftOperand.unwrapParenthesized().asCall()
            if (directCall != null && isPackageManagerCall(directCall, GET_PACKAGE_INFO_METHOD)) {
                // This is the `packageManager.getPackageInfo(...) != null` pattern.
                val packageName = directCall.valueArguments.firstOrNull()?.asSourceString() ?: return
                val replacement = "${directCall.receiverPrefix()}$HAS_PACKAGE($packageName)"
                reportAndFix(
                    context = context,
                    node = node,
                    replacement = replacement,
                    fixName = HAS_PACKAGE,
                    importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$HAS_PACKAGE"
                )
                return
            }

            val getOrNullCall = node.leftOperand.unwrapParenthesized().asCall() ?: return
            if (getOrNullCall.methodName != GET_OR_NULL_METHOD) return

            // This is the `runCatching { packageManager.getPackageInfo(...) }.getOrNull() != null` pattern.
            val packageInfoCall = resolveRunCatchingPackageInfoCall(getOrNullCall) ?: return
            val packageName = packageInfoCall.valueArguments.firstOrNull()?.asSourceString() ?: return
            val replacement = "${packageInfoCall.receiverPrefix()}$HAS_PACKAGE($packageName)"

            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                fixName = HAS_PACKAGE,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$HAS_PACKAGE"
            )
        }

        private fun reportHasLaunchActivity(node: UBinaryExpression) {
            if (node.operator == UastBinaryOperator.NOT_EQUALS && node.rightOperand.asSourceString() == NULL_LITERAL) {
                val call = node.leftOperand.unwrapParenthesized().asCall() ?: return

                // This is the `packageManager.getLaunchIntentForPackage(...) != null` pattern.
                if (!isPackageManagerCall(call, GET_LAUNCH_INTENT_FOR_PACKAGE_METHOD)) return
                val packageName = call.valueArguments.firstOrNull()?.asSourceString() ?: return
                val replacement = "${call.receiverPrefix()}$HAS_LAUNCH_ACTIVITY($packageName)"

                reportAndFix(
                    context = context,
                    node = node,
                    replacement = replacement,
                    fixName = HAS_LAUNCH_ACTIVITY,
                    importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$HAS_LAUNCH_ACTIVITY"
                )
                return
            }

            if (node.operator != UastBinaryOperator.GREATER) return
            if (node.rightOperand.asSourceString() != ZERO_LITERAL) return

            val queryAccess = node.leftOperand.unwrapParenthesized() as? UQualifiedReferenceExpression ?: return
            if (queryAccess.selector.resolveName() != SIZE_PROPERTY) return

            // This is the `queryIntentActivities(...).size > 0` pattern.
            val queryCall = queryAccess.receiver.asCall() ?: return
            val (_, packageName) = resolveQueryLaunchActivitiesInfo(queryCall) ?: return
            val replacement = "${queryCall.receiverPrefix()}$HAS_LAUNCH_ACTIVITY($packageName)"

            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                fixName = HAS_LAUNCH_ACTIVITY,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$HAS_LAUNCH_ACTIVITY"
            )
        }

        private fun reportQueryLaunchActivities(node: UQualifiedReferenceExpression) {
            if (node.selector.resolveName() != IS_NOT_EMPTY_METHOD) return

            // This is the `queryIntentActivities(...).isNotEmpty()` pattern.
            val queryCall = node.receiver.asCall() ?: return
            val (_, packageName) = resolveQueryLaunchActivitiesInfo(queryCall) ?: return
            val replacement = "${queryCall.receiverPrefix()}$HAS_LAUNCH_ACTIVITY($packageName)"

            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                fixName = HAS_LAUNCH_ACTIVITY,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$HAS_LAUNCH_ACTIVITY"
            )
        }

        private fun reportIsComponentEnabled(node: UBinaryExpression) {
            val call = node.leftOperand.unwrapParenthesized().asCall() ?: return

            // Validation is PackageManager component state query.
            if (!isPackageManagerCall(call, GET_COMPONENT_ENABLED_SETTING_METHOD)) return

            // This is the `getComponentEnabledSetting(...) ==/!= PackageManager.COMPONENT_ENABLED_STATE_*` pattern.
            val componentName = call.valueArguments.firstOrNull()?.asSourceString() ?: return
            val rightName = node.rightOperand.unwrapParenthesized().resolveName() ?: return

            val isMatch = when (node.operator) {
                UastBinaryOperator.NOT_EQUALS -> rightName == COMPONENT_ENABLED_STATE_DISABLED
                UastBinaryOperator.IDENTITY_EQUALS,
                UastBinaryOperator.EQUALS -> rightName == COMPONENT_ENABLED_STATE_ENABLED
                else -> false
            }
            if (!isMatch) return

            val replacement = "${call.receiverPrefix()}$IS_COMPONENT_ENABLED($componentName)"
            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                fixName = IS_COMPONENT_ENABLED,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$IS_COMPONENT_ENABLED"
            )
        }

        private fun reportSetComponentEnabledSetting(node: UCallExpression) {
            if (node.methodName != SET_COMPONENT_ENABLED_SETTING_METHOD) return

            // Validation is PackageManager component state update.
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, PACKAGE_MANAGER_CLASS)) return
            if (node.valueArguments.size < 3) return

            // This is the `setComponentEnabledSetting(..., PackageManager.COMPONENT_ENABLED_STATE_*, ...)` pattern.
            val componentName = node.valueArguments[0].asSourceString()
            val state = node.valueArguments[1].unwrapParenthesized().resolveName() ?: return
            val flags = node.valueArguments[2].asSourceString()

            val (functionName, importTarget) = when (state) {
                COMPONENT_ENABLED_STATE_ENABLED -> ENABLE_COMPONENT to "${DeclaredSymbol.COMPONENT_PACKAGE}.$ENABLE_COMPONENT"
                COMPONENT_ENABLED_STATE_DISABLED -> DISABLE_COMPONENT to "${DeclaredSymbol.COMPONENT_PACKAGE}.$DISABLE_COMPONENT"
                COMPONENT_ENABLED_STATE_DEFAULT -> RESET_COMPONENT to "${DeclaredSymbol.COMPONENT_PACKAGE}.$RESET_COMPONENT"
                else -> return
            }

            val replacement = "${node.receiverPrefix()}$functionName($componentName, $flags)"
            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                fixName = functionName,
                importTarget = importTarget
            )
        }

        private fun reportPackageInfoCompat(node: UCallExpression) {
            if (node.methodName != GET_LONG_VERSION_CODE_METHOD) return

            // Validation is PackageInfoCompat class.
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, PACKAGE_INFO_COMPAT_CLASS)) return

            // This is the `PackageInfoCompat.getLongVersionCode(packageInfo)` pattern.
            val receiver = node.valueArguments.firstOrNull()?.asSourceString() ?: return

            val replacement = "$receiver.$VERSION_CODE_COMPAT"
            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                fixName = VERSION_CODE_COMPAT,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$VERSION_CODE_COMPAT"
            )
        }

        private fun reportVersionCodeCompat(node: UIfExpression) {
            // Validation is Android P version check.
            if (!isVersionCodeCompatCheck(node.condition)) return

            val thenBranch = node.thenExpression.unwrapBranchSource() ?: return
            val elseBranch = node.elseExpression.unwrapBranchSource() ?: return

            // This is the `if (version >= P) longVersionCode else versionCode.toLong()` pattern.
            val thenResolved = resolveVersionCodeReceiver(thenBranch) ?: return
            val elseResolved = resolveVersionCodeReceiver(elseBranch) ?: return
            if (thenResolved.first != elseResolved.first) return
            if (thenResolved.second == elseResolved.second) return

            val replacement = "${thenResolved.first}.$VERSION_CODE_COMPAT"
            reportAndFix(
                context = context,
                node = node,
                replacement = replacement,
                fixName = VERSION_CODE_COMPAT,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$VERSION_CODE_COMPAT"
            )
        }

        private fun resolveClassLiteralType(expression: UExpression?) = when (val target = expression.unwrapParenthesized()) {
            is UClassLiteralExpression -> target.type?.canonicalText
            is UQualifiedReferenceExpression -> (target.receiver as? UClassLiteralExpression)?.type?.canonicalText
            else -> null
        }

        private fun isPackageManagerCall(node: UCallExpression, methodName: String): Boolean {
            if (node.methodName != methodName) return false
            val method = node.resolve() ?: return false
            return context.evaluator.isMemberInClass(method, PACKAGE_MANAGER_CLASS)
        }

        private fun resolveRunCatchingPackageInfoCall(node: UCallExpression): UCallExpression? {
            val runCatchingExpression = node.receiver ?: return null
            val runCatchingCall = runCatchingExpression.findFirstCall { it.methodName == RUN_CATCHING_METHOD } ?: return null
            return runCatchingCall.findFirstCall { isPackageManagerCall(it, GET_PACKAGE_INFO_METHOD) }
        }

        private fun resolveQueryLaunchActivitiesInfo(node: UCallExpression?): Pair<String, String>? {
            val queryCall = node ?: return null
            if (!isPackageManagerCall(queryCall, QUERY_INTENT_ACTIVITIES_METHOD)) return null

            val receiver = queryCall.receiver?.asSourceString() ?: return null
            val launchIntentCall = queryCall.valueArguments.firstOrNull()
                .unwrapNotNullAssertion()
                .asCall()
                ?.takeIf { isPackageManagerCall(it, GET_LAUNCH_INTENT_FOR_PACKAGE_METHOD) }
                ?: return null
            if (launchIntentCall.receiver?.asSourceString() != receiver) return null

            val packageName = launchIntentCall.valueArguments.firstOrNull()?.asSourceString() ?: return null
            return receiver to packageName
        }

        private fun isVersionCodeCompatCheck(expression: UExpression): Boolean {
            val binaryExpression = expression as? UBinaryExpression
            if (binaryExpression != null) {
                val left = binaryExpression.leftOperand.asSourceString()
                val isBuildVersion = left == BUILD_SDK_INT_NAME || left.endsWith(".$BUILD_SDK_INT_NAME")

                return isBuildVersion && (binaryExpression.operator == UastBinaryOperator.GREATER_OR_EQUALS ||
                    binaryExpression.operator == UastBinaryOperator.GREATER) && binaryExpression.rightOperand.isVersionCodeCompatThreshold()
            }

            val callExpression = expression.unwrapParenthesized().asCall() ?: return false
            val methodName = callExpression.methodName ?: return false
            if (methodName != IS_AT_LEAST_METHOD && methodName != IS_GREATER_THAN_METHOD) return false
            val method = callExpression.resolve() ?: return false

            return method.containingClass?.qualifiedName == ANDROID_VERSION_CLASS &&
                callExpression.valueArguments.firstOrNull()?.isVersionCodeCompatThreshold() == true
        }

        private fun resolveVersionCodeReceiver(source: String) = when {
            source.endsWith(".$VERSION_CODE_LONG_PROPERTY") ->
                source.removeSuffix(".$VERSION_CODE_LONG_PROPERTY") to VERSION_CODE_COMPAT_LONG
            source.endsWith(".$VERSION_CODE_TO_LONG_CALL") ->
                source.removeSuffix(".$VERSION_CODE_TO_LONG_CALL") to VERSION_CODE_COMPAT_VERSION
            source.endsWith(".$VERSION_CODE_PROPERTY") ->
                source.removeSuffix(".$VERSION_CODE_PROPERTY") to VERSION_CODE_COMPAT_VERSION
            else -> null
        }

        private fun UExpression.isVersionCodeCompatThreshold(): Boolean {
            val source = asSourceString().trim()
            if (source == VERSION_CODE_COMPAT_28) return true
            if (source == VERSION_CODES_P_QUALIFIED || source.endsWith(".$VERSION_CODES_P_QUALIFIED")) return true
            if (source == ANDROID_VERSION_P_QUALIFIED || source.endsWith(".$ANDROID_VERSION_P_QUALIFIED")) return true

            return unwrapParenthesized().resolveName() == P_FIELD
        }

        private fun UExpression?.unwrapBranchSource(): String? {
            val expression = when (this) {
                is UBlockExpression -> expressions.lastOrNull()
                else -> this
            } ?: return null

            return expression.asSourceString().trim()
        }

        private fun UExpression?.unwrapNotNullAssertion(): UElement? {
            var current = unwrapParenthesized()
            while (current is UPostfixExpression && current.asSourceString().endsWith(NOT_NULL_ASSERTION))
                current = current.operand.unwrapParenthesized()

            return current
        }

        private fun UElement?.findFirstCall(predicate: (UCallExpression) -> Boolean): UCallExpression? {
            if (this == null) return null

            var matched: UCallExpression? = null
            accept(object : AbstractUastVisitor() {

                override fun visitCallExpression(node: UCallExpression): Boolean {
                    if (predicate(node)) {
                        matched = node
                        return true
                    }

                    return super.visitCallExpression(node)
                }
            })

            return matched
        }

        private fun reportAndFix(
            context: JavaContext,
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
}
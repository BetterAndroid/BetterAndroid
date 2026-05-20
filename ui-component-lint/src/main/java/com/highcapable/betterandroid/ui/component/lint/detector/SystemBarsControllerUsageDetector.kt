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
 * This file is created by fankes on 2026/5/16.
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
import com.highcapable.betterandroid.ui.component.lint.detector.extension.buildDeleteFix
import com.highcapable.betterandroid.ui.component.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.component.lint.detector.extension.containingUClasses
import com.highcapable.betterandroid.ui.component.lint.detector.extension.getterPropertyName
import com.highcapable.betterandroid.ui.component.lint.detector.extension.resolveName
import com.highcapable.betterandroid.ui.component.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiMember
import org.jetbrains.uast.UBinaryExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UastBinaryOperator
import org.jetbrains.uast.toUElementOfType

class SystemBarsControllerUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        const val SYSTEM_BARS_CONTROLLER_CLASS_NAME = "SystemBarsController"
        const val SYSTEM_BARS_CONTROLLER_CLASS = "${DeclaredSymbol.SYSTEMBAR_PACKAGE}.$SYSTEM_BARS_CONTROLLER_CLASS_NAME"

        private const val SYSTEM_BARS_CONTROLLER_INTERFACE = "${DeclaredSymbol.PROXY_PACKAGE}.ISystemBarsController"

        private const val ENABLE_EDGE_TO_EDGE_METHOD = "enableEdgeToEdge"
        private const val INIT_METHOD = "init"
        private const val SHOW_METHOD = "show"
        private const val HIDE_METHOD = "hide"
        private const val GET_INSETS_CONTROLLER_METHOD = "getInsetsController"
        private const val SET_DECOR_FITS_SYSTEM_WINDOWS_METHOD = "setDecorFitsSystemWindows"
        private const val WINDOW_COMPAT_CLASS = "androidx.core.view.WindowCompat"
        private const val WINDOW_CLASS = "android.view.Window"
        private const val VIEW_CLASS = "android.view.View"
        private const val WINDOW_INSETS_CONTROLLER_COMPAT_CLASS = "androidx.core.view.WindowInsetsControllerCompat"
        private const val WINDOW_INSETS_CONTROLLER_CLASS = "android.view.WindowInsetsController"
        private const val WINDOW_INSETS_COMPAT_TYPE_CLASS = "androidx.core.view.WindowInsetsCompat.Type"
        private const val WINDOW_INSETS_TYPE_CLASS = "android.view.WindowInsets.Type"
        private const val STATUS_BARS_METHOD = "statusBars"
        private const val NAVIGATION_BARS_METHOD = "navigationBars"
        private const val SYSTEM_BARS_METHOD = "systemBars"
        private const val WINDOW_INSETS_CONTROLLER_PROPERTY = "insetsController"
        private const val VIEW_WINDOW_INSETS_CONTROLLER_PROPERTY = "windowInsetsController"
        private const val SYSTEM_BARS_BEHAVIOR_PROPERTY = "systemBarsBehavior"
        private const val GET_SYSTEM_BARS_BEHAVIOR_METHOD = "getSystemBarsBehavior"
        private const val APPEARANCE_LIGHT_STATUS_BARS_PROPERTY = "isAppearanceLightStatusBars"
        private const val APPEARANCE_LIGHT_NAVIGATION_BARS_PROPERTY = "isAppearanceLightNavigationBars"
        private const val SET_SYSTEM_BARS_BEHAVIOR_METHOD = "setSystemBarsBehavior"
        private const val SET_APPEARANCE_LIGHT_STATUS_BARS_METHOD = "setAppearanceLightStatusBars"
        private const val SET_APPEARANCE_LIGHT_NAVIGATION_BARS_METHOD = "setAppearanceLightNavigationBars"

        private const val BEHAVIOR_PROPERTY = "behavior"
        private const val STATUS_BAR_STYLE_PROPERTY = "statusBarStyle"
        private const val NAVIGATION_BAR_STYLE_PROPERTY = "navigationBarStyle"
        private const val STATUS_BAR_COLOR_PROPERTY = "statusBarColor"
        private const val NAVIGATION_BAR_COLOR_PROPERTY = "navigationBarColor"
        private const val STATUS_BAR_CONTRAST_ENFORCED_PROPERTY = "isStatusBarContrastEnforced"
        private const val NAVIGATION_BAR_DIVIDER_COLOR_PROPERTY = "navigationBarDividerColor"
        private const val NAVIGATION_BAR_CONTRAST_ENFORCED_PROPERTY = "isNavigationBarContrastEnforced"
        private const val SYSTEM_BAR_BEHAVIOR_CLASS_NAME = "SystemBarBehavior"
        private const val SYSTEM_BAR_STYLE_CLASS_NAME = "SystemBarStyle"
        private const val SYSTEM_BARS_CLASS_NAME = "SystemBars"
        private const val BEHAVIOR_DEFAULT = "BEHAVIOR_DEFAULT"
        private const val BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE = "BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE"

        val ISSUE = Issue.create(
            id = "ReplaceWithSystemBarsController",
            briefDescription = "Use ui-component's SystemBarsController instead.",
            explanation = """
                Using `enableEdgeToEdge(...)`, direct `WindowInsetsController` / \
                `WindowInsetsControllerCompat` access, direct `WindowCompat.getInsetsController(...)`, \
                direct `window` system bar assignments, or direct system bars behavior control can be \
                simplified by using `SystemBarsController` from BetterAndroid ui-component library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-component#system-bars-status-bars-navigation-bars-etc
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-component#system-bars-status-bars-navigation-bars-etc

                The `SystemBarsController.kt` provides:
                - A unified edge-to-edge initialization entry
                - Direct `show(...)` and `hide(...)` APIs for system bars
                - Unified `behavior` control for system bars
                - Direct `statusBarStyle` and `navigationBarStyle` control
                - Unified takeover of the current root view's `WindowInsetsController`
                - Unified management of edge-to-edge defaults and system bar contrast behavior

                Examples:
                ```kotlin
                // Before
                enableEdgeToEdge()
                val compatController = WindowCompat.getInsetsController(window, rootView)
                compatController.systemBarsBehavior
                WindowCompat.getInsetsController(window, rootView).show(WindowInsetsCompat.Type.systemBars())
                WindowCompat.getInsetsController(window, rootView).hide(WindowInsetsCompat.Type.navigationBars())
                window.insetsController?.hide(WindowInsets.Type.statusBars())
                WindowCompat.getInsetsController(window, rootView).systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                WindowCompat.getInsetsController(window, rootView).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, rootView).isAppearanceLightNavigationBars = false
                WindowCompat.getInsetsController(window, rootView)
                window.insetsController
                rootView.windowInsetsController
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.setDecorFitsSystemWindows(false)
                window.statusBarColor = Color.RED
                window.navigationBarColor = Color.BLUE
                window.isStatusBarContrastEnforced = false
                window.navigationBarDividerColor = Color.RED
                window.isNavigationBarContrastEnforced = false

                // After
                systemBars.init(rootView)
                systemBars.show(SystemBars.ALL)
                systemBars.hide(SystemBars.NAVIGATION_BARS)
                systemBars.hide(SystemBars.STATUS_BARS)
                systemBars.behavior
                systemBars.behavior = SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
                // Hand over light appearance control to styles.
                systemBars.statusBarStyle = SystemBarStyle(Color.RED)
                systemBars.navigationBarStyle = SystemBarStyle(Color.BLUE)
                // The following effects are already held after init and should not be set manually:
                // - setDecorFitsSystemWindows(..., false)
                // - isStatusBarContrastEnforced
                // - navigationBarDividerColor
                // - isNavigationBarContrastEnforced
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                SystemBarsControllerUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UBinaryExpression::class.java as Class<out UElement>,
        UQualifiedReferenceExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            when {
                reportEnableEdgeToEdge(node) -> Unit
                reportWindowInsetsControllerShowOrHide(node) -> Unit
                reportWindowInsetsControllerBehavior(node) -> Unit
                reportWindowInsetsControllerLightAppearance(node) -> Unit
                reportDecorFitsSystemWindows(node) -> Unit
                reportWindowInsetsControllerAccess(node) -> Unit
            }
        }

        override fun visitBinaryExpression(node: UBinaryExpression) {
            reportWindowBarPropertyRemoval(node)
            reportWindowBarStyleAssignment(node)
            reportWindowInsetsControllerLightAppearance(node)
            reportWindowInsetsControllerBehavior(node)
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            reportWindowInsetsControllerPropertyAccess(node)
            reportWindowInsetsControllerBehavior(node)
        }

        private fun reportEnableEdgeToEdge(node: UCallExpression): Boolean {
            if (node.methodName != ENABLE_EDGE_TO_EDGE_METHOD) return false
            val method = node.resolve() ?: return false
            if (method.containingClass?.qualifiedName?.startsWith("androidx.activity.") != true) return false

            val hasSystemBarsController = node.hasSystemBarsController(context)

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = redundantOrHandOverMessage(
                    hasSystemBarsController = hasSystemBarsController,
                    redundantWhat = "$ENABLE_EDGE_TO_EDGE_METHOD(...)",
                    handOverTarget = "$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$INIT_METHOD(...)"
                ),
                quickfixData = if (hasSystemBarsController) buildDeleteFix(
                    name = "Delete '$ENABLE_EDGE_TO_EDGE_METHOD'",
                    location = context.getLocation(node.sourcePsi ?: return false)
                ) else null
            )
            return true
        }

        private fun reportWindowInsetsControllerShowOrHide(node: UCallExpression): Boolean {
            if (node.methodName != SHOW_METHOD && node.methodName != HIDE_METHOD) return false
            if (!node.hasSystemBarsController(context)) return false

            val method = node.resolve() ?: return false
            if (!context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_COMPAT_CLASS) &&
                !context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_CLASS)
            ) return false

            val systemBarsType = resolveSystemBarsType(node.valueArguments.firstOrNull()) ?: return false
            val systemBarsName = node.resolveSystemBarsControllerName(context) ?: return false

            val replacement = "$systemBarsName.${node.methodName}($SYSTEM_BARS_CLASS_NAME.$systemBarsType)"
            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$systemBarsName.${node.methodName}'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.SYSTEMBAR_TYPE_PACKAGE}.$SYSTEM_BARS_CLASS_NAME")
                )
            )
            return true
        }

        private fun reportWindowInsetsControllerLightAppearance(node: UCallExpression): Boolean {
            val propertyName = when (node.methodName) {
                SET_APPEARANCE_LIGHT_STATUS_BARS_METHOD -> APPEARANCE_LIGHT_STATUS_BARS_PROPERTY
                SET_APPEARANCE_LIGHT_NAVIGATION_BARS_METHOD -> APPEARANCE_LIGHT_NAVIGATION_BARS_PROPERTY
                else -> return false
            }

            val method = node.resolve() ?: return false
            if (!context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_COMPAT_CLASS) &&
                !context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_CLASS)
            ) return false

            val target = when (propertyName) {
                APPEARANCE_LIGHT_STATUS_BARS_PROPERTY -> "`$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$STATUS_BAR_STYLE_PROPERTY`"
                APPEARANCE_LIGHT_NAVIGATION_BARS_PROPERTY -> "`$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$NAVIGATION_BAR_STYLE_PROPERTY`"
                else -> return false
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = handOverMessage(target)
            )
            return true
        }

        private fun reportWindowInsetsControllerBehavior(node: UCallExpression): Boolean {
            if (node.methodName != SET_SYSTEM_BARS_BEHAVIOR_METHOD &&
                node.methodName != GET_SYSTEM_BARS_BEHAVIOR_METHOD
            ) return false

            val method = node.resolve() ?: return false
            if (!context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_COMPAT_CLASS) &&
                !context.evaluator.isMemberInClass(method, WINDOW_INSETS_CONTROLLER_CLASS)
            ) return false

            val hasSystemBarsController = node.hasSystemBarsController(context)
            val systemBarsName = node.resolveSystemBarsControllerName(context)
            val replacement = when (node.methodName) {
                SET_SYSTEM_BARS_BEHAVIOR_METHOD -> {
                    val behaviorType = resolveSystemBarBehavior(node.valueArguments.firstOrNull()) ?: return false
                    systemBarsName?.let { "$it.$BEHAVIOR_PROPERTY = $SYSTEM_BAR_BEHAVIOR_CLASS_NAME.$behaviorType" }
                }
                GET_SYSTEM_BARS_BEHAVIOR_METHOD -> systemBarsName?.let { "$it.$BEHAVIOR_PROPERTY" }
                else -> return false
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = if (hasSystemBarsController && replacement != null)
                    "Can be replaced with `$replacement`."
                else handOverMessage("$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$BEHAVIOR_PROPERTY"),
                quickfixData = if (hasSystemBarsController && replacement != null) buildReplaceFix(
                    name = "Replace with '$systemBarsName.$BEHAVIOR_PROPERTY'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.SYSTEMBAR_TYPE_PACKAGE}.$SYSTEM_BAR_BEHAVIOR_CLASS_NAME")
                ) else null
            )
            return true
        }

        private fun reportWindowInsetsControllerAccess(node: UCallExpression): Boolean {
            if (node.methodName != GET_INSETS_CONTROLLER_METHOD) return false

            val method = node.resolve() ?: return false
            if (!context.evaluator.isMemberInClass(method, WINDOW_COMPAT_CLASS)) return false

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = handOverMessage()
            )
            return true
        }

        private fun reportWindowInsetsControllerPropertyAccess(node: UQualifiedReferenceExpression) {
            if (!isWindowInsetsControllerProperty(node)) return

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = handOverMessage()
            )
        }

        private fun reportDecorFitsSystemWindows(node: UCallExpression): Boolean {
            if (node.methodName != SET_DECOR_FITS_SYSTEM_WINDOWS_METHOD) return false
            if (!node.isDecorFitsSystemWindowsFalseCall()) return false
            val hasSystemBarsController = node.hasSystemBarsController(context)

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = redundantOrHandOverMessage(
                    hasSystemBarsController = hasSystemBarsController,
                    redundantWhat = "$SET_DECOR_FITS_SYSTEM_WINDOWS_METHOD(..., false)",
                    handOverTarget = "$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$INIT_METHOD(...)"
                ),
                quickfixData = if (hasSystemBarsController) buildDeleteFix(
                    name = "Delete '$SET_DECOR_FITS_SYSTEM_WINDOWS_METHOD'",
                    location = context.getLocation(node.fullCallSourcePsi() ?: return false)
                ) else null
            )
            return true
        }

        private fun reportWindowBarPropertyRemoval(node: UBinaryExpression) {
            if (node.operator != UastBinaryOperator.ASSIGN) return

            val left = node.leftOperand.unwrapParenthesized() as? UQualifiedReferenceExpression ?: return
            val receiver = left.receiver.asSourceString()
            if (receiver != "window" && receiver != "this.window") return

            val propertyName = left.selector.resolveName() ?: return
            val hasSystemBarsController = node.hasSystemBarsController(context)

            val message = when (propertyName) {
                STATUS_BAR_CONTRAST_ENFORCED_PROPERTY ->
                    redundantOrHandOverMessage(hasSystemBarsController, STATUS_BAR_CONTRAST_ENFORCED_PROPERTY)
                NAVIGATION_BAR_DIVIDER_COLOR_PROPERTY ->
                    redundantOrHandOverMessage(hasSystemBarsController, NAVIGATION_BAR_DIVIDER_COLOR_PROPERTY)
                NAVIGATION_BAR_CONTRAST_ENFORCED_PROPERTY ->
                    redundantOrHandOverMessage(hasSystemBarsController, NAVIGATION_BAR_CONTRAST_ENFORCED_PROPERTY)
                else -> return
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = message,
                quickfixData = if (hasSystemBarsController) buildDeleteFix(
                    name = "Delete '$propertyName'",
                    location = context.getLocation(node.sourcePsi ?: return)
                ) else null
            )
        }

        private fun reportWindowBarStyleAssignment(node: UBinaryExpression) {
            if (node.operator != UastBinaryOperator.ASSIGN) return

            val left = node.leftOperand.unwrapParenthesized() as? UQualifiedReferenceExpression ?: return
            val receiver = left.receiver.asSourceString()
            if (receiver != "window" && receiver != "this.window") return

            val propertyName = left.selector.resolveName() ?: return
            val hasSystemBarsController = node.hasSystemBarsController(context)
            val targetProperty = when (propertyName) {
                STATUS_BAR_COLOR_PROPERTY -> STATUS_BAR_STYLE_PROPERTY
                NAVIGATION_BAR_COLOR_PROPERTY -> NAVIGATION_BAR_STYLE_PROPERTY
                else -> return
            }

            val right = node.rightOperand.asSourceString()
            val systemBarsName = node.resolveSystemBarsControllerName(context)
            val replacement = systemBarsName?.let { "$it.$targetProperty = $SYSTEM_BAR_STYLE_CLASS_NAME($right)" }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = if (hasSystemBarsController && replacement != null)
                    "Can be replaced with `$replacement`."
                else handOverMessage("$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$targetProperty"),
                quickfixData = if (hasSystemBarsController && replacement != null) buildReplaceFix(
                    name = "Replace with '$systemBarsName.$targetProperty'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.SYSTEMBAR_STYLE_PACKAGE}.$SYSTEM_BAR_STYLE_CLASS_NAME")
                ) else null
            )
        }

        private fun reportWindowInsetsControllerLightAppearance(node: UBinaryExpression) {
            if (node.operator != UastBinaryOperator.ASSIGN) return

            val left = node.leftOperand.unwrapParenthesized() as? UQualifiedReferenceExpression ?: return
            if (resolveWindowInsetsControllerSource(left.receiver) == null) return

            val propertyName = left.selector.resolveName() ?: return
            val target = when (propertyName) {
                APPEARANCE_LIGHT_STATUS_BARS_PROPERTY -> "`$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$STATUS_BAR_STYLE_PROPERTY`"
                APPEARANCE_LIGHT_NAVIGATION_BARS_PROPERTY -> "`$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$NAVIGATION_BAR_STYLE_PROPERTY`"
                else -> return
            }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = handOverMessage(target)
            )
        }

        private fun reportWindowInsetsControllerBehavior(node: UBinaryExpression) {
            if (node.operator != UastBinaryOperator.ASSIGN) return

            val left = node.leftOperand.unwrapParenthesized() as? UQualifiedReferenceExpression ?: return
            if (resolveWindowInsetsControllerSource(left.receiver) == null) return
            if (left.selector.resolveName() != SYSTEM_BARS_BEHAVIOR_PROPERTY) return

            val hasSystemBarsController = node.hasSystemBarsController(context)
            val behaviorType = resolveSystemBarBehavior(node.rightOperand) ?: return
            val systemBarsName = node.resolveSystemBarsControllerName(context)
            val replacement = systemBarsName?.let { "$it.$BEHAVIOR_PROPERTY = $SYSTEM_BAR_BEHAVIOR_CLASS_NAME.$behaviorType" }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = if (hasSystemBarsController && replacement != null)
                    "Can be replaced with `$replacement`."
                else handOverMessage("$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$BEHAVIOR_PROPERTY"),
                quickfixData = if (hasSystemBarsController && replacement != null) buildReplaceFix(
                    name = "Replace with '$systemBarsName.$BEHAVIOR_PROPERTY'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.SYSTEMBAR_TYPE_PACKAGE}.$SYSTEM_BAR_BEHAVIOR_CLASS_NAME")
                ) else null
            )
        }

        private fun reportWindowInsetsControllerBehavior(node: UQualifiedReferenceExpression) {
            if (node.selector.resolveName() != SYSTEM_BARS_BEHAVIOR_PROPERTY &&
                node.selector.resolveName() != GET_SYSTEM_BARS_BEHAVIOR_METHOD &&
                !node.asSourceString().endsWith(".$SYSTEM_BARS_BEHAVIOR_PROPERTY")
            ) return
            if (resolveWindowInsetsControllerSource(node.receiver) == null) return

            val hasSystemBarsController = node.hasSystemBarsController(context)
            val systemBarsName = node.resolveSystemBarsControllerName(context)
            val replacement = systemBarsName?.let { "$it.$BEHAVIOR_PROPERTY" }

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = if (hasSystemBarsController && replacement != null)
                    "Can be replaced with `$replacement`."
                else handOverMessage("$SYSTEM_BARS_CONTROLLER_CLASS_NAME.$BEHAVIOR_PROPERTY"),
                quickfixData = if (hasSystemBarsController && replacement != null) buildReplaceFix(
                    name = "Replace with '$systemBarsName.$BEHAVIOR_PROPERTY'",
                    replacement = replacement
                ) else null
            )
        }

        private fun resolveWindowInsetsControllerSource(expression: UElement?): UElement? {
            val target = expression.unwrapParenthesized()
            if (target != null && isWindowInsetsControllerSource(target)) return target

            val reference = target as? USimpleNameReferenceExpression ?: return null
            val localVariable = when (val resolved = reference.resolve()) {
                is ULocalVariable -> resolved
                is PsiLocalVariable -> resolved.toUElementOfType<ULocalVariable>()
                else -> null
            } ?: return null

            val initializer = localVariable.uastInitializer.unwrapParenthesized() ?: return null
            return initializer.takeIf { isWindowInsetsControllerSource(it) }
        }

        private fun isWindowInsetsControllerSource(node: UElement): Boolean = when (node) {
            is UCallExpression -> isWindowInsetsControllerCall(node)
            is UQualifiedReferenceExpression -> isWindowInsetsControllerProperty(node)
            else -> false
        }

        private fun isWindowInsetsControllerCall(node: UCallExpression): Boolean {
            if (node.methodName != GET_INSETS_CONTROLLER_METHOD) return false
            val method = node.resolve() ?: return false

            return context.evaluator.isMemberInClass(method, WINDOW_COMPAT_CLASS)
        }

        private fun isWindowInsetsControllerProperty(node: UQualifiedReferenceExpression): Boolean {
            val propertyName = node.selector.resolveName() ?: return false
            if (propertyName != WINDOW_INSETS_CONTROLLER_PROPERTY &&
                propertyName != VIEW_WINDOW_INSETS_CONTROLLER_PROPERTY
            ) return false

            val resolved = (node.selector as? USimpleNameReferenceExpression)?.resolve() as? PsiMember ?: return false
            return context.evaluator.isMemberInClass(resolved, WINDOW_CLASS) ||
                context.evaluator.isMemberInClass(resolved, VIEW_CLASS)
        }

        private fun resolveSystemBarsType(expression: UElement?): String? {
            val call = expression.unwrapParenthesized().asCall() ?: return null
            val method = call.resolve() ?: return null
            if (!context.evaluator.isMemberInClass(method, WINDOW_INSETS_COMPAT_TYPE_CLASS) &&
                !context.evaluator.isMemberInClass(method, WINDOW_INSETS_TYPE_CLASS)
            ) return null

            return when (call.methodName) {
                SYSTEM_BARS_METHOD -> "ALL"
                STATUS_BARS_METHOD -> "STATUS_BARS"
                NAVIGATION_BARS_METHOD -> "NAVIGATION_BARS"
                else -> null
            }
        }

        private fun resolveSystemBarBehavior(expression: UElement?): String? = when (expression.unwrapParenthesized()?.resolveName()) {
            BEHAVIOR_DEFAULT -> "DEFAULT"
            BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE -> "SHOW_TRANSIENT_BARS_BY_SWIPE"
            else -> null
        }

        private fun UCallExpression.isDecorFitsSystemWindowsFalseCall(): Boolean {
            val method = resolve() ?: return false

            return when {
                context.evaluator.isMemberInClass(method, WINDOW_COMPAT_CLASS) ->
                    valueArguments.size == 2 && valueArguments[1].asSourceString() == "false"
                context.evaluator.isMemberInClass(method, WINDOW_CLASS) ->
                    receiver?.asSourceString() in setOf("window", "this.window") &&
                        valueArguments.singleOrNull()?.asSourceString() == "false"
                else -> false
            }
        }

        private fun UCallExpression.fullCallSourcePsi() = when (val parent = uastParent) {
            is UQualifiedReferenceExpression -> if (parent.selector == this) parent.sourcePsi else sourcePsi
            else -> sourcePsi
        }

        private fun handOverMessage(target: String = SYSTEM_BARS_CONTROLLER_CLASS_NAME) =
            "Consider handing this over to `$target`."

        private fun redundantOrHandOverMessage(
            hasSystemBarsController: Boolean,
            redundantWhat: String,
            handOverTarget: String = SYSTEM_BARS_CONTROLLER_CLASS_NAME
        ) = if (hasSystemBarsController)
            "`$redundantWhat` is redundant here because `$SYSTEM_BARS_CONTROLLER_CLASS_NAME` already holds this effect."
        else handOverMessage(handOverTarget)

        private fun UElement?.hasSystemBarsController(context: JavaContext) =
            containingUClasses().any { it.javaPsi.hasSystemBarsController(context) }

        private fun UElement?.resolveSystemBarsControllerName(context: JavaContext) =
            containingUClasses().firstNotNullOfOrNull { it.javaPsi.resolveSystemBarsControllerName(context) }

        private fun PsiClass?.hasSystemBarsController(context: JavaContext) =
            resolveSystemBarsControllerName(context) != null

        private fun PsiClass?.resolveSystemBarsControllerName(context: JavaContext): String? {
            val psiClass = this ?: return null

            psiClass.allFields.firstOrNull { it.type.canonicalText == SYSTEM_BARS_CONTROLLER_CLASS }?.name
                ?.takeIf { it.isNotBlank() }
                ?.let { return it }

            psiClass.allMethods.firstOrNull {
                it.parameterList.parametersCount == 0 &&
                    it.name.startsWith("get") &&
                    it.returnType?.canonicalText == SYSTEM_BARS_CONTROLLER_CLASS
            }?.getterPropertyName()?.let { return it }

            if (context.evaluator.implementsInterface(psiClass, SYSTEM_BARS_CONTROLLER_INTERFACE, false))
                return SYSTEM_BARS_METHOD
            return null
        }
    }
}
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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.isQualifiedSelector
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UBinaryExpressionWithType
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UastBinaryExpressionWithTypeKind

class FragmentUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val FRAGMENT_ACTIVITY_CLASS = "androidx.fragment.app.FragmentActivity"
        private const val FRAGMENT_CLASS = "androidx.fragment.app.Fragment"

        private const val FIND_FRAGMENT_BY_ID_METHOD = "findFragmentById"
        private const val FIND_FRAGMENT_BY_TAG_METHOD = "findFragmentByTag"

        private const val SUPPORT_FRAGMENT_MANAGER_PROPERTY = "supportFragmentManager"
        private const val PARENT_FRAGMENT_MANAGER_PROPERTY = "parentFragmentManager"
        private const val CHILD_FRAGMENT_MANAGER_PROPERTY = "childFragmentManager"
        private const val GET_SUPPORT_FRAGMENT_MANAGER_METHOD = "getSupportFragmentManager"
        private const val GET_PARENT_FRAGMENT_MANAGER_METHOD = "getParentFragmentManager"
        private const val GET_CHILD_FRAGMENT_MANAGER_METHOD = "getChildFragmentManager"

        private const val FRAGMENT_MANAGER_FUNCTION = "fragmentManager"
        private const val FIND_FRAGMENT_FUNCTION = "findFragment"
        private const val AS_OPERATOR = "as"
        private const val SAFE_AS_OPERATOR = "as?"
        private const val NOT_NULL_ASSERTION = "!!"

        val ISSUE = Issue.create(
            id = "ReplaceWithFragmentExtension",
            briefDescription = "Use ui-extension's fragment manager extensions instead",
            explanation = """
                Using `supportFragmentManager`, `parentFragmentManager`, `childFragmentManager`, \
                `findFragmentById(...) as T` or `findFragmentByTag(...) as T` can be simplified by \
                using fragment manager extensions from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#fragment-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#fragment-extension

                The `Fragment.kt` provides:
                - A consistent `fragmentManager()` access API
                - Generic `findFragment<T>(...)` lookups
                - Simpler parent and child fragment manager access
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                activity.supportFragmentManager
                fragment.parentFragmentManager
                fragment.childFragmentManager
                activity.supportFragmentManager.findFragmentById(containerId) as DemoFragment
                activity.supportFragmentManager.findFragmentByTag("demo") as? DemoFragment

                // After
                activity.fragmentManager()
                fragment.fragmentManager(parent = true)
                fragment.fragmentManager()
                activity.fragmentManager().findFragment<DemoFragment>(containerId)!!
                activity.fragmentManager().findFragment<DemoFragment>("demo")
                ```
            """.trimIndent(),
            category = Category.PRODUCTIVITY,
            priority = 5,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                FragmentUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UQualifiedReferenceExpression::class.java,
        USimpleNameReferenceExpression::class.java,
        UBinaryExpressionWithType::class.java
    )

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        private val visitedBinaryExpressionsWithType = hashSetOf<Any>()

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            val selectorName = node.selector.resolveName() ?: return
            val resolved = (node.selector as? UResolvable)?.resolve() ?: return
            if (!isFragmentManagerProperty(resolved, selectorName)) return

            // This is the `supportFragmentManager` or `Fragment` manager property access pattern.
            reportFragmentManager(node, node.receiverPrefix(), selectorName)
        }

        override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
            if (node.isQualifiedSelector()) return
            val selectorName = node.resolveName() ?: return
            val resolved = node.resolve() ?: return
            if (!isFragmentManagerProperty(resolved, selectorName)) return

            // This is the implicit `Fragment` manager property access pattern.
            reportFragmentManager(node, receiverPrefix = "", selectorName)
        }

        private fun reportFragmentManager(node: UElement, receiverPrefix: String, selectorName: String) {
            val replacement = when (selectorName) {
                SUPPORT_FRAGMENT_MANAGER_PROPERTY -> "$receiverPrefix$FRAGMENT_MANAGER_FUNCTION()"
                PARENT_FRAGMENT_MANAGER_PROPERTY -> "$receiverPrefix$FRAGMENT_MANAGER_FUNCTION(parent = true)"
                CHILD_FRAGMENT_MANAGER_PROPERTY -> "$receiverPrefix$FRAGMENT_MANAGER_FUNCTION()"
                else -> return
            }

            context.report(
                issue = ISSUE,
                scope = node,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$FRAGMENT_MANAGER_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$FRAGMENT_MANAGER_FUNCTION")
                )
            )
        }

        override fun visitBinaryExpressionWithType(node: UBinaryExpressionWithType) {
            node.sourcePsi?.let { if (!visitedBinaryExpressionsWithType.add(it)) return }
            if (node.operationKind !is UastBinaryExpressionWithTypeKind.TypeCast) return

            val isNullableCast = node.operationKind.name == SAFE_AS_OPERATOR
            if (!isNullableCast && node.operationKind.name != AS_OPERATOR) return

            val call = node.operand.unwrapParenthesized().asCall() ?: return

            val methodName = call.methodName ?: return
            if (methodName != FIND_FRAGMENT_BY_ID_METHOD && methodName != FIND_FRAGMENT_BY_TAG_METHOD) return

            val arg = call.valueArguments.firstOrNull()?.asSourceString() ?: return
            val targetType = node.typeReference?.asSourceString() ?: return

            // This is the `findFragmentById/Tag(...) as/as? T` pattern.
            val replacement =
                "${call.receiverPrefix()}$FIND_FRAGMENT_FUNCTION<$targetType>($arg)${if (isNullableCast) "" else NOT_NULL_ASSERTION}"

            context.report(
                issue = ISSUE,
                scope = node,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$FIND_FRAGMENT_FUNCTION'",
                    replacement = replacement,
                    imports = arrayOf("${DeclaredSymbol.COMPONENT_PACKAGE}.$FIND_FRAGMENT_FUNCTION")
                )
            )
        }

        private fun isFragmentManagerProperty(resolved: PsiElement, name: String) = when (resolved) {
            is PsiMethod -> when (name) {
                SUPPORT_FRAGMENT_MANAGER_PROPERTY ->
                    resolved.name == GET_SUPPORT_FRAGMENT_MANAGER_METHOD &&
                        context.evaluator.isMemberInClass(resolved, FRAGMENT_ACTIVITY_CLASS)
                PARENT_FRAGMENT_MANAGER_PROPERTY ->
                    resolved.name == GET_PARENT_FRAGMENT_MANAGER_METHOD &&
                        context.evaluator.isMemberInClass(resolved, FRAGMENT_CLASS)
                CHILD_FRAGMENT_MANAGER_PROPERTY ->
                    resolved.name == GET_CHILD_FRAGMENT_MANAGER_METHOD &&
                        context.evaluator.isMemberInClass(resolved, FRAGMENT_CLASS)
                else -> false
            }
            else -> false
        }
    })
}
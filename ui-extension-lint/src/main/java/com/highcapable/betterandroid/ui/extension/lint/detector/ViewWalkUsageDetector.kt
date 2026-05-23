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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.isQualifiedSelector
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.resolveName
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.USimpleNameReferenceExpression

class ViewWalkUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val ANDROIDX_CORE_VIEW_PACKAGE = "androidx.core.view"
        private const val ANCESTORS_PROPERTY = "ancestors"
        private const val DESCENDANTS_PROPERTY = "descendants"
        private const val ANCESTORS_METHOD = "getAncestors"
        private const val DESCENDANTS_METHOD = "getDescendants"

        private const val WALK_TO_ROOT_FUNCTION = "walkToRoot"
        private const val WALK_THROUGH_CHILDREN_FUNCTION = "walkThroughChildren"

        private const val WALK_TO_ROOT_FULL_NAME = "${DeclaredSymbol.VIEW_PACKAGE}.$WALK_TO_ROOT_FUNCTION"
        private const val WALK_THROUGH_CHILDREN_FULL_NAME = "${DeclaredSymbol.VIEW_PACKAGE}.$WALK_THROUGH_CHILDREN_FUNCTION"

        val ISSUE = Issue.create(
            id = "ReplaceWithViewWalkExtension",
            briefDescription = "Use ui-extension's `walkToRoot()` or `walkThroughChildren()` instead.",
            explanation = """
                Using `ancestors` or `descendants` can be simplified by using `walkToRoot()` or \
                `walkThroughChildren()` from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#view-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#view-extension

                The `View.kt` provides:
                - A more direct walk API for parent and child traversal
                - Naming closer to actual traversal behavior
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                view.ancestors
                view.descendants

                // After
                view.walkToRoot()
                view.walkThroughChildren()
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ViewWalkUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UQualifiedReferenceExpression::class.java,
        USimpleNameReferenceExpression::class.java
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            val selectorName = node.selector.resolveName() ?: return
            if (!node.isViewWalkProperty(selectorName)) return

            // This is the `view.ancestors/descendants` property access pattern.
            val receiverText = node.receiver.asSourceString()

            val replacement = when (selectorName) {
                ANCESTORS_PROPERTY -> "$receiverText.$WALK_TO_ROOT_FUNCTION()"
                DESCENDANTS_PROPERTY -> "$receiverText.$WALK_THROUGH_CHILDREN_FUNCTION()"
                else -> return
            }
            val importTarget = when (selectorName) {
                ANCESTORS_PROPERTY -> WALK_TO_ROOT_FULL_NAME
                DESCENDANTS_PROPERTY -> WALK_THROUGH_CHILDREN_FULL_NAME
                else -> return
            }
            val fixName = if (selectorName == ANCESTORS_PROPERTY) WALK_TO_ROOT_FUNCTION else WALK_THROUGH_CHILDREN_FUNCTION

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = replacement,
                    imports = arrayOf(importTarget)
                )
            )
        }

        override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
            if (node.isQualifiedSelector()) return
            val selectorName = node.resolveName() ?: return
            if (!node.isViewWalkProperty(selectorName)) return

            // This is the implicit `ancestors/descendants` property access pattern.
            val replacement = when (selectorName) {
                ANCESTORS_PROPERTY -> "$WALK_TO_ROOT_FUNCTION()"
                DESCENDANTS_PROPERTY -> "$WALK_THROUGH_CHILDREN_FUNCTION()"
                else -> return
            }
            val importTarget = when (selectorName) {
                ANCESTORS_PROPERTY -> WALK_TO_ROOT_FULL_NAME
                DESCENDANTS_PROPERTY -> WALK_THROUGH_CHILDREN_FULL_NAME
                else -> return
            }
            val fixName = if (selectorName == ANCESTORS_PROPERTY) WALK_TO_ROOT_FUNCTION else WALK_THROUGH_CHILDREN_FUNCTION

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$replacement`.",
                quickfixData = buildReplaceFix(
                    name = "Replace with '$fixName'",
                    replacement = replacement,
                    imports = arrayOf(importTarget)
                )
            )
        }

        private fun UQualifiedReferenceExpression.isViewWalkProperty(name: String): Boolean {
            val resolved = (selector as? UResolvable)?.resolve() as? PsiMethod ?: return false
            val resolvedClass = resolved.containingClass?.qualifiedName ?: return false
            if (!resolvedClass.startsWith(ANDROIDX_CORE_VIEW_PACKAGE)) return false

            return when (name) {
                ANCESTORS_PROPERTY -> resolved.name == ANCESTORS_METHOD || resolved.name == ANCESTORS_PROPERTY
                DESCENDANTS_PROPERTY -> resolved.name == DESCENDANTS_METHOD || resolved.name == DESCENDANTS_PROPERTY
                else -> false
            }
        }

        private fun USimpleNameReferenceExpression.isViewWalkProperty(name: String): Boolean {
            val resolved = resolve() as? PsiMethod ?: return false
            val resolvedClass = resolved.containingClass?.qualifiedName ?: return false
            if (!resolvedClass.startsWith(ANDROIDX_CORE_VIEW_PACKAGE)) return false

            return when (name) {
                ANCESTORS_PROPERTY -> resolved.name == ANCESTORS_METHOD || resolved.name == ANCESTORS_PROPERTY
                DESCENDANTS_PROPERTY -> resolved.name == DESCENDANTS_METHOD || resolved.name == DESCENDANTS_PROPERTY
                else -> false
            }
        }
    }
}
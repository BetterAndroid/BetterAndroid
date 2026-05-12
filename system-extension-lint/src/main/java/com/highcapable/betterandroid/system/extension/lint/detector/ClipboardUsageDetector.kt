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
import com.intellij.psi.PsiLocalVariable
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UPostfixExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.toUElementOfType

class ClipboardUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val CLIPBOARD_MANAGER_CLASS = "android.content.ClipboardManager"
        private const val CONTEXT_CLASS = "android.content.Context"
        private const val CONTEXT_COMPAT_CLASS = "androidx.core.content.ContextCompat"
        private const val CLIP_DATA_CLASS = "android.content.ClipData"

        private const val SET_PRIMARY_CLIP_METHOD = "setPrimaryClip"
        private const val NEW_PLAIN_TEXT_METHOD = "newPlainText"
        private const val NEW_HTML_TEXT_METHOD = "newHtmlText"
        private const val NEW_INTENT_METHOD = "newIntent"
        private const val GET_SYSTEM_SERVICE_METHOD = "getSystemService"

        private const val COPY = "copy"
        private const val CLIPBOARD_MANAGER = "clipboardManager"
        private const val PRIMARY_CLIP_ITEMS = "primaryClipItems"
        private const val PRIMARY_CLIP_ITEMS_OR_NULL = "primaryClipItemsOrNull"
        private const val FIRST_PRIMARY_CLIP_ITEM = "firstPrimaryClipItem"
        private const val FIRST_PRIMARY_CLIP_ITEM_OR_NULL = "firstPrimaryClipItemOrNull"

        private val nullablePrimaryClipRegex = Regex("""^(.+)\.primaryClip\?\.getItemAt\((.+)\)$""", RegexOption.DOT_MATCHES_ALL)
        private val nonNullPrimaryClipRegex = Regex("""^(.+)\.primaryClip!!\.getItemAt\((.+)\)$""", RegexOption.DOT_MATCHES_ALL)

        val ISSUE = Issue.create(
            id = "ReplaceWithClipboardExtension",
            briefDescription = "Use system-extension's clipboard extensions instead.",
            explanation = """
                Using `setPrimaryClip(ClipData.new...)`, raw clipboard item access, or manual
                clipboard manager lookups can be simplified by using the clipboard extensions from
                BetterAndroid system-extension library.
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ClipboardUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java as Class<out UElement>,
        UQualifiedReferenceExpression::class.java as Class<out UElement>
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            reportCopy(node)
            reportClipboardManager(node)
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            reportPrimaryClipItem(node)
        }

        private fun reportCopy(node: UCallExpression) {
            if (node.methodName != SET_PRIMARY_CLIP_METHOD) return
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, CLIPBOARD_MANAGER_CLASS)) return

            val receiver = node.receiver?.asSourceString() ?: return
            val clipCall = resolveClipDataCall(node.valueArguments.firstOrNull()) ?: return
            val clipMethod = clipCall.methodName ?: return
            val clipDataMethod = clipCall.resolve() ?: return
            if (clipDataMethod.containingClass?.qualifiedName != CLIP_DATA_CLASS) return

            val replacement = when (clipMethod) {
                NEW_PLAIN_TEXT_METHOD -> {
                    if (clipCall.valueArguments.size < 2) return
                    "$receiver.$COPY(${clipCall.valueArguments[1].asSourceString()}, ${clipCall.valueArguments[0].asSourceString()})"
                }
                NEW_HTML_TEXT_METHOD -> {
                    if (clipCall.valueArguments.size < 3) return
                    "$receiver.$COPY(${clipCall.valueArguments[1].asSourceString()}, ${clipCall.valueArguments[2].asSourceString()}, ${clipCall.valueArguments[0].asSourceString()})"
                }
                NEW_INTENT_METHOD -> {
                    if (clipCall.valueArguments.size < 2) return
                    "$receiver.$COPY(${clipCall.valueArguments[1].asSourceString()}, ${clipCall.valueArguments[0].asSourceString()})"
                }
                else -> return
            }

            report(
                node = node,
                replacement = replacement,
                displayName = "$COPY(...)",
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$COPY"
            )
        }

        private fun reportClipboardManager(node: UCallExpression) {
            if (node.methodName != GET_SYSTEM_SERVICE_METHOD) return

            val replacement = when {
                isClipboardManagerLookup(node) -> {
                    node.receiver?.asSourceString()?.let { "$it.$CLIPBOARD_MANAGER" } ?: CLIPBOARD_MANAGER
                }
                isContextCompatClipboardManagerLookup(node) -> {
                    val receiver = node.valueArguments.firstOrNull()?.asSourceString() ?: return
                    "$receiver.$CLIPBOARD_MANAGER"
                }
                isClipboardManagerTypeArgument(node) -> {
                    val source = node.asSourceString()
                    val receiver = source.removeSuffix(".getSystemService<ClipboardManager>()")
                    if (receiver == source) CLIPBOARD_MANAGER else "$receiver.$CLIPBOARD_MANAGER"
                }
                else -> return
            }

            val targetNode = node.unwrapNotNullAssertionParent()
            report(
                node = targetNode,
                replacement = replacement,
                displayName = CLIPBOARD_MANAGER,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$CLIPBOARD_MANAGER"
            )
        }

        private fun reportPrimaryClipItem(node: UQualifiedReferenceExpression) {
            val source = node.asSourceString()

            nullablePrimaryClipRegex.matchEntire(source)?.let {
                val receiver = it.groupValues[1].trim()
                val index = it.groupValues[2].trim()
                val replacement = if (index == "0") {
                    "$receiver.$FIRST_PRIMARY_CLIP_ITEM_OR_NULL"
                } else "$receiver.$PRIMARY_CLIP_ITEMS_OR_NULL($index)"

                report(
                    node = node,
                    replacement = replacement,
                    displayName = if (index == "0") FIRST_PRIMARY_CLIP_ITEM_OR_NULL else "$PRIMARY_CLIP_ITEMS_OR_NULL(...)",
                    importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.${if (index == "0") FIRST_PRIMARY_CLIP_ITEM_OR_NULL else PRIMARY_CLIP_ITEMS_OR_NULL}"
                )
                return
            }

            nonNullPrimaryClipRegex.matchEntire(source)?.let {
                val receiver = it.groupValues[1].trim()
                val index = it.groupValues[2].trim()
                val replacement = if (index == "0")
                    "$receiver.$FIRST_PRIMARY_CLIP_ITEM"
                else "$receiver.$PRIMARY_CLIP_ITEMS($index)"

                report(
                    node = node,
                    replacement = replacement,
                    displayName = if (index == "0") FIRST_PRIMARY_CLIP_ITEM else "$PRIMARY_CLIP_ITEMS(...)",
                    importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.${if (index == "0") FIRST_PRIMARY_CLIP_ITEM else PRIMARY_CLIP_ITEMS}"
                )
            }
        }

        private fun isClipboardManagerLookup(node: UCallExpression): Boolean {
            val method = node.resolve() ?: return false
            val containingClass = method.containingClass ?: return false
            if (!context.evaluator.extendsClass(containingClass, CONTEXT_CLASS, false)) return false

            return resolveClassLiteralType(node.valueArguments.firstOrNull()) == CLIPBOARD_MANAGER_CLASS
        }

        private fun isContextCompatClipboardManagerLookup(node: UCallExpression): Boolean {
            val method = node.resolve() ?: return false
            if (!context.evaluator.isMemberInClass(method, CONTEXT_COMPAT_CLASS)) return false

            return resolveClassLiteralType(node.valueArguments.getOrNull(1)) == CLIPBOARD_MANAGER_CLASS
        }

        private fun isClipboardManagerTypeArgument(node: UCallExpression) =
            node.asSourceString().endsWith(".getSystemService<ClipboardManager>()") ||
                node.asSourceString() == "getSystemService<ClipboardManager>()"

        private fun resolveClassLiteralType(expression: UElement?) = when (val target = expression.unwrapParenthesized()) {
            is UClassLiteralExpression -> target.type?.canonicalText
            is UQualifiedReferenceExpression -> (target.receiver as? UClassLiteralExpression)?.type?.canonicalText
            else -> null
        }

        private fun resolveClipDataCall(expression: UElement?): UCallExpression? {
            val target = expression.unwrapParenthesized()
            val directCall = target.asCall()
            if (directCall != null) return directCall

            val reference = target as? USimpleNameReferenceExpression ?: return null
            val resolved = reference.resolve()
            val localVariable = when (resolved) {
                is ULocalVariable -> resolved
                is PsiLocalVariable -> resolved.toUElementOfType<ULocalVariable>()
                else -> null
            } ?: return null

            return localVariable.uastInitializer.unwrapParenthesized().asCall()
        }

        private fun UCallExpression.unwrapNotNullAssertionParent(): UElement {
            var current: UElement = this
            var parent = current.uastParent
            while (parent is UParenthesizedExpression) {
                current = parent
                parent = current.uastParent
            }

            return if (parent is UPostfixExpression && parent.asSourceString() == "${current.asSourceString()}!!") parent else this
        }

        private fun report(
            node: UElement,
            replacement: String,
            importTarget: String,
            displayName: String = replacement.displayShortName()
        ) = context.report(
            issue = ISSUE,
            location = context.getLocation(node),
            message = "Can be replaced with `$replacement`.",
            quickfixData = buildReplaceFix(
                name = "Replace with '$displayName'",
                replacement = replacement,
                imports = arrayOf(importTarget)
            )
        )
    }
}
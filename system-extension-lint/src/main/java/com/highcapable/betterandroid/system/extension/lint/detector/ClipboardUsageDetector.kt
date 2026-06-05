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
import com.highcapable.betterandroid.system.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.system.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.system.extension.lint.detector.extension.findContainingUClass
import com.highcapable.betterandroid.system.extension.lint.detector.extension.receiverPrefix
import com.highcapable.betterandroid.system.extension.lint.detector.extension.resolveJavaClassTypeArgument
import com.highcapable.betterandroid.system.extension.lint.detector.extension.resolveName
import com.highcapable.betterandroid.system.extension.lint.detector.extension.unwrapParenthesized
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiMember
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UPostfixExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
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
        private const val GET_ITEM_AT_METHOD = "getItemAt"
        private const val PRIMARY_CLIP_PROPERTY = "primaryClip"

        private const val COPY = "copy"
        private const val CLIPBOARD_MANAGER = "clipboardManager"
        private const val PRIMARY_CLIP_ITEMS = "primaryClipItems"
        private const val PRIMARY_CLIP_ITEMS_OR_NULL = "primaryClipItemsOrNull"
        private const val FIRST_PRIMARY_CLIP_ITEM = "firstPrimaryClipItem"
        private const val FIRST_PRIMARY_CLIP_ITEM_OR_NULL = "firstPrimaryClipItemOrNull"

        private const val THIS_EXPRESSION = "this"
        private const val NULL_LITERAL = "null"
        private const val FIRST_INDEX = "0"
        private const val NOT_NULL_ASSERTION = "!!"

        val ISSUE = Issue.create(
            id = "ReplaceWithClipboardExtension",
            briefDescription = "Use system-extension's clipboard extensions instead.",
            explanation = """
                Using `setPrimaryClip(ClipData.new...)`, raw clipboard item access, or manual \
                clipboard manager lookups can be simplified by using clipboard extensions from \
                BetterAndroid system-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/system-extension#clipboard-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/system-extension#clipboard-extension

                The `Clipboard.kt` provides:
                - Direct `copy(...)` helpers for text, HTML and `Intent`
                - Simpler clipboard manager access
                - Direct primary clip item helpers
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                clipboardManager.setPrimaryClip(ClipData.newPlainText("label", "text"))
                clipboardManager.setPrimaryClip(ClipData.newHtmlText("label", "text", "<b>text</b>"))
                clipboardManager.setPrimaryClip(ClipData.newIntent("label", intent))
                clipboardManager.primaryClip?.getItemAt(0)
                clipboardManager.primaryClip!!.getItemAt(1)
                context.getSystemService(ClipboardManager::class.java)

                // After
                clipboardManager.copy("text", "label")
                clipboardManager.copy("text", "<b>text</b>", "label")
                clipboardManager.copy(intent, "label")
                clipboardManager.firstPrimaryClipItemOrNull
                clipboardManager.primaryClipItems(1)
                context.clipboardManager
                ```
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

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            reportCopy(node)
            reportClipboardManager(node)
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            reportPrimaryClipItem(node)
        }

        private fun reportCopy(node: UCallExpression) {
            if (node.methodName != SET_PRIMARY_CLIP_METHOD) return

            // Validation is ClipboardManager class.
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, CLIPBOARD_MANAGER_CLASS)) return

            val clipCall = resolveClipDataCall(node.valueArguments.firstOrNull()) ?: return
            val clipMethod = clipCall.methodName ?: return
            val clipDataMethod = clipCall.resolve() ?: return
            if (clipDataMethod.containingClass?.qualifiedName != CLIP_DATA_CLASS) return

            // This is the `clipboardManager.setPrimaryClip(ClipData.new...)` pattern.
            val arguments = clipCall.valueArguments
            val label = arguments.argumentSource(0) ?: return
            val text = arguments.argumentSource(1) ?: return
            val html = arguments.argumentSourceOrNull(2)

            val replacement = when (clipMethod) {
                NEW_PLAIN_TEXT_METHOD -> {
                    if (arguments.size < 2) return
                    "${node.receiverPrefix()}$COPY($text, $label)"
                }
                NEW_HTML_TEXT_METHOD -> {
                    if (arguments.size < 3) return
                    "${node.receiverPrefix()}$COPY($text, $html, $label)"
                }
                NEW_INTENT_METHOD -> {
                    if (arguments.size < 2) return
                    "${node.receiverPrefix()}$COPY(${arguments[1].asSourceString()}, $label)"
                }
                else -> return
            }

            reportAndFix(
                node = node,
                replacement = replacement,
                fixName = COPY,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$COPY"
            )
        }

        private fun reportClipboardManager(node: UCallExpression) {
            if (node.methodName != GET_SYSTEM_SERVICE_METHOD) return

            // This is the `Context.getSystemService(ClipboardManager::class.java)` pattern.
            val replacement = when {
                isClipboardManagerLookup(node) -> node.extensionPropertyReplacement(CLIPBOARD_MANAGER)
                isContextCompatClipboardManagerLookup(node) -> {
                    val receiver = node.valueArguments.firstOrNull()?.asSourceString()?.trim() ?: return
                    if (receiver == THIS_EXPRESSION) CLIPBOARD_MANAGER else "$receiver.$CLIPBOARD_MANAGER"
                }
                isClipboardManagerTypeArgument(node) -> node.extensionPropertyReplacement(CLIPBOARD_MANAGER)
                else -> return
            }

            val targetNode = node.unwrapNotNullAssertionParent()
            reportAndFix(
                node = targetNode,
                replacement = replacement,
                fixName = CLIPBOARD_MANAGER,
                importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$CLIPBOARD_MANAGER"
            )
        }

        private fun reportPrimaryClipItem(node: UQualifiedReferenceExpression) {
            val itemCall = node.selector.asCall() ?: return
            if (itemCall.methodName != GET_ITEM_AT_METHOD) return

            // Validation is ClipData class.
            val itemMethod = itemCall.resolve() ?: return
            if (!context.evaluator.isMemberInClass(itemMethod, CLIP_DATA_CLASS)) return

            val primaryClipAccess = node.receiver.unwrapNotNullAssertionElement() as? UQualifiedReferenceExpression ?: return
            if (primaryClipAccess.selector.resolveName() != PRIMARY_CLIP_PROPERTY) return

            val primaryClipResolved = (primaryClipAccess.selector as? UResolvable)?.resolve()
                ?: (primaryClipAccess as? UResolvable)?.resolve()
                ?: return
            if (!context.evaluator.isMemberInClass(primaryClipResolved as? PsiMember, CLIPBOARD_MANAGER_CLASS)) return

            val index = itemCall.valueArguments.firstOrNull()?.asSourceString()?.trim() ?: return
            val source = node.asSourceString()
            val receiverPrefix = primaryClipAccess.receiverPrefix()

            when {
                // This is the `clipboardManager.primaryClip?.getItemAt(index)` pattern.
                source.contains(".${PRIMARY_CLIP_PROPERTY}?.") -> {
                    val target = PrimaryClipItemTarget.of(index, nullable = true)
                    val replacement = target.buildReplacement(receiverPrefix, index)

                    reportAndFix(
                        node = node,
                        replacement = replacement,
                        fixName = target.functionName,
                        importTarget = target.importTarget
                    )
                }
                // This is the `clipboardManager.primaryClip!!.getItemAt(index)` pattern.
                source.contains(".${PRIMARY_CLIP_PROPERTY}!!.") -> {
                    val target = PrimaryClipItemTarget.of(index, nullable = false)
                    val replacement = target.buildReplacement(receiverPrefix, index)

                    reportAndFix(
                        node = node,
                        replacement = replacement,
                        fixName = target.functionName,
                        importTarget = target.importTarget
                    )
                }
            }
        }

        private fun isClipboardManagerLookup(node: UCallExpression): Boolean {
            val method = node.resolve() ?: return false
            val containingClass = method.containingClass ?: return false

            // Validation is Context class.
            if (!context.evaluator.extendsClass(containingClass, CONTEXT_CLASS, false)) return false

            return resolveClassLiteralType(node.valueArguments.firstOrNull()) == CLIPBOARD_MANAGER_CLASS
        }

        private fun isContextCompatClipboardManagerLookup(node: UCallExpression): Boolean {
            val method = node.resolve() ?: return false

            // Validation is ContextCompat class.
            if (!context.evaluator.isMemberInClass(method, CONTEXT_COMPAT_CLASS)) return false

            return resolveClassLiteralType(node.valueArguments.getOrNull(1)) == CLIPBOARD_MANAGER_CLASS
        }

        private fun isClipboardManagerTypeArgument(node: UCallExpression): Boolean {
            if (node.typeArguments.firstOrNull()?.canonicalText != CLIPBOARD_MANAGER_CLASS) return false

            // Validation is Context receiver.
            val receiverType = node.receiver?.getExpressionType()
            if (receiverType != null) return receiverType.extendsClass(context, CONTEXT_CLASS)

            val containingClass = node.findContainingUClass()?.javaPsi ?: return false
            return context.evaluator.extendsClass(containingClass, CONTEXT_CLASS, false)
        }

        private fun resolveClassLiteralType(expression: UElement?) = when (val target = expression.unwrapParenthesized()) {
            is UClassLiteralExpression -> target.type?.canonicalText
            is UQualifiedReferenceExpression -> (target.receiver as? UClassLiteralExpression)?.type?.canonicalText
                ?: (target as? UExpression)?.getExpressionType().resolveJavaClassTypeArgument()
            is UExpression -> target.getExpressionType().resolveJavaClassTypeArgument()
            else -> null
        }

        private fun resolveClipDataCall(expression: UElement?): UCallExpression? {
            val target = expression.unwrapParenthesized()
            val directCall = target.asCall()
            if (directCall != null) return directCall

            val reference = target as? USimpleNameReferenceExpression ?: return null
            val localVariable = when (val resolved = reference.resolve()) {
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

            return if (parent is UPostfixExpression && parent.asSourceString() == "${current.asSourceString()}$NOT_NULL_ASSERTION") parent else this
        }

        private fun UElement?.unwrapNotNullAssertionElement(): UElement? {
            var current = unwrapParenthesized()
            while (current is UPostfixExpression && current.asSourceString().endsWith(NOT_NULL_ASSERTION))
                current = current.operand.unwrapParenthesized()

            return current
        }

        private fun List<UExpression>.argumentSource(index: Int): String? = getOrNull(index)?.asSourceString()

        private fun List<UExpression>.argumentSourceOrNull(index: Int): String? {
            val source = getOrNull(index)?.asSourceString()?.trim() ?: return null
            return source.takeIf { it != NULL_LITERAL }
        }

        private fun UCallExpression.extensionPropertyReplacement(name: String) =
            receiverPrefix().takeIf { it.isNotEmpty() }?.plus(name) ?: name

        private fun reportAndFix(
            node: UElement,
            replacement: String,
            importTarget: String,
            fixName: String
        ) = context.report(
            issue = ISSUE,
            location = context.getLocation(node),
            message = "Can be replaced with `$replacement`.",
            quickfixData = buildReplaceFix(
                name = "Replace with '$fixName'",
                replacement = replacement,
                imports = arrayOf(importTarget)
            )
        )
    })

    private data class PrimaryClipItemTarget(val functionName: String, private val usesIndexArgument: Boolean) {

        companion object {

            fun of(index: String, nullable: Boolean) = when {
                nullable && index == FIRST_INDEX -> PrimaryClipItemTarget(FIRST_PRIMARY_CLIP_ITEM_OR_NULL, usesIndexArgument = false)
                nullable -> PrimaryClipItemTarget(PRIMARY_CLIP_ITEMS_OR_NULL, usesIndexArgument = true)
                index == FIRST_INDEX -> PrimaryClipItemTarget(FIRST_PRIMARY_CLIP_ITEM, usesIndexArgument = false)
                else -> PrimaryClipItemTarget(PRIMARY_CLIP_ITEMS, usesIndexArgument = true)
            }
        }

        val importTarget = "${DeclaredSymbol.COMPONENT_PACKAGE}.$functionName"

        fun buildReplacement(receiverPrefix: String, index: String) =
            if (usesIndexArgument) "$receiverPrefix$functionName($index)" else "$receiverPrefix$functionName"
    }
}
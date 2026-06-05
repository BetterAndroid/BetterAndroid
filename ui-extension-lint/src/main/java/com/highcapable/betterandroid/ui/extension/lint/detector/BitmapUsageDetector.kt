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
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.ui.extension.lint.DeclaredSymbol
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.buildAlternativesFix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.buildReplaceFix
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.joinSourceArguments
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

class BitmapUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val BITMAP_FACTORY_CLASS = "android.graphics.BitmapFactory"

        private const val DECODE_FILE_METHOD = "decodeFile"
        private const val DECODE_STREAM_METHOD = "decodeStream"
        private const val DECODE_BYTE_ARRAY_METHOD = "decodeByteArray"
        private const val DECODE_RESOURCE_METHOD = "decodeResource"

        private const val DECODE_TO_BITMAP = "decodeToBitmap"
        private const val DECODE_TO_BITMAP_OR_NULL = "decodeToBitmapOrNull"
        private const val CREATE_BITMAP = "createBitmap"
        private const val CREATE_BITMAP_OR_NULL = "createBitmapOrNull"

        private const val ABSOLUTE_PATH_PROPERTY = "absolutePath"
        private const val THIS_RECEIVER = "this"

        val ISSUE = Issue.create(
            id = "ReplaceWithBitmapExtension",
            briefDescription = "Use ui-extension's bitmap decode extensions instead.",
            explanation = """
                Using `BitmapFactory.decode...(...)` can be simplified by using bitmap decode \
                extensions from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#bitmap-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#bitmap-extension

                The `Bitmap.kt` provides:
                - `decodeToBitmap()` and `decodeToBitmapOrNull()` for common decode sources
                - `createBitmap()` and `createBitmapOrNull()` for `Resources`
                - A more direct API around existing decode inputs
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                BitmapFactory.decodeFile(file.absolutePath, options)
                BitmapFactory.decodeStream(inputStream, null, options)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                BitmapFactory.decodeResource(resources, drawableId, options)

                // After
                file.decodeToBitmapOrNull(options)
                inputStream.decodeToBitmapOrNull(null, options)
                bytes.decodeToBitmapOrNull(0, bytes.size)
                resources.createBitmapOrNull(drawableId, options)
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                BitmapUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext) = context.createKotlinOnlyUastHandler(object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            val methodName = node.methodName ?: return

            // Validation is BitmapFactory class.
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, BITMAP_FACTORY_CLASS)) return

            // This is the `BitmapFactory.decode...(...)` pattern.
            val fixes = when (methodName) {
                DECODE_FILE_METHOD -> createDecodeFileFixes(node)
                DECODE_STREAM_METHOD -> createDecodeStreamFixes(node)
                DECODE_BYTE_ARRAY_METHOD -> createDecodeByteArrayFixes(node)
                DECODE_RESOURCE_METHOD -> createDecodeResourceFixes(node)
                else -> emptyList()
            }
            if (fixes.isEmpty()) return

            val display = fixes.first().displayReplacement
            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `$display`.",
                quickfixData = buildAlternativesFix(*fixes.map { it.fix }.toTypedArray())
            )
        }

        private fun createDecodeFileFixes(node: UCallExpression): List<BitmapDecodeFix> {
            val arguments = node.valueArguments
            if (arguments.isEmpty()) return emptyList()

            val pathArg = arguments[0] as? UQualifiedReferenceExpression ?: return emptyList()
            val selector = pathArg.selector as? USimpleNameReferenceExpression ?: return emptyList()
            if (selector.identifier != ABSOLUTE_PATH_PROPERTY) return emptyList()

            val file = pathArg.receiver.asSourceString()
            val receiverPrefix = file.extensionReceiverPrefix()
            val opts = arguments.getOrNull(1)?.asSourceString()
            val decodeOrNullReplacement = if (opts != null)
                "$receiverPrefix$DECODE_TO_BITMAP_OR_NULL($opts)"
            else "$receiverPrefix$DECODE_TO_BITMAP_OR_NULL()"
            val decodeReplacement = if (opts != null)
                "$receiverPrefix$DECODE_TO_BITMAP($opts)"
            else "$receiverPrefix$DECODE_TO_BITMAP()"

            return listOf(
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$decodeOrNullReplacement'",
                        replacement = decodeOrNullReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP_OR_NULL")
                    ),
                    displayReplacement = decodeOrNullReplacement
                ),
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$decodeReplacement'",
                        replacement = decodeReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP")
                    ),
                    displayReplacement = decodeReplacement
                )
            )
        }

        private fun createDecodeStreamFixes(node: UCallExpression): List<BitmapDecodeFix> {
            val arguments = node.valueArguments
            if (arguments.isEmpty()) return emptyList()

            val receiver = arguments[0].asSourceString().trim()
            val receiverPrefix = receiver.extensionReceiverPrefix()
            val args = arguments.joinSourceArguments(startIndex = 1)
            val decodeOrNullReplacement = buildExtensionCall(receiverPrefix, DECODE_TO_BITMAP_OR_NULL, args)
            val decodeReplacement = buildExtensionCall(receiverPrefix, DECODE_TO_BITMAP, args)

            return listOf(
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$decodeOrNullReplacement'",
                        replacement = decodeOrNullReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP_OR_NULL")
                    ),
                    displayReplacement = decodeOrNullReplacement
                ),
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$decodeReplacement'",
                        replacement = decodeReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP")
                    ),
                    displayReplacement = decodeReplacement
                )
            )
        }

        private fun createDecodeByteArrayFixes(node: UCallExpression): List<BitmapDecodeFix> {
            val arguments = node.valueArguments
            if (arguments.size < 3) return emptyList()

            val receiver = arguments[0].asSourceString().trim()
            val receiverPrefix = receiver.extensionReceiverPrefix()
            val args = arguments.joinSourceArguments(startIndex = 1)
            val decodeOrNullReplacement = buildExtensionCall(receiverPrefix, DECODE_TO_BITMAP_OR_NULL, args)
            val decodeReplacement = buildExtensionCall(receiverPrefix, DECODE_TO_BITMAP, args)

            return listOf(
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$decodeOrNullReplacement'",
                        replacement = decodeOrNullReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP_OR_NULL")
                    ),
                    displayReplacement = decodeOrNullReplacement
                ),
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$decodeReplacement'",
                        replacement = decodeReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP")
                    ),
                    displayReplacement = decodeReplacement
                )
            )
        }

        private fun createDecodeResourceFixes(node: UCallExpression): List<BitmapDecodeFix> {
            val arguments = node.valueArguments
            if (arguments.size < 2) return emptyList()

            val receiver = arguments[0].asSourceString().trim()
            val receiverPrefix = receiver.extensionReceiverPrefix()
            val args = arguments.joinSourceArguments(startIndex = 1)
            val createOrNullReplacement = buildExtensionCall(receiverPrefix, CREATE_BITMAP_OR_NULL, args)
            val createReplacement = buildExtensionCall(receiverPrefix, CREATE_BITMAP, args)

            return listOf(
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$createOrNullReplacement'",
                        replacement = createOrNullReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$CREATE_BITMAP_OR_NULL")
                    ),
                    displayReplacement = createOrNullReplacement
                ),
                BitmapDecodeFix(
                    fix = buildReplaceFix(
                        name = "Replace with '$createReplacement'",
                        replacement = createReplacement,
                        imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$CREATE_BITMAP")
                    ),
                    displayReplacement = createReplacement
                )
            )
        }

        private fun buildExtensionCall(receiverPrefix: String, functionName: String, arguments: String) =
            if (arguments.isBlank()) "$receiverPrefix$functionName()" else "$receiverPrefix$functionName($arguments)"

        private fun String.extensionReceiverPrefix() = if (this == THIS_RECEIVER) "" else "$this."
    })

    private data class BitmapDecodeFix(
        val fix: LintFix,
        val displayReplacement: String
    )
}
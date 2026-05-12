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

        val ISSUE = Issue.create(
            id = "ReplaceWithBitmapExtension",
            briefDescription = "Use ui-extension's bitmap decode extensions instead.",
            explanation = """
                Using `BitmapFactory.decode...(...)` can be simplified by using the bitmap decode \
                extensions from BetterAndroid ui-extension library.
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

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitCallExpression(node: UCallExpression) {
            val methodName = node.methodName ?: return
            val method = node.resolve() ?: return
            if (!context.evaluator.isMemberInClass(method, BITMAP_FACTORY_CLASS)) return

            val fixes = when (methodName) {
                DECODE_FILE_METHOD -> createDecodeFileFixes(node)
                DECODE_STREAM_METHOD -> createDecodeStreamFixes(node)
                DECODE_BYTE_ARRAY_METHOD -> createDecodeByteArrayFixes(node)
                DECODE_RESOURCE_METHOD -> createDecodeResourceFixes(node)
                else -> emptyList()
            }
            if (fixes.isEmpty()) return

            val display = fixes.first().getDisplayName() ?: return
            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Can be replaced with `${display.removePrefix("Replace with '").removeSuffix("'")}`.",
                quickfixData = buildAlternativesFix(*fixes.toTypedArray())
            )
        }

        private fun createDecodeFileFixes(node: UCallExpression): List<LintFix> {
            val arguments = node.valueArguments
            if (arguments.isEmpty()) return emptyList()

            val pathArg = arguments[0] as? UQualifiedReferenceExpression ?: return emptyList()
            val selector = pathArg.selector as? USimpleNameReferenceExpression ?: return emptyList()
            if (selector.identifier != ABSOLUTE_PATH_PROPERTY) return emptyList()

            val file = pathArg.receiver.asSourceString()
            val optsSuffix = arguments.getOrNull(1)?.let { "(${it.asSourceString()})" } ?: "()"

            return listOf(
                buildReplaceFix(
                    name = "Replace with '$DECODE_TO_BITMAP_OR_NULL'",
                    replacement = "$file.$DECODE_TO_BITMAP_OR_NULL$optsSuffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP_OR_NULL")
                ),
                buildReplaceFix(
                    name = "Replace with '$DECODE_TO_BITMAP'",
                    replacement = "$file.$DECODE_TO_BITMAP$optsSuffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP")
                )
            )
        }

        private fun createDecodeStreamFixes(node: UCallExpression): List<LintFix> {
            val arguments = node.valueArguments
            if (arguments.isEmpty()) return emptyList()

            val receiver = arguments[0].asSourceString()
            val args = buildList {
                arguments.getOrNull(1)?.also { add(it.asSourceString()) }
                arguments.getOrNull(2)?.also { add(it.asSourceString()) }
            }.joinToString(", ")
            val suffix = "($args)"

            return listOf(
                buildReplaceFix(
                    name = "Replace with '$DECODE_TO_BITMAP_OR_NULL'",
                    replacement = "$receiver.$DECODE_TO_BITMAP_OR_NULL$suffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP_OR_NULL")
                ),
                buildReplaceFix(
                    name = "Replace with '$DECODE_TO_BITMAP'",
                    replacement = "$receiver.$DECODE_TO_BITMAP$suffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP")
                )
            )
        }

        private fun createDecodeByteArrayFixes(node: UCallExpression): List<LintFix> {
            val arguments = node.valueArguments
            if (arguments.size < 3) return emptyList()

            val receiver = arguments[0].asSourceString()
            val suffix = "(${arguments[1].asSourceString()}, ${arguments[2].asSourceString()})"

            return listOf(
                buildReplaceFix(
                    name = "Replace with '$DECODE_TO_BITMAP_OR_NULL'",
                    replacement = "$receiver.$DECODE_TO_BITMAP_OR_NULL$suffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP_OR_NULL")
                ),
                buildReplaceFix(
                    name = "Replace with '$DECODE_TO_BITMAP'",
                    replacement = "$receiver.$DECODE_TO_BITMAP$suffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$DECODE_TO_BITMAP")
                )
            )
        }

        private fun createDecodeResourceFixes(node: UCallExpression): List<LintFix> {
            val arguments = node.valueArguments
            if (arguments.size < 2) return emptyList()

            val receiver = arguments[0].asSourceString()
            val args = buildList {
                add(arguments[1].asSourceString())
                arguments.getOrNull(2)?.also { add(it.asSourceString()) }
            }.joinToString(", ")
            val suffix = "($args)"

            return listOf(
                buildReplaceFix(
                    name = "Replace with '$CREATE_BITMAP_OR_NULL'",
                    replacement = "$receiver.$CREATE_BITMAP_OR_NULL$suffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$CREATE_BITMAP_OR_NULL")
                ),
                buildReplaceFix(
                    name = "Replace with '$CREATE_BITMAP'",
                    replacement = "$receiver.$CREATE_BITMAP$suffix",
                    imports = arrayOf("${DeclaredSymbol.GRAPHICS_PACKAGE}.$CREATE_BITMAP")
                )
            )
        }
    }
}
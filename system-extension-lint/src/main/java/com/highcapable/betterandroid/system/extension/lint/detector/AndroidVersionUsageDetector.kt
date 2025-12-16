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
 * This file is created by fankes on 2025/12/16.
 */
package com.highcapable.betterandroid.system.extension.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.highcapable.betterandroid.system.extension.lint.DeclaredSymbol
import com.intellij.psi.PsiField
import org.jetbrains.uast.UBinaryExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UReferenceExpression
import org.jetbrains.uast.UastBinaryOperator

class AndroidVersionUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val BUILD_VERSION_NAME = "Build.VERSION"
        private const val BUILD_VERSION_CODES_NAME = "Build.VERSION_CODES"
        private const val BUILD_VERSION_CODES_CLASS = "android.os.$BUILD_VERSION_CODES_NAME"
        private const val SDK_INT_FIELD = "SDK_INT"

        private const val ANDROID_VERSION_PACKAGE = "${DeclaredSymbol.BASE_PACKAGE}.tool"
        private const val ANDROID_VERSION_NAME = "AndroidVersion"
        private const val ANDROID_VERSION_CLASS = "$ANDROID_VERSION_PACKAGE.$ANDROID_VERSION_NAME"
        private const val ANDROID_VERSION_CODE_FIELD = "code"

        private const val GREATER_OR_EQUALS_FUNCTION_NAME = "isAtLeast"
        private const val GREATER_FUNCTION_NAME = "isGreaterThan"
        private const val LESS_OR_EQUALS_FUNCTION_NAME = "isAtMost"
        private const val LESS_FUNCTION_NAME = "isLessThan"

        private val versionCodeMapping = mapOf(
            "BASE" to "B",
            "BASE_1_1" to "B_1_1",
            "CUPCAKE" to "C",
            "DONUT" to "D",
            "ECLAIR" to "E",
            "ECLAIR_0_1" to "E_0_1",
            "ECLAIR_MR1" to "E_MR1",
            "FROYO" to "FROYO",
            "GINGERBREAD" to "G",
            "GINGERBREAD_MR1" to "G_MR1",
            "HONEYCOMB" to "H",
            "HONEYCOMB_MR1" to "H_MR1",
            "HONEYCOMB_MR2" to "H_MR2",
            "ICE_CREAM_SANDWICH" to "I",
            "ICE_CREAM_SANDWICH_MR1" to "I_MR1",
            "JELLY_BEAN" to "J",
            "JELLY_BEAN_MR1" to "J_MR1",
            "JELLY_BEAN_MR2" to "J_MR2",
            "KITKAT" to "K",
            "KITKAT_WATCH" to "K_W",
            "LOLLIPOP" to "L",
            "LOLLIPOP_MR1" to "L_MR1",
            "M" to "M",
            "N" to "N",
            "N_MR1" to "N_MR1",
            "O" to "O",
            "O_MR1" to "O_MR1",
            "P" to "P",
            "Q" to "Q",
            "R" to "R",
            "S" to "S",
            "S_V2" to "S_V2",
            "TIRAMISU" to "T",
            "UPSIDE_DOWN_CAKE" to "U",
            "VANILLA_ICE_CREAM" to "V",
            "BAKLAVA" to "BAKLAVA"
        )

        val ISSUE = Issue.create(
            id = "ReplaceWithAndroidVersion",
            briefDescription = "Use system-extension's `AndroidVersion` instead of `Build.VERSION.SDK_INT`.",
            explanation = """
                Using `Build.VERSION.SDK_INT` comparisons can be simplified by using the `AndroidVersion` \
                from BetterAndroid system-extension library.

                The `AndroidVersion` provides:
                - Type-safe version constants (AndroidVersion.T, AndroidVersion.U, etc.)
                - Convenient comparison functions (isAtLeast, isGreaterThan, isAtMost, isLessThan)
                - Better readability and maintainability

                Examples:
                ```kotlin
                // Before
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Do something for Android T and above.
                }

                // After
                if (AndroidVersion.isAtLeast(AndroidVersion.T)) {
                    // Do something for Android T and above.
                }
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                AndroidVersionUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UBinaryExpression::class.java, UQualifiedReferenceExpression::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitBinaryExpression(node: UBinaryExpression) {
            // Detect comparison expression for `Build.VERSION.SDK_INT`.
            val leftOperand = node.leftOperand
            val rightOperand = node.rightOperand

            // Check if the left operand is `Build.VERSION.SDK_INT`.
            if (!isBuildVersionSdkInt(leftOperand)) return

            val operator = node.operator

            // Handle Kotlin's `in` operator for range checks.
            if (operator.text == "in") {
                val receiverText = rightOperand.asSourceString()
                val convertedRange = convertVersionCodesInRange(receiverText)
                val replacement = "$ANDROID_VERSION_NAME.$ANDROID_VERSION_CODE_FIELD in $convertedRange"
                reportAndFix(context, node, replacement, "$ANDROID_VERSION_NAME.$ANDROID_VERSION_CODE_FIELD in")
                return
            }

            val functionName = when (operator) {
                UastBinaryOperator.GREATER_OR_EQUALS -> GREATER_OR_EQUALS_FUNCTION_NAME
                UastBinaryOperator.GREATER -> GREATER_FUNCTION_NAME
                UastBinaryOperator.LESS_OR_EQUALS -> LESS_OR_EQUALS_FUNCTION_NAME
                UastBinaryOperator.LESS -> LESS_FUNCTION_NAME
                else -> return
            }

            val rightValue = convertVersionCode(rightOperand.asSourceString())
            val replacement = "$ANDROID_VERSION_NAME.$functionName($rightValue)"
            reportAndFix(context, node, replacement, "$ANDROID_VERSION_NAME.$functionName")
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            // Exclude cases already in binary expressions (those will be handled in visitBinaryExpression).
            if (node.uastParent is UBinaryExpression) return

            val selector = node.selector
            val resolved = (selector as? UReferenceExpression)?.resolve()
            val containingClass = (resolved as? PsiField)?.containingClass

            if (containingClass?.qualifiedName == BUILD_VERSION_CODES_CLASS) {
                val fieldName = selector.resolvedName ?: return
                val mappedName = versionCodeMapping[fieldName] ?: return

                val replacement = "$ANDROID_VERSION_NAME.$mappedName"
                reportAndFix(context, node, replacement, "$ANDROID_VERSION_NAME.$mappedName")
            }
        }

        private fun reportAndFix(context: JavaContext, node: UExpression, replacement: String, fixName: String) {
            val fix = LintFix.create()
                .name("Replace with '$fixName'")
                .replace()
                .all()
                .with(replacement)
                .reformat(true)
                .shortenNames()
                .imports(ANDROID_VERSION_CLASS)
                .build()

            val message = "Can be replaced with `$replacement`."

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = message,
                quickfixData = fix
            )
        }

        private fun isBuildVersionSdkInt(expression: UExpression): Boolean {
            // `Build.VERSION.SDK_INT` is a `UQualifiedReferenceExpression`.
            // Its structure is: Build.VERSION (receiver) -> SDK_INT (selector)
            if (expression !is UQualifiedReferenceExpression) return false

            // Check if selector is `SDK_INT`.
            val selector = expression.selector as? UReferenceExpression
            if (selector?.resolvedName != SDK_INT_FIELD) return false

            // Check if receiver is `Build.VERSION`.
            // `Build.VERSION` is also a `UQualifiedReferenceExpression`: Build -> VERSION
            val receiver = expression.receiver
            val receiverText = receiver.asSourceString()

            return receiverText == BUILD_VERSION_NAME || receiverText.endsWith(".$BUILD_VERSION_NAME")
        }

        private fun convertVersionCode(code: String): String {
            // If it is `Build.VERSION_CODES.?`, convert to `AndroidVersion.?`.
            if (code.startsWith("$BUILD_VERSION_CODES_NAME.")) {
                val fieldName = code.substringAfterLast(".")
                return versionCodeMapping[fieldName]?.let { "$ANDROID_VERSION_NAME.$it" } ?: code
            }

            // If already purely numeric or otherwise, leave it unchanged.
            return code
        }

        private fun convertVersionCodesInRange(rangeText: String): String {
            // Handle range like `33..36` or `Build.VERSION_CODES.TIRAMISU..Build.VERSION_CODES.BAKLAVA`
            val parts = rangeText.let {
                when {
                    it.contains("..") -> it.split("..") to ".."
                    it.contains("until") -> it.split("until") to " until "
                    else -> return rangeText
                }
            }
            if (parts.first.size != 2) return rangeText

            val start = convertVersionCode(parts.first[0].trim())
            val end = convertVersionCode(parts.first[1].trim())

            return "$start${parts.second}$end"
        }
    }
}
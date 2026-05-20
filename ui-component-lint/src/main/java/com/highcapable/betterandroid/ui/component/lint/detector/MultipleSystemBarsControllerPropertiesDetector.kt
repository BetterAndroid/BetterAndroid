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
import com.highcapable.betterandroid.ui.component.lint.detector.extension.buildDeleteFix
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

class MultipleSystemBarsControllerPropertiesDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val SYSTEM_BARS_CONTROLLER_CLASS_NAME = SystemBarsControllerUsageDetector.SYSTEM_BARS_CONTROLLER_CLASS_NAME
        private const val SYSTEM_BARS_CONTROLLER_CLASS = SystemBarsControllerUsageDetector.SYSTEM_BARS_CONTROLLER_CLASS

        val ISSUE = Issue.create(
            id = "MultipleSystemBarsControllerProperties",
            briefDescription = "Only one SystemBarsController property is allowed in a class.",
            explanation = """
                Declaring multiple `SystemBarsController` properties in the same class can make the \
                ownership of system bars ambiguous and is not supported by BetterAndroid ui-component library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-component#system-bars-status-bars-navigation-bars-etc
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-component#system-bars-status-bars-navigation-bars-etc

                The `SystemBarsController.kt` provides:
                - A single controller entry for the current `Window`
                - Unified control of system bar styles, visibility and behavior
                - A clear ownership model for the current class

                Examples:
                ```kotlin
                // Before
                class DemoActivity : AppCompatActivity() {
                    val systemBars by lazy { SystemBarsController.from(window) }
                    val anotherSystemBars by lazy { SystemBarsController.from(window) }
                }

                // After
                class DemoActivity : AppCompatActivity() {
                    val systemBars by lazy { SystemBarsController.from(window) }
                }
                ```
            """.trimIndent(),
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(
                MultipleSystemBarsControllerPropertiesDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UClass::class.java as Class<out UElement>)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitClass(node: UClass) {
            val members = buildList {
                node.javaPsi.fields.filter { it.type.canonicalText == SYSTEM_BARS_CONTROLLER_CLASS }
                    .forEach { add(Triple(it.name, it.navigationElement, it.navigationElement)) }
                node.javaPsi.methods.filter {
                    it.parameterList.parametersCount == 0 &&
                        it.name.startsWith("get") &&
                        it.returnType?.canonicalText == SYSTEM_BARS_CONTROLLER_CLASS
                }.forEach {
                    val propertyName = it.name.removePrefix("get").replaceFirstChar(Char::lowercaseChar)
                    add(Triple(propertyName, it.navigationElement, it.navigationElement))
                }
            }.distinctBy { it.first }
            if (members.size <= 1) return

            members.forEach { (propertyName, target, deleteTarget) ->
                context.report(
                    issue = ISSUE,
                    location = context.getLocation(target),
                    message = "Only one `$SYSTEM_BARS_CONTROLLER_CLASS_NAME` property can exist in the same class.",
                    quickfixData = buildDeleteFix(
                        name = "Delete '$propertyName'",
                        location = context.getLocation(deleteTarget)
                    )
                )
            }
        }
    }
}
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
 * This file is created by fankes on 2026/5/20.
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
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.createKotlinOnlyUastHandler
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.displayShortName
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.extendsClass
import com.highcapable.betterandroid.ui.extension.lint.detector.extension.findEnclosingCall
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

class ViewBindingUsageDetector : Detector(), Detector.UastScanner {

    companion object {

        private const val VIEW_BINDING_CLASS_NAME = "ViewBinding"
        private const val VIEW_BINDING_CLASS = "androidx.viewbinding.$VIEW_BINDING_CLASS_NAME"
        private const val CLASS_CLASS = "java.lang.Class"
        private const val GET_METHOD = "getMethod"
        private const val GET_DECLARED_METHOD = "getDeclaredMethod"
        private const val METHODS_PROPERTY = "methods"
        private const val DECLARED_METHODS_PROPERTY = "declaredMethods"
        private const val METHOD_NAME_PROPERTY = "name"
        private const val INFLATE_METHOD = "inflate"
        private const val BIND_METHOD = "bind"
        private const val CLASS_OF_METHOD = "classOf"
        private const val CLASS_LITERAL_SUFFIX = "::class.java"
        private const val VIEW_BINDING_BUILDER_NAME = "ViewBindingBuilder"

        val ISSUE = Issue.create(
            id = "ReplaceWithViewBindingExtension",
            briefDescription = "Use ui-extension's ViewBinding extensions instead.",
            explanation = """
                Using reflection to resolve `ViewBinding.inflate(...)` or `ViewBinding.bind(...)` \
                can be simplified by using ViewBinding extensions from BetterAndroid ui-extension library.

                See the documentation for more details:
                - English: https://betterandroid.github.io/BetterAndroid/en/library/ui-extension#viewbinding-extension
                - 简体中文: https://betterandroid.github.io/BetterAndroid/zh-cn/library/ui-extension#viewbinding-extension

                The `ViewBinding.kt` provides:
                - A direct `ViewBinding<VB>()` builder API
                - Direct `Context.viewBinding(...)` and `Fragment.viewBinding(...)` delegate APIs
                - `ViewBindingBuilder.fromGeneric(...)` for generic parent class scenarios
                - Better readability and maintainability without manually handling reflection

                Examples:
                ```kotlin
                // Before
                val binding by lazy { ActivityMainBinding.inflate(context.layoutInflater, ...) }
                ActivityMainBinding::class.java.getMethod(
                    "inflate",
                    LayoutInflater::class.java
                ).apply {
                    isAccessible = true
                }.invoke(null, context.layoutInflater, ...)
                bindingClass.getDeclaredMethod("bind", View::class.java).apply {
                    isAccessible = true
                }.invoke(null, view)

                // After
                val binding by context.viewBinding<ActivityMainBinding>()
                ViewBinding<ActivityMainBinding>().inflate(context.layoutInflater, ...)
                ViewBinding(bindingClass).bind(view)
                ```
            """.trimIndent(),
            category = Category.USABILITY,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ViewBindingUsageDetector::class.java,
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
            if (!node.isViewBindingMethodLookup(context) && !node.isViewBindingMethodsIteration(context)) return

            // This is the ViewBinding reflection method lookup or iteration pattern.
            report(node, context)
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
            if (!node.isViewBindingMethodNameCheck(context)) return

            report(node, context)
        }

        private fun report(node: UElement, context: JavaContext) {
            val suggestion = node.resolveViewBindingSuggestion()

            context.report(
                issue = ISSUE,
                location = context.getLocation(node),
                message = "Consider handing this reflection over to `$suggestion` or `$VIEW_BINDING_BUILDER_NAME`."
            )
        }

        private fun UElement.resolveViewBindingSuggestion(): String {
            val classExpression = when (this) {
                is UCallExpression -> resolveViewBindingClassExpression()
                is UQualifiedReferenceExpression -> findEnclosingCall()?.resolveViewBindingClassExpression()
                else -> null
            } ?: return "$VIEW_BINDING_CLASS_NAME<VB>()"

            return classExpression.toViewBindingSuggestion(context)
        }

        private fun UCallExpression.resolveViewBindingClassExpression() = when {
            isViewBindingMethodLookup(context) -> receiver
            isViewBindingMethodsIteration(context) -> (receiver as? UQualifiedReferenceExpression)?.receiver
            else -> null
        }

        private fun UExpression.toViewBindingSuggestion(context: JavaContext): String {
            resolveConcreteViewBindingName(context)?.let { return "$VIEW_BINDING_CLASS_NAME<$it>()" }
            return "$VIEW_BINDING_CLASS_NAME(${asSourceString().trim()})"
        }

        private fun UExpression.resolveConcreteViewBindingName(context: JavaContext): String? {
            resolveConcreteViewBindingNameFromSource()?.let { return it }

            val classType = getExpressionType() as? PsiClassType ?: return null
            val bindingType = classType.parameters.firstOrNull { it.extendsClass(context, VIEW_BINDING_CLASS) } ?: return null
            val bindingClassType = bindingType as? PsiClassType ?: return null
            val psiClass = bindingClassType.resolve() ?: return null
            if (!context.evaluator.extendsClass(psiClass, VIEW_BINDING_CLASS, false)) return null
            if (psiClass.qualifiedName == VIEW_BINDING_CLASS) return null

            return bindingType.presentableText.displayShortName()
        }

        private fun UExpression.resolveConcreteViewBindingNameFromSource(): String? {
            val source = asSourceString().trim()
            if (source.endsWith(CLASS_LITERAL_SUFFIX)) return source.removeSuffix(CLASS_LITERAL_SUFFIX)

            val methodName = (this as? UCallExpression)?.methodName
            if (methodName == CLASS_OF_METHOD) {
                val generic = source.substringAfter('<', "").substringBeforeLast('>', "")
                if (generic.isNotBlank()) return generic.trim()
            }

            return null
        }

        private fun UCallExpression.isViewBindingMethodLookup(context: JavaContext): Boolean {
            if (methodName != GET_METHOD && methodName != GET_DECLARED_METHOD) return false

            val method = resolve() ?: return false
            if (!context.evaluator.isMemberInClass(method, CLASS_CLASS)) return false

            val methodName = valueArguments.firstOrNull()?.evaluate() as? String ?: return false
            if (methodName != INFLATE_METHOD && methodName != BIND_METHOD) return false

            return isViewBindingReflection(context)
        }

        private fun UCallExpression.isViewBindingMethodsIteration(context: JavaContext): Boolean {
            val receiver = receiver as? UQualifiedReferenceExpression ?: return false
            if (!receiver.isViewBindingMethodsAccess(context)) return false

            val source = asSourceString()
            if (!source.contains(METHOD_NAME_PROPERTY)) return false

            return source.containsMethodName(INFLATE_METHOD) || source.containsMethodName(BIND_METHOD)
        }

        private fun UQualifiedReferenceExpression.isViewBindingMethodNameCheck(context: JavaContext): Boolean {
            if ((selector as? USimpleNameReferenceExpression)?.identifier != METHOD_NAME_PROPERTY) return false

            val rootCall = findEnclosingCall() ?: return false
            return rootCall.isViewBindingMethodsIteration(context)
        }

        private fun UCallExpression.isViewBindingReflection(context: JavaContext): Boolean {
            val source = receiver?.asSourceString().orEmpty()
            if (source.contains(VIEW_BINDING_CLASS) || source.contains(VIEW_BINDING_CLASS_NAME)) return true

            return receiverType.isViewBindingClassType(context)
        }

        private fun UQualifiedReferenceExpression.isViewBindingMethodsAccess(context: JavaContext): Boolean {
            val selectorName = (selector as? USimpleNameReferenceExpression)?.identifier ?: return false
            if (selectorName != METHODS_PROPERTY && selectorName != DECLARED_METHODS_PROPERTY) return false

            return receiver.getExpressionType().isViewBindingClassType(context)
        }

        private fun PsiType?.isViewBindingClassType(context: JavaContext): Boolean {
            if (this.extendsClass(context, VIEW_BINDING_CLASS)) return true
            if (this?.canonicalText?.contains(VIEW_BINDING_CLASS) == true) return true

            val classType = this as? PsiClassType ?: return false
            return classType.parameters.any { it.extendsClass(context, VIEW_BINDING_CLASS) }
        }

        private fun String.containsMethodName(name: String) = contains("\"$name\"")
    })
}
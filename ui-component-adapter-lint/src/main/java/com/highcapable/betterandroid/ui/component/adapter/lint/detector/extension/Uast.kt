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
 * This file is created by fankes on 2026/5/14.
 */
package com.highcapable.betterandroid.ui.component.adapter.lint.detector.extension

import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.util.PsiTypesUtil
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UDeclaration
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.toUElementOfType

internal fun UElement?.unwrapParenthesized(): UElement? {
    var current = this
    while (current is UParenthesizedExpression) current = current.expression

    return current
}

internal fun UElement?.asCall() = when (this) {
    is UCallExpression -> this
    is UQualifiedReferenceExpression -> selector as? UCallExpression
    else -> null
}

internal fun UCallExpression.findContainingBlock(): UBlockExpression? {
    var parent = uastParent
    while (parent != null) {
        if (parent is UBlockExpression) return parent
        parent = parent.uastParent
    }

    return null
}

internal fun UElement?.resolveLocalVariableInitializer(): UElement? {
    val reference = this.unwrapParenthesized() as? USimpleNameReferenceExpression ?: return null
    val localVariable = when (val resolved = reference.resolve()) {
        is ULocalVariable -> resolved
        is PsiLocalVariable -> resolved.toUElementOfType<ULocalVariable>()
        else -> null
    } ?: return null

    return localVariable.uastInitializer.unwrapParenthesized()
}

internal fun UObjectLiteralExpression.findMethodExpressionSource(name: String, suffixToTrim: String? = null): String? {
    val method = declaration.findMethod(name) ?: return null
    val body = method.uastBody ?: return null
    val source = when (body) {
        is UBlockExpression -> body.expressions.singleOrNull()?.asSourceString()
        else -> body.asSourceString()
    }?.removePrefix("return ")?.removeSuffix(";")?.trim() ?: return null

    return if (suffixToTrim != null) source.removeSuffix(".$suffixToTrim") else source
}

internal fun UDeclaration.findMethod(name: String) =
    (this as? UClass)?.methods?.firstOrNull { it.name == name }

internal fun UObjectLiteralExpression.isObjectLiteralOf(className: String): Boolean {
    val psiClass = PsiTypesUtil.getPsiClass(getExpressionType()) ?: return false
    return psiClass.qualifiedName == className || psiClass.supers.any { it.qualifiedName == className }
}
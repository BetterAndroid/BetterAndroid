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
package com.highcapable.betterandroid.ui.extension.lint.detector.extension

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTypesUtil
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UCallableReferenceExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UClassLiteralExpression
import org.jetbrains.uast.UDeclaration
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UObjectLiteralExpression
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UPostfixExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.UReturnExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UThisExpression
import org.jetbrains.uast.toUElementOfType

internal fun UElement?.resolveName() = when (this) {
    is USimpleNameReferenceExpression -> identifier
    is UCallExpression -> methodName
    is UCallableReferenceExpression -> callableName
    is UResolvable -> (resolve() as? PsiNamedElement?)?.name
    else -> null
}

internal fun UElement?.unwrapParenthesized(): UElement? {
    var current = this
    while (current is UParenthesizedExpression) current = current.expression

    return current
}

internal fun UElement?.unwrapParenthesizedOrNotNull(): UElement? {
    var current = this
    while (true) {
        current = when (current) {
            is UParenthesizedExpression -> current.expression
            is UPostfixExpression ->
                if (current.operator.text == "!!") current.operand else return current
            else -> return current
        }
    }
}

internal fun UElement?.asCall() = when (this) {
    is UCallExpression -> this
    is UQualifiedReferenceExpression -> selector as? UCallExpression
    else -> null
}

internal fun USimpleNameReferenceExpression.isQualifiedSelector() =
    (uastParent as? UQualifiedReferenceExpression)?.selector == this

internal fun UCallExpression.receiverPrefix(): String {
    val receiverText = receiver?.asSourceString() ?: return ""
    val source = (uastParent as? UQualifiedReferenceExpression)?.sourcePsi?.text?.trimStart()
        ?: sourcePsi?.text?.trimStart()
        ?: asSourceString().trimStart()

    return if (source.startsWith("$receiverText.")) "$receiverText." else ""
}

internal fun UDeclaration.findMethod(name: String) =
    (this as? UClass)?.methods?.firstOrNull { it.name == name }

internal fun UElement.findContainingStatement(): UExpression? {
    var current: UElement? = this
    while (current != null) {
        val parent = current.uastParent
        if (parent is UBlockExpression && current is UExpression) return current
        current = parent
    }
    return null
}

internal fun UElement.findEnclosingCall(): UCallExpression? {
    var current = uastParent
    while (current != null) {
        if (current is UCallExpression) return current
        current = current.uastParent
    }
    return null
}

internal fun UObjectLiteralExpression.isObjectLiteralOf(className: String): Boolean {
    val psiClass = PsiTypesUtil.getPsiClass(getExpressionType()) ?: return false
    return psiClass.qualifiedName == className || psiClass.supers.any { it.qualifiedName == className }
}

internal fun UElement.getContainingPsiClass(): PsiClass? {
    var current = uastParent
    while (current != null) {
        if (current.javaPsi is PsiClass) return current.javaPsi as PsiClass
        current = current.uastParent
    }
    return null
}

internal fun List<UExpression>.joinSourceArguments(startIndex: Int = 0) =
    drop(startIndex).joinToString(", ") { it.asSourceString() }

internal fun UExpression.asPropertyAccess(name: String): String {
    val source = asSourceString()
    return if (unwrapParenthesized() is UThisExpression) name else "$source.$name"
}

internal tailrec fun UElement?.resolveStaticClassLiteralType(): String? = when (val target = unwrapParenthesized()) {
    is UClassLiteralExpression -> target.type?.canonicalText
    is UQualifiedReferenceExpression -> (target.receiver as? UClassLiteralExpression)?.type?.canonicalText
    is USimpleNameReferenceExpression -> {
        val localVariable = when (val resolved = target.resolve()) {
            is ULocalVariable -> resolved
            is PsiLocalVariable -> resolved.toUElementOfType<ULocalVariable>()
            else -> null
        } ?: return null

        localVariable.uastInitializer.resolveStaticClassLiteralType()
    }
    else -> null
}

internal fun UExpression.unwrapReturnedExpression() = when (this) {
    is UReturnExpression -> returnExpression?.unwrapParenthesized() as? UExpression ?: this
    else -> this
}

internal fun String.displayShortName() = substringAfterLast('.')
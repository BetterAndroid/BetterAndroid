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
package com.highcapable.betterandroid.system.extension.lint.detector.extension

import com.intellij.psi.PsiNamedElement
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UCallableReferenceExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UParenthesizedExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UResolvable
import org.jetbrains.uast.USimpleNameReferenceExpression

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

internal fun UElement?.asCall() = when (this) {
    is UCallExpression -> this
    is UQualifiedReferenceExpression -> selector as? UCallExpression
    else -> null
}

internal fun UCallExpression.receiverPrefix(): String {
    val receiverText = receiver?.asSourceString() ?: return ""
    val source = (uastParent as? UQualifiedReferenceExpression)?.sourcePsi?.text?.trimStart()
        ?: sourcePsi?.text?.trimStart()
        ?: asSourceString().trimStart()

    return if (source.startsWith("$receiverText.")) "$receiverText." else ""
}

internal fun UElement?.findContainingUClass(): UClass? {
    var current = this
    while (current != null) {
        if (current is UClass) return current
        current = current.uastParent
    }
    return null
}

internal fun String.displayShortName() = substringAfterLast('.')
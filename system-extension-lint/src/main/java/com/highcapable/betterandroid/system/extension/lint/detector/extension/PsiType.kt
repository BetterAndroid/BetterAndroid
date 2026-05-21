/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019 HighCapable
 * https://github.com/BetterAndroid/BetterAndroid
 *
 * Apache License Version 2.0
 *
 * Licensed under the Apache License (the "License");
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
 * This file is created by fankes on 2026/5/21.
 */
package com.highcapable.betterandroid.system.extension.lint.detector.extension

import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiType

internal fun PsiType?.extendsClass(context: JavaContext, qualifiedName: String): Boolean {
    if (this == null) return false
    if (canonicalText == qualifiedName) return true
    val classType = this as? PsiClassType ?: return false
    val psiClass = classType.resolve() ?: return false

    return context.evaluator.extendsClass(psiClass, qualifiedName, false)
}

internal fun PsiType?.resolveJavaClassTypeArgument(): String? {
    val classType = this as? PsiClassType ?: return null
    if (classType.rawType().canonicalText != "java.lang.Class") return null

    return classType.parameters.firstOrNull()?.canonicalText
}
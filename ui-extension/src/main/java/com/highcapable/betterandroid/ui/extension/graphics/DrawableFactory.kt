/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2023 HighCapable
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
 * This file is created by fankes on 2022/10/22.
 */
@file:Suppress("unused")
@file:JvmName("DrawableUtils")

package com.highcapable.betterandroid.ui.extension.graphics

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.Px
import com.highcapable.betterandroid.system.extension.tool.SystemVersion

/**
 * Set drawable padding (compat).
 *
 * - Require Android 10 (29).
 * @receiver the current gradient drawable.
 * @param left the left (px).
 * @param top the top (px).
 * @param right the right (px).
 * @param bottom the bottom (px).
 */
fun GradientDrawable.setPaddingCompat(@Px left: Int, @Px top: Int, @Px right: Int, @Px bottom: Int) =
    SystemVersion.require(SystemVersion.Q) { setPadding(left, top, right, bottom) }

/**
 * Set drawable padding.
 *
 * - Only support [ShapeDrawable] and [GradientDrawable].
 * @receiver the current drawable.
 * @param left the left (px).
 * @param top the top (px).
 * @param right the right (px).
 * @param bottom the bottom (px).
 */
fun Drawable.setPadding(@Px left: Int, @Px top: Int, @Px right: Int, @Px bottom: Int) {
    when (this) {
        is ShapeDrawable -> setPadding(left, top, right, bottom)
        is GradientDrawable -> setPaddingCompat(left, top, right, bottom)
    }
}

/**
 * Set drawable padding.
 * @receiver the current drawable.
 * @param size the size of the top, left, bottom and right (px).
 */
fun Drawable.setPadding(@Px size: Int) = setPadding(size, size, size, size)
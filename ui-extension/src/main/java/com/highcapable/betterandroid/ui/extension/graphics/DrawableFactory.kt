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
@file:Suppress("unused", "USELESS_CAST")
@file:JvmName("DrawableUtils")

package com.highcapable.betterandroid.ui.extension.graphics

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.annotation.Px
import com.highcapable.betterandroid.system.extension.tool.SystemVersion

/**
 * A [GradientDrawable] with padding (compat).
 * @see GradientDrawable.setPaddingCompat
 * @see GradientDrawable
 */
open class GradientDrawableCompat : GradientDrawable() {

    /** The padding of the drawable. */
    private var padding: Rect? = null

    override fun getPadding(padding: Rect) =
        if (this.padding != null && this.padding != padding)
            this.padding?.let { padding.set(it); true } ?: false
        else super.getPadding(padding)

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if (SystemVersion.isLowTo(SystemVersion.Q)) {
            if (padding == null) padding = Rect()
            padding?.set(left, top, right, bottom)
            invalidateSelf()
        } else super.setPadding(left, top, right, bottom)
    }
}

/**
 * Set drawable padding (compat).
 *
 * - If target sdk lower than Android 10 (29),
 *   you need to use [GradientDrawableCompat] for backward compatibility.
 * @see GradientDrawableCompat
 * @receiver the current gradient drawable.
 * @param left the left (px).
 * @param top the top (px).
 * @param right the right (px).
 * @param bottom the bottom (px).
 */
fun GradientDrawable.setPaddingCompat(@Px left: Int, @Px top: Int, @Px right: Int, @Px bottom: Int) {
    when (this) {
        is GradientDrawableCompat -> (this as GradientDrawableCompat).setPadding(left, top, right, bottom)
        else -> SystemVersion.require(SystemVersion.Q) { setPadding(left, top, right, bottom) }
    }
}

/**
 * Set drawable padding.
 *
 * - Note: Only listed [Drawable]s are supported.
 * @see RippleDrawable
 * @see LayerDrawable
 * @see ShapeDrawable
 * @see GradientDrawable
 * @receiver the current drawable.
 * @param left the left (px).
 * @param top the top (px).
 * @param right the right (px).
 * @param bottom the bottom (px).
 */
fun Drawable.setPadding(@Px left: Int, @Px top: Int, @Px right: Int, @Px bottom: Int) {
    when (this) {
        is RippleDrawable -> setPadding(left, top, right, bottom)
        is LayerDrawable -> setPadding(left, top, right, bottom)
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
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
 * This file is created by fankes on 2023/12/7.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreGraphics.CGFloatVar
import platform.UIKit.UIColor

/**
 * Convert [Color] to [UIColor].
 * @receiver the current color.
 * @return [UIColor]
 */
@Stable
fun Color.toPlatformColor() =
    if (isSpecified) UIColor(
        red = red.toDouble(),
        green = green.toDouble(),
        blue = blue.toDouble(),
        alpha = alpha.toDouble()
    ) else DefaultPlatformColor

/**
 * Convert [UIColor] to [Color].
 *
 * - Note: If the [UIColor.getRed] returns false, the [Color.Unspecified] will be returned.
 * @receiver the current color.
 * @return [Color]
 */
@Stable
fun UIColor.toComposeColor(): Color {
    val red = nativeHeap.alloc<CGFloatVar>()
    val green = nativeHeap.alloc<CGFloatVar>()
    val blue = nativeHeap.alloc<CGFloatVar>()
    val alpha = nativeHeap.alloc<CGFloatVar>()

    return if (getRed(red.ptr, green.ptr, blue.ptr, alpha.ptr)) Color(
        red = red.value.toFloat(),
        green = green.value.toFloat(),
        blue = blue.value.toFloat(),
        alpha = alpha.value.toFloat()
    ) else Color.Unspecified
}

/**
 * Determine whether the current color is a bright tone.
 * @receiver the current color.
 * @return [Boolean]
 */
@Stable
val UIColor.isBrightColor get() = toComposeColor().isBrightColor

/**
 * Convert [UIColor] to hex string.
 * @receiver the current color.
 * @return [String]
 */
@Stable
fun UIColor.toHexColor() = toComposeColor().toHexColor()

/**
 * Mix two colors.
 * @param color1 the first color.
 * @param color2 the second color.
 * @param ratio the mixing ratio, default 0.5f.
 * @return [UIColor] mixed color.
 */
@Stable
fun mixColorOf(color1: UIColor, color2: UIColor, ratio: Float = 0.5f) =
    mixColorOf(color1.toComposeColor(), color2.toComposeColor(), ratio).toPlatformColor()

/** Default platform color. */
private val DefaultPlatformColor = UIColor(red = 0.0, green = 0.0, blue = 0.0, alpha = 0.0)
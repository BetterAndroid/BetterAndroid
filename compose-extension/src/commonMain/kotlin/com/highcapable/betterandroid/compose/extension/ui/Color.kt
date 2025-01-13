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
 * This file is created by fankes on 2023/12/4.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.isUnspecified

/**
 * Determine whether the current color is a bright tone.
 * @receiver the current color.
 * @return [Boolean]
 */
@Stable
val Color.isBrightColor get() = runCatching {
    if (isUnspecified) return@runCatching false
    ((299 * red + 587 * green + 114 * blue) / 1000) >= 0.5f
}.getOrNull() ?: false

/**
 * Convert [Color] to hex string.
 * @receiver the current color.
 * @return [String]
 */
@Stable
fun Color.toHexColor() = runCatching {
    if (isUnspecified) return@runCatching "#00000000"
    val a = (alpha * 255).toInt().toString(16).padStart(2, '0')
    val r = (red * 255).toInt().toString(16).padStart(2, '0')
    val g = (green * 255).toInt().toString(16).padStart(2, '0')
    val b = (blue * 255).toInt().toString(16).padStart(2, '0')
    "#${a.uppercase()}${r.uppercase()}${g.uppercase()}${b.uppercase()}"
}.getOrNull() ?: "#00000000"

/**
 * Mix two colors.
 * @param color1 the first color.
 * @param color2 the second color.
 * @param ratio the mixing ratio, default 0.5f.
 * @return [Color] mixed color.
 */
@Stable
fun mixColorOf(color1: Color, color2: Color, ratio: Float = 0.5f): Color {
    if (color1.isUnspecified || color2.isUnspecified) return Color.Unspecified
    val inverseRatio = 1 - ratio
    return Color(
        alpha = color1.alpha * inverseRatio + color2.alpha * ratio,
        red = color1.red * inverseRatio + color2.red * ratio,
        green = color1.green * inverseRatio + color2.green * ratio,
        blue = color1.blue * inverseRatio + color2.blue * ratio
    )
}

/**
 * [Color] extension, if [Color] is not specified, return null.
 * @receiver the current color.
 * @return [Color] or null.
 */
@Stable
fun Color.orNull() = if (isSpecified) this else null
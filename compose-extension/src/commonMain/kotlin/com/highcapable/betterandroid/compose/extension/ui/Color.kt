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
 * This file is created by fankes on 2023/12/4.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.isUnspecified

/**
 * Create a translucent color.
 * @receiver [Color.Companion]
 * @return [Color]
 */
@Stable
val Color.Companion.Translucent get() = Color(0x80000000)

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
 * Convert integer color to hex string.
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
 * Converts to mix color.
 * @receiver the current color.
 * @param color the color to mix with.
 * @param ratio the mixing ratio, default 0.5f.
 * @return [Color] mixed color.
 */
@Stable
fun Color.toMixColor(color: Color, ratio: Float = 0.5f): Color {
    if (isUnspecified || color.isUnspecified) return Color.Unspecified
    val inverseRatio = 1 - ratio
    return Color(
        alpha = alpha * inverseRatio + color.alpha * ratio,
        red = red * inverseRatio + color.red * ratio,
        green = green * inverseRatio + color.green * ratio,
        blue = blue * inverseRatio + color.blue * ratio
    )
}

/**
 * [Color] extension, if [Color] is not specified, return null.
 * @return [Color] or null.
 */
@Stable
fun Color.orNull() = if (isSpecified) this else null
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
 * This file is created by fankes on 2023/12/8.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import java.awt.Color as AwtColor

/**
 * Convert [Color] to [AwtColor].
 * @receiver the current color.
 * @return [AwtColor]
 */
@Stable
fun Color.toPlatformColor() =
    if (isSpecified) AwtColor(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt(),
        (alpha * 255).toInt()
    ) else DefaultPlatformColor

/**
 * Convert [AwtColor] to [Color].
 * @receiver the current color.
 * @return [Color]
 */
@Stable
fun AwtColor.toComposeColor() = Color(
    red = red / 255f,
    green = green / 255f,
    blue = blue / 255f,
    alpha = alpha / 255f
)

/**
 * Determine whether the current color is a bright tone.
 * @receiver the current color.
 * @return [Boolean]
 */
@Stable
val AwtColor.isBrightColor get() = toComposeColor().isBrightColor

/**
 * Convert [AwtColor] to hex string.
 * @receiver the current color.
 * @return [String]
 */
@Stable
fun AwtColor.toHexColor() = toComposeColor().toHexColor()

/**
 * Mix two colors.
 * @param color1 the first color.
 * @param color2 the second color.
 * @param ratio the mixing ratio, default 0.5f.
 * @return [AwtColor] mixed color.
 */
@Stable
fun mixColorOf(color1: AwtColor, color2: AwtColor, ratio: Float = 0.5f) =
    mixColorOf(color1.toComposeColor(), color2.toComposeColor(), ratio).toPlatformColor()

/** Default platform color. */
private val DefaultPlatformColor = AwtColor(0)
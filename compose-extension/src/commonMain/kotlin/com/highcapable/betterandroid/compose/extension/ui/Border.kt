/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2024 HighCapable
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified

/**
 * Workaround for [BorderStroke.width] is 0.dp like [Dp.Hairline], which will cause the [Modifier.border] still draw a border,
 * see [here](https://stackoverflow.com/questions/72514987/unexpected-border-in-composables-border-shows-even-if-border-width-is-zero).
 *
 * This function will stop draw the border if the [width] is 0.dp.
 * @see border
 * @receiver [Modifier]
 * @param width the border stroke width.
 * @param brush the border stroke brush.
 * @param shape the shape of the border stroke, default is [RectangleShape].
 * @return [Modifier]
 */
fun Modifier.borderOrElse(width: Dp, brush: Brush, shape: Shape = RectangleShape) =
    if (width.isSpecified && width > 0.dp) border(width, brush, shape) else this

/**
 * Workaround for [BorderStroke.width] is 0.dp like [Dp.Hairline], you can see another [borderOrElse] for more details.
 * @see borderOrElse
 * @receiver [Modifier]
 * @param width the border stroke width.
 * @param color the border stroke solid color.
 * @param shape the shape of the border stroke, default is [RectangleShape].
 * @return [Modifier]
 */
fun Modifier.borderOrElse(width: Dp, color: Color, shape: Shape = RectangleShape) = borderOrElse(width, SolidColor(color), shape)

/**
 * Workaround for [BorderStroke.width] is 0.dp like [Dp.Hairline], you can see another [borderOrElse] for more details.
 * @see borderOrElse
 * @receiver [Modifier]
 * @param border the border stroke.
 * @param shape the shape of the border stroke, default is [RectangleShape].
 * @return [Modifier]
 */
fun Modifier.borderOrElse(border: BorderStroke, shape: Shape = RectangleShape) = borderOrElse(border.width, border.brush, shape)
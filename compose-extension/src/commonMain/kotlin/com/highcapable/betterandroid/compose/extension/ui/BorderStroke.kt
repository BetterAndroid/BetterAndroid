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

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

/**
 * Returns the solid color of the [BorderStroke.brush], or [Color.Unspecified] if the [BorderStroke.brush] is not a [SolidColor].
 * @return [Color]
 */
@Stable
val BorderStroke.solidColor get() = solidColor()

/**
 * Returns the solid color of the [BorderStroke.brush], or [default] if the [BorderStroke.brush] is not a [SolidColor].
 * @param default the default color, default is unspecified.
 * @return [Color]
 */
@Stable
fun BorderStroke.solidColor(default: Color = Color.Unspecified) = (brush as? SolidColor?)?.value ?: default
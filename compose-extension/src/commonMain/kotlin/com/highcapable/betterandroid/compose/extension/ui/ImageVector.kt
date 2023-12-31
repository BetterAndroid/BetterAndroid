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
 * This file is created by fankes on 2023/11/11.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultGroupName
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

/**
 * Create an [ImageVector] with [ImageVector.Builder].
 * @param name the name of the image vector, default is [DefaultGroupName].
 * @param defaultWidth the default width of the image vector.
 * @param defaultHeight the default height of the image vector.
 * @param viewportWidth the width of the image vector's viewport.
 * @param viewportHeight the height of the image vector's viewport.
 * @param tintColor the tint color of the image vector, default is unspecified.
 * @param tintBlendMode the tint blend mode of the image vector, default is [BlendMode.SrcIn].
 * @param autoMirror the auto mirror of the image vector, default false.
 * @param builder the builder instance.
 * @return [ImageVector]
 */
@Stable
inline fun ImageVector(
    name: String = DefaultGroupName,
    defaultWidth: Dp,
    defaultHeight: Dp,
    viewportWidth: Float,
    viewportHeight: Float,
    tintColor: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    autoMirror: Boolean = false,
    builder: ImageVector.Builder.() -> Unit
) = ImageVector.Builder(
    name = name,
    defaultWidth = defaultWidth,
    defaultHeight = defaultHeight,
    viewportWidth = viewportWidth,
    viewportHeight = viewportHeight,
    tintColor = tintColor,
    tintBlendMode = tintBlendMode,
    autoMirror = autoMirror
).apply(builder).build()
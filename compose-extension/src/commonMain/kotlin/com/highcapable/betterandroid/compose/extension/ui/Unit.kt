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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.isSpecified
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified

/**
 * [Dp] extension, if [Dp] is not specified, return null.
 * @receiver [Dp]
 * @return [Dp] or null.
 */
@Stable
fun Dp.orNull() = if (isSpecified) this else null

/**
 * [DpSize] extension, if [DpSize] is not specified, return null.
 * @receiver [DpSize]
 * @return [DpSize] or null.
 */
@Stable
fun DpSize.orNull() = if (isSpecified) this else null

/**
 * [TextUnit] extension, if [TextUnit] is not specified, return null.
 * @receiver [TextUnit]
 * @return [TextUnit] or null.
 */
@Stable
fun TextUnit.orNull() = if (isSpecified) this else null

/**
 * [Size] extension, if [Size] is not specified, return null.
 * @receiver [Size]
 * @return [Size] or null.
 */
@Stable
fun Size.orNull() = if (isSpecified) this else null

/**
 * [Offset] extension, if [Offset] is not specified, return null.
 * @receiver [Offset]
 * @return [Offset] or null.
 */
@Stable
fun Offset.orNull() = if (isSpecified) this else null

/**
 * [DpOffset] extension, if [DpOffset] is not specified, return null.
 * @receiver [DpOffset]
 * @return [DpOffset] or null.
 */
@Stable
fun DpOffset.orNull() = if (isSpecified) this else null

/**
 * [ScaleFactor] extension, if [ScaleFactor] is not specified, return null.
 * @receiver [ScaleFactor]
 * @return [ScaleFactor] or null.
 */
@Stable
fun ScaleFactor.orNull() = if (isSpecified) this else null
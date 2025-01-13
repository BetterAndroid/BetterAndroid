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
 * This file is created by fankes on 2023/10/26.
 */
@file:Suppress("unused")
@file:JvmName("WindowUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.view.Window
import android.view.WindowManager

/**
 * Update window's attributes.
 * @receiver the current window.
 * @param initiate the [WindowManager.LayoutParams] builder body.
 */
inline fun Window.updateLayoutParams(initiate: WindowManager.LayoutParams.() -> Unit) {
    val params = attributes
    initiate(params)
    attributes = params
}

/**
 * Update the [WindowManager.LayoutParams.screenBrightness].
 * @receiver the current window.
 * @param progress the brightness, must between 0..100.
 */
fun Window.updateScreenBrightness(progress: Int) = updateLayoutParams { screenBrightness = progress.coerceIn(0, 100) / 100f }

/**
 * Update the [WindowManager.LayoutParams.screenBrightness].
 * @receiver the current window.
 * @param progress the brightness, must between 0f..1f.
 */
fun Window.updateScreenBrightness(progress: Float) = updateLayoutParams { screenBrightness = progress.coerceIn(0f, 1f) }

/**
 * Clear the [WindowManager.LayoutParams.screenBrightness].
 *
 * Clear the set value and use system screen brightness.
 * @receiver the current window.
 */
fun Window.clearScreenBrightness() = updateLayoutParams { screenBrightness = -1f }
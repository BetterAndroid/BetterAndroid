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
 * This file is created by fankes on 2023/12/5.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.platform.systembar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Native system bars controller for each platform.
 */
expect class NativeSystemBarsController

/**
 * Platform system bars controller.
 *
 * Supports Android and iOS.
 *
 * This is a controller with the ability to globally manage system bars of each platform.
 * @param actual the actual controller.
 */
@Stable
expect class PlatformSystemBarsController internal constructor(actual: NativeSystemBarsController?) {

    /** The actual controller. */
    internal val actual: NativeSystemBarsController?

    /** The current system bars insets. */
    @get:Composable
    val systemBarsInsets: PlatformSystemBarsInsets

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    fun show(type: PlatformSystemBars)

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    fun hide(type: PlatformSystemBars)

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    fun isVisible(type: PlatformSystemBars): Boolean

    /**
     * Set the system bars background color.
     * @param type the system bars type.
     * @param color the color to set.
     */
    fun setColor(type: PlatformSystemBars, color: Color)

    /**
     * Get or set the dark elements tint color (light appearance) of status bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     * @return [Boolean]
     */
    var isDarkColorStatusBars: Boolean

    /**
     * Get or set the dark elements tint color (light appearance) of navigation bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     * @return [Boolean]
     */
    var isDarkColorNavigationBars: Boolean

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    fun adaptiveAppearance(color: Color)
}

/**
 * System bars type definition.
 */
@Stable
enum class PlatformSystemBars {
    /** All types. */
    All,

    /** Status bars. */
    StatusBars,

    /**
     * Navigation bars.
     *
     * In iOS, this will called home indicator.
     */
    NavigationBars
}

/**
 * Creates and remember a [PlatformSystemBarsController].
 *
 * Platform requirements:
 *
 * > Android
 *
 * You need to use **AppComponentActivity** or implement **ISystemBarsController** of your Activity,
 * and you must use an **ComponentActivity** for basic.
 *
 * Requires library: `ui-component`, visit [here](https://github.com/BetterAndroid/BetterAndroid).
 *
 * > iOS
 *
 * TODO: iOS platform system bars.
 *
 * > Others
 *
 * No effect.
 * @return [PlatformSystemBarsController]
 */
@Composable
fun rememberSystemBarsController(): PlatformSystemBarsController {
    var systemBars by remember { mutableStateOf<PlatformSystemBarsController?>(null) }
    if (systemBars == null) systemBars = resolvePlatformSystemBarsController()
    return systemBars ?: error("No PlatformSystemBarsController provided of composables.")
}

/**
 * Resolve the [PlatformSystemBarsController].
 * @return [PlatformSystemBarsController]
 */
@Composable
@ReadOnlyComposable
internal expect fun resolvePlatformSystemBarsController(): PlatformSystemBarsController

/** Default platform system bars controller. */
internal val DefaultPlatformSystemBarsController = PlatformSystemBarsController(actual = null)
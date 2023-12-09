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
 * @param actual the native controller.
 */
@Stable
expect class PlatformSystemBarsController internal constructor(actual: NativeSystemBarsController?) {

    /** The native controller. */
    internal val actual: NativeSystemBarsController?

    /** The current system bars insets. */
    @get:Composable
    val systemBarsInsets: PlatformSystemBarsInsets

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [PlatformSystemBarsBehavior.Immersive].
     * @return [PlatformSystemBarsBehavior]
     */
    var behavior: PlatformSystemBarsBehavior

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
     * Get or set the dark content (light appearance) of status bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     * @return [Boolean]
     */
    var isDarkContentStatusBars: Boolean

    /**
     * Get or set the dark content (light appearance) of navigation bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     *
     * - Note: This will no-op for iOS.
     * @return [Boolean]
     */
    var isDarkContentNavigationBars: Boolean

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    fun adaptiveAppearance(color: Color)
}

/**
 * Platform system bars type definition.
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
 * Platform system bars behavior type definition.
 */
@Stable
enum class PlatformSystemBarsBehavior {
    /** The default mode selected by the system. */
    Default,

    /**
     * - Android:
     *
     * Appears as a semi-transparent system bars that slides out of the full screen
     * and continues to hide after a period of time.
     *
     * - iOS:
     *
     * The system bars can be revealed temporarily with system gestures
     * when the status bars hide, but disappears after a period of time.
     */
    Immersive
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
 * You need to use **AppComponentUIViewController**, see the **AppComponentUIViewController**
 * function for more help.
 *
 * > Others
 *
 * No-op.
 * @return [PlatformSystemBarsController]
 */
@Composable
fun rememberSystemBarsController(): PlatformSystemBarsController {
    var systemBars by remember { mutableStateOf<PlatformSystemBarsController?>(null) }
    if (systemBars == null) systemBars = resolvePlatformSystemBarsController()
    return systemBars ?: error("No PlatformSystemBarsController provided of composables.")
}

/**
 * Resolve the [NativeSystemBarsController].
 *
 * If the [NativeSystemBarsController] is destroyed or null, this will return null.
 * @receiver [PlatformSystemBarsController]
 * @return [NativeSystemBarsController] or null.
 */
expect val PlatformSystemBarsController.nativeController: NativeSystemBarsController?

/**
 * Resolve the [PlatformSystemBarsController].
 * @return [PlatformSystemBarsController]
 */
@Composable
@ReadOnlyComposable
internal expect fun resolvePlatformSystemBarsController(): PlatformSystemBarsController

/** Default platform system bars controller. */
internal val DefaultPlatformSystemBarsController = PlatformSystemBarsController(actual = null)

/** Default platform system bars behavior. */
internal val DefaultPlatformSystemBarsBehavior = PlatformSystemBarsBehavior.Immersive
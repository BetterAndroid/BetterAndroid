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
import androidx.compose.ui.graphics.Color

/**
 * Native system bars controller for each platform.
 */
actual typealias NativeSystemBarsController = Any // TODO: iOS platform system bars.

/**
 * Platform system bars controller.
 *
 * Supports Android and iOS.
 *
 * This is a controller with the ability to globally manage system bars of each platform.
 * @param actual the actual controller.
 */
@Stable
actual class PlatformSystemBarsController internal actual constructor(internal actual val actual: NativeSystemBarsController?) {

    /** The current system bars insets. */
    actual val systemInsets: PlatformSystemInsets
        @Composable
        get() {
            // TODO: iOS platform system bars.
            return PlatformSystemInsets.Default
        }

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    actual fun show(type: PlatformSystemBars) {
        // TODO: iOS platform system bars.
    }

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    actual fun hide(type: PlatformSystemBars) {
        // TODO: iOS platform system bars.
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    actual fun isVisible(type: PlatformSystemBars): Boolean {
        // TODO: iOS platform system bars.
        return false
    }

    /**
     * Set the system bars background color.
     * @param type the system bars type.
     * @param color the color to set.
     */
    actual fun setColor(type: PlatformSystemBars, color: Color) {
        // TODO: iOS platform system bars.
    }

    /**
     * Get or set the dark elements tint color (light appearance) of status bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     * @return [Boolean]
     */
    actual var isDarkColorStatusBars: Boolean
        get() = false
        set(value) {
            // TODO: iOS platform system bars.
        }

    /**
     * Get or set the dark elements tint color (light appearance) of navigation bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     * @return [Boolean]
     */
    actual var isDarkColorNavigationBars: Boolean
        get() = false
        set(value) {
            // TODO: iOS platform system bars.
        }

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    actual fun adaptiveAppearance(color: Color) {
        // TODO: iOS platform system bars.
    }
}

/**
 * Resolve the [PlatformSystemBarsController].
 * @return [PlatformSystemBarsController]
 */
@Composable
@ReadOnlyComposable
internal actual fun resolvePlatformSystemBarsController(): PlatformSystemBarsController {
    // TODO: iOS platform system bars.
    return com.highcapable.betterandroid.compose.extension.platform.systembar.PlatformSystemBarsController(Any())
}
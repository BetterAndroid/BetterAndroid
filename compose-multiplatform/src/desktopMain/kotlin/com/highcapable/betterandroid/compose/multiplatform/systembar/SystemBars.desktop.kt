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
 * This file is created by fankes on 2023/12/5.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.multiplatform.systembar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Native system bars controller for each platform.
 */
actual class NativeSystemBarsController

/**
 * Platform system bars controller.
 *
 * Supports Android and iOS.
 *
 * This is a controller with the ability to globally manage system bars of each platform.
 * @param actual the native controller.
 */
@Stable
actual class PlatformSystemBarsController internal actual constructor(internal actual val actual: NativeSystemBarsController?) {

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [PlatformSystemBarBehavior.Immersive].
     * @return [PlatformSystemBarBehavior]
     */
    @Suppress("UNUSED_PARAMETER")
    actual var behavior: PlatformSystemBarBehavior
        get() = DefaultPlatformSystemBarBehavior
        set(value) {
            // Platform desktop: No-op.
        }

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    actual fun show(type: PlatformSystemBars) {
        // Platform desktop: No-op.
    }

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    actual fun hide(type: PlatformSystemBars) {
        // Platform desktop: No-op.
    }

    /**
     * Get or set the style of status bars.
     * @see PlatformSystemBarStyle
     * @see setStyle
     * @return [PlatformSystemBarStyle]
     */
    @Suppress("UNUSED_PARAMETER")
    actual var statusBarStyle: PlatformSystemBarStyle
        get() = DefaultPlatformSystemBarStyle
        set(value) {
            // Platform desktop: No-op.
        }

    /**
     * Get or set the style of navigation bars.
     *
     * - Note: The [PlatformSystemBarStyle.darkContent] will no-op for iOS.
     * @see PlatformSystemBarStyle
     * @see setStyle
     * @return [PlatformSystemBarStyle]
     */
    @Suppress("UNUSED_PARAMETER")
    actual var navigationBarStyle: PlatformSystemBarStyle
        get() = DefaultPlatformSystemBarStyle
        set(value) {
            // Platform desktop: No-op.
        }
}

/**
 * Creates and remember a [PlatformSystemBarsController].
 *
 * Please visit the [compose-multiplatform](https://betterandroid.github.io/BetterAndroid/en/library/compose-multiplatform#system-bars-status-bars-navigation-bars-home-indicator-etc)
 * documentation for usage.
 * @return [PlatformSystemBarsController]
 */
@Composable
actual fun rememberSystemBarsController(): PlatformSystemBarsController {
    // Platform desktop: No-op.
    val systemBars by remember { mutableStateOf(DefaultPlatformSystemBarsController) }

    return systemBars
}

/**
 * Resolve the [NativeSystemBarsController].
 *
 * If the [NativeSystemBarsController] is destroyed or null, this will return null.
 * @receiver [PlatformSystemBarsController]
 * @return [NativeSystemBarsController] or null.
 */
actual val PlatformSystemBarsController.nativeController get(): NativeSystemBarsController? = null
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

    /** The current system bars insets. */
    actual val systemBarsInsets: PlatformSystemBarsInsets
        @Composable
        get() {
            // Platform desktop: No-op.
            return PlatformSystemBarsInsets.Default
        }

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [PlatformSystemBarsBehavior.Immersive].
     * @return [PlatformSystemBarsBehavior]
     */
    @Suppress("UNUSED_PARAMETER")
    actual var behavior: PlatformSystemBarsBehavior
        get() = DefaultPlatformSystemBarsBehavior
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
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    actual fun isVisible(type: PlatformSystemBars): Boolean {
        // Platform desktop: No-op.
        return false
    }

    /**
     * Set the system bars background color.
     * @param type the system bars type.
     * @param color the color to set.
     */
    actual fun setColor(type: PlatformSystemBars, color: Color) {
        // Platform desktop: No-op.
    }

    /**
     * Get or set the dark content (light appearance) of status bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     * @return [Boolean]
     */
    @Suppress("UNUSED_PARAMETER")
    actual var isDarkContentStatusBars: Boolean
        get() = false
        set(value) {
            // Platform desktop: No-op.
        }

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
    @Suppress("UNUSED_PARAMETER")
    actual var isDarkContentNavigationBars: Boolean
        get() = false
        set(value) {
            // Platform desktop: No-op.
        }

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    actual fun adaptiveAppearance(color: Color) {
        // Platform desktop: No-op.
    }
}

/**
 * Resolve the [NativeSystemBarsController].
 *
 * If the [NativeSystemBarsController] is destroyed or null, this will return null.
 * @receiver [PlatformSystemBarsController]
 * @return [NativeSystemBarsController] or null.
 */
actual val PlatformSystemBarsController.nativeController get(): NativeSystemBarsController? = null

/**
 * Resolve the [PlatformSystemBarsController].
 * @return [PlatformSystemBarsController]
 */
@Composable
@ReadOnlyComposable
internal actual fun resolvePlatformSystemBarsController(): PlatformSystemBarsController {
    // Platform desktop: No-op.
    return DefaultPlatformSystemBarsController
}
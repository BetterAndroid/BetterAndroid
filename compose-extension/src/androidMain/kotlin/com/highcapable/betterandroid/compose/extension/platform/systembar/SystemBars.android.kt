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

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.highcapable.betterandroid.ui.component.proxy.ISystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars

/**
 * Native system bars controller for each platform.
 */
actual typealias NativeSystemBarsController = SystemBarsController

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
        get() = actual?.resolvePlatformSystemInsets() ?: PlatformSystemInsets.Default

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    actual fun show(type: PlatformSystemBars) {
        actual?.show(type.toPlatformActual())
    }

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    actual fun hide(type: PlatformSystemBars) {
        actual?.hide(type.toPlatformActual())
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    actual fun isVisible(type: PlatformSystemBars): Boolean {
        return actual?.isVisible(type.toPlatformActual()) == true
    }

    /**
     * Set the system bars background color.
     * @param type the system bars type.
     * @param color the color to set.
     */
    actual fun setColor(type: PlatformSystemBars, color: Color) {
        actual?.setColor(type.toPlatformActual(), color.toArgb())
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
        get() = actual?.isDarkColorStatusBars == true
        set(value) {
            actual?.isDarkColorStatusBars = value
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
        get() = actual?.isDarkColorNavigationBars == true
        set(value) {
            actual?.isDarkColorNavigationBars = value
        }

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    actual fun adaptiveAppearance(color: Color) {
        actual?.adaptiveAppearance(color.toArgb())
    }
}

/**
 * Resolve the [PlatformSystemBarsController].
 * @return [PlatformSystemBarsController]
 */
@Composable
@ReadOnlyComposable
internal actual fun resolvePlatformSystemBarsController(): PlatformSystemBarsController {
    val activity = LocalContext.current as? Activity? ?: error("No Activity provided of composables.")
    return PlatformSystemBarsController(activity.resolveSystemBarsController())
}

/**
 * Resolve the [SystemBarsController] from [Activity].
 * @receiver the current activity.
 * @return [PlatformSystemBarsController] or null.
 */
private fun Activity.resolveSystemBarsController() = (this as? ISystemBarsController?)?.systemBars ?: run {
    Log.w(
        "BetterAndroid",
        "You need to use an AppComponentActivity or implement ISystemBarsController of your Activity " +
            "to use the system bars related functions of composables."
    ); null
}

/**
 * Convert [PlatformSystemBars] to [SystemBars].
 * @return [SystemBars]
 */
private fun PlatformSystemBars.toPlatformActual() = when (this) {
    PlatformSystemBars.All -> SystemBars.ALL
    PlatformSystemBars.StatusBars -> SystemBars.STATUS_BARS
    PlatformSystemBars.NavigationBars -> SystemBars.NAVIGATION_BARS
}
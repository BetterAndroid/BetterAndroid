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

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.highcapable.betterandroid.ui.component.proxy.ISystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBarsBehavior

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
    actual val systemBarsInsets: PlatformSystemBarsInsets
        @Composable
        get() = currentActual?.resolvePlatformSystemBarsInsets() ?: PlatformSystemBarsInsets.Default

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [PlatformSystemBarsBehavior.Immersive].
     * @return [PlatformSystemBarsBehavior]
     */
    actual var behavior: PlatformSystemBarsBehavior
        get() = currentActual?.behavior?.toPlatformExpect() ?: DefaultPlatformSystemBarsBehavior
        set(value) {
            currentActual?.behavior = value.toPlatformActual()
        }

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    actual fun show(type: PlatformSystemBars) {
        currentActual?.show(type.toPlatformActual())
    }

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    actual fun hide(type: PlatformSystemBars) {
        currentActual?.hide(type.toPlatformActual())
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    actual fun isVisible(type: PlatformSystemBars): Boolean {
        return currentActual?.isVisible(type.toPlatformActual()) == true
    }

    /**
     * Set the system bars background color.
     * @param type the system bars type.
     * @param color the color to set.
     */
    actual fun setColor(type: PlatformSystemBars, color: Color) {
        currentActual?.setColor(type.toPlatformActual(), color.toArgb())
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
    actual var isDarkContentStatusBars: Boolean
        get() = currentActual?.isDarkContentStatusBars == true
        set(value) {
            currentActual?.isDarkContentStatusBars = value
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
    actual var isDarkContentNavigationBars: Boolean
        get() = currentActual?.isDarkContentNavigationBars == true
        set(value) {
            currentActual?.isDarkContentNavigationBars = value
        }

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    actual fun adaptiveAppearance(color: Color) {
        currentActual?.adaptiveAppearance(color.toArgb())
    }
}

/**
 * Resolve the [SystemBarsController]'s undestroyed instance.
 * @receiver [PlatformSystemBarsController]
 * @return [SystemBarsController] or null.
 */
private val PlatformSystemBarsController.currentActual get() = if (actual?.isDestroyed == false) actual else null

/**
 * Resolve the [PlatformSystemBarsController].
 * @return [PlatformSystemBarsController]
 */
@Composable
@ReadOnlyComposable
internal actual fun resolvePlatformSystemBarsController(): PlatformSystemBarsController {
    val activity = LocalContext.current as? ComponentActivity? ?: error("No ComponentActivity provided of composables.")
    return PlatformSystemBarsController(activity.resolveSystemBarsController())
}

/**
 * Resolve the [SystemBarsController] from [ComponentActivity].
 * @receiver the current activity.
 * @return [SystemBarsController] or null.
 */
private fun ComponentActivity.resolveSystemBarsController() = (this as? ISystemBarsController?)?.systemBars ?: run {
    val invalidMessage = "You need to use AppComponentActivity or implement ISystemBarsController of your Activity " +
        "to use the system bars related functions of composables.\n" +
        "Please visit https://github.com/BetterAndroid/BetterAndroid for more help."
    Log.w("BetterAndroid", invalidMessage)
    null
}

/**
 * Convert [PlatformSystemBars] to [SystemBars].
 * @receiver [PlatformSystemBars]
 * @return [SystemBars]
 */
private fun PlatformSystemBars.toPlatformActual() = when (this) {
    PlatformSystemBars.All -> SystemBars.ALL
    PlatformSystemBars.StatusBars -> SystemBars.STATUS_BARS
    PlatformSystemBars.NavigationBars -> SystemBars.NAVIGATION_BARS
}

/**
 * Convert [PlatformSystemBarsBehavior] to [SystemBarsBehavior].
 * @receiver [PlatformSystemBarsBehavior]
 * @return [SystemBarsBehavior]
 */
private fun PlatformSystemBarsBehavior.toPlatformActual() = when (this) {
    PlatformSystemBarsBehavior.Default -> SystemBarsBehavior.DEFAULT
    PlatformSystemBarsBehavior.Immersive -> SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
}

/**
 * Convert [SystemBarsBehavior] to [PlatformSystemBarsBehavior].
 * @receiver [SystemBarsBehavior]
 * @return [PlatformSystemBarsBehavior]
 */
private fun SystemBarsBehavior.toPlatformExpect() = when (this) {
    SystemBarsBehavior.DEFAULT -> PlatformSystemBarsBehavior.Default
    SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE -> PlatformSystemBarsBehavior.Immersive
}
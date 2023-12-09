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
import androidx.compose.ui.interop.LocalUIViewController
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.SystemBarsController
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.type.SystemBars
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.type.SystemBarsBehavior
import com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller.AppComponentUIViewController
import com.highcapable.betterandroid.compose.extension.ui.toPlatformColor
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.childViewControllers

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
        currentActual?.setColor(type.toPlatformActual(), color.toPlatformColor())
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
        get() = currentActual?.statusBarStyle == UIStatusBarStyleDarkContent
        set(value) {
            currentActual?.statusBarStyle = if (value) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        }

    /**
     * Get or set the dark elements tint color (light appearance) of navigation bars.
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
    actual var isDarkColorNavigationBars: Boolean
        get() = false
        set(value) {
            // Platform iOS: No-op.
        }

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    actual fun adaptiveAppearance(color: Color) {
        currentActual?.adaptiveAppearance(color.toPlatformColor())
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
    val controller = LocalUIViewController.current
    return PlatformSystemBarsController(controller.resolveSystemBarsController())
}

/**
 * Resolve the [SystemBarsController] from [UIViewController].
 * @receiver the current UI view controller.
 * @return [SystemBarsController] or null.
 */
private fun UIViewController.resolveSystemBarsController() = resolveAppComponentUIViewController()?.systemBars ?: run {
    val invalidMessage = "You need to use AppComponentUIViewController to use the system bars related functions of composables.\n" +
        "Please visit https://github.com/BetterAndroid/BetterAndroid for more help."
    println("BetterAndroid: $invalidMessage")
    null
}

/**
 * Resolve the [AppComponentUIViewController].
 * @receiver the current UI view controller.
 * @return [AppComponentUIViewController] or null.
 */
private fun UIViewController.resolveAppComponentUIViewController() =
    this as? AppComponentUIViewController?
        ?: this.parentViewController as? AppComponentUIViewController?
        ?: this.childViewControllers.firstOrNull() as? AppComponentUIViewController?

/**
 * Convert [PlatformSystemBars] to [SystemBars].
 * @receiver [PlatformSystemBars]
 * @return [SystemBars]
 */
private fun PlatformSystemBars.toPlatformActual() = when (this) {
    PlatformSystemBars.All -> SystemBars.ALL
    PlatformSystemBars.StatusBars -> SystemBars.STATUS_BARS
    PlatformSystemBars.NavigationBars -> SystemBars.HOME_INDICATOR
}

/**
 * Convert [PlatformSystemBars] to [SystemBarsBehavior].
 * @receiver [PlatformSystemBarsBehavior]
 * @return [SystemBarsBehavior]
 */
private fun PlatformSystemBarsBehavior.toPlatformActual() = when (this) {
    PlatformSystemBarsBehavior.Default -> SystemBarsBehavior.DEFAULT
    PlatformSystemBarsBehavior.Immersive -> SystemBarsBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES
}

/**
 * Convert [SystemBarsBehavior] to [PlatformSystemBarsBehavior].
 * @receiver [SystemBarsBehavior]
 * @return [PlatformSystemBarsBehavior]
 */
private fun SystemBarsBehavior.toPlatformExpect() = when (this) {
    SystemBarsBehavior.DEFAULT -> PlatformSystemBarsBehavior.Default
    SystemBarsBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES -> PlatformSystemBarsBehavior.Immersive
}
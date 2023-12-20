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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.LocalUIViewController
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.SystemBarsController
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.style.SystemBarStyle
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.type.SystemBars
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.type.SystemBarsBehavior
import com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller.AppComponentUIViewController
import com.highcapable.betterandroid.compose.extension.ui.toComposeColor
import com.highcapable.betterandroid.compose.extension.ui.toPlatformColor
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
 * @param actual the native controller.
 */
@Stable
actual class PlatformSystemBarsController internal actual constructor(internal actual val actual: NativeSystemBarsController?) {

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [PlatformSystemBarsBehavior.Immersive].
     * @return [PlatformSystemBarsBehavior]
     */
    actual var behavior: PlatformSystemBarsBehavior
        get() = nativeController?.behavior?.toPlatformExpect() ?: DefaultPlatformSystemBarsBehavior
        set(value) {
            nativeController?.behavior = value.toPlatformActual()
        }

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    actual fun show(type: PlatformSystemBars) {
        nativeController?.show(type.toPlatformActual())
    }

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    actual fun hide(type: PlatformSystemBars) {
        nativeController?.hide(type.toPlatformActual())
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    actual fun isVisible(type: PlatformSystemBars): Boolean {
        return nativeController?.isVisible(type.toPlatformActual()) == true
    }

    /**
     * Get or set the style of status bars.
     * @see PlatformSystemBarStyle
     * @see setStyle
     * @return [PlatformSystemBarStyle]
     */
    actual var statusBarStyle: PlatformSystemBarStyle
        get() = nativeController?.statusBarStyle?.toPlatformExpect() ?: DefaultPlatformSystemBarStyle
        set(value) {
            nativeController?.statusBarStyle = value.toPlatformActual()
        }

    /**
     * Get or set the style of navigation bars.
     *
     * - Note: The [PlatformSystemBarStyle.darkContent] will no-op for iOS.
     * @see PlatformSystemBarStyle
     * @see setStyle
     * @return [PlatformSystemBarStyle]
     */
    actual var navigationBarStyle: PlatformSystemBarStyle
        get() = nativeController?.homeIndicatorStyle?.toPlatformExpect() ?: DefaultPlatformSystemBarStyle
        set(value) {
            nativeController?.homeIndicatorStyle = value.toPlatformActual()
        }
}

/**
 * Creates and remember a [PlatformSystemBarsController].
 *
 * Platform requirements:
 *
 * > Android
 *
 * You can use **AppComponentActivity** or implement **ISystemBarsController** of your Activity for better,
 * but you must use an **ComponentActivity** for basic.
 *
 * Requires library: `ui-component`, visit [here](https://github.com/BetterAndroid/BetterAndroid).
 *
 * > iOS
 *
 * You need to use [AppComponentUIViewController], see the [AppComponentUIViewController]
 * function for more help.
 *
 * > Others
 *
 * No-op.
 * @return [PlatformSystemBarsController]
 */
@Composable
actual fun rememberSystemBarsController(): PlatformSystemBarsController {
    val controller = LocalUIViewController.current
    var systemBars by remember { mutableStateOf(DefaultPlatformSystemBarsController) }
    if (systemBars.actual == null) {
        val nativeSystemBars = controller.resolveSystemBarsController()
        SideEffect {
            // If the controller is not initialized, initialize it.
            if (nativeSystemBars?.isDestroyed == true) nativeSystemBars.init()
        }
        systemBars = PlatformSystemBarsController(nativeSystemBars)
    }; return systemBars
}

/**
 * Resolve the [NativeSystemBarsController].
 *
 * If the [NativeSystemBarsController] is destroyed or null, this will return null.
 * @receiver [PlatformSystemBarsController]
 * @return [NativeSystemBarsController] or null.
 */
actual val PlatformSystemBarsController.nativeController get() = if (actual?.isDestroyed == false) actual else null

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

/**
 * Convert [PlatformSystemBarStyle] to [SystemBarStyle].
 * @receiver [PlatformSystemBarStyle]
 * @return [SystemBarStyle]
 */
private fun PlatformSystemBarStyle.toPlatformActual() = SystemBarStyle(color.toPlatformColor(), darkContent)

/**
 * Convert [SystemBarStyle] to [PlatformSystemBarStyle].
 * @receiver [SystemBarStyle]
 * @return [PlatformSystemBarStyle]
 */
private fun SystemBarStyle.toPlatformExpect() = PlatformSystemBarStyle(color?.toComposeColor() ?: Color.Unspecified, darkContent)
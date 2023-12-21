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

import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import com.highcapable.betterandroid.ui.component.activity.AppComponentActivity
import com.highcapable.betterandroid.ui.component.proxy.ISystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.style.SystemBarStyle
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBarsBehavior
import android.R as Android_R

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
        get() = nativeController?.navigationBarStyle?.toPlatformExpect() ?: DefaultPlatformSystemBarStyle
        set(value) {
            nativeController?.navigationBarStyle = value.toPlatformActual()
        }
}

/**
 * Creates and remember a [PlatformSystemBarsController].
 *
 * Platform requirements:
 *
 * > Android
 *
 * You can use [AppComponentActivity] or implement [ISystemBarsController] of your Activity for better,
 * but you must use an [ComponentActivity] for basic.
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
actual fun rememberSystemBarsController(): PlatformSystemBarsController {
    val activity = LocalContext.current as? ComponentActivity? ?: error("No ComponentActivity provided of composables.")
    var systemBars by remember { mutableStateOf(DefaultPlatformSystemBarsController) }
    if (systemBars.actual == null) {
        val nativeSystemBars = activity.resolveSystemBarsController()
        SideEffect {
            // Find the current [ComposeView].
            val existingComposeView = activity.findViewById<ViewGroup>(Android_R.id.content)?.getChildAt(0) as? ComposeView?
            // If the controller is not initialized, initialize it with [existingComposeView].
            if (nativeSystemBars.isDestroyed) nativeSystemBars.init(existingComposeView, handleWindowInsets = null)
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
 * Resolve the [SystemBarsController] from [ComponentActivity].
 * @receiver the current activity.
 * @return [SystemBarsController]
 */
private fun ComponentActivity.resolveSystemBarsController() =
    (this as? ISystemBarsController?)?.systemBars ?: SystemBarsController.from(activity = this)

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

/**
 * Convert [PlatformSystemBarStyle] to [SystemBarStyle].
 * @receiver [PlatformSystemBarStyle]
 * @return [SystemBarStyle]
 */
private fun PlatformSystemBarStyle.toPlatformActual() = SystemBarStyle(color.toArgb(), darkContent)

/**
 * Convert [SystemBarStyle] to [PlatformSystemBarStyle].
 * @receiver [SystemBarStyle]
 * @return [PlatformSystemBarStyle]
 */
private fun SystemBarStyle.toPlatformExpect() = PlatformSystemBarStyle(color?.let { Color(it) } ?: Color.Unspecified, darkContent)
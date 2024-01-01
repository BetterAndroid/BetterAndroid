/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2024 HighCapable
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
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.compose.multiplatform.systembar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.highcapable.betterandroid.compose.extension.ui.isBrightColor

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

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [PlatformSystemBarBehavior.Immersive].
     * @return [PlatformSystemBarBehavior]
     */
    var behavior: PlatformSystemBarBehavior

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
     * Get or set the style of status bars.
     * @see PlatformSystemBarStyle
     * @see setStyle
     * @return [PlatformSystemBarStyle]
     */
    var statusBarStyle: PlatformSystemBarStyle

    /**
     * Get or set the style of navigation bars.
     *
     * - Note: The [PlatformSystemBarStyle.darkContent] will no-op for iOS.
     * @see PlatformSystemBarStyle
     * @see setStyle
     * @return [PlatformSystemBarStyle]
     */
    var navigationBarStyle: PlatformSystemBarStyle
}

/**
 * Set the style of system bars.
 *
 * You can also use the [PlatformSystemBarsController.statusBarStyle]
 * and [PlatformSystemBarsController.navigationBarStyle].
 *
 * - Note: The [PlatformSystemBarStyle.darkContent] of
 *   [PlatformSystemBarsController.navigationBarStyle] will no-op for iOS.
 * @see PlatformSystemBarStyle
 * @see PlatformSystemBarsController.statusBarStyle
 * @see PlatformSystemBarsController.navigationBarStyle
 * @param style the system bars style.
 */
@Stable
fun PlatformSystemBarsController.setStyle(style: PlatformSystemBarStyle) = setStyle(style, style)

/**
 * Set the style of system bars.
 *
 * You can also use the [PlatformSystemBarsController.statusBarStyle]
 * and [PlatformSystemBarsController.navigationBarStyle].
 *
 * - Note: The [PlatformSystemBarStyle.darkContent] of [navigationBar] will no-op for iOS.
 * @see PlatformSystemBarStyle
 * @see PlatformSystemBarsController.statusBarStyle
 * @see PlatformSystemBarsController.navigationBarStyle
 * @param statusBar the status bars style.
 * @param navigationBar the navigation bars style.
 */
@Stable
fun PlatformSystemBarsController.setStyle(
    statusBar: PlatformSystemBarStyle = statusBarStyle,
    navigationBar: PlatformSystemBarStyle = navigationBarStyle
) {
    statusBarStyle = statusBar
    navigationBarStyle = navigationBar
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
 * Platform system bar behavior type definition.
 */
@Stable
enum class PlatformSystemBarBehavior {
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
 * Defines the style of the platfom system bars.
 * @param color the background color.
 * @param darkContent whether the content color is dark.
 */
@Immutable
data class PlatformSystemBarStyle(val color: Color = Color.Unspecified, val darkContent: Boolean? = null) {

    companion object {

        /**
         * An auto system bar style.
         *
         * Follow the dark mode of the system,
         * the light mode uses a white background & dark content color,
         * and the dark mode uses a black background & light content color.
         */
        @Stable
        val Auto = PlatformSystemBarStyle()

        /**
         * An auto transparent system bar style.
         *
         * Follow the dark mode of the system,
         * the light mode uses a dark content color, and the dark mode uses
         * a light content color, the both mode uses a transparent background.
         */
        @Stable
        val AutoTransparent = PlatformSystemBarStyle(color = Color.Transparent)

        /**
         * A light system bar style.
         *
         * Uses a white background & dark content color.
         */
        @Stable
        val Light = PlatformSystemBarStyle(color = Color.White, darkContent = true)

        /**
         * A light scrim system bar style.
         *
         * Uses a translucent white mask background & dark content color.
         */
        @Stable
        val LightScrim = PlatformSystemBarStyle(color = Color(0x7FFFFFFF), darkContent = true)

        /**
         * A light transparent system bar style.
         *
         * Uses a dark content color & transparent background.
         */
        @Stable
        val LightTransparent = PlatformSystemBarStyle(color = Color.Transparent, darkContent = true)

        /**
         * A dark system bar style.
         *
         * Uses a black background & light content color.
         */
        @Stable
        val Dark = PlatformSystemBarStyle(color = Color.Black, darkContent = false)

        /**
         * A dark scrim system bar style.
         *
         * Uses a translucent black mask background & light content color.
         */
        @Stable
        val DarkScrim = PlatformSystemBarStyle(color = Color(0x7F000000), darkContent = false)

        /**
         * A dark transparent system bar style.
         *
         * Uses a light content color & transparent background.
         */
        @Stable
        val DarkTransparent = PlatformSystemBarStyle(color = Color.Transparent, darkContent = false)

        /**
         * Gets the lightness and darkness from the given [detectContent] to detect
         * the content color, using [color] as the background color.
         *
         * If the [color] is unspecified, the background color will be like the [Auto] style.
         * @param detectContent the tint color.
         * @param color the background color, default is [Color.Unspecified].
         * @return [PlatformSystemBarStyle]
         */
        @Stable
        fun auto(detectContent: Color, color: Color = Color.Unspecified) = PlatformSystemBarStyle(color, detectContent.isBrightColor)
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
 * You need to use **AppComponentUIViewController**, see the **AppComponentUIViewController**
 * function for more help.
 *
 * > Others
 *
 * No-op.
 * @return [PlatformSystemBarsController]
 */
@Composable
expect fun rememberSystemBarsController(): PlatformSystemBarsController

/**
 * Resolve the [NativeSystemBarsController].
 *
 * If the [NativeSystemBarsController] is destroyed or null, this will return null.
 * @receiver [PlatformSystemBarsController]
 * @return [NativeSystemBarsController] or null.
 */
expect val PlatformSystemBarsController.nativeController: NativeSystemBarsController?

/** Default platform system bars controller. */
internal val DefaultPlatformSystemBarsController = PlatformSystemBarsController(actual = null)

/** Default platform system bars behavior. */
internal val DefaultPlatformSystemBarBehavior = PlatformSystemBarBehavior.Immersive

/** Default platform system bars style. */
internal val DefaultPlatformSystemBarStyle = PlatformSystemBarStyle.AutoTransparent
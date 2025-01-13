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
 * This file is created by fankes on 2023/12/20.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.compose.multiplatform.platform.systembar.style

import com.highcapable.betterandroid.compose.extension.ui.isBrightColor
import platform.UIKit.UIColor

/**
 * Defines the style of the system bars.
 * @param color the background color.
 * @param darkContent whether the content color is dark.
 */
data class SystemBarStyle(val color: UIColor? = null, val darkContent: Boolean? = null) {

    companion object {

        /**
         * An auto system bar style.
         *
         * Follow the dark mode of the system,
         * the light mode uses a white background & dark content color,
         * and the dark mode uses a black background & light content color.
         */
        val Auto = SystemBarStyle()

        /**
         * An auto transparent system bar style.
         *
         * Follow the dark mode of the system,
         * the light mode uses a dark content color, and the dark mode uses
         * a light content color, the both mode uses a transparent background.
         */
        val AutoTransparent = SystemBarStyle(color = UIColor.clearColor)

        /**
         * A light system bar style.
         *
         * Uses a white background & dark content color.
         */
        val Light = SystemBarStyle(color = UIColor.whiteColor, darkContent = true)

        /**
         * A light scrim system bar style.
         *
         * Uses a translucent white mask background & dark content color.
         */
        val LightScrim = SystemBarStyle(color = UIColor.colorWithWhite(white = 1.0, alpha = 0.5), darkContent = true)

        /**
         * A light transparent system bar style.
         *
         * Uses a dark content color & transparent background.
         */
        val LightTransparent = SystemBarStyle(color = UIColor.clearColor, darkContent = true)

        /**
         * A dark system bar style.
         *
         * Uses a black background & light content color.
         */
        val Dark = SystemBarStyle(color = UIColor.blackColor, darkContent = false)

        /**
         * A dark scrim system bar style.
         *
         * Uses a translucent black mask background & light content color.
         */
        val DarkScrim = SystemBarStyle(color = UIColor.colorWithWhite(white = 0.0, alpha = 0.5), darkContent = false)

        /**
         * A dark transparent system bar style.
         *
         * Uses a light content color & transparent background.
         */
        val DarkTransparent = SystemBarStyle(color = UIColor.clearColor, darkContent = false)

        /**
         * Gets the lightness and darkness from the given [detectContent] to detect
         * the content color, using [color] as the background color.
         *
         * If the [color] is unspecified, the background color will be like the [Auto] style.
         * @param detectContent the tint color.
         * @param color the background color, default is unspecified.
         * @return [SystemBarStyle]
         */
        fun auto(detectContent: UIColor, color: UIColor? = null) = SystemBarStyle(color, detectContent.isBrightColor)
    }
}
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
 * This file is created by fankes on 2022/10/28.
 */
@file:Suppress("unused", "DeprecatedCallableAddReplaceWith")

package com.highcapable.betterandroid.ui.component.systembar.wrapper

import android.content.Context
import androidx.annotation.Px
import androidx.core.graphics.Insets
import androidx.core.view.DisplayCutoutCompat
import com.highcapable.betterandroid.ui.component.systembar.compat.SystemBarsCompat

/**
 * This is a wrapper of [DisplayCutoutCompat] instance.
 *
 * - This class is deprecated and will be removed in the future.
 */
@Deprecated(message = "No effect and will be removed in the future.")
class DisplayCutoutCompatWrapper internal constructor(
    private val context: Context,
    private val compat: SystemBarsCompat,
    private val insets: Insets? = null,
    val wrapper: DisplayCutoutCompat? = null
) {

    /**
     * Get the notch safe inset top (px).
     *
     * - This class is deprecated and will be removed in the future.
     * @return [Int]
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    val safeInsetTop @Px get() = 0

    /**
     * Get the notch safe inset bottom (px).
     *
     * - This class is deprecated and will be removed in the future.
     * @return [Int]
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    val safeInsetBottom @Px get() = 0

    /**
     * Get the notch safe inset left (px).
     *
     * - This class is deprecated and will be removed in the future.
     * @return [Int]
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    val safeInsetLeft @Px get() = 0

    /**
     * Get the notch safe inset right (px).
     *
     * - This class is deprecated and will be removed in the future.
     * @return [Int]
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    val safeInsetRight @Px get() = 0
}
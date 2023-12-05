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
 * This file is created by fankes on 2022/10/28.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.systembar.wrapper

import android.content.Context
import androidx.annotation.Px
import androidx.core.graphics.Insets
import androidx.core.view.DisplayCutoutCompat
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.systembar.compat.SystemBarsCompat
import com.highcapable.betterandroid.ui.extension.component.base.isSpecialWindowingMode

/**
 * This is a wrapper of [DisplayCutoutCompat] instance.
 * @param context the current context.
 * @param compat the [SystemBarsCompat] instance.
 * @param insets the current [Insets].
 * @param wrapper the original instance.
 */
class DisplayCutoutCompatWrapper internal constructor(
    private val context: Context,
    private val compat: SystemBarsCompat,
    private val insets: Insets? = null,
    val wrapper: DisplayCutoutCompat? = null
) {

    /**
     * Get the notch safe inset top (px).
     * @return [Int]
     */
    val safeInsetTop
        @Px get() = withCondition { SystemVersion.requireOrNull(SystemVersion.P, insets?.top) { wrapper?.safeInsetTop } ?: 0 }

    /**
     * Get the notch safe inset bottom (px).
     * @return [Int]
     */
    val safeInsetBottom
        @Px get() = withCondition { SystemVersion.requireOrNull(SystemVersion.P, insets?.bottom) { wrapper?.safeInsetBottom } ?: 0 }

    /**
     * Get the notch safe inset left (px).
     * @return [Int]
     */
    val safeInsetLeft
        @Px get() = withCondition { SystemVersion.requireOrNull(SystemVersion.P, insets?.left) { wrapper?.safeInsetLeft } ?: 0 }

    /**
     * Get the notch safe inset right (px).
     * @return [Int]
     */
    val safeInsetRight
        @Px get() = withCondition { SystemVersion.requireOrNull(SystemVersion.P, insets?.right) { wrapper?.safeInsetRight } ?: 0 }

    override fun toString() = "DisplayCutoutCompatWrapper(" +
        "safeInsetTop=$safeInsetTop, " +
        "safeInsetBottom=$safeInsetBottom, " +
        "safeInsetLeft=$safeInsetLeft, " +
        "safeInsetRight=$safeInsetRight" +
        ")"

    /**
     * Call based on specified conditions.
     * @param result callback the result.
     * @return [Int]
     */
    private inline fun withCondition(result: () -> Int) = if (context.resources.configuration.isSpecialWindowingMode) 0 else result()
}
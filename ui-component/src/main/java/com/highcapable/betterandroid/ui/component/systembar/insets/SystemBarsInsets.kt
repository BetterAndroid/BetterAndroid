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

package com.highcapable.betterandroid.ui.component.systembar.insets

import androidx.annotation.Px
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import com.highcapable.betterandroid.ui.component.systembar.type.InsetsType

/**
 * Window insets of system bars.
 * @param stable the stable insets, usually the listener result for [WindowInsetsCompat].
 * @param cutout the notch size (cutout size).
 */
data class SystemBarsInsets(val stable: Insets, val cutout: Insets) {

    /**
     * Convert to padding of [Insets].
     * @param type the insets type, default is [InsetsType.ADAPTIVE].
     * @return [Insets]
     */
    fun toInsetsPadding(type: InsetsType = InsetsType.ADAPTIVE): Insets {
        val paddingLeft = compatSize(stable.left, cutout.left, type)
        val paddingTop = compatSize(stable.top, cutout.top, type)
        val paddingRight = compatSize(stable.right, cutout.right, type)
        val paddingBottom = compatSize(stable.bottom, cutout.bottom, type)
        return Insets.of(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    /**
     * Compatible, use the one with the largest size first and return.
     * @param stableSize the stable size (px).
     * @param cutoutSize the notch size (cutout size) (px).
     * @param type the insets padding type.
     * @return [Int]
     */
    private fun compatSize(@Px stableSize: Int, @Px cutoutSize: Int, type: InsetsType) =
        when (type) {
            InsetsType.ADAPTIVE -> if (stableSize > cutoutSize) stableSize else cutoutSize
            InsetsType.STABLE -> stableSize
            InsetsType.CUTOUT -> cutoutSize
        }
}
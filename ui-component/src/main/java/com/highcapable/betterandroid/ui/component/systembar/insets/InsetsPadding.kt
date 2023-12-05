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
import com.highcapable.betterandroid.ui.component.systembar.type.InsetsType

/**
 * Insets padding.
 * @param left the left padding (px).
 * @param top the top padding (px).
 * @param right the right padding (px).
 * @param bottom the bottom padding (px).
 */
data class InsetsPadding internal constructor(@Px val left: Int, @Px val top: Int, @Px val right: Int, @Px val bottom: Int) {

    companion object {

        /**
         * Create a new [InsetsPadding] from [SystemInsets].
         * @param systemInsets the system insets.
         * @param type the insets type, default is [InsetsType.ADAPTIVE].
         * @return [InsetsPadding]
         */
        @JvmStatic
        @JvmOverloads
        fun from(systemInsets: SystemInsets, type: InsetsType = InsetsType.ADAPTIVE): InsetsPadding {
            val paddingLeft = compatSize(systemInsets.stable.left, systemInsets.cutout.safeInsetLeft, type)
            val paddingTop = compatSize(systemInsets.stable.top, systemInsets.cutout.safeInsetTop, type)
            val paddingRight = compatSize(systemInsets.stable.right, systemInsets.cutout.safeInsetRight, type)
            val paddingBottom = compatSize(systemInsets.stable.bottom, systemInsets.cutout.safeInsetBottom, type)
            return InsetsPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
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
}
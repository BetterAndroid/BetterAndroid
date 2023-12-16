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
 * This file is created by fankes on 2022/11/8.
 */
@file:Suppress("unused", "UNUSED_PARAMETER", "DEPRECATION")

package com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic

import android.content.Context
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView] cosmetic maker.
 *
 * - This class is deprecated and no effect and will be removed in the future.
 *
 * - Please use [RecyclerCosmetic] instead.
 * @see RecyclerCosmetic
 */
@Deprecated(message = "Use RecyclerCosmetic instead.")
class RecyclerCosmeticMaker private constructor() {

    companion object {

        /**
         * - This class is deprecated and no effect and will be removed in the future.
         *
         * - Please use [RecyclerCosmetic] instead.
         * @see RecyclerCosmetic
         */
        @Deprecated(message = "Use RecyclerCosmetic instead.")
        @JvmStatic
        fun from(
            layoutManager: RecyclerView.LayoutManager,
            itemDecoration: RecyclerView.ItemDecoration
        ) = RecyclerCosmeticMaker()

        /**
         * - This class is deprecated and no effect and will be removed in the future.
         *
         * - Please use [RecyclerCosmetic] instead.
         * @see RecyclerCosmetic
         */
        @Deprecated(message = "Use RecyclerCosmetic instead.")
        @JvmStatic
        @JvmOverloads
        fun fromGridVertical(
            context: Context,
            spanCount: Int = 3,
            @Px columnSpacing: Int = -1,
            @Px rowSpacing: Int = -1,
            isReverseLayout: Boolean = false
        ) = RecyclerCosmeticMaker()

        /**
         * - This class is deprecated and no effect and will be removed in the future.
         *
         * - Please use [RecyclerCosmetic] instead.
         * @see RecyclerCosmetic
         */
        @Deprecated(message = "Use RecyclerCosmetic instead.")
        @JvmStatic
        @JvmOverloads
        @JvmName("fromCustomSpacingGridVertical")
        fun fromGridVertical(
            context: Context, spanCount: Int = 3, @Px firstLeft: Int = 0, @Px left: Int = 0, @Px firstTop: Int = 0,
            @Px top: Int = 0, @Px lastRight: Int = 0, @Px right: Int = -1,
            @Px lastBottom: Int = 0, @Px bottom: Int = -1,
            isReverseLayout: Boolean = false
        ) = RecyclerCosmeticMaker()

        /**
         * - This class is deprecated and no effect and will be removed in the future.
         *
         * - Please use [RecyclerCosmetic] instead.
         * @see RecyclerCosmetic
         */
        @Deprecated(message = "Use RecyclerCosmetic instead.")
        @JvmStatic
        @JvmOverloads
        fun fromLinearVertical(
            context: Context,
            @Px rowSpacing: Int = -1,
            isReverseLayout: Boolean = false
        ) = RecyclerCosmeticMaker()

        /**
         * - This class is deprecated and no effect and will be removed in the future.
         *
         * - Please use [RecyclerCosmetic] instead.
         * @see RecyclerCosmetic
         */
        @Deprecated(message = "Use RecyclerCosmetic instead.")
        @JvmStatic
        @JvmOverloads
        @JvmName("fromCustomSpacingLinearVertical")
        fun fromLinearVertical(
            context: Context, @Px left: Int = 0, @Px firstTop: Int = 0, @Px top: Int = 0,
            @Px right: Int = 0, @Px lastBottom: Int = 0, @Px bottom: Int = -1,
            isReverseLayout: Boolean = false
        ) = RecyclerCosmeticMaker()

        /**
         * - This class is deprecated and no effect and will be removed in the future.
         *
         * - Please use [RecyclerCosmetic] instead.
         */
        @Deprecated(message = "Use RecyclerCosmetic instead.")
        @JvmStatic
        @JvmOverloads
        fun fromLinearHorizontal(
            context: Context,
            @Px columnSpacing: Int = -1,
            isReverseLayout: Boolean = false
        ) = RecyclerCosmeticMaker()

        /**
         * - This class is deprecated and no effect and will be removed in the future.
         *
         * - Please use [RecyclerCosmetic] instead.
         */
        @Deprecated(message = "Use RecyclerCosmetic instead.")
        @JvmStatic
        @JvmOverloads
        @JvmName("fromCustomSpacingLinearHorizontal")
        fun fromLinearHorizontal(
            context: Context, @Px firstLeft: Int = 0, @Px left: Int = 0, @Px top: Int = 0, @Px lastRight: Int = 0,
            @Px right: Int = -1, @Px bottom: Int = 0,
            isReverseLayout: Boolean = false
        ) = RecyclerCosmeticMaker()
    }

    /**
     * - This class is deprecated and no effect and will be removed in the future.
     *
     * - Please use [RecyclerCosmetic] instead.
     * @see RecyclerCosmetic
     */
    @Deprecated(message = "Use RecyclerCosmetic instead.")
    var layoutManager: RecyclerView.LayoutManager? = null

    /**
     * - This class is deprecated and no effect and will be removed in the future.
     *
     * - Please use [RecyclerCosmetic] instead.
     * @see RecyclerCosmetic
     */
    @Deprecated(message = "Use RecyclerCosmetic instead.")
    var itemDecoration: RecyclerView.ItemDecoration? = null
}
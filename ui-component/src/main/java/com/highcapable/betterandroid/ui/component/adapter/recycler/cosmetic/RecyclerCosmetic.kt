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
 * This file is created by fankes on 2022/11/8.
 * This file is modified by fankes on 2023/12/13.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic

import android.content.Context
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.recycler.decoration.GridVerticalItemDecoration
import com.highcapable.betterandroid.ui.component.adapter.recycler.decoration.LinearHorizontalItemDecoration
import com.highcapable.betterandroid.ui.component.adapter.recycler.decoration.LinearVerticalItemDecoration
import com.highcapable.betterandroid.ui.extension.component.base.toPx

/**
 * [RecyclerView] cosmetic.
 * @param layoutManager the layout manager.
 * @param itemDecoration the item decoration.
 */
class RecyclerCosmetic<RVLM : RecyclerView.LayoutManager, RVID : RecyclerView.ItemDecoration> private constructor(
    val layoutManager: RVLM,
    val itemDecoration: RVID
) {

    companion object {

        /** The default column and row spacing (dp). */
        private const val DEFAULT_SPACING_DP = 10

        /**
         * Get the [DEFAULT_SPACING_DP].
         * @receiver the current context.
         * @return [Int]
         */
        private fun Context.defaultSpacingPx() = DEFAULT_SPACING_DP.toPx(this)

        /**
         * Create a custom cosmetic.
         * @param layoutManager the layout manager.
         * @param itemDecoration the item decoration.
         * @return [RecyclerCosmetic]<[RVLM], [RVID]>
         */
        @JvmStatic
        fun <RVLM : RecyclerView.LayoutManager, RVID : RecyclerView.ItemDecoration> from(
            layoutManager: RVLM,
            itemDecoration: RVID
        ) = RecyclerCosmetic(layoutManager, itemDecoration)

        /**
         * Create a grid vertical cosmetic.
         * @param context the current context.
         * @param spanCount the column count, default is 3.
         * @param columnSpacing the column spacing (px), default is [defaultSpacingPx].
         * @param rowSpacing the row spacing (px), default is [defaultSpacingPx].
         * @param isReverseLayout reverse the layout, default false.
         * @return [RecyclerCosmetic]
         */
        @JvmStatic
        @JvmOverloads
        fun fromGridVertical(
            context: Context,
            spanCount: Int = 3,
            @Px columnSpacing: Int = context.defaultSpacingPx(),
            @Px rowSpacing: Int = context.defaultSpacingPx(),
            isReverseLayout: Boolean = false
        ) = from(
            layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, isReverseLayout),
            itemDecoration = GridVerticalItemDecoration(spanCount, columnSpacing, rowSpacing)
        )

        /**
         * Create a grid vertical cosmetic.
         * @param context the current context.
         * @param spanCount the column count, default is 3.
         * @param firstLeft the first left spacing (px).
         * @param left the each left spacing (px).
         * @param firstTop the first top spacing (px).
         * @param top the each top spacing (px).
         * @param lastRight the last right spacing (px).
         * @param right the each right spacing (px).
         * @param lastBottom the last bottom spacing (px).
         * @param bottom the each bottom spacing (px).
         * @param isReverseLayout reverse the layout, default false.
         * @return [RecyclerCosmetic]
         */
        @JvmStatic
        @JvmOverloads
        @JvmName("fromCustomSpacingGridVertical")
        fun fromGridVertical(
            context: Context,
            spanCount: Int = 3,
            @Px firstLeft: Int = 0,
            @Px left: Int = 0,
            @Px firstTop: Int = 0,
            @Px top: Int = 0,
            @Px lastRight: Int = 0,
            @Px right: Int = 0,
            @Px lastBottom: Int = 0,
            @Px bottom: Int = 0,
            isReverseLayout: Boolean = false
        ) = from(
            layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, isReverseLayout),
            itemDecoration = GridVerticalItemDecoration(spanCount, firstLeft, left, firstTop, top, lastRight, right, lastBottom, bottom)
        )

        /**
         * Create a linear vertical cosmetic.
         * @param context the current context.
         * @param rowSpacing the row spacing (px), default is [defaultSpacingPx].
         * @param isReverseLayout reverse the layout, default false.
         * @return [RecyclerCosmetic]
         */
        @JvmStatic
        @JvmOverloads
        fun fromLinearVertical(
            context: Context,
            @Px rowSpacing: Int = context.defaultSpacingPx(),
            isReverseLayout: Boolean = false
        ) = from(
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, isReverseLayout),
            itemDecoration = LinearVerticalItemDecoration(rowSpacing)
        )

        /**
         * Create a linear vertical cosmetic.
         * @param context the current context.
         * @param left the each left spacing (px).
         * @param firstTop the first top spacing (px).
         * @param top the each top spacing (px).
         * @param right the each right spacing (px).
         * @param lastBottom the last bottom spacing (px).
         * @param bottom the each bottom spacing (px).
         * @param isReverseLayout reverse the layout, default false.
         * @return [RecyclerCosmetic]
         */
        @JvmStatic
        @JvmOverloads
        @JvmName("fromCustomSpacingLinearVertical")
        fun fromLinearVertical(
            context: Context,
            @Px left: Int = 0,
            @Px firstTop: Int = 0,
            @Px top: Int = 0,
            @Px right: Int = 0,
            @Px lastBottom: Int = 0,
            @Px bottom: Int = 0,
            isReverseLayout: Boolean = false
        ) = from(
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, isReverseLayout),
            itemDecoration = LinearVerticalItemDecoration(left, firstTop, top, right, lastBottom, bottom)
        )

        /**
         * Create a linear horizontal cosmetic.
         * @param context the current context.
         * @param columnSpacing the column spacing (px), default is [defaultSpacingPx].
         * @param isReverseLayout reverse the layout, default false.
         * @return [RecyclerCosmetic]
         */
        @JvmStatic
        @JvmOverloads
        fun fromLinearHorizontal(
            context: Context,
            @Px columnSpacing: Int = context.defaultSpacingPx(),
            isReverseLayout: Boolean = false
        ) = from(
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, isReverseLayout),
            itemDecoration = LinearHorizontalItemDecoration(columnSpacing)
        )

        /**
         * Create a linear horizontal cosmetic.
         * @param context the current context.
         * @param firstLeft the first left spacing (px).
         * @param left the each left spacing (px).
         * @param top the each top spacing (px).
         * @param lastRight the last right spacing (px).
         * @param right the each right spacing (px).
         * @param bottom the each bottom spacing (px).
         * @param isReverseLayout reverse the layout, default false.
         * @return [RecyclerCosmetic]
         */
        @JvmStatic
        @JvmOverloads
        @JvmName("fromCustomSpacingLinearHorizontal")
        fun fromLinearHorizontal(
            context: Context,
            @Px firstLeft: Int = 0,
            @Px left: Int = 0,
            @Px top: Int = 0,
            @Px lastRight: Int = 0,
            @Px right: Int = 0,
            @Px bottom: Int = 0,
            isReverseLayout: Boolean = false
        ) = from(
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, isReverseLayout),
            itemDecoration = LinearHorizontalItemDecoration(firstLeft, left, top, lastRight, right, bottom)
        )
    }
}
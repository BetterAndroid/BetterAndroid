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
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.adapter.recycler.decoration

import android.graphics.Rect
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.recycler.decoration.base.BaseRecyclerItemDecoration

/**
 * [RecyclerView] linear horizontal decoration.
 */
class LinearHorizontalItemDecoration : BaseRecyclerItemDecoration {

    /** The current item spacing. */
    private var rowColumnRect: RowColumnRect? = null

    /** The current item column spacing (px). */
    private var columnSpacing = 0

    /**
     * Spacing adaption rect.
     * @param firstLeft the first left spacing (px).
     * @param left the each left spacing (px).
     * @param top the each top spacing (px).
     * @param lastRight the last right spacing (px).
     * @param right the each right spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    private class RowColumnRect(
        @Px val firstLeft: Int,
        @Px val left: Int,
        @Px val top: Int,
        @Px val lastRight: Int,
        @Px val right: Int,
        @Px val bottom: Int
    )

    /**
     * Initialized as custom entry spacing adaptation.
     * @param firstLeft the first left spacing (px).
     * @param left the each left spacing (px).
     * @param top the each top spacing (px).
     * @param lastRight the last right spacing (px).
     * @param right the each right spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    constructor(
        @Px firstLeft: Int = 0,
        @Px left: Int = 0,
        @Px top: Int = 0,
        @Px lastRight: Int = 0,
        @Px right: Int = 0,
        @Px bottom: Int = 0
    ) {
        update(firstLeft, left, top, lastRight, right, bottom)
    }

    /**
     * Initialized as fixed column spacing.
     * @param columnSpacing the column spacing (px).
     */
    constructor(@Px columnSpacing: Int) {
        update(columnSpacing)
    }

    /**
     * Update the current item spacing.
     * @param firstLeft the first left spacing (px).
     * @param left the each left spacing (px).
     * @param top the each top spacing (px).
     * @param lastRight the last right spacing (px).
     * @param right the each right spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    fun update(
        @Px firstLeft: Int = rowColumnRect?.firstLeft ?: 0,
        @Px left: Int = rowColumnRect?.left ?: 0,
        @Px top: Int = rowColumnRect?.top ?: 0,
        @Px lastRight: Int = rowColumnRect?.lastRight ?: 0,
        @Px right: Int = rowColumnRect?.right ?: 0,
        @Px bottom: Int = rowColumnRect?.bottom ?: 0
    ) {
        rowColumnRect = RowColumnRect(firstLeft, left, top, lastRight, right, bottom)
    }

    /**
     * Update the current item spacing.
     * @param columnSpacing the column spacing (px).
     */
    fun update(@Px columnSpacing: Int = this.columnSpacing) {
        rowColumnRect = null
        this.columnSpacing = columnSpacing
    }

    override fun onCalculateItemOffsets(outRect: Rect, position: Int, itemCount: Int) {
        rowColumnRect?.apply {
            outRect.set(if (position == 0) firstLeft else left, top, if (position == itemCount - 1) lastRight else right, bottom)
        } ?: run { outRect.set(0, 0, if (position == itemCount - 1) 0 else columnSpacing, 0) }
    }
}
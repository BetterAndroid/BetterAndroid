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
 * [RecyclerView] linear vertical decoration.
 */
class LinearVerticalItemDecoration : BaseRecyclerItemDecoration {

    /** The current item spacing. */
    private var rowColumnRect: RowColumnRect? = null

    /** The current item row spacing (px). */
    private var rowSpacing = 0

    /**
     * Spacing adaption rect.
     * @param left the each left spacing (px).
     * @param firstTop the first top spacing (px).
     * @param top the each top spacing (px).
     * @param right the each right spacing (px).
     * @param lastBottom the last bottom spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    private data class RowColumnRect(
        @Px val left: Int,
        @Px val firstTop: Int,
        @Px val top: Int,
        @Px val right: Int,
        @Px val lastBottom: Int,
        @Px val bottom: Int
    )

    /**
     * Initialized as custom entry spacing adaptation.
     * @param left the each left spacing (px).
     * @param firstTop the first top spacing (px).
     * @param top the each top spacing (px).
     * @param right the each right spacing (px).
     * @param lastBottom the last bottom spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    constructor(
        @Px left: Int = 0,
        @Px firstTop: Int = 0,
        @Px top: Int = 0,
        @Px right: Int = 0,
        @Px lastBottom: Int = 0,
        @Px bottom: Int = 0
    ) {
        update(left, firstTop, top, right, lastBottom, bottom)
    }

    /**
     * Initialized as fixed row spacing.
     * @param rowSpacing the row spacing (px).
     */
    constructor(@Px rowSpacing: Int) {
        update(rowSpacing)
    }

    /**
     * Update the current item spacing.
     * @param left the each left spacing (px).
     * @param firstTop the first top spacing (px).
     * @param top the each top spacing (px).
     * @param right the each right spacing (px).
     * @param lastBottom the last bottom spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    fun update(
        @Px left: Int = rowColumnRect?.left ?: 0,
        @Px firstTop: Int = rowColumnRect?.firstTop ?: 0,
        @Px top: Int = rowColumnRect?.top ?: 0,
        @Px right: Int = rowColumnRect?.right ?: 0,
        @Px lastBottom: Int = rowColumnRect?.lastBottom ?: 0,
        @Px bottom: Int = rowColumnRect?.bottom ?: 0
    ) {
        rowColumnRect = RowColumnRect(left, firstTop, top, right, lastBottom, bottom)
    }

    /**
     * Update the current item spacing.
     * @param rowSpacing the row spacing (px).
     */
    fun update(@Px rowSpacing: Int = this.rowSpacing) {
        rowColumnRect = null
        this.rowSpacing = rowSpacing
    }

    override fun onCalculateItemOffsets(outRect: Rect, position: Int, itemCount: Int) {
        rowColumnRect?.apply {
            outRect.set(left, if (position == 0) firstTop else top, right, if (position == itemCount - 1) lastBottom else bottom)
        } ?: run { outRect.set(0, 0, 0, if (position == itemCount - 1) 0 else rowSpacing) }
    }
}
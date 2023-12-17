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
package com.highcapable.betterandroid.ui.component.adapter.recycler.decoration

import android.graphics.Rect
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.recycler.decoration.base.BaseRecyclerItemDecoration

/**
 * [RecyclerView] grid item decoration.
 */
class GridVerticalItemDecoration : BaseRecyclerItemDecoration {

    /** The current column count. */
    private var spanCount = 0

    /** The current item spacing. */
    private var rowColumnRect: RowColumnRect? = null

    /** The current item column spacing (px). */
    private var columnSpacing = 0

    /** The current item row spacing (px). */
    private var rowSpacing = 0

    /**
     * Spacing adaption rect.
     * @param firstLeft the first left spacing (px).
     * @param left the each left spacing (px).
     * @param firstTop the first top spacing (px).
     * @param top the each top spacing (px).
     * @param lastRight the last right spacing (px).
     * @param right the each right spacing (px).
     * @param lastBottom the last bottom spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    private class RowColumnRect(
        @Px val firstLeft: Int,
        @Px val left: Int,
        @Px val firstTop: Int,
        @Px val top: Int,
        @Px val lastRight: Int,
        @Px val right: Int,
        @Px val lastBottom: Int,
        @Px val bottom: Int
    )

    /**
     * Initialized as custom entry spacing adaptation.
     * @param spanCount the column count, default is 3.
     * @param firstLeft the first left spacing (px).
     * @param left the each left spacing (px).
     * @param firstTop the first top spacing (px).
     * @param top the each top spacing (px).
     * @param lastRight the last right spacing (px).
     * @param right the each right spacing (px).
     * @param lastBottom the last bottom spacing (px).
     * @param bottom the each bottom spacing (px).
     */
    constructor(
        spanCount: Int = 3,
        @Px firstLeft: Int = 0,
        @Px left: Int = 0,
        @Px firstTop: Int = 0,
        @Px top: Int = 0,
        @Px lastRight: Int = 0,
        @Px right: Int = 0,
        @Px lastBottom: Int = 0,
        @Px bottom: Int = 0
    ) {
        this.spanCount = spanCount
        rowColumnRect = RowColumnRect(firstLeft, left, firstTop, top, lastRight, right, lastBottom, bottom)
    }

    /**
     * Initialized as fixed column spacing and row spacing.
     * @param spanCount the column count, default is 3.
     * @param columnSpacing the column spacing (px).
     * @param rowSpacing the row spacing (px).
     */
    constructor(spanCount: Int = 3, @Px columnSpacing: Int, @Px rowSpacing: Int) {
        this.spanCount = spanCount
        this.columnSpacing = columnSpacing
        this.rowSpacing = rowSpacing
    }

    override fun onCalculateItemOffsets(outRect: Rect, position: Int, itemCount: Int) {
        rowColumnRect?.apply {
            val fullyLastIndex = itemCount - (itemCount % spanCount) - 1
            val isFirstColumn = position % spanCount == 0
            val isLastColumn = (position + 1) % spanCount == 0
            val isFirstRow = position < spanCount
            val isLastRow = position > fullyLastIndex || ((fullyLastIndex == itemCount - 1) &&
                (position in itemCount - spanCount..itemCount))
            outRect.set(
                if (isFirstColumn) firstLeft else left,
                if (isFirstRow) firstTop else top,
                if (isLastColumn) lastRight else right,
                if (isLastRow) lastBottom else bottom
            )
        } ?: run {
            val columnIndex = position % spanCount
            outRect.left = columnIndex * columnSpacing / spanCount
            outRect.right = columnSpacing - (columnIndex + 1) * columnSpacing / spanCount
            if (position >= spanCount) outRect.top = rowSpacing
        }
    }
}
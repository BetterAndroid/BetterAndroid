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
        @Px val firstLeft: Int, @Px val left: Int,
        @Px val top: Int, @Px val lastRight: Int,
        @Px val right: Int, @Px val bottom: Int
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
    internal constructor(@Px firstLeft: Int, @Px left: Int, @Px top: Int, @Px lastRight: Int, @Px right: Int, @Px bottom: Int) {
        rowColumnRect = RowColumnRect(firstLeft, left, top, lastRight, right, bottom)
    }

    /**
     * Initialized as fixed column spacing.
     * @param columnSpacing the column spacing (px).
     */
    internal constructor(@Px columnSpacing: Int) {
        this.columnSpacing = columnSpacing
    }

    override fun calculateItemOffsets(outRect: Rect, position: Int, size: Int) {
        rowColumnRect?.apply { outRect.set(if (position == 0) firstLeft else left, top, if (position == size - 1) lastRight else right, bottom) }
            ?: run { outRect.set(0, 0, if (position == size - 1) 0 else columnSpacing, 0) }
    }
}
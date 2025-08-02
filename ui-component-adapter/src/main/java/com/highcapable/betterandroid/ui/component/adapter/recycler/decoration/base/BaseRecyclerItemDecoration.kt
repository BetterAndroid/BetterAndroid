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
package com.highcapable.betterandroid.ui.component.adapter.recycler.decoration.base

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView] base item decoration.
 */
abstract class BaseRecyclerItemDecoration internal constructor() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val dataSetCount = parent.adapter?.itemCount ?: 0
        if (dataSetCount >= 0) onCalculateItemOffsets(outRect, parent.getChildAdapterPosition(view), dataSetCount)
    }

    /**
     * Callback and calculate the each item offsets.
     * @param outRect the item out rect.
     * @param position the current position.
     * @param itemCount the data set count.
     */
    internal abstract fun onCalculateItemOffsets(outRect: Rect, position: Int, itemCount: Int)
}
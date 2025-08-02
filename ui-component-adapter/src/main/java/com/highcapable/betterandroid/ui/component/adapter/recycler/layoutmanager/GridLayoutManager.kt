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
 * This file is created by fankes on 2025/1/24.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.RecyclerAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.recycler.factory.wrapper
import com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager.base.RecyclerLayoutManager
import androidx.recyclerview.widget.GridLayoutManager as AndroidXGridLayoutManager

/**
 * An enhanced [AndroidXGridLayoutManager] with the ability to handle the position compatibility.
 *
 * Call [excludingPosition] or [includingPosition] in any scene where you want to use position to convert it
 * to get the correct position when using [RecyclerAdapterBuilder].
 *
 * - Note: The following functions such as
 *   [findFirstVisibleItemPosition],
 *   [findFirstCompletelyVisibleItemPosition],
 *   [findLastVisibleItemPosition],
 *   [findLastCompletelyVisibleItemPosition],
 *   [scrollToPosition],
 *   [scrollToPositionWithOffset],
 *   [smoothScrollToPosition]
 *   are already overridden to handle the
 *   compatibility, you should not continue process the position compatibility in these functions.
 * @see RecyclerLayoutManager
 */
open class GridLayoutManager : AndroidXGridLayoutManager {

    /** The current held [RecyclerView] instance. */
    protected var base: RecyclerView? = null

    constructor(context: Context?, spanCount: Int) : super(context, spanCount)
    constructor(context: Context?, spanCount: Int, @RecyclerView.Orientation orientation: Int, reverseLayout: Boolean) :
        super(context, spanCount, orientation, reverseLayout)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
        super(context, attrs, defStyleAttr, defStyleRes)

    @CallSuper
    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        this.base = view
    }

    @CallSuper
    override fun onDetachedFromWindow(view: RecyclerView, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        this.base = null
    }

    @CallSuper
    override fun findFirstVisibleItemPosition() = excludingPosition(super.findFirstVisibleItemPosition())

    @CallSuper
    override fun findFirstCompletelyVisibleItemPosition() = excludingPosition(super.findFirstCompletelyVisibleItemPosition())

    @CallSuper
    override fun findLastVisibleItemPosition() = excludingPosition(super.findLastVisibleItemPosition())

    @CallSuper
    override fun findLastCompletelyVisibleItemPosition() = excludingPosition(super.findLastCompletelyVisibleItemPosition())

    @CallSuper
    override fun scrollToPosition(position: Int) {
        val current = includingPosition(position)
        super.scrollToPosition(current)
    }

    @CallSuper
    override fun scrollToPositionWithOffset(position: Int, offset: Int) {
        val current = includingPosition(position)
        super.scrollToPositionWithOffset(current, offset)
    }

    @CallSuper
    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val current = includingPosition(position)
        super.smoothScrollToPosition(recyclerView, state, current)
    }

    /**
     * Convert the current position of the item view excluding the header view
     * param position the current position.
     * @param position the current position.
     * @return [Int]
     */
    protected fun excludingPosition(position: Int) = base?.adapter?.wrapper?.excludingPosition(position) ?: position

    /**
     * Convert the current position of the item view excluding the header view
     * param position the current position.
     * @param position the current position.
     * @return [Int]
     */
    protected fun includingPosition(position: Int) = base?.adapter?.wrapper?.includingPosition(position) ?: position
}
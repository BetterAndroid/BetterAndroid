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
import androidx.recyclerview.widget.LinearLayoutManager as AndroidXLinearLayoutManager

/**
 * An enhanced [AndroidXLinearLayoutManager] with the ability to handle the position compatibility.
 *
 * Call [Int.toExcludingPosition] or [Int.toIncludingPosition] in any scene where you want to use position to convert it
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
open class LinearLayoutManager : AndroidXLinearLayoutManager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, @RecyclerView.Orientation orientation: Int, reverseLayout: Boolean) :
        super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
        super(context, attrs, defStyleAttr, defStyleRes)

    private var base: RecyclerView? = null

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
    override fun findFirstVisibleItemPosition() = super.findFirstVisibleItemPosition().toExcludingPosition()

    @CallSuper
    override fun findFirstCompletelyVisibleItemPosition() = super.findFirstCompletelyVisibleItemPosition().toExcludingPosition()

    @CallSuper
    override fun findLastVisibleItemPosition() = super.findLastVisibleItemPosition().toExcludingPosition()

    @CallSuper
    override fun findLastCompletelyVisibleItemPosition() = super.findLastCompletelyVisibleItemPosition().toExcludingPosition()

    @CallSuper
    override fun scrollToPosition(position: Int) {
        val current = position.toIncludingPosition()
        super.scrollToPosition(current)
    }

    @CallSuper
    override fun scrollToPositionWithOffset(position: Int, offset: Int) {
        val current = position.toIncludingPosition()
        super.scrollToPositionWithOffset(current, offset)
    }

    @CallSuper
    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val current = position.toIncludingPosition()
        super.smoothScrollToPosition(recyclerView, state, current)
    }

    /**
     * Convert the current position of the item view excluding the header view.
     * param position the current position.
     * @receiver the current position.
     * @return [Int]
     */
    protected fun Int.toExcludingPosition() = base?.adapter?.wrapper?.excludingPosition(position = this) ?: this

    /**
     * Convert the current position of the item view excluding the header view.
     * param position the current position.
     * @receiver the current position.
     * @return [Int]
     */
    protected fun Int.toIncludingPosition() = base?.adapter?.wrapper?.includingPosition(position = this) ?: this
}
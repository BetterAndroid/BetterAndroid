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
 * This file is created by fankes on 2025/8/2.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.component.adapter.recycler.wrapper

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.RecyclerAdapterBuilder

/**
 * The adapter wrapper of [RecyclerAdapterBuilder].
 *
 * This is an extension to [RecyclerView.Adapter] to support header and footer layouts.
 */
class RecyclerAdapterWrapper internal constructor(
    private val builder: RecyclerAdapterBuilder<*>,
    private val instance: RecyclerAdapterBuilder<*>.Instance
) {

    /**
     * Convert the current position of the item view excluding the header view
     * param position the current position.
     * @return [Int]
     */
    @JvmSynthetic
    internal fun excludingPosition(position: Int) = builder.excludingPosition(position)

    /**
     * Convert the current position of the item view including the header view
     * param position the current position.
     * @return [Int]
     */
    @JvmSynthetic
    internal fun includingPosition(position: Int) = builder.includingPosition(position)

    /**
     * Whether the adapter has a header view.
     * @see RecyclerAdapterBuilder.hasHeaderView
     * @return [Boolean]
     */
    val hasHeaderView get() = builder.hasHeaderView

    /**
     * Whether the adapter has a footer view.
     * @see RecyclerAdapterBuilder.hasFooterView
     * @return [Boolean]
     */
    val hasFooterView get() = builder.hasFooterView

    /**
     * @see RecyclerView.Adapter.notifyDataSetChanged
     */
    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetChanged() = instance.notifyDataSetChanged()

    /**
     * Notify the header view changed whether it exists, if not will ignore.
     * @see RecyclerView.Adapter.notifyItemChanged
     */
    fun notifyHeaderItemChanged() {
        if (hasHeaderView) instance.notifyItemChanged(0)
    }

    /**
     * Notify the footer view changed whether it exists, if not will ignore.
     * @see RecyclerView.Adapter.notifyItemChanged
     */
    fun notifyFooterItemChanged() {
        if (hasFooterView) instance.notifyItemChanged(instance.itemCount - 1)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemChanged
     */
    fun notifyItemChanged(position: Int) {
        val current = includingPosition(position)

        instance.notifyItemChanged(current)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemChanged
     */
    fun notifyItemChanged(position: Int, payload: Any?) {
        val current = includingPosition(position)

        instance.notifyItemChanged(current, payload)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemRangeChanged
     */
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        val start = includingPosition(positionStart)
        val count = includingPosition(itemCount)

        instance.notifyItemRangeChanged(start, count)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemRangeChanged
     */
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        val start = includingPosition(positionStart)
        val count = includingPosition(itemCount)

        instance.notifyItemRangeChanged(start, count, payload)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemInserted
     */
    fun notifyItemInserted(position: Int) {
        val current = includingPosition(position)

        instance.notifyItemInserted(current)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemMoved
     */
    fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        val from = includingPosition(fromPosition)
        val to = includingPosition(toPosition)

        instance.notifyItemMoved(from, to)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemRangeInserted
     */
    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        val start = includingPosition(positionStart)
        val count = includingPosition(itemCount)

        instance.notifyItemRangeInserted(start, count)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemRemoved
     */
    fun notifyItemRemoved(position: Int) {
        val current = includingPosition(position)

        instance.notifyItemRemoved(current)
    }

    /**
     * @see RecyclerView.Adapter.notifyItemRangeRemoved
     */
    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        val start = includingPosition(positionStart)
        val count = includingPosition(itemCount)

        instance.notifyItemRangeRemoved(start, count)
    }
}
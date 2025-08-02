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
 * This file is created by fankes on 2025/1/22.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.adapter.entity

import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter dynamic position entity.
 *
 * Use [value] to get the current position.
 */
class AdapterPosition private constructor() {

    internal companion object {

        /**
         * Create a new [AdapterPosition].
         * @param layout located in [RecyclerView.ViewHolder.getLayoutPosition]
         * @param binding located in [RecyclerView.ViewHolder.getBindingAdapterPosition]
         * @param absolute located in [RecyclerView.ViewHolder.getAbsoluteAdapterPosition]
         * @return [AdapterPosition]
         */
        fun from(
            layout: () -> Int,
            binding: () -> Int,
            absolute: () -> Int
        ) = AdapterPosition().apply {
            layoutValueCallback = layout
            bindingValueCallback = binding
            absoluteValueCallback = absolute
        }
    }

    /** The [RecyclerView.ViewHolder.getLayoutPosition] callback. */
    private var layoutValueCallback: (() -> Int)? = null

    /** The [RecyclerView.ViewHolder.getBindingAdapterPosition] callback. */
    private var bindingValueCallback: (() -> Int)? = null

    /** The [RecyclerView.ViewHolder.getAbsoluteAdapterPosition] callback. */
    private var absoluteValueCallback: (() -> Int)? = null

    /**
     * Get the current layout position value.
     *
     * Located in [RecyclerView.ViewHolder.getLayoutPosition].
     * @see value
     * @see absolute
     * @return [Int]
     */
    val layout get() = layoutValueCallback?.invoke() ?: RecyclerView.NO_POSITION

    /**
     * Get the current binding position value.
     *
     * Located in [RecyclerView.ViewHolder.getBindingAdapterPosition].
     * @see layout
     * @see absolute
     * @return [Int]
     */
    val value get() = bindingValueCallback?.invoke() ?: RecyclerView.NO_POSITION

    /**
     * Get the current absolute position value.
     *
     * Located in [RecyclerView.ViewHolder.getAbsoluteAdapterPosition].
     * @see layout
     * @see value
     * @return [Int]
     */
    val absolute get() = absoluteValueCallback?.invoke() ?: RecyclerView.NO_POSITION

    override fun toString() = "AdapterPosition(layout=$layout, value=$value, absolute=$absolute)"
}
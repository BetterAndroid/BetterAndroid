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
 * This file is created by fankes on 2025/7/20.
 */
@file:Suppress("unused")
@file:JvmName("RecyclerViewUtils")

package com.highcapable.betterandroid.ui.component.adapter.recycler.factory

import androidx.recyclerview.widget.RecyclerView

/**
 * Scrolls the [RecyclerView.LayoutManager] to the first position.
 * @see RecyclerView.scrollToPosition
 */
fun RecyclerView.scrollToFirstPosition() {
    val position = calculateFirstPosition() ?: return
    scrollToPosition(position)
}

/**
 * Scrolls the [RecyclerView.LayoutManager] to the last position.
 * @see RecyclerView.scrollToPosition
 */
fun RecyclerView.scrollToLastPosition() {
    val position = calculateLastPosition() ?: return
    scrollToPosition(position)
}

/**
 * Smoothly scrolls the [RecyclerView.LayoutManager] to the first position.
 * @see RecyclerView.smoothScrollToPosition
 */
fun RecyclerView.smoothScrollToFirstPosition() {
    val position = calculateFirstPosition() ?: return
    smoothScrollToPosition(position)
}

/**
 * Smoothly scrolls the [RecyclerView.LayoutManager] to the last position.
 * @see RecyclerView.smoothScrollToPosition
 */
fun RecyclerView.smoothScrollToLastPosition() {
    val position = calculateLastPosition() ?: return
    smoothScrollToPosition(position)
}

private fun RecyclerView.calculateFirstPosition(): Int? {
    if (adapter == null) return null
    val hasHeaderView = adapter?.wrapper?.hasHeaderView == true
    return if (hasHeaderView) -1 else 0
}

private fun RecyclerView.calculateLastPosition(): Int? {
    if (adapter == null) return null
    val hasFooterView = adapter?.wrapper?.hasFooterView == true
    val count = adapter?.itemCount ?: 0
    return if (hasFooterView) count else count - 1
}
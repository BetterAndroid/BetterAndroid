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
 * This file is created by fankes on 2025/1/25.
 */
@file:Suppress("unused")
@file:JvmName("AdapterUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.extension.generated.BetterAndroidProperties

/**
 * Notify that all items in the adapter have been inserted.
 *
 * Usage:
 *
 * ```kotlin
 * // The [dataSet] must belong to the adapter and reflected in [itemCount].
 * dataSet.addAll(newDataSet)
 * adapter.notifyAllItemsChanged()
 * // Or.
 * adapter.notifyAllItemsChanged(dataSet)
 * ```
 * @see RecyclerView.Adapter.notifyItemRangeInserted
 * @receiver [RecyclerView.Adapter]
 * @param dataSet the data set to be inserted, default is null.
 */
fun RecyclerView.Adapter<*>.notifyAllItemsInserted(dataSet: Collection<*>? = null) {
    val count = dataSet?.size ?: itemCount
    if (count <= 0) {
        Log.w(BetterAndroidProperties.PROJECT_NAME, "notifyAllItemsInserted called with count <= 0.")
        return
    }; notifyItemRangeInserted(0, count)
}

/**
 * Notify that all items in the adapter have been updated.
 *
 * Usage:
 * 
 * ```kotlin
 * // The [dataSet] must belong to the adapter and reflected in [itemCount].
 * dataSet.forEach {
 *     // For example, the content of the data set is updated.
 *     it.update()
 * }
 * adapter.notifyAllItemsChanged()
 * // Or.
 * adapter.notifyAllItemsChanged(dataSet)
 * ```
 * @see RecyclerView.Adapter.notifyItemRangeChanged
 * @receiver [RecyclerView.Adapter]
 * @param dataSet the data set to be updated, default is null.
 */
fun RecyclerView.Adapter<*>.notifyAllItemsChanged(dataSet: Collection<*>? = null) {
    val count = dataSet?.size ?: itemCount
    if (count <= 0) {
        Log.w(BetterAndroidProperties.PROJECT_NAME, "notifyAllItemsChanged called with count <= 0.")
        return
    }; notifyItemRangeChanged(0, count)
}

/**
 * Clear the [dataSet] and notify that all items in the adapter have been removed.
 *
 * Usage:
 *
 * ```kotlin
 * // The [dataSet] must belong to the adapter and reflected in [itemCount].
 * adapter.clearAndNotify(dataSet)
 * ```
 * @see RecyclerView.Adapter.notifyItemRangeRemoved
 * @receiver [RecyclerView.Adapter]
 * @param dataSet the data set to be cleared.
 */
fun RecyclerView.Adapter<*>.clearAndNotify(dataSet: MutableCollection<*>) {
    val count = dataSet.size
    if (count <= 0) {
        Log.w(BetterAndroidProperties.PROJECT_NAME, "clearAndNotify called with dataSet.size <= 0.")
        return
    }; dataSet.clear()
    notifyItemRangeRemoved(0, count)
}

/**
 * Notify any registered observers that the data set has changed (ignore lint warning).
 *
 * - Note: We do not recommend always using this function to notify that the data set has changed.
 * @see RecyclerView.Adapter.notifyDataSetChanged
 */
fun RecyclerView.Adapter<*>.notifyDataSetChangedIgnore() {
    @Suppress("NotifyDataSetChanged")
    notifyDataSetChanged()
}
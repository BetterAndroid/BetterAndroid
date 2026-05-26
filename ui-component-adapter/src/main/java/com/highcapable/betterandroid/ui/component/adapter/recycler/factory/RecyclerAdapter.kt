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
@file:Suppress("unused", "FunctionName")
@file:JvmName("RecyclerAdapterUtils")

package com.highcapable.betterandroid.ui.component.adapter.recycler.factory

import android.util.Log
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.RecyclerAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.adapter.recycler.wrapper.RecyclerAdapterWrapper

/**
 * Get the [RecyclerView.Adapter] wrapper from [RecyclerAdapterBuilder].
 *
 * If the current adapter is not creating by [RecyclerAdapterBuilder], it will return null.
 * @receiver [RecyclerView.Adapter]
 * @return [RecyclerAdapterWrapper] or null.
 */
val RecyclerView.Adapter<*>.wrapper get() = when (this) {
    is RecyclerAdapterBuilder<*>.Instance -> this.wrapper
    else -> null
}

/**
 * Create the [ListUpdateCallback] of current adapter.
 * @receiver [RecyclerView.Adapter]
 * @return [ListUpdateCallback]
 */
internal fun RecyclerView.Adapter<*>.createAdapterListUpdateCallback() = wrapper?.let { wrapper ->
    ListUpdateCallback(
        onInserted = { position, count ->
            notifyItemRangeInserted(wrapper.includingPosition(position), count)
        },
        onRemoved = { position, count ->
            notifyItemRangeRemoved(wrapper.includingPosition(position), count)
        },
        onMoved = { fromPosition, toPosition ->
            notifyItemMoved(wrapper.includingPosition(fromPosition), wrapper.includingPosition(toPosition))
        },
        onChanged = { position, count, payload ->
            notifyItemRangeChanged(wrapper.includingPosition(position), count, payload)
        }
    )
} ?: AdapterListUpdateCallback(this)

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
    }

    notifyItemRangeInserted(0, count)
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
    }

    notifyItemRangeChanged(0, count)
}

/**
 * Notify that the adapter has been updated by diff.
 *
 * Usage:
 *
 * ```kotlin
 * // Assume that's your dataset.
 * val dataSet = mutableListOf<MyEntity>()
 * // Save the old data snapshot.
 * val oldList = dataSet.toList()
 * // Update the current dataset.
 * dataSet.clear()
 * dataSet.addAll(newDataSet)
 * // Notify the adapter using DiffUtil.
 * adapter.notifyByDiff(
 *     oldList = oldList,
 *     newList = dataSet,
 *     areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
 *     areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
 *     detectMoves = true
 * )
 * ```
 *
 * - Note: The [newList] must belong to the adapter and already be reflected in [RecyclerView.Adapter.itemCount].
 * @see DiffUtil.calculateDiff
 * @receiver [RecyclerView.Adapter]
 * @param oldList the old data list snapshot.
 * @param newList the new data list snapshot.
 * @param areItemsTheSame compare whether the two items represent the same object.
 * @param areContentsTheSame compare whether the contents of the two items are the same.
 * @param getChangePayload get the changed payload between two items, default is null.
 * @param detectMoves whether to detect moved items, default true.
 */
fun <T> RecyclerView.Adapter<*>.notifyByDiff(
    oldList: List<T>,
    newList: List<T>,
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean,
    getChangePayload: (oldItem: T, newItem: T) -> Any? = { _, _ -> null },
    detectMoves: Boolean = true
) {
    val wrapper = wrapper
    val expectedItemCount = newList.size +
        (if (wrapper?.hasHeaderView == true) 1 else 0) +
        (if (wrapper?.hasFooterView == true) 1 else 0)

    if (itemCount != expectedItemCount) Log.w(
        BetterAndroidProperties.PROJECT_NAME,
        "notifyByDiff called with mismatched itemCount, current adapter.itemCount is $itemCount, but expected $expectedItemCount."
    )

    notifyByDiff(
        callback = DiffUtilCallback(
            oldList = oldList,
            newList = newList,
            areItemsTheSame = areItemsTheSame,
            areContentsTheSame = areContentsTheSame,
            getChangePayload = getChangePayload
        ),
        detectMoves = detectMoves
    )
}

/**
 * Notify that the adapter has been updated by [DiffUtil.Callback].
 *
 * Usage:
 *
 * ```kotlin
 * val callback = DiffUtilCallback(
 *     oldList = oldList,
 *     newList = dataSet,
 *     areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
 *     areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
 * )
 * adapter.notifyByDiff(callback)
 * ```
 *
 * @see DiffUtil.calculateDiff
 * @receiver [RecyclerView.Adapter]
 * @param callback the [DiffUtil.Callback] to calculate differences.
 * @param detectMoves whether to detect moved items, default true.
 */
fun RecyclerView.Adapter<*>.notifyByDiff(
    callback: DiffUtil.Callback,
    detectMoves: Boolean = true
) {
    val diffResult = DiffUtil.calculateDiff(callback, detectMoves)
    diffResult.dispatchUpdatesTo(createAdapterListUpdateCallback())
}

/**
 * Create a [DiffUtil.Callback] for list diff calculation.
 *
 * Usage:
 *
 * ```kotlin
 * val callback = DiffUtilCallback(
 *     oldList = oldList,
 *     newList = newList,
 *     areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
 *     areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
 * )
 * ```
 * @see DiffUtil.Callback
 * @param oldList the old data list snapshot.
 * @param newList the new data list snapshot.
 * @param areItemsTheSame compare whether the two items represent the same object.
 * @param areContentsTheSame compare whether the contents of the two items are the same.
 * @param getChangePayload get the changed payload between two items, default is null.
 * @return [DiffUtil.Callback]
 */
fun <T> DiffUtilCallback(
    oldList: List<T>,
    newList: List<T>,
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean,
    getChangePayload: (oldItem: T, newItem: T) -> Any? = { _, _ -> null }
) = object : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) =
        getChangePayload(oldList[oldItemPosition], newList[newItemPosition])
}

/**
 * Create a [ListUpdateCallback] for dispatching diff updates.
 *
 * Usage:
 *
 * ```kotlin
 * val callback = ListUpdateCallback(
 *     onInserted = { position, count -> /* Handle inserted items */ },
 *     onRemoved = { position, count -> /* Handle removed items */ },
 *     onMoved = { fromPosition, toPosition -> /* Handle moved items */ },
 *     onChanged = { position, count, payload -> /* Handle changed items */ } }
 * )
 * ```
 * @see ListUpdateCallback
 * @param onInserted callback for inserted items, default is no-op.
 * @param onRemoved callback for removed items, default is no-op.
 * @param onMoved callback for moved items, default is no-op.
 * @param onChanged callback for changed items, default is no-op.
 * @return [ListUpdateCallback]
 */
fun ListUpdateCallback(
    onInserted: (position: Int, count: Int) -> Unit = { _, _ -> },
    onRemoved: (position: Int, count: Int) -> Unit = { _, _ -> },
    onMoved: (fromPosition: Int, toPosition: Int) -> Unit = { _, _ -> },
    onChanged: (position: Int, count: Int, payload: Any?) -> Unit = { _, _, _ -> }
): ListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) = onInserted(position, count)
    override fun onRemoved(position: Int, count: Int) = onRemoved(position, count)
    override fun onMoved(fromPosition: Int, toPosition: Int) = onMoved(fromPosition, toPosition)
    override fun onChanged(position: Int, count: Int, payload: Any?) = onChanged(position, count, payload)
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
    }

    dataSet.clear()
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
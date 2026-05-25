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
 * This file is created by fankes on 2026/5/26.
 */
package com.highcapable.betterandroid.ui.component.adapter.recycler.diff

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.recycler.factory.DiffUtilCallback
import com.highcapable.betterandroid.ui.component.adapter.recycler.factory.createAdapterListUpdateCallback
import java.util.Collections
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * The async list differ of [RecyclerView.Adapter].
 * @param callback the diff callback.
 */
class RecyclerAsyncDiffer<E> private constructor(
    adapter: RecyclerView.Adapter<*>,
    private val dataSet: List<E>,
    private val callback: Callback<E>
) {

    companion object {

        /** The main thread executor. */
        private val mainThreadExecutor = Executor { Handler(Looper.getMainLooper()).post(it) }

        /** The background executor for list diffing. */
        private val diffBackgroundExecutor: ExecutorService = Executors.newSingleThreadExecutor()

        /**
         * Create a new instance of [RecyclerAsyncDiffer].
         * @param adapter the current adapter.
         * @param dataSet the current data set.
         * @param callback the current diff callback.
         * @return [RecyclerAsyncDiffer]
         */
        fun <E> from(
            adapter: RecyclerView.Adapter<*>,
            dataSet: List<E>,
            callback: Callback<E>
        ) = RecyclerAsyncDiffer(adapter, dataSet, callback)
    }

    /**
     * The current diff callback.
     */
    class Callback<E>(
        val areItemsTheSame: (oldItem: E, newItem: E) -> Boolean,
        val areContentsTheSame: (oldItem: E, newItem: E) -> Boolean,
        val getChangePayload: (oldItem: E, newItem: E) -> Any? = { _, _ -> null },
        val detectMoves: Boolean
    )

    /** The current update callback. */
    private val updateCallback = adapter.createAdapterListUpdateCallback()

    /** The current submitted source list. */
    private var submittedSourceList: List<E>? = null

    /** The current submitted list. */
    private var submittedList: List<E>? = null

    /** The current max generation. */
    private var maxScheduledGeneration = 0

    /** The current list. */
    var currentList: List<E> = dataSet
        private set

    /**
     * Submit the current list.
     * @param list the current list.
     * @param commitCallback the commit callback, default is null.
     */
    @JvmOverloads
    fun submitList(list: List<E>?, commitCallback: Runnable? = null) {
        val runGeneration = ++maxScheduledGeneration

        if (list === submittedSourceList) {
            commitCallback?.run()
            return
        }

        if (list == null) {
            val countRemoved = submittedList?.size ?: currentList.size

            submittedSourceList = null
            submittedList = null
            currentList = dataSet

            updateCallback.onRemoved(0, countRemoved)
            commitCallback?.run()
            return
        }

        val newList = list.toList()

        if (submittedList == null) {
            submittedSourceList = list
            submittedList = newList
            currentList = Collections.unmodifiableList(newList)

            updateCallback.onInserted(0, newList.size)
            commitCallback?.run()
            return
        }

        val oldList = submittedList ?: dataSet

        diffBackgroundExecutor.execute {
            val diffResult = DiffUtil.calculateDiff(
                DiffUtilCallback(
                    oldList = oldList,
                    newList = newList,
                    areItemsTheSame = callback.areItemsTheSame,
                    areContentsTheSame = callback.areContentsTheSame,
                    getChangePayload = callback.getChangePayload
                ),
                callback.detectMoves
            )

            mainThreadExecutor.execute {
                if (maxScheduledGeneration != runGeneration) return@execute

                submittedSourceList = list
                submittedList = newList
                currentList = Collections.unmodifiableList(newList)

                diffResult.dispatchUpdatesTo(updateCallback)
                commitCallback?.run()
            }
        }
    }
}
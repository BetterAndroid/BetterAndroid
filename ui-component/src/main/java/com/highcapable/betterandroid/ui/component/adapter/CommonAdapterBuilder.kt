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
 * This file is created by fankes on 2022/11/2.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.highcapable.betterandroid.ui.component.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ListView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.viewbinding.ViewBinding
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.factory.bindAdapter
import com.highcapable.betterandroid.ui.component.adapter.view.CommonItemView
import com.highcapable.betterandroid.ui.extension.binding.ViewBinding
import com.highcapable.betterandroid.ui.extension.view.inflate
import com.highcapable.betterandroid.ui.extension.view.layoutInflater
import androidx.appcompat.widget.ListPopupWindow as AndroidX_ListPopupWindow

/**
 * [BaseAdapter] builder, using entity [E].
 * @param adapterContext the current context.
 */
class CommonAdapterBuilder<E> private constructor(private val adapterContext: Context) : IAdapterBuilder {

    companion object {

        /** The no ID's item ID. */
        private const val ITEM_NO_ID = -1L

        /**
         * Create a new [CommonAdapterBuilder]<[E]> from [context].
         * @see ListView.bindAdapter
         * @see AutoCompleteTextView.bindAdapter
         * @see ListPopupWindow.bindAdapter
         * @see AndroidX_ListPopupWindow.bindAdapter
         * @param context the current context.
         * @return [CommonAdapterBuilder]<[E]>
         */
        @JvmStatic
        fun <E> from(context: Context) = CommonAdapterBuilder<E>(context)
    }

    /** The current each item function callback. */
    private var boundItemViewsCallback: CommonItemView<E>? = null

    /** The current each item on click event callbacks. */
    private var itemViewsOnClickCallbacks = mutableMapOf<Long, (View, E, Int) -> Unit>()

    /** The current each item on long click event callbacks. */
    private var itemViewsOnLongClickCallbacks = mutableMapOf<Long, (View, E, Int) -> Boolean>()

    /** The current [Filter] callback. */
    private var filterCallback: (() -> Filter)? = null

    /** The current [List] data callback. */
    private var listDataCallback: (() -> List<E>)? = null

    /** The current entity ID callback. */
    private var entityIdCallback: ((E, Int) -> Long)? = null

    /** The empty [Filter] callback. */
    private val emptyFilterCallback = {
        object : Filter() {
            override fun performFiltering(constraint: CharSequence?) = FilterResults()
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }
    }

    /**
     * Get the entity [E].
     * @param position the current position.
     * @return [E]
     */
    private fun getCurrentEntity(position: Int) = listDataCallback?.invoke()?.get(position) ?: Any() as E

    /**
     * Manually set the total number of data to be displayed.
     *
     * If you need an existing data array of entities, please bind using [onBindData].
     *
     * - If you have already set [onBindData],
     *   do not readjust the number here, wrong number may cause error.
     */
    var dataSetCount = -1

    /**
     * Manually set the total number of data to be displayed.
     *
     * If you need an existing data array of entities, please bind using [onBindData].
     *
     * - If you have already set [onBindData],
     *   do not readjust the number here, wrong number may cause error.
     * @param dataSetCount
     * @return [CommonAdapterBuilder]<[E]>
     */
    fun dataSetCount(dataSetCount: Int) = apply { this.dataSetCount = dataSetCount }

    /**
     * Bind [Filter] to [BaseAdapter].
     * @param result callback the data.
     * @return [CommonAdapterBuilder]<[E]>
     */
    fun onBindFilter(result: (() -> Filter)) = apply { filterCallback = result }

    /**
     * Bind [List] to [BaseAdapter].
     * @param result callback the data.
     * @return [CommonAdapterBuilder]<[E]>
     */
    fun onBindData(result: (() -> List<E>)) = apply { listDataCallback = result }

    /**
     * Bind each item ID to [BaseAdapter].
     *
     * If not set will use current position as the ID.
     * @param entityId callback the each item ID function.
     * @return [CommonAdapterBuilder]<[E]>
     */
    fun onBindItemId(entityId: (entity: E, position: Int) -> Long) = apply { entityIdCallback = entityId }

    /**
     * Add and create view holder with [VB].
     * @param boundItemViews callback and return each bound item function.
     * @return [CommonAdapterBuilder]<[E]>
     */
    @JvmName("onBindViewsTyped")
    inline fun <reified VB : ViewBinding> onBindViews(
        noinline boundItemViews: (binding: VB, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = apply {
        boundItemViewsCallback = CommonItemView(ViewBinding<VB>()) { binding, _, entity, position ->
            binding?.also { boundItemViews(it as VB, entity, position) }
        }
    }

    /**
     * Add and create view holder.
     * @param resId item view layout ID.
     * @param boundItemViews callback and return each bound item function.
     * @return [CommonAdapterBuilder]<[E]>
     */
    fun onBindViews(@LayoutRes resId: Int, boundItemViews: (view: View, entity: E, position: Int) -> Unit = { _, _, _ -> }) = apply {
        boundItemViewsCallback = CommonItemView(rootViewResId = resId) { _, itemView, entity, position ->
            itemView?.also { boundItemViews(it, entity, position) }
        }
    }

    /**
     * Add and create view holder.
     * @param view item [View], there must be no parent layout.
     * @param boundItemViews callback and return each bound item function.
     * @return [CommonAdapterBuilder]<[E]>
     */
    fun onBindViews(view: View, boundItemViews: (view: View, entity: E, position: Int) -> Unit = { _, _, _ -> }) = apply {
        boundItemViewsCallback = CommonItemView(rootView = view) { _, itemView, entity, position ->
            itemView?.also { boundItemViews(it, entity, position) }
        }
    }

    /**
     * Set the each item view on click events.
     * @param id specific a item ID to bind the on click event, default is [ITEM_NO_ID].
     * @param onClick callback and return each bound item function (the on click event callback).
     * @return [CommonAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onItemViewsClick(id: Long = ITEM_NO_ID, onClick: (view: View, entity: E, position: Int) -> Unit) =
        apply { itemViewsOnClickCallbacks[id] = onClick }

    /**
     * Set the each item view on long click events.
     * @param id specific a item ID to bind the on long click event, default is [ITEM_NO_ID].
     * @param onLongClick callback and return each bound item function (the on long click event callback).
     * @return [CommonAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onItemViewsLongClick(id: Long = ITEM_NO_ID, onLongClick: (view: View, entity: E, position: Int) -> Boolean) =
        apply { itemViewsOnLongClickCallbacks[id] = onLongClick }

    override fun build(): BaseAdapter = object : BaseAdapter(), Filterable {
        override fun getFilter() = filterCallback?.invoke() ?: emptyFilterCallback()
        override fun getCount() = dataSetCount.takeIf { it >= 0 } ?: listDataCallback?.invoke()?.size ?: 0
        override fun getItem(position: Int) = getCurrentEntity(position)
        override fun getItemId(position: Int) = entityIdCallback?.invoke(getCurrentEntity(position), position) ?: position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var holderView = convertView
            val holder: CommonAdapterBuilder<E>.BaseViewHolder
            if (convertView == null) {
                holder = boundItemViewsCallback?.let {
                    when {
                        it.bindingBuilder != null ->
                            it.bindingBuilder.inflate(adapterContext.layoutInflater).let { binding ->
                                holderView = binding.root
                                BindingBaseHolder(binding = binding)
                            }
                        it.rootViewResId >= 0 ->
                            adapterContext.layoutInflater.inflate(it.rootViewResId).let { itemView ->
                                holderView = itemView
                                CommonBaseHolder(rootView = itemView)
                            }
                        it.rootView != null -> {
                            holderView = it.rootView
                            CommonBaseHolder(rootView = it.rootView)
                        }
                        else -> null
                    }
                } ?: error("Cannot bound ViewHolder on BaseAdapter, did you forgot to called onBindViews function?")
            } else holder = convertView.tag as CommonAdapterBuilder<E>.BaseViewHolder
            holderView?.apply {
                tag = holder
                itemViewsOnClickCallbacks
                    .filterKeys { it == ITEM_NO_ID || it == getItemId(position) }
                    .also { callbacks ->
                        if (callbacks.isNotEmpty()) setOnClickListener {
                            callbacks.forEach { (_, callback) -> callback(it, getCurrentEntity(position), position) }
                        }
                    }
                itemViewsOnLongClickCallbacks
                    .filterKeys { it == ITEM_NO_ID || it == getItemId(position) }
                    .also { callbacks ->
                        if (callbacks.isNotEmpty()) setOnLongClickListener {
                            var result = false
                            callbacks.forEach { (_, callback) -> result = callback(it, getCurrentEntity(position), position) }
                            result
                        }
                    }
            }
            boundItemViewsCallback?.onBindCallback?.invoke(
                (holder as? BindingBaseHolder?)?.binding, holder.rootView, getCurrentEntity(position), position
            ); return holderView ?: error("Cannot bound ViewHolder on BaseAdapter because got null instance.")
        }
    }

    /**
     * View holder of [ViewBinding].
     * @param binding the [ViewBinding].
     */
    inner class BindingBaseHolder(val binding: ViewBinding) : BaseViewHolder(binding.root)

    /**
     * Common view holder.
     * @param rootView the item view.
     */
    inner class CommonBaseHolder(override val rootView: View) : BaseViewHolder(rootView)

    /**
     * Base view holder.
     * @param rootView the item view.
     */
    abstract inner class BaseViewHolder(open val rootView: View)
}
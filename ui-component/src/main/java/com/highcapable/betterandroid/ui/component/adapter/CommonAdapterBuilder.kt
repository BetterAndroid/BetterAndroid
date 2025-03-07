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
import android.widget.AdapterView
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
import com.highcapable.betterandroid.ui.component.adapter.viewholder.CommonViewHolder
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.ViewBindingHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.XmlLayoutHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base.ViewHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.impl.CommonViewHolderImpl
import com.highcapable.betterandroid.ui.extension.binding.ViewBinding
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
    private var boundViewHolderCallback: CommonViewHolder<E>? = null

    /** The current each item on click event callbacks. */
    private var viewHolderOnClickCallbacks = mutableMapOf<Long, (View, E, Int) -> Unit>()

    /** The current each item on long click event callbacks. */
    private var viewHolderOnLongClickCallbacks = mutableMapOf<Long, (View, E, Int) -> Boolean>()

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
     * @return [E] or null.
     */
    private fun getCurrentEntity(position: Int) = (listDataCallback?.invoke() ?: emptyList()).let {
        if (it.isEmpty() && dataSetCount > 0) Any() as E else it.getOrNull(position)
    }

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
     * Create and add view holder from [ViewHolderDelegate]<[VD]>.
     * @param delegate the custom item view delegate.
     * @param viewHolder callback and return each bound item function.
     * @return [CommonAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun <VD : Any> onBindItemView(
        delegate: ViewHolderDelegate<VD>,
        viewHolder: (delegate: VD, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = apply {
        boundViewHolderCallback = CommonViewHolder(delegate as ViewHolderDelegate<Any>) { delegate, entity, position ->
            runCatching {
                viewHolder(delegate as VD, entity, position)
            }.onFailure {
                if (it is ClassCastException) error(
                    "The correct entity type is not provided with onBindData. " +
                        "Please remove the generic declaration or use onBindData to pass in the corresponding entity type collection."
                ) else throw it
            }
        }
    }

    /**
     * Create and add view holder from [ViewBinding]<[VB]>.
     * @param viewHolder callback and return each bound item function.
     * @return [CommonAdapterBuilder]<[E]>
     */
    inline fun <reified VB : ViewBinding> onBindItemView(
        noinline viewHolder: (binding: VB, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = onBindItemView(ViewBindingHolderDelegate(ViewBinding<VB>()), viewHolder)

    /**
     * Create and add view holder from XML layout ID.
     * @param resId item view layout ID.
     * @param viewHolder callback and return each bound item function.
     * @return [CommonAdapterBuilder]<[E]>
     */
    fun onBindItemView(
        @LayoutRes resId: Int,
        viewHolder: (itemView: View, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = onBindItemView(XmlLayoutHolderDelegate(resId), viewHolder)

    /**
     * Set the each item view on click events.
     * @param id specific a item ID to bind the on click event, default is [ITEM_NO_ID].
     * @param onClick callback and return each bound item function (the on click event callback).
     * @return [CommonAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onItemViewClick(id: Long = ITEM_NO_ID, onClick: (itemView: View, entity: E, position: Int) -> Unit) =
        apply { viewHolderOnClickCallbacks[id] = onClick }

    /**
     * Set the each item view on long click events.
     * @param id specific a item ID to bind the on long click event, default is [ITEM_NO_ID].
     * @param onLongClick callback and return each bound item function (the on long click event callback).
     * @return [CommonAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onItemViewLongClick(id: Long = ITEM_NO_ID, onLongClick: (itemView: View, entity: E, position: Int) -> Boolean) =
        apply { viewHolderOnLongClickCallbacks[id] = onLongClick }

    /**
     * Create and add view holder with [VB].
     *
     * - This function is deprecated, use [onBindItemView] instead.
     */
    @Deprecated(message = "Use onBindItemView instead.", ReplaceWith("onBindItemView<VB>(boundItemViews)"))
    inline fun <reified VB : ViewBinding> onBindViews(
        noinline boundItemViews: (binding: VB, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = onBindItemView<VB>(boundItemViews)

    /**
     * Create and add view holder from XML layout ID.
     *
     * - This function is deprecated, use [onBindItemView] instead.
     */
    @Deprecated(message = "Use onBindItemView instead.", ReplaceWith("onBindItemView(resId, boundItemViews)"))
    fun onBindViews(
        @LayoutRes resId: Int,
        boundItemViews: (view: View, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = onBindItemView(resId, boundItemViews)

    /**
     * Create and add view holder.
     *
     * - This solution was undesirable, so it was deprecated and no effect, don't use it.
     * @see ViewHolderDelegate
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "This function is unreasonable and has been deprecated, please use the custom ViewHolderDelegate solution instead.")
    fun onBindViews(view: View, boundItemViews: (view: View, entity: E, position: Int) -> Unit = { _, _, _ -> }) = this

    /**
     * Set the each item view on click events.
     *
     * - This function is deprecated, use [onItemViewClick] instead.
     */
    @Deprecated(message = "Use onItemViewClick instead.", ReplaceWith("onItemViewClick(id, onClick)"))
    @JvmOverloads
    fun onItemViewsClick(id: Long = ITEM_NO_ID, onClick: (view: View, entity: E, position: Int) -> Unit) =
        onItemViewClick(id, onClick)

    /**
     * Set the each item view on long click events.
     *
     * - This function is deprecated, use [onItemViewLongClick] instead.
     */
    @Deprecated(message = "Use onItemViewLongClick instead.", ReplaceWith("onItemViewLongClick(id, onLongClick)"))
    @JvmOverloads
    fun onItemViewsLongClick(id: Long = ITEM_NO_ID, onLongClick: (view: View, entity: E, position: Int) -> Boolean) =
        onItemViewLongClick(id, onLongClick)

    override fun build() = Instance()

    /**
     * The [CommonAdapterBuilder] instance.
     */
    inner class Instance internal constructor() : BaseAdapter(), Filterable {

        init {
            require(dataSetCount <= 0 || listDataCallback?.invoke().isNullOrEmpty()) {
                "You can only use once dataSetCount or entities on BaseAdapter."
            }
        }

        override fun getFilter() = filterCallback?.invoke() ?: emptyFilterCallback()
        override fun getCount() = dataSetCount.takeIf { it >= 0 } ?: listDataCallback?.invoke()?.size ?: 0
        override fun getItem(position: Int) = getCurrentEntity(position)
        override fun getItemId(position: Int) = getCurrentEntity(position)?.let { entityIdCallback?.invoke(it, position) } ?: position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val owner = parent as? AdapterView<*>?
            var itemView = convertView
            val viewHolder = if (convertView == null)
                boundViewHolderCallback?.delegate?.let {
                    CommonViewHolderImpl.from(it, adapterContext, parent).apply { 
                        itemView = this.rootView
                    }
                } ?: error("No ViewHolder found, are you sure you have created one using onBindItemView?")
            else convertView.tag as CommonViewHolderImpl<Any>
            itemView?.apply {
                tag = viewHolder
                viewHolderOnClickCallbacks
                    .filterKeys { it == ITEM_NO_ID || it == getItemId(position) }
                    .takeIf { it.isNotEmpty() }
                    ?.also { callbacks ->
                        /**
                         * Call the onClick event.
                         * @param position the current position.
                         */
                        fun View.doOnClick(position: Int) {
                            callbacks.forEach { (_, callback) ->
                                getCurrentEntity(position)?.let { entity ->
                                    callback(this, entity, position)
                                }
                            }
                        }
                        owner?.setOnItemClickListener { _, view, position, _ ->
                            view.doOnClick(position)
                        } ?: setOnClickListener { it.doOnClick(position) }
                    }
                viewHolderOnLongClickCallbacks
                    .filterKeys { it == ITEM_NO_ID || it == getItemId(position) }
                    .takeIf { it.isNotEmpty() }
                    ?.also { callbacks ->
                        /**
                         * Call the onLongClick event.
                         * @param position the current position.
                         * @return [Boolean]
                         */
                        fun View.doOnLongClick(position: Int): Boolean {
                            var result = false
                            callbacks.forEach { (_, callback) ->
                                getCurrentEntity(position)?.let { entity -> result = callback(this, entity, position) }
                            }; return result
                        }
                        owner?.setOnItemLongClickListener { _, view, position, _ ->
                            view.doOnLongClick(position)
                        } ?: setOnLongClickListener { it.doOnLongClick(position) }
                    }
            }
            getCurrentEntity(position)?.let {
                boundViewHolderCallback?.onBindCallback?.invoke(viewHolder.delegateInstance, it, position)
            }; return itemView ?: error("ViewHolder create failed.")
        }
    }
}
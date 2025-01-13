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
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.factory.bindAdapter
import com.highcapable.betterandroid.ui.component.adapter.view.RecyclerItemView
import com.highcapable.betterandroid.ui.extension.binding.ViewBinding
import com.highcapable.betterandroid.ui.extension.view.layoutInflater

/**
 * [RecyclerView.Adapter] builder, using entity [E].
 * @param adapterContext the current context.
 */
class RecyclerAdapterBuilder<E> private constructor(private val adapterContext: Context) : IAdapterBuilder {

    companion object {

        /** The default view type. */
        const val DEFAULT_VIEW_TYPE = 0

        /** The header view type. */
        private const val HEADER_VIEW_TYPE = -10

        /** The footer view type. */
        private const val FOOTER_VIEW_TYPE = -20

        /** The no ID's item ID. */
        private const val ITEM_NO_ID = -1L

        /**
         * Create a new [RecyclerAdapterBuilder]<[E]> from [context].
         * @see RecyclerView.bindAdapter
         * @see ViewPager2.bindAdapter
         * @param context the current context.
         * @return [RecyclerAdapterBuilder]<[E]>
         */
        @JvmStatic
        fun <E> from(context: Context) = RecyclerAdapterBuilder<E>(context)
    }

    /** The current each item function callbacks. */
    private val boundItemViewsCallbacks = linkedSetOf<RecyclerItemView<E>>()

    /** The current header item view callback. */
    private var boundHeaderItemViewCallback: RecyclerItemView<Any>? = null

    /** The current footer item view callback. */
    private var boundFooterItemViewCallback: RecyclerItemView<Any>? = null

    /** The current each item on click event callbacks. */
    private var itemViewsOnClickCallbacks = mutableMapOf<Any, (View, E, Int) -> Unit>()

    /** The current each item on long click event callbacks. */
    private var itemViewsOnLongClickCallbacks = mutableMapOf<Any, (View, E, Int) -> Boolean>()

    /** The current [List] data callback. */
    private var listDataCallback: (() -> List<E>)? = null

    /** The current entity ID callback. */
    private var entityIdCallback: ((E, Int) -> Long)? = null

    /** The current entity type callback. */
    private var entityTypeCallback: ((E, Int) -> Int)? = null

    /** Whether the adapter has a header view. */
    private val hasHeaderView get() = boundHeaderItemViewCallback != null

    /** Whether the adapter has a footer view. */
    private val hasFooterView get() = boundFooterItemViewCallback != null

    /**
     * Get the current position of the item view excluding the header view.
     * @receiver the current position.
     * @return [Int]
     */
    private fun Int.overflowPosition() = if (hasHeaderView) this - 1 else this

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
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun dataSetCount(dataSetCount: Int) = apply { this.dataSetCount = dataSetCount }

    /**
     * Bind [List] to [RecyclerView.Adapter].
     * @param result callback the data.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindData(result: (() -> List<E>)) = apply { listDataCallback = result }

    /**
     * Bind each item ID to [RecyclerView.Adapter].
     *
     * If not set will use current position as the ID.
     * @param entityId callback the each item ID function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindItemId(entityId: (entity: E, position: Int) -> Long) = apply { entityIdCallback = entityId }

    /**
     * Bind each view type to [RecyclerView.Adapter].
     * @param entityType callback the each view type function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindViewsType(entityType: (entity: E, position: Int) -> Int) = apply { entityTypeCallback = entityType }

    /**
     * Add and create view holder for a header view with [VB].
     *
     * A header item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one header view.
     * @see onBindFooterView
     * @see onBindViews
     * @param boundHeaderView callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the header view already exists.
     */
    @JvmOverloads
    @JvmName("onBindHeaderViewTyped")
    inline fun <reified VB : ViewBinding> onBindHeaderView(noinline boundHeaderView: (binding: VB) -> Unit = {}) = apply {
        require(!hasHeaderView) { "The header view already exists, you can only set one header view." }
        boundHeaderItemViewCallback = RecyclerItemView(ViewBinding<VB>(), viewType = HEADER_VIEW_TYPE) { binding, _, _, _ ->
            binding?.also { boundHeaderView(it as VB) }
        }
    }

    /**
     * Add and create view holder for a header view.
     *
     * A header item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one header view.
     * @see onBindFooterView
     * @see onBindViews
     * @param resId item view layout ID.
     * @param boundHeaderView callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the header view already exists.
     */
    @JvmOverloads
    fun onBindHeaderView(@LayoutRes resId: Int, boundHeaderView: (view: View) -> Unit = {}) = apply {
        require(!hasHeaderView) { "The header view already exists, you can only set one header view." }
        boundHeaderItemViewCallback = RecyclerItemView(rootViewResId = resId, viewType = HEADER_VIEW_TYPE) { _, rootView, _, _ ->
            rootView?.also { boundHeaderView(it) }
        }
    }

    /**
     * Add and create view holder for a header view.
     *
     * A header item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one header view.
     * @see onBindFooterView
     * @see onBindViews
     * @param view item [View], there must be no parent layout.
     * @param boundHeaderView callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the header view already exists.
     */
    @JvmOverloads
    fun onBindHeaderView(view: View, boundHeaderView: (view: View) -> Unit = {}) = apply {
        require(!hasHeaderView) { "The header view already exists, you can only set one header view." }
        boundHeaderItemViewCallback = RecyclerItemView(rootView = view, viewType = HEADER_VIEW_TYPE) { _, rootView, _, _ ->
            rootView?.also { boundHeaderView(it) }
        }
    }

    /**
     * Add and create view holder for a footer view with [VB].
     *
     * A footer item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one footer view.
     * @see onBindHeaderView
     * @see onBindViews
     * @param boundFooterView callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the footer view already exists.
     */
    @JvmOverloads
    @JvmName("onBindFooterViewTyped")
    inline fun <reified VB : ViewBinding> onBindFooterView(noinline boundFooterView: (binding: VB) -> Unit = {}) = apply {
        require(!hasFooterView) { "The footer view already exists, you can only set one footer view." }
        boundFooterItemViewCallback = RecyclerItemView(ViewBinding<VB>(), viewType = FOOTER_VIEW_TYPE) { binding, _, _, _ ->
            binding?.also { boundFooterView(it as VB) }
        }
    }

    /**
     * Add and create view holder for a footer view.
     *
     * A footer item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one footer view.
     * @see onBindHeaderView
     * @see onBindViews
     * @param resId item view layout ID.
     * @param boundFooterView callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the footer view already exists.
     */
    @JvmOverloads
    fun onBindFooterView(@LayoutRes resId: Int, boundFooterView: (view: View) -> Unit = {}) = apply {
        require(!hasFooterView) { "The footer view already exists, you can only set one footer view." }
        boundFooterItemViewCallback = RecyclerItemView(rootViewResId = resId, viewType = FOOTER_VIEW_TYPE) { _, rootView, _, _ ->
            rootView?.also { boundFooterView(it) }
        }
    }

    /**
     * Add and create view holder for a footer view.
     *
     * A footer item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one footer view.
     * @see onBindHeaderView
     * @see onBindViews
     * @param view item [View], there must be no parent layout.
     * @param boundFooterView callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the footer view already exists.
     */
    @JvmOverloads
    fun onBindFooterView(view: View, boundFooterView: (view: View) -> Unit = {}) = apply {
        require(!hasFooterView) { "The footer view already exists, you can only set one footer view." }
        boundFooterItemViewCallback = RecyclerItemView(rootView = view, viewType = FOOTER_VIEW_TYPE) { _, rootView, _, _ ->
            rootView?.also { boundFooterView(it) }
        }
    }

    /**
     * Add and create view holder with [VB].
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewsType] to specify the each position of view type.
     * @param boundItemViews callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    @JvmName("onBindViewsTyped")
    inline fun <reified VB : ViewBinding> onBindViews(
        viewType: Int = DEFAULT_VIEW_TYPE,
        noinline boundItemViews: (binding: VB, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = apply {
        boundItemViewsCallbacks.add(RecyclerItemView(ViewBinding<VB>(), viewType = viewType) { binding, _, entity, position ->
            binding?.also { boundItemViews(it as VB, entity, position) }
        })
    }

    /**
     * Add and create view holder.
     * @param resId item view layout ID.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewsType] to specify the each position of view type.
     * @param boundItemViews callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onBindViews(
        @LayoutRes resId: Int,
        viewType: Int = DEFAULT_VIEW_TYPE,
        boundItemViews: (view: View, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = apply {
        boundItemViewsCallbacks.add(RecyclerItemView(rootViewResId = resId, viewType = viewType) { _, rootView, entity, position ->
            rootView?.also { boundItemViews(it, entity, position) }
        })
    }

    /**
     * Add and create view holder.
     * @param view item [View], there must be no parent layout.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewsType] to specify the each position of view type.
     * @param boundItemViews callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onBindViews(
        view: View,
        viewType: Int = DEFAULT_VIEW_TYPE,
        boundItemViews: (view: View, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = apply {
        boundItemViewsCallbacks.add(RecyclerItemView(rootView = view, viewType = viewType) { _, rootView, entity, position ->
            rootView?.also { boundItemViews(it, entity, position) }
        })
    }

    /**
     * Set the each item view on click events.
     * @see onItemViewsLongClick
     * @param id specific a item ID to bind the on click event, only one of [id] and [viewType] can be used.
     * @param viewType specific a item view type to bind the on click event, only one of [id] and [viewType] can be used.
     * @param onClick callback and return each bound item function (the on click event callback).
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalArgumentException if [id] and [viewType] are used at the same time.
     */
    @JvmOverloads
    fun onItemViewsClick(id: Long? = null, viewType: Int? = null, onClick: (view: View, entity: E, position: Int) -> Unit) = apply {
        require(id == null || viewType == null) { "Only one of id and viewType can be used of onItemViewsClick." }
        itemViewsOnClickCallbacks[id ?: viewType ?: ITEM_NO_ID] = onClick
    }

    /**
     * Set the each item view on long click events.
     * @see onItemViewsClick
     * @param id specific a item ID to bind the on long click event, only one of [id] and [viewType] can be used.
     * @param viewType specific a item view type to bind the on long click event, only one of [id] and [viewType] can be used.
     * @param onLongClick callback and return each bound item function (the on long click event callback).
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalArgumentException if [id] and [viewType] are used at the same time.
     */
    @JvmOverloads
    fun onItemViewsLongClick(id: Long? = null, viewType: Int? = null, onLongClick: (view: View, entity: E, position: Int) -> Boolean) = apply {
        require(id == null || viewType == null) { "Only one of id and viewType can be used of onItemViewsLongClick." }
        itemViewsOnLongClickCallbacks[id ?: viewType ?: ITEM_NO_ID] = onLongClick
    }

    /**
     * Set the each item view on click events.
     *
     * - This function is deprecated and no effect, use [onItemViewsClick] instead.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use onItemViewsClick instead.")
    fun onItemViewsClick(onClick: (view: View, viewType: Int, entity: E, position: Int) -> Unit) = apply {}

    /**
     * Set the each item view on long click events.
     *
     * - This function is deprecated and no effect, use [onItemViewsLongClick] instead.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use onItemViewsLongClick instead.")
    fun onItemViewsLongClick(onLongClick: (view: View, viewType: Int, entity: E, position: Int) -> Boolean) = apply {}

    override fun build() = object : RecyclerView.Adapter<RecyclerAdapterBuilder<E>.BaseRecyclerHolder>() {

        /**
         * Get the current each item function callbacks.
         * @param viewType the current view type。
         * @return [RecyclerItemView]<[E]> or null.
         */
        private fun boundItemViewsCallbacks(viewType: Int) = when (viewType) {
            HEADER_VIEW_TYPE -> boundHeaderItemViewCallback
            FOOTER_VIEW_TYPE -> boundFooterItemViewCallback
            else -> boundItemViewsCallbacks.firstOrNull { it.viewType == viewType }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            boundItemViewsCallbacks(viewType)?.let {
                when {
                    it.bindingBuilder != null -> {
                        val binding = it.bindingBuilder.inflate(adapterContext.layoutInflater, parent)
                        BindingRecyclerHolder(binding, binding.root, viewType)
                    }
                    it.rootViewResId >= 0 -> CommonRecyclerHolder(adapterContext.layoutInflater.inflate(it.rootViewResId, parent), it.viewType)
                    it.rootView != null -> CommonRecyclerHolder(it.rootView.apply { layoutParams = parent.layoutParams }, it.viewType)
                    else -> null
                }
            } ?: error(
                "Cannot bound ViewHolder on RecyclerAdapter with type $viewType, " +
                    "did you forgot to called onBindViews or onBindViewsType function?"
            )

        override fun onBindViewHolder(holder: RecyclerAdapterBuilder<E>.BaseRecyclerHolder, position: Int) {
            when {
                hasHeaderView && position == 0 || hasFooterView && position == itemCount - 1 -> when (holder) {
                    is BindingRecyclerHolder ->
                        boundHeaderItemViewCallback?.onBindCallback?.invoke(holder.binding, holder.binding.root, Any(), position)
                    is CommonRecyclerHolder ->
                        boundHeaderItemViewCallback?.onBindCallback?.invoke(null, holder.rootView, Any(), position)
                }
                else -> {
                    val sPosition = position.overflowPosition()
                    boundItemViewsCallbacks.forEach {
                        if (it.viewType == holder.viewType) when (holder) {
                            is BindingRecyclerHolder ->
                                it.onBindCallback(holder.binding, holder.binding.root, getCurrentEntity(sPosition), sPosition)
                            is CommonRecyclerHolder ->
                                it.onBindCallback(null, holder.rootView, getCurrentEntity(sPosition), sPosition)
                        }
                    }
                    itemViewsOnClickCallbacks.filterKeys {
                        when (it) {
                            is Long -> it == ITEM_NO_ID || it == getItemId(sPosition)
                            is Int -> it == DEFAULT_VIEW_TYPE || it == holder.viewType
                            else -> false
                        }
                    }.also { callbacks ->
                        if (callbacks.isNotEmpty()) holder.rootView.setOnClickListener {
                            val ePosition = holder.adapterPosition.overflowPosition()
                            callbacks.forEach { (_, callback) -> callback(it, getCurrentEntity(ePosition), ePosition) }
                        }
                    }
                    itemViewsOnLongClickCallbacks.filterKeys {
                        when (it) {
                            is Long -> it == ITEM_NO_ID || it == getItemId(sPosition)
                            is Int -> it == DEFAULT_VIEW_TYPE || it == holder.viewType
                            else -> false
                        }
                    }.also { callbacks ->
                        if (callbacks.isNotEmpty()) holder.rootView.setOnLongClickListener {
                            val ePosition = holder.adapterPosition.overflowPosition()
                            var result = false
                            callbacks.forEach { (_, callback) ->
                                result = result || callback(it, getCurrentEntity(ePosition), ePosition)
                            }; result
                        }
                    }
                }
            }
        }

        override fun getItemViewType(position: Int) = when {
            hasHeaderView && position == 0 -> HEADER_VIEW_TYPE
            hasFooterView && position == itemCount - 1 -> FOOTER_VIEW_TYPE
            else -> entityTypeCallback?.invoke(getCurrentEntity(position.overflowPosition()), position.overflowPosition()) ?: DEFAULT_VIEW_TYPE
        }
        override fun getItemId(position: Int) = when {
            hasHeaderView && position == 0 -> HEADER_VIEW_TYPE.toLong()
            hasFooterView && position == itemCount - 1 -> FOOTER_VIEW_TYPE.toLong()
            else -> entityIdCallback?.invoke(getCurrentEntity(position.overflowPosition()), position.overflowPosition()) ?: position.toLong()
        }
        override fun getItemCount() = (dataSetCount.takeIf { it >= 0 } ?: listDataCallback?.invoke()?.size ?: 0) +
            (if (hasHeaderView) 1 else 0) + (if (hasFooterView) 1 else 0)
    }

    /**
     * [RecyclerView.ViewHolder] of [ViewBinding].
     * @param binding the [ViewBinding].
     * @param rootView the item view.
     * @param viewType the view type。
     */
    inner class BindingRecyclerHolder(val binding: ViewBinding, override val rootView: View, override val viewType: Int) :
        BaseRecyclerHolder(rootView, viewType)

    /**
     * Common [RecyclerView.ViewHolder].
     * @param rootView the item view.
     * @param viewType the view type。
     */
    inner class CommonRecyclerHolder(override val rootView: View, override val viewType: Int) : BaseRecyclerHolder(rootView, viewType)

    /**
     * Base [RecyclerView.ViewHolder].
     * @param rootView the item view.
     * @param viewType the view type。
     */
    abstract inner class BaseRecyclerHolder(open val rootView: View, open val viewType: Int) : RecyclerView.ViewHolder(rootView)
}
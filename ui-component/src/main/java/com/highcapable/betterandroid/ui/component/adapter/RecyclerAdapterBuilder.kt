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
@file:Suppress("unused", "LocalVariableName", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.highcapable.betterandroid.ui.component.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.entity.AdapterPosition
import com.highcapable.betterandroid.ui.component.adapter.factory.bindAdapter
import com.highcapable.betterandroid.ui.component.adapter.viewholder.RecyclerViewHolder
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.ViewBindingHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.XmlLayoutHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base.ViewHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.impl.RecyclerViewHolderImpl
import com.highcapable.betterandroid.ui.extension.binding.ViewBinding

/**
 * [RecyclerView.Adapter] builder, using entity [E].
 * @param adapterContext the current context.
 */
class RecyclerAdapterBuilder<E> private constructor(private val adapterContext: Context) : IAdapterBuilder {

    companion object {

        /** The default view type. */
        const val DEFAULT_VIEW_TYPE = 0

        /** The header view type. */
        private const val HEADER_VIEW_TYPE = -115 xor -225 xor -335

        /** The footer view type. */
        private const val FOOTER_VIEW_TYPE = -555 xor -665 xor -775

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
    private val boundViewHolderCallbacks = linkedSetOf<RecyclerViewHolder<E>>()

    /** The current each item on click event callbacks. */
    private var viewHolderOnClickCallbacks = mutableMapOf<Number, (View, E, Int) -> Unit>()

    /** The current each item on long click event callbacks. */
    private var viewHolderOnLongClickCallbacks = mutableMapOf<Number, (View, E, Int) -> Boolean>()

    /** The current [List] data callback. */
    private var listDataCallback: (() -> List<E>)? = null

    /** The current entity ID callback. */
    private var entityIdCallback: ((E, Int) -> Long)? = null

    /** The current entity type callback. */
    private var entityTypeCallback: ((E, Int) -> Int)? = null

    /** Whether the adapter has a header view. */
    private val hasHeaderView get() = boundViewHolderCallbacks.any { it.viewType == HEADER_VIEW_TYPE }

    /** Whether the adapter has a footer view. */
    private val hasFooterView get() = boundViewHolderCallbacks.any { it.viewType == FOOTER_VIEW_TYPE }

    /**
     * Get the current position of the item view excluding the header view.
     * @receiver the current position.
     * @return [Int]
     */
    private fun Int.excludingPosition() = if (hasHeaderView) this - 1 else this

    /**
     * Get the current position of the item view including the header view.
     * @receiver the current position.
     * @return [Int]
     */
    private fun Int.includingPosition() = if (hasHeaderView) this + 1 else this

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
    fun onBindViewType(entityType: (entity: E, position: Int) -> Int) = apply { entityTypeCallback = entityType }

    /**
     * Create and add view holder from [ViewHolderDelegate]<[VD]> (Nullable).
     * @param delegate the custom item view delegate.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewType] to specify the each position of view type.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    private fun <VD : Any> onBindNullableItemView(
        delegate: ViewHolderDelegate<VD>,
        viewType: Int = DEFAULT_VIEW_TYPE,
        viewHolder: (delegate: VD, entity: E?, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = apply {
        when (viewType) {
            HEADER_VIEW_TYPE -> require(!hasHeaderView) { "The header view already exists, you can only set one header view." }
            FOOTER_VIEW_TYPE -> require(!hasFooterView) { "The footer view already exists, you can only set one footer view." }
        }
        boundViewHolderCallbacks.add(RecyclerViewHolder(delegate as ViewHolderDelegate<Any>, viewType) { delegate, entity, position ->
            runCatching {
                viewHolder(delegate as VD, entity, position)
            }.onFailure {
                if (it is ClassCastException) error(
                    "The correct entity type is not provided with onBindData. " +
                        "Please remove the generic declaration or use onBindData to pass in the corresponding entity type collection."
                ) else throw it
            }
        })
    }

    /**
     * Create and add view holder from [ViewHolderDelegate]<[VD]>.
     * @param delegate the custom item view delegate.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewType] to specify the each position of view type.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun <VD : Any> onBindItemView(
        delegate: ViewHolderDelegate<VD>,
        viewType: Int = DEFAULT_VIEW_TYPE,
        viewHolder: (delegate: VD, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = onBindNullableItemView(delegate, viewType) { _delegate, entity, position ->
        viewHolder(_delegate, entity ?: error("An existing binding object cannot be passed into a null entity."), position)
    }

    /**
     * Create and add view holder from [ViewHolderDelegate]<[VD]> for a header view.
     *
     * A header item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one header view.
     * @see onBindFooterView
     * @see onBindItemView
     * @param delegate the custom item view delegate.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the header view already exists.
     */
    @JvmOverloads
    fun <VD : Any> onBindHeaderView(
        delegate: ViewHolderDelegate<VD>,
        viewHolder: (delegate: VD) -> Unit = {}
    ) = onBindNullableItemView(delegate, HEADER_VIEW_TYPE) { _delegate, _, _ -> viewHolder(_delegate) }

    /**
     * Create and add view holder from [ViewHolderDelegate]<[VD]> for a footer view.
     *
     * A footer item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one footer view.
     * @see onBindHeaderView
     * @see onBindItemView
     * @param delegate the custom item view delegate.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the footer view already exists.
     */
    @JvmOverloads
    fun <VD : Any> onBindFooterView(
        delegate: ViewHolderDelegate<VD>,
        viewHolder: (delegate: VD) -> Unit = {}
    ) = onBindNullableItemView(delegate, FOOTER_VIEW_TYPE) { _delegate, _, _ -> viewHolder(_delegate) }

    /**
     * Create and add view holder from [ViewBinding]<[VB]> for a header view.
     *
     * A header item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one header view.
     * @see onBindFooterView
     * @see onBindItemView
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the header view already exists.
     */
    inline fun <reified VB : ViewBinding> onBindHeaderView(noinline viewHolder: (binding: VB) -> Unit = {}) =
        onBindHeaderView(ViewBindingHolderDelegate(ViewBinding<VB>()), viewHolder)

    /**
     * Create and add view holder from XML layout ID for a header view.
     *
     * A header item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one header view.
     * @see onBindFooterView
     * @see onBindItemView
     * @param resId item view layout ID.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the header view already exists.
     */
    @JvmOverloads
    fun onBindHeaderView(@LayoutRes resId: Int, viewHolder: (view: View) -> Unit = {}) =
        onBindHeaderView(XmlLayoutHolderDelegate(resId), viewHolder)

    /**
     * Create and add view holder from [ViewBinding]<[VB]> for a footer view.
     *
     * A footer item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one footer view.
     * @see onBindHeaderView
     * @see onBindItemView
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the footer view already exists.
     */
    inline fun <reified VB : ViewBinding> onBindFooterView(noinline viewHolder: (binding: VB) -> Unit = {}) =
        onBindFooterView(ViewBindingHolderDelegate(ViewBinding<VB>()), viewHolder)

    /**
     * Create and add view holder from XML layout ID for a footer view.
     *
     * A footer item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one footer view.
     * @see onBindHeaderView
     * @see onBindItemView
     * @param resId item view layout ID.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the footer view already exists.
     */
    @JvmOverloads
    fun onBindFooterView(@LayoutRes resId: Int, viewHolder: (view: View) -> Unit = {}) =
        onBindFooterView(XmlLayoutHolderDelegate(resId), viewHolder)

    /**
     * Create and add view holder from [ViewBinding]<[VB]>.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewType] to specify the each position of view type.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    inline fun <reified VB : ViewBinding> onBindItemView(
        viewType: Int = DEFAULT_VIEW_TYPE,
        noinline viewHolder: (binding: VB, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = onBindItemView(ViewBindingHolderDelegate(ViewBinding<VB>()), viewType, viewHolder)

    /**
     * Create and add view holder from XML layout ID.
     * @param resId item view layout ID.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewType] to specify the each position of view type.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindItemView(
        @LayoutRes resId: Int,
        viewType: Int = DEFAULT_VIEW_TYPE,
        viewHolder: (itemView: View, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = onBindItemView(XmlLayoutHolderDelegate(resId), viewType, viewHolder)

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
    fun onItemViewClick(
        id: Long? = null,
        viewType: Int? = null,
        onClick: (itemView: View, entity: E, position: Int) -> Unit
    ) = apply {
        require(id == null || viewType == null) { "Only one of id and viewType can be used of onItemViewClick." }
        viewHolderOnClickCallbacks[id ?: viewType ?: ITEM_NO_ID] = onClick
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
    fun onItemViewLongClick(
        id: Long? = null,
        viewType: Int? = null,
        onLongClick: (itemView: View, entity: E, position: Int) -> Boolean
    ) = apply {
        require(id == null || viewType == null) { "Only one of id and viewType can be used of onItemViewLongClick." }
        viewHolderOnLongClickCallbacks[id ?: viewType ?: ITEM_NO_ID] = onLongClick
    }

    /**
     * Bind each view type to [RecyclerView.Adapter].
     *
     * - This function is deprecated, use [onBindViewType] instead.
     */
    @Deprecated(message = "Use onBindViewType instead.", ReplaceWith("onBindViewType(entityType)"))
    fun onBindViewsType(entityType: (entity: E, position: Int) -> Int) = onBindViewType(entityType)

    /**
     * Create and add view holder from [View] for a header view.
     *
     * - This solution was undesirable, so it was deprecated and no effect, don't use it.
     * @see ViewHolderDelegate
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "This function is unreasonable and has been deprecated, please use the custom ViewHolderDelegate solution instead.")
    @JvmOverloads
    fun onBindHeaderView(view: View, boundHeaderView: (view: View) -> Unit = {}) = this

    /**
     * Create and add view holder from [View] for a footer view.
     *
     * - This solution was undesirable, so it was deprecated and no effect, don't use it.
     * @see ViewHolderDelegate
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "This function is unreasonable and has been deprecated, please use the custom ViewHolderDelegate solution instead.")
    @JvmOverloads
    fun onBindFooterView(view: View, boundFooterView: (view: View) -> Unit = {}) = this

    /**
     * Create and add view holder from [ViewBinding]<[VB]>.
     *
     * - This function is deprecated, use [onBindItemView] instead.
     */
    @Deprecated(message = "Use onBindItemView instead.", ReplaceWith("onBindItemView<VB>(viewType, boundItemViews)"))
    inline fun <reified VB : ViewBinding> onBindViews(
        viewType: Int = DEFAULT_VIEW_TYPE,
        noinline boundItemViews: (binding: VB, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = onBindItemView<VB>(viewType, boundItemViews)

    /**
     * Create and add view holder from XML layout ID.
     *
     * - This function is deprecated, use [onBindItemView] instead.
     */
    @Deprecated(message = "Use onBindItemView instead.", ReplaceWith("onBindItemView(resId, viewType, boundItemViews)"))
    @JvmOverloads
    fun onBindViews(
        @LayoutRes resId: Int,
        viewType: Int = DEFAULT_VIEW_TYPE,
        boundItemViews: (view: View, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = onBindItemView(resId, viewType, boundItemViews)

    /**
     * Create and add view holder from [View].
     *
     * - This solution was undesirable, so it was deprecated and no effect, don't use it.
     * @see ViewHolderDelegate
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "This function is unreasonable and has been deprecated, please use the custom ViewHolderDelegate solution instead.")
    @JvmOverloads
    fun onBindViews(
        view: View,
        viewType: Int = DEFAULT_VIEW_TYPE,
        boundItemViews: (view: View, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = this

    /**
     * Set the each item view on click events.
     *
     * - This function is deprecated, use [onItemViewClick] instead.
     */
    @Deprecated(message = "Use onItemViewClick instead.", ReplaceWith("onItemViewClick(id, viewType, onClick)"))
    @JvmOverloads
    fun onItemViewsClick(
        id: Long? = null,
        viewType: Int? = null,
        onClick: (view: View, entity: E, position: Int) -> Unit
    ) = onItemViewClick(id, viewType, onClick)

    /**
     * Set the each item view on long click events.
     *
     * - This function is deprecated, use [onItemViewLongClick] instead.
     */
    @Deprecated(message = "Use onItemViewLongClick instead.", ReplaceWith("onItemViewLongClick(id, viewType, onLongClick)"))
    @JvmOverloads
    fun onItemViewsLongClick(
        id: Long? = null,
        viewType: Int? = null,
        onLongClick: (view: View, entity: E, position: Int) -> Boolean
    ) = onItemViewLongClick(id, viewType, onLongClick)

    /**
     * Set the each item view on click events.
     *
     * - This function is deprecated and no effect, use [onItemViewClick] instead.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use onItemViewClick instead.")
    fun onItemViewsClick(onClick: (view: View, viewType: Int, entity: E, position: Int) -> Unit) = this

    /**
     * Set the each item view on long click events.
     *
     * - This function is deprecated and no effect, use [onItemViewLongClick] instead.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use onItemViewLongClick instead.")
    fun onItemViewsLongClick(onLongClick: (view: View, viewType: Int, entity: E, position: Int) -> Boolean) = this

    override fun build() = Instance()

    /**
     * The [RecyclerAdapterBuilder] instance.
     */
    inner class Instance internal constructor() : RecyclerView.Adapter<RecyclerViewHolderImpl<Any>>() {

        /**
         * The adapter wrapper of [RecyclerAdapterBuilder].
         *
         * This is an extension to [RecyclerView.Adapter] to support header and footer layouts.
         */
        inner class Wrapper internal constructor() {

            /** The current instance of [RecyclerAdapterBuilder.Instance]. */
            private val instance = this@Instance

            /**
             * Convert the current position of the item view excluding the header view.
             * param position the current position.
             * @return [Int]
             */
            internal fun excludingPosition(position: Int) = position.excludingPosition()

            /**
             * Convert the current position of the item view including the header view.
             * param position the current position.
             * @return [Int]
             */
            internal fun includingPosition(position: Int) = position.includingPosition()

            /**
             * Whether the adapter has a header view.
             * @see RecyclerAdapterBuilder.hasHeaderView
             * @return [Boolean]
             */
            val hasHeaderView get() = this@RecyclerAdapterBuilder.hasHeaderView

            /**
             * Whether the adapter has a footer view.
             * @see RecyclerAdapterBuilder.hasFooterView
             * @return [Boolean]
             */
            val hasFooterView get() = this@RecyclerAdapterBuilder.hasFooterView

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
                if (hasFooterView) instance.notifyItemChanged(itemCount - 1)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemChanged
             */
            fun notifyItemChanged(position: Int) {
                val current = position.includingPosition()
                instance.notifyItemChanged(current)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemChanged
             */
            fun notifyItemChanged(position: Int, payload: Any?) {
                val current = position.includingPosition()
                instance.notifyItemChanged(current, payload)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemRangeChanged
             */
            fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
                val start = positionStart.includingPosition()
                val count = itemCount.includingPosition()
                instance.notifyItemRangeChanged(start, count)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemRangeChanged
             */
            fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                val start = positionStart.includingPosition()
                val count = itemCount.includingPosition()
                instance.notifyItemRangeChanged(start, count, payload)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemInserted
             */
            fun notifyItemInserted(position: Int) {
                val current = position.includingPosition()
                instance.notifyItemInserted(current)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemMoved
             */
            fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
                val from = fromPosition.includingPosition()
                val to = toPosition.includingPosition()
                instance.notifyItemMoved(from, to)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemRangeInserted
             */
            fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
                val start = positionStart.includingPosition()
                val count = itemCount.includingPosition()
                instance.notifyItemRangeInserted(start, count)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemRemoved
             */
            fun notifyItemRemoved(position: Int) {
                val current = position.includingPosition()
                instance.notifyItemRemoved(current)
            }

            /**
             * @see RecyclerView.Adapter.notifyItemRangeRemoved
             */
            fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
                val start = positionStart.includingPosition()
                val count = itemCount.includingPosition()
                instance.notifyItemRangeRemoved(start, count)
            }
        }

        init {
            require(dataSetCount <= 0 || listDataCallback?.invoke().isNullOrEmpty()) {
                "You can only use once dataSetCount or entities on RecyclerView.Adapter."
            }
        }

        /**
         * Get the wrapper of [RecyclerView.Adapter].
         * @return [Wrapper]
         */
        val wrapper = Wrapper()

        /**
         * Whether the current position is in the header view holder.
         * @param position the current position.
         * @return [Boolean]
         */
        private fun isInHeaderViewHolder(position: Int) = hasHeaderView && position == 0

        /**
         * Whether the current position is in the footer view holder.
         * @param position the current position.
         * @return [Boolean]
         */
        private fun isInFooterViewHolder(position: Int) = hasFooterView && position == itemCount - 1

        /**
         * Get the current each item function callbacks.
         * @param viewType the current view type。
         * @return [RecyclerViewHolder]<[E]> or null.
         */
        private fun getCallback(viewType: Int) = boundViewHolderCallbacks.firstOrNull { it.viewType == viewType }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            getCallback(viewType)?.delegate?.let {
                RecyclerViewHolderImpl.from(it, viewType, adapterContext, parent)
            } ?: error(when {
                boundViewHolderCallbacks.isNotEmpty() && viewType == DEFAULT_VIEW_TYPE ->
                    "No default view type $DEFAULT_VIEW_TYPE found, " +
                        "you need to declare a default view type $DEFAULT_VIEW_TYPE or used onBindViewType to create a processor."
                boundViewHolderCallbacks.isNotEmpty() ->
                    "No ViewHolder with type $viewType found, " +
                        "are you sure you used onBindViewType to create a processor that uses type $viewType " +
                        "or created one using onBindItemView by type $viewType?"
                else -> "No ViewHolder found, are you sure you have created one using onBindItemView?"
            })

        override fun onBindViewHolder(holder: RecyclerViewHolderImpl<Any>, position: Int) {
            val isInHeaderOrFooter = isInHeaderViewHolder(position) || isInFooterViewHolder(position)
            val staticPosition = position.excludingPosition()
            val dynamicPosition = AdapterPosition.from(
                layout = { holder.layoutPosition.excludingPosition() },
                binding = { holder.bindingAdapterPosition.excludingPosition() },
                absolute = { holder.absoluteAdapterPosition.excludingPosition() }
            )
            boundViewHolderCallbacks.filter { it.viewType == holder.viewType }.forEach {
                // If it is in the header or footer view holder,
                // use the null value as the placeholder for entity, and null is not allowed in other cases.
                val entity = if (isInHeaderOrFooter) null else getCurrentEntity(staticPosition) ?: return
                it.onBindCallback(holder.delegateInstance, entity, dynamicPosition)
            }
            // Header and footer view does not handle item view click events.
            if (isInHeaderOrFooter) return
            viewHolderOnClickCallbacks.filterKeys {
                when (it) {
                    is Long -> it == ITEM_NO_ID || it == getItemId(staticPosition)
                    is Int -> it == DEFAULT_VIEW_TYPE || it == holder.viewType
                    else -> false
                }
            }.takeIf { it.isNotEmpty() }?.also { callbacks ->
                holder.itemView.setOnClickListener {
                    callbacks.forEach { (_, callback) ->
                        val value = dynamicPosition.value
                        getCurrentEntity(value)?.let { entity -> callback(it, entity, value) }
                    }
                }
            }
            viewHolderOnLongClickCallbacks.filterKeys {
                when (it) {
                    is Long -> it == ITEM_NO_ID || it == getItemId(staticPosition)
                    is Int -> it == DEFAULT_VIEW_TYPE || it == holder.viewType
                    else -> false
                }
            }.takeIf { it.isNotEmpty() }?.also { callbacks ->
                holder.rootView.setOnLongClickListener {
                    var result = false
                    callbacks.forEach { (_, callback) ->
                        val value = dynamicPosition.value
                        result = result || getCurrentEntity(value)?.let { entity ->
                            callback(it, entity, value)
                        } == true
                    }; result
                }
            }
        }

        override fun getItemViewType(position: Int) = when {
            isInHeaderViewHolder(position) -> HEADER_VIEW_TYPE
            isInFooterViewHolder(position) -> FOOTER_VIEW_TYPE
            else -> getCurrentEntity(position.excludingPosition())?.let {
                entityTypeCallback?.invoke(it, position.excludingPosition())
            } ?: DEFAULT_VIEW_TYPE
        }

        override fun getItemId(position: Int) = when {
            isInHeaderViewHolder(position) -> HEADER_VIEW_TYPE.toLong()
            isInFooterViewHolder(position) -> FOOTER_VIEW_TYPE.toLong()
            else -> getCurrentEntity(position.excludingPosition())?.let {
                entityIdCallback?.invoke(it, position.excludingPosition())
            } ?: position.toLong()
        }

        override fun getItemCount(): Int {
            val primaryCount = dataSetCount.takeIf { it >= 0 } ?: listDataCallback?.invoke()?.size ?: 0
            val headerCount = if (hasHeaderView) 1 else 0
            val footerCount = if (hasFooterView) 1 else 0
            return primaryCount + headerCount + footerCount
        }
    }
}
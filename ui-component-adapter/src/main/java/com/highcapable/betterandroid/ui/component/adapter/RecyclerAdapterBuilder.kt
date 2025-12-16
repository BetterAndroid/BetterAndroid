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
@file:Suppress("unused", "LocalVariableName", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package com.highcapable.betterandroid.ui.component.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.highcapable.betterandroid.ui.component.adapter.RecyclerAdapterBuilder.Companion.DEFAULT_VIEW_TYPE
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.entity.AdapterPosition
import com.highcapable.betterandroid.ui.component.adapter.factory.bindAdapter
import com.highcapable.betterandroid.ui.component.adapter.recycler.wrapper.RecyclerAdapterWrapper
import com.highcapable.betterandroid.ui.component.adapter.viewholder.RecyclerViewHolder
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.ViewBindingHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.XmlLayoutHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base.ViewHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.impl.RecyclerViewHolderImpl
import com.highcapable.betterandroid.ui.extension.binding.ViewBinding
import com.highcapable.betterandroid.ui.extension.binding.ViewBindingBuilder

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
    @get:JvmSynthetic
    internal val hasHeaderView get() = boundViewHolderCallbacks.any { it.viewType == HEADER_VIEW_TYPE }

    /** Whether the adapter has a footer view. */
    @get:JvmSynthetic
    internal val hasFooterView get() = boundViewHolderCallbacks.any { it.viewType == FOOTER_VIEW_TYPE }

    /**
     * Get the current position of the item view excluding the header view.
     * @param position the current position.
     * @return [Int]
     */
    @JvmSynthetic
    internal fun excludingPosition(position: Int) = if (hasHeaderView) position - 1 else position

    /**
     * Get the current position of the item view including the header view.
     * @param position the current position.
     * @return [Int]
     */
    @JvmSynthetic
    internal fun includingPosition(position: Int) = if (hasHeaderView) position + 1 else position

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
        onBindHeaderView(ViewBinding<VB>(), viewHolder)

    /**
     * Create and add view holder from [ViewBinding]<[VB]> for a header view.
     *
     * A header item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one header view.
     * @see onBindFooterView
     * @see onBindItemView
     * @param bindingBuilder the view binding builder.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the header view already exists.
     */
    @JvmOverloads
    fun <VB : ViewBinding> onBindHeaderView(bindingBuilder: ViewBindingBuilder<VB>, viewHolder: (binding: VB) -> Unit = {}) =
        onBindHeaderView(ViewBindingHolderDelegate(bindingBuilder), viewHolder)

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
        onBindFooterView(ViewBinding<VB>(), viewHolder)

    /**
     * Create and add view holder from [ViewBinding]<[VB]> for a footer view.
     *
     * A footer item view is a special item view that is not included in the [dataSetCount] or [onBindData].
     *
     * - Note: You can only set one footer view.
     * @see onBindHeaderView
     * @see onBindItemView
     * @param bindingBuilder the view binding builder.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     * @throws IllegalStateException if the footer view already exists.
     */
    @JvmOverloads
    fun <VB : ViewBinding> onBindFooterView(bindingBuilder: ViewBindingBuilder<VB>, viewHolder: (binding: VB) -> Unit = {}) =
        onBindFooterView(ViewBindingHolderDelegate(bindingBuilder), viewHolder)

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
    ) = onBindItemView(ViewBinding<VB>(), viewType, viewHolder)

    /**
     * Create and add view holder from [ViewBinding]<[VB]>.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewType] to specify the each position of view type.
     * @param bindingBuilder the view binding builder.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun <VB : ViewBinding> onBindItemView(
        bindingBuilder: ViewBindingBuilder<VB>,
        viewType: Int = DEFAULT_VIEW_TYPE,
        viewHolder: (binding: VB, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = onBindItemView(ViewBindingHolderDelegate(bindingBuilder), viewType, viewHolder)

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
     * @see onItemViewLongClick
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
     * @see onItemViewClick
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

    override fun build() = Instance()

    /**
     * The [RecyclerAdapterBuilder] instance.
     */
    inner class Instance internal constructor() : RecyclerView.Adapter<RecyclerViewHolderImpl<Any>>() {

        init {
            require(dataSetCount <= 0 || listDataCallback?.invoke().isNullOrEmpty()) {
                "You can only use once dataSetCount or entities on RecyclerView.Adapter."
            }
        }

        /**
         * Get the wrapper of [RecyclerView.Adapter].
         * @return [RecyclerAdapterWrapper]
         */
        val wrapper = RecyclerAdapterWrapper(this@RecyclerAdapterBuilder, this)

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

            val staticPosition = excludingPosition(position)
            val dynamicPosition = AdapterPosition.from(
                layout = { excludingPosition(holder.layoutPosition) },
                binding = { excludingPosition(holder.bindingAdapterPosition) },
                absolute = { excludingPosition(holder.absoluteAdapterPosition) }
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
                    }

                    result
                }
            }
        }

        override fun getItemViewType(position: Int) = when {
            isInHeaderViewHolder(position) -> HEADER_VIEW_TYPE
            isInFooterViewHolder(position) -> FOOTER_VIEW_TYPE
            else -> getCurrentEntity(excludingPosition(position))?.let {
                entityTypeCallback?.invoke(it, excludingPosition(position))
            } ?: DEFAULT_VIEW_TYPE
        }

        override fun getItemId(position: Int) = when {
            isInHeaderViewHolder(position) -> HEADER_VIEW_TYPE.toLong()
            isInFooterViewHolder(position) -> FOOTER_VIEW_TYPE.toLong()
            else -> getCurrentEntity(excludingPosition(position))?.let {
                entityIdCallback?.invoke(it, excludingPosition(position))
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
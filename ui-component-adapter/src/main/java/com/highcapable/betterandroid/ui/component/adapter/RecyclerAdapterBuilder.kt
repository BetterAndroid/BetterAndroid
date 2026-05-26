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
import com.highcapable.betterandroid.ui.component.adapter.recycler.diff.RecyclerAsyncDiffer
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

    /** The current diff callback. */
    private var diffCallback: RecyclerAsyncDiffer.Callback<E>? = null

    /** The empty list shortcut to reduce repeated allocations. */
    private val emptyDataSet = emptyList<E>()

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
     * @return [E] or null.
     */
    private fun currentDataSet() = listDataCallback?.invoke() ?: emptyDataSet

    /**
     * Get the entity [E].
     * @param dataSet the current data set snapshot.
     * @param position the current position.
     * @return [E] or null.
     */
    private fun getCurrentEntity(dataSet: List<E>, position: Int) = dataSet.let {
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
        set(value) {
            require(diffCallback == null) { "dataSetCount cannot be used with onBindDiffer." }
            field = value
        }

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
    fun dataSetCount(dataSetCount: Int) = apply {
        require(diffCallback == null) { "dataSetCount cannot be used with onBindDiffer." }
        this.dataSetCount = dataSetCount
    }

    /**
     * Bind [List] to [RecyclerView.Adapter].
     * @param result callback the data.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindData(result: (() -> List<E>)) = apply {
        require(diffCallback == null) { "onBindData cannot be used with onBindDiffer." }
        listDataCallback = result
    }

    /**
     * Bind the async list differ to [RecyclerView.Adapter].
     * @param areItemsTheSame compare whether the two items represent the same object.
     * @param areContentsTheSame compare whether the contents of the two items are the same.
     * @param getChangePayload get the changed payload between two items, default is null.
     * @param detectMoves whether to detect moved items, default true.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onBindDiffer(
        areItemsTheSame: (oldItem: E, newItem: E) -> Boolean,
        areContentsTheSame: (oldItem: E, newItem: E) -> Boolean,
        getChangePayload: (oldItem: E, newItem: E) -> Any? = { _, _ -> null },
        detectMoves: Boolean = true
    ) = onBindDiffer(
        RecyclerAsyncDiffer.Callback(
            areItemsTheSame = areItemsTheSame,
            areContentsTheSame = areContentsTheSame,
            getChangePayload = getChangePayload,
            detectMoves = detectMoves
        )
    )

    /**
     * Bind the async list differ to [RecyclerView.Adapter].
     * @param callback the diff callback.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindDiffer(callback: RecyclerAsyncDiffer.Callback<E>) = apply {
        require(listDataCallback == null) { "onBindDiffer cannot be used with onBindData." }
        require(dataSetCount < 0) { "onBindDiffer cannot be used with dataSetCount." }

        diffCallback = callback
    }

    /**
     * Bind each item ID to [RecyclerView.Adapter].
     *
     * If not set will use current position as the ID.
     * @param entityId callback the item ID function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindItemId(entityId: (entity: E, position: Int) -> Long) = apply { entityIdCallback = entityId }

    /**
     * Bind each view type to [RecyclerView.Adapter].
     * @param entityType callback the view type function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindViewType(entityType: (entity: E, position: Int) -> Int) = apply { entityTypeCallback = entityType }

    /**
     * Create and add view holder from [ViewHolderDelegate]<[VD]> (Nullable).
     * @param delegate the custom item view delegate.
     * @param viewType the view type, default is [DEFAULT_VIEW_TYPE],
     * you must use [onBindViewType] to specify the position of view type.
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
     * you must use [onBindViewType] to specify the position of view type.
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
     * you must use [onBindViewType] to specify the position of view type.
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
     * you must use [onBindViewType] to specify the position of view type.
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
     * you must use [onBindViewType] to specify the position of view type.
     * @param viewHolder callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindItemView(
        @LayoutRes resId: Int,
        viewType: Int = DEFAULT_VIEW_TYPE,
        viewHolder: (itemView: View, entity: E, position: AdapterPosition) -> Unit = { _, _, _ -> }
    ) = onBindItemView(XmlLayoutHolderDelegate(resId), viewType, viewHolder)

    /**
     * Set the item view on click events.
     * @see onItemViewLongClick
     * @param id specific an item ID to bind the on click event, only one of [id] and [viewType] can be used.
     * @param viewType specific an item view type to bind the on click event, only one of [id] and [viewType] can be used.
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
     * Set the item view on long click events.
     * @see onItemViewClick
     * @param id specific an item ID to bind the on long click event, only one of [id] and [viewType] can be used.
     * @param viewType specific an item view type to bind the on long click event, only one of [id] and [viewType] can be used.
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

        /** The current view type callback cache. */
        private val boundViewHolderCallbacksByViewType = boundViewHolderCallbacks.groupBy { it.viewType }

        init {
            require(dataSetCount <= 0 || currentDataSet().isEmpty()) {
                "You can only use once dataSetCount or entities on RecyclerView.Adapter."
            }
            setHasStableIds(entityIdCallback != null)
        }

        /** The differ. */
        val differ = diffCallback?.let { RecyclerAsyncDiffer.from(this, emptyDataSet, it) }

        /**
         * Get the wrapper of [RecyclerView.Adapter].
         * @return [RecyclerAdapterWrapper]
         */
        val wrapper = RecyclerAdapterWrapper(this@RecyclerAdapterBuilder, this)

        /**
         * Submit the current list.
         * @see RecyclerAsyncDiffer.submitList
         * @param list the current list.
         * @param commitCallback the commit callback.
         */
        @JvmOverloads
        fun submitList(list: List<E>?, commitCallback: Runnable? = null) {
            requireNotNull(differ) { "You can only use submitList when onBindDiffer is configured." }
            differ.submitList(list, commitCallback)
        }

        /**
         * Submit the current list without generic checks.
         * @param list the current list.
         * @param commitCallback the commit callback, default is null.
         */
        internal fun submitListUnchecked(list: List<*>?, commitCallback: Runnable? = null) =
            submitList(list as List<E>?, commitCallback)

        /**
         * Get the current data set snapshot.
         * @return [List]<[E]>
         */
        private fun currentDataSet() = differ?.currentList ?: this@RecyclerAdapterBuilder.currentDataSet()

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
        private fun getCallback(viewType: Int) = boundViewHolderCallbacksByViewType[viewType]?.firstOrNull()

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
            val dataSet = currentDataSet()
            val currentEntity = if (isInHeaderOrFooter) null else getCurrentEntity(dataSet, staticPosition)
            val dynamicPosition = AdapterPosition.from(
                layout = { excludingPosition(holder.layoutPosition) },
                binding = { excludingPosition(holder.bindingAdapterPosition) },
                absolute = { excludingPosition(holder.absoluteAdapterPosition) }
            )
            val callbacks = boundViewHolderCallbacksByViewType[holder.viewType].orEmpty()

            callbacks.forEach {
                val entity = currentEntity ?: return
                it.onBindCallback(holder.delegateInstance, entity, dynamicPosition)
            }

            // Header and footer view does not handle item view click events.
            if (isInHeaderOrFooter) return

            if (viewHolderOnClickCallbacks.isNotEmpty()) holder.itemView.setOnClickListener {
                val value = dynamicPosition.value
                val latestDataSet = currentDataSet()
                getCurrentEntity(latestDataSet, value)?.let { entity ->
                    val currentItemId = entityIdCallback?.invoke(entity, value) ?: value.toLong()

                    viewHolderOnClickCallbacks.forEach { (key, callback) ->
                        val isMatch = when (key) {
                            is Long -> key == ITEM_NO_ID || key == currentItemId
                            is Int -> key == DEFAULT_VIEW_TYPE || key == holder.viewType
                            else -> false
                        }

                        if (isMatch) callback(it, entity, value)
                    }
                }
            } else holder.itemView.setOnClickListener(null)

            if (viewHolderOnLongClickCallbacks.isNotEmpty()) holder.rootView.setOnLongClickListener {
                val value = dynamicPosition.value
                var result = false

                val latestDataSet = currentDataSet()
                getCurrentEntity(latestDataSet, value)?.let { entity ->
                    val currentItemId = entityIdCallback?.invoke(entity, value) ?: value.toLong()

                    viewHolderOnLongClickCallbacks.forEach { (key, callback) ->
                        val isMatch = when (key) {
                            is Long -> key == ITEM_NO_ID || key == currentItemId
                            is Int -> key == DEFAULT_VIEW_TYPE || key == holder.viewType
                            else -> false
                        }

                        if (isMatch) result = callback(it, entity, value) || result
                    }
                }

                result
            } else holder.rootView.setOnLongClickListener(null)
        }

        override fun getItemViewType(position: Int) = when {
            isInHeaderViewHolder(position) -> HEADER_VIEW_TYPE
            isInFooterViewHolder(position) -> FOOTER_VIEW_TYPE
            else -> getCurrentEntity(currentDataSet(), excludingPosition(position))?.let {
                entityTypeCallback?.invoke(it, excludingPosition(position))
            } ?: DEFAULT_VIEW_TYPE
        }

        override fun getItemId(position: Int) = when {
            isInHeaderViewHolder(position) -> HEADER_VIEW_TYPE.toLong()
            isInFooterViewHolder(position) -> FOOTER_VIEW_TYPE.toLong()
            else -> getCurrentEntity(currentDataSet(), excludingPosition(position))?.let {
                entityIdCallback?.invoke(it, excludingPosition(position))
            } ?: position.toLong()
        }

        override fun getItemCount(): Int {
            val primaryCount = dataSetCount.takeIf { it >= 0 } ?: currentDataSet().size
            val headerCount = if (hasHeaderView) 1 else 0
            val footerCount = if (hasFooterView) 1 else 0

            return primaryCount + headerCount + footerCount
        }
    }
}
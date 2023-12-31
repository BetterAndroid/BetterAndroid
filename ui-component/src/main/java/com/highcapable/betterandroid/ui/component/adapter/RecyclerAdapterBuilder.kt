/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2024 HighCapable
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
import com.highcapable.betterandroid.ui.extension.view.inflate
import com.highcapable.betterandroid.ui.extension.view.inflateViewBinding
import com.highcapable.yukireflection.factory.classOf

/**
 * [RecyclerView.Adapter] builder, using entity [E].
 * @param adapterContext the current context.
 */
class RecyclerAdapterBuilder<E> private constructor(private val adapterContext: Context) : IAdapterBuilder {

    companion object {

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
    private val boundItemViewsCallbacks = mutableListOf<RecyclerItemView<E>>()

    /** The current each item on click event callback. */
    private var itemViewsOnClickCallback: ((View, Int, E, Int) -> Unit)? = null

    /** The current each item on long click event callback. */
    private var itemViewsOnLongClickCallback: ((View, Int, E, Int) -> Boolean)? = null

    /** The current [List] data callback. */
    private var listDataCallback: (() -> List<E>)? = null

    /** The current entity type callback. */
    private var entityTypeCallback: ((E, Int) -> Int)? = null

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
     * Bind each view type to [RecyclerView.Adapter].
     * @param entityType callback the each view type function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onBindViewsType(entityType: (entity: E, position: Int) -> Int) = apply { entityTypeCallback = entityType }

    /**
     * Add and create view holder with [VB].
     * @param viewType the view type, default is 0.
     * @param boundItemViews callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    @JvmName("onBindCustomViews")
    inline fun <reified VB : ViewBinding> onBindViews(
        viewType: Int = 0, noinline boundItemViews: (binding: VB, entity: E, position: Int) -> Unit
    ) = apply {
        boundItemViewsCallbacks.add(RecyclerItemView(bindingClass = classOf<VB>(), viewType = viewType) { binding, _, entity, position ->
            binding?.also { boundItemViews(it as VB, entity, position) }
        })
    }

    /**
     * Add and create view holder.
     * @param resId item view layout ID.
     * @param viewType the view type, default is 0.
     * @param boundItemViews callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onBindViews(@LayoutRes resId: Int, viewType: Int = 0, boundItemViews: (view: View, entity: E, position: Int) -> Unit) = apply {
        boundItemViewsCallbacks.add(RecyclerItemView(rootViewResId = resId, viewType = viewType) { _, rootView, entity, position ->
            rootView?.also { boundItemViews(it, entity, position) }
        })
    }

    /**
     * Add and create view holder.
     * @param view item [View], there must be no parent layout.
     * @param viewType the view type, default is 0.
     * @param boundItemViews callback and return each bound item function.
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun onBindViews(view: View, viewType: Int = 0, boundItemViews: (view: View, entity: E, position: Int) -> Unit) = apply {
        boundItemViewsCallbacks.add(RecyclerItemView(rootView = view, viewType = viewType) { _, rootView, entity, position ->
            rootView?.also { boundItemViews(it, entity, position) }
        })
    }

    /**
     * Set the each item view on click events.
     * @param onClick callback and return each bound item function (the on click event callback).
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onItemViewsClick(onClick: (view: View, viewType: Int, entity: E, position: Int) -> Unit) =
        apply { itemViewsOnClickCallback = onClick }

    /**
     * Set the each item view on long click events.
     * @param onLongClick callback and return each bound item function (the on long click event callback).
     * @return [RecyclerAdapterBuilder]<[E]>
     */
    fun onItemViewsLongClick(onLongClick: (view: View, viewType: Int, entity: E, position: Int) -> Boolean) =
        apply { itemViewsOnLongClickCallback = onLongClick }

    override fun build() = object : RecyclerView.Adapter<RecyclerAdapterBuilder<E>.BaseRecyclerHolder>() {

        /**
         * Get the current each item function callbacks.
         * @param viewType the current view type。
         * @return [RecyclerItemView]<[E]> or null.
         */
        private fun boundItemViewsCallbacks(viewType: Int) = boundItemViewsCallbacks.firstOrNull { it.viewType == viewType }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            boundItemViewsCallbacks(viewType)?.let {
                when {
                    it.bindingClass != null -> {
                        val binding = adapterContext.inflateViewBinding(it.bindingClass, parent = parent, attachToRoot = false)
                        BindingRecyclerHolder(binding, binding.root, viewType)
                    }
                    it.rootViewResId >= 0 -> CommonRecyclerHolder(adapterContext.inflate(it.rootViewResId, parent), it.viewType)
                    it.rootView != null -> CommonRecyclerHolder(it.rootView.apply { layoutParams = parent.layoutParams }, it.viewType)
                    else -> null
                }
            } ?: error("Cannot bound ViewHolder on RecyclerAdapter with type $viewType, did you forgot to called onBindViews function?")

        override fun onBindViewHolder(holder: RecyclerAdapterBuilder<E>.BaseRecyclerHolder, position: Int) {
            boundItemViewsCallbacks.forEach {
                if (it.viewType == holder.viewType) when (holder) {
                    is BindingRecyclerHolder -> it.onBindCallback(holder.binding, holder.binding.root, getCurrentEntity(position), position)
                    is CommonRecyclerHolder -> it.onBindCallback(null, holder.rootView, getCurrentEntity(position), position)
                }
            }
            itemViewsOnClickCallback?.also {
                holder.rootView.setOnClickListener {
                    it(holder.rootView, holder.viewType, getCurrentEntity(holder.adapterPosition), holder.adapterPosition)
                }
            }
            itemViewsOnLongClickCallback?.also {
                holder.rootView.setOnLongClickListener {
                    it(holder.rootView, holder.viewType, getCurrentEntity(holder.adapterPosition), holder.adapterPosition)
                }
            }
        }

        override fun getItemViewType(position: Int) = entityTypeCallback?.invoke(getCurrentEntity(position), position) ?: 0
        override fun getItemId(position: Int) = position.toLong()
        override fun getItemCount() = dataSetCount.takeIf { it >= 0 } ?: listDataCallback?.invoke()?.size ?: 0
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
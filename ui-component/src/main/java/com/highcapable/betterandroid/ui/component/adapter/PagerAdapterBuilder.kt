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
 * This file is created by fankes on 2022/11/22.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.highcapable.betterandroid.ui.component.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.factory.bindAdapter
import com.highcapable.betterandroid.ui.component.adapter.mediator.PagerMediator
import com.highcapable.betterandroid.ui.component.adapter.view.CommonItemView
import com.highcapable.betterandroid.ui.extension.view.inflate
import com.highcapable.betterandroid.ui.extension.view.inflateViewBinding
import com.highcapable.yukireflection.factory.classOf

/**
 * [PagerAdapter] builder, using entity [E].
 * @param adapterContext the current context.
 */
class PagerAdapterBuilder<E> private constructor(private val adapterContext: Context) : IAdapterBuilder {

    companion object {

        /**
         * Create a new [PagerAdapterBuilder]<[E]> from [context].
         * @see ViewPager.bindAdapter
         * @param context the current context.
         * @return [PagerAdapterBuilder]<[E]>
         */
        @JvmStatic
        fun <E> from(context: Context) = PagerAdapterBuilder<E>(context)
    }

    /** The current each item function callbacks. */
    private val boundItemViewsCallbacks = mutableListOf<CommonItemView<E>>()

    /** The current each [PagerMediator] function callback. */
    private var pagerMediatorsCallback: (PagerMediator.() -> Unit)? = null

    /** The current [List] data callback. */
    private var listDataCallback: (() -> List<E>)? = null

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
     * @return [PagerAdapterBuilder]<[E]>
     */
    fun dataSetCount(dataSetCount: Int) = apply { this.dataSetCount = dataSetCount }

    /**
     * Bind [List] to [PagerAdapter].
     * @param result callback the data.
     * @return [PagerAdapterBuilder]<[E]>
     */
    fun onBindData(result: (() -> List<E>)) = apply { listDataCallback = result }

    /**
     * Add and bind the each page's [PagerMediator].
     * @param initiate the [PagerMediator] builder body.
     * @return [PagerAdapterBuilder]<[E]>
     */
    fun onBindMediators(initiate: PagerMediator.() -> Unit) = apply { pagerMediatorsCallback = initiate }

    /**
     * Add and create view holder with [VB].
     * @param boundItemViews callback and return each bound item function.
     * @return [PagerAdapterBuilder]<[E]>
     */
    inline fun <reified VB : ViewBinding> onBindViews(noinline boundItemViews: (binding: VB, entity: E, position: Int) -> Unit) = apply {
        boundItemViewsCallbacks.add(CommonItemView(bindingClass = classOf<VB>()) { binding, _, entity, position ->
            binding?.also { boundItemViews(it as VB, entity, position) }
        })
    }

    /**
     * Add and create view holder.
     * @param resId item view layout ID.
     * @param boundItemViews callback and return each bound item function.
     * @return [PagerAdapterBuilder]<[E]>
     */
    fun onBindViews(@LayoutRes resId: Int, boundItemViews: (view: View, entity: E, position: Int) -> Unit) = apply {
        boundItemViewsCallbacks.add(CommonItemView(rootViewResId = resId) { _, itemView, entity, position ->
            itemView?.also { boundItemViews(it, entity, position) }
        })
    }

    /**
     * Add and create view holder.
     * @param view item [View], there must be no parent layout.
     * @param boundItemViews callback and return each bound item function.
     * @return [PagerAdapterBuilder]<[E]>
     */
    fun onBindViews(view: View, boundItemViews: (view: View, entity: E, position: Int) -> Unit) = apply {
        boundItemViewsCallbacks.add(CommonItemView(rootView = view) { _, itemView, entity, position ->
            itemView?.also { boundItemViews(it, entity, position) }
        })
    }

    override fun build() = object : PagerAdapter() {

        init {
            if ((dataSetCount > 0 || !listDataCallback?.invoke().isNullOrEmpty()) && boundItemViewsCallbacks.size > 1)
                error("Cannot bound multiple ViewHolders with dataSetCount or entities on PagerAdapter.")
        }

        /** The current cached [BaseViewHolder]. */
        private val viewHolders = mutableMapOf<Int, PagerAdapterBuilder<E>.BaseViewHolder>()

        /**
         * Get the current each item function callbacks.
         * @param position the current position.
         * @return [CommonItemView]<[E]> or null.
         */
        private fun boundItemViewsCallbacks(position: Int) = boundItemViewsCallbacks.let { it.getOrNull(position) ?: it.getOrNull(0) }

        override fun instantiateItem(container: ViewGroup, position: Int) =
            (viewHolders[position]?.also { container.addView(it.rootView) } ?: boundItemViewsCallbacks(position)?.let {
                when {
                    it.bindingClass != null ->
                        adapterContext.inflateViewBinding(it.bindingClass).let { binding ->
                            container.addView(binding.root)
                            BindingBaseHolder(binding = binding).apply { viewHolders[position] = this }
                        }
                    it.rootViewResId >= 0 ->
                        adapterContext.inflate(it.rootViewResId).let { itemView ->
                            container.addView(itemView)
                            CommonBaseHolder(rootView = itemView).apply { viewHolders[position] = this }
                        }
                    it.rootView != null -> {
                        container.addView(it.rootView)
                        CommonBaseHolder(rootView = it.rootView).apply { viewHolders[position] = this }
                    }
                    else -> null
                }
            })?.let {
                getCurrentEntity(position).also { entity ->
                    boundItemViewsCallbacks(position)?.onBindCallback
                        ?.invoke((it as? BindingBaseHolder?)?.binding, it.rootView, entity, position)
                }; it.rootView
            } ?: error("Cannot bound ViewHolder on PagerAdapter, did you forgot to called onBindViews function?")

        override fun getPageTitle(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.title }
        override fun getPageWidth(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.width }
        override fun getCount() = dataSetCount.takeIf { it >= 0 } ?: listDataCallback?.invoke()?.size ?: boundItemViewsCallbacks.size
        override fun isViewFromObject(view: View, any: Any) = view == any
        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            container.removeView(any as? View?)
        }
    }

    /**
     * View holder of [ViewBinding].
     * @param binding the [ViewBinding].
     */
    inner class BindingBaseHolder(val binding: ViewBinding) : BaseViewHolder(rootView = binding.root)

    /**
     * Common view holder.
     * @param rootView the item view.
     */
    inner class CommonBaseHolder(override val rootView: View) : BaseViewHolder(rootView = rootView)

    /**
     * Base view holder.
     * @param rootView the item view.
     */
    abstract inner class BaseViewHolder(open val rootView: View? = null)
}
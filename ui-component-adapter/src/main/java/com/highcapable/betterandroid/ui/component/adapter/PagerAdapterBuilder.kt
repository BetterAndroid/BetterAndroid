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
import com.highcapable.betterandroid.ui.component.adapter.viewholder.BaseViewHolder
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.ViewBindingHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.XmlLayoutHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base.ViewHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.impl.BaseViewHolderImpl
import com.highcapable.betterandroid.ui.extension.binding.ViewBinding

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
    private val boundViewHolderCallbacks = linkedSetOf<BaseViewHolder<E>>()

    /** The current each [PagerMediator] function callback. */
    private var pagerMediatorsCallback: (PagerMediator.() -> Unit)? = null

    /** The current [List] data callback. */
    private var listDataCallback: (() -> List<E>)? = null

    /**
     * Get the entity [E].
     * @param position the current position.
     * @return [E] or null.
     */
    private fun getCurrentEntity(position: Int) = (listDataCallback?.invoke() ?: emptyList()).let {
        if (it.isEmpty() && (dataSetCount > 0 || boundViewHolderCallbacks.size > 0)) Any() as E else it.getOrNull(position)
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
     * Create and add view holder from [ViewHolderDelegate]<[VD]>.
     * @param delegate the custom page view delegate.
     * @param viewHolder callback and return each bound item function.
     * @return [PagerAdapterBuilder]<[E]>
     */
    @JvmOverloads
    fun <VD : Any> onBindPageView(
        delegate: ViewHolderDelegate<VD>,
        viewHolder: (delegate: VD, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = apply {
        boundViewHolderCallbacks.add(BaseViewHolder(delegate as ViewHolderDelegate<Any>) { delegate, entity, position ->
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
     * Create and add view holder from [ViewBinding]<[VB]>.
     * @param viewHolder callback and return each bound item function.
     * @return [PagerAdapterBuilder]<[E]>
     */
    inline fun <reified VB : ViewBinding> onBindPageView(
        noinline viewHolder: (binding: VB, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = onBindPageView(ViewBindingHolderDelegate(ViewBinding<VB>()), viewHolder)

    /**
     * Create and add view holder from XML layout ID.
     * @param resId item view layout ID.
     * @param viewHolder callback and return each bound item function.
     * @return [PagerAdapterBuilder]<[E]>
     */
    fun onBindPageView(
        @LayoutRes resId: Int,
        viewHolder: (pageView: View, entity: E, position: Int) -> Unit = { _, _, _ -> }
    ) = onBindPageView(XmlLayoutHolderDelegate(resId), viewHolder)

    override fun build() = Instance()

    /**
     * The [PagerAdapterBuilder] instance.
     */
    inner class Instance internal constructor() : PagerAdapter() {

        init {
            require(dataSetCount <= 0 || listDataCallback?.invoke().isNullOrEmpty()) {
                "You can only use once dataSetCount or entities on PagerAdapter."
            }
            require((dataSetCount <= 0 && listDataCallback?.invoke().isNullOrEmpty()) || boundViewHolderCallbacks.size <= 1) {
                "Cannot bound multiple ViewHolders with dataSetCount or entities on PagerAdapter."
            }
        }

        /** The current cached [BaseViewHolderImpl]. */
        private val viewHolderImpls = mutableMapOf<Int, BaseViewHolderImpl<Any>>()

        /**
         * Get the current each item function callbacks.
         * @param position the current position.
         * @return [BaseViewHolder]<[E]> or null.
         */
        private fun getCallback(position: Int) = boundViewHolderCallbacks.toList().let { it.getOrNull(position) ?: it.getOrNull(0) }

        override fun instantiateItem(container: ViewGroup, position: Int) =
            (viewHolderImpls[position] ?: getCallback(position)?.delegate?.let {
                BaseViewHolderImpl.from(it, adapterContext, container)
            })?.let {
                container.addView(it.rootView)
                getCurrentEntity(position)?.let { entity ->
                    getCallback(position)?.onBindCallback
                        ?.invoke(it.delegateInstance, entity, position)
                }; it.rootView
            } ?: error("No ViewHolder found, are you sure you have created one using onBindPageView?")

        override fun getPageTitle(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.title }
        override fun getPageWidth(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.width }
        override fun getCount() = (dataSetCount.takeIf { it >= 0 } ?: listDataCallback?.invoke()?.size ?: boundViewHolderCallbacks.size)
        override fun isViewFromObject(view: View, any: Any) = view == any
        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            container.removeView(any as? View?)
        }
    }
}
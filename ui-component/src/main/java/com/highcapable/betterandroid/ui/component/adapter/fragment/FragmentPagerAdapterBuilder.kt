/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2023 HighCapable
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
 * This file is created by fankes on 2022/11/23.
 */
@file:Suppress("unused", "DEPRECATION")

package com.highcapable.betterandroid.ui.component.adapter.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.factory.bindFragments
import com.highcapable.betterandroid.ui.component.adapter.mediator.PagerMediator

/**
 * [FragmentPagerAdapter] builder.
 * @param adapterInstance the adapter context, only can be [Context] or [Fragment].
 * @param behavior the current behavior.
 */
class FragmentPagerAdapterBuilder private constructor(private val adapterInstance: Any, private val behavior: Int) : IAdapterBuilder {

    companion object {

        /**
         * Create a new [FragmentPagerAdapter] from [instance].
         *
         * - We recommend to using [ViewPager.bindFragments] at first.
         * @param instance the current instance, only can be [Context] or [Fragment].
         * @param behavior the current behavior.
         * @return [FragmentPagerAdapter]
         */
        @JvmStatic
        fun from(instance: Any, behavior: Int) = FragmentPagerAdapterBuilder(instance, behavior)
    }

    /** The current each [Fragment] function callbacks. */
    private var onBindFragmentsCallback: ((Int) -> Fragment)? = null

    /** The current each [PagerMediator] function callbacks. */
    private var pagerMediatorsCallback: (PagerMediator.() -> Unit)? = null

    /** Number of pages required. */
    var pageCount = 0

    /**
     * Number of pages required.
     * @param pageCount
     * @return [FragmentPagerAdapterBuilder]
     */
    fun pageCount(pageCount: Int) = apply { this.pageCount = pageCount }

    /**
     * Add and bind each [PagerMediator] of pages.
     * @param initiate the [PagerMediator] builder body.
     * @return [FragmentPagerAdapterBuilder]
     */
    fun onBindMediators(initiate: PagerMediator.() -> Unit) = apply { pagerMediatorsCallback = initiate }

    /**
     * Add and create each [Fragment].
     * @param bindFragments callback and return each [Fragment].
     * @return [FragmentPagerAdapterBuilder]
     */
    fun onBindFragments(bindFragments: (position: Int) -> Fragment) = apply { onBindFragmentsCallback = bindFragments }

    override fun build() = object : FragmentPagerAdapter(
        (adapterInstance as? Fragment?)?.childFragmentManager ?: (adapterInstance as? FragmentActivity?)?.supportFragmentManager
            ?: error("FragmentPagerAdapter need a FragmentActivity or Fragment instance."), behavior
    ) {
        override fun getPageTitle(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.title }
        override fun getPageWidth(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.width }
        override fun getCount() = pageCount
        override fun getItem(position: Int) = onBindFragmentsCallback?.invoke(position)
            ?: error("Cannot bound Fragments on FragmentPagerAdapter, did you forgot to called onBindFragments function?")
    }
}
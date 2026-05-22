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
 * This file is created by fankes on 2022/11/23.
 */
@file:Suppress("unused", "DEPRECATION")

package com.highcapable.betterandroid.ui.component.adapter.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.factory.bindFragments
import com.highcapable.betterandroid.ui.component.adapter.fragment.FragmentPagerAdapterBuilder.Companion.from
import com.highcapable.betterandroid.ui.component.adapter.mediator.PagerMediator
import com.highcapable.betterandroid.ui.extension.component.activity
import com.highcapable.betterandroid.ui.extension.component.fragmentManager

/**
 * [FragmentPagerAdapter] builder.
 * @param lcOwner the adapter lifecycle owner, only can be [FragmentActivity] or [Fragment].
 * @param behavior the current behavior.
 */
class FragmentPagerAdapterBuilder private constructor(private val lcOwner: LifecycleOwner, private val behavior: Int) : IAdapterBuilder {

    companion object {

        /**
         * Create a new [FragmentPagerAdapter] from [activity].
         * @see ViewPager.bindFragments
         * @param activity the current activity.
         * @param behavior the current behavior.
         * @return [FragmentPagerAdapter]
         */
        @JvmStatic
        fun from(activity: FragmentActivity, behavior: Int) = FragmentPagerAdapterBuilder(activity, behavior)

        /**
         * Create a new [FragmentPagerAdapter] from [fragment].
         * @see ViewPager.bindFragments
         * @param fragment the current fragment.
         * @param behavior the current behavior.
         * @return [FragmentPagerAdapter]
         */
        @JvmStatic
        fun from(fragment: Fragment, behavior: Int) = FragmentPagerAdapterBuilder(fragment, behavior)

        /**
         * Create a new [FragmentPagerAdapter] from [instance].
         *
         * - This function is deprecated, use [from] instead.
         */
        @Deprecated(message = "Use the overloads that accept FragmentActivity or Fragment directly.")
        @JvmStatic
        fun from(instance: Any, behavior: Int) = FragmentPagerAdapterBuilder(
            lcOwner = instance as? LifecycleOwner ?: error("FragmentPagerAdapter requires a FragmentActivity or Fragment instance."),
            behavior = behavior
        )
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
     * @param body the [PagerMediator] builder body.
     * @return [FragmentPagerAdapterBuilder]
     */
    fun onBindMediators(body: PagerMediator.() -> Unit) = apply { pagerMediatorsCallback = body }

    /**
     * Add and create each [Fragment].
     * @param bindFragments callback and return each [Fragment].
     * @return [FragmentPagerAdapterBuilder]
     */
    fun onBindFragments(bindFragments: (position: Int) -> Fragment) = apply { onBindFragmentsCallback = bindFragments }

    override fun build() = object : FragmentPagerAdapter(
        (lcOwner as? Fragment?)?.fragmentManager() ?: lcOwner.activity<FragmentActivity>()?.fragmentManager()
            ?: error("FragmentPagerAdapter requires a FragmentActivity or Fragment instance."), behavior
    ) {
        override fun getPageTitle(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.title }
        override fun getPageWidth(position: Int) = PagerMediator(position).let { pagerMediatorsCallback?.invoke(it); it.width }
        override fun getCount() = pageCount
        override fun getItem(position: Int) = onBindFragmentsCallback?.invoke(position)
            ?: error("Cannot bind Fragments on FragmentPagerAdapter, did you forget to call onBindFragments?")
    }
}
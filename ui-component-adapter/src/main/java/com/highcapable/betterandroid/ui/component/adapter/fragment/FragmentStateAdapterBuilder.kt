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
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.component.adapter.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.factory.bindFragments
import com.highcapable.betterandroid.ui.component.adapter.fragment.FragmentStateAdapterBuilder.Companion.from
import com.highcapable.betterandroid.ui.extension.component.activity
import com.highcapable.betterandroid.ui.extension.component.fragmentManager

/**
 * [FragmentStateAdapter] builder.
 * @param lcOwner the adapter lifecycle owner, only can be [FragmentActivity] or [Fragment].
 */
class FragmentStateAdapterBuilder private constructor(private val lcOwner: LifecycleOwner) : IAdapterBuilder {

    companion object {

        /**
         * Create a new [FragmentStateAdapter] from [activity].
         * @see ViewPager2.bindFragments
         * @param activity the current activity.
         * @return [FragmentStateAdapter]
         */
        @JvmStatic
        fun from(activity: FragmentActivity) = FragmentStateAdapterBuilder(activity)

        /**
         * Create a new [FragmentStateAdapter] from [fragment].
         * @see ViewPager2.bindFragments
         * @param fragment the current fragment.
         * @return [FragmentStateAdapter]
         */
        @JvmStatic
        fun from(fragment: Fragment) = FragmentStateAdapterBuilder(fragment)

        /**
         * Create a new [FragmentStateAdapter] from [instance].
         *
         * - This function is deprecated, use [from] instead.
         */
        @Deprecated(message = "Use the overloads that accept FragmentActivity or Fragment directly.")
        @JvmStatic
        fun from(instance: Any) = FragmentStateAdapterBuilder(
            lcOwner = instance as? LifecycleOwner ?: error("FragmentStateAdapter requires a FragmentActivity or Fragment instance.")
        )
    }

    /** The current each [Fragment] function callbacks. */
    private var onBindFragmentsCallback: ((Int) -> Fragment)? = null

    /** Number of pages required. */
    var pageCount = 0

    /**
     * Number of pages required.
     * @param pageCount
     * @return [FragmentStateAdapterBuilder]
     */
    fun pageCount(pageCount: Int) = apply { this.pageCount = pageCount }

    /**
     * Add and create each [Fragment].
     * @param bindFragments callback and return each [Fragment].
     * @return [FragmentStateAdapterBuilder]
     */
    fun onBindFragments(bindFragments: (position: Int) -> Fragment) = apply { onBindFragmentsCallback = bindFragments }

    override fun build() = object : FragmentStateAdapter(
        (lcOwner as? Fragment?)?.fragmentManager() ?: lcOwner.activity<FragmentActivity>()?.fragmentManager()
            ?: error("FragmentStateAdapter requires a FragmentActivity or Fragment instance."),
        lcOwner.lifecycle
    ) {
        override fun getItemCount() = pageCount
        override fun createFragment(position: Int) = onBindFragmentsCallback?.invoke(position)
            ?: error("Cannot bind Fragments on FragmentStateAdapter, did you forget to call onBindFragments?")
    }
}
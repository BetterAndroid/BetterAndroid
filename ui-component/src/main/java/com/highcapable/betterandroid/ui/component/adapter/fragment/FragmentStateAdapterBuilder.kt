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
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.component.adapter.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.highcapable.betterandroid.ui.component.adapter.base.IAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.factory.bindFragments
import com.highcapable.betterandroid.ui.extension.component.fragmentManager

/**
 * [FragmentStateAdapter] builder.
 * @param adapterInstance the adapter context, only can be [Context] or [Fragment].
 */
class FragmentStateAdapterBuilder private constructor(private val adapterInstance: Any) : IAdapterBuilder {

    companion object {

        /**
         * Create a new [FragmentStateAdapter] from [instance].
         * @see ViewPager2.bindFragments
         * @param instance the current instance, only can be [Context] or [Fragment].
         * @return [FragmentStateAdapter]
         */
        @JvmStatic
        fun from(instance: Any) = FragmentStateAdapterBuilder(instance)
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
        (adapterInstance as? Fragment?)?.fragmentManager() ?: (adapterInstance as? FragmentActivity?)?.fragmentManager()
            ?: error("FragmentStateAdapter need a FragmentActivity or Fragment instance."),
        (adapterInstance as? LifecycleOwner?)?.lifecycle ?: error("FragmentStateAdapter need an implemented instance of LifecycleOwner.")
    ) {
        override fun getItemCount() = pageCount
        override fun createFragment(position: Int) = onBindFragmentsCallback?.invoke(position)
            ?: error("Cannot bound Fragments on FragmentStateAdapter, did you forgot to called onBindFragments function?")
    }
}
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
 * This file is created by fankes on 2022/11/2.
 */
@file:JvmName("CommonAdapterUtils")
@file:Suppress("unused", "DEPRECATION")

package com.highcapable.betterandroid.ui.component.adapter.factory

import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.ListPopupWindow
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.highcapable.betterandroid.ui.component.adapter.CommonAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.PagerAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.RecyclerAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.fragment.FragmentPageAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.fragment.FragmentStateAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic.RecyclerCosmeticMaker
import com.highcapable.yukireflection.factory.current
import androidx.appcompat.widget.ListPopupWindow as AndroidX_ListPopupWindow

/**
 * Bind the [BaseAdapter] to [ListView], using entity [E].
 * @receiver [ListView]
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter_Generics-")
inline fun <reified E> ListView.bindAdapter(initiate: CommonAdapterBuilder<E>.() -> Unit) =
    CommonAdapterBuilder.from<E>(context).apply(initiate).build().apply { adapter = this }

/**
 * Bind the [BaseAdapter] to [ListView].
 * @receiver [ListView]
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter-")
inline fun ListView.bindAdapter(initiate: CommonAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(initiate)

/**
 * Bind the [BaseAdapter] to [AutoCompleteTextView], using entity [E].
 * @receiver [AutoCompleteTextView]
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter_Generics-")
inline fun <reified E> AutoCompleteTextView.bindAdapter(initiate: CommonAdapterBuilder<E>.() -> Unit) =
    CommonAdapterBuilder.from<E>(context).apply(initiate).build()
        .also { current(ignored = true).method { name = "setAdapter"; paramCount = 1 }.call(it) }

/**
 * Bind the [BaseAdapter] to [AutoCompleteTextView].
 * @receiver [AutoCompleteTextView]
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter-")
inline fun AutoCompleteTextView.bindAdapter(initiate: CommonAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(initiate)

/**
 * Bind the [BaseAdapter] to [ListPopupWindow], using entity [E].
 * @receiver [ListPopupWindow]
 * @param context the current context.
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter_Generics-")
inline fun <reified E> ListPopupWindow.bindAdapter(context: Context, initiate: CommonAdapterBuilder<E>.() -> Unit) =
    CommonAdapterBuilder.from<E>(context).apply(initiate).build().apply { setAdapter(this) }

/**
 * Bind the [BaseAdapter] to [ListPopupWindow].
 * @receiver [ListPopupWindow]
 * @param context the current context.
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter-")
inline fun ListPopupWindow.bindAdapter(context: Context, initiate: CommonAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(context, initiate)

/**
 * Bind the [BaseAdapter] to [AndroidX_ListPopupWindow], using entity [E].
 * @receiver [AndroidX_ListPopupWindow]
 * @param context the current context.
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter_Generics-")
inline fun <reified E> AndroidX_ListPopupWindow.bindAdapter(context: Context, initiate: CommonAdapterBuilder<E>.() -> Unit) =
    CommonAdapterBuilder.from<E>(context).apply(initiate).build().apply { setAdapter(this) }

/**
 * Bind the [BaseAdapter] to [AndroidX_ListPopupWindow].
 * @receiver [AndroidX_ListPopupWindow]
 * @param context the current context.
 * @param initiate the [CommonAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("-bindAdapter-")
inline fun AndroidX_ListPopupWindow.bindAdapter(context: Context, initiate: CommonAdapterBuilder<*>.() -> Unit) =
    bindAdapter<Any>(context, initiate)

/**
 * Bind the [RecyclerView.Adapter] to [RecyclerView], using entity [E].
 *
 * We have preset several common adapter layout types for you to use:
 *
 * - [RecyclerCosmeticMaker.fromLinearVertical] the linear vertical cosmetic (there are two kinds).
 *
 * - [RecyclerCosmeticMaker.fromLinearHorizontal] the linear horizontal cosmetic (there are two kinds).
 *
 * - [RecyclerCosmeticMaker.fromGridVertical] the grid vertical cosmetic (there are two kinds).
 *
 * You can also manually call [RecyclerCosmeticMaker.from] to create a custom cosmetic.
 * @receiver [RecyclerView]
 * @param cosmeticMaker the maker, default is [RecyclerCosmeticMaker.fromLinearVertical].
 * @param initiate the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerAdapterBuilder.BaseRecyclerHolder]>
 */
@JvmName("-bindAdapter_Generics-")
inline fun <reified E> RecyclerView.bindAdapter(
    cosmeticMaker: RecyclerCosmeticMaker = RecyclerCosmeticMaker.fromLinearVertical(context),
    initiate: RecyclerAdapterBuilder<E>.() -> Unit
): RecyclerView.Adapter<RecyclerAdapterBuilder<E>.BaseRecyclerHolder> {
    layoutManager = cosmeticMaker.layoutManager
    cosmeticMaker.itemDecoration?.also { addItemDecoration(it) }
    return RecyclerAdapterBuilder.from<E>(context, cosmeticMaker).apply(initiate).build().apply { adapter = this }
}

/**
 * Bind the [RecyclerView.Adapter] to [RecyclerView].
 *
 * We have preset several common adapter layout types for you to use:
 *
 * - [RecyclerCosmeticMaker.fromLinearVertical] the linear vertical cosmetic (there are two kinds).
 *
 * - [RecyclerCosmeticMaker.fromLinearHorizontal] the linear horizontal cosmetic (there are two kinds).
 *
 * - [RecyclerCosmeticMaker.fromGridVertical] the grid vertical cosmetic (there are two kinds).
 *
 * You can also manually call [RecyclerCosmeticMaker.from] to create a custom cosmetic.
 * @receiver [RecyclerView]
 * @param cosmeticMaker the maker, default is [RecyclerCosmeticMaker.fromLinearVertical].
 * @param initiate the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerAdapterBuilder.BaseRecyclerHolder]>
 */
@JvmName("-bindAdapter-")
inline fun RecyclerView.bindAdapter(
    cosmeticMaker: RecyclerCosmeticMaker = RecyclerCosmeticMaker.fromLinearVertical(context),
    initiate: RecyclerAdapterBuilder<*>.() -> Unit
) = bindAdapter<Any>(cosmeticMaker, initiate)

/**
 * Bind the [PagerAdapter] to [ViewPager], using entity [E].
 * @receiver [ViewPager]
 * @param initiate the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
@JvmName("-bindAdapter_Generics-")
inline fun <reified E> ViewPager.bindAdapter(initiate: PagerAdapterBuilder<E>.() -> Unit) =
    PagerAdapterBuilder.from<E>(context).apply(initiate).build().apply { adapter = this }

/**
 * Bind the [PagerAdapter] to [ViewPager].
 * @receiver [ViewPager]
 * @param initiate the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
@JvmName("-bindAdapter-")
inline fun ViewPager.bindAdapter(initiate: PagerAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(initiate)

/**
 * Bind the [FragmentPagerAdapter] to [ViewPager].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @receiver [ViewPager]
 * @param activity the current activity.
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param initiate the [FragmentPageAdapterBuilder] builder body.
 * @return [FragmentPageAdapterBuilder]
 */
@JvmName("-bindFragments-")
inline fun ViewPager.bindFragments(
    activity: FragmentActivity,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    initiate: FragmentPageAdapterBuilder.() -> Unit
) = FragmentPageAdapterBuilder.from(activity, behavior).apply(initiate).build().apply { adapter = this }

/**
 * Bind the [FragmentPagerAdapter] to [ViewPager].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @receiver [ViewPager]
 * @param fragment the current fragment.
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param initiate the [FragmentPageAdapterBuilder] builder body.
 * @return [FragmentPageAdapterBuilder]
 */
@JvmName("-bindFragments-")
inline fun ViewPager.bindFragments(
    fragment: Fragment,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    initiate: FragmentPageAdapterBuilder.() -> Unit
) = FragmentPageAdapterBuilder.from(fragment, behavior).apply(initiate).build().apply { adapter = this }

/**
 * Bind the [RecyclerView.Adapter] to [ViewPager2], using entity [E].
 * @receiver [ViewPager2]
 * @param initiate the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerAdapterBuilder.BaseRecyclerHolder]>
 */
@JvmName("-bindAdapter_Generics-")
inline fun <reified E> ViewPager2.bindAdapter(initiate: RecyclerAdapterBuilder<E>.() -> Unit) =
    RecyclerAdapterBuilder.from<E>(context).apply(initiate).build().apply { adapter = this }

/**
 * Bind the [RecyclerView.Adapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param initiate the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerAdapterBuilder.BaseRecyclerHolder]>
 */
@JvmName("-bindAdapter-")
inline fun ViewPager2.bindAdapter(initiate: RecyclerAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(initiate)

/**
 * Bind the [FragmentStateAdapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param activity the current activity.
 * @param initiate the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
@JvmName("-bindFragments-")
inline fun ViewPager2.bindFragments(activity: FragmentActivity, initiate: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapterBuilder.from(activity).apply(initiate).build().apply { adapter = this }

/**
 * Bind the [FragmentStateAdapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param fragment the current fragment.
 * @param initiate the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
@JvmName("-bindFragments-")
inline fun ViewPager2.bindFragments(fragment: Fragment, initiate: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapterBuilder.from(fragment).apply(initiate).build().apply { adapter = this }
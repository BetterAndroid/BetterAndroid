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
@file:Suppress("unused", "FunctionName", "DEPRECATION")
@file:JvmName("CommonAdapterUtils")

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
import com.highcapable.betterandroid.ui.component.adapter.BaseAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.PagerAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.RecyclerAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.fragment.FragmentPagerAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.fragment.FragmentStateAdapterBuilder
import com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic.RecyclerCosmetic
import com.highcapable.betterandroid.ui.component.adapter.viewholder.impl.RecyclerViewHolderImpl
import com.highcapable.kavaref.KavaRef.Companion.asResolver
import androidx.appcompat.widget.ListPopupWindow as AndroidX_ListPopupWindow

/**
 * Bind the [BaseAdapter] to [ListView], using entity [E].
 * @receiver [ListView]
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <E> ListView.bindAdapter(builder: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter(context, builder).apply { adapter = this }

/**
 * Bind the [BaseAdapter] to [ListView].
 * @receiver [ListView]
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun ListView.bindAdapter(builder: BaseAdapterBuilder<Any>.() -> Unit) = bindAdapter<Any>(builder)

/**
 * Bind the [BaseAdapter] to [AutoCompleteTextView], using entity [E].
 * @receiver [AutoCompleteTextView]
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <E> AutoCompleteTextView.bindAdapter(builder: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter(context, builder).also {
        asResolver().optional().firstMethodOrNull {
            name = "setAdapter"
            parameterCount = 1
        }?.invokeQuietly(it)
    }

/**
 * Bind the [BaseAdapter] to [AutoCompleteTextView].
 * @receiver [AutoCompleteTextView]
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun AutoCompleteTextView.bindAdapter(builder: BaseAdapterBuilder<Any>.() -> Unit) = bindAdapter<Any>(builder)

/**
 * Bind the [BaseAdapter] to [ListPopupWindow], using entity [E].
 * @receiver [ListPopupWindow]
 * @param context the current context.
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <E> ListPopupWindow.bindAdapter(context: Context, builder: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter(context, builder).apply { setAdapter(this) }

/**
 * Bind the [BaseAdapter] to [ListPopupWindow].
 * @receiver [ListPopupWindow]
 * @param context the current context.
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun ListPopupWindow.bindAdapter(context: Context, builder: BaseAdapterBuilder<Any>.() -> Unit) = bindAdapter<Any>(context, builder)

/**
 * Bind the [BaseAdapter] to [AndroidX_ListPopupWindow], using entity [E].
 * @receiver [AndroidX_ListPopupWindow]
 * @param context the current context.
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <E> AndroidX_ListPopupWindow.bindAdapter(context: Context, builder: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter(context, builder).apply { setAdapter(this) }

/**
 * Bind the [BaseAdapter] to [AndroidX_ListPopupWindow].
 * @receiver [AndroidX_ListPopupWindow]
 * @param context the current context.
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun AndroidX_ListPopupWindow.bindAdapter(context: Context, builder: BaseAdapterBuilder<Any>.() -> Unit) =
    bindAdapter<Any>(context, builder)

/**
 * Bind the [RecyclerView.Adapter] to [RecyclerView], using entity [E].
 *
 * We have preset several common adapter layout types for you to use:
 *
 * - [RecyclerCosmetic.fromLinearVertical] the linear vertical cosmetic (there are two kinds).
 *
 * - [RecyclerCosmetic.fromLinearHorizontal] the linear horizontal cosmetic (there are two kinds).
 *
 * - [RecyclerCosmetic.fromGridVertical] the grid vertical cosmetic (there are two kinds).
 *
 * You can also manually call [RecyclerCosmetic.from] to create a custom cosmetic.
 * @see RecyclerCosmetic.from
 * @receiver [RecyclerView]
 * @param cosmetic the cosmetic, default is [RecyclerCosmetic.fromLinearVertical].
 * @param builder the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
@JvmName("bindAdapterTyped")
inline fun <E> RecyclerView.bindAdapter(
    cosmetic: RecyclerCosmetic<*, *> = RecyclerCosmetic.fromLinearVertical(context),
    builder: RecyclerAdapterBuilder<E>.() -> Unit
): RecyclerView.Adapter<RecyclerViewHolderImpl<Any>> {
    layoutManager = cosmetic.layoutManager
    addItemDecoration(cosmetic.itemDecoration)

    return RecyclerAdapter(context, builder).apply { adapter = this }
}

/**
 * Bind the [RecyclerView.Adapter] to [RecyclerView].
 *
 * We have preset several common adapter layout types for you to use:
 *
 * - [RecyclerCosmetic.fromLinearVertical] the linear vertical cosmetic (there are two kinds).
 *
 * - [RecyclerCosmetic.fromLinearHorizontal] the linear horizontal cosmetic (there are two kinds).
 *
 * - [RecyclerCosmetic.fromGridVertical] the grid vertical cosmetic (there are two kinds).
 *
 * You can also manually call [RecyclerCosmetic.from] to create a custom cosmetic.
 * @see RecyclerCosmetic.from
 * @receiver [RecyclerView]
 * @param cosmetic the cosmetic, default is [RecyclerCosmetic.fromLinearVertical].
 * @param builder the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
inline fun RecyclerView.bindAdapter(
    cosmetic: RecyclerCosmetic<*, *> = RecyclerCosmetic.fromLinearVertical(context),
    builder: RecyclerAdapterBuilder<Any>.() -> Unit
) = bindAdapter<Any>(cosmetic, builder)

/**
 * Bind the [PagerAdapter] to [ViewPager], using entity [E].
 * @receiver [ViewPager]
 * @param builder the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <E> ViewPager.bindAdapter(builder: PagerAdapterBuilder<E>.() -> Unit) =
    PagerAdapter(context, builder).apply { adapter = this }

/**
 * Bind the [PagerAdapter] to [ViewPager].
 * @receiver [ViewPager]
 * @param builder the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
inline fun ViewPager.bindAdapter(builder: PagerAdapterBuilder<Any>.() -> Unit) = bindAdapter<Any>(builder)

/**
 * Bind the [FragmentPagerAdapter] to [ViewPager].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @receiver [ViewPager]
 * @param activity the current activity.
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param builder the [FragmentPagerAdapterBuilder] builder body.
 * @return [FragmentPagerAdapter]
 */
inline fun ViewPager.bindFragments(
    activity: FragmentActivity,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    builder: FragmentPagerAdapterBuilder.() -> Unit
) = FragmentPagerAdapter(activity, behavior, builder).apply { adapter = this }

/**
 * Bind the [FragmentPagerAdapter] to [ViewPager].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @receiver [ViewPager]
 * @param fragment the current fragment.
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param builder the [FragmentPagerAdapterBuilder] builder body.
 * @return [FragmentPagerAdapter]
 */
inline fun ViewPager.bindFragments(
    fragment: Fragment,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    builder: FragmentPagerAdapterBuilder.() -> Unit
) = FragmentPagerAdapter(fragment, behavior, builder).apply { adapter = this }

/**
 * Bind the [RecyclerView.Adapter] to [ViewPager2], using entity [E].
 * @receiver [ViewPager2]
 * @param builder the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
@JvmName("bindAdapterTyped")
inline fun <E> ViewPager2.bindAdapter(builder: RecyclerAdapterBuilder<E>.() -> Unit) =
    RecyclerAdapter(context, builder).apply { adapter = this }

/**
 * Bind the [RecyclerView.Adapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param builder the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
inline fun ViewPager2.bindAdapter(builder: RecyclerAdapterBuilder<Any>.() -> Unit) = bindAdapter<Any>(builder)

/**
 * Bind the [FragmentStateAdapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param activity the current activity.
 * @param builder the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
inline fun ViewPager2.bindFragments(activity: FragmentActivity, builder: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapter(activity, builder).apply { adapter = this }

/**
 * Bind the [FragmentStateAdapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param fragment the current fragment.
 * @param builder the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
inline fun ViewPager2.bindFragments(fragment: Fragment, builder: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapter(fragment, builder).apply { adapter = this }

/**
 * Create a [BaseAdapter], using entity [E].
 * @param context the current context.
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("BaseAdapterTyped")
inline fun <E> BaseAdapter(context: Context, builder: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapterBuilder.from<E>(context).apply(builder).build()

/**
 * Create a [BaseAdapter].
 * @param context the current context.
 * @param builder the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun BaseAdapter(context: Context, builder: BaseAdapterBuilder<Any>.() -> Unit) = BaseAdapter<Any>(context, builder)

/**
 * Create a [RecyclerView.Adapter], using entity [E].
 * @param context the current context.
 * @param builder the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
@JvmName("RecyclerAdapterTyped")
inline fun <E> RecyclerAdapter(context: Context, builder: RecyclerAdapterBuilder<E>.() -> Unit) =
    RecyclerAdapterBuilder.from<E>(context).apply(builder).build()

/**
 * Create a [RecyclerView.Adapter].
 * @param context the current context.
 * @param builder the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
inline fun RecyclerAdapter(context: Context, builder: RecyclerAdapterBuilder<Any>.() -> Unit) = RecyclerAdapter<Any>(context, builder)

/**
 * Create a [PagerAdapter], using entity [E].
 * @param context the current context.
 * @param builder the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
@JvmName("PagerAdapterTyped")
inline fun <E> PagerAdapter(context: Context, builder: PagerAdapterBuilder<E>.() -> Unit) =
    PagerAdapterBuilder.from<E>(context).apply(builder).build()

/**
 * Create a [PagerAdapter].
 * @param context the current context.
 * @param builder the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
inline fun PagerAdapter(context: Context, builder: PagerAdapterBuilder<Any>.() -> Unit) = PagerAdapter<Any>(context, builder)

/**
 * Create a [FragmentPagerAdapter].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @param instance the current instance, only can be [Context] or [Fragment].
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param builder the [FragmentPagerAdapterBuilder] builder body.
 * @return [FragmentPagerAdapter]
 */
inline fun FragmentPagerAdapter(
    instance: Any,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    builder: FragmentPagerAdapterBuilder.() -> Unit
) = FragmentPagerAdapterBuilder.from(instance, behavior).apply(builder).build()

/**
 * Create a [FragmentStateAdapter].
 * @param instance the current instance, only can be [Context] or [Fragment].
 * @param builder the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
inline fun FragmentStateAdapter(instance: Any, builder: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapterBuilder.from(instance).apply(builder).build()
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
@file:Suppress("unused", "FunctionName", "DEPRECATION", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
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
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <reified E> ListView.bindAdapter(body: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter<E>(context, body).apply { adapter = this }

/**
 * Bind the [BaseAdapter] to [ListView].
 * @receiver [ListView]
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun ListView.bindAdapter(body: BaseAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(body)

/**
 * Bind the [BaseAdapter] to [AutoCompleteTextView], using entity [E].
 * @receiver [AutoCompleteTextView]
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <reified E> AutoCompleteTextView.bindAdapter(body: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter<E>(context, body).also {
        asResolver().optional().firstMethodOrNull {
            name = "setAdapter"
            parameterCount = 1
        }?.invokeQuietly(it)
    }

/**
 * Bind the [BaseAdapter] to [AutoCompleteTextView].
 * @receiver [AutoCompleteTextView]
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun AutoCompleteTextView.bindAdapter(body: BaseAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(body)

/**
 * Bind the [BaseAdapter] to [ListPopupWindow], using entity [E].
 * @receiver [ListPopupWindow]
 * @param context the current context.
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <reified E> ListPopupWindow.bindAdapter(context: Context, body: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter<E>(context, body).apply { setAdapter(this) }

/**
 * Bind the [BaseAdapter] to [ListPopupWindow].
 * @receiver [ListPopupWindow]
 * @param context the current context.
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun ListPopupWindow.bindAdapter(context: Context, body: BaseAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(context, body)

/**
 * Bind the [BaseAdapter] to [AndroidX_ListPopupWindow], using entity [E].
 * @receiver [AndroidX_ListPopupWindow]
 * @param context the current context.
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <reified E> AndroidX_ListPopupWindow.bindAdapter(context: Context, body: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapter<E>(context, body).apply { setAdapter(this) }

/**
 * Bind the [BaseAdapter] to [AndroidX_ListPopupWindow].
 * @receiver [AndroidX_ListPopupWindow]
 * @param context the current context.
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun AndroidX_ListPopupWindow.bindAdapter(context: Context, body: BaseAdapterBuilder<*>.() -> Unit) =
    bindAdapter<Any>(context, body)

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
 * @param body the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
@JvmName("bindAdapterTyped")
inline fun <reified E> RecyclerView.bindAdapter(
    cosmetic: RecyclerCosmetic<*, *> = RecyclerCosmetic.fromLinearVertical(context),
    body: RecyclerAdapterBuilder<E>.() -> Unit
): RecyclerView.Adapter<RecyclerViewHolderImpl<Any>> {
    layoutManager = cosmetic.layoutManager
    addItemDecoration(cosmetic.itemDecoration)
    return RecyclerAdapter<E>(context, body).apply { adapter = this }
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
 * @param body the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
inline fun RecyclerView.bindAdapter(
    cosmetic: RecyclerCosmetic<*, *> = RecyclerCosmetic.fromLinearVertical(context),
    body: RecyclerAdapterBuilder<*>.() -> Unit
) = bindAdapter<Any>(cosmetic, body)

/**
 * Bind the [PagerAdapter] to [ViewPager], using entity [E].
 * @receiver [ViewPager]
 * @param body the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
@JvmName("bindAdapterTyped")
inline fun <reified E> ViewPager.bindAdapter(body: PagerAdapterBuilder<E>.() -> Unit) =
    PagerAdapter<E>(context, body).apply { adapter = this }

/**
 * Bind the [PagerAdapter] to [ViewPager].
 * @receiver [ViewPager]
 * @param body the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
inline fun ViewPager.bindAdapter(body: PagerAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(body)

/**
 * Bind the [FragmentPagerAdapter] to [ViewPager].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @receiver [ViewPager]
 * @param activity the current activity.
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param body the [FragmentPagerAdapterBuilder] builder body.
 * @return [FragmentPagerAdapter]
 */
inline fun ViewPager.bindFragments(
    activity: FragmentActivity,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    body: FragmentPagerAdapterBuilder.() -> Unit
) = FragmentPagerAdapter(activity, behavior, body).apply { adapter = this }

/**
 * Bind the [FragmentPagerAdapter] to [ViewPager].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @receiver [ViewPager]
 * @param fragment the current fragment.
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param body the [FragmentPagerAdapterBuilder] builder body.
 * @return [FragmentPagerAdapter]
 */
inline fun ViewPager.bindFragments(
    fragment: Fragment,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    body: FragmentPagerAdapterBuilder.() -> Unit
) = FragmentPagerAdapter(fragment, behavior, body).apply { adapter = this }

/**
 * Bind the [RecyclerView.Adapter] to [ViewPager2], using entity [E].
 * @receiver [ViewPager2]
 * @param body the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
@JvmName("bindAdapterTyped")
inline fun <reified E> ViewPager2.bindAdapter(body: RecyclerAdapterBuilder<E>.() -> Unit) =
    RecyclerAdapter<E>(context, body).apply { adapter = this }

/**
 * Bind the [RecyclerView.Adapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param body the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
inline fun ViewPager2.bindAdapter(body: RecyclerAdapterBuilder<*>.() -> Unit) = bindAdapter<Any>(body)

/**
 * Bind the [FragmentStateAdapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param activity the current activity.
 * @param body the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
inline fun ViewPager2.bindFragments(activity: FragmentActivity, body: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapter(activity, body).apply { adapter = this }

/**
 * Bind the [FragmentStateAdapter] to [ViewPager2].
 * @receiver [ViewPager2]
 * @param fragment the current fragment.
 * @param body the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
inline fun ViewPager2.bindFragments(fragment: Fragment, body: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapter(fragment, body).apply { adapter = this }

/**
 * Create a [BaseAdapter], using entity [E].
 * @param context the current context.
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
@JvmName("BaseAdapterTyped")
inline fun <reified E> BaseAdapter(context: Context, body: BaseAdapterBuilder<E>.() -> Unit) =
    BaseAdapterBuilder.from<E>(context).apply(body).build()

/**
 * Create a [BaseAdapter].
 * @param context the current context.
 * @param body the [BaseAdapterBuilder] builder body.
 * @return [BaseAdapter]
 */
inline fun BaseAdapter(context: Context, body: BaseAdapterBuilder<*>.() -> Unit) = BaseAdapter<Any>(context, body)

/**
 * Create a [RecyclerView.Adapter], using entity [E].
 * @param context the current context.
 * @param body the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
@JvmName("RecyclerAdapterTyped")
inline fun <reified E> RecyclerAdapter(context: Context, body: RecyclerAdapterBuilder<E>.() -> Unit) =
    RecyclerAdapterBuilder.from<E>(context).apply(body).build()

/**
 * Create a [RecyclerView.Adapter].
 * @param context the current context.
 * @param body the [RecyclerAdapterBuilder] builder body.
 * @return [RecyclerView.Adapter]<[RecyclerViewHolderImpl]>
 */
inline fun RecyclerAdapter(context: Context, body: RecyclerAdapterBuilder<*>.() -> Unit) = RecyclerAdapter<Any>(context, body)

/**
 * Create a [PagerAdapter], using entity [E].
 * @param context the current context.
 * @param body the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
@JvmName("PagerAdapterTyped")
inline fun <reified E> PagerAdapter(context: Context, body: PagerAdapterBuilder<E>.() -> Unit) =
    PagerAdapterBuilder.from<E>(context).apply(body).build()

/**
 * Create a [PagerAdapter].
 * @param context the current context.
 * @param body the [PagerAdapterBuilder] builder body.
 * @return [PagerAdapter]
 */
inline fun PagerAdapter(context: Context, body: PagerAdapterBuilder<*>.() -> Unit) = PagerAdapter<Any>(context, body)

/**
 * Create a [FragmentPagerAdapter].
 *
 * - This method is officially deprecated,
 *   the recommended approach is to start using [ViewPager2] and use [ViewPager2.bindFragments].
 * @param instance the current instance, only can be [Context] or [Fragment].
 * @param behavior the current behavior, default is [FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT].
 * @param body the [FragmentPagerAdapterBuilder] builder body.
 * @return [FragmentPagerAdapter]
 */
inline fun FragmentPagerAdapter(
    instance: Any,
    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
    body: FragmentPagerAdapterBuilder.() -> Unit
) = FragmentPagerAdapterBuilder.from(instance, behavior).apply(body).build()

/**
 * Create a [FragmentStateAdapter].
 * @param instance the current instance, only can be [Context] or [Fragment].
 * @param body the [FragmentStateAdapterBuilder] builder body.
 * @return [FragmentStateAdapter]
 */
inline fun FragmentStateAdapter(instance: Any, body: FragmentStateAdapterBuilder.() -> Unit) =
    FragmentStateAdapterBuilder.from(instance).apply(body).build()
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
 * This file is created by fankes on 2024/3/5.
 */
@file:Suppress("unused", "EXTENSION_SHADOWED_BY_MEMBER")
@file:JvmName("LayoutInflaterUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.highcapable.betterandroid.ui.extension.component.base.toHexResourceId
import com.highcapable.yukireflection.factory.classOf

/**
 * Get the layout inflater from context.
 * @receiver the current context.
 * @return [LayoutInflater]
 * @throws IllegalStateException if the layout inflater is null.
 */
val Context.layoutInflater
    get() = (if (this is Activity) this.layoutInflater else LayoutInflater.from(this)) ?: error("Cannot get LayoutInflater from context.")

/**
 * Extension for [LayoutInflater.inflate].
 * @see LayoutInflater.inflate
 * @see LayoutInflater.inflateOrNull
 * @receiver [LayoutInflater]
 * @return [V]
 * @throws IllegalStateException if the inflated view is not a type of [V] or is null.
 */
@JvmName("inflateTyped")
inline fun <reified V : View> LayoutInflater.inflate(
    @LayoutRes resource: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = root != null
) = inflate(resource, root, attachToRoot) as? V?
    ?: error("This inflated view ${resource.toHexResourceId()} is not a type of ${classOf<V>()} or is null.")

/**
 * Extension for [LayoutInflater.inflate].
 * @see LayoutInflater.inflate
 * @receiver [LayoutInflater]
 * @return [V] or null.
 */
@JvmName("inflateOrNullTyped")
inline fun <reified V : View> LayoutInflater.inflateOrNull(
    @LayoutRes resource: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = root != null
) = runCatching { inflate(resource, root, attachToRoot) as? V? }.getOrNull()

/**
 * Extension for [LayoutInflater.inflate].
 * @see LayoutInflater.inflate
 * @see LayoutInflater.inflateOrNull
 * @receiver [LayoutInflater]
 * @return [View]
 * @throws IllegalStateException if the inflated view is not a type of [View] or is null.
 */
@JvmOverloads
fun LayoutInflater.inflate(
    @LayoutRes resource: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = root != null
) = inflate<View>(resource, root, attachToRoot)

/**
 * Extension for [LayoutInflater.inflate].
 * @see LayoutInflater.inflate
 * @receiver [LayoutInflater]
 * @return [View] or null.
 */
@JvmOverloads
fun LayoutInflater.inflateOrNull(
    @LayoutRes resource: Int,
    root: ViewGroup? = null,
    attachToRoot: Boolean = root != null
) = inflateOrNull<View>(resource, root, attachToRoot)
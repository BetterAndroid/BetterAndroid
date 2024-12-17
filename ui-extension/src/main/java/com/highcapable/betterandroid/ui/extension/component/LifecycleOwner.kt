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
 * This file is created by fankes on 2024/12/17.
 */
@file:Suppress("unused", "USELESS_CAST")
@file:JvmName("LifecycleOwnerUtils")

package com.highcapable.betterandroid.ui.extension.component

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.highcapable.yukireflection.factory.classOf

/**
 * Returns the [Context] this [LifecycleOwner] is attached to, or null if it is not attached to a [Context].
 *
 * A [Context] or a [Fragment] that is attached to a [Context] can be used as a [LifecycleOwner].
 * @see LifecycleOwner.requireContext
 * @receiver the current [LifecycleOwner].
 * @return [Context] or null.
 */
val LifecycleOwner.context get() = when (this) {
    is Context -> this as Context
    is Fragment -> context
    else -> null
}

/**
 * Returns the [Activity] this [LifecycleOwner] is attached to, or null if it is not attached to an [Activity].
 *
 * An [Activity] or a [Fragment] that is attached to an [Activity] can be used as a [LifecycleOwner].
 * @see LifecycleOwner.requireActivity
 * @receiver the current [LifecycleOwner].
 * @return [Activity] or null.
 */
val LifecycleOwner.activity get() = when (this) {
    is Activity -> this as Activity
    is Fragment -> activity
    else -> null
}

/**
 * Returns the [Context] this [LifecycleOwner] is attached to, or throws an [IllegalStateException] if it is not attached to a [Context].
 * @see LifecycleOwner.context
 * @receiver the current [LifecycleOwner].
 * @return [Context]
 * @throws IllegalStateException if it is not attached to a [Context].
 */
fun LifecycleOwner.requireContext() = when (this) {
    is Context -> this as Context
    is Fragment -> requireContext()
    else -> error("LifecycleOwner is not a Context.")
}

/**
 * Returns the [Activity] this [LifecycleOwner] is attached to, or throws an [IllegalStateException] if it is not attached to an [Activity].
 * @see LifecycleOwner.activity
 * @receiver the current [LifecycleOwner].
 * @return [Activity]
 * @throws IllegalStateException if it is not attached to an [Activity].
 */
fun LifecycleOwner.requireActivity() = when (this) {
    is Activity -> this as Activity
    is Fragment -> requireActivity()
    else -> error("LifecycleOwner is not an Activity.")
}

/**
 * Returns the [Activity] this [LifecycleOwner] is attached to, or null if it is not attached to an [Activity].
 * @see LifecycleOwner.requireActivity
 * @receiver the current [LifecycleOwner].
 * @return [T] or null.
 */
@JvmName("activity_Typed")
inline fun <reified T : Activity> LifecycleOwner.activity() = activity as? T?

/**
 * Returns the [Activity] this [LifecycleOwner] is attached to, or throws an [IllegalStateException] if it is not attached to an [Activity].
 * @see LifecycleOwner.activity
 * @receiver the current [LifecycleOwner].
 * @return [T]
 * @throws IllegalStateException if it is not attached to an [Activity] or if it is not [T].
 */
@JvmName("requireActivity_Typed")
inline fun <reified T : Activity> LifecycleOwner.requireActivity() = requireActivity() as? T? ?: error("LifecycleOwner is not ${classOf<T>()}.")
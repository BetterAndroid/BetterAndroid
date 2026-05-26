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
 * This file is created by fankes on 2026/5/27.
 */
@file:Suppress("unused")
@file:JvmName("ContextUtils")

package com.highcapable.betterandroid.ui.extension.component

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.highcapable.kavaref.extension.classOf

/**
 * Returns the [Activity] this [Context] is attached to, or null if it is not attached to an [Activity].
 *
 * This property will recursively unwrap [ContextWrapper] until it finds the final [Activity].
 * @see Context.requireHostActivity
 * @receiver the current [Context].
 * @return [Activity] or null.
 */
val Context.hostActivity: Activity? get() = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.hostActivity
    else -> null
}

/**
 * Returns the [Activity] this [Context] is attached to, or throws an [IllegalStateException] if it is not attached to an [Activity].
 * @see Context.hostActivity
 * @receiver the current [Context].
 * @return [Activity]
 * @throws IllegalStateException if it is not attached to an [Activity].
 */
fun Context.requireHostActivity() = hostActivity ?: error("Context $this is not an Activity.")

/**
 * Returns the [Activity] this [Context] is attached to, or null if it is not attached to an [Activity].
 * @see Context.requireHostActivity
 * @receiver the current [Context].
 * @return [T] or null.
 */
@JvmName("hostActivityTyped")
inline fun <reified T : Activity> Context.hostActivity() = hostActivity as? T?

/**
 * Returns the [Activity] this [Context] is attached to, or throws an [IllegalStateException] if it is not attached to an [Activity].
 * @see Context.hostActivity
 * @receiver the current [Context].
 * @return [T]
 * @throws IllegalStateException if it is not attached to an [Activity] or if it is not [T].
 */
@JvmName("requireHostActivityTyped")
inline fun <reified T : Activity> Context.requireHostActivity() = requireHostActivity() as? T? ?: error("Context is not ${classOf<T>()}.")
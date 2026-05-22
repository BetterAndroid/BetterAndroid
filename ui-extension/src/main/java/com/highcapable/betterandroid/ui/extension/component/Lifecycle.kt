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
 * This file is created by fankes on 2026/5/22.
 */
@file:Suppress("unused")
@file:JvmName("LifecycleUtils")

package com.highcapable.betterandroid.ui.extension.component

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Creates a [DefaultLifecycleObserver] with the specified lifecycle event handlers.
 * @see DefaultLifecycleObserver
 * @see LifecycleObserver
 */
inline fun DefaultLifecycleObserver(
    crossinline onCreate: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onStart: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onResume: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onPause: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onStop: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onDestroy: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {}
) = object : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) = onCreate(this, owner)
    override fun onStart(owner: LifecycleOwner) = onStart(this, owner)
    override fun onResume(owner: LifecycleOwner) = onResume(this, owner)
    override fun onPause(owner: LifecycleOwner) = onPause(this, owner)
    override fun onStop(owner: LifecycleOwner) = onStop(this, owner)
    override fun onDestroy(owner: LifecycleOwner) = onDestroy(this, owner)
}

/**
 * Creates a [LifecycleEventObserver] with the specified lifecycle event handlers.
 * @see LifecycleEventObserver
 * @see LifecycleObserver
 */
inline fun LifecycleEventObserver(
    crossinline onStateChanged: LifecycleEventObserver.(source: LifecycleOwner, event: Lifecycle.Event) -> Unit
) = object : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) = onStateChanged(this, source, event)
}

/**
 * Adds a [DefaultLifecycleObserver] to the [Lifecycle] with the specified lifecycle event handlers.
 * @see DefaultLifecycleObserver
 * @see LifecycleObserver
 * @see Lifecycle.addObserver
 * @return [DefaultLifecycleObserver]
 */
inline fun Lifecycle.addObserver(
    crossinline onCreate: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onStart: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onResume: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onPause: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onStop: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {},
    crossinline onDestroy: DefaultLifecycleObserver.(owner: LifecycleOwner) -> Unit = {}
) = DefaultLifecycleObserver(onCreate, onStart, onResume, onPause, onStop, onDestroy).apply { addObserver(this) }

/**
 * Adds a [LifecycleEventObserver] to the [Lifecycle] with the specified lifecycle event handlers.
 * @see LifecycleEventObserver
 * @see LifecycleObserver
 * @see Lifecycle.addObserver
 * @return [LifecycleEventObserver]
 */
inline fun Lifecycle.addObserver(
    crossinline onStateChanged: LifecycleEventObserver.(source: LifecycleOwner, event: Lifecycle.Event) -> Unit
) = LifecycleEventObserver(onStateChanged).apply { addObserver(this) }
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
 * This file is created by fankes on 2026/5/18.
 */
@file:Suppress("unused")
@file:JvmName("BackPressedUtils")

package com.highcapable.betterandroid.ui.extension.component

import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

/**
 * Get the current [OnBackPressedDispatcher] from [Fragment].
 *
 * The current [Fragment] must be attached to a [ComponentActivity].
 * @receiver the current [Fragment].
 * @return [OnBackPressedDispatcher]
 * @throws IllegalStateException if the current [Fragment] is not attached to a [ComponentActivity].
 */
val Fragment.onBackPressedDispatcher get() = requireActivity<ComponentActivity>().onBackPressedDispatcher

/**
 * Get the current [OnBackPressedDispatcher] from [View].
 *
 * The current [View] must be attached to a view tree and hosted by a [ComponentActivity].
 * @receiver the current [View].
 * @return [OnBackPressedDispatcher]
 * @throws IllegalStateException if the current [View] is not attached to a [LifecycleOwner]
 * or its host is not a [ComponentActivity].
 */
val View.onBackPressedDispatcher: OnBackPressedDispatcher
    get() {
        val owner = requireLifecycleOwner()
        val activity = owner.activity<ComponentActivity>() ?: context.hostActivity<ComponentActivity>()
            ?: error("View $this is not attached to a ComponentActivity.")

        return activity.onBackPressedDispatcher
    }

/**
 * Create a new [OnBackPressedCallback].
 * @param enabled whether the callback is enabled, default true.
 * @param onBackPressed the back pressed event.
 * @return [OnBackPressedCallback]
 */
inline fun OnBackPressedCallback(
    enabled: Boolean = true,
    crossinline onBackPressed: OnBackPressedCallback.() -> Unit
) = object : OnBackPressedCallback(enabled) {
    override fun handleOnBackPressed() = onBackPressed()
}

/**
 * Trigger the current back pressed event.
 *
 * This function temporarily disables itself, then dispatches the current
 * back pressed event to the next callback or fallback.
 * @receiver the current [OnBackPressedCallback].
 * @param dispatcher the current [OnBackPressedDispatcher].
 * @param removed whether to remove this callback after triggering, default false.
 */
@JvmOverloads
fun OnBackPressedCallback.trigger(dispatcher: OnBackPressedDispatcher, removed: Boolean = false) {
    isEnabled = false

    try {
        dispatcher.onBackPressed()
    } finally {
        if (removed) remove()
        else isEnabled = true
    }
}
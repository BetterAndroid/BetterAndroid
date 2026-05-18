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
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.activity.OnBackPressedCallback as BaseOnBackPressedCallback

/**
 * Add a new back pressed callback.
 *
 * This function is based on AndroidX [OnBackPressedDispatcher.addCallback],
 * but provides a simpler callback receiver and return type.
 * @receiver the current [OnBackPressedDispatcher].
 * @param owner the [LifecycleOwner], default is null.
 * @param enabled whether this callback is enabled, default true.
 * @param callback the callback when back pressed.
 * @return [BackPressedCallback]
 */
@JvmOverloads
fun OnBackPressedDispatcher.addBackPressedCallback(
    owner: LifecycleOwner? = null,
    enabled: Boolean = true,
    callback: BackPressedCallback.() -> Unit
) = BackPressedCallback(this, enabled, callback).also {
    if (owner != null) addCallback(owner, it)
    else addCallback(it)
}

/**
 * Add a new back pressed callback.
 *
 * This function automatically binds the callback to the current [ComponentActivity].
 * @receiver the current [ComponentActivity].
 * @param enabled whether this callback is enabled, default true.
 * @param callback the callback when back pressed.
 * @return [BackPressedCallback]
 */
@JvmOverloads
fun ComponentActivity.addBackPressedCallback(
    enabled: Boolean = true,
    callback: BackPressedCallback.() -> Unit
) = onBackPressedDispatcher.addBackPressedCallback(this, enabled, callback)

/**
 * Add a new back pressed callback.
 *
 * This function automatically binds the callback to the current [Fragment.viewLifecycleOwner]
 * by default, and you can also provide a custom [LifecycleOwner].
 * @receiver the current [Fragment].
 * @param owner the [LifecycleOwner], default is [Fragment.viewLifecycleOwner].
 * @param enabled whether this callback is enabled, default true.
 * @param callback the callback when back pressed.
 * @return [BackPressedCallback]
 */
@JvmOverloads
fun Fragment.addBackPressedCallback(
    owner: LifecycleOwner = viewLifecycleOwner,
    enabled: Boolean = true,
    callback: BackPressedCallback.() -> Unit
) = requireActivity<ComponentActivity>().onBackPressedDispatcher.addBackPressedCallback(owner, enabled, callback)

/**
 * Add a new back pressed callback.
 *
 * This function automatically binds the callback to the current [View]'s lifecycle owner.
 * The current [View] must be attached to a view tree and hosted by a [ComponentActivity].
 * @receiver the current [View].
 * @param enabled whether this callback is enabled, default true.
 * @param callback the callback when back pressed.
 * @return [BackPressedCallback]
 * @throws IllegalStateException if the current [View] is not attached to a [LifecycleOwner]
 * or its host is not a [ComponentActivity].
 */
@JvmOverloads
fun View.addBackPressedCallback(
    enabled: Boolean = true,
    callback: BackPressedCallback.() -> Unit
): BackPressedCallback {
    val owner = requireLifecycleOwner()
    val activity = owner.requireActivity<ComponentActivity>()

    return activity.onBackPressedDispatcher.addBackPressedCallback(owner, enabled, callback)
}

/**
 * A simple back pressed callback.
 *
 * Inherit from [BaseOnBackPressedCallback].
 *
 * This callback is based on AndroidX [OnBackPressedDispatcher],
 * and provides a [trigger] function to continue dispatching the current back event.
 * @param dispatcher the current [OnBackPressedDispatcher].
 * @param enabled whether this callback is enabled, default true.
 * @param onBackPressed the callback when back pressed.
 */
class BackPressedCallback internal constructor(
    private val dispatcher: OnBackPressedDispatcher,
    enabled: Boolean = true,
    private val onBackPressed: BackPressedCallback.() -> Unit
) : BaseOnBackPressedCallback(enabled) {

    /**
     * Trigger the current back pressed event.
     *
     * This function temporarily disables itself, then dispatches the current
     * back pressed event to the next callback or fallback.
     * @param removed whether to remove this callback after triggering, default false.
     */
    @JvmOverloads
    fun trigger(removed: Boolean = false) {
        isEnabled = false

        try {
            dispatcher.onBackPressed()
        } finally {
            if (removed) remove()
            else isEnabled = true
        }
    }

    override fun handleOnBackPressed() = onBackPressed()
}
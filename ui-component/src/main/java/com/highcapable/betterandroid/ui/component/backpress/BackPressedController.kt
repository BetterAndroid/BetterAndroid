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
 * This file is created by fankes on 2023/10/25.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.backpress

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import com.highcapable.betterandroid.ui.component.activity.AppBindingActivity
import com.highcapable.betterandroid.ui.component.activity.AppComponentActivity
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.component.backpress.callback.OnBackPressedCallback
import com.highcapable.betterandroid.ui.component.fragment.AppBindingFragment
import com.highcapable.betterandroid.ui.component.fragment.AppViewsFragment

/**
 * Back pressed controller.
 *
 * This is a controller with the ability to globally manage system back pressed event.
 * @param activity the current activity.
 */
class BackPressedController private constructor(private val activity: ComponentActivity) {

    companion object {

        /**
         * Create a new [BackPressedController] from [activity].
         *
         * Usage:
         *
         * ```kotlin
         * class YourActivity : AppCompatActivity() {
         *
         *     val backPressed by lazy { BackPressedController.from(this) }
         *
         *     override fun onCreate(savedInstanceState: Bundle?) {
         *         super.onCreate(savedInstanceState)
         *         setContentView(R.layout.activity_main)
         *         // Create a back pressed callback.
         *         val callback = backPressed.addCallback {
         *             // Do something.
         *         }
         *         // Trigger the back pressed.
         *         backPressed.trigger()
         *     }
         *
         *     override fun onDestroy() {
         *         super.onDestroy()
         *         // Destroy the back pressed controller.
         *         // Optional, prevent memory leaks.
         *         backPressed.destroy()
         *     }
         * }
         * ```
         * Or you can inherit the related activities or fragments.
         * @see AppBindingActivity
         * @see AppViewsActivity
         * @see AppComponentActivity
         * @see AppBindingFragment
         * @see AppViewsFragment
         * @param activity the current activity.
         * @return [BackPressedController]
         */
        @JvmStatic
        fun from(activity: ComponentActivity) = BackPressedController(activity)
    }

    /** The [OnBackPressedCallback] callback sets. */
    private val onBackPressedCallbacks = mutableSetOf<OnBackPressedCallback>()

    /**
     * Get the current [OnBackPressedDispatcher].
     * @return [OnBackPressedDispatcher]
     */
    private val onBackPressedDispatcher get() = activity.onBackPressedDispatcher

    /**
     * Checks if there is at least one enabled callback registered with this controller.
     * @return [Boolean]
     */
    val hasEnabledCallbacks get() = onBackPressedCallbacks.any { it.isEnabled }

    /**
     * Manually trigger the back pressed callbacks.
     *
     * - This function is deprecated, use [trigger] instead.
     * @see trigger
     */
    @Deprecated(message = "Use trigger instead.", ReplaceWith("trigger(ignored)"))
    @JvmOverloads
    fun call(ignored: Boolean = false) = trigger(ignored)

    /**
     * Manually trigger the back pressed callbacks.
     * @param ignored whether to ignore all callbacks and back pressed immediately, default false.
     */
    @JvmOverloads
    fun trigger(ignored: Boolean = false) {
        if (activity.isFinishing || activity.isDestroyed) return
        if (ignored) onBackPressedCallbacks.forEach { it.isEnabled = false }
        onBackPressedDispatcher.onBackPressed()
    }

    /**
     * Add a new callback to this controller.
     * @param enabled whether to enable this callback, default true.
     * @param initiate the [OnBackPressedCallback] builder body.
     * @return [OnBackPressedCallback]
     */
    @JvmOverloads
    fun addCallback(enabled: Boolean = true, initiate: OnBackPressedCallback.() -> Unit) =
        OnBackPressedCallback(enabled, initiate).also { addCallback(it) }

    /**
     * Add a new callback to this controller.
     * @param callback the callback.
     */
    fun addCallback(callback: OnBackPressedCallback) {
        onBackPressedDispatcher.addCallback(callback)
        onBackPressedCallbacks.add(callback)
    }

    /**
     * Remove a callback from this controller.
     * @param callback the callback.
     */
    fun removeCallback(callback: OnBackPressedCallback) {
        callback.remove()
        onBackPressedCallbacks.remove(callback)
    }

    /** Remove all callbacks from this controller. */
    fun destroy() {
        onBackPressedCallbacks.forEach { it.remove() }
        onBackPressedCallbacks.clear()
    }
}
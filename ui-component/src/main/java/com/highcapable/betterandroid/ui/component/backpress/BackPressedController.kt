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
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.component.backpress

import androidx.activity.ComponentActivity
import com.highcapable.betterandroid.ui.component.activity.AppBindingActivity
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.component.activity.base.BaseCompatActivity
import com.highcapable.betterandroid.ui.component.activity.base.BaseComponentActivity
import com.highcapable.betterandroid.ui.component.fragment.AppBindingFragment
import com.highcapable.betterandroid.ui.component.fragment.AppViewsFragment
import com.highcapable.betterandroid.ui.component.fragment.base.BaseFragment
import androidx.activity.OnBackPressedCallback as BaseOnBackPressedCallback

/**
 * Back pressed controller.
 *
 * This is a controller with the ability to globally manage system back pressed event.
 * @param activity the current activity.
 */
class BackPressedController private constructor(private val activity: ComponentActivity) {

    companion object {

        /**
         * Create an new [BackPressedController] from [activity].
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
         *         // Direct call the back pressed callbacks.
         *         backPressed.call()
         *     }
         * }
         * ```
         *
         * Or you can use [AppBindingActivity], [AppViewsActivity], [AppBindingFragment], [AppViewsFragment].
         *
         * Usage in [BaseFragment] based on [BaseCompatActivity] or [BaseComponentActivity]:
         *
         * ```kotlin
         * class YourFragment : AppViewsFragment() {
         *
         *     // Omit layout inflate code.
         *
         *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         *         super.onViewCreated(view, savedInstanceState)
         *         // Just add this code.
         *         backPressed.addCallback {
         *             // If you want to back pressed and destroy this.
         *             detachFromActivity()
         *             remove()
         *             // If you want to back pressed and hide this.
         *             hide()
         *             // When calling show later, remember to re-enable this callback.
         *             isEnable = false
         *         }
         *     }
         * }
         * ```
         * @param activity the current activity.
         * @return [BackPressedController]
         */
        @JvmStatic
        fun from(activity: ComponentActivity) = BackPressedController(activity)
    }

    /** The [OnBackPressedCallback] callbacks. */
    private val onBackPressedCallbacks = mutableMapOf<String, OnBackPressedCallback>()

    /**
     * Manually trigger the back pressed callbacks.
     *
     * You need to call [addCallback] to add back pressed callbacks.
     * @param ignored whether to ignore all [onBackPressedCallbacks] and back pressed immediately, default false.
     */
    @JvmOverloads
    fun call(ignored: Boolean = false) {
        if (activity.isDestroyed) return
        val states = mutableMapOf<String, Boolean>().apply { onBackPressedCallbacks.forEach { (key, value) -> this[key] = value.isEnable } }
        if (ignored) onBackPressedCallbacks.forEach { (_, value) -> value.isEnable = false }
        activity.onBackPressedDispatcher.onBackPressed()
        if (ignored) onBackPressedCallbacks.forEach { (key, value) -> states[key]?.also { state -> value.isEnable = state } }
        states.clear()
    }

    /**
     * Add a new callback.
     *
     * To release on back pressed callback, you just need to call [OnBackPressedCallback.releaseAndBack].
     *
     * To remove this callback from the [onBackPressedCallbacks], you just need to call [OnBackPressedCallback.remove].
     * @param isEnable whether to enable this callback, default true.
     * @param initiate the [OnBackPressedCallback] builder body.
     * @return [OnBackPressedCallback]
     */
    @JvmOverloads
    fun addCallback(isEnable: Boolean = true, initiate: OnBackPressedCallback.() -> Unit): OnBackPressedCallback {
        val callbackId = "back_pressed_callback_${(999..99999).random()}"
        val callback = OnBackPressedCallback(callbackId).also {
            it.wrapper = object : BaseOnBackPressedCallback(isEnable) {
                override fun handleOnBackPressed() {
                    it.apply(initiate)
                }
            }.also { callback -> activity.onBackPressedDispatcher.addCallback(callback) }
        }; onBackPressedCallbacks[callbackId] = callback
        return callback
    }

    /**
     * On back pressed callback.
     * @param callbackId The current callback ID.
     * @param wrapper the current wrapper of [OnBackPressedCallback].
     */
    inner class OnBackPressedCallback internal constructor(private val callbackId: String, internal var wrapper: BaseOnBackPressedCallback? = null) {

        /**
         * Get or set whether the callback is enabled.
         * @return [Boolean]
         */
        var isEnable
            get() = wrapper?.isEnabled ?: false
            set(value) {
                wrapper?.isEnabled = value
            }

        /** Remove the current callback. */
        fun remove() {
            wrapper?.remove()
            onBackPressedCallbacks.remove(callbackId)
        }

        /**
         * Immediately release the current callback and call the back pressed callbacks.
         * @param isRemove whether to remove this callback after calling, default false.
         */
        @JvmOverloads
        fun releaseAndBack(isRemove: Boolean = false) {
            isEnable = false
            activity.onBackPressedDispatcher.onBackPressed()
            isEnable = true
            if (isRemove) remove()
        }
    }
}
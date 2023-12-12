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
 * This file is created by fankes on 2023/12/13.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.component.backpress.callback

import com.highcapable.betterandroid.ui.component.backpress.BackPressedController
import androidx.activity.OnBackPressedCallback as BaseOnBackPressedCallback

/**
 * A simple back pressed callback.
 *
 * Inherit from [BaseOnBackPressedCallback].
 * @param enabled whether this callback is enabled, default true.
 * @param handleOnBackPressed the callback when back pressed.
 */
class OnBackPressedCallback(enabled: Boolean = true, private val handleOnBackPressed: OnBackPressedCallback.() -> Unit) :
    BaseOnBackPressedCallback(enabled) {

    /**
     * Trigger the back pressed button of [controller].
     * @param controller the owner of this callback.
     * @param removed whether to remove this callback after calling, default false.
     */
    fun trigger(controller: BackPressedController, removed: Boolean = false) {
        isEnabled = false
        controller.trigger()
        if (removed) controller.removeCallback(callback = this)
    }

    @Deprecated(message = "Hide, use handleOnBackPressed.", level = DeprecationLevel.HIDDEN)
    override fun handleOnBackPressed() {
        handleOnBackPressed(this)
    }
}
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
 * This file is created by fankes on 2023/10/25.
 */
package com.highcapable.betterandroid.ui.component.fragment.base

import android.app.Activity
import androidx.fragment.app.Fragment
import com.highcapable.betterandroid.ui.component.proxy.IBackPressedController
import com.highcapable.betterandroid.ui.component.proxy.ISystemBarsController

/**
 * Base fragment with [ISystemBarsController], [IBackPressedController].
 *
 * Inherited from [Fragment].
 */
abstract class BaseFragment internal constructor() : Fragment(), ISystemBarsController, IBackPressedController {

    override val systemBars
        get() = (requireActivity() as? ISystemBarsController?)?.systemBars
            ?: error("This Fragment attached Activity is not implements from ISystemBarsController.")

    override val backPressed
        get() = (requireActivity() as? IBackPressedController?)?.backPressed
            ?: error("This Fragment attached Activity is not implements from IBackPressedController.")

    /**
     * Get the current activity through [getActivity] and cast to [T].
     * @return [T] or null.
     */
    @JvmName("getActivityTyped")
    inline fun <reified T : Activity> activity() = activity as? T?
}
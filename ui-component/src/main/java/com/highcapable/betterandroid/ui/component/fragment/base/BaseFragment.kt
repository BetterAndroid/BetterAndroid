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
import com.highcapable.betterandroid.ui.component.proxy.ISystemBarsController

/**
 * Base fragment with [ISystemBarsController].
 *
 * Inherited from [Fragment].
 */
abstract class BaseFragment internal constructor() : Fragment(), ISystemBarsController {

    /**
     * Get the current [ISystemBarsController] if the host activity provides it.
     * @return [ISystemBarsController] or null.
     */
    val systemBarsOrNull get() = (activity as? ISystemBarsController?)?.systemBars

    override val systemBars get() = systemBarsOrNull
        ?: error("This Fragment attached Activity is not implements from ISystemBarsController.")

    /**
     * Get the current activity through [getActivity] and cast to [T].
     * @return [T] or null.
     */
    @JvmName("getActivityTyped")
    inline fun <reified T : Activity> activity() = activity as? T?
}
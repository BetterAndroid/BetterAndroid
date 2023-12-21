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
@file:Suppress("MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.activity.base

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.highcapable.betterandroid.ui.component.backpress.BackPressedController
import com.highcapable.betterandroid.ui.component.proxy.IBackPressedController
import com.highcapable.betterandroid.ui.component.proxy.ISystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController

/**
 * Base compat activity with [ISystemBarsController], [IBackPressedController].
 *
 * Inherited from [AppCompatActivity].
 */
abstract class BaseCompatActivity internal constructor() : AppCompatActivity(), ISystemBarsController, IBackPressedController {

    override val systemBars by lazy { SystemBarsController.from(window) }

    override val backPressed by lazy { BackPressedController.from(activity = this) }

    @Deprecated(
        message = "Android has deprecated overriding or calling onBackPressed, " +
            "please use backPressed.addCallback(...) and backPressed.trigger(...) to replace it.",
        replaceWith = ReplaceWith("backPressed.trigger()"),
        level = DeprecationLevel.ERROR
    )
    @Suppress("DEPRECATION", "KotlinRedundantDiagnosticSuppress")
    override fun onBackPressed() {
        super.onBackPressed()
    }

    @CallSuper
    override fun onDestroy() {
        systemBars.destroy()
        backPressed.destroy()
        super.onDestroy()
    }
}
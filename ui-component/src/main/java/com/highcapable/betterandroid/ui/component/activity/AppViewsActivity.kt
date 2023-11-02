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
package com.highcapable.betterandroid.ui.component.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import com.highcapable.betterandroid.ui.component.activity.base.AppBaseActivity

/**
 * App views activity.
 *
 * Inherited from [AppBaseActivity].
 *
 * Usage:
 *
 * ```kotlin
 * class YourActivity : AppViewsActivity() {
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContentView(R.layout.activity_main)
 *         findViewById<TextView>(R.id.main_text).text = "Hello World!"
 *         findViewById<Button>(R.id.enter_full_screen).setOnClickListener {
 *             systemBarsController.hide(SystemBars.ALL)
 *         }
 *     }
 * }
 * ```
 */
open class AppViewsActivity : AppBaseActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        systemBars.init()
    }
}
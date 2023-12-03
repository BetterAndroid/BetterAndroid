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
package com.highcapable.betterandroid.ui.component.fragment

import com.highcapable.betterandroid.ui.component.activity.base.BaseCompatActivity
import com.highcapable.betterandroid.ui.component.activity.base.BaseComponentActivity
import com.highcapable.betterandroid.ui.component.fragment.base.BaseFragment
import com.highcapable.betterandroid.ui.component.proxy.IBackPressedController
import com.highcapable.betterandroid.ui.component.proxy.ISystemBarsController

/**
 * App binding fragment.
 *
 * Inherited from [BaseFragment].
 *
 * Usage:
 *
 * ```kotlin
 * class YourFragment : AppViewsFragment() {
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 *         val view = inflater.inflate(R.layout.fragment_main, container, false)
 *         return view
 *     }
 *
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         super.onViewCreated(view, savedInstanceState)
 *         view.findViewById<TextView>(R.id.main_text).text = "Hello World!"
 *         view.findViewById<Button>(R.id.enter_full_screen).setOnClickListener {
 *             systemBars.hide(SystemBars.ALL)
 *         }
 *     }
 * }
 * ```
 *
 * - You must use an inherited from [BaseCompatActivity] or [BaseComponentActivity]'s activity or implements
 *   [ISystemBarsController], [IBackPressedController] to use this fragment,
 *   otherwise some functions such as [systemBars], [backPressed] will not work.
 */
open class AppViewsFragment : BaseFragment()
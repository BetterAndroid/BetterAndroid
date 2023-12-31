/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2024 HighCapable
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
 * This file is created by fankes on 2023/12/3.
 */
package com.highcapable.betterandroid.ui.component.activity

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.highcapable.betterandroid.ui.component.activity.base.BaseComponentActivity
import com.highcapable.betterandroid.ui.component.insets.factory.handleOnWindowInsetsChanged
import android.R as Android_R

/**
 * App component activity.
 *
 * Inherited from [BaseComponentActivity].
 *
 * - Note: This activity does not add any possible window insets to the root view,
 *   and all layout content is tiled and expanded to full screen.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 */
open class AppComponentActivity : BaseComponentActivity() {

    @CallSuper
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val rootView = findViewById<ViewGroup>(Android_R.id.content).getChildAt(0)
        systemBars.init(rootView, edgeToEdgeInsets = null)
    }

    @CallSuper
    override fun setContentView(view: View?) {
        super.setContentView(view)
        systemBars.init(view, edgeToEdgeInsets = null)
    }

    @CallSuper
    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        systemBars.init(view, edgeToEdgeInsets = null)
    }
}
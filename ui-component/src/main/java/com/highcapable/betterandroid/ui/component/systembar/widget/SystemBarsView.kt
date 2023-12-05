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
 * This file is created by fankes on 2022/10/11.
 */
package com.highcapable.betterandroid.ui.component.systembar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.factory.appendSystemInsets
import com.highcapable.betterandroid.ui.component.systembar.factory.applySystemInsets
import com.highcapable.betterandroid.ui.component.systembar.factory.removeSystemInsets

/**
 * System bars stub view (a placeholder layout).
 *
 * - This view is deprecated and no effect, see [SystemBarsController.SystemBarsView],
 *   please use [View.applySystemInsets], [View.appendSystemInsets], [View.removeSystemInsets] instead.
 */
@Deprecated(message = "Use View.applySystemInsets, View.appendSystemInsets, View.removeSystemInsets instead.")
class SystemBarsView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    @Deprecated(message = "Use View.applySystemInsets, View.appendSystemInsets, View.removeSystemInsets instead.")
    enum class InsetsType

    @Suppress("unused", "DEPRECATION", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use View.applySystemInsets, View.appendSystemInsets, View.removeSystemInsets instead.")
    val insetsType: InsetsType
        get() = error("SystemBarsView is deprecated, use View.applySystemInsets, View.appendSystemInsets, View.removeSystemInsets instead.")

    @Deprecated(message = "Use View.applySystemInsets, View.appendSystemInsets, View.removeSystemInsets instead.")
    fun show() {
    }

    @Suppress("UNUSED_PARAMETER")
    @JvmOverloads
    @Deprecated(message = "Use View.applySystemInsets, View.appendSystemInsets, View.removeSystemInsets instead.")
    fun hide(ignoredCutout: Boolean = false) {
    }
}
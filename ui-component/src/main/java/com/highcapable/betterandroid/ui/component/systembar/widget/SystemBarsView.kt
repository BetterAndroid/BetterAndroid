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
 * This file is created by fankes on 2022/10/11.
 */
package com.highcapable.betterandroid.ui.component.systembar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * System bars stub view. (a placeholder layout)
 *
 * - This class is deprecated and will be removed in the future.
 */
@Deprecated(message = "No effect and will be removed in the future.")
class SystemBarsView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    @Deprecated(message = "No effect and will be removed in the future.")
    enum class InsetsType

    @Suppress("unused", "DEPRECATION", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "No effect and will be removed in the future.")
    val insetsType: InsetsType
        get() = error("SystemBarsView is deprecated, No effect and will be removed in the future.")

    @Deprecated(message = "No effect and will be removed in the future.")
    fun show() {
    }

    @Suppress("UNUSED_PARAMETER")
    @JvmOverloads
    @Deprecated(message = "No effect and will be removed in the future.")
    fun hide(ignoredCutout: Boolean = false) {
    }
}
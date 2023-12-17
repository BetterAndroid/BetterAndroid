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
 * This file is created by fankes on 2022/10/27.
 */
package com.highcapable.betterandroid.ui.component.systembar.compat

import android.app.Activity
import android.util.Log
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.yukireflection.factory.field
import com.highcapable.yukireflection.factory.hasClass
import com.highcapable.yukireflection.factory.method
import com.highcapable.yukireflection.factory.toClassOrNull
import com.highcapable.yukireflection.type.java.IntType

/**
 * System bars compatible adaptation tool for various devices and systems.
 * @param activity the current activity.
 */
internal class SystemBarsCompat internal constructor(private val activity: Activity) {

    /**
     * Returns true if a legacy MIUI system.
     * @return [Boolean]
     */
    internal val isLegacyMiui get() = SystemVersion.isLowTo(SystemVersion.M) && "com.android.internal.policy.impl.MiuiPhoneWindow".hasClass()

    /**
     * Set the legacy MIUI system status bar dark mode.
     * @param isDarkMode the current dark mode or bright mode.
     */
    internal fun setStatusBarDarkModeForLegacyMiui(isDarkMode: Boolean) {
        runCatching {
            val darkModeFlag = "android.view.MiuiWindowManager\$LayoutParams".toClassOrNull()
                ?.field { name = "EXTRA_FLAG_STATUS_BAR_DARK_MODE" }?.ignored()?.get()?.int() ?: 0
            "com.android.internal.policy.impl.MiuiPhoneWindow".toClassOrNull()
                ?.method {
                    name = "setExtraFlags"
                    param(IntType, IntType)
                    superClass()
                }?.ignored()
                ?.get(activity.window)
                ?.call(if (isDarkMode) darkModeFlag else 0, darkModeFlag)
        }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Called setStatusBarDarkModeForLegacyMiui function failed.", it) }
    }
}
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
 * This file is created by fankes on 2022/10/27.
 */
package com.highcapable.betterandroid.ui.component.systembar.compat

import android.util.Log
import android.view.Window
import com.highcapable.betterandroid.system.extension.tool.SystemKind
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.kavaref.extension.toClassOrNull

/**
 * System bars compatible adaptation tool for various devices and systems.
 * @param window the current window.
 */
internal class SystemBarsCompat internal constructor(private val window: Window) {

    /**
     * Returns true if a legacy system.
     * @return [Boolean]
     */
    internal val isLegacySystem get() = isLegacyMiui || isLegacyFlyme

    /**
     * Returns true if a legacy MIUI system.
     * @return [Boolean]
     */
    private val isLegacyMiui get() = SystemVersion.isLowTo(SystemVersion.M) && SystemKind.equals(SystemKind.MIUI)

    /**
     * Returns true if a legacy Flyme system.
     * @return [Boolean]
     */
    private val isLegacyFlyme get() = SystemVersion.isLowTo(SystemVersion.M) && SystemKind.equals(SystemKind.FLYME)

    /**
     * Set the legacy system status bar dark mode.
     * @param isDarkMode the current dark mode or bright mode.
     */
    internal fun setStatusBarDarkMode(isDarkMode: Boolean) {
        when {
            isLegacyMiui -> setStatusBarDarkModeForLegacyMiui(isDarkMode)
            isLegacyFlyme -> setStatusBarDarkModeForLegacyFlyme(isDarkMode)
        }
    }

    /**
     * Set the legacy MIUI system status bar dark mode.
     * @param isDarkMode the current dark mode or bright mode.
     */
    private fun setStatusBarDarkModeForLegacyMiui(isDarkMode: Boolean) {
        runCatching {
            val darkModeFlag = "android.view.MiuiWindowManager\$LayoutParams".toClassOrNull()
                ?.resolve()
                ?.firstField { name = "EXTRA_FLAG_STATUS_BAR_DARK_MODE" }
                ?.get<Int>() ?: 0
            "com.android.internal.policy.impl.MiuiPhoneWindow".toClassOrNull()
                ?.resolve()
                ?.firstMethod {
                    name = "setExtraFlags"
                    parameters(Int::class, Int::class)
                    superclass()
                }?.of(window)
                ?.invoke(if (isDarkMode) darkModeFlag else 0, darkModeFlag)
        }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Called setStatusBarDarkModeForLegacyMiui function failed.", it) }
    }

    /**
     * Set the legacy Flyme system status bar dark mode.
     *
     * The adaptation method is referenced from
     * [here](https://github.com/gyf-dev/ImmersionBar/blob/master/immersionbar/src/main/java/com/gyf/immersionbar/SpecialBarFontUtils.java#L131)
     * and thanks for it.
     * @param isDarkMode the current dark mode or bright mode.
     */
    private fun setStatusBarDarkModeForLegacyFlyme(isDarkMode: Boolean) {
        runCatching {
            window.attributes?.resolve()?.apply {
                val flags = firstField { name = "MEIZU_FLAG_DARK_STATUS_BAR_ICON" }.get<Int>() ?: -1
                val meizuFlagField = firstField { name = "meizuFlags" }
                var meizuFlags = meizuFlagField.get<Int>() ?: -1
                val oldFlags = meizuFlags
                meizuFlags = if (isDarkMode) meizuFlags or flags else meizuFlags and flags.inv()
                if (oldFlags != meizuFlags) meizuFlagField.set(meizuFlags)
            }
        }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Called setStatusBarDarkModeForLegacyFlyme function failed.", it) }
    }
}
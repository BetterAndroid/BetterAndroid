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
 * This file is created by fankes on 2023/12/21.
 */
package com.highcapable.betterandroid.ui.component.insets.compat

import android.util.Log
import android.view.View
import android.view.Window
import com.highcapable.betterandroid.system.extension.tool.AndroidVersion
import com.highcapable.betterandroid.system.extension.tool.RomType
import com.highcapable.betterandroid.system.extension.tool.SystemProperties
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.insets.InsetsWrapper
import com.highcapable.betterandroid.ui.extension.component.base.toPx
import com.highcapable.kavaref.KavaRef.Companion.asResolver
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.kavaref.extension.toClassOrNull

/**
 * Window insets wrapper's compatible adaptation tool for various devices and systems.
 * @param window the current window.
 */
internal class WindowInsetsWrapperCompat internal constructor(private val window: Window?) {

    /**
     * Get the status bar visibility.
     * @return [Boolean]
     */
    @Suppress("DEPRECATION")
    internal val isStatusBarShowing: Boolean
        get() {
            // We need to keep default value still to true because the default behavior is show.
            val decorView = window?.decorView ?: return true
            val uiOptions = decorView.systemUiVisibility
            return (uiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN) != uiOptions
        }

    /**
     * Get the navigation bar visibility.
     * @return [Boolean]
     */
    @Suppress("DEPRECATION")
    internal val isNavigationBarShowing: Boolean
        get() {
            // We need to keep default value still to true because the default behavior is show.
            val decorView = window?.decorView ?: return true
            val uiOptions = decorView.systemUiVisibility
            return (uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != uiOptions
        }

    /**
     * Create a compatible [InsetsWrapper] to adapt to the
     * notch size (cutout size) given by custom ROMs and manufacturer in legacy systems.
     *
     * - Higher than Android 9 calling this function the safeInsetTop will be set to 0
     *
     * - Note: The compatibility of each device and system has not been tested in turn,
     *   if there are legacy system compatibility issues and the device and system
     *   are very niche, they will no longer be adapted.
     * @param statusBars the status bar insets.
     * @return [InsetsWrapper]
     */
    internal fun createLegacyDisplayCutoutInsets(statusBars: InsetsWrapper): InsetsWrapper {
        val context = window?.context ?: return InsetsWrapper.NONE
        var safeInsetTop = 0
        if (AndroidVersion.isAtMost(AndroidVersion.P)) when (RomType.current) {
            RomType.EMUI -> runCatching {
                val huaweiRet = "com.huawei.android.util.HwNotchSizeUtil".toClassOrNull()
                    ?.resolve()
                    ?.optional(silent = true)
                    ?.firstMethodOrNull { name = "getNotchSize" }
                    ?.invoke<IntArray>()
                    ?: intArrayOf(0, 0)
                if (huaweiRet[1] != 0)
                    "com.huawei.android.view.LayoutParamsEx".toClassOrNull()
                        ?.resolve()
                        ?.optional(silent = true)
                        ?.firstMethodOrNull {
                            name = "addHwFlags"
                            parameters(Int::class)
                        }
                        ?.of(window?.attributes)
                        ?.invokeQuietly(0x00010000)
                safeInsetTop = huaweiRet[1]
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for EMUI.", it) }
            RomType.FUNTOUCHOS, RomType.ORIGINOS -> runCatching {
                if ("android.util.FtFeature".toClassOrNull()
                        ?.resolve()
                        ?.optional(silent = true)
                        ?.firstMethodOrNull {
                            name = "isFeatureSupport"
                            parameters(Int::class)
                        }?.invokeQuietly<Boolean>(0x00000020) == true
                ) safeInsetTop = 27.toPx(context)
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for FuntouchOS/OriginalOS.", it) }
            RomType.COLOROS -> runCatching {
                if (context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism"))
                    safeInsetTop = 80
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for ColorOS.", it) }
            RomType.MIUI -> runCatching {
                val hasMiuiNotch = SystemProperties.getBoolean("ro.miui.notch")
                if (hasMiuiNotch) {
                    safeInsetTop = statusBars.top
                    window?.asResolver()?.optional(silent = true)?.firstMethodOrNull {
                        name = "addExtraFlags"
                        parameters(Int::class)
                        superclass()
                    }?.invokeQuietly(0x00000100 or 0x00000200 or 0x00000400)
                }
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for MIUI.", it) }
        }; return InsetsWrapper.of(top = safeInsetTop)
    }
}
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
 * This file is created by fankes on 2023/12/21.
 */
package com.highcapable.betterandroid.ui.component.insets.compat

import android.util.Log
import android.view.Window
import com.highcapable.betterandroid.system.extension.tool.SystemKind
import com.highcapable.betterandroid.system.extension.tool.SystemProperties
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.insets.InsetsWrapper
import com.highcapable.betterandroid.ui.extension.component.base.toPx
import com.highcapable.yukireflection.factory.method
import com.highcapable.yukireflection.factory.toClassOrNull
import com.highcapable.yukireflection.type.android.WindowClass
import com.highcapable.yukireflection.type.java.IntType

/**
 * Window insets wrapper's compatible adaptation tool for various devices and systems.
 * @param window the current window.
 */
internal class WindowInsetsWrapperCompat internal constructor(private val window: Window?) {

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
        if (SystemVersion.isLowAndEqualsTo(SystemVersion.P)) when (SystemKind.get()) {
            SystemKind.EMUI -> runCatching {
                val huaweiRet = "com.huawei.android.util.HwNotchSizeUtil".toClassOrNull()
                    ?.method { name = "getNotchSize" }?.ignored()?.get()?.invoke() ?: intArrayOf(0, 0)
                if (huaweiRet[1] != 0)
                    "com.huawei.android.view.LayoutParamsEx".toClassOrNull()
                        ?.method {
                            name = "addHwFlags"
                            param(IntType)
                        }?.ignored()
                        ?.get(window?.attributes)
                        ?.call(0x00010000)
                safeInsetTop = huaweiRet[1]
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for EMUI.", it) }
            SystemKind.FUNTOUCHOS, SystemKind.ORIGINOS -> runCatching {
                if ("android.util.FtFeature".toClassOrNull()
                        ?.method {
                            name = "isFeatureSupport"
                            param(IntType)
                        }?.ignored()?.get()?.boolean(0x00000020) == true
                ) safeInsetTop = 27.toPx(context)
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for FuntouchOS/OriginalOS.", it) }
            SystemKind.COLOROS -> runCatching {
                if (context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism"))
                    safeInsetTop = 80
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for ColorOS.", it) }
            SystemKind.MIUI -> runCatching {
                val hasMiuiNotch = SystemProperties.getBoolean("ro.miui.notch")
                if (hasMiuiNotch) {
                    safeInsetTop = statusBars.top
                    WindowClass.method {
                        name = "addExtraFlags"
                        param(IntType)
                    }.ignored().get(window).call(0x00000100 or 0x00000200 or 0x00000400)
                }
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for MIUI.", it) }
        }; return InsetsWrapper.of(top = safeInsetTop)
    }
}
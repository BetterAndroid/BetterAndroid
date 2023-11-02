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
@file:Suppress("InternalInsetResource", "DiscouragedApi", "DEPRECATION")

package com.highcapable.betterandroid.ui.component.systembar.compat

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.annotation.Px
import androidx.core.graphics.Insets
import androidx.core.view.DisplayCutoutCompat
import com.highcapable.betterandroid.system.extension.tool.SystemKind
import com.highcapable.betterandroid.system.extension.tool.SystemProperties
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.systembar.wrapper.DisplayCutoutCompatWrapper
import com.highcapable.betterandroid.ui.extension.component.base.asDp
import com.highcapable.yukireflection.factory.field
import com.highcapable.yukireflection.factory.hasClass
import com.highcapable.yukireflection.factory.method
import com.highcapable.yukireflection.factory.toClassOrNull
import com.highcapable.yukireflection.type.android.WindowClass
import com.highcapable.yukireflection.type.java.IntType
import kotlin.math.abs
import android.R as Android_R

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
     * Create a compatible [DisplayCutoutCompat] to adapt to the
     * notch size given by custom ROMs and manufacturer in legacy systems.
     *
     * - Higher than Android 9 calling this function the safeInsetTop will be set to 0
     *
     * - Note: The compatibility of each device and system has not been tested in turn,
     *         if there are legacy system compatibility issues and the device and system
     *         are very niche, they will no longer be adapted.
     * @param insetTop the top padding of system bars (px).
     * @return [DisplayCutoutCompatWrapper]
     */
    internal fun createLegacyDisplayCutoutCompat(@Px insetTop: Int): DisplayCutoutCompatWrapper {
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
                        ?.get(activity.window?.attributes)
                        ?.call(0x00010000)
                safeInsetTop = huaweiRet[1]
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for EMUI/HarmonyOS", it) }
            SystemKind.FUNTOUCHOS, SystemKind.ORIGINOS -> runCatching {
                if ("android.util.FtFeature".toClassOrNull()
                        ?.method {
                            name = "isFeatureSupport"
                            param(IntType)
                        }?.ignored()?.get(activity)?.boolean(0x00000020) == true
                ) safeInsetTop = 27.asDp(activity)
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for FuntouchOS/OriginalOS", it) }
            SystemKind.COLOROS -> runCatching {
                if (activity.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism"))
                    safeInsetTop = 80
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for ColorOS", it) }
            SystemKind.MIUI -> runCatching {
                val hasMiuiNotch = SystemProperties.getBoolean("ro.miui.notch")
                if (hasMiuiNotch) {
                    safeInsetTop = insetTop
                    WindowClass.method {
                        name = "addExtraFlags"
                        param(IntType)
                    }.ignored().get(activity.window).call(0x00000100 or 0x00000200 or 0x00000400)
                }
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for MIUI", it) }
        }; return DisplayCutoutCompatWrapper(activity, compat = this, insets = Insets.of(0, safeInsetTop, 0, 0))
    }

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
        }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Called setStatusBarDarkModeForLegacyMiui function failed", it) }
    }

    /**
     * Get the absolute status bar height (px).
     * @return [Int]
     */
    internal val absoluteStatusBarHeight
        @Px get() = Rect().also {
            activity.window.decorView.getWindowVisibleDisplayFrame(it)
        }.top.takeIf { it > 0 } ?: activity.resources.getIdentifier("status_bar_height", "dimen", "android").asDp(activity)

    /**
     * Get the absolute navigation bar height (px).
     *
     * - Unexpected behavior may occur if notch size exists on the system.
     * @return [Int]
     */
    internal val absoluteNavigationBarHeight
        @Px get() = Point().also { activity.window?.windowManager?.defaultDisplay?.getRealSize(it) }.let { point ->
            if (activity.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE)
                abs(point.x - (activity.window?.decorView?.findViewById<View>(Android_R.id.content)?.width ?: 0))
            else Rect().also {
                activity.window?.decorView?.getWindowVisibleDisplayFrame(it)
            }.let { rect -> abs(rect.bottom - point.y) }
        }
}
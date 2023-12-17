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
 * This file is created by fankes on 2023/12/14.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.insets

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.Window
import androidx.annotation.Px
import androidx.core.view.DisplayCutoutCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.highcapable.betterandroid.system.extension.tool.SystemKind
import com.highcapable.betterandroid.system.extension.tool.SystemProperties
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.insets.factory.createWrapper
import com.highcapable.betterandroid.ui.component.insets.factory.toWrapper
import com.highcapable.betterandroid.ui.extension.component.base.asDp
import com.highcapable.yukireflection.factory.method
import com.highcapable.yukireflection.factory.toClassOrNull
import com.highcapable.yukireflection.type.android.WindowClass
import com.highcapable.yukireflection.type.java.IntType
import kotlin.math.abs
import android.R as Android_R

/**
 * A wrapper carried the common window insets.
 * @param windowInsets the window insets.
 * @param window the current window.
 */
class WindowInsetsWrapper private constructor(private val windowInsets: WindowInsetsCompat, private val window: Window?) {

    companion object {

        /**
         * Create a [WindowInsetsWrapper] from an existing [windowInsets].
         *
         * Compatibility Guidelines:
         *
         * If your app needs to be compatible with the notch screen that
         * may appear on devices below Android 9, you need to set the [window] parameter,
         * this parameter usually comes from [Activity.getWindow], otherwise it can be null.
         * @see WindowInsetsCompat.createWrapper
         * @param windowInsets the window insets.
         * @param window the window, default is null.
         * @return [WindowInsetsWrapper]
         */
        @JvmStatic
        @JvmOverloads
        fun from(windowInsets: WindowInsetsCompat, window: Window? = null) = WindowInsetsWrapper(windowInsets, window)

        /**
         * Create a [WindowInsetsWrapper] from [view] or [Window.getDecorView].
         *
         * Compatibility Guidelines:
         *
         * If your app needs to be compatible with the notch screen that
         * may appear on devices below Android 9, you need to set the [window] parameter,
         * this parameter usually comes from [Activity.getWindow], otherwise it can be null.
         * @param view the view.
         * @param window the window, default is null.
         * @return [WindowInsetsWrapper] or null.
         */
        @JvmStatic
        @JvmOverloads
        fun from(view: View, window: Window? = null) = ViewCompat.getRootWindowInsets(view)?.let { from(it, window) }
    }

    /**
     * An absolute window insets object obtained from [Window.getDecorView].
     * @param window the current window.
     */
    class Absolute private constructor(private val window: Window) {

        companion object {

            /**
             * Create a [WindowInsetsWrapper.Absolute] from [window].
             *
             * We not recommend you to use this function,
             * an inset that is not obtained from the [View]'s window insets
             * changes callback or [View.getRootWindowInsets] is unstable and is for reference only.
             * @param window the current window.
             * @return [WindowInsetsWrapper.Absolute]
             */
            @JvmStatic
            fun from(window: Window) = Absolute(window)
        }

        /**
         * Get the status bars insets.
         * @return [InsetsWrapper]
         */
        val statusBars get() = InsetsWrapper.of(top = statusBarHeight)

        /**
         * Get the navigation bars insets.
         * @return [InsetsWrapper]
         */
        val navigationBars get() = InsetsWrapper.of(bottom = navigationBarHeight)

        /**
         * Get the system bars insets. (include [statusBars] and [navigationBars])
         * @return [InsetsWrapper]
         */
        val systemBars get() = InsetsWrapper.of(top = statusBarHeight, bottom = navigationBarHeight)

        /**
         * Get the status bar height (px).
         * @return [Int]
         */
        @Suppress("DiscouragedApi", "InternalInsetResource")
        private val statusBarHeight
            @Px get() = Rect().also {
                window.decorView.getWindowVisibleDisplayFrame(it)
            }.top.takeIf { it > 0 } ?: window.context.resources.getIdentifier("status_bar_height", "dimen", "android").asDp(window.context)

        /**
         * Get the navigation bar height (px).
         *
         * - Unexpected behavior may occur if notch size exists on the system.
         * @return [Int]
         */
        @Suppress("DEPRECATION")
        private val navigationBarHeight
            @Px get() = Point().also { window.windowManager?.defaultDisplay?.getRealSize(it) }.let { point ->
                if (window.context.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    abs(point.x - (window.decorView.findViewById<View>(Android_R.id.content)?.width ?: 0))
                else abs(Rect().also { window.decorView.getWindowVisibleDisplayFrame(it) }.bottom - point.y)
            }

        override fun toString() =
            "WindowInsetsWrapper.Absolute(statusBars=$statusBars, navigationBars=$navigationBars, systemBars=$systemBars)"
    }

    /**
     * Get the status bars insets.
     * @see WindowInsetsCompat.Type.statusBars
     * @return [InsetsWrapper]
     */
    val statusBars get() = statusBars()

    /**
     * Get the navigation bars insets.
     * @see WindowInsetsCompat.Type.navigationBars
     * @return [InsetsWrapper]
     */
    val navigationBars get() = navigationBars()

    /**
     * Get the caption bar insets.
     * @see WindowInsetsCompat.Type.captionBar
     * @return [InsetsWrapper]
     */
    val captionBar get() = captionBar()

    /**
     * Get the system bars insets. (include [captionBar], [statusBars] and [navigationBars])
     * @see WindowInsetsCompat.Type.systemBars
     * @return [InsetsWrapper]
     */
    val systemBars get() = systemBars()

    /**
     * Get the ime insets.
     * @see WindowInsetsCompat.Type.ime
     * @return [InsetsWrapper]
     */
    val ime get() = ime()

    /**
     * Get the tappable element insets.
     * @see WindowInsetsCompat.Type.tappableElement
     * @return [InsetsWrapper]
     */
    val tappableElement get() = tappableElement()

    /**
     * Get the system gestures insets.
     * @see WindowInsetsCompat.Type.systemGestures
     * @return [InsetsWrapper]
     */
    val systemGestures get() = systemGestures()

    /**
     * Get the mandatory system gestures insets.
     * @see WindowInsetsCompat.Type.mandatorySystemGestures
     * @return [InsetsWrapper]
     */
    val mandatorySystemGestures get() = mandatorySystemGestures()

    /**
     * Get the display cutout insets.
     * @see WindowInsetsCompat.getDisplayCutout
     * @see WindowInsetsCompat.Type.displayCutout
     * @return [InsetsWrapper]
     */
    val displayCutout get() = displayCutout()

    /**
     * Get the waterfall insets.
     * @see DisplayCutoutCompat.getWaterfallInsets
     * @see WindowInsetsCompat.getDisplayCutout
     * @see WindowInsetsCompat.Type.displayCutout
     * @return [InsetsWrapper]
     */
    val waterfall get() = waterfall()

    /**
     * Get the safe content insets. (include [displayCutout] and [systemBars])
     * @see WindowInsetsCompat.Type.displayCutout
     * @see WindowInsetsCompat.Type.systemBars
     * @return [InsetsWrapper]
     */
    val safeContent get() = safeContent()

    /**
     * Get the status bars insets.
     * @see WindowInsetsCompat.Type.statusBars
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun statusBars(ignoringVisibility: Boolean = false) = getInsets(ignoringVisibility, WindowInsetsCompat.Type.statusBars())

    /**
     * Get the navigation bars insets.
     * @see WindowInsetsCompat.Type.navigationBars
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun navigationBars(ignoringVisibility: Boolean = false) = getInsets(ignoringVisibility, WindowInsetsCompat.Type.navigationBars())

    /**
     * Get the caption bar insets.
     * @see WindowInsetsCompat.Type.captionBar
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun captionBar(ignoringVisibility: Boolean = false) = getInsets(ignoringVisibility, WindowInsetsCompat.Type.captionBar())

    /**
     * Get the system bars insets. (include [captionBar], [statusBars] and [navigationBars])
     * @see WindowInsetsCompat.Type.systemBars
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun systemBars(ignoringVisibility: Boolean = false) = getInsets(ignoringVisibility, WindowInsetsCompat.Type.systemBars())

    /**
     * Get the ime insets.
     * @see WindowInsetsCompat.Type.ime
     * @return [InsetsWrapper]
     */
    fun ime() = getInsets(typeMask = WindowInsetsCompat.Type.ime())

    /**
     * Get the tappable element insets.
     * @see WindowInsetsCompat.Type.tappableElement
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun tappableElement(ignoringVisibility: Boolean = false) = getInsets(ignoringVisibility, WindowInsetsCompat.Type.tappableElement())

    /**
     * Get the system gestures insets.
     * @see WindowInsetsCompat.Type.systemGestures
     * @return [InsetsWrapper]
     */
    fun systemGestures() = getInsets(typeMask = WindowInsetsCompat.Type.systemGestures())

    /**
     * Get the mandatory system gestures insets.
     * @see WindowInsetsCompat.Type.mandatorySystemGestures
     * @return [InsetsWrapper]
     */
    fun mandatorySystemGestures() = getInsets(typeMask = WindowInsetsCompat.Type.mandatorySystemGestures())

    /**
     * Get the display cutout insets.
     * @see WindowInsetsCompat.getDisplayCutout
     * @see WindowInsetsCompat.Type.displayCutout
     * @return [InsetsWrapper]
     */
    fun displayCutout() = SystemVersion.require(SystemVersion.P, createLegacyDisplayCutoutInsets()) {
        getInsets(typeMask = WindowInsetsCompat.Type.displayCutout())
    }

    /**
     * Get the waterfall insets.
     * @see DisplayCutoutCompat.getWaterfallInsets
     * @see WindowInsetsCompat.getDisplayCutout
     * @see WindowInsetsCompat.Type.displayCutout
     * @return [InsetsWrapper]
     */
    fun waterfall() = windowInsets.displayCutout?.waterfallInsets?.toWrapper() ?: InsetsWrapper.NONE

    /**
     * Get the safe content insets. (include [displayCutout] and [systemBars])
     * @see WindowInsetsCompat.Type.displayCutout
     * @see WindowInsetsCompat.Type.systemBars
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun safeContent(ignoringVisibility: Boolean = false) = InsetsWrapper.max(displayCutout(), systemBars(ignoringVisibility))

    /**
     * Get the window insets.
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @param typeMask the insets type mask.
     * @return [InsetsWrapper]
     */
    private fun getInsets(ignoringVisibility: Boolean = false, typeMask: Int): InsetsWrapper {
        val isVisible = windowInsets.isVisible(typeMask)
        val insets = if (ignoringVisibility) windowInsets.getInsetsIgnoringVisibility(typeMask) else windowInsets.getInsets(typeMask)
        return insets.toWrapper(isVisible)
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
     * @return [InsetsWrapper]
     */
    private fun createLegacyDisplayCutoutInsets(): InsetsWrapper {
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
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for EMUI/HarmonyOS.", it) }
            SystemKind.FUNTOUCHOS, SystemKind.ORIGINOS -> runCatching {
                if ("android.util.FtFeature".toClassOrNull()
                        ?.method {
                            name = "isFeatureSupport"
                            param(IntType)
                        }?.ignored()?.get()?.boolean(0x00000020) == true
                ) safeInsetTop = 27.asDp(context)
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for FuntouchOS/OriginalOS.", it) }
            SystemKind.COLOROS -> runCatching {
                if (context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism"))
                    safeInsetTop = 80
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for ColorOS.", it) }
            SystemKind.MIUI -> runCatching {
                val hasMiuiNotch = SystemProperties.getBoolean("ro.miui.notch")
                if (hasMiuiNotch) {
                    safeInsetTop = statusBars(ignoringVisibility = true).top
                    WindowClass.method {
                        name = "addExtraFlags"
                        param(IntType)
                    }.ignored().get(window).call(0x00000100 or 0x00000200 or 0x00000400)
                }
            }.onFailure { Log.w(BetterAndroidProperties.PROJECT_NAME, "Failed to set display cutout configuration for MIUI.", it) }
        }; return InsetsWrapper.of(top = safeInsetTop)
    }

    override fun toString() =
        "WindowInsetsWrapper(" +
            "statusBars=$statusBars, " +
            "statusBarsIgnoringVisibility=${statusBars(ignoringVisibility = true)}, " +
            "navigationBars=$navigationBars, " +
            "navigationBarsIgnoringVisibility=${navigationBars(ignoringVisibility = true)}, " +
            "captionBar=$captionBar, " +
            "captionBarIgnoringVisibility=${captionBar(ignoringVisibility = true)}, " +
            "systemBars=$systemBars, " +
            "systemBarsIgnoringVisibility=${systemBars(ignoringVisibility = true)}, " +
            "ime=$ime, " +
            "tappableElement=$tappableElement, " +
            "tappableElementIgnoringVisibility=${tappableElement(ignoringVisibility = true)}, " +
            "systemGestures=$systemGestures, " +
            "mandatorySystemGestures=$mandatorySystemGestures, " +
            "displayCutout=$displayCutout, " +
            "waterfall=$waterfall, " +
            "safeContent=$safeContent, " +
            "safeContentIgnoringVisibility=${safeContent(ignoringVisibility = true)}" +
            ")"
}
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
import android.view.View
import android.view.Window
import androidx.annotation.Px
import androidx.core.graphics.Insets
import androidx.core.view.DisplayCutoutCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.insets.compat.WindowInsetsWrapperCompat
import com.highcapable.betterandroid.ui.component.insets.factory.createWrapper
import com.highcapable.betterandroid.ui.component.insets.factory.toWrapper
import com.highcapable.betterandroid.ui.extension.component.base.toPx
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
         *
         * If your app is targeting Android 10 and below, we recommend to set the [window] parameter at all times.
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
         *
         * If your app is targeting Android 10 and below, we recommend to set the [window] parameter at all times.
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
            }.top.takeIf { it > 0 } ?: window.context.resources.getIdentifier("status_bar_height", "dimen", "android").toPx(window.context)

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
     * Get the current window insets wrapper compat instance.
     * @return [WindowInsetsWrapperCompat]
     */
    private val wrapperCompat by lazy { WindowInsetsWrapperCompat(window) }

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
     * Get the safe gestures insets. (include [systemGestures], [mandatorySystemGestures], [waterfall] and [tappableElement])
     *
     * See [here](https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.foundation.layout.WindowInsets.Companion).safeGestures())
     * for more information.
     * @see systemGestures
     * @see mandatorySystemGestures
     * @see waterfall
     * @see tappableElement
     * @return [InsetsWrapper]
     */
    val safeGestures get() = safeGestures()

    /**
     * Get the safe drawing insets. (include [displayCutout], [systemBars] and [ime])
     *
     * See [here](https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.foundation.layout.WindowInsets.Companion).safeDrawing())
     * for more information.
     * @see displayCutout
     * @see systemBars
     * @see ime
     * @return [InsetsWrapper]
     */
    val safeDrawing get() = safeDrawing()

    /**
     * Get the safe drawing (ignoring ime) insets. (include [displayCutout], [systemBars])
     * @see displayCutout
     * @see systemBars
     * @return [InsetsWrapper]
     */
    val safeDrawingIgnoringIme get() = safeDrawingIgnoringIme()

    /**
     * Get the safe content insets. (include [safeDrawing] and [safeGestures])
     *
     * See [here](https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.foundation.layout.WindowInsets.Companion).safeContent())
     * for more information.
     * @see safeDrawing
     * @see safeGestures
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
    fun displayCutout() =
        SystemVersion.require(SystemVersion.P, wrapperCompat.createLegacyDisplayCutoutInsets(statusBars(ignoringVisibility = true))) {
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
     * Get the safe gestures insets. (include [systemGestures], [mandatorySystemGestures], [waterfall] and [tappableElement])
     *
     * See [here](https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.foundation.layout.WindowInsets.Companion).safeGestures())
     * for more information.
     * @see systemGestures
     * @see mandatorySystemGestures
     * @see waterfall
     * @see tappableElement
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun safeGestures(ignoringVisibility: Boolean = false) =
        systemGestures() or mandatorySystemGestures() or waterfall() or tappableElement(ignoringVisibility)

    /**
     * Get the safe drawing insets. (include [displayCutout], [systemBars] and [ime])
     *
     * See [here](https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.foundation.layout.WindowInsets.Companion).safeDrawing())
     * for more information.
     * @see displayCutout
     * @see systemBars
     * @see ime
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun safeDrawing(ignoringVisibility: Boolean = false) = displayCutout() or systemBars(ignoringVisibility) or ime()

    /**
     * Get the safe drawing (ignoring ime) insets. (include [displayCutout], [systemBars])
     * @see displayCutout
     * @see systemBars
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun safeDrawingIgnoringIme(ignoringVisibility: Boolean = false) = displayCutout() or systemBars(ignoringVisibility)

    /**
     * Get the safe content insets. (include [safeDrawing] and [safeGestures])
     *
     * See [here](https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.foundation.layout.WindowInsets.Companion).safeContent())
     * for more information.
     * @see safeDrawing
     * @see safeGestures
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @return [InsetsWrapper]
     */
    fun safeContent(ignoringVisibility: Boolean = false) = safeDrawing(ignoringVisibility) or safeGestures(ignoringVisibility)

    /**
     * Get the window insets.
     * @param ignoringVisibility whether ignoring the visibility, default false.
     * @param typeMask the insets type mask.
     * @return [InsetsWrapper]
     */
    private fun getInsets(ignoringVisibility: Boolean = false, typeMask: Int): InsetsWrapper {
        var isVisible = windowInsets.isVisible(typeMask)
        var insets = if (ignoringVisibility) windowInsets.getInsetsIgnoringVisibility(typeMask) else windowInsets.getInsets(typeMask)
        // Workaround for the visible state of the system bars below Android 11.
        if (!ignoringVisibility && SystemVersion.isLowTo(SystemVersion.R) && window != null)
            when (typeMask) {
                WindowInsetsCompat.Type.systemBars() -> {
                    isVisible = wrapperCompat.isStatusBarShowing && wrapperCompat.isNavigationBarShowing
                    if (!isVisible) insets = Insets.NONE
                }
                WindowInsetsCompat.Type.statusBars() -> {
                    isVisible = wrapperCompat.isStatusBarShowing
                    if (!isVisible) insets = Insets.NONE
                }
                WindowInsetsCompat.Type.navigationBars() -> {
                    isVisible = wrapperCompat.isNavigationBarShowing
                    if (!isVisible) insets = Insets.NONE
                }
            }
        return insets.toWrapper(isVisible)
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
            "safeGestures=$safeGestures, " +
            "safeGesturesIgnoringVisibility=${safeGestures(ignoringVisibility = true)}, " +
            "safeDrawing=$safeDrawing, " +
            "safeDrawingIgnoringVisibility=${safeDrawing(ignoringVisibility = true)}, " +
            "safeDrawingIgnoringIme=$safeDrawingIgnoringIme, " +
            "safeDrawingIgnoringImeIgnoringVisibility=${safeDrawingIgnoringIme(ignoringVisibility = true)}, " +
            "safeContent=$safeContent, " +
            "safeContentIgnoringVisibility=${safeContent(ignoringVisibility = true)}" +
            ")"
}
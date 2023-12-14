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
 * This file is created by fankes on 2023/12/8.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.platform.component.systembar

import com.highcapable.betterandroid.compose.extension.platform.component.systembar.insets.SystemBarsInsets
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.insets.wrapper.UIEdgeInsetsWrapper
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.type.SystemBars
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.type.SystemBarsBehavior
import com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller.AppComponentUIViewController
import com.highcapable.betterandroid.compose.extension.ui.isBrightColor
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIRectEdgeAll
import platform.UIKit.UIRectEdgeNone
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIView
import platform.UIKit.UIViewController

/**
 * System bars controller.
 *
 * This is a controller with the ability to globally manage system bars.
 * @param controller the current UI view controller.
 */
class SystemBarsController private constructor(private val controller: AppComponentUIViewController) {

    companion object {

        /**
         * Create a new [SystemBarsController] from [AppComponentUIViewController].
         *
         * Creating an [UIViewController] yourself is still in the experimental stage in Kotlin Native,
         * @see AppComponentUIViewController
         * @param controller the current UI view controller.
         */
        fun from(controller: AppComponentUIViewController) = SystemBarsController(controller)
    }

    /** Indicates init only once times. */
    private var isInitOnce = false

    /**
     * Get the system bars views. (included status bars, home indicator)
     * @return [Array]<[SystemBarsView]>
     */
    private val systemBarsViews by lazy { arrayOf(SystemBarsView(), SystemBarsView()) }

    /** The system bars insets changes callback sets. */
    private var onInsetsChangedCallbacks = mutableSetOf<(SystemBarsInsets) -> Unit>()

    /** The behavior of system bars. */
    private var currentBehavior = SystemBarsBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES

    /**
     * System bars stub view (a placeholder layout).
     */
    private class SystemBarsView {

        /** The current view. */
        val current = UIView()

        /** The height constraint. */
        private var heightConstraint = current.heightAnchor.constraintEqualToConstant(0.0)

        init {
            current.translatesAutoresizingMaskIntoConstraints = false
            current.setUserInteractionEnabled(false)
        }

        /**
         * Create the constraints.
         * @param rootView the root view.
         * @param direction the direction.
         * @return [List]<[NSLayoutConstraint]>
         */
        fun createConstraints(rootView: UIView, direction: Direction) = listOf(
            when (direction) {
                Direction.TOP -> current.topAnchor.constraintEqualToAnchor(rootView.topAnchor)
                Direction.BOTTOM -> current.bottomAnchor.constraintEqualToAnchor(rootView.bottomAnchor)
            },
            current.leadingAnchor.constraintEqualToAnchor(rootView.leadingAnchor),
            current.trailingAnchor.constraintEqualToAnchor(rootView.trailingAnchor),
            heightConstraint
        )

        /**
         * Update the height constraint.
         * @param height the height to set.
         */
        fun updateHeight(height: CGFloat) {
            heightConstraint.constant = height
        }

        /**
         * Set the background color.
         * @param color the color to set.
         */
        fun setBackgroundColor(color: UIColor) = current.setBackgroundColor(color)
    }

    /**
     * Direction of the system bars view.
     */
    private enum class Direction { TOP, BOTTOM }

    /** Refresh the behavior of system bars. */
    private fun refreshBehavior() {
        controller.screenEdgesDeferringSystemGestures = when (currentBehavior) {
            SystemBarsBehavior.DEFAULT -> UIRectEdgeNone
            SystemBarsBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES ->
                if (controller.isStatusBarHidden) UIRectEdgeAll else UIRectEdgeNone
        }
    }

    /**
     * Update the system bars views height.
     * @param insets the insets padding.
     */
    private fun updateSystemBarsViewsHeight(insets: UIEdgeInsetsWrapper?) {
        insets ?: return
        systemBarsViews[0].updateHeight(insets.top)
        systemBarsViews[1].updateHeight(insets.bottom)
    }

    /**
     * Create the system bars insets from [UIEdgeInsetsWrapper].
     * @return [SystemBarsInsets]
     */
    private fun UIEdgeInsetsWrapper.createSystemBarsInsets() = SystemBarsInsets(safeArea = this)

    /**
     * Create the [SystemBarsController]'s container layout.
     *
     * ```
     * Parent Layout (UIWindow)
     * └─ Base Container Layout (UIView)
     *    ├─ Root View (UIView) ← The content view
     *    ├─ Status Bars (SystemBarsView (UIView + Constraints))
     *    └─ Home Indicator (SystemBarsView (UIView + Constraints))
     * ```
     */
    private fun createSystemBarsLayout() {
        controller.view.addSubview(systemBarsViews[0].current)
        controller.view.addSubview(systemBarsViews[1].current)
        NSLayoutConstraint.activateConstraints(systemBarsViews[0].createConstraints(controller.view, Direction.TOP))
        NSLayoutConstraint.activateConstraints(systemBarsViews[1].createConstraints(controller.view, Direction.BOTTOM))
    }

    /**
     * The current system bars insets.
     *
     * The first initialization may be null, it is recommended to
     * use [addOnInsetsChangeListener] to add the system bars insets changes listener.
     */
    var systemBarsInsets: SystemBarsInsets? = null

    /**
     * Get the current [SystemBarsController]'s status.
     * @return [Boolean]
     */
    val isDestroyed get() = !isInitOnce

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [SystemBarsBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES].
     * @return [SystemBarsBehavior]
     */
    var behavior
        get() = currentBehavior
        set(value) {
            currentBehavior = value
            refreshBehavior()
        }

    /**
     * Initialize [SystemBarsController].
     *
     * - The initialization operation will not be called repeatedly,
     *   repeated calls will only be performed once.
     */
    fun init() {
        if (isInitOnce) return
        isInitOnce = true
        createSystemBarsLayout()
        controller.onViewDidLayoutSubviewsCallback = {
            val safeAreaInsets = controller.view.safeAreaInsets.useContents { UIEdgeInsetsWrapper.from(insets = this) }
            val systemBarsInsets = safeAreaInsets.createSystemBarsInsets()
            this.systemBarsInsets = systemBarsInsets
            updateSystemBarsViewsHeight(safeAreaInsets)
            onInsetsChangedCallbacks.forEach { callback -> callback(systemBarsInsets) }
        }
    }

    /**
     * Get or set the status bar style.
     * @return [UIStatusBarStyle].
     */
    var statusBarStyle
        get() = controller.statusBarStyle
        set(value) {
            controller.statusBarStyle = value
        }

    /**
     * Add the system bars insets changes listener.
     * @param onChange callback [SystemBarsInsets].
     */
    fun addOnInsetsChangeListener(onChange: (SystemBarsInsets) -> Unit) {
        onInsetsChangedCallbacks.add(onChange)
    }

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    fun show(type: SystemBars) {
        when (type) {
            SystemBars.ALL -> {
                controller.isStatusBarHidden = false
                controller.isHomeIndicatorAutoHidden = false
            }
            SystemBars.STATUS_BARS -> controller.isStatusBarHidden = false
            SystemBars.HOME_INDICATOR -> controller.isHomeIndicatorAutoHidden = false
        }; refreshBehavior()
    }

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    fun hide(type: SystemBars) {
        when (type) {
            SystemBars.ALL -> {
                controller.isStatusBarHidden = true
                controller.isHomeIndicatorAutoHidden = true
            }
            SystemBars.STATUS_BARS -> controller.isStatusBarHidden = true
            SystemBars.HOME_INDICATOR -> controller.isHomeIndicatorAutoHidden = true
        }; refreshBehavior()
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    fun isVisible(type: SystemBars) = when (type) {
        SystemBars.ALL -> !controller.isStatusBarHidden && !controller.isHomeIndicatorAutoHidden
        SystemBars.STATUS_BARS -> !controller.isStatusBarHidden
        SystemBars.HOME_INDICATOR -> !controller.isHomeIndicatorAutoHidden
    }

    /**
     * Set the system bars background color.
     * @param type the system bars type.
     * @param color the color to set.
     */
    fun setColor(type: SystemBars, color: UIColor) {
        when (type) {
            SystemBars.ALL -> {
                systemBarsViews[0].setBackgroundColor(color)
                systemBarsViews[1].setBackgroundColor(color)
            }
            SystemBars.STATUS_BARS -> systemBarsViews[0].setBackgroundColor(color)
            SystemBars.HOME_INDICATOR -> systemBarsViews[1].setBackgroundColor(color)
        }
    }

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    fun adaptiveAppearance(color: UIColor) {
        val isBrightColor = color.isBrightColor
        statusBarStyle = if (isBrightColor) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
    }
}
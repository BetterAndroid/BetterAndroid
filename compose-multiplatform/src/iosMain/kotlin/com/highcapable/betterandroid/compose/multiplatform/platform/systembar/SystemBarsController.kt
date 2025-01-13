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
 * This file is created by fankes on 2023/12/8.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.compose.multiplatform.platform.systembar

import com.highcapable.betterandroid.compose.multiplatform.platform.AppComponentUIViewController
import com.highcapable.betterandroid.compose.multiplatform.platform.insets.UIEdgeInsetsWrapper
import com.highcapable.betterandroid.compose.multiplatform.platform.systembar.style.SystemBarStyle
import com.highcapable.betterandroid.compose.multiplatform.platform.systembar.type.SystemBarBehavior
import com.highcapable.betterandroid.compose.multiplatform.platform.systembar.type.SystemBars
import com.highcapable.betterandroid.compose.multiplatform.platform.uiviewcontroller.AppComponentUIViewController
import platform.CoreGraphics.CGFloat
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIRectEdgeAll
import platform.UIKit.UIRectEdgeNone
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleDefault
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIView
import platform.UIKit.UIViewController

/**
 * System bars controller.
 *
 * This is a controller with the ability to globally manage system bars.
 * @param controller the current UI view controller.
 */
class SystemBarsController private constructor(private val controller: UIViewController) {

    companion object {

        /**
         * Create a new [SystemBarsController] from [AppComponentUIViewController].
         *
         * Creating an [UIViewController] yourself is still in the experimental stage in Kotlin Native,
         * we recommend that you use [AppComponentUIViewController] directly for now.
         *
         * - Note: Currently only [AppComponentUIViewController] is supported as a parameter,
         *   otherwise an exception will be thrown.
         * @see AppComponentUIViewController
         * @param controller the current UI view controller.
         * @throws IllegalArgumentException if the [controller] is not [AppComponentUIViewController].
         */
        fun from(controller: UIViewController): SystemBarsController {
            if (controller !is AppComponentUIViewController) error("Only AppComponentUIViewController is supported for now.")
            return SystemBarsController(controller)
        }
    }

    /** Indicates init only once times. */
    private var isInitOnce = false

    /**
     * Get the system bar views. (included status bars, home indicator)
     * @return [Array]<[SystemBarView]>
     */
    private val systemBarViews by lazy { arrayOf(SystemBarView(), SystemBarView()) }

    /** The behavior of system bars. */
    private var currentBehavior = SystemBarBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES

    /**
     * Returns the current [AppComponentUIViewController].
     * @return [AppComponentUIViewController]
     */
    private val appController get() = controller as AppComponentUIViewController

    /**
     * System bar stub view. (a placeholder layout)
     *
     * iOS does not have a system bars background color function,
     * so we use this stub view to simulate background color related functions.
     */
    private class SystemBarView {

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

    /** Initialize the system bars defaults. */
    private fun initializeDefaults() {
        behavior = currentBehavior
        setStyle(SystemBarStyle.AutoTransparent)
    }

    /** Refresh the behavior of system bars. */
    private fun refreshBehavior() {
        appController.screenEdgesDeferringSystemGestures = when (currentBehavior) {
            SystemBarBehavior.DEFAULT -> UIRectEdgeNone
            SystemBarBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES ->
                if (appController.isStatusBarHidden) UIRectEdgeAll else UIRectEdgeNone
        }
    }

    /**
     * Update the system bar views height.
     * @param insets the insets padding.
     */
    private fun updateSystemBarViewsHeight(insets: UIEdgeInsetsWrapper?) {
        insets ?: return
        systemBarViews[0].updateHeight(insets.top)
        systemBarViews[1].updateHeight(insets.bottom)
    }

    /** Bind the system bar views to the current view controller. */
    private fun bindSystemBarViews() {
        controller.view.addSubview(systemBarViews[0].current)
        controller.view.addSubview(systemBarViews[1].current)
        NSLayoutConstraint.activateConstraints(systemBarViews[0].createConstraints(controller.view, Direction.TOP))
        NSLayoutConstraint.activateConstraints(systemBarViews[1].createConstraints(controller.view, Direction.BOTTOM))
    }

    /**
     * Get the current [SystemBarsController]'s status.
     * @return [Boolean]
     */
    val isDestroyed get() = !isInitOnce

    /**
     * Initialize [SystemBarsController].
     *
     * - The initialization operation will not be called repeatedly,
     *   repeated calls will only be performed once.
     */
    fun init() {
        if (isInitOnce) return
        isInitOnce = true
        bindSystemBarViews()
        initializeDefaults()
        appController.onViewDidLayoutSubviewsCallback = {
            val safeAreaInsets = UIEdgeInsetsWrapper.from(controller.view.safeAreaInsets)
            updateSystemBarViewsHeight(safeAreaInsets)
        }
    }

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [SystemBarBehavior.SCREEN_EDGES_DEFERRING_SYSTEM_GESTURES].
     * @return [SystemBarBehavior]
     */
    var behavior
        get() = currentBehavior
        set(value) {
            currentBehavior = value
            refreshBehavior()
        }

    /**
     * Show system bars.
     * @param type the system bars type.
     */
    fun show(type: SystemBars) {
        when (type) {
            SystemBars.ALL -> {
                appController.isStatusBarHidden = false
                appController.isHomeIndicatorAutoHidden = false
            }
            SystemBars.STATUS_BARS -> appController.isStatusBarHidden = false
            SystemBars.HOME_INDICATOR -> appController.isHomeIndicatorAutoHidden = false
        }; refreshBehavior()
    }

    /**
     * Hide system bars.
     * @param type the system bars type.
     */
    fun hide(type: SystemBars) {
        when (type) {
            SystemBars.ALL -> {
                appController.isStatusBarHidden = true
                appController.isHomeIndicatorAutoHidden = true
            }
            SystemBars.STATUS_BARS -> appController.isStatusBarHidden = true
            SystemBars.HOME_INDICATOR -> appController.isHomeIndicatorAutoHidden = true
        }; refreshBehavior()
    }

    /**
     * Get or set the style of status bars.
     * @see SystemBarStyle
     * @see setStyle
     * @return [SystemBarStyle]
     */
    var statusBarStyle = SystemBarStyle.AutoTransparent
        set(value) {
            field = value
            applyStyle(SystemBars.STATUS_BARS, value)
        }

    /**
     * Get or set the style of home indicator.
     * @see SystemBarStyle
     * @see setStyle
     * @return [SystemBarStyle]
     */
    var homeIndicatorStyle = SystemBarStyle.AutoTransparent
        set(value) {
            field = value
            applyStyle(SystemBars.HOME_INDICATOR, value)
        }

    /**
     * Set the style of system bars.
     *
     * You can also use the [statusBarStyle] and [homeIndicatorStyle].
     * @see SystemBarStyle
     * @see statusBarStyle
     * @see homeIndicatorStyle
     * @param style the system bars style.
     */
    fun setStyle(style: SystemBarStyle) = setStyle(style, style)

    /**
     * Set the style of system bars.
     *
     * You can also use the [statusBarStyle] and [homeIndicatorStyle].
     * @see SystemBarStyle
     * @see statusBarStyle
     * @see homeIndicatorStyle
     * @param statusBar the status bars style.
     * @param homeIndicator the home indicator style.
     */
    fun setStyle(
        statusBar: SystemBarStyle = statusBarStyle,
        homeIndicator: SystemBarStyle = homeIndicatorStyle
    ) {
        statusBarStyle = statusBar
        homeIndicatorStyle = homeIndicator
    }

    /**
     * Apply the system bars style.
     * @param type the system bars type.
     * @param style the system bars style.
     */
    private fun applyStyle(type: SystemBars, style: SystemBarStyle) {
        val isUiInNightMode = controller.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
        val defaultColor = if (isUiInNightMode) UIColor.blackColor else UIColor.whiteColor
        val backgroundColor = style.color ?: defaultColor
        when (type) {
            SystemBars.STATUS_BARS -> {
                appController.statusBarStyle = when (style.darkContent) {
                    null -> UIStatusBarStyleDefault
                    true -> UIStatusBarStyleDarkContent
                    else -> UIStatusBarStyleLightContent
                }; systemBarViews[0].setBackgroundColor(backgroundColor)
            }
            // Home indicator is not supported change its content color.
            SystemBars.HOME_INDICATOR -> systemBarViews[1].setBackgroundColor(backgroundColor)
            else -> {}
        }
    }
}
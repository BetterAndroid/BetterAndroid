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

package com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller

import com.highcapable.betterandroid.compose.extension.cinterop.UIViewControllerWithOverridesProtocol
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.SystemBarsController
import com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller.wrapper.UIEdgeInsetsWrapper
import kotlinx.cinterop.useContents
import platform.Foundation.NSCoder
import platform.UIKit.UIRectEdgeNone
import platform.UIKit.UIStatusBarStyleDefault
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.setNeedsUpdateOfHomeIndicatorAutoHidden
import platform.UIKit.setNeedsUpdateOfScreenEdgesDeferringSystemGestures

/**
 * App component UI view controller.
 *
 * Inherited from [UIViewController].
 */
class AppComponentUIViewController : UIViewController, UIViewControllerWithOverridesProtocol {

    companion object {

        /**
         * Create a new [AppComponentUIViewController] from another [UIViewController].
         * @param controller another controller.
         * @return [AppComponentUIViewController]
         */
        fun from(controller: UIViewController) = AppComponentUIViewController().apply { childController = controller }

        /**
         * Create a new [AppComponentUIViewController] from [UIView].
         * @param view the view.
         * @return [AppComponentUIViewController]
         */
        fun from(view: UIView) = AppComponentUIViewController().apply { childView = view }
    }

    /** The child controller. */
    private var childController: UIViewController? = null

    /** The child view. */
    private var childView: UIView? = null

    @OverrideInit
    constructor() : super(nibName = null, bundle = null)

    @OverrideInit
    constructor(coder: NSCoder) : super(coder)

    /**
     * Get the system bars controller.
     * @retrun [SystemBarsController]
     */
    internal val systemBars by lazy { SystemBarsController.from(controller = this) }

    /** The safe area insets of [view] changed callback. */
    internal var onSafeAreaInsetsChangedCallback: ((UIEdgeInsetsWrapper) -> Unit)? = null

    /** Whether the status bar is hidden. */
    internal var isStatusBarHidden = false
        set(value) {
            field = value
            setNeedsStatusBarAppearanceUpdate()
        }

    /** Whether the home indicator is auto hidden. */
    internal var isHomeIndicatorAutoHidden = false
        set(value) {
            field = value
            setNeedsUpdateOfHomeIndicatorAutoHidden()
        }

    /** The screen edges deferring system gestures. */
    internal var screenEdgesDeferringSystemGestures = UIRectEdgeNone
        set(value) {
            field = value
            setNeedsUpdateOfScreenEdgesDeferringSystemGestures()
        }

    /** The status bar style. */
    internal var statusBarStyle = UIStatusBarStyleDefault
        set(value) {
            field = value
            setNeedsStatusBarAppearanceUpdate()
        }

    override fun prefersStatusBarHidden() = isStatusBarHidden
    override fun prefersHomeIndicatorAutoHidden() = isHomeIndicatorAutoHidden
    override fun preferredStatusBarStyle() = statusBarStyle
    override fun preferredScreenEdgesDeferringSystemGestures() = screenEdgesDeferringSystemGestures

    override fun loadView() {
        super.loadView()
        if (childController != null && childView != null)
            error("Only one of child controller and child view can be set for AppComponentUIViewController.")
        childController?.also { loadViewFromChildController(it) }
        childView?.also { loadViewFromChildView(it) }
    }

    /**
     * Load view from child controller.
     * @param childController the child controller.
     */
    private fun loadViewFromChildController(childController: UIViewController) {
        val rootView = UIView()
        addChildViewController(childController)
        rootView.addSubview(childController.view)
        rootView.setAutoresizesSubviews(true)
        childController.view.setFlexibleAutoresizingMask()
        view = rootView
        // Move to parent view controller.
        childController.didMoveToParentViewController(this)
        systemBars.init()
    }

    /**
     * Load view from child view.
     * @param childView the child view.
     */
    private fun loadViewFromChildView(childView: UIView) {
        val rootView = UIView()
        rootView.addSubview(childView)
        rootView.setAutoresizesSubviews(true)
        childView.setFlexibleAutoresizingMask()
        view = rootView
        systemBars.init()
    }

    /**
     * Set the auto resizing mask of flexible width and height.
     * @receiver [UIView]
     */
    private fun UIView.setFlexibleAutoresizingMask() =
        setAutoresizingMask(UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight)

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        val safeAreaInsets = view.safeAreaInsets.useContents { UIEdgeInsetsWrapper.from(insets = this) }
        onSafeAreaInsetsChangedCallback?.invoke(safeAreaInsets)
    }
}
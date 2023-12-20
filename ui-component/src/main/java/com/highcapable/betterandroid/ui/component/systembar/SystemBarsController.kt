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
 * This file is created by fankes on 2022/10/12.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "SwitchIntDef")

package com.highcapable.betterandroid.ui.component.systembar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.setPadding
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.activity.AppBindingActivity
import com.highcapable.betterandroid.ui.component.activity.AppComponentActivity
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.component.fragment.AppBindingFragment
import com.highcapable.betterandroid.ui.component.fragment.AppViewsFragment
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.insets.InsetsWrapper
import com.highcapable.betterandroid.ui.component.insets.WindowInsetsWrapper
import com.highcapable.betterandroid.ui.component.insets.factory.createRootWindowInsetsWrapper
import com.highcapable.betterandroid.ui.component.insets.factory.handleOnWindowInsetsChanged
import com.highcapable.betterandroid.ui.component.insets.factory.setInsetsPadding
import com.highcapable.betterandroid.ui.component.insets.factory.updateInsetsPadding
import com.highcapable.betterandroid.ui.component.systembar.compat.SystemBarsCompat
import com.highcapable.betterandroid.ui.component.systembar.style.SystemBarStyle
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBarsBehavior
import com.highcapable.betterandroid.ui.extension.component.base.isUiInNightMode
import com.highcapable.betterandroid.ui.extension.graphics.isBrightColor
import com.highcapable.betterandroid.ui.extension.graphics.mixColorOf
import android.R as Android_R

/**
 * System bars controller.
 *
 * This is a controller with the ability to globally manage system bars.
 * @param activity the current activity.
 */
class SystemBarsController private constructor(private val activity: Activity) {

    companion object {

        /**
         * Create a new [SystemBarsController] from [activity].
         *
         * Usage:
         *
         * ```kotlin
         * class YourActivity : AppCompatActivity() {
         *
         *     val systemBars by lazy { SystemBarsController.from(this) }
         *
         *     override fun onCreate(savedInstanceState: Bundle?) {
         *         super.onCreate(savedInstanceState)
         *         // Create your binding.
         *         val binding = ActivityMainBinding.inflate(layoutInflater)
         *         setContentView(binding.root)
         *         // Initialize the system bars controller.
         *         systemBars.init(binding.root)
         *     }
         *
         *     override fun onDestroy() {
         *         super.onDestroy()
         *         // Destroy the system bars controller.
         *         // Optional, prevent memory leaks.
         *         systemBars.destroy()
         *     }
         * }
         * ```
         * Or you can inherit the related activities or fragments.
         * @see AppBindingActivity
         * @see AppViewsActivity
         * @see AppComponentActivity
         * @see AppBindingFragment
         * @see AppViewsFragment
         * @param activity the current activity.
         * @return [SystemBarsController]
         */
        @JvmStatic
        fun from(activity: Activity) = SystemBarsController(activity)

        /**
         * Create a new [SystemBarsController] from [activity].
         *
         * - This function is deprecated, use [from] instead.
         *
         * - The root view's initialization timing has been changed.
         * @see from
         */
        @Suppress("UNUSED_PARAMETER")
        @Deprecated(message = "Use from instead.", ReplaceWith("from(activity)"))
        @JvmStatic
        fun from(activity: Activity, rootView: View? = null) = from(activity)

        /**
         * Create a new [AbsoluteController].
         *
         * - This function is deprecated and no effect, use [WindowInsetsWrapper.Absolute] instead.
         * @see WindowInsetsWrapper.Absolute
         */
        @Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
        @Deprecated(message = "Use WindowInsetsWrapper.Absolute instead.")
        @JvmStatic
        fun createAbsolute(context: Context) = AbsoluteController(context)
    }

    /**
     * System bar params definition.
     */
    private data class SystemBarParams(
        val statusBarColor: Int,
        val navigationBarColor: Int,
        val isStatusBarContrastEnforced: Boolean,
        val isNavigationBarContrastEnforced: Boolean,
        val isAppearanceLightStatusBars: Boolean,
        val isAppearanceLightNavigationBars: Boolean
    )

    /** Indicates init only once times. */
    private var isInitOnce = false

    /** The original system bar params. */
    private var originalSystemBarParams: SystemBarParams? = null

    /** The root view. */
    private var rootView: View? = null

    /** The callback of root view padding function. */
    private var rootPaddingCallback: (() -> Unit)? = null

    /** The current window insets wrapper of [parentView]. */
    private var parentInsetsWrapper: WindowInsetsWrapper? = null

    /** The current window insets controller of [parentView]. */
    private var parentInsetsController: WindowInsetsControllerCompat? = null

    /**
     * Get the [rootView]'s parent.
     * @return [View] or null.
     */
    private val parentView get() = rootView?.parent as? View?

    /**
     * Get the current system bars compat instance.
     * @return [SystemBarsCompat]
     */
    private val systemBarsCompat by lazy { SystemBarsCompat(activity) }

    /**
     * Get the system bars behavior type.
     * @receiver [WindowInsetsControllerCompat]
     * @return [SystemBarsBehavior]
     */
    private val WindowInsetsControllerCompat.systemBarBehaviorType get() = when (systemBarsBehavior) {
        WindowInsetsControllerCompat.BEHAVIOR_DEFAULT -> SystemBarsBehavior.DEFAULT
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE -> SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
        else -> SystemBarsBehavior.DEFAULT
    }

    /**
     * Convert to [WindowInsetsControllerCompat] types.
     * @receiver the [SystemBarsBehavior] type.
     * @return [Int]
     */
    private fun SystemBarsBehavior.toBehaviorType() = when (this) {
        SystemBarsBehavior.DEFAULT -> WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE -> WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    /**
     * Convert [SystemBars] to [WindowInsetsCompat.Type].
     * @receiver [SystemBars]
     * @return [Int]
     */
    private fun SystemBars.toInsetsType() = when (this) {
        SystemBars.ALL -> WindowInsetsCompat.Type.systemBars()
        SystemBars.STATUS_BARS -> WindowInsetsCompat.Type.statusBars()
        SystemBars.NAVIGATION_BARS -> WindowInsetsCompat.Type.navigationBars()
    }

    /** Initialize the system bars defaults. */
    private fun initializeDefaults() {
        behavior = SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
        setStyle(SystemBarStyle.AutoTransparent, SystemBarStyle.AutoTransparent)
    }

    /** Create the default [rootView]'s padding callback. */
    private fun createDefaultRootPaddingCallback() {
        withRootPadding(autoUpdate = false) { rootView?.setInsetsPadding(it.safeDrawingIgnoringIme) }
    }

    /**
     * With the [rootView]'s padding function block.
     * @param autoUpdate whether to update the padding, default true.
     * @param callback the callback function.
     */
    private fun withRootPadding(autoUpdate: Boolean = true, callback: (WindowInsetsWrapper) -> Unit) {
        rootPaddingCallback = { parentInsetsWrapper?.also(callback) }
        if (autoUpdate) updateRootPadding()
    }

    /** Update the [rootView]'s padding. */
    private fun updateRootPadding() {
        rootPaddingCallback?.invoke()
    }

    /** Handle and make the [parentView] window insets available. */
    private fun handleOnParentWindowInsetsChanged() {
        /**
         * Do on parent window insets changed.
         * @param insetsWrapper the window insets wrapper.
         */
        fun onChange(insetsWrapper: WindowInsetsWrapper?) {
            insetsWrapper ?: return
            parentInsetsWrapper = insetsWrapper
            updateRootPadding()
        }
        onChange(parentView?.createRootWindowInsetsWrapper(activity.window))
        parentView?.handleOnWindowInsetsChanged { _, insetsWrapper ->
            onChange(insetsWrapper)
            // In here, we won't consumed the insets for child views,
            // so you can use the [View.handleOnWindowInsetsChanged] to handle the child views insets.
            false
        }
    }

    /**
     * Get the current [SystemBarsController]'s status.
     * @return [Boolean]
     */
    val isDestroyed get() = !isInitOnce

    /**
     * Initialize [SystemBarsController].
     *
     * It is recommended to use this function in [Activity.onCreate].
     *
     * You can use [destroy] to destroy this [SystemBarsController].
     *
     * - The initialization operation will not be called repeatedly,
     *   repeated calls will only be performed once.
     * @see destroy
     * @param rootView the root view (must have a parent), default is [Android_R.id.content].
     * @param defaultPadding whether to initialize the default window insets padding to [rootView], default true.
     * @throws IllegalStateException if the [rootView] is not available.
     */
    fun init(rootView: View? = null, defaultPadding: Boolean = true) {
        if (isInitOnce) return
        var throwable: Throwable? = null
        val absRootView = rootView ?: runCatching { activity.findViewById<ViewGroup>(Android_R.id.content) }.onFailure {
            throwable = it
            Log.e(BetterAndroidProperties.PROJECT_NAME, "Failed to get the rootView from android.R.id.content.", it)
        }.getOrNull() ?: error(
            "The root view is not available at this stage, " +
                "please set the rootView parameter manually and init the SystemBarsController in Activity's onCreate method." +
                (throwable?.message?.let { "\nCaused by: $it" } ?: "")
        )
        require(absRootView is ViewGroup) { "The rootView $absRootView must inherit from a ViewGroup." }
        requireNotNull(absRootView.parent) { "The rootView $absRootView must have a parent." }
        isInitOnce = true
        this.rootView = absRootView
        parentInsetsController = parentView?.let { WindowCompat.getInsetsController(activity.window, it) }
        var isStatusBarContrastEnforced = false
        var isNavigationBarContrastEnforced = false
        SystemVersion.require(SystemVersion.Q) {
            isStatusBarContrastEnforced = activity.window?.isStatusBarContrastEnforced == true
            isNavigationBarContrastEnforced = activity.window?.isNavigationBarContrastEnforced == true
            // Remove system-made masking of forced contrast colors.
            activity.window?.isStatusBarContrastEnforced = false
            activity.window?.isNavigationBarContrastEnforced = false
        }
        // Save the original system bars params.
        originalSystemBarParams = SystemBarParams(
            statusBarColor = activity.window?.statusBarColor ?: Color.TRANSPARENT,
            navigationBarColor = activity.window?.navigationBarColor ?: Color.TRANSPARENT,
            isStatusBarContrastEnforced = isStatusBarContrastEnforced,
            isNavigationBarContrastEnforced = isNavigationBarContrastEnforced,
            isAppearanceLightStatusBars = parentInsetsController?.isAppearanceLightStatusBars == true,
            isAppearanceLightNavigationBars = parentInsetsController?.isAppearanceLightNavigationBars == true
        )
        // Set the layout overlay to status bars and navigation bars.
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        SystemVersion.require(SystemVersion.P) {
            // Set the notch area not to interfere with the current UI.
            activity.window?.attributes?.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            // Remove the color of the navigation bars divider.
            activity.window?.navigationBarDividerColor = Color.TRANSPARENT
        }
        initializeDefaults()
        if (defaultPadding) createDefaultRootPaddingCallback()
        handleOnParentWindowInsetsChanged()
    }

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE].
     * @return [SystemBarsBehavior]
     */
    var behavior
        get() = parentInsetsController?.systemBarBehaviorType ?: SystemBarsBehavior.DEFAULT
        set(value) {
            parentInsetsController?.systemBarsBehavior = value.toBehaviorType()
        }

    /**
     * Set the [rootView]'s padding with window insets.
     *
     * This function will be re-called when the window insets changes.
     *
     * You can use [removeRootInsetsPadding] to remove the [rootView]'s padding.
     *
     * Usage:
     *
     * ```kotlin
     * // Set all padding by safeDrawingIgnoringIme.
     * systemBars.setRootInsetsPadding(insets = { safeDrawingIgnoringIme })
     * // Set all padding by systemBars.
     * systemBars.setRootInsetsPadding(insets = { systemBars })
     * ```
     * @see WindowInsetsWrapper
     * @see updateRootInsetsPadding
     * @see removeRootInsetsPadding
     * @see View.handleOnWindowInsetsChanged
     * @see View.setInsetsPadding
     * @param insets the insets wrapper callback.
     * @param left whether set the left padding.
     * @param top whether set the top padding.
     * @param right whether set the right padding.
     * @param bottom whether set the bottom padding.
     */
    @JvmOverloads
    fun setRootInsetsPadding(
        insets: WindowInsetsWrapper.() -> InsetsWrapper,
        left: Boolean = true,
        top: Boolean = true,
        right: Boolean = true,
        bottom: Boolean = true
    ) = withRootPadding { rootView?.setInsetsPadding(insets(it), left, top, right, bottom) }

    /**
     * Set the [rootView]'s padding with window insets.
     *
     * This function will be re-called when the window insets changes.
     *
     * You can use [removeRootInsetsPadding] to remove the [rootView]'s padding.
     *
     * Usage:
     *
     * ```kotlin
     * // Set all padding by safeDrawingIgnoringIme.
     * systemBars.setRootInsetsPadding(insets = { safeDrawingIgnoringIme })
     * // Set all padding by systemBars.
     * systemBars.setRootInsetsPadding(insets = { systemBars })
     * ```
     * @see WindowInsetsWrapper
     * @see updateRootInsetsPadding
     * @see removeRootInsetsPadding
     * @see View.handleOnWindowInsetsChanged
     * @see View.setInsetsPadding
     * @param insets the insets wrapper callback.
     * @param horizontal whether set the horizontal padding.
     * @param vertical whether set the vertical padding.
     */
    @JvmOverloads
    @JvmName("setRootHVInsetsPadding")
    fun setRootInsetsPadding(
        insets: WindowInsetsWrapper.() -> InsetsWrapper,
        horizontal: Boolean = true,
        vertical: Boolean = true
    ) = withRootPadding { rootView?.setInsetsPadding(insets(it), horizontal, vertical) }

    /**
     * Update the [rootView]'s padding with window insets.
     *
     * This function will be re-called when the window insets changes.
     *
     * You can use [removeRootInsetsPadding] to remove the [rootView]'s padding.
     *
     * Usage:
     *
     * ```kotlin
     * // Update top padding by safeDrawingIgnoringIme.
     * systemBars.updateRootInsetsPadding(insets = { safeDrawingIgnoringIme }, top = true)
     * // Update bottom padding by systemBars.
     * systemBars.updateRootInsetsPadding(insets = { systemBars }, bottom = true)
     * ```
     * @see WindowInsetsWrapper
     * @see setRootInsetsPadding
     * @see removeRootInsetsPadding
     * @see View.handleOnWindowInsetsChanged
     * @see View.updateInsetsPadding
     * @param insets the insets wrapper callback.
     * @param left whether update the left padding.
     * @param top whether update the top padding.
     * @param right whether update the right padding.
     * @param bottom whether update the bottom padding.
     */
    @JvmOverloads
    fun updateRootInsetsPadding(
        insets: WindowInsetsWrapper.() -> InsetsWrapper,
        left: Boolean = false,
        top: Boolean = false,
        right: Boolean = false,
        bottom: Boolean = false
    ) = withRootPadding { rootView?.updateInsetsPadding(insets(it), left, top, right, bottom) }

    /**
     * Update the [rootView]'s padding with window insets.
     *
     * This function will be re-called when the window insets changes.
     *
     * You can use [removeRootInsetsPadding] to remove the [rootView]'s padding.
     *
     * Usage:
     *
     * ```kotlin
     * // Update horizontal padding by safeDrawingIgnoringIme.
     * systemBars.updateRootInsetsPadding(insets = { safeDrawingIgnoringIme }, horizontal = true)
     * // Update vertical padding by systemBars.
     * systemBars.updateRootInsetsPadding(insets = { systemBars }, vertical = true)
     * ```
     * @see WindowInsetsWrapper
     * @see setRootInsetsPadding
     * @see removeRootInsetsPadding
     * @see View.handleOnWindowInsetsChanged
     * @see View.updateInsetsPadding
     * @param insets the insets wrapper callback.
     * @param horizontal whether update the horizontal padding.
     * @param vertical whether update the vertical padding.
     */
    @JvmOverloads
    @JvmName("updateRootHVInsetsPadding")
    fun updateRootInsetsPadding(
        insets: WindowInsetsWrapper.() -> InsetsWrapper,
        horizontal: Boolean = false,
        vertical: Boolean = false
    ) = withRootPadding { rootView?.updateInsetsPadding(insets(it), horizontal, vertical) }

    /**
     * Remove the [rootView]'s padding and callback.
     *
     * - Note: If you init your own [rootView] in [init] and set the defaultPadding to true
     *   or you used [setRootInsetsPadding] or [updateRootInsetsPadding],
     *   be sure to use this function to remove the padding and callback for it,
     *   because the padding of [rootView] has been taken over by [SystemBarsController] before,
     *   your manual modification operations will not work.
     */
    fun removeRootInsetsPadding() {
        // If the window insets is not applied to the root view,
        // we should not change the root view's padding.
        if (rootPaddingCallback == null) return
        rootPaddingCallback = null
        rootView?.setPadding(0)
        rootView?.requestLayout()
    }

    /**
     * Show system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @see behavior
     * @param type the system bars type.
     */
    fun show(type: SystemBars) {
        parentInsetsController?.show(type.toInsetsType())
    }

    /**
     * Hide system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @see behavior
     * @param type the system bars type.
     */
    fun hide(type: SystemBars) {
        parentInsetsController?.hide(type.toInsetsType())
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    fun isVisible(type: SystemBars) = when (type) {
        SystemBars.ALL -> parentInsetsWrapper?.systemBars?.isVisible == true
        SystemBars.STATUS_BARS -> parentInsetsWrapper?.statusBars?.isVisible == true
        SystemBars.NAVIGATION_BARS -> parentInsetsWrapper?.navigationBars?.isVisible == true
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
     * Get or set the style of navigation bars.
     * @see SystemBarStyle
     * @see setStyle
     * @return [SystemBarStyle]
     */
    var navigationBarStyle = SystemBarStyle.AutoTransparent
        set(value) {
            field = value
            applyStyle(SystemBars.NAVIGATION_BARS, value)
        }

    /**
     * Set the style of system bars.
     *
     * You can also use the [statusBarStyle] and [navigationBarStyle].
     * @see SystemBarStyle
     * @see statusBarStyle
     * @see navigationBarStyle
     * @param statusBar the status bars style.
     * @param navigationBar the navigation bars style.
     */
    @JvmOverloads
    fun setStyle(
        statusBar: SystemBarStyle = statusBarStyle,
        navigationBar: SystemBarStyle = navigationBarStyle
    ) {
        statusBarStyle = statusBar
        navigationBarStyle = navigationBar
    }

    /**
     * Apply the system bars style.
     * @param type the system bars type.
     * @param style the system bars style.
     */
    private fun applyStyle(type: SystemBars, style: SystemBarStyle) {
        val isUiInNightMode = activity.resources.configuration.isUiInNightMode
        val defaultColor = if (isUiInNightMode) Color.BLACK else Color.WHITE
        val backgroundColor = style.color ?: defaultColor
        val darkContent = style.darkContent ?: !isUiInNightMode
        val lightApperance = darkContent && backgroundColor == Color.TRANSPARENT || backgroundColor.isBrightColor
        when (type) {
            SystemBars.STATUS_BARS -> {
                enableDrawsSystemBarBackgrounds()
                // Below Android 6.0 will add a translucent mask,
                // as the system does not support inverting colors.
                // Some systems, such as MIUI based on Android 5,
                // will automatically adapt to their own set of inverse color schemes.
                activity.window?.statusBarColor =
                    if (SystemVersion.isLowTo(SystemVersion.M) && !systemBarsCompat.isLegacyMiui && lightApperance)
                        mixColorOf(backgroundColor, Color.BLACK)
                    else backgroundColor
                if (systemBarsCompat.isLegacyMiui) systemBarsCompat.setStatusBarDarkModeForLegacyMiui(darkContent)
                else parentInsetsController?.isAppearanceLightStatusBars = darkContent
            }
            SystemBars.NAVIGATION_BARS -> {
                enableDrawsSystemBarBackgrounds()
                // Below Android 8.0 will add a transparent mask,
                // because the system does not support inverting colors.
                activity.window?.navigationBarColor =
                    if (SystemVersion.isLowTo(SystemVersion.O) && lightApperance)
                        mixColorOf(backgroundColor, Color.BLACK)
                    else backgroundColor
                parentInsetsController?.isAppearanceLightNavigationBars = darkContent
            }
            else -> {}
        }
    }

    /** Enable to draws system bar backgrounds if not. */
    private fun enableDrawsSystemBarBackgrounds() {
        @Suppress("DEPRECATION")
        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        @Suppress("DEPRECATION")
        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }

    /**
     * Destroy the current [SystemBarsController].
     *
     * You can call [init] again to re-initialize the [SystemBarsController].
     *
     * - The destroy operation will not be called repeatedly,
     *   repeated calls will only be performed once.
     * @see init
     */
    fun destroy() {
        if (!isInitOnce || rootView == null) return
        // Restore to default sets.
        originalSystemBarParams?.also {
            WindowCompat.setDecorFitsSystemWindows(activity.window, true)
            activity.window?.statusBarColor = it.statusBarColor
            activity.window?.navigationBarColor = it.navigationBarColor
            SystemVersion.require(SystemVersion.Q) {
                activity.window?.isStatusBarContrastEnforced = it.isStatusBarContrastEnforced
                activity.window?.isNavigationBarContrastEnforced = it.isNavigationBarContrastEnforced
            }
            parentInsetsController?.isAppearanceLightStatusBars = it.isAppearanceLightStatusBars
            parentInsetsController?.isAppearanceLightNavigationBars = it.isAppearanceLightNavigationBars
        }
        removeRootInsetsPadding()
        parentInsetsWrapper = null
        parentInsetsController = null
        rootView = null
        isInitOnce = false
    }

    // The following functions are deprecated, you should not use them.

    /**
     * System bars absolute controller.
     *
     * - This class is deprecated and no effect, use [WindowInsetsWrapper.Absolute] instead.
     * @see WindowInsetsWrapper.Absolute
     */
    @Deprecated(message = "Use WindowInsetsWrapper.Absolute instead.")
    class AbsoluteController internal constructor(private val context: Context) {

        /**
         * Get the status bar height (px).
         *
         * - This class is deprecated and no effect, use [WindowInsetsWrapper.Absolute] instead.
         * @see WindowInsetsWrapper.Absolute
         */
        @Suppress("DeprecatedCallableAddReplaceWith")
        @Deprecated(message = "Use WindowInsetsWrapper.Absolute instead.")
        val statusBarHeight @Px get() = 0

        /**
         * Get the navigation bar height (px).
         *
         * - This class is deprecated and no effect, use [WindowInsetsWrapper.Absolute] instead.
         * @see WindowInsetsWrapper.Absolute
         */
        @Suppress("DeprecatedCallableAddReplaceWith")
        @Deprecated(message = "Use WindowInsetsWrapper.Absolute instead.")
        val navigationBarHeight @Px get() = 0
    }

    /**
     * Get the base layout.
     *
     * - This function is deprecated and no effect,
     *   because the root view is controlled by yourself now.
     *
     * - You should avoid using the default [Android_R.id.content] to init the root view.
     */
    @Deprecated(message = "No longer needed, the root view is controlled by yourself.")
    val baseLayout get() = rootView

    /**
     * The current system bars insets.
     *
     * - This property is deprecated and no effect, see the [addOnInsetsChangeListener] for more details.
     * @see addOnInsetsChangeListener
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "No effect.")
    val systemInsets get() = Any()

    /**
     * Initialize [SystemBarsController].
     *
     * - This function is deprecated and no effect, use [init] instead.
     *
     * - The root view's initialization timing has been changed.
     * @see init
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use init instead.")
    fun init(defaultPaddings: Boolean = true) {
    }

    /**
     * - This function is deprecated and no effect,
     *   because the root view is controlled by yourself now.
     *
     * - You should avoid using the default [Android_R.id.content] to init the root view.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "No longer needed, the root view is controlled by yourself.")
    fun setBaseBackground(background: Drawable?) {
    }

    /**
     * - This function is deprecated and no effect,
     *   because the root view is controlled by yourself now.
     *
     * - You should avoid using the default [Android_R.id.content] to init the root view.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "No longer needed, the root view is controlled by yourself.")
    fun setBaseBackgroundColor(@ColorInt color: Int) {
    }

    /**
     * - This function is deprecated and no effect,
     *   because the root view is controlled by yourself now.
     *
     * - You should avoid using the default [Android_R.id.content] to init the root view.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "No longer needed, the root view is controlled by yourself.")
    fun setBaseBackgroundResource(@DrawableRes resId: Int) {
    }

    /**
     * Add the system bars insets changes listener.
     *
     * - This function is deprecated and no effect, if you want to add your own insets changes listener,
     *   please use the [View.handleOnWindowInsetsChanged] to your view,
     *   [SystemBarsController] will not consume the insets for child views.
     * @see View.handleOnWindowInsetsChanged
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
    fun addOnInsetsChangeListener(onChange: (Any) -> Unit) {
    }

    /**
     * Show system bars.
     *
     * - This function is deprecated, use [show] instead.
     * @see show
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use show instead.", ReplaceWith("show(type)"))
    fun show(type: SystemBars, appendExtraPaddings: Boolean = false, ignoredCutout: Boolean = false) = show(type)

    /**
     * Hide system bars.
     *
     * - This function is deprecated, use [hide] instead.
     * @see hide
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use hide instead.", ReplaceWith("hide(type)"))
    fun hide(type: SystemBars, removeExtraPaddings: Boolean = false, ignoredCutout: Boolean = false) = hide(type)

    /**
     * Apply the system bars extra paddings.
     *
     * - This function is deprecated and no effect, use [setRootInsetsPadding] instead.
     * @see setRootInsetsPadding
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use setRootInsetsPadding instead.")
    @JvmOverloads
    fun applyExtraPaddings(vararg types: Any, ignoredCutout: Boolean = false) {
    }

    /**
     * Append the system bars extra paddings.
     *
     * - This function is deprecated and no effect, use [updateRootInsetsPadding] instead.
     * @see updateRootInsetsPadding
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use updateRootInsetsPadding instead.")
    @JvmOverloads
    fun appendExtraPaddings(vararg types: Any, ignoredCutout: Boolean = false) {
    }

    /**
     * Remove the system bars extra paddings.
     *
     * - This function is deprecated, use [removeRootInsetsPadding] instead.
     * @see removeRootInsetsPadding
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use removeRootInsetsPadding instead.", ReplaceWith("removeRootInsetsPadding()"))
    @JvmOverloads
    fun removeExtraPaddings(vararg types: Any, ignoredCutout: Boolean = false) = removeRootInsetsPadding()

    /**
     * Show the system bars stub views.
     *
     * - This function is deprecated and no effect, use [setRootInsetsPadding] or [updateRootInsetsPadding] instead.
     * @see setRootInsetsPadding
     * @see updateRootInsetsPadding
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use setRootInsetsPadding or updateRootInsetsPadding instead.")
    fun showStub(type: SystemBars) {
    }

    /**
     * Hide the system bars stub views.
     *
     * - This function is deprecated and no effect, use [removeRootInsetsPadding] instead.
     * @see removeRootInsetsPadding
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use removeBaseInsetsPadding instead.")
    @JvmOverloads
    fun hideStub(type: SystemBars, ignoredCutout: Boolean = false) {
    }

    /**
     * Set the system bars background color.
     *
     * - This function is deprecated and no effect, use [statusBarStyle], [navigationBarStyle], [setStyle] instead.
     * @see statusBarStyle
     * @see navigationBarStyle
     * @see setStyle
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use statusBarStyle or navigationBarStyle or setStyle instead.")
    fun setColor(type: SystemBars, @ColorInt color: Int) {
    }

    /**
     * Get or set the dark elements tint color (light appearance) of status bars.
     *
     * - This function is deprecated and no effect, use [statusBarStyle], [setStyle] instead.
     * @see statusBarStyle
     * @see setStyle
     */
    @Deprecated(message = "Use statusBarStyle or setStyle instead.")
    var isDarkColorStatusBars = false

    /**
     * Get or set the dark elements tint color (light appearance) of navigation bars.
     *
     * - This function is deprecated and no effect, use [navigationBarStyle], [setStyle] instead.
     * @see navigationBarStyle
     * @see setStyle
     */
    @Deprecated(message = "Use navigationBarStyle or setStyle instead.")
    var isDarkColorNavigationBars = false

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     *
     * - This function is deprecated and no effect, use [statusBarStyle], [navigationBarStyle], [setStyle] instead.
     * @see statusBarStyle
     * @see navigationBarStyle
     * @see setStyle
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use statusBarStyle or navigationBarStyle or setStyle instead.")
    fun adaptiveAppearance(color: Int) {
    }
}
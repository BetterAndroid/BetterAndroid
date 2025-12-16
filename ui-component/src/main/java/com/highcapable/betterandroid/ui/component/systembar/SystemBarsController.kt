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
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.highcapable.betterandroid.system.extension.utils.AndroidVersion
import com.highcapable.betterandroid.ui.component.activity.AppBindingActivity
import com.highcapable.betterandroid.ui.component.activity.AppComponentActivity
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.component.fragment.AppBindingFragment
import com.highcapable.betterandroid.ui.component.fragment.AppViewsFragment
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController.Companion.from
import com.highcapable.betterandroid.ui.component.systembar.compat.SystemBarsCompat
import com.highcapable.betterandroid.ui.component.systembar.style.SystemBarStyle
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBarBehavior
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars
import com.highcapable.betterandroid.ui.extension.component.base.isUiInNightMode
import com.highcapable.betterandroid.ui.extension.graphics.isBrightColor
import com.highcapable.betterandroid.ui.extension.graphics.mixColorOf
import com.highcapable.betterandroid.ui.extension.insets.InsetsWrapper
import com.highcapable.betterandroid.ui.extension.insets.WindowInsetsWrapper
import com.highcapable.betterandroid.ui.extension.insets.factory.handleOnWindowInsetsChanged
import com.highcapable.betterandroid.ui.extension.insets.factory.removeWindowInsetsListener
import com.highcapable.betterandroid.ui.extension.insets.factory.setInsetsPadding
import com.highcapable.betterandroid.ui.extension.view.updateLayoutParams
import android.R as Android_R

/**
 * System bars controller.
 *
 * This is a controller with the ability to globally manage system bars.
 * @param window the current window.
 */
class SystemBarsController private constructor(private val window: Window) {

    companion object {

        /**
         * Create a new [SystemBarsController] from [window].
         *
         * Usage:
         *
         * ```kotlin
         * class YourActivity : AppCompatActivity() {
         *
         *     val systemBars by lazy { SystemBarsController.from(window) }
         *
         *     override fun onCreate(savedInstanceState: Bundle?) {
         *         super.onCreate(savedInstanceState)
         *
         *         // Create your binding.
         *         val binding = ActivityMainBinding.inflate(layoutInflater)
         *         setContentView(binding.root)
         *         // Initialize the system bars controller.
         *         systemBars.init(binding.root)
         *     }
         *
         *     override fun onDestroy() {
         *         // Destroy the system bars controller.
         *         // Optional, prevent memory leaks.
         *         systemBars.destroy()
         *
         *         super.onDestroy()
         *     }
         * }
         * ```
         * Or you can inherit the related activities or fragments.
         * @see AppBindingActivity
         * @see AppViewsActivity
         * @see AppComponentActivity
         * @see AppBindingFragment
         * @see AppViewsFragment
         * @param window the current window.
         * @return [SystemBarsController]
         */
        @JvmStatic
        fun from(window: Window) = SystemBarsController(window)

        /**
         * Create a new [SystemBarsController] from [activity].
         *
         * - This function is deprecated, use [from] instead.
         *
         * - The root view's initialization timing has been changed.
         * @see from
         */
        @Suppress("UNUSED_PARAMETER")
        @Deprecated(message = "Use from instead.", ReplaceWith("from(activity.window)"))
        @JvmStatic
        fun from(activity: Activity, rootView: View? = null) = from(activity.window)

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
        val navigationBarDividerColor: Int,
        val isStatusBarContrastEnforced: Boolean,
        val isNavigationBarContrastEnforced: Boolean,
        val isAppearanceLightStatusBars: Boolean,
        val isAppearanceLightNavigationBars: Boolean,
        val layoutInDisplayCutoutMode: Int
    )

    /** Indicates init only once times. */
    private var isInitOnce = false

    /** The original system bar params. */
    private var originalSystemBarParams: SystemBarParams? = null

    /** The root view. */
    private var rootView: View? = null

    /** The current window insets controller of [rootView]. */
    private var rootInsetsController: WindowInsetsControllerCompat? = null

    /** Whether to handle the [rootView]'s window insets for edge-to-edge. */
    private var hasEdgeToEdgeInsets = false

    /**
     * Get the current system bars compat instance.
     * @return [SystemBarsCompat]
     */
    private val systemBarsCompat by lazy { SystemBarsCompat(window) }

    /**
     * Get the system bars behavior type.
     * @receiver [WindowInsetsControllerCompat]
     * @return [SystemBarBehavior]
     */
    private val WindowInsetsControllerCompat.systemBarBehaviorType get() = when (systemBarsBehavior) {
        WindowInsetsControllerCompat.BEHAVIOR_DEFAULT -> SystemBarBehavior.DEFAULT
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE -> SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
        else -> SystemBarBehavior.DEFAULT
    }

    /**
     * Convert to [WindowInsetsControllerCompat] types.
     * @receiver the [SystemBarBehavior] type.
     * @return [Int]
     */
    private fun SystemBarBehavior.toBehaviorType() = when (this) {
        SystemBarBehavior.DEFAULT -> WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE -> WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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
        behavior = SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE

        setStyle(SystemBarStyle.AutoTransparent)
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
     * @param edgeToEdgeInsets handle the [rootView]'s window insets for edge-to-edge, default is [WindowInsetsWrapper.safeDrawingIgnoringIme],
     * if you don't want to set that, you can set it to null.
     * @throws IllegalStateException if the [rootView] is not available.
     */
    @JvmOverloads
    fun init(rootView: View? = null, edgeToEdgeInsets: (WindowInsetsWrapper.() -> InsetsWrapper)? = { safeDrawingIgnoringIme }) {
        if (isInitOnce) return

        var throwable: Throwable? = null
        val absRootView = rootView ?: runCatching { window.findViewById<ViewGroup>(Android_R.id.content) }.onFailure {
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

        rootInsetsController = WindowCompat.getInsetsController(window, absRootView)
        hasEdgeToEdgeInsets = edgeToEdgeInsets != null

        var isStatusBarContrastEnforced = false
        var isNavigationBarContrastEnforced = false
        var navigationBarDividerColor = Color.TRANSPARENT
        var layoutInDisplayCutoutMode = 0

        @Suppress("DEPRECATION")
        AndroidVersion.require(AndroidVersion.Q) {
            isStatusBarContrastEnforced = window.isStatusBarContrastEnforced == true
            isNavigationBarContrastEnforced = window.isNavigationBarContrastEnforced == true

            // Remove system-made masking of forced contrast colors.
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
        @Suppress("DEPRECATION")
        AndroidVersion.require(AndroidVersion.P) {
            navigationBarDividerColor = window.navigationBarDividerColor
            layoutInDisplayCutoutMode = window.attributes?.layoutInDisplayCutoutMode ?: 0

            // Set the notch area not to interfere with the current UI.
            window.updateLayoutParams {
                this.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            // Remove the color of the navigation bars divider.
            window.navigationBarDividerColor = Color.TRANSPARENT
        }

        // Save the original system bars params.
        @Suppress("DEPRECATION")
        originalSystemBarParams = SystemBarParams(
            statusBarColor = window.statusBarColor,
            navigationBarColor = window.navigationBarColor,
            navigationBarDividerColor = navigationBarDividerColor,
            isStatusBarContrastEnforced = isStatusBarContrastEnforced,
            isNavigationBarContrastEnforced = isNavigationBarContrastEnforced,
            isAppearanceLightStatusBars = rootInsetsController?.isAppearanceLightStatusBars == true,
            isAppearanceLightNavigationBars = rootInsetsController?.isAppearanceLightNavigationBars == true,
            layoutInDisplayCutoutMode = layoutInDisplayCutoutMode
        )

        // Set the layout overlay to status bars and navigation bars.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initializeDefaults()

        // If has [edgeToEdgeInsets],
        // the controller will handle the root window insets by default.
        edgeToEdgeInsets?.also {
            absRootView.handleOnWindowInsetsChanged(animated = true) { rootView, insetsWrapper ->
                rootView.setInsetsPadding(it(insetsWrapper))
            }
        }
    }

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE].
     * @return [SystemBarBehavior]
     */
    var behavior
        get() = rootInsetsController?.systemBarBehaviorType ?: SystemBarBehavior.DEFAULT
        set(value) {
            rootInsetsController?.systemBarsBehavior = value.toBehaviorType()
        }

    /**
     * Show system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @see behavior
     * @param type the system bars type.
     */
    fun show(type: SystemBars) {
        rootInsetsController?.show(type.toInsetsType())
    }

    /**
     * Hide system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @see behavior
     * @param type the system bars type.
     */
    fun hide(type: SystemBars) {
        rootInsetsController?.hide(type.toInsetsType())
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
     * @param style the system bars style.
     */
    fun setStyle(style: SystemBarStyle) = setStyle(style, style)

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
    @JvmName("setSNStyle")
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
        val isUiInNightMode = window.context.resources.configuration.isUiInNightMode

        val defaultColor = if (isUiInNightMode) Color.BLACK else Color.WHITE
        val backgroundColor = style.color ?: defaultColor

        val darkContent = style.darkContent ?: !isUiInNightMode
        val lightAppearance = darkContent && backgroundColor == Color.TRANSPARENT || backgroundColor.isBrightColor

        @Suppress("DEPRECATION")
        when (type) {
            SystemBars.STATUS_BARS -> {
                enableDrawsSystemBarBackgrounds()

                // Below Android 6.0 will add a translucent mask,
                // as the system does not support inverting colors.
                // Some systems, such as MIUI based on Android 5,
                // will automatically adapt to their own set of inverse color schemes.
                window.statusBarColor =
                    if (AndroidVersion.isLessThan(AndroidVersion.M) && !systemBarsCompat.isLegacySystem && lightAppearance)
                        mixColorOf(backgroundColor, Color.BLACK)
                    else backgroundColor

                if (systemBarsCompat.isLegacySystem) systemBarsCompat.setStatusBarDarkMode(darkContent)
                else rootInsetsController?.isAppearanceLightStatusBars = darkContent
            }
            SystemBars.NAVIGATION_BARS -> {
                enableDrawsSystemBarBackgrounds()

                // Below Android 8.0 will add a transparent mask,
                // because the system does not support inverting colors.
                window.navigationBarColor =
                    if (AndroidVersion.isLessThan(AndroidVersion.O) && lightAppearance)
                        mixColorOf(backgroundColor, Color.BLACK)
                    else backgroundColor

                rootInsetsController?.isAppearanceLightNavigationBars = darkContent
            }
            else -> {}
        }
    }

    /** Enable to draws system bar backgrounds if not. */
    private fun enableDrawsSystemBarBackgrounds() {
        @Suppress("DEPRECATION")
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        @Suppress("DEPRECATION")
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
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
        @Suppress("DEPRECATION")
        originalSystemBarParams?.also {
            WindowCompat.setDecorFitsSystemWindows(window, true)

            window.statusBarColor = it.statusBarColor
            window.navigationBarColor = it.navigationBarColor

            AndroidVersion.require(AndroidVersion.Q) {
                window.isStatusBarContrastEnforced = it.isStatusBarContrastEnforced
                window.isNavigationBarContrastEnforced = it.isNavigationBarContrastEnforced
            }
            AndroidVersion.require(AndroidVersion.P) {
                window.navigationBarDividerColor = it.navigationBarDividerColor
                window.updateLayoutParams { layoutInDisplayCutoutMode = it.layoutInDisplayCutoutMode }
            }

            rootInsetsController?.isAppearanceLightStatusBars = it.isAppearanceLightStatusBars
            rootInsetsController?.isAppearanceLightNavigationBars = it.isAppearanceLightNavigationBars
        }
        if (hasEdgeToEdgeInsets) rootView?.removeWindowInsetsListener()

        rootInsetsController = null
        rootView = null
        hasEdgeToEdgeInsets = false
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
    fun init(defaultPaddings: Boolean) {
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
     * Determine whether the system bars is visible.
     *
     * - This function is deprecated and no effect, use your own [View.handleOnWindowInsetsChanged] instead.
     * @see View.handleOnWindowInsetsChanged
     */
    @Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
    fun isVisible(type: SystemBars) = false

    /**
     * Apply the system bars extra paddings.
     *
     * - This function is deprecated and no effect, use your own [View.handleOnWindowInsetsChanged] instead.
     * @see View.handleOnWindowInsetsChanged
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
    @JvmOverloads
    fun applyExtraPaddings(vararg types: Any, ignoredCutout: Boolean = false) {
    }

    /**
     * Append the system bars extra paddings.
     *
     * - This function is deprecated and no effect, use your own [View.handleOnWindowInsetsChanged] instead.
     * @see View.handleOnWindowInsetsChanged
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
    @JvmOverloads
    fun appendExtraPaddings(vararg types: Any, ignoredCutout: Boolean = false) {
    }

    /**
     * Remove the system bars extra paddings.
     *
     * - This function is deprecated and no effect, use your own [View.handleOnWindowInsetsChanged] instead.
     * @see View.handleOnWindowInsetsChanged
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
    @JvmOverloads
    fun removeExtraPaddings(vararg types: Any, ignoredCutout: Boolean = false) {
    }

    /**
     * Show the system bars stub views.
     *
     * - This function is deprecated and no effect, use your own [View.handleOnWindowInsetsChanged] instead.
     * @see View.handleOnWindowInsetsChanged
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
    fun showStub(type: SystemBars) {
    }

    /**
     * Hide the system bars stub views.
     *
     * - This function is deprecated and no effect, use your own [View.handleOnWindowInsetsChanged] instead.
     * @see View.handleOnWindowInsetsChanged
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
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
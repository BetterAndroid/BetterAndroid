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
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.activity.AppBindingActivity
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.component.fragment.AppBindingFragment
import com.highcapable.betterandroid.ui.component.fragment.AppViewsFragment
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.systembar.compat.SystemBarsCompat
import com.highcapable.betterandroid.ui.component.systembar.factory.appendSystemInsets
import com.highcapable.betterandroid.ui.component.systembar.factory.applySystemInsets
import com.highcapable.betterandroid.ui.component.systembar.insets.InsetsPadding
import com.highcapable.betterandroid.ui.component.systembar.insets.SystemInsets
import com.highcapable.betterandroid.ui.component.systembar.type.InsetsType
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBarsBehavior
import com.highcapable.betterandroid.ui.component.systembar.wrapper.DisplayCutoutCompatWrapper
import com.highcapable.betterandroid.ui.extension.component.base.isBrightColor
import com.highcapable.betterandroid.ui.extension.component.base.isUiInNightMode
import com.highcapable.betterandroid.ui.extension.widget.ViewLayoutParams
import com.highcapable.betterandroid.ui.extension.widget.removeSelfInLayout
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
         *         // For a better experience, you can hide the system action bar.
         *         supportActionBar?.hide()
         *         // Can be called before or after the setContentView method.
         *         systemBars.init()
         *         setContentView(R.layout.activity_main)
         *     }
         * }
         * ```
         *
         * Or you can use [AppBindingActivity], [AppViewsActivity], [AppBindingFragment], [AppViewsFragment].
         * @param activity the current activity.
         * @param rootView the root view (must have a parent) to initialize [SystemBarsController], default is [Android_R.id.content].
         * @return [SystemBarsController]
         * @throws IllegalStateException if [rootView] is invalid.
         */
        @JvmStatic
        fun from(activity: Activity, rootView: View? = null): SystemBarsController {
            var throwable: Throwable? = null
            val absRootView = rootView ?: runCatching { activity.findViewById<ViewGroup>(Android_R.id.content) }.onFailure {
                throwable = it
                Log.e(BetterAndroidProperties.PROJECT_NAME, "Failed to get the rootView from android.R.id.content.", it)
            }.getOrNull() ?: error(
                "The root view is not available at this stage, " +
                    "please set the rootView parameter manually or create the SystemBarsController in Activity's onCreate event." +
                    (throwable?.message?.let { "\nCaused by: $it" } ?: "")
            )
            require(absRootView is ViewGroup) { "The rootView $absRootView must inherit from a ViewGroup." }
            requireNotNull(absRootView.parent) { "The rootView $absRootView must have a parent." }
            return SystemBarsController(activity).apply { this.rootView = absRootView }
        }

        /**
         * Create a new [AbsoluteController].
         * @param context the current context, it should be an [Activity],
         * don't use [Application].
         * @return [AbsoluteController]
         */
        @JvmStatic
        fun createAbsolute(context: Context) = AbsoluteController(context)
    }

    /**
     * System bars absolute controller.
     * @param context the current context.
     */
    class AbsoluteController internal constructor(private val context: Context) {

        /**
         * Get the status bar height (px).
         * @return [Int]
         */
        val statusBarHeight @Px get() = (context as? Activity?)?.let { SystemBarsCompat(it).absoluteStatusBarHeight } ?: 0

        /**
         * Get the navigation bar height (px).
         *
         * - Unexpected behavior may occur if notch size exists on the system.
         * @return [Int]
         */
        val navigationBarHeight @Px get() = (context as? Activity?)?.let { SystemBarsCompat(it).absoluteNavigationBarHeight } ?: 0
    }

    /**
     * System bars params.
     * @param statusBarColor the status bars color.
     * @param navigationBarColor the navigation bars color.
     * @param isDarkColorStatusBars whether the status bars color is dark color.
     * @param isDarkColorNavigationBars whether the navigation bars color is dark color.
     */
    private data class SystemBarsParams(
        val statusBarColor: Int,
        val navigationBarColor: Int,
        val isDarkColorStatusBars: Boolean,
        val isDarkColorNavigationBars: Boolean
    )

    /**
     * System bars stub view (a placeholder layout).
     * @param context the current context.
     */
    private class SystemBarsView(context: Context) : FrameLayout(context) {

        private companion object {

            /** A translucent color. */
            private const val TRANSLUCENT_COLOR = 0x80000000
        }

        /** The translucent mask view. */
        private var translucentView: View

        init {
            translucentView = View(context).apply {
                isVisible = false
                layoutParams = ViewLayoutParams.create(matchParent = true)
                setBackgroundColor(TRANSLUCENT_COLOR.toInt())
            }
            addView(translucentView)
        }

        /** Show the translucent mask. */
        fun showMask() {
            translucentView.isVisible = true
        }
    }

    /** Indicates init only once times. */
    private var isInitOnce = false

    /** The original system bars params. */
    private var originalSystemBarsParams: SystemBarsParams? = null

    /** The root view. */
    private lateinit var rootView: ViewGroup

    /** The parent layout included [containerLayout]. */
    private var parentLayout: ViewGroup? = null

    /** The container layout included [rootView]. */
    private var containerLayout: ViewGroup? = null

    /** The callback of container padding function. */
    private var containerPaddingCallback: (() -> Unit)? = null

    /**
     * The [Window.getDecorView] in this [activity].
     * @return [View]
     * @throws IllegalStateException if [activity] is destroyed.
     */
    private val decorView get() = activity.window?.decorView ?: error("Activity has destroyed.")

    /**
     * Get the current insets controller.
     * @return [WindowInsetsControllerCompat]
     */
    private val insetsController get() = WindowCompat.getInsetsController(activity.window, decorView)

    /** The current window insets. */
    private var windowInsets = ViewCompat.getRootWindowInsets(decorView)

    /**
     * Get the system bars views. (included status bars, navigation bars)
     * @return [Array]<[SystemBarsView]>
     */
    private val systemBarsViews by lazy { arrayOf(SystemBarsView(activity), SystemBarsView(activity)) }

    /**
     * Get the current system bars compat instance.
     * @return [SystemBarsCompat]
     */
    private val systemBarsCompat by lazy { SystemBarsCompat(activity) }

    /** The system bars insets changes callback map. */
    private var onInsetsChangedCallbacks = mutableMapOf<String, (SystemInsets) -> Unit>()

    /**
     * Convert to [WindowInsetsCompat.Type] types.
     * @receiver the [SystemBars] type.
     * @return [Int]
     */
    private fun SystemBars.toInsetsType() = when (this) {
        SystemBars.ALL -> WindowInsetsCompat.Type.systemBars()
        SystemBars.STATUS_BARS -> WindowInsetsCompat.Type.statusBars()
        SystemBars.NAVIGATION_BARS -> WindowInsetsCompat.Type.navigationBars()
        SystemBars.SYSTEM_GESTURES -> WindowInsetsCompat.Type.systemGestures()
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
     * Create the [SystemBarsController]'s container layout.
     *
     * ```
     * Parent Layout (FrameLayout)
     * └─ Base Container Layout (FrameLayout)
     *    ├─ Status Bars (SystemBarsView)
     *    ├─ Navigation Bars (SystemBarsView)
     *    └─ Container Layout (RelativeLayout)
     *       └─ Root View ← The content view
     * ```
     * @throws IllegalStateException if there is no parent container layout.
     */
    private fun createSystemBarsLayout() {
        require(this::rootView.isInitialized) { "SystemBarsController has destroyed." }
        parentLayout = rootView.parent as? ViewGroup? ?: error("Could not found parent container layout to bind SystemBarsController.")
        parentLayout?.removeView(rootView)
        val baseContainerLayout = FrameLayout(activity).apply {
            layoutParams = ViewLayoutParams.create(matchParent = true)
            containerLayout = RelativeLayout(activity).apply { layoutParams = ViewLayoutParams.create(matchParent = true) }
            addView(containerLayout)
            addView(systemBarsViews[0].apply {
                layoutParams = ViewLayoutParams.create<FrameLayout.LayoutParams>(widthMatchParent = true)
                    .apply { gravity = Gravity.TOP }
            })
            addView(systemBarsViews[1].apply {
                layoutParams = ViewLayoutParams.create<FrameLayout.LayoutParams>(widthMatchParent = true)
                    .apply { gravity = Gravity.BOTTOM }
            })
        }
        containerLayout?.addView(rootView)
        parentLayout?.addView(baseContainerLayout)
    }

    /** Create the default container padding callback if not exists. */
    private fun createDefaultContainerPaddingCallback() {
        if (containerPaddingCallback == null)
            containerPaddingCallback = {
                val padding = systemInsets?.let { containerLayout?.applySystemInsets(it) }
                updateSystemBarsViewsHeight(padding)
            }
    }

    /** Update the container layout padding. */
    private fun updateContainerPadding() {
        containerPaddingCallback?.invoke()
    }

    /**
     * Update the system bars views height.
     * @param padding the system insets padding.
     */
    private fun updateSystemBarsViewsHeight(padding: InsetsPadding?) {
        padding ?: return
        systemBarsViews[0].updateLayoutParams { height = padding.top }
        systemBarsViews[1].updateLayoutParams { height = padding.bottom }
    }

    /**
     * The current system bars insets.
     *
     * The first initialization may be null, it is recommended to
     * use [addOnInsetsChangeListener] to add the system bars insets changes listener.
     */
    var systemInsets: SystemInsets? = null

    /**
     * Get the current [SystemBarsController]'s status.
     * @return [Boolean]
     */
    val isDestroyed get() = !isInitOnce

    /**
     * Get the base layout. (The [SystemBarsController] injected parent layout)
     * @return [ViewGroup]
     * @throws IllegalStateException if [SystemBarsController] is not initialize.
     */
    val baseLayout get() = containerLayout ?: error("Failed to got baseLayout, SystemBarsController may not initialize.")

    /**
     * Get or set the behavior of system bars.
     *
     * The default behavior type is [SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE].
     *
     * - Before using this function, you must call [init] first.
     * @return [SystemBarsBehavior]
     * @throws IllegalStateException if [init] never been called.
     */
    var behavior
        get() = when (insetsController.systemBarsBehavior) {
            WindowInsetsControllerCompat.BEHAVIOR_DEFAULT -> SystemBarsBehavior.DEFAULT
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE -> SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
            else -> SystemBarsBehavior.DEFAULT
        }
        set(value) {
            require(isInitOnce) { "You must call init function first before using behavior function." }
            insetsController.systemBarsBehavior = value.toBehaviorType()
        }

    /**
     * Initialize [SystemBarsController].
     *
     * It is recommended to use this function in [Activity.onCreate].
     *
     * You can use [destroy] to destroy this [SystemBarsController].
     *
     * - The initialization operation will not be called repeatedly,
     *   repeated calls will only be performed once.
     * @param defaultPadding whether to initialize the default system insets padding, default true.
     */
    fun init(defaultPadding: Boolean = true) {
        if (isInitOnce) return
        isInitOnce = true
        originalSystemBarsParams = SystemBarsParams(
            statusBarColor = activity.window?.statusBarColor ?: Color.TRANSPARENT,
            navigationBarColor = activity.window?.navigationBarColor ?: Color.TRANSPARENT,
            isDarkColorStatusBars = isDarkColorStatusBars,
            isDarkColorNavigationBars = isDarkColorNavigationBars
        )
        behavior = SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
        createSystemBarsLayout()
        if (defaultPadding) createDefaultContainerPaddingCallback()
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, windowInsets ->
            this.windowInsets = windowInsets
            windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).also { insets ->
                val systemInsets = SystemInsets(insets, SystemVersion.require(
                    SystemVersion.P,
                    systemBarsCompat.createLegacyDisplayCutoutCompat(insets.top)
                ) { DisplayCutoutCompatWrapper(activity, systemBarsCompat, wrapper = windowInsets.displayCutout) })
                this.systemInsets = systemInsets
                onInsetsChangedCallbacks.forEach { (_, e) -> e(systemInsets) }
                updateContainerPadding()
            }; windowInsets
        }
        val isUiInNightMode = activity.resources.configuration.isUiInNightMode
        isDarkColorStatusBars = !isUiInNightMode
        isDarkColorNavigationBars = !isUiInNightMode
        /** Set the layout overlay to status bars and navigation bars. */
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        /** We no longer need the system status bars and navigation bars background color. */
        activity.window?.statusBarColor = Color.TRANSPARENT
        activity.window?.navigationBarColor = Color.TRANSPARENT
        SystemVersion.require(SystemVersion.P) {
            /** Set the notch area not to interfere with the current UI. */
            activity.window?.attributes?.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            /** Remove the color of the navigation bars divider. */
            activity.window?.navigationBarDividerColor = Color.TRANSPARENT
        }
    }

    /**
     * Set the [baseLayout]'s background drawable.
     * @param background the background drawable.
     */
    fun setBaseBackground(background: Drawable?) {
        containerLayout?.background = background
    }

    /**
     * Set the [baseLayout]'s background color.
     * @param color the background color.
     */
    fun setBaseBackgroundColor(@ColorInt color: Int) {
        containerLayout?.setBackgroundColor(color)
    }

    /**
     * Set the [baseLayout]'s background resource.
     * @param resId the background resource ID.
     */
    fun setBaseBackgroundResource(@DrawableRes resId: Int) {
        containerLayout?.setBackgroundResource(resId)
    }

    /**
     * Add the system bars insets changes listener.
     * @param tag the callback TAG, default null.
     * @param onChange callback [SystemInsets].
     */
    fun addOnInsetsChangeListener(tag: Any? = null, onChange: (SystemInsets) -> Unit) {
        val currentTag = tag?.toString() ?: onChange.toString()
        onInsetsChangedCallbacks[currentTag] = onChange
    }

    /**
     * Remove the system bars insets changes listener.
     * @param tag the callback TAG.
     */
    fun removeOnInsetsChangeListener(tag: Any) {
        onInsetsChangedCallbacks.remove(tag.toString())
    }

    /**
     * Show system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @param type the system bars type.
     */
    fun show(type: SystemBars) = insetsController.show(type.toInsetsType())

    /**
     * Hide system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @param type the system bars type.
     */
    fun hide(type: SystemBars) = insetsController.hide(type.toInsetsType())

    /**
     * Apply the system insets and cutout padding in [containerLayout].
     *
     * See also [View.applySystemInsets].
     *
     * This function's inner callback [containerPaddingCallback] will be re-called when the system insets changes.
     * @param left whether to apply the left insets.
     * @param top whether to apply the top insets.
     * @param right whether to apply the right insets.
     * @param bottom whether to apply the bottom insets.
     * @param type the insets type, default is [InsetsType.ADAPTIVE].
     */
    @JvmOverloads
    fun applySystemInsets(
        left: Boolean = true,
        top: Boolean = true,
        right: Boolean = true,
        bottom: Boolean = true,
        type: InsetsType = InsetsType.ADAPTIVE
    ) {
        containerPaddingCallback = {
            val padding = systemInsets?.let { containerLayout?.applySystemInsets(it, left, top, right, bottom, type) }
            updateSystemBarsViewsHeight(padding)
        }; updateContainerPadding()
    }

    /**
     * Append the system insets and cutout padding in [containerLayout].
     *
     * See also [View.appendSystemInsets].
     *
     * This function's inner callback [containerPaddingCallback] will be re-called when the system insets changes.
     * @param left whether to apply the left insets.
     * @param top whether to apply the top insets.
     * @param right whether to apply the right insets.
     * @param bottom whether to apply the bottom insets.
     * @param type the insets type, default is [InsetsType.ADAPTIVE].
     */
    @JvmOverloads
    fun appendSystemInsets(
        left: Boolean = true,
        top: Boolean = true,
        right: Boolean = true,
        bottom: Boolean = true,
        type: InsetsType = InsetsType.ADAPTIVE
    ) {
        containerPaddingCallback = {
            val padding = systemInsets?.let { containerLayout?.appendSystemInsets(it, left, top, right, bottom, type) }
            updateSystemBarsViewsHeight(padding)
        }; updateContainerPadding()
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    fun isVisible(type: SystemBars) = windowInsets?.isVisible(type.toInsetsType()) ?: false

    /**
     * Show system bars.
     *
     * - This function is deprecated, use [show] instead.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use show instead.", ReplaceWith("show(type)"))
    fun show(type: SystemBars, appendExtraPaddings: Boolean = false, ignoredCutout: Boolean = false) = show(type)

    /**
     * Hide system bars.
     *
     * - This function is deprecated, use [hide] instead.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use hide instead.", ReplaceWith("hide(type)"))
    fun hide(type: SystemBars, removeExtraPaddings: Boolean = false, ignoredCutout: Boolean = false) = hide(type)

    /**
     * Apply the system bars extra paddings.
     *
     * - This function is deprecated, use [applySystemInsets] instead.
     */
    @Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use applySystemInsets instead.")
    @JvmOverloads
    fun applyExtraPaddings(
        vararg types: com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType,
        ignoredCutout: Boolean = false
    ) = appendSystemInsets(
        left = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.LEFT),
        top = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.TOP),
        right = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.RIGHT),
        bottom = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.BOTTOM),
        type = if (ignoredCutout) InsetsType.STABLE else InsetsType.ADAPTIVE
    )

    /**
     * Append the system bars extra paddings.
     *
     * - This function is deprecated, use [appendSystemInsets] instead.
     */
    @Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use appendSystemInsets instead.")
    @JvmOverloads
    fun appendExtraPaddings(
        vararg types: com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType,
        ignoredCutout: Boolean = false
    ) = appendSystemInsets(
        left = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.LEFT),
        top = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.TOP),
        right = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.RIGHT),
        bottom = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.BOTTOM),
        type = if (ignoredCutout) InsetsType.STABLE else InsetsType.ADAPTIVE
    )

    /**
     * Remove the system bars extra paddings.
     *
     * - This function is deprecated and no effect, use [appendSystemInsets] or [appendSystemInsets] instead.
     */
    @Suppress("DEPRECATION", "UNUSED_PARAMETER")
    @Deprecated(message = "Use appendSystemInsets or appendSystemInsets instead.")
    @JvmOverloads
    fun removeExtraPaddings(
        vararg types: com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType,
        ignoredCutout: Boolean = false
    ) {
    }

    /**
     * Show the system bars stub views.
     *
     * - This function is deprecated and no effect, use [applyExtraPaddings], [appendExtraPaddings] instead.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use applyExtraPaddings, appendExtraPaddings instead.")
    fun showStub(type: SystemBars) {
    }

    /**
     * Hide the system bars stub views.
     *
     * - This function is deprecated and no effect, use [removeExtraPaddings] instead.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(message = "Use removeExtraPaddings instead.")
    @JvmOverloads
    fun hideStub(type: SystemBars, ignoredCutout: Boolean = false) {
    }

    /**
     * Set the system bars background color.
     * @param type the system bars type.
     * @param color the color to set.
     * @throws IllegalStateException if [type] is invalid.
     */
    fun setColor(type: SystemBars, @ColorInt color: Int) {
        when (type) {
            SystemBars.ALL -> {
                systemBarsViews[0].setBackgroundColor(color)
                systemBarsViews[1].setBackgroundColor(color)
            }
            SystemBars.STATUS_BARS -> systemBarsViews[0].setBackgroundColor(color)
            SystemBars.NAVIGATION_BARS -> systemBarsViews[1].setBackgroundColor(color)
            else -> error("Invalid SystemBars type.")
        }
    }

    /**
     * Get or set the dark elements tint color (light appearance) of status bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     *
     * - Set to true below Android 6.0 will add a translucent mask
     *   when [setColor] is not used, as the system does not support inverting colors.
     *
     * - Some systems, such as MIUI based on Android 5,
     *   will automatically adapt to their own set of inverse color schemes.
     * @return [Boolean]
     */
    var isDarkColorStatusBars
        get() = insetsController.isAppearanceLightStatusBars
        set(value) {
            if (SystemVersion.isLowTo(SystemVersion.M) && !systemBarsCompat.isLegacyMiui) systemBarsViews[0].showMask()
            if (systemBarsCompat.isLegacyMiui) systemBarsCompat.setStatusBarDarkModeForLegacyMiui(value)
            insetsController.isAppearanceLightStatusBars = value
        }

    /**
     * Get or set the dark elements tint color (light appearance) of navigation bars.
     *
     * | Value | Behavior                                |
     * | ----- | --------------------------------------- |
     * | true  | Background bright, font and icons dark. |
     * | false | Background dark, font and icons bright. |
     *
     * - Set to true below Android 8.0 will add a transparent mask
     *   when [setColor] is not used, because the system does not support inverting colors.
     * @return [Boolean]
     */
    var isDarkColorNavigationBars
        get() = insetsController.isAppearanceLightNavigationBars
        set(value) {
            if (SystemVersion.isLowTo(SystemVersion.O)) systemBarsViews[1].showMask()
            insetsController.isAppearanceLightNavigationBars = value
        }

    /**
     * Automatically adapts the appearance of system bars based on the given [color].
     * @param color the current color.
     */
    fun adaptiveAppearance(color: Int) {
        val isBrightColor = color.isBrightColor
        isDarkColorStatusBars = isBrightColor
        isDarkColorNavigationBars = isBrightColor
    }

    /**
     * Destroy the current [SystemBarsController] and restore the current [rootView].
     *
     * You can call [init] again to re-initialize the [SystemBarsController].
     *
     * - The destroy operation will not be called repeatedly,
     *   repeated calls will only be performed once.
     */
    fun destroy() {
        if (!isInitOnce || containerLayout == null) return
        rootView.removeSelfInLayout()
        systemBarsViews.forEach { it.removeSelfInLayout() }
        containerLayout?.removeSelfInLayout()
        parentLayout?.removeAllViewsInLayout()
        parentLayout?.addView(rootView)
        parentLayout = null
        containerLayout = null
        containerPaddingCallback = null
        /** Restore to default sets. */
        originalSystemBarsParams?.also {
            WindowCompat.setDecorFitsSystemWindows(activity.window, true)
            activity.window?.statusBarColor = it.statusBarColor
            activity.window?.navigationBarColor = it.navigationBarColor
            isDarkColorStatusBars = it.isDarkColorStatusBars
            isDarkColorNavigationBars = it.isDarkColorNavigationBars
        }
        isInitOnce = false
    }
}
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
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.activity.AppBindingActivity
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.component.fragment.AppBindingFragment
import com.highcapable.betterandroid.ui.component.fragment.AppViewsFragment
import com.highcapable.betterandroid.ui.component.generated.BetterAndroidProperties
import com.highcapable.betterandroid.ui.component.systembar.compat.SystemBarsCompat
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBarsBehavior
import com.highcapable.betterandroid.ui.component.systembar.widget.SystemBarsView
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
 * @param systemBarsViews the current stub views.
 */
class SystemBarsController private constructor(private val activity: Activity, private val systemBarsViews: List<SystemBarsView>) {

    companion object {

        /** A translucent color. */
        private const val TRANSLUCENT_COLOR = 0x80000000

        /** The [SystemBarsController]'s container view tag. */
        private const val CONTAINER_VIEW_TAG = "system_bars_controller_container_view"

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
        fun from(activity: Activity, rootView: View ? = null): SystemBarsController {
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
            val systemBars = mutableListOf<SystemBarsView>()
            for (i in 0..3)
                systemBars.add(SystemBarsView(activity).apply {
                    insetsType = when (i) {
                        0 -> SystemBarsView.InsetsType.TOP
                        1 -> SystemBarsView.InsetsType.LEFT
                        2 -> SystemBarsView.InsetsType.RIGHT
                        else -> SystemBarsView.InsetsType.BOTTOM
                    }
                })
            return SystemBarsController(activity, systemBars).apply { this.rootView = absRootView }
        }

        /**
         * Create a new [AbsoluteController].
         * @param context the current context, it should be an [Activity],
         * don't use [Application].
         * @return [AbsoluteController]
         */
        @JvmStatic
        fun createAbsolute(context: Context) = AbsoluteController(context)

        /**
         * Determine whether the current [View] has a transparent,
         * semi-transparent background color or no background.
         * @return [Boolean]
         */
        private fun View.isNullOrTranslucentColor() =
            background == null || (background as? ColorDrawable?)?.color == TRANSLUCENT_COLOR.toInt() ||
                (background as? ColorDrawable?)?.color == Color.TRANSPARENT
    }

    init {
        require(systemBarsViews.size == 4) { "Failed to initialize SystemBarsController, systemBarsViews size must be equal to 4." }
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
     * System bars insets.
     * @param stable the stable insets, usually the listener result for [WindowInsetsCompat].
     * @param cutout the notch size (cutout size).
     */
    class SystemInsets private constructor(val stable: Insets, val cutout: DisplayCutoutCompatWrapper) {

        internal companion object {

            /**
             * Create a new [SystemInsets] from an exists [Insets] and [DisplayCutoutCompatWrapper].
             * @param stable the stable insets, usually the listener result for [WindowInsetsCompat].
             * @param cutout the notch size (cutout size).
             * @throws IllegalStateException if [stable] or [cutout] is null.
             */
            internal fun from(stable: Insets?, cutout: DisplayCutoutCompatWrapper?) =
                if (stable == null || cutout == null)
                    error("Your system not support system insets or have an error, SystemBarsController initialization failed.")
                else SystemInsets(stable, cutout)
        }
    }

    /** Indicates init only once times. */
    private var isInitOnce = false

    /** The root view for initialize [systemBarsViews]. */
    private lateinit var rootView: ViewGroup

    /** The container layout included [rootView]. */
    private var containerLayout: ViewGroup? = null

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

    /**
     * Get the current system bars compat instance.
     * @return [SystemBarsCompat]
     */
    private val systemBarsCompat by lazy { SystemBarsCompat(activity) }

    /** The system bars insets changes callback sets. */
    private var onInsetsChangedCallbacks = mutableSetOf<(SystemInsets) -> Unit>()

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
     * This is the core working function of [SystemBarsController].
     *
     * Which uses magic to inject the current root view to obtain control permissions for system bars.
     * @throws IllegalStateException if there is no parent container layout.
     */
    private fun createSystemBarsLayout() {
        require(this::rootView.isInitialized) { "SystemBarsController has destroyed." }
        val widthMatchParentParams = ViewLayoutParams.create<LinearLayout.LayoutParams>(widthMatchParent = true)
        val heightMatchParentParams = ViewLayoutParams.create<LinearLayout.LayoutParams>(heightMatchParent = true)
        val widthMatchResizeParams = ViewLayoutParams.create<LinearLayout.LayoutParams>(widthMatchParent = true, height = 0).apply { weight = 1f }
        val heightMatchResizeParams = ViewLayoutParams.create<LinearLayout.LayoutParams>(heightMatchParent = true, width = 0).apply { weight = 1f }
        val parentLayout = rootView.parent as? ViewGroup? ?: error("Could not found parent container layout to bind SystemBarsController.")
        parentLayout.removeViewInLayout(rootView)
        val mainContainer = LinearLayout(activity).apply {
            tag = CONTAINER_VIEW_TAG
            layoutParams = ViewLayoutParams.create(matchParent = true)
            orientation = LinearLayout.VERTICAL
            addView(systemBarsViews[0].apply { layoutParams = widthMatchParentParams })
            addView(systemBarsViews[3].apply { layoutParams = widthMatchParentParams })
        }
        val innerContainer = LinearLayout(activity).apply {
            layoutParams = widthMatchResizeParams
            orientation = LinearLayout.HORIZONTAL
            addView(systemBarsViews[1].apply { layoutParams = heightMatchParentParams })
            addView(systemBarsViews[2].apply { layoutParams = heightMatchParentParams })
        }
        innerContainer.addView(rootView.apply { layoutParams = heightMatchResizeParams }, 1)
        mainContainer.addView(innerContainer, 1)
        parentLayout.addView(mainContainer)
        containerLayout = parentLayout
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
     */
    fun init() {
        if (isInitOnce) return
        isInitOnce = true
        behavior = SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
        createSystemBarsLayout()
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, windowInsets ->
            systemBarsViews.forEach {
                windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars()).also { insets ->
                    it.applyInsetsAndCutout(
                        SystemInsets.from(insets, SystemVersion.require(
                            SystemVersion.P,
                            systemBarsCompat.createLegacyDisplayCutoutCompat(insets.top)
                        ) { DisplayCutoutCompatWrapper(activity, systemBarsCompat, wrapper = windowInsets.displayCutout) }).also {
                            systemInsets = it
                            onInsetsChangedCallbacks.takeIf { e -> e.isNotEmpty() }?.forEach { e -> e(it) }
                        }
                    )
                }
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
     * Set the [baseLayout]'s background color.
     * @param color the background color.
     */
    fun setBaseBackgroundColor(@ColorInt color: Int) {
        containerLayout?.setBackgroundColor(color)
    }

    /**
     * Set the [baseLayout]'s background drawable.
     * @param background the background drawable.
     */
    fun setBaseBackground(background: Drawable?) {
        containerLayout?.background = background
    }

    /**
     * Add the system bars insets changes listener.
     * @param onChange callback [SystemInsets].
     */
    fun addOnInsetsChangeListener(onChange: (SystemInsets) -> Unit) {
        onInsetsChangedCallbacks.add(onChange)
    }

    /**
     * Show system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @param type the system bars type.
     * @param isShowStub Also show the system bars stub views, default true.
     */
    @JvmOverloads
    fun show(type: SystemBars, isShowStub: Boolean = true) {
        insetsController.show(type.toInsetsType())
        if (isShowStub) showStub(type)
    }

    /**
     * Hide system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @param type the system bars type.
     * @param isHideStub Also hide the system bars stub views, default true.
     */
    @JvmOverloads
    fun hide(type: SystemBars, isHideStub: Boolean = true) {
        insetsController.hide(type.toInsetsType())
        if (isHideStub) hideStub(type)
    }

    /**
     * Show the system bars stub views.
     * @param type the system bars type.
     * @throws IllegalStateException if [type] is invalid.
     */
    fun showStub(type: SystemBars) {
        when (type) {
            SystemBars.ALL -> systemBarsViews.forEach { it.show() }
            SystemBars.STATUS_BARS -> systemBarsViews[0].show()
            SystemBars.NAVIGATION_BARS -> systemBarsViews[3].show()
            else -> error("Invalid SystemBars type.")
        }
    }

    /**
     * Hide the system bars stub views.
     *
     * - Note: The [ignoredCutout] parameter may be delayed based on the loading time of [Activity].
     * @param type the system bars type.
     * @param ignoredCutout Whether to ignore the notch size,
     * if not ignored, it will stop hiding on the notch size,
     * default is false.
     * @throws IllegalStateException if [type] is invalid.
     */
    @JvmOverloads
    fun hideStub(type: SystemBars, ignoredCutout: Boolean = false) {
        when (type) {
            SystemBars.ALL -> systemBarsViews.forEach { it.hide(ignoredCutout) }
            SystemBars.STATUS_BARS -> systemBarsViews[0].hide(ignoredCutout)
            SystemBars.NAVIGATION_BARS -> systemBarsViews[3].hide(ignoredCutout)
            else -> error("Invalid SystemBars type.")
        }
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
                systemBarsViews[3].setBackgroundColor(color)
            }
            SystemBars.STATUS_BARS -> systemBarsViews[0].setBackgroundColor(color)
            SystemBars.NAVIGATION_BARS -> systemBarsViews[3].setBackgroundColor(color)
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
            if (SystemVersion.isLowTo(SystemVersion.M) && !systemBarsCompat.isLegacyMiui)
                if (value && systemBarsViews[0].isNullOrTranslucentColor())
                    setColor(SystemBars.STATUS_BARS, TRANSLUCENT_COLOR.toInt())
                else setColor(SystemBars.STATUS_BARS, Color.TRANSPARENT)
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
            if (SystemVersion.isLowTo(SystemVersion.O))
                if (value && systemBarsViews[3].isNullOrTranslucentColor())
                    setColor(SystemBars.NAVIGATION_BARS, TRANSLUCENT_COLOR.toInt())
                else setColor(SystemBars.NAVIGATION_BARS, Color.TRANSPARENT)
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
        val mainContainer = containerLayout?.findViewWithTag<ViewGroup>(CONTAINER_VIEW_TAG)
        if (mainContainer != null) {
            systemBarsViews.forEach { it.removeSelfInLayout() }
            rootView.removeSelfInLayout()
            rootView.layoutParams = ViewLayoutParams.create(matchParent = true)
            containerLayout?.removeViewInLayout(mainContainer)
            containerLayout?.addView(rootView)
        }; isInitOnce = false
    }
}
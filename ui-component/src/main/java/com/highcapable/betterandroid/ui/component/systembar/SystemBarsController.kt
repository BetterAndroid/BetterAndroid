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
import androidx.core.graphics.Insets
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
import com.highcapable.betterandroid.ui.component.systembar.factory.removeSystemInsets
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBars
import com.highcapable.betterandroid.ui.component.systembar.type.SystemBarsBehavior
import com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType
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

        /**
         * Insets paddings.
         * @param left the left padding (px).
         * @param top the top padding (px).
         * @param right the right padding (px).
         * @param bottom the bottom padding (px).
         */
        class Paddings private constructor(@Px val left: Int, @Px val top: Int, @Px val right: Int, @Px val bottom: Int) {

            companion object {

                /**
                 * Create a new [Paddings] from [SystemInsets].
                 * @param systemInsets the system insets instance.
                 * @param ignoredCutout whether to ignore the notch size,
                 * if not ignored, it will stop handing on the notch size,
                 * default false.
                 * @param isOnlyCutout whether to only return the notch size, default false.
                 * @return [Paddings]
                 */
                @JvmStatic
                @JvmOverloads
                fun from(systemInsets: SystemInsets, ignoredCutout: Boolean = false, isOnlyCutout: Boolean = false): Paddings {
                    val paddingTop = compatSize(systemInsets.cutout.safeInsetTop, systemInsets.stable.top, ignoredCutout, isOnlyCutout)
                    val paddingLeft = compatSize(systemInsets.cutout.safeInsetLeft, systemInsets.stable.left, ignoredCutout, isOnlyCutout)
                    val paddingRight = compatSize(systemInsets.cutout.safeInsetRight, systemInsets.stable.right, ignoredCutout, isOnlyCutout)
                    val paddingBottom = compatSize(systemInsets.cutout.safeInsetBottom, systemInsets.stable.bottom, ignoredCutout, isOnlyCutout)
                    return Paddings(paddingLeft, paddingTop, paddingRight, paddingBottom)
                }

                /**
                 * Compatible, use the one with the largest size first and return.
                 * @param safeSize the safe size (px).
                 * @param stableSize the stable size (px).
                 * @param ignoredCutout if true, only return the [stableSize].
                 * @param isOnlyCutout if true, only return the [safeSize], if [ignoredCutout] is true, return 0.
                 * @return [Int]
                 */
                private fun compatSize(@Px safeSize: Int, @Px stableSize: Int, ignoredCutout: Boolean, isOnlyCutout: Boolean) =
                    when {
                        isOnlyCutout -> if (ignoredCutout) 0 else safeSize
                        ignoredCutout -> stableSize
                        safeSize > stableSize || stableSize <= 0 -> safeSize
                        else -> stableSize
                    }
            }
        }
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

    /** The callback of container paddings function. */
    private var containerPaddingsCallback: (() -> Unit)? = null

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

    /** Create the default container paddings callback if not exists. */
    private fun createDefaultContainerPaddingsCallback() {
        if (containerPaddingsCallback == null)
            containerPaddingsCallback = {
                val paddings = systemInsets?.let { containerLayout?.applySystemInsets(it) }
                updateSystemBarsViewsHeight(paddings)
            }
    }

    /** Update the container layout paddings. */
    private fun updateContainerPaddings() {
        containerPaddingsCallback?.invoke()
    }

    /**
     * Update the system bars views height.
     * @param paddings the system insets paddings.
     */
    private fun updateSystemBarsViewsHeight(paddings: SystemInsets.Paddings?) {
        paddings ?: return
        val isStatusBarsVisible = isVisible(SystemBars.STATUS_BARS)
        val isNavigationBarsVisible = isVisible(SystemBars.NAVIGATION_BARS)
        systemBarsViews[0].updateLayoutParams { height = if (isStatusBarsVisible) paddings.top else 0 }
        systemBarsViews[1].updateLayoutParams { height = if (isNavigationBarsVisible) paddings.bottom else 0 }
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
     * @param defaultPaddings whether to initialize the default system insets paddings, default true.
     */
    fun init(defaultPaddings: Boolean = true) {
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
        if (defaultPaddings) createDefaultContainerPaddingsCallback()
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, windowInsets ->
            this.windowInsets = windowInsets
            windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars()).also { insets ->
                val systemInsets = SystemInsets.from(insets, SystemVersion.require(
                    SystemVersion.P,
                    systemBarsCompat.createLegacyDisplayCutoutCompat(insets.top)
                ) { DisplayCutoutCompatWrapper(activity, systemBarsCompat, wrapper = windowInsets.displayCutout) })
                this.systemInsets = systemInsets
                onInsetsChangedCallbacks.takeIf { e -> e.isNotEmpty() }?.forEach { e -> e(systemInsets) }
                updateContainerPaddings()
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
     * @param appendExtraPaddings also append the system bars extra paddings,
     * when [containerPaddingsCallback] exists, default true.
     * @param ignoredCutout whether to ignore the notch size,
     * if not ignored, it will stop handing on the notch size, default false.
     * @throws IllegalStateException if [type] is invalid.
     */
    @JvmOverloads
    fun show(type: SystemBars, appendExtraPaddings: Boolean = containerPaddingsCallback != null, ignoredCutout: Boolean = false) {
        insetsController.show(type.toInsetsType())
        if (appendExtraPaddings) when (type) {
            SystemBars.ALL -> appendExtraPaddings(types = arrayOf(SystemInsetsType.TOP, SystemInsetsType.BOTTOM), ignoredCutout = ignoredCutout)
            SystemBars.STATUS_BARS -> appendExtraPaddings(types = arrayOf(SystemInsetsType.TOP), ignoredCutout = ignoredCutout)
            SystemBars.NAVIGATION_BARS -> appendExtraPaddings(types = arrayOf(SystemInsetsType.BOTTOM), ignoredCutout = ignoredCutout)
            else -> error("Invalid SystemBars type.")
        }
    }

    /**
     * Hide system bars.
     *
     * You can change the system bars behavior by using [behavior].
     * @param type the system bars type.
     * @param removeExtraPaddings also remove the system bars extra paddings,
     * when [containerPaddingsCallback] exists, default true.
     * @param ignoredCutout whether to ignore the notch size,
     * if not ignored, it will stop handing on the notch size, default false.
     * @throws IllegalStateException if [type] is invalid.
     */
    @JvmOverloads
    fun hide(type: SystemBars, removeExtraPaddings: Boolean = containerPaddingsCallback != null, ignoredCutout: Boolean = false) {
        insetsController.hide(type.toInsetsType())
        if (removeExtraPaddings) when (type) {
            SystemBars.ALL -> removeExtraPaddings(types = arrayOf(SystemInsetsType.TOP, SystemInsetsType.BOTTOM), ignoredCutout = ignoredCutout)
            SystemBars.STATUS_BARS -> removeExtraPaddings(types = arrayOf(SystemInsetsType.TOP), ignoredCutout = ignoredCutout)
            SystemBars.NAVIGATION_BARS -> removeExtraPaddings(types = arrayOf(SystemInsetsType.BOTTOM), ignoredCutout = ignoredCutout)
            else -> error("Invalid SystemBars type.")
        }
    }

    /**
     * Apply the system bars extra paddings.
     *
     * See also [View.applySystemInsets].
     *
     * This function's inner callback [containerPaddingsCallback] will be re-called when the system insets changes.
     * @param types the system insets types, default are [SystemInsetsType.LEFT], [SystemInsetsType.TOP],
     * [SystemInsetsType.RIGHT], [SystemInsetsType.BOTTOM].
     * @param ignoredCutout whether to ignore the notch size,
     * if not ignored, it will stop handing on the notch size, default false.
     */
    @JvmOverloads
    fun applyExtraPaddings(
        vararg types: SystemInsetsType = arrayOf(
            SystemInsetsType.LEFT,
            SystemInsetsType.TOP,
            SystemInsetsType.RIGHT,
            SystemInsetsType.BOTTOM
        ),
        ignoredCutout: Boolean = false
    ) {
        containerPaddingsCallback = {
            val paddings = systemInsets?.let { containerLayout?.applySystemInsets(it, *types, ignoredCutout = ignoredCutout) }
            updateSystemBarsViewsHeight(paddings)
        }
        updateContainerPaddings()
    }

    /**
     * Append the system bars extra paddings.
     *
     * See also [View.appendSystemInsets].
     *
     * This function's inner callback [containerPaddingsCallback] will be re-called when the system insets changes.
     * @param types the system insets types.
     * @param ignoredCutout whether to ignore the notch size,
     * if not ignored, it will stop handing on the notch size, default false.
     */
    @JvmOverloads
    fun appendExtraPaddings(vararg types: SystemInsetsType, ignoredCutout: Boolean = false) {
        containerPaddingsCallback = {
            val paddings = systemInsets?.let { containerLayout?.appendSystemInsets(it, *types, ignoredCutout = ignoredCutout) }
            updateSystemBarsViewsHeight(paddings)
        }
        updateContainerPaddings()
    }

    /**
     * Remove the system bars extra paddings.
     *
     * See also [View.removeSystemInsets].
     *
     * This function's inner callback [containerPaddingsCallback] will be re-called when the system insets changes.
     * @param types the system insets types, default are [SystemInsetsType.LEFT], [SystemInsetsType.TOP],
     * [SystemInsetsType.RIGHT], [SystemInsetsType.BOTTOM].
     * @param ignoredCutout whether to ignore the notch size,
     * if not ignored, it will stop handing on the notch size, default false.
     */
    @JvmOverloads
    fun removeExtraPaddings(
        vararg types: SystemInsetsType = arrayOf(
            SystemInsetsType.LEFT,
            SystemInsetsType.TOP,
            SystemInsetsType.RIGHT,
            SystemInsetsType.BOTTOM
        ),
        ignoredCutout: Boolean = false
    ) {
        containerPaddingsCallback = {
            val paddings = systemInsets?.let { containerLayout?.removeSystemInsets(it, *types, ignoredCutout = ignoredCutout) }
            updateSystemBarsViewsHeight(paddings)
        }
        updateContainerPaddings()
    }

    /**
     * Determine whether the system bars is visible.
     * @param type the system bars type.
     * @return [Boolean]
     */
    fun isVisible(type: SystemBars) = windowInsets?.isVisible(type.toInsetsType()) ?: false

    /**
     * Show the system bars stub views.
     *
     * - This function is deprecated and no effect, use [applyExtraPaddings], [appendExtraPaddings] instead.
     */
    @Deprecated(message = "Use applyExtraPaddings, appendExtraPaddings instead.")
    fun showStub(type: SystemBars) {
    }

    /**
     * Hide the system bars stub views.
     *
     * - This function is deprecated and no effect, use [removeExtraPaddings] instead.
     */
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
        containerPaddingsCallback = null
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
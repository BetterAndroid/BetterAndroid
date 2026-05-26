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
 * This file is created by fankes on 2023/12/14.
 */
@file:Suppress("unused")
@file:JvmName("InsetsUtils")

package com.highcapable.betterandroid.ui.extension.insets.factory

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import com.highcapable.betterandroid.ui.extension.R
import com.highcapable.betterandroid.ui.extension.insets.InsetsWrapper
import com.highcapable.betterandroid.ui.extension.insets.WindowInsetsWrapper
import com.highcapable.betterandroid.ui.extension.view.AbsolutePadding
import com.highcapable.betterandroid.ui.extension.view.getTag
import com.highcapable.betterandroid.ui.extension.view.padding
import com.highcapable.betterandroid.ui.extension.view.setPadding

/**
 * Create a [WindowInsetsWrapper] from [WindowInsetsCompat].
 *
 * Please see [WindowInsetsWrapper.from] that why you may need the [window] parameter.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see WindowInsetsWrapper.from
 * @see View.handleOnWindowInsetsChanged
 * @receiver [WindowInsetsCompat]
 * @param window the current window, default is null.
 * @return [WindowInsetsWrapper]
 */
@JvmOverloads
fun WindowInsetsCompat.createWrapper(window: Window? = null) = WindowInsetsWrapper.from(windowInsets = this, window)

/**
 * Create a [WindowInsetsWrapper] from [View.getRootWindowInsets].
 *
 * Please see [WindowInsetsWrapper.from] that why you may need the [window] parameter.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see WindowInsetsWrapper.from
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param window the current window, default is null.
 * @return [WindowInsetsWrapper]
 */
@JvmOverloads
fun View.createRootWindowInsetsWrapper(window: Window? = null) = WindowInsetsWrapper.from(view = this, window)

/**
 * Convert [Insets] to [InsetsWrapper].
 * @receiver [Insets]
 * @param isVisible the visibility state, default true.
 * @return [InsetsWrapper]
 */
@JvmOverloads
fun Insets.toWrapper(isVisible: Boolean = true) = InsetsWrapper.of(left, top, right, bottom, isVisible)

/**
 * Convert [Insets] to [AbsolutePadding].
 * @receiver [Insets]
 * @return [AbsolutePadding]
 */
fun Insets.toPaddingValues() = AbsolutePadding(left, top, right, bottom)

/**
 * Convert [InsetsWrapper] to [AbsolutePadding].
 * @receiver [InsetsWrapper]
 * @return [AbsolutePadding]
 */
fun InsetsWrapper.toPaddingValues() = AbsolutePadding(left, top, right, bottom)

/**
 * Handle the window insets change for this view.
 *
 * This function is based on [ViewCompat.setOnApplyWindowInsetsListener]
 * and [ViewCompat.setWindowInsetsAnimationCallback].
 *
 * You can easy to get the [WindowInsetsWrapper] when the insets change.
 *
 * For the [consumed] parameter, set to true if you want to consume the insets for child views,
 * this will pass the [WindowInsetsCompat.CONSUMED], otherwise or default is false.
 *
 * Usage:
 *
 * ```kotlin
 * // Assume you are dealing with input method insets.
 * val imeSpaceLayout: FrameLayout
 * imeSpaceLayout.handleOnWindowInsetsChanged { imeSpaceLayout, insetsWrapper ->
 *     // Set the view insets padding by ime.
 *     imeSpaceLayout.setInsetsPadding(insetsWrapper.ime)
 *     // Or update the view insets bottom padding by ime.
 *     imeSpaceLayout.updateInsetsPadding(insetsWrapper.ime, bottom = true)
 * }
 * ```
 *
 * If you want to handle the window insets change with animation, just set the [animated] to true.
 *
 * - Note: The animation only works on Android 11 and above.
 *
 * Usage:
 *
 * ```kotlin
 * imeSpaceLayout.handleOnWindowInsetsChanged(animated = true) { imeSpaceLayout, insetsWrapper ->
 *    // The other code is same as above.
 * }
 * ```
 *
 * If you find that the callback does not take effect immediately, you can set [requestApplyOnLayout] to true.
 *
 * This function calls [removeWindowInsetsListener] before setting its listener, so it will replace the current
 * window insets listener and animation callback on this view.
 *
 * If you want to remove the window insets listener after, just call [View.removeWindowInsetsListener].
 * @see View.removeWindowInsetsListener
 * @see WindowInsetsWrapper
 * @see View.setInsetsPadding
 * @see View.updateInsetsPadding
 * @see ViewCompat.setOnApplyWindowInsetsListener
 * @see ViewCompat.setWindowInsetsAnimationCallback
 * @receiver [View] of [V].
 * @param consumed whether consume the insets for child views, default false.
 * @param animated whether handle the insets change with animation, default false.
 * @param animationDispatchMode the animation dispatch mode, default is [DISPATCH_MODE_CONTINUE_ON_SUBTREE].
 * @param requestApplyOnLayout whether request apply insets on layout, default false.
 * @param onChange the insets change callback.
 */
@JvmOverloads
fun <V : View> V.handleOnWindowInsetsChanged(
    consumed: Boolean = false,
    animated: Boolean = false,
    animationDispatchMode: Int = DISPATCH_MODE_CONTINUE_ON_SUBTREE,
    requestApplyOnLayout: Boolean = false,
    onChange: (V, insetsWrapper: WindowInsetsWrapper) -> Unit
) {
    // To avoid redundant listening, so remove them before adding.
    removeWindowInsetsListener()

    val self = this
    val windowFromActivity = (context as? Activity?)?.window
    var isAnimating = false

    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        // Ignored when the animation is running.
        if (!isAnimating) onChange(self, insets.createWrapper(windowFromActivity))

        if (consumed) WindowInsetsCompat.CONSUMED else insets
    }

    if (animated) ViewCompat.setWindowInsetsAnimationCallback(this, object : WindowInsetsAnimationCompat.Callback(animationDispatchMode) {

        override fun onPrepare(animation: WindowInsetsAnimationCompat) {
            super.onPrepare(animation)

            isAnimating = true
        }

        override fun onEnd(animation: WindowInsetsAnimationCompat) {
            super.onEnd(animation)

            isAnimating = false
        }

        override fun onProgress(
            insets: WindowInsetsCompat,
            runningAnimations: MutableList<WindowInsetsAnimationCompat>
        ): WindowInsetsCompat {
            onChange(self, insets.createWrapper(windowFromActivity))

            return if (consumed) WindowInsetsCompat.CONSUMED else insets
        }
    })

    if (requestApplyOnLayout) doOnLayout { it.requestApplyInsets() }
}

/**
 * Remove the window insets listener for this view.
 * @see View.handleOnWindowInsetsChanged
 * @see ViewCompat.setOnApplyWindowInsetsListener
 * @see ViewCompat.setWindowInsetsAnimationCallback
 * @receiver [View]
 */
fun View.removeWindowInsetsListener() {
    ViewCompat.setOnApplyWindowInsetsListener(this, null)
    ViewCompat.setWindowInsetsAnimationCallback(this, null)
}

/**
 * Set this view's padding with [insets].
 *
 * This function automatically records the current padding as the original baseline on first use,
 * then combines it with the current insets padding.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets.
 * @param left whether set the left padding.
 * @param top whether set the top padding.
 * @param right whether set the right padding.
 * @param bottom whether set the bottom padding.
 */
@JvmOverloads
fun View.setInsetsPadding(
    insets: Insets,
    left: Boolean = true,
    top: Boolean = true,
    right: Boolean = true,
    bottom: Boolean = true
) {
    val state = resolveInsetsPaddingState()
    val values = insets.toPaddingValues()
    val insetsPadding = AbsolutePadding(
        left = if (left) values.left else 0,
        top = if (top) values.top else 0,
        right = if (right) values.right else 0,
        bottom = if (bottom) values.bottom else 0
    )

    applyInsetsPadding(state.basePadding, insetsPadding)
}

/**
 * Set this view's padding with [insets].
 *
 * This function automatically records the current padding as the original baseline on first use,
 * then combines it with the current insets padding.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets.
 * @param horizontal whether set the horizontal padding.
 * @param vertical whether set the vertical padding.
 */
@JvmOverloads
@JvmName("setHVInsetsPadding")
fun View.setInsetsPadding(
    insets: Insets,
    horizontal: Boolean = true,
    vertical: Boolean = true
) = setInsetsPadding(insets, horizontal, vertical, horizontal, vertical)

/**
 * Set this view's padding with [insets].
 *
 * This function automatically records the current padding as the original baseline on first use,
 * then combines it with the current insets padding.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets' wrapper.
 * @param left whether set the left padding.
 * @param top whether set the top padding.
 * @param right whether set the right padding.
 * @param bottom whether set the bottom padding.
 */
@JvmOverloads
fun View.setInsetsPadding(
    insets: InsetsWrapper,
    left: Boolean = true,
    top: Boolean = true,
    right: Boolean = true,
    bottom: Boolean = true
) = setInsetsPadding(insets.toInsets(), left, top, right, bottom)

/**
 * Set this view's padding with [insets].
 *
 * This function automatically records the current padding as the original baseline on first use,
 * then combines it with the current insets padding.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets' wrapper.
 * @param horizontal whether set the horizontal padding.
 * @param vertical whether set the vertical padding.
 */
@JvmOverloads
@JvmName("setHVInsetsPadding")
fun View.setInsetsPadding(
    insets: InsetsWrapper,
    horizontal: Boolean = true,
    vertical: Boolean = true
) = setInsetsPadding(insets.toInsets(), horizontal, vertical)

/**
 * Update this view's padding with [insets].
 *
 * This function keeps the current original padding baseline and only updates the selected
 * insets padding directions.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets.
 * @param left whether update the left padding.
 * @param top whether update the top padding.
 * @param right whether update the right padding.
 * @param bottom whether update the bottom padding.
 */
@JvmOverloads
fun View.updateInsetsPadding(
    insets: Insets,
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false
) {
    val state = resolveInsetsPaddingState()
    val values = insets.toPaddingValues()
    val insetsPadding = state.insetsPadding.copy(
        left = if (left) values.left else state.insetsPadding.left,
        top = if (top) values.top else state.insetsPadding.top,
        right = if (right) values.right else state.insetsPadding.right,
        bottom = if (bottom) values.bottom else state.insetsPadding.bottom
    )

    applyInsetsPadding(state.basePadding, insetsPadding)
}

/**
 * Update this view's padding with [insets].
 *
 * This function keeps the current original padding baseline and only updates the selected
 * insets padding directions.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets' wrapper.
 * @param left whether update the left padding.
 * @param top whether update the top padding.
 * @param right whether update the right padding.
 * @param bottom whether update the bottom padding.
 */
@JvmOverloads
fun View.updateInsetsPadding(
    insets: InsetsWrapper,
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false
) = updateInsetsPadding(insets.toInsets(), left, top, right, bottom)

/**
 * Update this view's padding with [insets].
 *
 * This function keeps the current original padding baseline and only updates the selected
 * insets padding directions.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets.
 * @param horizontal whether update the horizontal padding.
 * @param vertical whether update the vertical padding.
 */
@JvmOverloads
@JvmName("updateHVInsetsPadding")
fun View.updateInsetsPadding(
    insets: Insets,
    horizontal: Boolean = false,
    vertical: Boolean = false
) = updateInsetsPadding(insets, horizontal, vertical, horizontal, vertical)

/**
 * Update this view's padding with [insets].
 *
 * This function keeps the current original padding baseline and only updates the selected
 * insets padding directions.
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
 * @see View.syncInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets' wrapper.
 * @param horizontal whether update the horizontal padding.
 * @param vertical whether update the vertical padding.
 */
@JvmOverloads
@JvmName("updateHVInsetsPadding")
fun View.updateInsetsPadding(
    insets: InsetsWrapper,
    horizontal: Boolean = false,
    vertical: Boolean = false
) = updateInsetsPadding(insets.toInsets(), horizontal, vertical)

/**
 * Synchronize the current padding back to the original insets padding baseline.
 *
 * This function does not re-apply the padding. It only updates the internally recorded baseline
 * using the current total padding minus the current applied insets padding.
 *
 * You can call this after manually changing the view's total padding.
 * @see View.setInsetsPadding
 * @see View.updateInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param left whether synchronize the left padding baseline.
 * @param top whether synchronize the top padding baseline.
 * @param right whether synchronize the right padding baseline.
 * @param bottom whether synchronize the bottom padding baseline.
 */
@JvmOverloads
fun View.syncInsetsPadding(
    left: Boolean = true,
    top: Boolean = true,
    right: Boolean = true,
    bottom: Boolean = true
) {
    val state = resolveInsetsPaddingState()
    val currentBasePadding = padding.toAbsolute() - state.insetsPadding
    val basePadding = state.basePadding.copy(
        left = if (left) currentBasePadding.left else state.basePadding.left,
        top = if (top) currentBasePadding.top else state.basePadding.top,
        right = if (right) currentBasePadding.right else state.basePadding.right,
        bottom = if (bottom) currentBasePadding.bottom else state.basePadding.bottom
    )

    setBaseInsetsPadding(basePadding)
}

/**
 * Synchronize the current padding back to the original insets padding baseline.
 *
 * This function does not re-apply the padding. It only updates the internally recorded baseline
 * using the current total padding minus the current applied insets padding.
 *
 * You can call this after manually changing the view's total padding.
 * @see View.setInsetsPadding
 * @see View.updateInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param horizontal whether synchronize the horizontal padding baseline.
 * @param vertical whether synchronize the vertical padding baseline.
 */
@JvmOverloads
@JvmName("syncHVInsetsPadding")
fun View.syncInsetsPadding(
    horizontal: Boolean = true,
    vertical: Boolean = true
) = syncInsetsPadding(horizontal, vertical, horizontal, vertical)

/**
 * Resolve the current insets padding state for this view, which contains the original baseline padding
 * and the currently applied insets padding.
 * @receiver [View]
 * @return [InsetsPaddingState]
 */
private fun View.resolveInsetsPaddingState(): InsetsPaddingState {
    val basePadding = getTag<AbsolutePadding>(R.id.tag_better_android_insets_padding_baseline)
        ?: padding.toAbsolute().also { setTag(R.id.tag_better_android_insets_padding_baseline, it) }
    val insetsPadding = getTag<AbsolutePadding>(R.id.tag_better_android_insets_padding_applied)
        ?: AbsolutePadding.Zero.also { setTag(R.id.tag_better_android_insets_padding_applied, it) }

    return InsetsPaddingState(basePadding, insetsPadding)
}

/**
 * Set the original baseline padding for this view, which is used to calculate the total padding with insets.
 * @receiver [View]
 * @param basePadding the original baseline padding.
 */
private fun View.setBaseInsetsPadding(basePadding: AbsolutePadding) {
    setTag(R.id.tag_better_android_insets_padding_baseline, basePadding)
}

/**
 * Set the currently applied insets padding for this view, which is used to calculate the total padding with insets.
 * @receiver [View]
 * @param insetsPadding the currently applied insets padding.
 */
private fun View.setAppliedInsetsPadding(insetsPadding: AbsolutePadding) {
    setTag(R.id.tag_better_android_insets_padding_applied, insetsPadding)
}

/**
 * Apply the insets padding to this view by combining the original baseline padding and the insets padding.
 * @receiver [View]
 * @param basePadding the original baseline padding.
 * @param insetsPadding the currently applied insets padding.
 */
private fun View.applyInsetsPadding(basePadding: AbsolutePadding, insetsPadding: AbsolutePadding) {
    setAppliedInsetsPadding(insetsPadding)
    setPadding(basePadding + insetsPadding)
}

/**
 * The internal state data class for insets padding,
 * which contains the original baseline padding and the currently applied insets padding.
 */
private data class InsetsPaddingState(
    val basePadding: AbsolutePadding,
    val insetsPadding: AbsolutePadding
)
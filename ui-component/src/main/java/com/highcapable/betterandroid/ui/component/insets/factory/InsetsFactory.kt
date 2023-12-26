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
@file:Suppress("unused")
@file:JvmName("InsetsUtils")

package com.highcapable.betterandroid.ui.component.insets.factory

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.highcapable.betterandroid.ui.component.insets.InsetsWrapper
import com.highcapable.betterandroid.ui.component.insets.WindowInsetsWrapper

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
 * Handle the window insets change for this view.
 *
 * This function is based on [ViewCompat.setOnApplyWindowInsetsListener]
 * and [ViewCompat.setWindowInsetsAnimationCallback].
 *
 * You can easy to get the [WindowInsetsWrapper] when the insets change.
 *
 * For the [onChange] callback, return true if you want to consume the insets for child views,
 * this will pass the [WindowInsetsCompat.CONSUMED], otherwise return false.
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
 *     false // Whether to consume the insets for child views or not.
 * }
 * ```
 *
 * If you want to handle the window insets change with animation, just set the [animated] to true.
 *
 * Usage:
 *
 * ```kotlin
 * imeSpaceLayout.handleOnWindowInsetsChanged(animated = true) { imeSpaceLayout, insetsWrapper ->
 *    // The other code is same as above.
 * }
 * ```
 *
 * If you want to remove the window insets listener after, just call [View.removeWindowInsetsListener].
 * @see View.removeWindowInsetsListener
 * @see WindowInsetsWrapper
 * @see View.setInsetsPadding
 * @see View.updateInsetsPadding
 * @see ViewCompat.setOnApplyWindowInsetsListener
 * @see ViewCompat.setWindowInsetsAnimationCallback
 * @receiver [View] of [V].
 * @param animated whether handle the insets change with animation, default false.
 * @param animationDispatchMode the animation dispatch mode, default is [DISPATCH_MODE_CONTINUE_ON_SUBTREE].
 * @param onChange the insets change callback.
 */
@JvmOverloads
fun <V : View> V.handleOnWindowInsetsChanged(
    animated: Boolean = false,
    animationDispatchMode: Int = DISPATCH_MODE_CONTINUE_ON_SUBTREE,
    onChange: (V, insetsWrapper: WindowInsetsWrapper) -> Boolean
) {
    // To avoid redundant listening, so remove them before adding.
    removeWindowInsetsListener()
    val self = this
    val windowFromActivity = (context as? Activity?)?.window
    var isAnimating = false
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        // Ignored when the animation is running.
        if (animated && isAnimating) return@setOnApplyWindowInsetsListener insets
        val consumed = onChange(self, insets.createWrapper(windowFromActivity))
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
            val consumed = onChange(self, insets.createWrapper(windowFromActivity))
            return if (consumed) WindowInsetsCompat.CONSUMED else insets
        }
    })
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
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
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
) = setPadding(
    if (left) insets.left else 0,
    if (top) insets.top else 0,
    if (right) insets.right else 0,
    if (bottom) insets.bottom else 0
)

/**
 * Set this view's padding with [insets].
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
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
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets wrapper.
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
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.updateInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets wrapper.
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
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
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
    if (left) updatePadding(left = insets.left)
    if (top) updatePadding(top = insets.top)
    if (right) updatePadding(right = insets.right)
    if (bottom) updatePadding(bottom = insets.bottom)
}

/**
 * Update this view's padding with [insets].
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets wrapper.
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
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
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
) {
    if (horizontal) updatePadding(left = insets.left, right = insets.right)
    if (vertical) updatePadding(top = insets.top, bottom = insets.bottom)
}

/**
 * Update this view's padding with [insets].
 *
 * You can use [View.handleOnWindowInsetsChanged] to handle the insets change.
 * @see View.setInsetsPadding
 * @see View.handleOnWindowInsetsChanged
 * @receiver [View]
 * @param insets the insets wrapper.
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
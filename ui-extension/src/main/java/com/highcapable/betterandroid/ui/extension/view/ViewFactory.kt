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
 * This file is created by fankes on 2023/10/25.
 */
@file:Suppress("unused", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
@file:JvmName("ViewUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.Px
import androidx.core.content.getSystemService
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.highcapable.betterandroid.ui.extension.component.base.toHexResourceId
import com.highcapable.yukireflection.factory.classOf

/**
 * Get the view's location on screen.
 * @receiver [View]
 * @return [Point]
 */
val View.location
    get() = runCatching {
        val locations = IntArray(2)
        getLocationInWindow(locations)
        Point(locations[0], locations[1])
    }.getOrNull() ?: Point()

/**
 * Remove self from its parent view using [ViewGroup.removeView].
 * @receiver [View]
 */
fun View.removeSelf() {
    (parent as? ViewGroup?)?.removeView(this)
}

/**
 * Remove self from its parent view using [ViewGroup.removeViewInLayout].
 * @receiver [View]
 */
fun View.removeSelfInLayout() {
    (parent as? ViewGroup?)?.removeViewInLayout(this)
}

/**
 * Pop up the soft input.
 *
 * - Note: This function may not succeed every time depending on the usage scenario.
 * @receiver [View]
 */
fun View.showSoftInput() {
    context?.getSystemService<InputMethodManager>()?.showSoftInput(this, 0)
}

/**
 * Disappear the soft input.
 *
 * - Note: This function may not succeed every time depending on the usage scenario.
 * @receiver [View]
 */
fun View.hideSoftInput() {
    context?.getSystemService<InputMethodManager>()
        ?.also { if (it.isActive) it.hideSoftInputFromWindow(applicationWindowToken, 0) }
}

/**
 * Simulate the key down and up events.
 * @receiver [View]
 * @param keyCode the key code.
 */
fun View.performKeyPressed(keyCode: Int) {
    onKeyDown(keyCode, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
    onKeyUp(keyCode, KeyEvent(KeyEvent.ACTION_UP, keyCode))
}

/**
 * Simulate the touch events.
 *
 * Same like [View.performClick].
 * @receiver [View]
 * @param downX the down x coordinate.
 * @param downY the down y coordinate.
 * @param upX the up x coordinate.
 * @param upY the up y coordinate.
 * @param duration the touching duration time.
 */
fun View.performTouch(downX: Float, downY: Float, upX: Float, upY: Float, duration: Long) {
    val metaState = 0
    val downTime = System.currentTimeMillis()
    val eventTime = downTime + duration
    val motionEventDown = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, downX, downY, metaState)
    dispatchTouchEvent(motionEventDown)
    val motionEventUp = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, upX, upY, metaState)
    dispatchTouchEvent(motionEventUp)
}

/**
 * Updates this view's horizontal or vertical padding.
 * @see View.updatePadding
 * @receiver [View]
 * @param horizontal the horizontal padding (px).
 * @param vertical the vertical padding (px).
 */
@JvmOverloads
fun View.updatePadding(@Px horizontal: Int = -1, @Px vertical: Int = -1) {
    if (horizontal >= 0) updatePadding(left = horizontal, right = horizontal)
    if (vertical >= 0) updatePadding(top = vertical, bottom = vertical)
}

/**
 * Updates this view's margin.
 *
 * Same like [View.updatePadding].
 *
 * This view layout params need to be a [ViewGroup.MarginLayoutParams].
 * @receiver [View]
 * @param left the left margin (px).
 * @param top the top margin (px).
 * @param right the right margin (px).
 * @param bottom the bottom margin (px).
 */
@JvmOverloads
fun View.updateMargin(
    @Px left: Int = marginLeft,
    @Px top: Int = marginTop,
    @Px right: Int = marginRight,
    @Px bottom: Int = marginBottom
) {
    if (layoutParams !is ViewGroup.MarginLayoutParams) return
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = left
        topMargin = top
        rightMargin = right
        bottomMargin = bottom
    }
}

/**
 * Inflate a view using [resId].
 * @receiver the root view.
 * @param resId the view id.
 * @param attachToRoot whether to attach to the root view, default false.
 * @return [View]
 */
@JvmOverloads
fun ViewGroup.inflate(resId: Int, attachToRoot: Boolean = false): View = LayoutInflater.from(context).inflate(resId, this, attachToRoot)

/**
 * Inflate a view using [resId].
 * @receiver the current context.
 * @param resId the view id.
 * @param parent the root view, default is null.
 * @param attachToRoot whether to attach to the root view, default false.
 * @return [View]
 */
@JvmOverloads
fun Context.inflate(resId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View =
    (if (this is Activity) layoutInflater else LayoutInflater.from(this)).inflate(resId, parent, attachToRoot)

/**
 * Inflate a view using [resId].
 * @receiver the root view.
 * @param resId the view id.
 * @param attachToRoot whether to attach to the root view, default false.
 * @return [V]
 * @throws IllegalStateException if [resId] is invalid.
 */
@JvmName("inflateTyped")
inline fun <reified V : View> ViewGroup.inflate(resId: Int, attachToRoot: Boolean = false): V =
    inflate(resId, attachToRoot) as? V? ?: error("This view with ID ${resId.toHexResourceId()} is not a type of ${classOf<V>()}")

/**
 * Inflate a view using [resId].
 * @receiver the current context.
 * @param resId the view id.
 * @param parent the root view, default is null.
 * @param attachToRoot whether to attach to the root view, default false.
 * @return [V]
 * @throws IllegalStateException if [resId] is invalid.
 */
@JvmName("inflateTyped")
inline fun <reified V : View> Context.inflate(resId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): V =
    inflate(resId, parent, attachToRoot) as? V? ?: error("This view with ID ${resId.toHexResourceId()} is not a type of ${classOf<V>()}")

/**
 * Views layout params tool.
 *
 * Its purpose is to make it easier to create a layout params.
 */
object ViewLayoutParams {

    /** An unspecific dimension. */
    private const val UNSPEC_DIMEN = -3

    /**
     * Create a new [ViewGroup.LayoutParams].
     *
     * Usage:
     *
     * ```kotlin
     * // Create a LinearLayout.LayoutParams with WRAP_CONTENT.
     * val linLayoutParams = ViewLayoutParams.create<LinearLayout.LayoutParams>()
     * // Create a FrameLayout.LayoutParams with MATCH_PARENT.
     * val fraLayoutParams = ViewLayoutParams.create<FrameLayout.LayoutParams>(matchParent = true)
     * ```
     * @param matchParent set width and height to [ViewGroup.LayoutParams.MATCH_PARENT], default false.
     * @param widthMatchParent set width to [ViewGroup.LayoutParams.MATCH_PARENT], default false.
     * @param heightMatchParent set height to [ViewGroup.LayoutParams.MATCH_PARENT], default false.
     * @param width the layout params width (px), default is [UNSPEC_DIMEN].
     * @param height the layout params height (px), default is [UNSPEC_DIMEN].
     * @return [VGLP]
     * @throws IllegalStateException if [VGLP] is not supported.
     */
    @JvmStatic
    inline fun <reified VGLP : ViewGroup.LayoutParams> create(
        matchParent: Boolean = false,
        widthMatchParent: Boolean = false,
        heightMatchParent: Boolean = false,
        @Px width: Int = UNSPEC_DIMEN,
        @Px height: Int = UNSPEC_DIMEN
    ): VGLP {
        val absWidth = when {
            width != UNSPEC_DIMEN -> width
            matchParent || widthMatchParent -> ViewGroup.LayoutParams.MATCH_PARENT
            else -> ViewGroup.LayoutParams.WRAP_CONTENT
        }
        val absHeight = when {
            height != UNSPEC_DIMEN -> height
            matchParent || heightMatchParent -> ViewGroup.LayoutParams.MATCH_PARENT
            else -> ViewGroup.LayoutParams.WRAP_CONTENT
        }
        return when (classOf<VGLP>()) {
            classOf<LinearLayout.LayoutParams>() -> LinearLayout.LayoutParams(absWidth, absHeight) as VGLP
            classOf<RelativeLayout.LayoutParams>() -> RelativeLayout.LayoutParams(absWidth, absHeight) as VGLP
            classOf<FrameLayout.LayoutParams>() -> FrameLayout.LayoutParams(absWidth, absHeight) as VGLP
            classOf<ViewGroup.MarginLayoutParams>() -> ViewGroup.MarginLayoutParams(absWidth, absHeight) as VGLP
            classOf<ViewGroup.LayoutParams>() -> ViewGroup.LayoutParams(absWidth, absHeight) as VGLP
            else -> error("Unknown ViewGroup.LayoutParams type ${classOf<VGLP>()}.")
        }
    }
}
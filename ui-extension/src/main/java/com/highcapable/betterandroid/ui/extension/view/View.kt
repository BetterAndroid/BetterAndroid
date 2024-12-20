/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2024 HighCapable
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
@file:Suppress("unused", "FunctionName", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "EXTENSION_SHADOWED_BY_MEMBER")
@file:JvmName("ViewUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.app.Activity
import android.content.Context
import android.graphics.Outline
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.ViewPropertyAnimator
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.Px
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
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
 * Get the view's tag.
 * @receiver [View]
 * @return [T] or null.
 */
inline fun <reified T> View.tag() = tag as? T?

/**
 * Get the view's tag.
 * @receiver [View]
 * @param key the key name.
 * @param default the default value.
 * @return [T]
 */
inline fun <reified T> View.getTag(key: Int, default: T) = getTag<T>(key) ?: default

/**
 * Get the view's tag.
 * @receiver [View]
 * @param key the key name.
 * @return [T] or null.
 */
inline fun <reified T> View.getTag(key: Int) = getTag(key) as? T?

/**
 * Get the view's parent view [VG].
 * @see ViewGroup.parentOrNull
 * @receiver [View]
 * @return [VG]
 * @throws IllegalStateException if the parent view is not a type of [VG] or is null.
 */
@JvmName("parentTyped")
inline fun <reified VG : ViewGroup> View.parent() = parent as? VG?
    ?: error("This view's parent is not a type of ${classOf<VG>()} or is null.")

/**
 * Get the view's parent view [VG].
 * @see ViewGroup.parent
 * @receiver [View]
 * @return [VG] or null.
 */
@JvmName("parentOrNullTyped")
inline fun <reified VG : ViewGroup> View.parentOrNull() = parent as? VG?

/**
 * Get the view's parent view.
 * @see ViewGroup.parentOrNull
 * @receiver [View]
 * @return [ViewGroup]
 * @throws IllegalStateException if the parent view is not a type of [ViewGroup].
 */
fun View.parent() = parent<ViewGroup>()

/**
 * Get the view's parent view.
 * @see ViewGroup.parent
 * @receiver [View]
 * @return [ViewGroup] or null.
 */
fun View.parentOrNull() = parentOrNull<ViewGroup>()

/**
 * Get the view [V] by [index] in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @param index the index of the view.
 * @return [V]
 * @throws IllegalStateException if the view at [index] is not a type of [V] or is null.
 */
@JvmName("childTyped")
inline fun <reified V : View> ViewGroup.child(index: Int) = getChildAt(index) as? V?
    ?: error("This view at $index is not a type of ${classOf<V>()} or is null.")

/**
 * Get the view [V] by [index] in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @param index the index of the view.
 * @return [V] or null.
 */
@JvmName("childOrNullTyped")
inline fun <reified V : View> ViewGroup.childOrNull(index: Int) = getChildAt(index) as? V?

/**
 * Get the first child view [V] in its parent view.
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [V]
 * @throws IllegalStateException if the first child view is not a type of [V] or is null.
 */
inline fun <reified V : View> ViewGroup.firstChild() = child<V>(index = 0)

/**
 * Get the last child view [V] in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [V]
 * @throws IllegalStateException if the last child view is not a type of [V] or is null.
 */
inline fun <reified V : View> ViewGroup.lastChild() = child<V>(index = childCount - 1)

/**
 * Get the first child view [V] in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [V] or null.
 */
inline fun <reified V : View> ViewGroup.firstChildOrNull() = childOrNull<V>(index = 0)

/**
 * Get the last child view [V] in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.lastChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [V] or null.
 */
inline fun <reified V : View> ViewGroup.lastChildOrNull() = childOrNull<V>(index = childCount - 1)

/**
 * Get the view by [index] in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @param index the index of the view.
 * @return [View]
 * @throws IllegalStateException if the view at [index] is null.
 */
fun ViewGroup.child(index: Int) = child<View>(index)

/**
 * Get the view by [index] in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @param index the index of the view.
 * @return [View] or null.
 */
fun ViewGroup.childOrNull(index: Int) = childOrNull<View>(index)

/**
 * Get the first child view in its parent view.
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [View]
 * @throws IllegalStateException if the first child view is null.
 */
val ViewGroup.firstChild get() = firstChild<View>()

/**
 * Get the last child view in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [View]
 * @throws IllegalStateException if the last child view is null.
 */
val ViewGroup.lastChild get() = lastChild<View>()

/**
 * Get the first child view in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.lastChild
 * @see ViewGroup.lastChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [View] or null.
 */
val ViewGroup.firstChildOrNull get() = firstChildOrNull<View>()

/**
 * Get the last child view in its parent view.
 * @see ViewGroup.firstChild
 * @see ViewGroup.lastChild
 * @see ViewGroup.firstChildOrNull
 * @see ViewGroup.child
 * @see ViewGroup.childOrNull
 * @see ViewGroup.getChildAt
 * @receiver [ViewGroup]
 * @return [View] or null.
 */
val ViewGroup.lastChildOrNull get() = lastChildOrNull<View>()

/**
 * Remove self from its parent view using [ViewGroup.removeView].
 * @receiver [View]
 */
fun View.removeSelf() {
    parentOrNull()?.removeView(this)
}

/**
 * Remove self from its parent view using [ViewGroup.removeViewInLayout].
 * @receiver [View]
 */
fun View.removeSelfInLayout() {
    parentOrNull()?.removeViewInLayout(this)
}

/**
 * Show the input method.
 *
 * - Note: This function may not work on Android lower than 11
 *   or when the current [View] is not in [Activity].
 * @receiver [View]
 */
fun View.showIme() {
    /** Whatever try to show the input method. */
    fun showSoftInput() {
        @Suppress("DEPRECATION")
        context?.getSystemService<InputMethodManager>()
            ?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
    val windowFromActivity = (context as? Activity?)?.window
    if (SystemVersion.isHighOrEqualsTo(SystemVersion.R) && windowFromActivity != null)
        WindowCompat.getInsetsController(windowFromActivity, this).show(WindowInsetsCompat.Type.ime())
    else showSoftInput()
}

/**
 * Hide the input method.
 *
 * - Note: This function may not work on Android lower than 11
 *   or when the current [View] is not in [Activity].
 * @receiver [View]
 */
fun View.hideIme() {
    /** Whatever try to hide the input method. */
    fun hideSoftInput() {
        context?.getSystemService<InputMethodManager>()
            ?.also { if (it.isActive) it.hideSoftInputFromWindow(applicationWindowToken, 0) }
    }
    val windowFromActivity = (context as? Activity?)?.window
    if (SystemVersion.isHighOrEqualsTo(SystemVersion.R) && windowFromActivity != null)
        WindowCompat.getInsetsController(windowFromActivity, this).hide(WindowInsetsCompat.Type.ime())
    else hideSoftInput()
}

/**
 * Show the soft input.
 *
 * - This function is deprecated, use [View.showIme] instead.
 */
@Deprecated(message = "Use View.showIme instead.", ReplaceWith("showIme()"))
fun View.showSoftInput() = showIme()

/**
 * Disappear the soft input.
 *
 * - This function is deprecated, use [View.hideIme] instead.
 */
@Deprecated(message = "Use View.hideIme instead.", ReplaceWith("hideIme()"))
fun View.hideSoftInput() = hideIme()

/**
 * Animate the view.
 * @receiver [View]
 * @param action the view property animator action.
 */
fun View.animate(action: ViewPropertyAnimator.() -> Unit) = animate().apply(action).start()

/**
 * Simulate the key down and up events.
 * @receiver [View]
 * @param keyCode the key code.
 * @param duration the key pressing duration time, default is 150.
 */
@JvmOverloads
fun View.performKeyPressed(keyCode: Int, duration: Long = 150) {
    onKeyDown(keyCode, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
    Handler(Looper.getMainLooper()).postDelayed({ onKeyUp(keyCode, KeyEvent(KeyEvent.ACTION_UP, keyCode)) }, duration)
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
 * Set the view's click listener with interval.
 * @see View.setOnClickListener
 * @receiver [View]
 * @param timeMillis the interval time in milliseconds, default is 300.
 * @param listener the click listener.
 */
@JvmOverloads
fun View.setIntervalOnClickListener(timeMillis: Long = 300L, listener: View.OnClickListener) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < timeMillis)
            return@setOnClickListener
        lastClickTime = currentTime
        listener.onClick(view)
    }
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
 * Updates this view's horizontal or vertical margin.
 *
 * Same like [View.updatePadding].
 *
 * This view layout params need to be a [ViewGroup.MarginLayoutParams].
 * @receiver [View]
 * @param horizontal the horizontal margin (px).
 * @param vertical the vertical margin (px).
 */
@JvmOverloads
@JvmName("updateHVMargin")
fun View.updateMargin(@Px horizontal: Int = -1, @Px vertical: Int = -1) {
    if (horizontal >= 0) updateMargin(left = horizontal, right = horizontal)
    if (vertical >= 0) updateMargin(top = vertical, bottom = vertical)
}

/**
 * Walk to the view's parent views.
 * @receiver [View]
 * @return [List]<[View]>
 */
fun View.walkToRoot(): List<View> {
    val views = mutableListOf<View>()
    var current: View? = this
    while (current != null) {
        views.add(current)
        current = current.parentOrNull()
    }; return views
}

/**
 * Walk through the view's children views.
 * @receiver [ViewGroup]
 * @return [List]<[View]>
 */
fun ViewGroup.walkThroughChildren(): List<View> {
    val children = mutableListOf<View>()
    this.children.forEach { child ->
        children.add(child)
        if (child is ViewGroup) children.addAll(child.walkThroughChildren())
    }; return children
}

/**
 * Get the view's index in its parent view.
 *
 * If the parent view is not exists, return -1.
 * @see ViewGroup.indexOfChild
 * @receiver [View]
 * @return [Int]
 */
fun View.indexOfInParent() = parentOrNull()?.indexOfChild(this) ?: -1

/**
 * Set the view's outline provider.
 * @receiver [V]
 * @param provider the outline provider callback.
 */
inline fun <reified V : View> V.outlineProvider(crossinline provider: (view: V, outline: Outline) -> Unit) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            provider(view as V, outline)
        }
    }
}

/**
 * Inflate a view using [resId].
 *
 * - This function is deprecated, use [LayoutInflater.inflate] instead.
 */
@Deprecated(
    message = "Use LayoutInflater.inflate instead.",
    ReplaceWith("layoutInflater.inflate<View>(resId, parent, attachToRoot)", "android.view.View")
)
@JvmOverloads
fun Context.inflate(resId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false) =
    layoutInflater.inflate<View>(resId, parent, attachToRoot)

/**
 * Inflate a view using [resId].
 *
 * - This function is deprecated, use [LayoutInflater.inflate] instead.
 */
@Deprecated(
    message = "Use LayoutInflater.inflate instead.",
    ReplaceWith("layoutInflater.inflate<V>(resId, parent, attachToRoot)")
)
@JvmName("inflateTyped")
inline fun <reified V : View> Context.inflate(resId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): V =
    layoutInflater.inflate<V>(resId, parent, attachToRoot)

/**
 * Inflate a view using [resId].
 *
 * - This function is deprecated, use [LayoutInflater.inflate] instead.
 */
@Deprecated(
    message = "Use LayoutInflater.inflate instead.",
    ReplaceWith("context.layoutInflater.inflate<View>(resId, root = this, attachToRoot)", "android.view.View")
)
@JvmOverloads
fun ViewGroup.inflate(resId: Int, attachToRoot: Boolean = false) =
    context.layoutInflater.inflate<View>(resId, root = this, attachToRoot)

/**
 * Inflate a view using [resId].
 *
 * - This function is deprecated, use [LayoutInflater.inflate] instead.
 */
@Deprecated(
    message = "Use LayoutInflater.inflate instead.",
    ReplaceWith("context.layoutInflater.inflate<V>(resId, root = this, attachToRoot)")
)
@JvmName("inflateTyped")
inline fun <reified V : View> ViewGroup.inflate(resId: Int, attachToRoot: Boolean = false) =
    context.layoutInflater.inflate<V>(resId, root = this, attachToRoot)

/**
 * Create a new [ViewGroup.LayoutParams].
 *
 * Usage:
 *
 * ```kotlin
 * // Create a LinearLayout.LayoutParams with fully WRAP_CONTENT.
 * val linLayoutParams = ViewLayoutParams<LinearLayout.LayoutParams>()
 * // Create a FrameLayout.LayoutParams with fully MATCH_PARENT.
 * val fraLayoutParams = ViewLayoutParams<FrameLayout.LayoutParams>(matchParent = true)
 * ```
 * @param width the layout params width (px).
 * @param height the layout params height (px).
 * @param matchParent set width and height to [LayoutParamsMatchParent], default false.
 * @param widthMatchParent set width to [LayoutParamsMatchParent], default false.
 * @param heightMatchParent set height to [LayoutParamsMatchParent], default false.
 * @return [VGLP]
 * @throws IllegalStateException if [VGLP] is not supported.
 */
inline fun <reified VGLP : ViewGroup.LayoutParams> ViewLayoutParams(
    @Px width: Int = LayoutParamsUnspecified,
    @Px height: Int = LayoutParamsUnspecified,
    matchParent: Boolean = false,
    widthMatchParent: Boolean = false,
    heightMatchParent: Boolean = false
): VGLP {
    val absWidth = when {
        width != LayoutParamsUnspecified -> width
        matchParent || widthMatchParent -> LayoutParamsMatchParent
        else -> LayoutParamsWrapContent
    }
    val absHeight = when {
        height != LayoutParamsUnspecified -> height
        matchParent || heightMatchParent -> LayoutParamsMatchParent
        else -> LayoutParamsWrapContent
    }
    return when (classOf<VGLP>()) {
        classOf<AbsListView.LayoutParams>() -> AbsListView.LayoutParams(absWidth, absHeight) as VGLP
        classOf<LinearLayout.LayoutParams>() -> LinearLayout.LayoutParams(absWidth, absHeight) as VGLP
        classOf<RelativeLayout.LayoutParams>() -> RelativeLayout.LayoutParams(absWidth, absHeight) as VGLP
        classOf<FrameLayout.LayoutParams>() -> FrameLayout.LayoutParams(absWidth, absHeight) as VGLP
        classOf<ViewGroup.MarginLayoutParams>() -> ViewGroup.MarginLayoutParams(absWidth, absHeight) as VGLP
        classOf<ViewGroup.LayoutParams>() -> ViewGroup.LayoutParams(absWidth, absHeight) as VGLP
        else -> error("Unknown ViewGroup.LayoutParams type ${classOf<VGLP>()}.")
    }
}

/** Reference to [ViewGroup.LayoutParams.MATCH_PARENT]. */
const val LayoutParamsMatchParent = ViewGroup.LayoutParams.MATCH_PARENT

/** Reference to [ViewGroup.LayoutParams.WRAP_CONTENT]. */
const val LayoutParamsWrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

/** The unspecified layout params value. */
private const val LayoutParamsUnspecified = LayoutParamsWrapContent - 1

/**
 * Views layout params tool.
 *
 * - This class is deprecated, use [ViewLayoutParams] function instead.
 */
@Deprecated(message = "Use ViewLayoutParams function instead.")
object ViewLayoutParams {

    /**
     * Create a new [ViewGroup.LayoutParams].
     *
     * - This function is deprecated, use [ViewLayoutParams] function instead.
     */
    @Deprecated(
        message = "Use ViewLayoutParams instead.",
        replaceWith = ReplaceWith("ViewLayoutParams(matchParent, widthMatchParent, heightMatchParent, width, height)")
    )
    inline fun <reified VGLP : ViewGroup.LayoutParams> create(
        matchParent: Boolean = false,
        widthMatchParent: Boolean = false,
        heightMatchParent: Boolean = false,
        @Px width: Int = LayoutParamsUnspecified,
        @Px height: Int = LayoutParamsUnspecified
    ) = ViewLayoutParams<VGLP>(width, height, matchParent, widthMatchParent, heightMatchParent)
}
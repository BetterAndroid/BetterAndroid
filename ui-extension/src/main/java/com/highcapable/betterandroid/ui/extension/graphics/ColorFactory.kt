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
 * This file is created by fankes on 2022/10/22.
 */
@file:Suppress("unused")
@file:JvmName("ColorUtils")

package com.highcapable.betterandroid.ui.extension.graphics

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import android.R as Android_R

/**
 * Determine whether the current color is a bright tone.
 * @receiver the current color.
 * @return [Boolean]
 */
val @receiver:ColorInt Int.isBrightColor get() = runCatching {
    (1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255) < 0.5
}.getOrNull() ?: false

/**
 * Convert integer color to hex string.
 * @receiver the current color.
 * @return [String]
 */
@JvmName("convertToHexColor")
fun @receiver:ColorInt Int.toHexColor() = runCatching {
    var r = Integer.toHexString(Color.red(this))
    var g = Integer.toHexString(Color.green(this))
    var b = Integer.toHexString(Color.blue(this))
    r = if (r.length == 1) "0$r" else r
    g = if (g.length == 1) "0$g" else g
    b = if (b.length == 1) "0$b" else b
    buildString {
        append("#")
        append(r.uppercase())
        append(g.uppercase())
        append(b.uppercase())
    }
}.getOrNull() ?: "#00000000"

/**
 * Use (0.0f - 1.0f) to modify color transparency.
 * @receiver the current color.
 * @param value the transparency (0.0f - 1.0f).
 * @return [Int] modified color.
 * @throws IllegalStateException if the wrong [value] range is passed in.
 */
@JvmName("convertToAlphaColor")
fun @receiver:ColorInt Int.toAlphaColor(value: Float) = if (value in 0f..1f)
    runCatching { (255.coerceAtMost(0.coerceAtLeast((value * 255).toInt())) shl 24) + (0x00ffffff and this) }.getOrNull() ?: this
else error("Color alpha must be between 0f and 1f.")

/**
 * Use (0 - 255) to modify color transparency.
 *
 * Depends on [ColorUtils.setAlphaComponent]
 * @receiver the current color..
 * @param value the transparency (0 - 255).
 * @return [Int] modified color.
 * @throws IllegalStateException if the wrong [value] range is passed in.
 */
@JvmName("convertToAlphaColor")
fun @receiver:ColorInt Int.toAlphaColor(value: Int) = if (value in 0..255)
    runCatching { ColorUtils.setAlphaComponent(this, value) }.getOrNull() ?: this
else error("Color alpha must be between 0 and 255.")

/**
 * Converts to [AttrState.NORMAL] state of [ColorStateList].
 * @receiver the current color.
 * @return [ColorStateList]
 */
@JvmName("convertToNormalColorStateList")
fun @receiver:ColorInt Int.toNormalColorStateList() = ColorStateList(AttrState.NORMAL to this)

/**
 * Converts to [AttrState.NORMAL] state of [ColorStateList].
 *
 * If the current color is [Color.TRANSPARENT] will be return null.
 * @receiver the current color.
 * @return [ColorStateList] or null
 */
@JvmName("convertToNullableColorStateList")
fun @receiver:ColorInt Int.toNullableColorStateList() = if (this == Color.TRANSPARENT) null else toNormalColorStateList()

/**
 * Mix two colors.
 * @param color1 the first color.
 * @param color2 the second color.
 * @param ratio the mixing ratio, default 0.5f.
 * @return [Int] mixed color.
 */
@JvmOverloads
fun mixColorOf(@ColorInt color1: Int, @ColorInt color2: Int, ratio: Float = 0.5f): Int {
    val inverse = 1 - ratio
    val a = (color1 ushr 24) * inverse + (color2 ushr 24) * ratio
    val r = (color1 shr 16 and 0xFF) * inverse + (color2 shr 16 and 0xFF) * ratio
    val g = (color1 shr 8 and 0xFF) * inverse + (color2 shr 8 and 0xFF) * ratio
    val b = (color1 and 0xFF) * inverse + (color2 and 0xFF) * ratio
    return a.toInt() shl 24 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
}

/**
 * Converts to mix color.
 *
 * - This function is deprecated, use [mixColorOf] instead.
 */
@Deprecated(message = "Use mixColorOf instead.", ReplaceWith("mixColorOf(this, color, ratio)"))
@JvmOverloads
@JvmName("convertToMixColor")
fun @receiver:ColorInt Int.toMixColor(@ColorInt color: Int, ratio: Float = 0.5f) = mixColorOf(color1 = this, color, ratio)

/**
 * Event state attribute ID list definition.
 */
object AttrState {
    const val NORMAL = 0x0
    const val CHECKED = Android_R.attr.state_checked
    const val ENABLED = Android_R.attr.state_enabled
    const val PRESSED = Android_R.attr.state_pressed
    const val FOCUSED = Android_R.attr.state_focused
    const val ABOVE_ANCHOR = Android_R.attr.state_above_anchor
    const val ACCELERATED = Android_R.attr.state_accelerated
    const val ACTIVATED = Android_R.attr.state_activated
    const val ACTIVE = Android_R.attr.state_active
    const val CHECKABLE = Android_R.attr.state_checkable
    const val DRAG_CAN_ACCEPT = Android_R.attr.state_drag_can_accept
    const val DRAG_HOVERED = Android_R.attr.state_drag_hovered
    const val EMPTY = Android_R.attr.state_empty
    const val EXPANDED = Android_R.attr.state_expanded
    const val FIRST = Android_R.attr.state_first
    const val HOVERED = Android_R.attr.state_hovered
    const val LAST = Android_R.attr.state_last
    const val MIDDLE = Android_R.attr.state_middle
    const val MULTILINE = Android_R.attr.state_multiline
    const val SELECTED = Android_R.attr.state_selected
    const val SINGLE = Android_R.attr.state_single
    const val WINDOW_FOCUSED = Android_R.attr.state_window_focused

    @Suppress("DEPRECATION")
    @Deprecated(message = "Deprecated in Java")
    const val LONG_PRESSABLE = Android_R.attr.state_long_pressable
}

/**
 * Create a new [ColorStateList].
 *
 * Please use [AttrState] to set the preset state.
 *
 * Usage:
 *
 * ```kotlin
 * ColorStateList(
 *     AttrState.CHECKED to Color.WHITE,
 *     AttrState.PRESSED to Color.BLACK,
 *     AttrState.NORMAL to Color.TRANSPARENT
 * )
 * ```
 * @param statesAndColors the state and color [Map].
 * @return [ColorStateList]
 */
@JvmName("createColorStateList")
fun ColorStateList(vararg statesAndColors: Pair<Int, Int>): ColorStateList {
    val states = mutableListOf<IntArray>()
    val colors = mutableListOf<Int>()
    statesAndColors.toMap().forEach { (state, color) ->
        if (state == AttrState.NORMAL) states.add(intArrayOf()) else states.add(intArrayOf(state))
        colors.add(color)
    }; return ColorStateList(states.toTypedArray(), colors.toIntArray())
}
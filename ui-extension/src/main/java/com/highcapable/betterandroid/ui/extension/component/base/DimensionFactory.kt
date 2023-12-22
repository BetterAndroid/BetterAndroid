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
@file:Suppress("unused", "UNCHECKED_CAST")
@file:JvmName("DimensionUtils")

package com.highcapable.betterandroid.ui.extension.component.base

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment

/**
 * Display density interface.
 *
 * Wherever you need to use px and dp, just implement it as an interface on the current class.
 *
 * Supports: [Context], [Window], [View], [Resources], [Fragment], [Dialog].
 *
 * Usage:
 *
 * ```kotlin
 * class YourActivity : AppCompatActivity(), DisplayDensity {
 *
 *    override fun onCreate(savedInstanceState: Bundle?) {
 *        super.onCreate(savedInstanceState)
 *        setContentView(R.layout.activity_main)
 *        // Convert dp to px.
 *        val tenDp = 10.dp
 *        // Or you can use it like this.
 *        val tenDp = dp.toPx()
 *        // Convert px to dp.
 *        val thirtySixPx = 36.px
 *        // Or you can use it like this.
 *        val thirtySixPx = px.toDp()
 *    }
 * }
 * ```
 */
interface DisplayDensity {

    /**
     * Convert dp to px.
     * @see toPx
     * @receiver the original dimension.
     * @return [N]
     */
    val <N : Number> N.dp: N get() = toPx()

    /**
     * Convert px to dp.
     * @see toDp
     * @receiver the original dimension.
     * @return [N]
     */
    val <N : Number> N.px: N get() = toDp()

    /**
     * Convert dp to px.
     *
     * Looks like: 10dp → 36px (Please refer to the target device).
     * @see dp
     * @receiver the original dimension.
     * @return [N]
     */
    fun <N : Number> N.toPx() = resolvePx(dimension = this)

    /**
     * Convert px to dp.
     *
     * Looks like: 36px → 10dp (Please refer to the target device).
     * @see px
     * @receiver the original dimension.
     * @return [N]
     */
    fun <N : Number> N.toDp() = resolveDp(dimension = this)
}

/**
 * Resolve the px dimension from [DisplayDensity].
 * @receiver [DisplayDensity]
 * @param dimension the original dimension.
 * @return [N]
 * @throws IllegalStateException if the current [DisplayDensity] is not supported.
 */
private fun <N : Number> DisplayDensity.resolvePx(dimension: N) = when (this) {
    is Context -> dimension.toPx(context = this)
    is Window -> dimension.toPx(context)
    is View -> dimension.toPx(context)
    is Resources -> dimension.toPx(resources = this)
    is Fragment -> dimension.toPx(resources)
    is Dialog -> dimension.toPx(context)
    else -> error("DisplayDensity only supports Context, Window, View, Resources, Fragment and Dialog.")
}

/**
 * Resolve the dp dimension from [DisplayDensity].
 * @receiver [DisplayDensity]
 * @param dimension the original dimension.
 * @return [N]
 * @throws IllegalStateException if the current [DisplayDensity] is not supported.
 */
private fun <N : Number> DisplayDensity.resolveDp(dimension: N) = when (this) {
    is Context -> dimension.toDp(context = this)
    is Window -> dimension.toDp(context)
    is View -> dimension.toDp(context)
    is Resources -> dimension.toDp(resources = this)
    is Fragment -> dimension.toDp(resources)
    is Dialog -> dimension.toDp(context)
    else -> error("DisplayDensity only supports Context, Window, View, Resources, Fragment and Dialog.")
}

/**
 * Convert dp to px.
 *
 * Looks like: 10dp → 36px (Please refer to the target device).
 * @receiver the original dimension.
 * @param context the current context.
 * @return [N]
 */
@JvmName("numberToPx")
fun <N : Number> N.toPx(context: Context) = toPx(context.resources)

/**
 * Convert dp to px.
 *
 * Looks like: 10dp → 36px (Please refer to the target device).
 * @receiver the original dimension.
 * @param resources the current resources.
 * @return [N]
 */
@JvmName("numberToPx")
fun <N : Number> N.toPx(resources: Resources) = convertToPx(resources)

/**
 * Convert px to dp.
 *
 * Looks like: 36px → 10dp (Please refer to the target device).
 * @receiver the original dimension.
 * @param context the current context.
 * @return [N]
 */
@JvmName("numberToDp")
fun <N : Number> N.toDp(context: Context) = toDp(context.resources)

/**
 * Convert px to dp.
 *
 * Looks like: 36px → 10dp (Please refer to the target device).
 * @receiver the original dimension.
 * @param resources the current resources.
 * @return [N]
 */
@JvmName("numberToDp")
fun <N : Number> N.toDp(resources: Resources) = convertToDp(resources)

/**
 * Convert dp to px.
 *
 * - This function is deprecated, use [toPx] instead.
 */
@Deprecated(message = "Use toPx instead.", ReplaceWith("toPx(context)"))
@JvmName("numberAsDp")
fun <N : Number> N.asDp(context: Context) = toPx(context)

/**
 * Convert dp to px.
 *
 * - This function is deprecated, use [toPx] instead.
 */
@Deprecated(message = "Use toPx instead.", ReplaceWith("toPx(view.resources)"))
@JvmName("numberAsDp")
fun <N : Number> N.asDp(view: View) = toPx(view.resources)

/**
 * Convert px to dp.
 *
 * - This function is deprecated, use [toDp] instead.
 */
@Deprecated(message = "Use toDp instead.", ReplaceWith("toDp(context)"))
@JvmName("numberAsPx")
fun <N : Number> N.asPx(context: Context) = toDp(context)

/**
 * Convert px to dp.
 *
 * - This function is deprecated, use [toDp] instead.
 */
@Deprecated(message = "Use toDp instead.", ReplaceWith("toDp(view.resources)"))
@JvmName("numberAsPx")
fun <N : Number> N.asPx(view: View) = toDp(view.resources)

/**
 * Convert dp to px (base function).
 * @receiver the original dimension.
 * @param resources the current resources.
 * @return [N]
 */
private fun <N : Number> N.convertToPx(resources: Resources): N {
    /**
     * Convert to corresponding type.
     * @receiver the original value.
     * @return [Float]
     * @throws IllegalStateException if the number type is not supported.
     */
    fun Float.convert() = if (this >= 0f) this * resources.displayMetrics.density else this
    return when (this) {
        is Int -> toFloat().convert().toInt() as N
        is Long -> toFloat().convert().toLong() as N
        is Float -> toFloat().convert() as N
        is Double -> toFloat().convert().toDouble() as N
        else -> error("Number type only supports Int, Long, Float and Double.")
    }
}

/**
 * Convert px to dp (base function).
 * @receiver the original dimension.
 * @param resources the current resources.
 * @return [N]
 */
private fun <N : Number> N.convertToDp(resources: Resources): N {
    /**
     * Convert to corresponding type.
     * @receiver the original value.
     * @return [Float]
     * @throws IllegalStateException if the number type is not supported.
     */
    fun Float.convert() = if (this >= 0f) this / resources.displayMetrics.density + 0.5f else this
    return when (this) {
        is Int -> toFloat().convert().toInt() as N
        is Long -> toFloat().convert().toLong() as N
        is Float -> toFloat().convert() as N
        is Double -> toFloat().convert().toDouble() as N
        else -> error("Number type only supports Int, Long, Float and Double.")
    }
}
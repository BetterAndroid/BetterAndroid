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

import android.content.Context
import android.content.res.Resources
import android.view.View

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
        else -> error("Number type only support Int, Long, Float and Double.")
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
        else -> error("Number type only support Int, Long, Float and Double.")
    }
}
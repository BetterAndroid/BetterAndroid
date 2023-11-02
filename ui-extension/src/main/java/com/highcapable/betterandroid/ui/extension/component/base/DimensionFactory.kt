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
import android.view.View

/**
 * Convert dp to px.
 *
 * Looks like: 10dp → 36px (Please refer to the target device).
 * @receiver the original dimension.
 * @param context the current context.
 * @return [N]
 */
@JvmName("numberAsDp")
fun <N : Number> Number.asDp(context: Context) = convertToDpDimension<N>(context)

/**
 * Convert dp to px.
 *
 * Looks like: 10dp → 36px (Please refer to the target device).
 * @receiver the original dimension.
 * @param view the current context.
 * @return [N]
 */
@JvmName("numberAsDp")
fun <N : Number> Number.asDp(view: View) = asDp<N>(view.context)

/**
 * Convert px to dp.
 *
 * Looks like: 36px → 10dp (Please refer to the target device).
 * @receiver the original dimension.
 * @param context the current context.
 * @return [N]
 */
@JvmName("numberAsPx")
fun <N : Number> Number.asPx(context: Context) = convertToPxDimension<N>(context)

/**
 * Convert px to dp.
 *
 * Looks like: 36px → 10dp (Please refer to the target device).
 * @receiver the original dimension.
 * @param view the current context.
 * @return [N]
 */
@JvmName("numberAsPx")
fun <N : Number> Number.asPx(view: View) = asPx<N>(view.context)

/**
 * Convert dp to px (base function).
 * @receiver the original dimension.
 * @param context the current context.
 * @return [N]
 */
private fun <N : Number> Number.convertToDpDimension(context: Context): N {
    /**
     * Convert to corresponding type.
     * @receiver the original value.
     * @return [Float]
     * @throws IllegalStateException if the number type is not supported.
     */
    fun Float.convert() = if (this >= 0f) this * context.resources.displayMetrics.density else this
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
 * @param context the current context.
 * @return [N]
 */
private fun <N : Number> Number.convertToPxDimension(context: Context): N {
    /**
     * Convert to corresponding type.
     * @receiver the original value.
     * @return [Float]
     * @throws IllegalStateException if the number type is not supported.
     */
    fun Float.convert() = if (this >= 0f) this / context.resources.displayMetrics.density + 0.5f else this
    return when (this) {
        is Int -> toFloat().convert().toInt() as N
        is Long -> toFloat().convert().toLong() as N
        is Float -> toFloat().convert() as N
        is Double -> toFloat().convert().toDouble() as N
        else -> error("Number type only support Int, Long, Float and Double.")
    }
}
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
 * This file is created by fankes on 2023/10/22.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.system.extension.tool

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.highcapable.betterandroid.system.extension.tool.SystemVersion.isHighOrEqualsTo

/**
 * Android system sdk version tool.
 */
object SystemVersion {

    /** Android 4.4.3、4.4.4 (19) */
    const val K = Build.VERSION_CODES.KITKAT

    /** Android 4.4W (20) */
    const val K_W = Build.VERSION_CODES.KITKAT_WATCH

    /** Android 5.0、5.0.2 (21) */
    const val L = Build.VERSION_CODES.LOLLIPOP

    /** Android 5.1、5.1.1 (22) */
    const val L_MR1 = Build.VERSION_CODES.LOLLIPOP_MR1

    /** Android 6.0、6.0.1 (23) */
    const val M = Build.VERSION_CODES.M

    /** Android 7.0 (24) */
    const val N = Build.VERSION_CODES.N

    /** Android 7.1、7.1.1、7.1.2 (25) */
    const val N_MR1 = Build.VERSION_CODES.N_MR1

    /** Android 8.0 (26) */
    const val O = Build.VERSION_CODES.O

    /** Android 8.1 (27) */
    const val O_MR1 = Build.VERSION_CODES.O_MR1

    /** Android 9 (28) */
    const val P = Build.VERSION_CODES.P

    /** Android 10 (29) */
    const val Q = Build.VERSION_CODES.Q

    /** Android 11 (30) */
    const val R = Build.VERSION_CODES.R

    /** Android 12 (31) */
    const val S = Build.VERSION_CODES.S

    /** Android 12L、12.1 (32) */
    const val S_V2 = Build.VERSION_CODES.S_V2

    /** Android 13 (33) */
    const val T = Build.VERSION_CODES.TIRAMISU

    /** Android 14 (34) */
    const val U = Build.VERSION_CODES.UPSIDE_DOWN_CAKE

    /**
     * Between the target sdk.
     *
     * Usage:
     *
     * ```kotlin
     * val isBetweenQAndS = SystemVersion.isIn(SystemVersion.Q..SystemVersion.S)
     * ```
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isIn(target: IntRange) = Build.VERSION.SDK_INT in target

    /**
     * Lower than target sdk.
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowTo(target: Int) = Build.VERSION.SDK_INT < target

    /**
     * Lower than or equal to target sdk.
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowOrEqualsTo(target: Int) = Build.VERSION.SDK_INT <= target

    /**
     * Higher than target sdk.
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighTo(target: Int) = Build.VERSION.SDK_INT > target

    /**
     * Higher than or equal to target sdk.
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighOrEqualsTo(target: Int) = Build.VERSION.SDK_INT >= target

    /**
     * Lower than or equal to target sdk.
     *
     * - This function is deprecated, use [isLowOrEqualsTo] instead.
     */
    @Deprecated(message = "Use isLowOrEqualsTo instead.", ReplaceWith("isLowOrEqualsTo(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowAndEqualsTo(target: Int) = isLowOrEqualsTo(target)

    /**
     * Higher than or equal to target sdk.
     *
     * - This function is deprecated, use [isHighOrEqualsTo] instead.
     */
    @Deprecated(message = "Use isHighOrEqualsTo instead.", ReplaceWith("isHighOrEqualsTo(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighAndEqualsTo(target: Int) = isHighOrEqualsTo(target)

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     * @param target the target sdk.
     * @param callback callback when the above conditions are met.
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun require(target: Int, callback: () -> Unit) {
        if (isHighOrEqualsTo(target)) callback()
    }

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     *
     * - Note: This function required [defaultValue] non-null, otherwise please use [requireOrNull] instead it.
     * @see requireOrNull
     * @param target the target sdk.
     * @param defaultValue the default value when the above conditions are not met.
     * @param callback callback when the above conditions are met.
     * @return [T]
     * @throws IllegalStateException if [defaultValue] is null.
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T> require(target: Int, defaultValue: T, callback: () -> T): T =
        if (defaultValue != null)
            if (isHighOrEqualsTo(target)) (callback() ?: defaultValue) else defaultValue
        else error("The defaultValue is null and not allowed, please use requireOrNull function instead it.")

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     * @param target the target sdk.
     * @param defaultValue the default value when the above conditions are not met.
     * @param callback callback when the above conditions are met.
     * @return [T] or null.
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T> requireOrNull(target: Int, defaultValue: T?, callback: () -> T?): T? =
        if (isHighOrEqualsTo(target)) (callback() ?: defaultValue) else defaultValue
}
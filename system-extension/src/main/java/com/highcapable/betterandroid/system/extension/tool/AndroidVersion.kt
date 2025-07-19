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
 * This file is created by fankes on 2023/10/22.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.system.extension.tool

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.highcapable.betterandroid.system.extension.tool.AndroidVersion.isHighOrEqualsTo

/**
 * Android SDK version tool.
 */
object AndroidVersion {

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

    /** Android 15 (35) */
    const val V = Build.VERSION_CODES.VANILLA_ICE_CREAM

    /** Android 16 (36) */
    const val BAKLAVA = Build.VERSION_CODES.BAKLAVA

    /**
     * Get the current Android version name.
     *
     * For example Android 12, it will return a string "12".
     * @see Build.VERSION.RELEASE
     * @return [String]
     */
    @JvmStatic
    val name by lazy { Build.VERSION.RELEASE ?: "" }

    /**
     * Get the current Android version code.
     *
     * For example Android 12, it will return an integer 31.
     * @see Build.VERSION.SDK_INT
     * @return [Int]
     */
    @JvmStatic
    val code by lazy { Build.VERSION.SDK_INT }

    /**
     * Less than target sdk. `code < target`
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLessThan(target: Int) = code < target

    /**
     * At most target sdk. `code <= target`
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isAtMost(target: Int) = code <= target

    /**
     * Greater than target sdk. `code > target`
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    infix fun isGreaterThan(target: Int) = code > target

    /**
     * At least target sdk. `code >= target`
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isAtLeast(target: Int) = code >= target

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     * @param target the target sdk.
     * @param callback callback when the above conditions are met.
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun require(target: Int, callback: () -> Unit) {
        if (isAtLeast(target)) callback()
    }

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     * @see requireOrNull
     * @param target the target sdk.
     * @param defaultValue the default value when the above conditions are not met.
     * @param callback callback when the above conditions are met.
     * @return [T]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T : Any> require(target: Int, defaultValue: T, callback: () -> T): T =
        if (isAtLeast(target)) callback() else defaultValue

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     * @param target the target sdk.
     * @param defaultValue the default value when the above conditions are not met.
     * @param callback callback when the above conditions are met.
     * @return [T] or null.
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T : Any?> requireOrNull(target: Int, defaultValue: T?, callback: () -> T?): T? =
        if (isAtLeast(target)) (callback() ?: defaultValue) else defaultValue

    // The following functions are deprecated, you should not use them.

    /**
     * Between the target sdk.
     *
     * - This function is deprecated and no effect and will be removed in the future.
     *
     * - The reason is that [ChecksSdkIntAtLeast] cannot handle this complex situation, causing the lint check to make the correct judgment.
     */
    @Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Please implement it yourself.")
    @JvmStatic
    fun isBetween(target: IntRange) = false

    /**
     * Between the target sdk.
     *
     * - This function is deprecated, use [isBetween] instead.
     */
    @Suppress("DEPRECATION")
    @Deprecated(message = "Use isBetween instead.", ReplaceWith("AndroidVersion.isBetween(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isIn(target: IntRange) = isBetween(target)

    /**
     * Lower than target sdk.
     *
     * - This function is deprecated, use [isLessThan] instead.
     */
    @Deprecated(message = "Use isLessThan instead.", ReplaceWith("AndroidVersion.isLessThan(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowTo(target: Int) = isLessThan(target)

    /**
     * Lower than or equal to target sdk.
     *
     * - This function is deprecated, use [isAtMost] instead.
     */
    @Deprecated(message = "Use isAtMost instead.", ReplaceWith("AndroidVersion.isAtMost(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowOrEqualsTo(target: Int) = isAtMost(target)

    /**
     * Higher than target sdk.
     *
     * - This function is deprecated, use [isGreaterThan] instead.
     * @return [Boolean]
     */
    @Deprecated(message = "Use isGreaterThan instead.", ReplaceWith("AndroidVersion.isGreaterThan(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    infix fun isHighTo(target: Int) = isGreaterThan(target)

    /**
     * Higher than or equal to target sdk.
     *
     * - This function is deprecated, use [isAtLeast] instead.
     */
    @Deprecated(message = "Use isAtLeast instead.", ReplaceWith("AndroidVersion.isAtLeast(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighOrEqualsTo(target: Int) = isAtLeast(target)

    /**
     * Lower than or equal to target sdk.
     *
     * - This function is deprecated, use [isLowOrEqualsTo] instead.
     */
    @Suppress("DEPRECATION")
    @Deprecated(message = "Use isLowOrEqualsTo instead.", ReplaceWith("AndroidVersion.isLowOrEqualsTo(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowAndEqualsTo(target: Int) = isLowOrEqualsTo(target)

    /**
     * Higher than or equal to target sdk.
     *
     * - This function is deprecated, use [isHighOrEqualsTo] instead.
     */
    @Suppress("DEPRECATION")
    @Deprecated(message = "Use isHighOrEqualsTo instead.", ReplaceWith("AndroidVersion.isHighOrEqualsTo(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighAndEqualsTo(target: Int) = isHighOrEqualsTo(target)
}
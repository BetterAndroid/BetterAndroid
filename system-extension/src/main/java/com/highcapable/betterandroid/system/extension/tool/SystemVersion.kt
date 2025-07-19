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

import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Use [AndroidVersion] instead.
 */
@Deprecated(message = "Use AndroidVersion instead.", ReplaceWith("AndroidVersion"))
object SystemVersion {

    /**
     * - This property is deprecated, use [AndroidVersion.K] instead.
     */
    @Deprecated(message = "Use AndroidVersion.K instead.", ReplaceWith("AndroidVersion.K"))
    const val K = AndroidVersion.K

    /**
     * - This property is deprecated, use [AndroidVersion.K_W] instead.
     */
    @Deprecated(message = "Use AndroidVersion.K_W instead.", ReplaceWith("AndroidVersion.K_W"))
    const val K_W = AndroidVersion.K_W

    /**
     * - This property is deprecated, use [AndroidVersion.L] instead.
     */
    @Deprecated(message = "Use AndroidVersion.L instead.", ReplaceWith("AndroidVersion.L"))
    const val L = AndroidVersion.L

    /**
     * - This property is deprecated, use [AndroidVersion.L_MR1] instead.
     */
    @Deprecated(message = "Use AndroidVersion.L_MR1 instead.", ReplaceWith("AndroidVersion.L_MR1"))
    const val L_MR1 = AndroidVersion.L_MR1

    /**
     * - This property is deprecated, use [AndroidVersion.M] instead.
     */
    @Deprecated(message = "Use AndroidVersion.M instead.", ReplaceWith("AndroidVersion.M"))
    const val M = AndroidVersion.M

    /**
     * - This property is deprecated, use [AndroidVersion.N] instead.
     */
    @Deprecated(message = "Use AndroidVersion.N instead.", ReplaceWith("AndroidVersion.N"))
    const val N = AndroidVersion.N

    /**
     * - This property is deprecated, use [AndroidVersion.N_MR1] instead.
     */
    @Deprecated(message = "Use AndroidVersion.N_MR1 instead.", ReplaceWith("AndroidVersion.N_MR1"))
    const val N_MR1 = AndroidVersion.N_MR1

    /**
     * - This property is deprecated, use [AndroidVersion.O] instead.
     */
    @Deprecated(message = "Use AndroidVersion.O instead.", ReplaceWith("AndroidVersion.O"))
    const val O = AndroidVersion.O

    /**
     * - This property is deprecated, use [AndroidVersion.O_MR1] instead.
     */
    @Deprecated(message = "Use AndroidVersion.O_MR1 instead.", ReplaceWith("AndroidVersion.O_MR1"))
    const val O_MR1 = AndroidVersion.O_MR1

    /**
     * - This property is deprecated, use [AndroidVersion.P] instead.
     */
    @Deprecated(message = "Use AndroidVersion.P instead.", ReplaceWith("AndroidVersion.P"))
    const val P = AndroidVersion.P

    /**
     * - This property is deprecated, use [AndroidVersion.Q] instead.
     */
    @Deprecated(message = "Use AndroidVersion.Q instead.", ReplaceWith("AndroidVersion.Q"))
    const val Q = AndroidVersion.Q

    /**
     * - This property is deprecated, use [AndroidVersion.R] instead.
     */
    @Deprecated(message = "Use AndroidVersion.R instead.", ReplaceWith("AndroidVersion.R"))
    const val R = AndroidVersion.R

    /**
     * - This property is deprecated, use [AndroidVersion.S] instead.
     */
    @Deprecated(message = "Use AndroidVersion.S instead.", ReplaceWith("AndroidVersion.S"))
    const val S = AndroidVersion.S

    /**
     * - This property is deprecated, use [AndroidVersion.S_V2] instead.
     */
    @Deprecated(message = "Use AndroidVersion.S_V2 instead.", ReplaceWith("AndroidVersion.S_V2"))
    const val S_V2 = AndroidVersion.S_V2

    /**
     * - This property is deprecated, use [AndroidVersion.T] instead.
     */
    @Deprecated(message = "Use AndroidVersion.T instead.", ReplaceWith("AndroidVersion.T"))
    const val T = AndroidVersion.T

    /**
     * - This property is deprecated, use [AndroidVersion.U] instead.
     */
    @Deprecated(message = "Use AndroidVersion.U instead.", ReplaceWith("AndroidVersion.U"))
    const val U = AndroidVersion.U

    /**
     * - This property is deprecated, use [AndroidVersion.name] instead.
     */
    @Deprecated(message = "Use AndroidVersion.name instead.", ReplaceWith("AndroidVersion.name"))
    @JvmStatic
    val name get() = AndroidVersion.name

    /**
     * - This function is deprecated and no effect and will be removed in the future.
     *
     * - The reason is that [ChecksSdkIntAtLeast] cannot handle this complex situation, causing the lint check to make the correct judgment.
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Please implement it yourself.")
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isBetween(target: IntRange) = false

    /**
     * - This function is deprecated and no effect and will be removed in the future.
     *
     * - The reason is that [ChecksSdkIntAtLeast] cannot handle this complex situation, causing the lint check to make the correct judgment.
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Please implement it yourself.")
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isIn(target: IntRange) = false

    /**
     * - This function is deprecated, use [AndroidVersion.isAtMost] instead.
     */
    @Deprecated(message = "Use AndroidVersion.isAtMost instead.", ReplaceWith("AndroidVersion.isAtMost(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowTo(target: Int) = AndroidVersion.isAtMost(target)

    /**
     * - This function is deprecated, use [AndroidVersion.isAtMost] instead.
     */
    @Deprecated(message = "Use AndroidVersion.isAtMost instead.", ReplaceWith("AndroidVersion.isAtMost(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowOrEqualsTo(target: Int) = AndroidVersion.isAtMost(target)

    /**
     * - This function is deprecated, use [AndroidVersion.isGreaterThan] instead.
     */
    @Deprecated(message = "Use AndroidVersion.isGreaterThan instead.", ReplaceWith("AndroidVersion.isGreaterThan(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighTo(target: Int) = AndroidVersion.isGreaterThan(target)

    /**
     * - This function is deprecated, use [AndroidVersion.isAtLeast] instead.
     */
    @Deprecated(message = "Use AndroidVersion.isAtLeast instead.", ReplaceWith("AndroidVersion.isAtLeast(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighOrEqualsTo(target: Int) = AndroidVersion.isAtLeast(target)

    /**
     * - This function is deprecated, use [AndroidVersion.isAtMost] instead.
     */
    @Deprecated(message = "Use AndroidVersion.isAtMost instead.", ReplaceWith("AndroidVersion.isAtMost(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isLowAndEqualsTo(target: Int) = AndroidVersion.isAtMost(target)

    /**
     * - This function is deprecated, use [AndroidVersion.isAtLeast] instead.
     */
    @Deprecated(message = "Use AndroidVersion.isAtLeast instead.", ReplaceWith("AndroidVersion.isAtLeast(target)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isHighAndEqualsTo(target: Int) = AndroidVersion.isAtLeast(target)

    /**
     * - This function is deprecated, use [AndroidVersion.require] instead.
     */
    @Deprecated(message = "Use AndroidVersion.require instead.", ReplaceWith("AndroidVersion.require(target, callback)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun require(target: Int, callback: () -> Unit) =
        AndroidVersion.require(target, callback)

    /**
     * - This function is deprecated, use [AndroidVersion.require] instead.
     */
    @Deprecated(message = "Use AndroidVersion.require instead.", ReplaceWith("AndroidVersion.require(target, defaultValue, callback)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T : Any> require(target: Int, defaultValue: T, callback: () -> T): T =
        AndroidVersion.require(target, defaultValue, callback)

    /**
     * - This function is deprecated, use [AndroidVersion.requireOrNull] instead.
     */
    @Deprecated(message = "Use AndroidVersion.requireOrNull instead.", ReplaceWith("AndroidVersion.requireOrNull(target, defaultValue, callback)"))
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T : Any?> requireOrNull(target: Int, defaultValue: T?, callback: () -> T?): T? =
        AndroidVersion.requireOrNull(target, defaultValue, callback)
}
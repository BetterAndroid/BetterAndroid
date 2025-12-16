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
 * This file is created by fankes on 2025/6/26.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.system.extension.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.IntDef
import com.highcapable.betterandroid.system.extension.utils.AndroidVersion.requireOrNull

/**
 * Android SDK version tool.
 */
object AndroidVersion {

    /**
     * The Android SDK version code annotation.
     */
    @IntDef(
        B, B_1_1, C, D, E, E_0_1, E_MR1, FROYO, G, G_MR1,
        H, H_MR1, H_MR2, I, I_MR1, J, J_MR1, J_MR2, K,
        K_W, L, L_MR1, M, N, N_MR1, O, O_MR1, P,
        Q, R, S, S_V2, T, U, V, BAKLAVA
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class SdkInt

    /** Android 1.0 (1) */
    const val B = Build.VERSION_CODES.BASE

    /** Android 1.1 (2) */
    const val B_1_1 = Build.VERSION_CODES.BASE_1_1

    /** Android 1.5 (3) */
    const val C = Build.VERSION_CODES.CUPCAKE

    /** Android 1.6 (4) */
    const val D = Build.VERSION_CODES.DONUT

    /** Android 2.0 (5) */
    const val E = Build.VERSION_CODES.ECLAIR

    /** Android 2.0.1 (6) */
    const val E_0_1 = Build.VERSION_CODES.ECLAIR_0_1

    /** Android 2.1 (7) */
    const val E_MR1 = Build.VERSION_CODES.ECLAIR_MR1

    /** Android 2.2 (8) */
    const val FROYO = Build.VERSION_CODES.FROYO

    /** Android 2.3 (9) */
    const val G = Build.VERSION_CODES.GINGERBREAD

    /** Android 2.3.3 (10) */
    const val G_MR1 = Build.VERSION_CODES.GINGERBREAD_MR1

    /** Android 3.0 (11) */
    const val H = Build.VERSION_CODES.HONEYCOMB

    /** Android 3.1 (12) */
    const val H_MR1 = Build.VERSION_CODES.HONEYCOMB_MR1

    /** Android 3.2 (13) */
    const val H_MR2 = Build.VERSION_CODES.HONEYCOMB_MR2

    /** Android 4.0 (14) */
    const val I = Build.VERSION_CODES.ICE_CREAM_SANDWICH

    /** Android 4.03 (15) */
    const val I_MR1 = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1

    /** Android 4.1 (16) */
    const val J = Build.VERSION_CODES.JELLY_BEAN

    /** Android 4.2 (17) */
    const val J_MR1 = Build.VERSION_CODES.JELLY_BEAN_MR1

    /** Android 4.3 (18) */
    const val J_MR2 = Build.VERSION_CODES.JELLY_BEAN_MR2

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
    fun isLessThan(@SdkInt target: Int) = code < target

    /**
     * At most target sdk. `code <= target`
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isAtMost(@SdkInt target: Int) = code <= target

    /**
     * Greater than target sdk. `code > target`
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isGreaterThan(@SdkInt target: Int) = code > target

    /**
     * At least target sdk. `code >= target`
     * @param target the target sdk.
     * @return [Boolean]
     */
    @JvmStatic
    @ChecksSdkIntAtLeast(parameter = 0)
    fun isAtLeast(@SdkInt target: Int) = code >= target

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     * @param target the target sdk.
     * @param callback callback when the above conditions are met.
     */
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun require(@SdkInt target: Int, callback: () -> Unit) {
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
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T : Any> require(@SdkInt target: Int, defaultValue: T, callback: () -> T): T =
        if (isAtLeast(target)) callback() else defaultValue

    /**
     * The target sdk is required to be [target] or higher than [target] to call [callback].
     * @param target the target sdk.
     * @param defaultValue the default value when the above conditions are not met.
     * @param callback callback when the above conditions are met.
     * @return [T] or null.
     */
    @ChecksSdkIntAtLeast(parameter = 0)
    inline fun <T : Any?> requireOrNull(@SdkInt target: Int, defaultValue: T?, callback: () -> T?): T? =
        if (isAtLeast(target)) (callback() ?: defaultValue) else defaultValue
}
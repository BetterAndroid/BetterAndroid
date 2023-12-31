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
 * This file is created by fankes on 2022/11/6.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.extension.component.feature

import android.content.Context
import android.graphics.Color
import com.google.android.material.color.DynamicColors
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.extension.component.base.getColorCompat
import android.R as Android_R
import com.google.android.material.R as Material_R

/**
 * System colors tool.
 *
 * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
 * @param context the current context.
 */
class SystemColors private constructor(private val context: Context) {

    companion object {

        /**
         * Determine whether the dynamic colors is available on this system.
         *
         * - Note: The dynamic colors may not be applicable to all systems with Android version ≥ 12.
         * @return [Boolean]
         */
        @JvmStatic
        val isAvailable by lazy { DynamicColors.isDynamicColorAvailable() }

        /**
         * Create a new [SystemColors] from [context].
         * @param context the current context.
         * @return [SystemColors]
         */
        @JvmStatic
        fun from(context: Context) = SystemColors(context)
    }

    /**
     * Get the "system-accent-1"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getSystemAccentPrimary")
    fun systemAccentPrimary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Android_R.color.system_accent1_0
            10 -> Android_R.color.system_accent1_10
            50 -> Android_R.color.system_accent1_50
            100 -> Android_R.color.system_accent1_100
            200 -> Android_R.color.system_accent1_200
            300 -> Android_R.color.system_accent1_300
            400 -> Android_R.color.system_accent1_400
            500 -> Android_R.color.system_accent1_500
            600 -> Android_R.color.system_accent1_600
            700 -> Android_R.color.system_accent1_700
            800 -> Android_R.color.system_accent1_800
            900 -> Android_R.color.system_accent1_900
            1000 -> Android_R.color.system_accent1_1000
            else -> error("Invalid systemAccentPrimary color value $value.")
        })
    }

    /**
     * Get the "system-accent-2"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getSystemAccentSecondary")
    fun systemAccentSecondary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Android_R.color.system_accent2_0
            10 -> Android_R.color.system_accent2_10
            50 -> Android_R.color.system_accent2_50
            100 -> Android_R.color.system_accent2_100
            200 -> Android_R.color.system_accent2_200
            300 -> Android_R.color.system_accent2_300
            400 -> Android_R.color.system_accent2_400
            500 -> Android_R.color.system_accent2_500
            600 -> Android_R.color.system_accent2_600
            700 -> Android_R.color.system_accent2_700
            800 -> Android_R.color.system_accent2_800
            900 -> Android_R.color.system_accent2_900
            1000 -> Android_R.color.system_accent2_1000
            else -> error("Invalid systemAccentSecondary color value $value.")
        })
    }

    /**
     * Get the "system-accent-3"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getSystemAccentTertiary")
    fun systemAccentTertiary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Android_R.color.system_accent3_0
            10 -> Android_R.color.system_accent3_10
            50 -> Android_R.color.system_accent3_50
            100 -> Android_R.color.system_accent3_100
            200 -> Android_R.color.system_accent3_200
            300 -> Android_R.color.system_accent3_300
            400 -> Android_R.color.system_accent3_400
            500 -> Android_R.color.system_accent3_500
            600 -> Android_R.color.system_accent3_600
            700 -> Android_R.color.system_accent3_700
            800 -> Android_R.color.system_accent3_800
            900 -> Android_R.color.system_accent3_900
            1000 -> Android_R.color.system_accent3_1000
            else -> error("Invalid systemAccentTertiary color value $value.")
        })
    }

    /**
     * Get the "system-neutral-1"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getSystemNeutralPrimary")
    fun systemNeutralPrimary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Android_R.color.system_neutral1_0
            10 -> Android_R.color.system_neutral1_10
            50 -> Android_R.color.system_neutral1_50
            100 -> Android_R.color.system_neutral1_100
            200 -> Android_R.color.system_neutral1_200
            300 -> Android_R.color.system_neutral1_300
            400 -> Android_R.color.system_neutral1_400
            500 -> Android_R.color.system_neutral1_500
            600 -> Android_R.color.system_neutral1_600
            700 -> Android_R.color.system_neutral1_700
            800 -> Android_R.color.system_neutral1_800
            900 -> Android_R.color.system_neutral1_900
            1000 -> Android_R.color.system_neutral1_1000
            else -> error("Invalid systemNeutralPrimary color value $value.")
        })
    }

    /**
     * Get the "system-neutral-2"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getSystemNeutralSecondary")
    fun systemNeutralSecondary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Android_R.color.system_neutral2_0
            10 -> Android_R.color.system_neutral2_10
            50 -> Android_R.color.system_neutral2_50
            100 -> Android_R.color.system_neutral2_100
            200 -> Android_R.color.system_neutral2_200
            300 -> Android_R.color.system_neutral2_300
            400 -> Android_R.color.system_neutral2_400
            500 -> Android_R.color.system_neutral2_500
            600 -> Android_R.color.system_neutral2_600
            700 -> Android_R.color.system_neutral2_700
            800 -> Android_R.color.system_neutral2_800
            900 -> Android_R.color.system_neutral2_900
            1000 -> Android_R.color.system_neutral2_1000
            else -> error("Invalid systemNeutralSecondary color value $value.")
        })
    }

    /**
     * Get the "material_dynamic_primary"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getMaterialDynamicPrimary")
    fun materialDynamicPrimary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Material_R.color.material_dynamic_primary0
            10 -> Material_R.color.material_dynamic_primary10
            20 -> Material_R.color.material_dynamic_primary20
            30 -> Material_R.color.material_dynamic_primary30
            40 -> Material_R.color.material_dynamic_primary40
            50 -> Material_R.color.material_dynamic_primary50
            60 -> Material_R.color.material_dynamic_primary60
            70 -> Material_R.color.material_dynamic_primary70
            80 -> Material_R.color.material_dynamic_primary80
            90 -> Material_R.color.material_dynamic_primary90
            95 -> Material_R.color.material_dynamic_primary95
            99 -> Material_R.color.material_dynamic_primary99
            100 -> Material_R.color.material_dynamic_primary100
            else -> error("Invalid materialDynamicPrimary color value $value.")
        })
    }

    /**
     * Get the "material_dynamic_secondary"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getMaterialDynamicSecondary")
    fun materialDynamicSecondary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Material_R.color.material_dynamic_secondary0
            10 -> Material_R.color.material_dynamic_secondary10
            20 -> Material_R.color.material_dynamic_secondary20
            30 -> Material_R.color.material_dynamic_secondary30
            40 -> Material_R.color.material_dynamic_secondary40
            50 -> Material_R.color.material_dynamic_secondary50
            60 -> Material_R.color.material_dynamic_secondary60
            70 -> Material_R.color.material_dynamic_secondary70
            80 -> Material_R.color.material_dynamic_secondary80
            90 -> Material_R.color.material_dynamic_secondary90
            95 -> Material_R.color.material_dynamic_secondary95
            99 -> Material_R.color.material_dynamic_secondary99
            100 -> Material_R.color.material_dynamic_secondary100
            else -> error("Invalid materialDynamicSecondary color value $value.")
        })
    }

    /**
     * Get the "material_dynamic_tertiary"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getMaterialDynamicTertiary")
    fun materialDynamicTertiary(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Material_R.color.material_dynamic_tertiary0
            10 -> Material_R.color.material_dynamic_tertiary10
            20 -> Material_R.color.material_dynamic_tertiary20
            30 -> Material_R.color.material_dynamic_tertiary30
            40 -> Material_R.color.material_dynamic_tertiary40
            50 -> Material_R.color.material_dynamic_tertiary50
            60 -> Material_R.color.material_dynamic_tertiary60
            70 -> Material_R.color.material_dynamic_tertiary70
            80 -> Material_R.color.material_dynamic_tertiary80
            90 -> Material_R.color.material_dynamic_tertiary90
            95 -> Material_R.color.material_dynamic_tertiary95
            99 -> Material_R.color.material_dynamic_tertiary99
            100 -> Material_R.color.material_dynamic_tertiary100
            else -> error("Invalid materialDynamicTertiary color value $value.")
        })
    }

    /**
     * Get the "material_dynamic_neutral"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getMaterialDynamicNeutral")
    fun materialDynamicNeutral(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Material_R.color.material_dynamic_neutral0
            10 -> Material_R.color.material_dynamic_neutral10
            20 -> Material_R.color.material_dynamic_neutral20
            30 -> Material_R.color.material_dynamic_neutral30
            40 -> Material_R.color.material_dynamic_neutral40
            50 -> Material_R.color.material_dynamic_neutral50
            60 -> Material_R.color.material_dynamic_neutral60
            70 -> Material_R.color.material_dynamic_neutral70
            80 -> Material_R.color.material_dynamic_neutral80
            90 -> Material_R.color.material_dynamic_neutral90
            95 -> Material_R.color.material_dynamic_neutral95
            99 -> Material_R.color.material_dynamic_neutral99
            100 -> Material_R.color.material_dynamic_neutral100
            else -> error("Invalid materialDynamicNeutral color value $value.")
        })
    }

    /**
     * Get the "material_dynamic_neutral_variant"'s all depth colors.
     *
     * - Require Android 12 (31), if not will returns [Color.TRANSPARENT].
     * @param value the color depth, only can be 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100, default is 0.
     * @return [Int]
     * @throws IllegalStateException if [value] is invalid.
     */
    @JvmOverloads
    @JvmName("getMaterialDynamicNeutralVariant")
    fun materialDynamicNeutralVariant(value: Int = 0) = SystemVersion.require(SystemVersion.S, Color.TRANSPARENT) {
        context.getColorCompat(when (value) {
            0 -> Material_R.color.material_dynamic_neutral_variant0
            10 -> Material_R.color.material_dynamic_neutral_variant10
            20 -> Material_R.color.material_dynamic_neutral_variant20
            30 -> Material_R.color.material_dynamic_neutral_variant30
            40 -> Material_R.color.material_dynamic_neutral_variant40
            50 -> Material_R.color.material_dynamic_neutral_variant50
            60 -> Material_R.color.material_dynamic_neutral_variant60
            70 -> Material_R.color.material_dynamic_neutral_variant70
            80 -> Material_R.color.material_dynamic_neutral_variant80
            90 -> Material_R.color.material_dynamic_neutral_variant90
            95 -> Material_R.color.material_dynamic_neutral_variant95
            99 -> Material_R.color.material_dynamic_neutral_variant99
            100 -> Material_R.color.material_dynamic_neutral_variant100
            else -> error("Invalid materialDynamicNeutralVariant color value $value.")
        })
    }
}
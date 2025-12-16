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

import com.highcapable.betterandroid.system.extension.utils.RomType

/**
 * Use [RomType] instead.
 */
@Deprecated(message = "Use RomType instead.",
    ReplaceWith(
        expression = "RomType",
        imports = arrayOf("com.highcapable.betterandroid.system.extension.utils.RomType")
    )
)
object SystemKind {

    /**
     * - This property is deprecated, use [RomType.DEFAULT] instead.
     */
    @Deprecated(message = "Use RomType.DEFAULT instead.", ReplaceWith("RomType.DEFAULT"))
    const val DEFAULT = RomType.DEFAULT

    /**
     * - This property is deprecated, use [RomType.HARMONYOS] instead.
     */
    @Deprecated(message = "Use RomType.HARMONYOS instead.", ReplaceWith("RomType.HARMONYOS"))
    const val HARMONYOS = RomType.HARMONYOS

    /**
     * - This property is deprecated, use [RomType.EMUI] instead.
     */
    @Deprecated(message = "Use RomType.EMUI instead.", ReplaceWith("RomType.EMUI"))
    const val EMUI = RomType.EMUI

    /**
     * - This property is deprecated, use [RomType.MIUI] instead.
     */
    @Deprecated(message = "Use RomType.MIUI instead.", ReplaceWith("RomType.MIUI"))
    const val MIUI = RomType.MIUI

    /**
     * - This property is deprecated, use [RomType.HYPEROS] instead.
     */
    @Deprecated(message = "Use RomType.HYPEROS instead.", ReplaceWith("RomType.HYPEROS"))
    const val HYPEROS = RomType.HYPEROS

    /**
     * - This property is deprecated, use [RomType.COLOROS] instead.
     */
    @Deprecated(message = "Use RomType.COLOROS instead.", ReplaceWith("RomType.COLOROS"))
    const val COLOROS = RomType.COLOROS

    /**
     * - This property is deprecated, use [RomType.FUNTOUCHOS] instead.
     */
    @Deprecated(message = "Use RomType.FUNTOUCHOS instead.", ReplaceWith("RomType.FUNTOUCHOS"))
    const val FUNTOUCHOS = RomType.FUNTOUCHOS

    /**
     * - This property is deprecated, use [RomType.ORIGINOS] instead.
     */
    @Deprecated(message = "Use RomType.ORIGINOS instead.", ReplaceWith("RomType.ORIGINOS"))
    const val ORIGINOS = RomType.ORIGINOS

    /**
     * - This property is deprecated, use [RomType.FLYME] instead.
     */
    @Deprecated(message = "Use RomType.FLYME instead.", ReplaceWith("RomType.FLYME"))
    const val FLYME = RomType.FLYME

    /**
     * - This property is deprecated, use [RomType.ONEUI] instead.
     */
    @Deprecated(message = "Use RomType.ONEUI instead.", ReplaceWith("RomType.ONEUI"))
    const val ONEUI = RomType.ONEUI

    /**
     * - This property is deprecated, use [RomType.ZUI] instead.
     */
    @Deprecated(message = "Use RomType.ZUI instead.", ReplaceWith("RomType.ZUI"))
    const val ZUI = RomType.ZUI

    /**
     * - This property is deprecated, use [RomType.REDMAGICOS] instead.
     */
    @Deprecated(message = "Use RomType.REDMAGICOS instead.", ReplaceWith("RomType.REDMAGICOS"))
    const val REDMAGICOS = RomType.REDMAGICOS

    /**
     * - This property is deprecated, use [RomType.NUBIAUI] instead.
     */
    @Deprecated(message = "Use RomType.NUBIAUI instead.", ReplaceWith("RomType.NUBIAUI"))
    const val NUBIAUI = RomType.NUBIAUI

    /**
     * - This property is deprecated, use [RomType.ROGUI] instead.
     */
    @Deprecated(message = "Use RomType.ROGUI instead.", ReplaceWith("RomType.ROGUI"))
    const val ROGUI = RomType.ROGUI

    /**
     * - This property is deprecated, use [RomType.VISIONOS] instead.
     */
    @Deprecated(message = "Use RomType.VISIONOS instead.", ReplaceWith("RomType.VISIONOS"))
    const val VISIONOS = RomType.VISIONOS

    /**
     * - This property is deprecated, use [RomType.current] instead.
     */
    @Deprecated(message = "Use RomType.current instead.", ReplaceWith("RomType.current"))
    @JvmStatic
    val current get() = RomType.current

    /**
     * - This function is deprecated, use [RomType.current] instead.
     */
    @Deprecated(message = "Use RomType.current instead.", ReplaceWith("RomType.current"))
    @JvmStatic
    fun get() = RomType.current

    /**
     * - This function is deprecated, use [RomType.matches] instead.
     */
    @Suppress("CovariantEquals")
    @Deprecated(message = "Use RomType.matches instead.", ReplaceWith("RomType.matches(type)"))
    @JvmStatic
    fun equals(type: Int) = RomType.matches(type)
}
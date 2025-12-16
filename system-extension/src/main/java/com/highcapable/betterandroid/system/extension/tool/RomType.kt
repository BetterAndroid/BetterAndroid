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

package com.highcapable.betterandroid.system.extension.tool

import com.highcapable.betterandroid.system.extension.utils.RomType as NewRomType

/**
 * Use [NewRomType] instead.
 */
@Deprecated(
    message = "Use RomType instead.",
    ReplaceWith(
        expression = "RomType",
        imports = arrayOf("com.highcapable.betterandroid.system.extension.utils.RomType")
    )
)
object RomType {

    /**
     * - This property is deprecated, use [NewRomType.DEFAULT] instead.
     */
    @Deprecated(message = "Use RomType.DEFAULT instead.", ReplaceWith("RomType.DEFAULT"))
    const val DEFAULT = NewRomType.DEFAULT

    /**
     * - This property is deprecated, use [NewRomType.HARMONYOS] instead.
     */
    @Deprecated(message = "Use RomType.HARMONYOS instead.", ReplaceWith("RomType.HARMONYOS"))
    const val HARMONYOS = NewRomType.HARMONYOS

    /**
     * - This property is deprecated, use [NewRomType.EMUI] instead.
     */
    @Deprecated(message = "Use RomType.EMUI instead.", ReplaceWith("RomType.EMUI"))
    const val EMUI = NewRomType.EMUI

    /**
     * - This property is deprecated, use [NewRomType.MIUI] instead.
     */
    @Deprecated(message = "Use RomType.MIUI instead.", ReplaceWith("RomType.MIUI"))
    const val MIUI = NewRomType.MIUI

    /**
     * - This property is deprecated, use [NewRomType.HYPEROS] instead.
     */
    @Deprecated(message = "Use RomType.HYPEROS instead.", ReplaceWith("RomType.HYPEROS"))
    const val HYPEROS = NewRomType.HYPEROS

    /**
     * - This property is deprecated, use [NewRomType.COLOROS] instead.
     */
    @Deprecated(message = "Use RomType.COLOROS instead.", ReplaceWith("RomType.COLOROS"))
    const val COLOROS = NewRomType.COLOROS

    /**
     * - This property is deprecated, use [NewRomType.FUNTOUCHOS] instead.
     */
    @Deprecated(message = "Use RomType.FUNTOUCHOS instead.", ReplaceWith("RomType.FUNTOUCHOS"))
    const val FUNTOUCHOS = NewRomType.FUNTOUCHOS

    /**
     * - This property is deprecated, use [NewRomType.ORIGINOS] instead.
     */
    @Deprecated(message = "Use RomType.ORIGINOS instead.", ReplaceWith("RomType.ORIGINOS"))
    const val ORIGINOS = NewRomType.ORIGINOS

    /**
     * - This property is deprecated, use [NewRomType.FLYME] instead.
     */
    @Deprecated(message = "Use RomType.FLYME instead.", ReplaceWith("RomType.FLYME"))
    const val FLYME = NewRomType.FLYME

    /**
     * - This property is deprecated, use [NewRomType.ONEUI] instead.
     */
    @Deprecated(message = "Use RomType.ONEUI instead.", ReplaceWith("RomType.ONEUI"))
    const val ONEUI = NewRomType.ONEUI

    /**
     * - This property is deprecated, use [NewRomType.ZUI] instead.
     */
    @Deprecated(message = "Use RomType.ZUI instead.", ReplaceWith("RomType.ZUI"))
    const val ZUI = NewRomType.ZUI

    /**
     * - This property is deprecated, use [NewRomType.REDMAGICOS] instead.
     */
    @Deprecated(message = "Use RomType.REDMAGICOS instead.", ReplaceWith("RomType.REDMAGICOS"))
    const val REDMAGICOS = NewRomType.REDMAGICOS

    /**
     * - This property is deprecated, use [NewRomType.NUBIAUI] instead.
     */
    @Deprecated(message = "Use RomType.NUBIAUI instead.", ReplaceWith("RomType.NUBIAUI"))
    const val NUBIAUI = NewRomType.NUBIAUI

    /**
     * - This property is deprecated, use [NewRomType.ROGUI] instead.
     */
    @Deprecated(message = "Use RomType.ROGUI instead.", ReplaceWith("RomType.ROGUI"))
    const val ROGUI = NewRomType.ROGUI

    /**
     * - This property is deprecated, use [NewRomType.VISIONOS] instead.
     */
    @Deprecated(message = "Use RomType.VISIONOS instead.", ReplaceWith("RomType.VISIONOS"))
    const val VISIONOS = NewRomType.VISIONOS

    /**
     * - This property is deprecated, use [NewRomType.current] instead.
     */
    @Deprecated(message = "Use RomType.current instead.", ReplaceWith("RomType.current"))
    @JvmStatic
    val current get() = NewRomType.current

    /**
     * - This function is deprecated, use [NewRomType.matches] instead.
     */
    @Deprecated(message = "Use RomType.matches instead.", ReplaceWith("RomType.matches(type)"))
    @JvmStatic
    fun matches(type: Int) = NewRomType.matches(type)
}
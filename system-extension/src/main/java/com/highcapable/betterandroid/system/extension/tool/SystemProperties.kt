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
 * This file is created by fankes on 2023/10/23.
 */
package com.highcapable.betterandroid.system.extension.tool

import com.highcapable.betterandroid.system.extension.utils.SystemProperties as NewSystemProperties

/**
 * Use [NewSystemProperties] instead.
 */
@Deprecated(
    message = "Use SystemProperties instead.",
    ReplaceWith(
        expression = "SystemProperties",
        imports = arrayOf("com.highcapable.betterandroid.system.extension.utils.SystemProperties")
    )
)
object SystemProperties {

    /**
     * - This function is deprecated, use [NewSystemProperties.contains] instead.
     */
    @Deprecated(message = "Use SystemProperties.contains instead.", ReplaceWith("SystemProperties.contains(key)"))
    @JvmStatic
    fun contains(key: String) = NewSystemProperties.contains(key)

    /**
     * - This function is deprecated, use [NewSystemProperties.get] instead.
     */
    @Deprecated(message = "Use SystemProperties.get instead.", ReplaceWith("SystemProperties.get(key, default)"))
    @JvmStatic
    @JvmOverloads
    fun get(key: String, default: String = "") = NewSystemProperties.get(key, default)

    /**
     * - This function is deprecated, use [NewSystemProperties.getInt] instead.
     */
    @Deprecated(message = "Use SystemProperties.getInt instead.", ReplaceWith("SystemProperties.getInt(key, default)"))
    @JvmStatic
    @JvmOverloads
    fun getInt(key: String, default: Int = 0) = NewSystemProperties.getInt(key, default)

    /**
     * - This function is deprecated, use [NewSystemProperties.getLong] instead.
     */
    @Deprecated(message = "Use SystemProperties.getLong instead.", ReplaceWith("SystemProperties.getLong(key, default)"))
    @JvmStatic
    @JvmOverloads
    fun getLong(key: String, default: Long = 0L) = NewSystemProperties.getLong(key, default)

    /**
     * - This function is deprecated, use [NewSystemProperties.getBoolean] instead.
     */
    @Deprecated(message = "Use SystemProperties.getBoolean instead.", ReplaceWith("SystemProperties.getBoolean(key, default)"))
    @JvmStatic
    @JvmOverloads
    fun getBoolean(key: String, default: Boolean = false) = NewSystemProperties.getBoolean(key, default)
}
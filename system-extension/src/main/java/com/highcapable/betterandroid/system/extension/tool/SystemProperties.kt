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
 * This file is created by fankes on 2023/10/23.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.system.extension.tool

import com.highcapable.yukireflection.factory.lazyClass
import com.highcapable.yukireflection.factory.method
import com.highcapable.yukireflection.type.java.BooleanType
import com.highcapable.yukireflection.type.java.IntType
import com.highcapable.yukireflection.type.java.LongType
import com.highcapable.yukireflection.type.java.StringClass

/**
 * This is a system properties extension tool mirrored from "android.os.SystemProperties".
 *
 * You can directly access the system's properties through this tool.
 *
 * - Note: The core functionality relies on calling system class through Java reflection,
 *   which may be limited by the system versions and third-party ROMs.
 */
object SystemProperties {

    /** The current [Class] instance. */
    private val SystemPropertiesClass by lazyClass("android.os.SystemProperties")

    /**
     * Determine whether system properties contain the specified [key].
     * @param key the key name.
     * @return [Boolean]
     */
    @JvmStatic
    fun contains(key: String) = get(key).isNotBlank()

    /**
     * Get the system properties key-value, and return as [String].
     * @param key the key name.
     * @param default the default value.
     * @return [String]
     */
    @JvmStatic
    @JvmOverloads
    fun get(key: String, default: String = "") =
        SystemPropertiesClass.method {
            name = "get"
            param(StringClass, StringClass)
        }.ignored().get().string(key, default)

    /**
     * Get the system properties key-value, and return as [Int].
     * @param key the key name.
     * @param default the default value.
     * @return [Int]
     */
    @JvmStatic
    @JvmOverloads
    fun getInt(key: String, default: Int = 0) =
        SystemPropertiesClass.method {
            name = "getInt"
            param(StringClass, IntType)
        }.ignored().get().int(key, default)

    /**
     * Get the system properties key-value, and return as [Long].
     * @param key the key name.
     * @param default the default value.
     * @return [Long]
     */
    @JvmStatic
    @JvmOverloads
    fun getLong(key: String, default: Long = 0L) =
        SystemPropertiesClass.method {
            name = "getLong"
            param(StringClass, LongType)
        }.ignored().get().long(key, default)

    /**
     * Get the system properties key-value, and return as [Boolean].
     *
     * Values 'n', 'no', '0', 'false' or 'off' are considered false.
     *
     * Values 'y', 'yes', '1', 'true' or 'on' are considered true.
     *
     * (case sensitive).
     *
     * See [here](https://android.googlesource.com/platform/frameworks/base.git/+/refs/heads/main/core/java/android/os/SystemProperties.java#213).
     * @param key the key name.
     * @param default the default value.
     * @return [Boolean]
     */
    @JvmStatic
    @JvmOverloads
    fun getBoolean(key: String, default: Boolean = false) =
        SystemPropertiesClass.method {
            name = "getInt"
            param(StringClass, BooleanType)
        }.ignored().get().boolean(key, default)
}
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
@file:Suppress("unused")

package com.highcapable.betterandroid.system.extension.utils

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.kavaref.extension.lazyClass

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

    /** The method used to get system properties key-value. */
    private val getMethod by lazy {
        SystemPropertiesClass.resolve()
            .optional(silent = true)
            .firstMethodOrNull {
                name = "get"
                parameters(String::class, String::class)
            }
    }

    /** The method used to get system properties key-value as [Int]. */
    private val getIntMethod by lazy {
        SystemPropertiesClass.resolve()
            .optional(silent = true)
            .firstMethodOrNull {
                name = "getInt"
                parameters(String::class, Int::class)
            }
    }

    /** The method used to get system properties key-value as [Long]. */
    private val getLongMethod by lazy {
        SystemPropertiesClass.resolve()
            .optional(silent = true)
            .firstMethodOrNull {
                name = "getLong"
                parameters(String::class, Long::class)
            }
    }

    /** The method used to get system properties key-value as [Boolean]. */
    private val getBooleanMethod by lazy {
        SystemPropertiesClass.resolve()
            .optional(silent = true)
            .firstMethodOrNull {
                name = "getBoolean"
                parameters(String::class, Int::class)
            }
    }

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
        getMethod?.invokeQuietly<String>(key, default) ?: default

    /**
     * Get the system properties key-value, and return as [Int].
     * @param key the key name.
     * @param default the default value.
     * @return [Int]
     */
    @JvmStatic
    @JvmOverloads
    fun getInt(key: String, default: Int = 0) =
        getIntMethod?.invokeQuietly<Int>(key, default) ?: default

    /**
     * Get the system properties key-value, and return as [Long].
     * @param key the key name.
     * @param default the default value.
     * @return [Long]
     */
    @JvmStatic
    @JvmOverloads
    fun getLong(key: String, default: Long = 0L) =
        getLongMethod?.invokeQuietly<Long>(key, default) ?: default

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
        getBooleanMethod?.invokeQuietly<Boolean>(key, default) ?: default
}
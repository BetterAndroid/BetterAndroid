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
 * This file is created by fankes on 2023/4/18.
 */
@file:Suppress("unused", "DEPRECATION", "UNCHECKED_CAST")
@file:JvmName("IntentUtils")

package com.highcapable.betterandroid.system.extension.component

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.highcapable.betterandroid.system.extension.utils.AndroidVersion
import com.highcapable.kavaref.extension.classOf
import java.io.Serializable

/**
 * Get the [Serializable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(name: String) = getSerializableExtraCompat(classOf<T>(), name)

/**
 * Get the [Serializable] data (compat).
 * @see Intent.getSerializableExtraCompat
 * @receiver the current intent.
 * @param type the value type.
 * @param name the key name.
 * @return [T] or null.
 */
fun <T : Serializable> Intent.getSerializableExtraCompat(type: Class<T>, name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getSerializableExtra(name) as? T?) { getSerializableExtra(name, type) }

/**
 * Get the [Serializable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Serializable> Bundle.getSerializableCompat(name: String) = getSerializableCompat(classOf<T>(), name)

/**
 * Get the [Serializable] data (compat).
 * @see Bundle.getSerializableCompat
 * @receiver the current intent.
 * @param type the value type.
 * @param name the key name.
 * @return [T] or null.
 */
fun <T : Serializable> Bundle.getSerializableCompat(type: Class<T>, name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getSerializable(name) as? T?) { getSerializable(name, type) }

/**
 * Get the [Parcelable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(name: String) = getParcelableExtraCompat(classOf<T>(), name)

/**
 * Get the [Parcelable] data (compat).
 * @see Intent.getParcelableExtraCompat
 * @receiver the current intent.
 * @param type the value type.
 * @param name the key name.
 * @return [T] or null.
 */
fun <T : Parcelable> Intent.getParcelableExtraCompat(type: Class<T>, name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getParcelableExtra(name) as? T?) { getParcelableExtra(name, type) }

/**
 * Get the [Parcelable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(name: String) = getParcelableCompat(classOf<T>(), name)

/**
 * Get the [Parcelable] data (compat).
 * @see Bundle.getParcelableCompat
 * @receiver the current intent.
 * @param type the value type.
 * @param name the key name.
 * @return [T] or null.
 */
fun <T : Parcelable> Bundle.getParcelableCompat(type: Class<T>, name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getParcelable(name) as? T?) { getParcelable(name, type) }
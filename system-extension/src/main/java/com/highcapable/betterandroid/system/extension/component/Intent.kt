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
@file:Suppress("unused", "DEPRECATION")
@file:JvmName("IntentUtils")

package com.highcapable.betterandroid.system.extension.component

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.highcapable.betterandroid.system.extension.tool.AndroidVersion
import com.highcapable.kavaref.extension.classOf
import java.io.Serializable

/**
 * Get the [Serializable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getSerializableExtra(name) as? T?) { getSerializableExtra(name, classOf<T>()) }

/**
 * Get the [Serializable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Serializable> Bundle.getSerializableCompat(name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getSerializable(name) as? T?) { getSerializable(name, classOf<T>()) }

/**
 * Get the [Parcelable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getParcelableExtra(name) as? T?) { getParcelableExtra(name, classOf<T>()) }

/**
 * Get the [Parcelable] data (compat).
 * @receiver the current intent.
 * @param name the key name.
 * @return [T] or null.
 */
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(name: String) =
    AndroidVersion.requireOrNull(AndroidVersion.T, getParcelable(name) as? T?) { getParcelable(name, classOf<T>()) }
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
 * This file is created by fankes on 2023/10/27.
 */
@file:Suppress("unused")
@file:JvmName("ServiceUtils")

package com.highcapable.betterandroid.system.extension.component

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresApi
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.yukireflection.factory.classOf

/**
 * Start a [Service] instance [T].
 * @receiver the current context.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [ComponentName] or null.
 */
inline fun <reified T : Service> Context.startService(initiate: Intent.() -> Unit = {}) =
    startService(Intent(this, classOf<T>()).apply(initiate))

/**
 * Start a foreground [Service] instance [T].
 * @receiver the current context.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [ComponentName] or null.
 */
@RequiresApi(SystemVersion.O)
inline fun <reified T : Service> Context.startForegroundService(initiate: Intent.() -> Unit = {}) =
    startForegroundService(Intent(this, classOf<T>()).apply(initiate))

/**
 * Start a [Service] using [ComponentName].
 * @receiver the current context.
 * @param packageName the target package name.
 * @param serviceClass the target app [Service] class name.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [ComponentName] or null.
 */
inline fun Context.startService(packageName: String, serviceClass: String, initiate: Intent.() -> Unit = {}) =
    startService(Intent().apply { component = ComponentName(packageName, serviceClass) }.apply(initiate))

/**
 * Start a foreground [Service] using [ComponentName].
 * @receiver the current context.
 * @param packageName the target package name.
 * @param serviceClass the target app [Service] class name.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [ComponentName] or null.
 */
@RequiresApi(SystemVersion.O)
inline fun Context.startForegroundService(packageName: String, serviceClass: String, initiate: Intent.() -> Unit = {}) =
    startForegroundService(Intent().apply { component = ComponentName(packageName, serviceClass) }.apply(initiate))

/**
 * Start a [Service] instance [T].
 * @receiver the current context.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun <reified T : Service> Context.startServiceOrElse(initiate: Intent.() -> Unit = {}) =
    runCatching { startService<T>(initiate) }.isSuccess

/**
 * Start a foreground [Service] instance [T].
 * @receiver the current context.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
@RequiresApi(SystemVersion.O)
inline fun <reified T : Service> Context.startForegroundServiceOrElse(initiate: Intent.() -> Unit = {}) =
    runCatching { startForegroundService<T>(initiate) }.isSuccess

/**
 * Start a [Service] using [ComponentName].
 * @receiver the current context.
 * @param packageName the target package name.
 * @param serviceClass the target app [Service] class name.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun Context.startServiceOrElse(packageName: String, serviceClass: String, initiate: Intent.() -> Unit = {}) =
    runCatching { startService(packageName, serviceClass, initiate) }.isSuccess

/**
 * Start a foreground [Service] using [ComponentName].
 * @receiver the current context.
 * @param packageName the target package name.
 * @param serviceClass the target app [Service] class name.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
@RequiresApi(SystemVersion.O)
inline fun Context.startForegroundServiceOrElse(packageName: String, serviceClass: String, initiate: Intent.() -> Unit = {}) =
    runCatching { startForegroundService(packageName, serviceClass, initiate) }.isSuccess

/**
 * Start a [Service]
 * @receiver the current context.
 * @param intent the intent to start.
 * @return [Boolean] whether succeed.
 */
fun Context.startServiceOrElse(intent: Intent) = runCatching { startService(intent) }.isSuccess

/**
 * Start a foreground [Service]
 * @receiver the current context.
 * @param intent the intent to start.
 * @return [Boolean] whether succeed.
 */
@RequiresApi(SystemVersion.O)
fun Context.startForegroundServiceOrElse(intent: Intent) = runCatching { startForegroundService(intent) }.isSuccess
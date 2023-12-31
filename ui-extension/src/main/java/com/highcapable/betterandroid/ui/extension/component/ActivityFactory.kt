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
 * This file is created by fankes on 2022/11/24.
 */
@file:Suppress("unused")
@file:JvmName("ActivityUtils")

package com.highcapable.betterandroid.ui.extension.component

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.highcapable.betterandroid.system.extension.component.queryLaunchActivitiesForPackage
import com.highcapable.betterandroid.system.extension.component.queryLaunchActivitiesForPackageOrNull
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.yukireflection.factory.classOf

/**
 * Determine whether the current activity is in any special mode,
 * such as mini-window, multi-window, split-screen, etc.
 *
 * - This solution was undesirable, so it was deprecated, use [Activity.isInMultiWindowModeCompat] instead.
 * @see Activity.isInMultiWindowModeCompat
 */
@Deprecated(message = "Use isInMultiWindowModeCompat instead.", ReplaceWith("isInMultiWindowModeCompat"))
val Activity.isInSpecialWindowMode get() = isInMultiWindowModeCompat

/**
 * Whether the activity is currently in multi-window mode (compat).
 *
 * - Note: This function will always return false on system version prior to Android 7.
 * @receiver the current activty.
 * @return [Boolean]
 */
val Activity.isInMultiWindowModeCompat get() = SystemVersion.require(SystemVersion.N, false) { isInMultiWindowMode }

/**
 * Start an [Activity] instance [T].
 * @see Context.startActivityOrElse
 * @receiver the current context.
 * @param newTask whether to start with a new task, default false.
 * @param initiate the [Intent] builder body, default is empty.
 */
inline fun <reified T : Activity> Context.startActivity(newTask: Boolean = false, initiate: Intent.() -> Unit = {}) =
    startActivity(Intent(this, classOf<T>()).apply { if (newTask) flags = flags or Intent.FLAG_ACTIVITY_NEW_TASK }.apply(initiate))

/**
 * Start an [Activity] using [ComponentName].
 * @see Context.startActivityOrElse
 * @receiver the current context.
 * @param packageName the target package name.
 * @param activityClass the target app [Activity] class name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param initiate the [Intent] builder body, default is empty.
 */
inline fun Context.startActivity(packageName: String, activityClass: String, newTask: Boolean = true, initiate: Intent.() -> Unit = {}) =
    startActivity(Intent().apply {
        if (newTask) flags = flags or Intent.FLAG_ACTIVITY_NEW_TASK
        component = ComponentName(packageName, activityClass)
    }.apply(initiate))

/**
 * Start an [Activity] using [ComponentName].
 *
 * This function auto obtains the [Activity] class name that
 * the target app needs to start through [PackageManager.getLaunchIntentForPackage].
 *
 * - The [Manifest.permission.QUERY_ALL_PACKAGES] permissions or a queries list declaration required
 *   when target sdk higher than 29.
 * @see Context.startActivityOrElse
 * @receiver the current context.
 * @param packageName the target package name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param initiate the [Intent] builder body, default is empty.
 * @throws IllegalStateException if the [Activity] class name that needs to be
 * started by the target app cannot be found.
 */
inline fun Context.startActivity(packageName: String, newTask: Boolean = true, initiate: Intent.() -> Unit = {}) {
    val className = packageManager?.queryLaunchActivitiesForPackage(packageName)?.firstOrNull()?.activityInfo?.name
        ?: error("No launch activities found for package \"$packageName\".")
    startActivity(packageName, className, newTask, initiate)
}

/**
 * Start an [Activity] instance [T].
 * @receiver the current context.
 * @param newTask whether to start with a new task, default false.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun <reified T : Activity> Context.startActivityOrElse(newTask: Boolean = false, initiate: Intent.() -> Unit = {}) =
    runCatching { startActivity<T>(newTask, initiate) }.isSuccess

/**
 * Start an [Activity] using [ComponentName].
 * @receiver the current context.
 * @param packageName the target package name.
 * @param activityClass the target app [Activity] class name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun Context.startActivityOrElse(packageName: String, activityClass: String, newTask: Boolean = true, initiate: Intent.() -> Unit = {}) =
    runCatching { startActivity(packageName, activityClass, newTask, initiate) }.isSuccess

/**
 * Start an [Activity] using [ComponentName].
 *
 * This function auto obtains the [Activity] class name that
 * the target app needs to start through [PackageManager.getLaunchIntentForPackage].
 *
 * - The [Manifest.permission.QUERY_ALL_PACKAGES] permissions or a queries list declaration required
 *   when target sdk higher than 29.
 * @receiver the current context.
 * @param packageName the target package name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param initiate the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun Context.startActivityOrElse(packageName: String, newTask: Boolean = true, initiate: Intent.() -> Unit = {}): Boolean {
    val className = packageManager?.queryLaunchActivitiesForPackageOrNull(packageName)?.firstOrNull()?.activityInfo?.name ?: return false
    return runCatching { startActivity(packageName, className, newTask, initiate) }.isSuccess
}

/**
 * Start an [Activity].
 * @receiver the current context.
 * @param intent the intent to start.
 * @return [Boolean] whether succeed.
 */
fun Context.startActivityOrElse(intent: Intent) = runCatching { startActivity(intent) }.isSuccess
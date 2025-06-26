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
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.highcapable.betterandroid.system.extension.component.queryLaunchActivitiesForPackage
import com.highcapable.betterandroid.system.extension.component.queryLaunchActivitiesForPackageOrNull
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.kavaref.extension.classOf

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
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 */
inline fun <reified T : Activity> Context.startActivity(
    newTask: Boolean = false,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = startActivity(
    Intent(this, classOf<T>()).apply {
        if (newTask) flags = flags or Intent.FLAG_ACTIVITY_NEW_TASK
    }.apply(intent), options
)

/**
 * Start an [Activity] instance [T].
 * @see Fragment.startActivityOrElse
 * @receiver the current context.
 * @param newTask whether to start with a new task, default false.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 */
inline fun <reified T : Activity> Fragment.startActivity(
    newTask: Boolean = false,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = requireContext().startActivity<T>(newTask, options, intent)

/**
 * Start an [Activity] using [ComponentName].
 * @see Context.startActivityOrElse
 * @receiver the current context.
 * @param packageName the target package name.
 * @param activityClass the target app [Activity] class name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 */
inline fun Context.startActivity(
    packageName: String,
    activityClass: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = startActivity(
    Intent().apply {
        if (newTask) flags = flags or Intent.FLAG_ACTIVITY_NEW_TASK
        component = ComponentName(packageName, activityClass)
    }.apply(intent), options
)

/**
 * Start an [Activity] using [ComponentName].
 * @see Fragment.startActivityOrElse
 * @receiver the current context.
 * @param packageName the target package name.
 * @param activityClass the target app [Activity] class name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 */
inline fun Fragment.startActivity(
    packageName: String,
    activityClass: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = requireContext().startActivity(packageName, activityClass, newTask, options, intent)

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
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @throws IllegalStateException if the [Activity] class name that needs to be
 * started by the target app cannot be found.
 */
inline fun Context.startActivity(
    packageName: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) {
    val className = packageManager?.queryLaunchActivitiesForPackage(packageName)?.firstOrNull()?.activityInfo?.name
        ?: error("No launch activities found for package \"$packageName\".")
    startActivity(packageName, className, newTask, options, intent)
}

/**
 * Start an [Activity] using [ComponentName].
 *
 * This function auto obtains the [Activity] class name that
 * the target app needs to start through [PackageManager.getLaunchIntentForPackage].
 *
 * - The [Manifest.permission.QUERY_ALL_PACKAGES] permissions or a queries list declaration required
 *   when target sdk higher than 29.
 * @see Fragment.startActivityOrElse
 * @receiver the current context.
 * @param packageName the target package name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @throws IllegalStateException if the [Activity] class name that needs to be
 * started by the target app cannot be found.
 */
inline fun Fragment.startActivity(
    packageName: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = requireContext().startActivity(packageName, newTask, options, intent)

/**
 * Start an [Activity] instance [T].
 * @receiver the current context.
 * @param newTask whether to start with a new task, default false.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun <reified T : Activity> Context.startActivityOrElse(
    newTask: Boolean = false,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = runCatching { startActivity<T>(newTask, options, intent) }.isSuccess

/**
 * Start an [Activity] instance [T].
 * @receiver the current context.
 * @param newTask whether to start with a new task, default false.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun <reified T : Activity> Fragment.startActivityOrElse(
    newTask: Boolean = false,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = requireContext().startActivityOrElse<T>(newTask, options, intent)

/**
 * Start an [Activity] using [ComponentName].
 * @receiver the current context.
 * @param packageName the target package name.
 * @param activityClass the target app [Activity] class name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun Context.startActivityOrElse(
    packageName: String,
    activityClass: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = runCatching { startActivity(packageName, activityClass, newTask, options, intent) }.isSuccess

/**
 * Start an [Activity] using [ComponentName].
 * @receiver the current context.
 * @param packageName the target package name.
 * @param activityClass the target app [Activity] class name.
 * @param newTask whether to start with a new task, default true,
 * if not it will cause the top stack to overlap.
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun Fragment.startActivityOrElse(
    packageName: String,
    activityClass: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = requireContext().startActivityOrElse(packageName, activityClass, newTask, options, intent)

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
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun Context.startActivityOrElse(
    packageName: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
): Boolean {
    val className = packageManager?.queryLaunchActivitiesForPackageOrNull(packageName)?.firstOrNull()?.activityInfo?.name ?: return false
    return runCatching { startActivity(packageName, className, newTask, options, intent) }.isSuccess
}

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
 * @param options the [Bundle], default is null.
 * @param intent the [Intent] builder body, default is empty.
 * @return [Boolean] whether succeed.
 */
inline fun Fragment.startActivityOrElse(
    packageName: String,
    newTask: Boolean = true,
    options: Bundle? = null,
    intent: Intent.() -> Unit = {}
) = requireContext().startActivityOrElse(packageName, newTask, options, intent)

/**
 * Start an [Activity].
 * @receiver the current context.
 * @param intent the intent to start.
 * @param options the [Bundle], default is null.
 * @return [Boolean] whether succeed.
 */
fun Context.startActivityOrElse(intent: Intent, options: Bundle? = null) = runCatching { startActivity(intent, options) }.isSuccess

/**
 * Start an [Activity].
 * @receiver the current context.
 * @param intent the intent to start.
 * @param options the [Bundle], default is null.
 * @return [Boolean] whether succeed.
 */
fun Fragment.startActivityOrElse(intent: Intent, options: Bundle? = null) = requireContext().startActivityOrElse(intent, options)
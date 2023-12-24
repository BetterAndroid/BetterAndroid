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
 * This file is created by fankes on 2023/10/28.
 */
@file:Suppress("unused", "InlinedApi")
@file:JvmName("ApplicationUtils")

package com.highcapable.betterandroid.system.extension.component

import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.annotation.RequiresApi
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.yukireflection.factory.classOf
import com.highcapable.yukireflection.factory.current

/**
 * Get the current component name of this application.
 *
 * Usage:
 *
 * ```kotlin
 * // Get the MainActivity's component name.
 * val componentName = context.getComponentName<MainActivity>()
 * ```
 * @receiver the current context.
 * @return [ComponentName]
 */
inline fun <reified T> Context.getComponentName() = ComponentName(this, classOf<T>())

/**
 * Determine whether the [packageName] was installed.
 * @receiver the current package manager.
 * @param packageName the target package name.
 * @return [Boolean]
 */
fun PackageManager.hasPackage(packageName: String) = getPackageInfoOrNull(packageName) != null

/**
 * Determine whether the [packageName] has any launch activity.
 * @receiver the current package manager.
 * @param packageName the target package name.
 * @return [Boolean]
 */
fun PackageManager.hasLaunchActivity(packageName: String) = !queryLaunchActivitiesForPackageOrNull(packageName).isNullOrEmpty()

/**
 * The wrapper of [PackageManager]'s function.
 * @see PackageManager.getPackageInfo
 * @see PackageManager.getPackageInfoOrNull
 * @see PackageInfoFlagsWrapper
 * @receiver the current package manager.
 * @param packageName the target package name.
 * @param flags the package info flags wrappers,
 * default is [PackageInfoFlagsWrapper.GET_CONFIGURATIONS].
 * @return [PackageInfo]
 * @throws IllegalStateException if there is a permission exception or other errors.
 */
fun PackageManager.getPackageInfo(packageName: String, vararg flags: PackageInfoFlagsWrapper) =
    getPackageInfoOrNull(packageName, *flags) ?: error("Unable to get package info for package \"$packageName\", package not found or invalid.")

/**
 * The wrapper of [PackageManager]'s function.
 * @see PackageManager.getPackageInfo
 * @see PackageInfoFlagsWrapper
 * @receiver the current package manager.
 * @param packageName the target package name.
 * @param flags the package info flags wrappers,
 * default is [PackageInfoFlagsWrapper.GET_CONFIGURATIONS].
 * @return [PackageInfo] or null.
 */
fun PackageManager.getPackageInfoOrNull(packageName: String, vararg flags: PackageInfoFlagsWrapper) =
    runCatching { getPackageInfo(packageName, flags.toSystemType()) }.getOrNull()

/**
 * The wrapper of [PackageManager]'s function.
 * @see PackageManager.getInstalledPackages
 * @see PackageManager.getInstalledPackagesOrNull
 * @see PackageInfoFlagsWrapper
 * @receiver the current package manager.
 * @param flags the package info flags wrappers,
 * default is [PackageInfoFlagsWrapper.GET_CONFIGURATIONS].
 * @return [List]<[PackageInfo]>
 * @throws IllegalStateException if there is a permission exception or other errors.
 */
fun PackageManager.getInstalledPackages(vararg flags: PackageInfoFlagsWrapper) =
    getInstalledPackagesOrNull(*flags) ?: error("Unable to get installed packages.")

/**
 * The wrapper of [PackageManager]'s function.
 * @see PackageManager.getInstalledPackages
 * @see PackageInfoFlagsWrapper
 * @receiver the current package manager.
 * @param flags the package info flags wrappers,
 * default is [PackageInfoFlagsWrapper.GET_CONFIGURATIONS].
 * @return [List]<[PackageInfo]> or null.
 */
fun PackageManager.getInstalledPackagesOrNull(vararg flags: PackageInfoFlagsWrapper): List<PackageInfo>? =
    runCatching { getInstalledPackages(flags.toSystemType()) }.getOrNull()

/**
 * Query launch activities for [packageName].
 * @receiver the current package manager.
 * @param packageName the target package name.
 * @return [List]<[ResolveInfo]>
 * @throws IllegalStateException if there is a permission exception or other errors.
 */
fun PackageManager.queryLaunchActivitiesForPackage(packageName: String) =
    queryLaunchActivitiesForPackageOrNull(packageName) ?: error("Unable to get launch activities for package \"$packageName\".")

/**
 * Query launch activities for [packageName].
 * @receiver the current package manager.
 * @param packageName the target package name.
 * @return [List]<[ResolveInfo]> or null.
 */
fun PackageManager.queryLaunchActivitiesForPackageOrNull(packageName: String): List<ResolveInfo>? =
    runCatching { queryIntentActivities(getLaunchIntentForPackage(packageName)!!, 0) }.getOrNull()

/**
 * Determine whether the [componentName] is enabled or default.
 *
 * If you want to get the application self component name,
 * see [Context.getComponentName].
 * @see Context.getComponentName
 * @receiver the current package manager.
 * @param componentName the current component name.
 * @return [Boolean]
 */
fun PackageManager.isComponentEnabled(componentName: ComponentName) =
    getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED

/**
 * Determine whether the [componentName] is enabled.
 *
 * - This function is deprecated, use [PackageManager.isComponentEnabled] instead.
 */
@Deprecated(message = "Use PackageManager.isComponentEnabled instead.", ReplaceWith("isComponentEnabled(componentName)"))
fun PackageManager.hasComponentEnabled(componentName: ComponentName) = isComponentEnabled(componentName)

/**
 * Enable an [componentName].
 *
 * There will set the component status to [PackageManager.COMPONENT_ENABLED_STATE_ENABLED].
 *
 * If you want to get the application self component name,
 * see [Context.getComponentName].
 * @see Context.getComponentName
 * @receiver the current package manager.
 * @param componentName the current component name.
 * @param flags the optional behavior flags, default is [PackageManager.DONT_KILL_APP].
 */
fun PackageManager.enableComponent(componentName: ComponentName, vararg flags: Int) {
    val newFlags = flags.fold(PackageManager.DONT_KILL_APP) { flag, newFlag -> flag or newFlag }
    setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, newFlags)
}

/**
 * Disable an [componentName].
 *
 * There will set the component status to [PackageManager.COMPONENT_ENABLED_STATE_DISABLED].
 *
 * If you want to get the application self component name,
 * see [Context.getComponentName].
 * @see Context.getComponentName
 * @receiver the current package manager.
 * @param componentName the current component name.
 * @param flags the optional behavior flags, default is [PackageManager.DONT_KILL_APP].
 */
fun PackageManager.disableComponent(componentName: ComponentName, vararg flags: Int) {
    val newFlags = flags.fold(PackageManager.DONT_KILL_APP) { flag, newFlag -> flag or newFlag }
    setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, newFlags)
}

/**
 * Reset an [componentName].
 *
 * There will set the component status to [PackageManager.COMPONENT_ENABLED_STATE_DEFAULT].
 *
 * If you want to get the application self component name,
 * see [Context.getComponentName].
 * @see Context.getComponentName
 * @receiver the current package manager.
 * @param componentName the current component name.
 * @param flags the optional behavior flags, default is [PackageManager.DONT_KILL_APP].
 */
fun PackageManager.resetComponent(componentName: ComponentName, vararg flags: Int) {
    val newFlags = flags.fold(PackageManager.DONT_KILL_APP) { flag, newFlag -> flag or newFlag }
    setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, newFlags)
}

/**
 * Get the version code of package info (compat).
 * @receiver [PackageInfo]
 * @return [Long]
 */
val PackageInfo.versionCodeCompat
    @Suppress("DEPRECATION")
    get() = SystemVersion.require(SystemVersion.P, versionCode.toLong()) { longVersionCode }

/**
 * Get the long version code of package info (compat).
 *
 * - This property is deprecated, use [PackageInfo.versionCodeCompat] instead.
 */
@Deprecated(message = "Use PackageInfo.versionCodeCompat instead.", ReplaceWith("versionCodeCompat"))
val PackageInfo.longVersionCodeCompat get() = versionCodeCompat

/**
 * Get the primary cpu abi for the application.
 * @receiver [ApplicationInfo]
 * @return [String]
 */
val ApplicationInfo.primaryCpuAbi get() = current(ignored = true).field { name = "primaryCpuAbi" }.string()

/**
 * Get the secondary cpu abi for the application.
 * @receiver [ApplicationInfo]
 * @return [String]
 */
val ApplicationInfo.secondaryCpuAbi get() = current(ignored = true).field { name = "secondaryCpuAbi" }.string()

/**
 * Determine whether the application has given flags.
 * @receiver the current application info.
 * @param flags the application flags wrappers.
 * @return [Boolean]
 */
fun ApplicationInfo.hasFlags(vararg flags: ApplicationInfoFlagsWrapper) = this.flags == flags.toSystemType()

/**
 * Convert [Array]<[PackageInfoFlagsWrapper]> to [PackageManager] types.
 *
 * If empty will returns [PackageInfoFlagsWrapper.GET_CONFIGURATIONS].
 * @receiver the [PackageInfoFlagsWrapper] type.
 * @return [Int]
 */
private fun Array<out PackageInfoFlagsWrapper>.toSystemType() =
    fold(PackageInfoFlagsWrapper.GET_CONFIGURATIONS.original) { flag, wrapper -> flag or wrapper.original }

/**
 * Convert [Array]<[ApplicationInfoFlagsWrapper]> to [ApplicationInfo] types.
 *
 * If empty will returns -1.
 * @receiver the [ApplicationInfoFlagsWrapper] type.
 * @return [Int]
 */
private fun Array<out ApplicationInfoFlagsWrapper>.toSystemType() = fold(-1) { flag, wrapper -> flag or wrapper.original }

/**
 * Package info flags wrapper.
 * @param original the original [PackageManager] type.
 */
enum class PackageInfoFlagsWrapper(val original: Int) {
    /** @see PackageManager.GET_ACTIVITIES */
    GET_ACTIVITIES(PackageManager.GET_ACTIVITIES),

    /** @see PackageManager.GET_CONFIGURATIONS */
    GET_CONFIGURATIONS(PackageManager.GET_CONFIGURATIONS),

    /** @see PackageManager.GET_GIDS */
    GET_GIDS(PackageManager.GET_GIDS),

    /** @see PackageManager.GET_INSTRUMENTATION */
    GET_INSTRUMENTATION(PackageManager.GET_INSTRUMENTATION),

    /** @see PackageManager.GET_INTENT_FILTERS */
    @Suppress("DEPRECATION")
    GET_INTENT_FILTERS(PackageManager.GET_INTENT_FILTERS),

    /** @see PackageManager.GET_META_DATA */
    GET_META_DATA(PackageManager.GET_META_DATA),

    /** @see PackageManager.GET_PERMISSIONS */
    GET_PERMISSIONS(PackageManager.GET_PERMISSIONS),

    /** @see PackageManager.GET_PROVIDERS */
    GET_PROVIDERS(PackageManager.GET_PROVIDERS),

    /** @see PackageManager.GET_RECEIVERS */
    GET_RECEIVERS(PackageManager.GET_RECEIVERS),

    /** @see PackageManager.GET_SERVICES */
    GET_SERVICES(PackageManager.GET_SERVICES),

    /** @see PackageManager.GET_SHARED_LIBRARY_FILES */
    GET_SHARED_LIBRARY_FILES(PackageManager.GET_SHARED_LIBRARY_FILES),

    /** @see PackageManager.GET_SIGNATURES */
    @Suppress("DEPRECATION")
    GET_SIGNATURES(PackageManager.GET_SIGNATURES),

    /** @see PackageManager.GET_SIGNING_CERTIFICATES */
    @RequiresApi(SystemVersion.P)
    GET_SIGNING_CERTIFICATES(PackageManager.GET_SIGNING_CERTIFICATES),

    /** @see PackageManager.MATCH_UNINSTALLED_PACKAGES */
    MATCH_UNINSTALLED_PACKAGES(PackageManager.MATCH_UNINSTALLED_PACKAGES)
}

/**
 * Application info flags wrapper.
 * @param original the original [ApplicationInfo] type.
 */
enum class ApplicationInfoFlagsWrapper(val original: Int) {
    /** @see ApplicationInfo.FLAG_SYSTEM */
    SYSTEM(ApplicationInfo.FLAG_SYSTEM),

    /** @see ApplicationInfo.FLAG_DEBUGGABLE */
    DEBUGGABLE(ApplicationInfo.FLAG_DEBUGGABLE),

    /** @see ApplicationInfo.FLAG_HAS_CODE */
    HAS_CODE(ApplicationInfo.FLAG_HAS_CODE),

    /** @see ApplicationInfo.FLAG_PERSISTENT */
    PERSISTENT(ApplicationInfo.FLAG_PERSISTENT),

    /** @see ApplicationInfo.FLAG_FACTORY_TEST */
    FACTORY_TEST(ApplicationInfo.FLAG_FACTORY_TEST),

    /** @see ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING */
    ALLOW_TASK_REPARENTING(ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING),

    /** @see ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA */
    ALLOW_CLEAR_USER_DATA(ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA),

    /** @see ApplicationInfo.FLAG_UPDATED_SYSTEM_APP */
    UPDATED_SYSTEM_APP(ApplicationInfo.FLAG_UPDATED_SYSTEM_APP),

    /** @see ApplicationInfo.FLAG_TEST_ONLY */
    TEST_ONLY(ApplicationInfo.FLAG_TEST_ONLY),

    /** @see ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS */
    SUPPORTS_SMALL_SCREENS(ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS),

    /** @see ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS */
    SUPPORTS_NORMAL_SCREENS(ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS),

    /** @see ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS */
    SUPPORTS_LARGE_SCREENS(ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS),

    /** @see ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS */
    SUPPORTS_XLARGE_SCREENS(ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS),

    /** @see ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS */
    RESIZEABLE_FOR_SCREENS(ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS),

    /** @see ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES */
    @Suppress("DEPRECATION")
    SUPPORTS_SCREEN_DENSITIES(ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES),

    /** @see ApplicationInfo.FLAG_VM_SAFE_MODE */
    VM_SAFE_MODE(ApplicationInfo.FLAG_VM_SAFE_MODE),

    /** @see ApplicationInfo.FLAG_ALLOW_BACKUP */
    ALLOW_BACKUP(ApplicationInfo.FLAG_ALLOW_BACKUP),

    /** @see ApplicationInfo.FLAG_KILL_AFTER_RESTORE */
    KILL_AFTER_RESTORE(ApplicationInfo.FLAG_KILL_AFTER_RESTORE),

    /** @see ApplicationInfo.FLAG_RESTORE_ANY_VERSION */
    RESTORE_ANY_VERSION(ApplicationInfo.FLAG_RESTORE_ANY_VERSION),

    /** @see ApplicationInfo.FLAG_EXTERNAL_STORAGE */
    EXTERNAL_STORAGE(ApplicationInfo.FLAG_EXTERNAL_STORAGE),

    /** @see ApplicationInfo.FLAG_LARGE_HEAP */
    LARGE_HEAP(ApplicationInfo.FLAG_LARGE_HEAP),

    /** @see ApplicationInfo.FLAG_STOPPED */
    STOPPED(ApplicationInfo.FLAG_STOPPED),

    /** @see ApplicationInfo.FLAG_SUPPORTS_RTL */
    SUPPORTS_RTL(ApplicationInfo.FLAG_SUPPORTS_RTL),

    /** @see ApplicationInfo.FLAG_INSTALLED */
    INSTALLED(ApplicationInfo.FLAG_INSTALLED),

    /** @see ApplicationInfo.FLAG_IS_DATA_ONLY */
    IS_DATA_ONLY(ApplicationInfo.FLAG_IS_DATA_ONLY),

    /** @see ApplicationInfo.FLAG_IS_GAME */
    @Suppress("DEPRECATION")
    IS_GAME(ApplicationInfo.FLAG_IS_GAME),

    /** @see ApplicationInfo.FLAG_FULL_BACKUP_ONLY */
    FULL_BACKUP_ONLY(ApplicationInfo.FLAG_FULL_BACKUP_ONLY)
}

/**
 * Workaround for class name changed for [ApplicationInfoFlagsWrapper].
 */
@Deprecated(message = "Use ApplicationInfoFlagsWrapper instead.", ReplaceWith("ApplicationInfoFlagsWrapper"))
typealias ApplicationFlagsWrapper = ApplicationInfoFlagsWrapper
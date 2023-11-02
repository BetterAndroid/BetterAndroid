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
import androidx.core.content.pm.PackageInfoCompat
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
 *
 * See [PackageManager.getPackageInfo].
 *
 * See also [PackageManager.getPackageInfoOrNull], [PackageInfoFlagsWrapper].
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
 *
 * See [PackageManager.getPackageInfo].
 *
 * See also [PackageInfoFlagsWrapper].
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
 *
 * See [PackageManager.getInstalledPackages].
 *
 * See also [PackageManager.getInstalledPackagesOrNull], [PackageInfoFlagsWrapper].
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
 *
 * See [PackageManager.getInstalledPackages].
 *
 * See also [PackageInfoFlagsWrapper].
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
 * Determine whether the [componentName] is enabled.
 *
 * If you want to get the application self component name,
 * see [Context.getComponentName].
 * @receiver the current package manager.
 * @param componentName the current component name.
 * @return [Boolean]
 */
fun PackageManager.hasComponentEnabled(componentName: ComponentName) =
    getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED

/**
 * Enable an [componentName].
 *
 * There will set the component status to [PackageManager.COMPONENT_ENABLED_STATE_ENABLED].
 *
 * If you want to get the application self component name,
 * see [Context.getComponentName].
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
 * @receiver the current package manager.
 * @param componentName the current component name.
 * @param flags the optional behavior flags, default is [PackageManager.DONT_KILL_APP].
 */
fun PackageManager.resetComponent(componentName: ComponentName, vararg flags: Int) {
    val newFlags = flags.fold(PackageManager.DONT_KILL_APP) { flag, newFlag -> flag or newFlag }
    setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, newFlags)
}

/**
 * Get the long version code of package info (compat).
 * @receiver [PackageInfo]
 * @return [Long]
 */
val PackageInfo.longVersionCodeCompat get() = PackageInfoCompat.getLongVersionCode(this)

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
fun ApplicationInfo.hasFlags(vararg flags: ApplicationFlagsWrapper) = this.flags == flags.toSystemType()

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
 * Convert [Array]<[ApplicationFlagsWrapper]> to [ApplicationInfo] types.
 *
 * If empty will returns -1.
 * @receiver the [ApplicationFlagsWrapper] type.
 * @return [Int]
 */
private fun Array<out ApplicationFlagsWrapper>.toSystemType() = fold(-1) { flag, wrapper -> flag or wrapper.original }

/**
 * Package info flags wrapper.
 * @param original the original [PackageManager] type.
 */
enum class PackageInfoFlagsWrapper(val original: Int) {
    /** See [PackageManager.GET_ACTIVITIES]. */
    GET_ACTIVITIES(PackageManager.GET_ACTIVITIES),

    /** See [PackageManager.GET_CONFIGURATIONS]. */
    GET_CONFIGURATIONS(PackageManager.GET_CONFIGURATIONS),

    /** See [PackageManager.GET_GIDS]. */
    GET_GIDS(PackageManager.GET_GIDS),

    /** See [PackageManager.GET_INSTRUMENTATION]. */
    GET_INSTRUMENTATION(PackageManager.GET_INSTRUMENTATION),

    /** See [PackageManager.GET_INTENT_FILTERS]. */
    @Suppress("DEPRECATION")
    GET_INTENT_FILTERS(PackageManager.GET_INTENT_FILTERS),

    /** See [PackageManager.GET_META_DATA]. */
    GET_META_DATA(PackageManager.GET_META_DATA),

    /** See [PackageManager.GET_PERMISSIONS]. */
    GET_PERMISSIONS(PackageManager.GET_PERMISSIONS),

    /** See [PackageManager.GET_PROVIDERS]. */
    GET_PROVIDERS(PackageManager.GET_PROVIDERS),

    /** See [PackageManager.GET_RECEIVERS]. */
    GET_RECEIVERS(PackageManager.GET_RECEIVERS),

    /** See [PackageManager.GET_SERVICES]. */
    GET_SERVICES(PackageManager.GET_SERVICES),

    /** See [PackageManager.GET_SHARED_LIBRARY_FILES]. */
    GET_SHARED_LIBRARY_FILES(PackageManager.GET_SHARED_LIBRARY_FILES),

    /** See [PackageManager.GET_SIGNATURES]. */
    @Suppress("DEPRECATION")
    GET_SIGNATURES(PackageManager.GET_SIGNATURES),

    /** See [PackageManager.GET_SIGNING_CERTIFICATES]. */
    @RequiresApi(SystemVersion.P)
    GET_SIGNING_CERTIFICATES(PackageManager.GET_SIGNING_CERTIFICATES),

    /** See [PackageManager.MATCH_UNINSTALLED_PACKAGES]. */
    MATCH_UNINSTALLED_PACKAGES(PackageManager.MATCH_UNINSTALLED_PACKAGES)
}

/**
 * Application info flags wrapper.
 * @param original the original [ApplicationInfo] type.
 */
enum class ApplicationFlagsWrapper(val original: Int) {
    /** See [ApplicationInfo.FLAG_SYSTEM]. */
    FLAG_SYSTEM(ApplicationInfo.FLAG_SYSTEM),

    /** See [ApplicationInfo.FLAG_DEBUGGABLE]. */
    FLAG_DEBUGGABLE(ApplicationInfo.FLAG_DEBUGGABLE),

    /** See [ApplicationInfo.FLAG_HAS_CODE]. */
    FLAG_HAS_CODE(ApplicationInfo.FLAG_HAS_CODE),

    /** See [ApplicationInfo.FLAG_PERSISTENT]. */
    FLAG_PERSISTENT(ApplicationInfo.FLAG_PERSISTENT),

    /** See [ApplicationInfo.FLAG_FACTORY_TEST]. */
    FLAG_FACTORY_TEST(ApplicationInfo.FLAG_FACTORY_TEST),

    /** See [ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING]. */
    FLAG_ALLOW_TASK_REPARENTING(ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING),

    /** See [ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA]. */
    FLAG_ALLOW_CLEAR_USER_DATA(ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA),

    /** See [ApplicationInfo.FLAG_UPDATED_SYSTEM_APP]. */
    FLAG_UPDATED_SYSTEM_APP(ApplicationInfo.FLAG_UPDATED_SYSTEM_APP),

    /** See [ApplicationInfo.FLAG_TEST_ONLY]. */
    FLAG_TEST_ONLY(ApplicationInfo.FLAG_TEST_ONLY),

    /** See [ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS]. */
    FLAG_SUPPORTS_SMALL_SCREENS(ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS),

    /** See [ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS]. */
    FLAG_SUPPORTS_NORMAL_SCREENS(ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS),

    /** See [ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS]. */
    FLAG_SUPPORTS_LARGE_SCREENS(ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS),

    /** See [ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS]. */
    FLAG_SUPPORTS_XLARGE_SCREENS(ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS),

    /** See [ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS]. */
    FLAG_RESIZEABLE_FOR_SCREENS(ApplicationInfo.FLAG_RESIZEABLE_FOR_SCREENS),

    /** See [ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES]. */
    @Suppress("DEPRECATION")
    FLAG_SUPPORTS_SCREEN_DENSITIES(ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES),

    /** See [ApplicationInfo.FLAG_VM_SAFE_MODE]. */
    FLAG_VM_SAFE_MODE(ApplicationInfo.FLAG_VM_SAFE_MODE),

    /** See [ApplicationInfo.FLAG_ALLOW_BACKUP]. */
    FLAG_ALLOW_BACKUP(ApplicationInfo.FLAG_ALLOW_BACKUP),

    /** See [ApplicationInfo.FLAG_KILL_AFTER_RESTORE]. */
    FLAG_KILL_AFTER_RESTORE(ApplicationInfo.FLAG_KILL_AFTER_RESTORE),

    /** See [ApplicationInfo.FLAG_RESTORE_ANY_VERSION]. */
    FLAG_RESTORE_ANY_VERSION(ApplicationInfo.FLAG_RESTORE_ANY_VERSION),

    /** See [ApplicationInfo.FLAG_EXTERNAL_STORAGE]. */
    FLAG_EXTERNAL_STORAGE(ApplicationInfo.FLAG_EXTERNAL_STORAGE),

    /** See [ApplicationInfo.FLAG_LARGE_HEAP]. */
    FLAG_LARGE_HEAP(ApplicationInfo.FLAG_LARGE_HEAP),

    /** See [ApplicationInfo.FLAG_STOPPED]. */
    FLAG_STOPPED(ApplicationInfo.FLAG_STOPPED),

    /** See [ApplicationInfo.FLAG_SUPPORTS_RTL]. */
    FLAG_SUPPORTS_RTL(ApplicationInfo.FLAG_SUPPORTS_RTL),

    /** See [ApplicationInfo.FLAG_INSTALLED]. */
    FLAG_INSTALLED(ApplicationInfo.FLAG_INSTALLED),

    /** See [ApplicationInfo.FLAG_IS_DATA_ONLY]. */
    FLAG_IS_DATA_ONLY(ApplicationInfo.FLAG_IS_DATA_ONLY),

    /** See [ApplicationInfo.FLAG_IS_GAME]. */
    @Suppress("DEPRECATION")
    FLAG_IS_GAME(ApplicationInfo.FLAG_IS_GAME),

    /** See [ApplicationInfo.FLAG_FULL_BACKUP_ONLY]. */
    FLAG_FULL_BACKUP_ONLY(ApplicationInfo.FLAG_FULL_BACKUP_ONLY)
}
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
 * This file is created by fankes on 2023/11/24.
 */
@file:JvmName("SystemInsetsUtils")

package com.highcapable.betterandroid.ui.component.systembar.factory

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.insets.SystemBarsInsets
import com.highcapable.betterandroid.ui.component.systembar.type.InsetsType

/**
 * Apply the system bars insets and cutout padding in layout.
 *
 * See also [View.appendSystemBarsInsets].
 * @param systemBarsInsets the system bars insets, you can use [SystemBarsController.systemBarsInsets]
 * or use [SystemBarsController.addOnInsetsChangeListener].
 * @param left whether to apply the left insets.
 * @param top whether to apply the top insets.
 * @param right whether to apply the right insets.
 * @param bottom whether to apply the bottom insets.
 * @param type the insets type, default is [InsetsType.ADAPTIVE].
 * @return [Insets]
 */
@JvmOverloads
fun View.applySystemBarsInsets(
    systemBarsInsets: SystemBarsInsets,
    left: Boolean = true,
    top: Boolean = true,
    right: Boolean = true,
    bottom: Boolean = true,
    type: InsetsType = InsetsType.ADAPTIVE
) = systemBarsInsets.toInsetsPadding(type).also {
    setPadding(
        if (left) it.left else 0,
        if (top) it.top else 0,
        if (right) it.right else 0,
        if (bottom) it.bottom else 0
    )
}

/**
 * Append the system bars insets and cutout padding in layout.
 *
 * See also [View.applySystemBarsInsets].
 * @param systemBarsInsets the system bars insets, you can use [SystemBarsController.systemBarsInsets]
 * or use [SystemBarsController.addOnInsetsChangeListener].
 * @param left whether to apply the left insets.
 * @param top whether to apply the top insets.
 * @param right whether to apply the right insets.
 * @param bottom whether to apply the bottom insets.
 * @param type the insets type, default is [InsetsType.ADAPTIVE].
 * @return [Insets]
 */
@JvmOverloads
fun View.appendSystemBarsInsets(
    systemBarsInsets: SystemBarsInsets,
    left: Boolean = true,
    top: Boolean = true,
    right: Boolean = true,
    bottom: Boolean = true,
    type: InsetsType = InsetsType.ADAPTIVE
) = systemBarsInsets.toInsetsPadding(type).also {
    if (left) updatePadding(left = it.left)
    if (top) updatePadding(top = it.top)
    if (right) updatePadding(right = it.right)
    if (bottom) updatePadding(bottom = it.bottom)
}

/**
 * Apply the system insets and cutout padding in layout.
 *
 * - This function is deprecated, use [View.applySystemBarsInsets] instead.
 * @return [Insets]
 */
@Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
@Deprecated(message = "Use View.applySystemBarsInsets instead.")
@JvmOverloads
fun View.applySystemInsets(
    systemInsets: SystemBarsInsets,
    vararg types: com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType,
    ignoredCutout: Boolean = false
) = applySystemBarsInsets(
    systemBarsInsets = systemInsets,
    left = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.LEFT),
    top = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.TOP),
    right = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.RIGHT),
    bottom = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.BOTTOM),
    type = if (ignoredCutout) InsetsType.STABLE else InsetsType.ADAPTIVE
)

/**
 * Append the system insets and cutout padding in layout.
 *
 * - This function is deprecated, use [View.appendSystemBarsInsets] instead.
 * @return [Insets]
 */
@Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
@Deprecated(message = "Use View.appendSystemBarsInsets instead.")
@JvmOverloads
fun View.appendSystemInsets(
    systemInsets: SystemBarsInsets,
    vararg types: com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType,
    ignoredCutout: Boolean = false
) = appendSystemBarsInsets(
    systemBarsInsets = systemInsets,
    left = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.LEFT),
    top = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.TOP),
    right = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.RIGHT),
    bottom = types.contains(com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType.BOTTOM),
    type = if (ignoredCutout) InsetsType.STABLE else InsetsType.ADAPTIVE
)

/**
 * Remove the system insets and cutout padding in layout.
 *
 * - This function is deprecated and no effect, use [View.applySystemBarsInsets] or [View.appendSystemBarsInsets] instead.
 * @return [Insets]
 */
@Suppress("DEPRECATION", "UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith", "UnusedReceiverParameter")
@Deprecated(message = "Use View.applySystemBarsInsets or View.appendSystemBarsInsets instead.")
@JvmOverloads
fun View.removeSystemInsets(
    systemInsets: SystemBarsInsets,
    vararg types: com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType,
    ignoredCutout: Boolean = false
) = Insets.NONE
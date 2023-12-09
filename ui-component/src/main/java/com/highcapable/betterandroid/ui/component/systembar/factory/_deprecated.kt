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
@file:Suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith", "UNUSED_PARAMETER", "UnusedReceiverParameter")
@file:JvmName("SystemInsetsUtils")

package com.highcapable.betterandroid.ui.component.systembar.factory

import android.view.View
import androidx.core.graphics.Insets
import com.highcapable.betterandroid.ui.component.systembar.insets.SystemBarsInsets
import com.highcapable.betterandroid.ui.component.systembar.type.InsetsType
import com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType

/**
 * Apply the system insets and cutout padding in layout.
 *
 * - This function is deprecated, use [View.applySystemBarsInsets] instead.
 * @return [Insets]
 */
@Deprecated(message = "Use View.applySystemBarsInsets instead.")
@JvmOverloads
fun View.applySystemInsets(
    systemInsets: SystemBarsInsets,
    vararg types: SystemInsetsType,
    ignoredCutout: Boolean = false
) = applySystemBarsInsets(
    systemBarsInsets = systemInsets,
    left = types.contains(SystemInsetsType.LEFT),
    top = types.contains(SystemInsetsType.TOP),
    right = types.contains(SystemInsetsType.RIGHT),
    bottom = types.contains(SystemInsetsType.BOTTOM),
    type = if (ignoredCutout) InsetsType.STABLE else InsetsType.ADAPTIVE
)

/**
 * Append the system insets and cutout padding in layout.
 *
 * - This function is deprecated, use [View.appendSystemBarsInsets] instead.
 * @return [Insets]
 */
@Deprecated(message = "Use View.appendSystemBarsInsets instead.")
@JvmOverloads
fun View.appendSystemInsets(
    systemInsets: SystemBarsInsets,
    vararg types: SystemInsetsType,
    ignoredCutout: Boolean = false
) = appendSystemBarsInsets(
    systemBarsInsets = systemInsets,
    left = types.contains(SystemInsetsType.LEFT),
    top = types.contains(SystemInsetsType.TOP),
    right = types.contains(SystemInsetsType.RIGHT),
    bottom = types.contains(SystemInsetsType.BOTTOM),
    type = if (ignoredCutout) InsetsType.STABLE else InsetsType.ADAPTIVE
)

/**
 * Remove the system insets and cutout padding in layout.
 *
 * - This function is deprecated and no effect, use [View.applySystemBarsInsets] or [View.appendSystemBarsInsets] instead.
 * @return [Insets]
 */
@Deprecated(message = "Use View.applySystemBarsInsets or View.appendSystemBarsInsets instead.")
@JvmOverloads
fun View.removeSystemInsets(
    systemInsets: SystemBarsInsets,
    vararg types: SystemInsetsType,
    ignoredCutout: Boolean = false
) = Insets.NONE
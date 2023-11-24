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
import androidx.core.view.updatePadding
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.type.SystemInsetsType

/**
 * Apply the system insets and cutout paddings in layout.
 *
 * See also [View.appendSystemInsets], [View.removeSystemInsets].
 * @param systemInsets the system insets instance, you can use [SystemBarsController.systemInsets]
 * or use [SystemBarsController.addOnInsetsChangeListener].
 * @param types the system insets types, default are [SystemInsetsType.LEFT], [SystemInsetsType.TOP],
 * [SystemInsetsType.RIGHT], [SystemInsetsType.BOTTOM],
 * when you set this, the [SystemInsetsType] decided this layout's paddings (top, left, right, bottom).
 * @param ignoredCutout whether to ignore the notch size,
 * if not ignored, it will stop handing on the notch size, default false.
 * @return [SystemBarsController.SystemInsets.Paddings]
 */
@JvmOverloads
fun View.applySystemInsets(
    systemInsets: SystemBarsController.SystemInsets,
    vararg types: SystemInsetsType = arrayOf(
        SystemInsetsType.LEFT,
        SystemInsetsType.TOP,
        SystemInsetsType.RIGHT,
        SystemInsetsType.BOTTOM
    ),
    ignoredCutout: Boolean = false
): SystemBarsController.SystemInsets.Paddings {
    val paddings = SystemBarsController.SystemInsets.Paddings.from(systemInsets, ignoredCutout)
    setPadding(
        if (types.contains(SystemInsetsType.LEFT)) paddings.left else 0,
        if (types.contains(SystemInsetsType.TOP)) paddings.top else 0,
        if (types.contains(SystemInsetsType.RIGHT)) paddings.right else 0,
        if (types.contains(SystemInsetsType.BOTTOM)) paddings.bottom else 0
    ); return paddings
}

/**
 * Append the system insets and cutout paddings in layout.
 *
 * See also [View.applySystemInsets], [View.removeSystemInsets].
 * @param systemInsets the system insets instance, you can use [SystemBarsController.systemInsets]
 * or use [SystemBarsController.addOnInsetsChangeListener].
 * @param types the system insets types,
 * when you set this, the [SystemInsetsType] decided this layout's paddings (top, left, right, bottom).
 * @param ignoredCutout whether to ignore the notch size,
 * if not ignored, it will stop handing on the notch size, default false.
 * @return [SystemBarsController.SystemInsets.Paddings]
 */
@JvmOverloads
fun View.appendSystemInsets(
    systemInsets: SystemBarsController.SystemInsets,
    vararg types: SystemInsetsType,
    ignoredCutout: Boolean = false
): SystemBarsController.SystemInsets.Paddings {
    val paddings = SystemBarsController.SystemInsets.Paddings.from(systemInsets, ignoredCutout)
    if (types.contains(SystemInsetsType.LEFT)) updatePadding(left = paddings.left)
    if (types.contains(SystemInsetsType.TOP)) updatePadding(top = paddings.top)
    if (types.contains(SystemInsetsType.RIGHT)) updatePadding(right = paddings.right)
    if (types.contains(SystemInsetsType.BOTTOM)) updatePadding(bottom = paddings.bottom)
    return paddings
}

/**
 * Remove the system insets and cutout paddings in layout.
 *
 * See also [View.applySystemInsets], [View.appendSystemInsets].
 * @param systemInsets the system insets instance, you can use [SystemBarsController.systemInsets]
 * or use [SystemBarsController.addOnInsetsChangeListener].
 * @param types the system insets types, default are [SystemInsetsType.LEFT], [SystemInsetsType.TOP],
 * [SystemInsetsType.RIGHT], [SystemInsetsType.BOTTOM],
 * when you set this, the [SystemInsetsType] decided this layout's paddings (top, left, right, bottom).
 * @param ignoredCutout whether to ignore the notch size,
 * if not ignored, it will stop handing on the notch size, default false.
 * @return [SystemBarsController.SystemInsets.Paddings]
 */
@JvmOverloads
fun View.removeSystemInsets(
    systemInsets: SystemBarsController.SystemInsets,
    vararg types: SystemInsetsType = arrayOf(
        SystemInsetsType.LEFT,
        SystemInsetsType.TOP,
        SystemInsetsType.RIGHT,
        SystemInsetsType.BOTTOM
    ),
    ignoredCutout: Boolean = false
): SystemBarsController.SystemInsets.Paddings {
    val paddings = SystemBarsController.SystemInsets.Paddings.from(systemInsets, ignoredCutout, isOnlyCutout = true)
    if (types.contains(SystemInsetsType.LEFT)) updatePadding(left = paddings.left)
    if (types.contains(SystemInsetsType.TOP)) updatePadding(top = paddings.top)
    if (types.contains(SystemInsetsType.RIGHT)) updatePadding(right = paddings.right)
    if (types.contains(SystemInsetsType.BOTTOM)) updatePadding(bottom = paddings.bottom)
    return paddings
}
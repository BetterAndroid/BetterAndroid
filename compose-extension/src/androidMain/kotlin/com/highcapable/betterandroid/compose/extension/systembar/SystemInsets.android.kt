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
 * This file is created by fankes on 2023/12/4.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.systembar

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.component.systembar.insets.SystemInsets

/**
 * Resolve the system insets from [SystemBarsController].
 * @receiver the system bars controller.
 * @return [PlatformSystemInsets]
 */
@Composable
internal fun SystemBarsController.resolvePlatformSystemInsets(): PlatformSystemInsets {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val activity = LocalContext.current as? Activity? ?: return PlatformSystemInsets.Default
    var systemInsets by remember { mutableStateOf<SystemInsets?>(null) }
    addOnInsetsChangeListener { systemInsets = it }
    // We need recompostion to get the latest insets.
    return systemInsets?.toPlatformExpect(density, layoutDirection)
        ?: this.systemInsets?.toPlatformExpect(density, layoutDirection)
        ?: activity.createAbsolutePlatformSystemInsets(density)
}

/**
 * Convert [SystemInsets] to [PlatformSystemInsets].
 * @param density the density.
 * @param layoutDirection the layout direction.
 * @return [PlatformSystemInsets]
 */
private fun SystemInsets.toPlatformExpect(density: Density, layoutDirection: LayoutDirection) =
    with(density) {
        PlatformSystemInsets(
            layoutDirection = layoutDirection,
            stableLeft = stable.left.toDp(),
            stableTop = stable.top.toDp(),
            stableRight = stable.right.toDp(),
            stableBottom = stable.bottom.toDp(),
            cutoutLeft = cutout.safeInsetLeft.toDp(),
            cutoutTop = cutout.safeInsetTop.toDp(),
            cutoutRight = cutout.safeInsetRight.toDp(),
            cutoutBottom = cutout.safeInsetBottom.toDp()
        )
    }

/**
 * Create a [PlatformSystemInsets] from [SystemBarsController.AbsoluteController].
 * @receiver the current context.
 * @param density the density.
 * @return [PlatformSystemInsets]
 */
private fun Context.createAbsolutePlatformSystemInsets(density: Density) =
    with(SystemBarsController.createAbsolute(context = this)) {
        with(density) {
            PlatformSystemInsets(
                stableTop = statusBarHeight.toDp(),
                stableBottom = navigationBarHeight.toDp()
            )
        }
    }
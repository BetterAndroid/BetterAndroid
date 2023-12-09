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

package com.highcapable.betterandroid.compose.extension.platform.systembar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.SystemBarsController
import com.highcapable.betterandroid.compose.extension.platform.component.systembar.insets.SystemBarsInsets

/**
 * Resolve the system bars insets from [SystemBarsController].
 * @receiver the system bars controller.
 * @return [PlatformSystemBarsInsets]
 */
@Composable
internal fun SystemBarsController.resolvePlatformSystemBarsInsets(): PlatformSystemBarsInsets {
    val layoutDirection = LocalLayoutDirection.current
    var systemBarsInsets by remember { mutableStateOf(systemBarsInsets) }
    addOnInsetsChangeListener { systemBarsInsets = it }
    // We need recompostion to get the latest insets.
    return systemBarsInsets?.toPlatformExpect(layoutDirection) ?: PlatformSystemBarsInsets.Default
}

/**
 * Convert [SystemBarsInsets] to [PlatformSystemBarsInsets].
 * @param layoutDirection the layout direction.
 * @return [PlatformSystemBarsInsets]
 */
private fun SystemBarsInsets.toPlatformExpect(layoutDirection: LayoutDirection) =
    PlatformSystemBarsInsets(
        layoutDirection = layoutDirection,
        stableLeft = safeArea.left.toFloat().dp,
        stableTop = safeArea.top.toFloat().dp,
        stableRight = safeArea.right.toFloat().dp,
        stableBottom = safeArea.bottom.toFloat().dp,
        cutoutLeft = safeArea.left.toFloat().dp,
        cutoutTop = safeArea.top.toFloat().dp,
        cutoutRight = safeArea.right.toFloat().dp,
        cutoutBottom = safeArea.bottom.toFloat().dp
    )
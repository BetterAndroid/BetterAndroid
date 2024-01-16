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
 * This file is created by fankes on 2023/11/9.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Dialog as ComposeDialog

/**
 * A wrapper to resolve [DialogProperties].
 *
 * - Note: The [scrimColor] only the alpha can support on Android platform's dimAmount.
 * @see DialogProperties
 */
@Immutable
data class DialogPropertiesWrapper(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val usePlatformDefaultWidth: Boolean = true,
    val usePlatformInsets: Boolean = true,
    val scrimColor: Color = DefaultScrimColor,
    val androidProperties: AndroidProperties = AndroidProperties()
) {

    /**
     * Android platform's properties.
     *
     * - Note: This properties only available on Android platform.
     * @see DialogProperties
     */
    @Immutable
    data class AndroidProperties(
        val securePolicy: SecureFlagPolicyWrapper = SecureFlagPolicyWrapper.Inherit,
        val decorFitsSystemWindows: Boolean = true
    )
}

/**
 * A wrapper to resolve SecureFlagPolicy.
 * @see androidx.compose.ui.window.SecureFlagPolicy
 */
@Stable
enum class SecureFlagPolicyWrapper {
    /** @see androidx.compose.ui.window.SecureFlagPolicy.Inherit */
    Inherit,

    /** @see androidx.compose.ui.window.SecureFlagPolicy.SecureOn */
    SecureOn,

    /** @see androidx.compose.ui.window.SecureFlagPolicy.SecureOff */
    SecureOff
}

/**
 * Extension for [ComposeDialog] to support different parameters of Android.
 *
 * Use [DialogPropertiesWrapper] instead [DialogProperties].
 * @see ComposeDialog
 */
@Composable
expect fun Dialog(
    onDismissRequest: () -> Unit,
    properties: DialogPropertiesWrapper = DialogPropertiesWrapper(),
    content: @Composable () -> Unit
)

/** The default scrim opacity. */
private const val DefaultScrimOpacity = 0.6f

/** The default scrim color. */
private val DefaultScrimColor = Color.Black.copy(alpha = DefaultScrimOpacity)
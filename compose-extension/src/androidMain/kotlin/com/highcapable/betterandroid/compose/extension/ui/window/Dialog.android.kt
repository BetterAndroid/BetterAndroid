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
 * This file is created by fankes on 2023/12/4.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.compose.ui.window.Dialog as ComposeDialog

/**
 * Extension for [ComposeDialog] to support different parameters of Android.
 *
 * Use [DialogPropertiesWrapper] instead [DialogProperties].
 * @see ComposeDialog
 */
@Composable
actual fun Dialog(
    onDismissRequest: () -> Unit,
    properties: DialogPropertiesWrapper,
    content: @Composable () -> Unit
) {
    ComposeDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            securePolicy = properties.androidProperties.securePolicy.toPlatformActual(),
            usePlatformDefaultWidth = properties.usePlatformDefaultWidth,
            decorFitsSystemWindows = properties.androidProperties.decorFitsSystemWindows
        )
    ) {
        var windowProvider by remember { mutableStateOf<DialogWindowProvider?>(null) }
        if (windowProvider == null) windowProvider = LocalView.current.parent as? DialogWindowProvider?
        windowProvider?.window?.setDimAmount(properties.scrimColor.alpha)
        content()
    }
}

/**
 * Convert [SecureFlagPolicyWrapper] to [SecureFlagPolicy].
 * @receiver [SecureFlagPolicyWrapper]
 * @return [SecureFlagPolicy]
 */
private fun SecureFlagPolicyWrapper.toPlatformActual() = when (this) {
    SecureFlagPolicyWrapper.Inherit -> SecureFlagPolicy.Inherit
    SecureFlagPolicyWrapper.SecureOn -> SecureFlagPolicy.SecureOn
    SecureFlagPolicyWrapper.SecureOff -> SecureFlagPolicy.SecureOff
}
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

package com.highcapable.betterandroid.compose.extension.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
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
            usePlatformDefaultWidth = properties.usePlatformDefaultWidth,
            usePlatformInsets = properties.usePlatformInsets,
            scrimColor = properties.scrimColor
        ),
        content = content
    )
}
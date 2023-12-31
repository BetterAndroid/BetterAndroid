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
 * This file is created by fankes on 2023/11/16.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import android.view.KeyEvent as AndroidKeyEvent
import androidx.compose.ui.window.Popup as ComposePopup

/**
 * Extension for [ComposePopup] to separate [onPreviewKeyEvent], [onKeyEvent] on Android.
 * @see ComposePopup
 */
@Composable
actual fun Popup(
    alignment: Alignment,
    offset: IntOffset,
    popupPositionProvider: PopupPositionProvider?,
    onDismissRequest: (() -> Unit)?,
    properties: PopupProperties,
    onPreviewKeyEvent: ((KeyEvent) -> Boolean)?,
    onKeyEvent: ((KeyEvent) -> Boolean)?,
    content: @Composable () -> Unit
) {
    popupPositionProvider?.also {
        ComposePopup(
            popupPositionProvider = it,
            onDismissRequest = onDismissRequest,
            properties = properties,
            content = content
        )
    } ?: ComposePopup(
        alignment = alignment,
        offset = offset,
        onDismissRequest = onDismissRequest,
        properties = properties,
        content = content
    )
    // Emulate onPreviewKeyEvent, onKeyEvent on Android.
    if (onPreviewKeyEvent != null || onKeyEvent != null)
        LocalView.current.setOnKeyListener { _, _, event ->
            onPreviewKeyEvent?.invoke(event.toPlatformExpect())
                ?: onKeyEvent?.invoke(event.toPlatformExpect()) ?: false
        }
}

/**
 * Convert [AndroidKeyEvent] to [KeyEvent].
 * @return [KeyEvent]
 */
private fun AndroidKeyEvent.toPlatformExpect() = KeyEvent(nativeKeyEvent = this)
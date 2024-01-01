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
 * This file is created by fankes on 2023/12/6.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.multiplatform.backpress

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.highcapable.betterandroid.ui.component.activity.AppComponentActivity
import com.highcapable.betterandroid.ui.component.backpress.BackPressedController
import com.highcapable.betterandroid.ui.component.backpress.callback.OnBackPressedCallback
import com.highcapable.betterandroid.ui.component.proxy.IBackPressedController

/**
 * An effect for handling presses of the system back button.
 *
 * Only support Android platform.
 *
 * There is a system global back press event in Android,
 * by listening to this event, we can know that the user has performed a back operation,
 * but there is no such event in other systems. For example, in iOS,
 * "back" is passed through the navigation controller (UINavigationController)
 * stack operation or the dismiss operation of the modal view controller.
 *
 * These operations are managed by specific view controllers or navigation controllers,
 * rather than by the system globally.
 *
 * It is even more impossible to have a "back press" on a desktop platform.
 *
 * Platform requirements:
 *
 * > Android
 *
 * You can use [AppComponentActivity] or implement [IBackPressedController] of your Activity for better,
 * but you must use an [ComponentActivity] for basic.
 *
 * Requires library: `ui-component`, visit [here](https://github.com/BetterAndroid/BetterAndroid).
 *
 * > Others
 *
 * No-op.
 * @param enabled if this BackHandler should be enabled, default true.
 * @param onBack the action invoked by pressing the system back.
 */
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    val backPressed = rememberBackPressedController()
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember { OnBackPressedCallback { currentOnBack() } }
    // On every successful composition, update the callback with the `enabled` value.
    SideEffect { backCallback.isEnabled = enabled }
    // Destroy the callback when the effect leaves the composition.
    DisposableEffect(backPressed) {
        backPressed.addCallback(backCallback)
        onDispose { backPressed.removeCallback(backCallback) }
    }
}

/**
 * Creates and remember a [BackPressedController].
 * @return [BackPressedController]
 */
@Composable
private fun rememberBackPressedController(): BackPressedController {
    val activity = LocalContext.current as? ComponentActivity? ?: error("No ComponentActivity provided of composables.")
    var backPressed by remember { mutableStateOf<BackPressedController?>(null) }
    if (backPressed == null) backPressed = activity.resolveBackPressedController()
    return backPressed ?: error("No BackPressedController provided of composables.")
}

/**
 * Resolve the [BackPressedController] from [ComponentActivity].
 * @receiver the current activity.
 * @return [BackPressedController]
 */
private fun ComponentActivity.resolveBackPressedController() =
    (this as? IBackPressedController?)?.backPressed ?: BackPressedController.from(activity = this)
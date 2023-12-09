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
 * This file is created by fankes on 2023/12/6.
 */
@file:Suppress("unused", "ComposableNaming")

package com.highcapable.betterandroid.compose.extension.platform.backpress

import androidx.compose.runtime.Composable

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
 * You can use **AppComponentActivity** or implement **IBackPressedController** of your Activity for better,
 * but you must use an **ComponentActivity** for basic.
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
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    _BackHandler(enabled, onBack)
}

/**
 * An effect for handling presses of the system back button.
 *
 * Only support Android platform.
 * @param enabled if this BackHandler should be enabled.
 * @param onBack the action invoked by pressing the system back.
 */
@Composable
internal expect fun _BackHandler(enabled: Boolean, onBack: () -> Unit)
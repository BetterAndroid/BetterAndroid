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
 * Supports Android and iOS.
 * @param enabled if this BackHandler should be enabled, default true.
 * @param onBack the action invoked by pressing the system back.
 */
@Composable
internal actual fun _BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // TODO: Android platform back handler.
}
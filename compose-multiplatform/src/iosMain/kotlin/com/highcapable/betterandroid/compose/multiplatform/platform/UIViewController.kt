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
 * This file is created by fankes on 2023/12/9.
 */
@file:Suppress("unused", "FunctionName")

package com.highcapable.betterandroid.compose.multiplatform.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.highcapable.betterandroid.compose.multiplatform.platform.systembar.SystemBarsController
import com.highcapable.betterandroid.compose.multiplatform.platform.uiviewcontroller.AppComponentUIViewController

/**
 * Create a new [AppComponentUIViewController] for composables.
 *
 * - Note: Don't use an **UIViewControllerRepresentable** to create an **UIViewController** for Swift UI,
 *   that will cause the [SystemBarsController] related functions to become invalid,
 *   because Swift UI will take over the state of the entire view, at this time,
 *   you can only control system bars in Swift UI.
 *
 * Please visit the [compose-multiplatform](https://betterandroid.github.io/BetterAndroid/en/library/compose-multiplatform#initial-configuration)
 * documentation for usage.
 * @param content the content.
 * @return [AppComponentUIViewController]
 */
fun AppComponentUIViewController(content: @Composable () -> Unit) = AppComponentUIViewController.from(ComposeUIViewController(content))
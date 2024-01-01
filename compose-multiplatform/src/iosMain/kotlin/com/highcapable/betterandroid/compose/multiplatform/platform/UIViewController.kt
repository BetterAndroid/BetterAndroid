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
import com.highcapable.betterandroid.compose.multiplatform.platform.uiviewcontroller.AppComponentUIViewController

/**
 * Create a new [AppComponentUIViewController] for composables.
 *
 * - Note: Don't use an UIViewControllerRepresentable to create an UIViewController for Swift UI,
 *   that will cause the system bars controller related functions to become invalid,
 *   because Swift UI will take over the state of the entire view, at this time,
 *   you can only control system bars in Swift UI.
 *
 * You can use the AppDelegate to create a standard project that you want to create an **Compose UI Only** project.
 *
 * Usage (In Kotlin file "App.ios.kt"):
 *
 * ```kotlin
 * fun createUIViewController() = AppComponentUIViewController {
 *     // Your composable content here.
 * }
 * ```
 *
 * Usage (In the iOS App with Swift file "AppDelegate.swift"):
 *
 * ```swift
 * import UIKit
 * import shared // Here is your shared module name.
 *
 * @UIApplicationMain
 * class AppDelegate: UIResponder, UIApplicationDelegate {
 *
 *     var window: UIWindow?
 *
 *     func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions:
 *         [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
 *         // Create a new UI window.
 *         window = UIWindow(frame: UIScreen.main.bounds)
 *         // Set the root view controller.
 *         window?.rootViewController = App_iosKt.createUIViewController()
 *         // Make the window visible.
 *         window?.makeKeyAndVisible()
 *         return true
 *     }
 * }
 * ```
 * @param content the content.
 * @return [AppComponentUIViewController]
 */
fun AppComponentUIViewController(content: @Composable () -> Unit) = AppComponentUIViewController.from(ComposeUIViewController(content))
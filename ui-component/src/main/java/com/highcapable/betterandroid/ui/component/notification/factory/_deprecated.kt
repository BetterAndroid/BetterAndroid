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
 * This file is created by fankes on 2023/12/15.
 */
@file:Suppress("unused", "DEPRECATION", "DeprecatedCallableAddReplaceWith")

package com.highcapable.betterandroid.ui.component.notification.factory

import android.content.Context
import com.highcapable.betterandroid.ui.component.notification.NotificationCreator

/**
 * Create a notification.
 *
 * - This function is deprecated and no effect, use [Context.createNotification] instead.
 */
@Deprecated(message = "Use createNotification instead.")
inline fun Context.createNotification(initiate: NotificationCreator.() -> Unit) = NotificationCreator.from(this).apply(initiate).build()
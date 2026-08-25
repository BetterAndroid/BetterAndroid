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
 * This file is created by fankes on 2026/8/25.
 */
@file:Suppress("unused", "FunctionName")
@file:JvmName("NotificationActionUtils")

package com.highcapable.betterandroid.ui.component.notification.factory

import android.app.PendingIntent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat

/**
 * Create a notification action.
 * @param icon the drawable resource ID that represents the action.
 * @param title the title of the action.
 * @param intent the intent to fire when users trigger the action.
 * @param builder the [NotificationCompat.Action.Builder] builder body.
 * @return [NotificationCompat.Action]
 */
inline fun NotificationAction(
    @DrawableRes icon: Int,
    title: CharSequence,
    intent: PendingIntent,
    builder: NotificationCompat.Action.Builder.() -> Unit = {}
) = NotificationCompat.Action.Builder(icon, title, intent).apply(builder).build()

/**
 * Create a notification action.
 * @param icon the icon that represents the action.
 * @param title the title of the action.
 * @param intent the intent to fire when users trigger the action.
 * @param builder the [NotificationCompat.Action.Builder] builder body.
 * @return [NotificationCompat.Action]
 */
inline fun NotificationAction(
    icon: IconCompat,
    title: CharSequence,
    intent: PendingIntent,
    builder: NotificationCompat.Action.Builder.() -> Unit = {}
) = NotificationCompat.Action.Builder(icon, title, intent).apply(builder).build()

/**
 * Create a notification action from an existing action.
 * @param action the existing action.
 * @param builder the [NotificationCompat.Action.Builder] builder body.
 * @return [NotificationCompat.Action]
 */
inline fun NotificationAction(
    action: NotificationCompat.Action,
    builder: NotificationCompat.Action.Builder.() -> Unit = {}
) = NotificationCompat.Action.Builder(action).apply(builder).build()

/**
 * Create a notification remote input.
 * @param resultKey the key that refers to this input when collected from the user.
 * @param builder the [RemoteInput.Builder] builder body.
 * @return [RemoteInput]
 */
inline fun NotificationRemoteInput(
    resultKey: String,
    builder: RemoteInput.Builder.() -> Unit
) = RemoteInput.Builder(resultKey).apply(builder).build()

/**
 * Add a notification remote input to an action builder.
 * @see NotificationCompat.Action.Builder.addRemoteInput
 * @param resultKey the key that refers to this input when collected from the user.
 * @param builder the [RemoteInput.Builder] builder body.
 */
inline fun NotificationCompat.Action.Builder.addRemoteInput(
    resultKey: String,
    builder: RemoteInput.Builder.() -> Unit
) = addRemoteInput(NotificationRemoteInput(resultKey, builder))
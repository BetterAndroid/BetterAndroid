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
 * This file is created by fankes on 2023/10/28.
 */
@file:Suppress("unused", "FunctionName")
@file:JvmName("NotificationUtils")

package com.highcapable.betterandroid.ui.component.notification.factory

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.highcapable.betterandroid.ui.component.notification.NotificationBuilder
import com.highcapable.betterandroid.ui.component.notification.NotificationChannelBuilder
import com.highcapable.betterandroid.ui.component.notification.NotificationChannelGroupBuilder
import com.highcapable.betterandroid.ui.component.notification.NotificationPoster
import com.highcapable.betterandroid.ui.component.notification.type.NotificationImportance
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelGroupWrapper
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelWrapper
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationWrapper

/**
 * Get notification manager.
 * @receiver the current context.
 * @return [NotificationManagerCompat]
 */
val Context.notificationManager get() = NotificationManagerCompat.from(this)

/**
 * Create the [NotificationPoster] from [NotificationWrapper].
 * @receiver [NotificationWrapper]
 * @return [NotificationPoster]
 */
fun NotificationWrapper.asPoster() = NotificationPoster(notification = this)

/**
 * Create a notification to post.
 *
 * Usage:
 *
 * ```kotlin
 * context.createNotification(
 *     channel = NotificationChannel("my_channel_id") {
 *         name = "My Channel"
 *         description = "My channel description."
 *     }
 * ) {
 *     contentTitle = "My Notification"
 *     contentText = "Hello World!"
 * }.post() // Post the notification.
 * ```
 * @receiver the current context.
 * @param channel the notification channel.
 * @param initiate the [NotificationBuilder] builder body.
 * @return [NotificationPoster]
 */
inline fun Context.createNotification(channel: NotificationChannelWrapper, initiate: NotificationBuilder.() -> Unit) =
    Notification(context = this, channel, initiate).asPoster()

/**
 * Create a notification.
 * @param context the current context.
 * @param initiate the [NotificationBuilder] builder body.
 * @return [NotificationWrapper]
 */
inline fun Notification(context: Context, channel: NotificationChannelWrapper, initiate: NotificationBuilder.() -> Unit) =
    NotificationBuilder.from(context, channel).apply(initiate).build()

/**
 * Create a notification channel.
 * @param channelId the channel ID.
 * @param group the channel group, default is null.
 * @param importance the notification importance, default is [NotificationImportance.DEFAULT].
 * @param initiate the [NotificationChannelBuilder] builder body.
 * @return [NotificationChannelWrapper]
 */
inline fun NotificationChannel(
    channelId: String,
    group: NotificationChannelGroupWrapper? = null,
    importance: NotificationImportance = NotificationImportance.DEFAULT,
    initiate: NotificationChannelBuilder.() -> Unit
) = NotificationChannelBuilder.from(channelId, group, importance).apply(initiate).build()

/**
 * Create a notification channel group.
 * @param groupId the channel group ID.
 * @param initiate the [NotificationChannelGroupBuilder] builder body.
 * @return [NotificationChannelGroupWrapper]
 */
inline fun NotificationChannelGroup(groupId: String, initiate: NotificationChannelGroupBuilder.() -> Unit) =
    NotificationChannelGroupBuilder.from(groupId).apply(initiate).build()
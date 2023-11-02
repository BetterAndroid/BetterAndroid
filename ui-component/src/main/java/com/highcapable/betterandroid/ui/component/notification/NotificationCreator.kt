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
 * This file is created by fankes on 2023/10/28.
 */
@file:Suppress("unused", "InlinedApi", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.highcapable.betterandroid.ui.component.notification

import android.Manifest
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.notification.builder.NotificationBuilder
import com.highcapable.betterandroid.ui.component.notification.builder.NotificationChannelBuilder
import com.highcapable.betterandroid.ui.component.notification.type.NotificationImportance
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelWrapper
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationWrapper

/**
 * Notification creator.
 * @param context the current context.
 */
class NotificationCreator private constructor(private val context: Context) {

    companion object {

        /**
         * Create a new [NotificationCreator] from [context].
         * @param context the current context.
         * @return [NotificationCreator]
         */
        @JvmStatic
        fun from(context: Context) = NotificationCreator(context)
    }

    /** The current notification channel. */
    private var channel: NotificationChannelWrapper? = null

    /** The current notification. */
    private var notification: NotificationWrapper? = null

    /**
     * Set a notification channel.
     *
     * - Note: You don't need to consider compatibility with system version below Android 8,
     *         we have made it compatible for you, this feature will be automatically converted
     *         to a legacy solution below Android 8.
     * @param channel the [NotificationChannelWrapper].
     * @return [NotificationCreator]
     */
    fun channel(channel: NotificationChannelWrapper) = apply { this.channel = channel }

    /**
     * Set a notification channel.
     *
     * - Note: You don't need to consider compatibility with system version below Android 8,
     *         we have made it compatible for you, this feature will be automatically converted
     *         to a legacy solution below Android 8.
     * @param channelId the channel ID.
     * @param importance the notification importance, default is [NotificationImportance.DEFAULT].
     * @param initiate the [NotificationChannelBuilder] builder body.
     * @return [NotificationCreator]
     */
    inline fun channel(
        channelId: String,
        importance: NotificationImportance = NotificationImportance.DEFAULT,
        initiate: NotificationChannelBuilder.() -> Unit = {}
    ) = channel(NotificationChannelBuilder.from(channelId, importance).apply(initiate).build())

    /**
     * Set a notification.
     * @param notification the [NotificationWrapper].
     * @return [NotificationCreator]
     */
    fun notification(notification: NotificationWrapper) = apply { this.notification = notification }

    /**
     * Set a notification.
     * @param initiate the [NotificationBuilder] builder body.
     * @return [NotificationCreator]
     * @throws IllegalStateException if [channel] is not set.
     */
    inline fun notification(initiate: NotificationBuilder.() -> Unit = {}) = apply {
        val channel = this.channel ?: error("You must to use channel function to create a notification channel at first.")
        notification(NotificationBuilder.from(context, channel).apply(initiate).build())
    }

    /**
     * Create the notification poster.
     * @return [Poster]
     */
    fun build() = Poster()

    /**
     * Notification poster.
     */
    inner class Poster internal constructor() {

        /** The current posted notificaion ID. */
        private var shownId: Int? = null

        /** The current posted notificaion tag. */
        private var shownTag = ""

        /**
         * Get the system notification manager.
         * @return [NotificationManagerCompat]
         */
        private val manager by lazy { NotificationManagerCompat.from(context) }

        /**
         * Determine whether the current notification has been canceled.
         * @return [Boolean]
         */
        val isCanceled get() = manager.activeNotifications.none { it.id == shownId || it.tag == shownTag }

        /**
         * Post the current notification.
         *
         * See [NotificationManagerCompat.notify].
         * @param id the notification ID, default is 0.
         * @param tag the notification tag.
         * @return [Poster]
         * @throws IllegalStateException if [channel] and [notification] are not set.
         */
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        @JvmOverloads
        fun post(id: Int = 0, tag: String = "") = apply {
            val currentChannel = channel ?: error("You must to use channel function to create a notification channel.")
            val currentNotification = notification ?: error("You must to use notification function to create a notification.")
            currentChannel.groupInstance?.also { manager.createNotificationChannelGroup(it) }
            manager.createNotificationChannel(currentChannel.instance)
            currentNotification.instance.also {
                if (tag.isNotBlank())
                    manager.notify(tag, id, it)
                else manager.notify(id, it)
                shownId = id
                shownTag = tag
                /** Compat the [NotificationCompat.Builder.setTimeoutAfter]. */
                if (SystemVersion.isLowTo(SystemVersion.O))
                    currentNotification.builder.timeoutAfter?.also { timeoutAfter ->
                        Handler(Looper.getMainLooper()).postDelayed({ cancel() }, timeoutAfter)
                    }
            }
        }

        /**
         * Cancel the current notification.
         *
         * See [NotificationManagerCompat.cancel].
         * @return [Poster]
         */
        fun cancel() = apply {
            if (isCanceled) return@apply
            val currentShownId = shownId ?: return@apply
            if (shownTag.isNotBlank())
                manager.cancel(shownTag, currentShownId)
            else manager.cancel(currentShownId)
        }
    }
}
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
 * This file is created by fankes on 2023/12/14.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "InlinedApi")

package com.highcapable.betterandroid.ui.component.notification

import android.Manifest
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import com.highcapable.betterandroid.system.extension.tool.AndroidVersion
import com.highcapable.betterandroid.ui.component.notification.factory.notificationManager
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationWrapper

/**
 * Notification poster.
 *
 * Use [NotificationManagerCompat] to post a notification.
 * @param notification the current notification.
 */
class NotificationPoster internal constructor(private val notification: NotificationWrapper) {

    /** The current shown notificaion ID. */
    private var shownId: Int? = null

    /** The current shown notificaion tag. */
    private var shownTag = ""

    /**
     * Get the system notification manager.
     * @return [NotificationManagerCompat]
     */
    private val manager by lazy { notification.builder.context.notificationManager }

    /**
     * Determine whether the current notification has been canceled.
     * @return [Boolean]
     */
    val isCanceled get() = manager.activeNotifications.none { it.id == shownId || it.tag == shownTag }

    /**
     * Post the current notification.
     * @see NotificationManagerCompat.notify
     * @param id the notification ID, default is 0.
     * @param tag the notification tag.
     * @return [NotificationPoster]
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @JvmOverloads
    fun post(id: Int = 0, tag: String = "") = apply {
        val channel = notification.builder.channel

        val channelGroup = channel.builder.group?.instance
        channelGroup?.also { manager.createNotificationChannelGroup(it) }

        manager.createNotificationChannel(channel.instance)
        notification.instance.also {
            if (tag.isNotBlank())
                manager.notify(tag, id, it)
            else manager.notify(id, it)

            shownId = id
            shownTag = tag

            // Compat the [NotificationCompat.Builder.setTimeoutAfter].
            if (AndroidVersion.isLessThan(AndroidVersion.O))
                notification.builder.timeoutAfter?.also { timeoutAfter ->
                    Handler(Looper.getMainLooper()).postDelayed({ cancel() }, timeoutAfter)
                }
        }
    }

    /**
     * Cancel the current notification.
     * @see NotificationManagerCompat.cancel
     * @return [NotificationPoster]
     */
    fun cancel() = apply {
        if (isCanceled) return@apply

        val currentShownId = shownId ?: return@apply
        if (shownTag.isNotBlank())
            manager.cancel(shownTag, currentShownId)
        else manager.cancel(currentShownId)
    }
}
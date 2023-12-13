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
@file:Suppress("unused", "UNUSED_PARAMETER", "DEPRECATION", "DeprecatedCallableAddReplaceWith")

package com.highcapable.betterandroid.ui.component.notification

import android.content.Context
import com.highcapable.betterandroid.ui.component.notification.builder.NotificationBuilder
import com.highcapable.betterandroid.ui.component.notification.builder.NotificationChannelBuilder
import com.highcapable.betterandroid.ui.component.notification.factory.createNotification
import com.highcapable.betterandroid.ui.component.notification.type.NotificationImportance
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelWrapper
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationWrapper

/**
 * Notification creator.
 *
 * - This class is deprecated and will be removed in the future.
 *
 * - Please use [Context.createNotification] instead.
 */
@Deprecated(message = "No effect and will be removed in the future.")
class NotificationCreator private constructor(private val context: Context) {

    companion object {

        /**
         * - This class is deprecated and will be removed in the future.
         */
        @Deprecated(message = "No effect and will be removed in the future.")
        @JvmStatic
        fun from(context: Context) = NotificationCreator(context)
    }

    /**
     * - This class is deprecated and will be removed in the future.
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    fun channel(channel: NotificationChannelWrapper) = this

    /**
     * - This class is deprecated and will be removed in the future.
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    inline fun channel(
        channelId: String,
        importance: NotificationImportance = NotificationImportance.DEFAULT,
        initiate: NotificationChannelBuilder.() -> Unit = {}
    ) = this

    /**
     * - This class is deprecated and will be removed in the future.
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    fun notification(notification: NotificationWrapper) = this

    /**
     * - This class is deprecated and will be removed in the future.
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    inline fun notification(initiate: NotificationBuilder.() -> Unit = {}) = this

    /**
     * - This class is deprecated and will be removed in the future.
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    fun build() = Poster()

    /**
     * - This class is deprecated and will be removed in the future.
     */
    @Deprecated(message = "No effect and will be removed in the future.")
    inner class Poster internal constructor() {

        /**
         * - This class is deprecated and will be removed in the future.
         */
        @Deprecated(message = "No effect and will be removed in the future.")
        val isCanceled get() = false

        /**
         * - This class is deprecated and will be removed in the future.
         */
        @Deprecated(message = "No effect and will be removed in the future.")
        @JvmOverloads
        fun post(id: Int = 0, tag: String = "") = this

        /**
         * - This class is deprecated and will be removed in the future.
         */
        @Deprecated(message = "No effect and will be removed in the future.")
        fun cancel() = this
    }
}
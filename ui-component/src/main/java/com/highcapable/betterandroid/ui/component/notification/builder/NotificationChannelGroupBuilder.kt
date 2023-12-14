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
 * This file is created by fankes on 2023/12/13.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.notification.builder

import android.content.Context
import androidx.core.app.NotificationChannelGroupCompat
import com.highcapable.betterandroid.ui.component.notification.factory.createNotification
import com.highcapable.betterandroid.ui.component.notification.proxy.INotificationBuilder
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelGroupWrapper

/**
 * [NotificationChannelGroupWrapper] builder.
 *
 * - Note: You don't need to consider compatibility with system version below Android 8,
 *         we have made it compatible for you, this feature will be automatically converted
 *         to a legacy solution below Android 8.
 * @param groupId the channel group ID.
 */
class NotificationChannelGroupBuilder private constructor(internal val groupId: String) : INotificationBuilder {

    companion object {

        /**
         * Create a new [NotificationChannelGroupBuilder] from [groupId].
         * @see Context.createNotification
         * @param groupId the channel group ID.
         * @return [NotificationChannelGroupBuilder]
         */
        @JvmStatic
        fun from(groupId: String) = NotificationChannelGroupBuilder(groupId)
    }

    /** @see NotificationChannelGroupCompat.Builder.setName */
    var name: CharSequence = ""

    /** @see NotificationChannelGroupCompat.Builder.setDescription */
    var description = ""

    /**
     * @see NotificationChannelGroupCompat.Builder.setName
     * @param name
     * @return [NotificationChannelGroupBuilder]
     */
    fun name(name: CharSequence) = apply { this.name = name }

    /**
     * @see NotificationChannelGroupCompat.Builder.setDescription
     * @param description
     * @return [NotificationChannelGroupBuilder]
     */
    fun description(description: String) = apply { this.description = description }

    override fun build() = NotificationChannelGroupWrapper(builder = this)
}
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
 * This file is created by fankes on 2023/10/28.
 */
package com.highcapable.betterandroid.ui.component.notification.wrapper

import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.highcapable.betterandroid.ui.component.notification.NotificationChannelBuilder
import com.highcapable.betterandroid.ui.component.notification.proxy.INotificationWrapper
import com.highcapable.betterandroid.ui.component.notification.type.NotificationImportance

/**
 * A wrapper of [NotificationChannelBuilder].
 * @param builder the builder instance.
 */
class NotificationChannelWrapper internal constructor(internal var builder: NotificationChannelBuilder) : INotificationWrapper {

    /**
     * Convert [NotificationImportance] to [NotificationManagerCompat] types.
     * @receiver the [NotificationImportance] type.
     * @return [Int]
     */
    private fun NotificationImportance.toImportanceType() = when (this) {
        NotificationImportance.MIN -> NotificationManagerCompat.IMPORTANCE_MIN
        NotificationImportance.LOW -> NotificationManagerCompat.IMPORTANCE_LOW
        NotificationImportance.DEFAULT -> NotificationManagerCompat.IMPORTANCE_DEFAULT
        NotificationImportance.HIGH -> NotificationManagerCompat.IMPORTANCE_HIGH
    }

    override val instance by lazy {
        NotificationChannelCompat.Builder(builder.channelId, builder.importance.toImportanceType()).apply {
            builder.group?.also { setGroup(it.builder.groupId) }
            builder.name.takeIf(CharSequence::isNotBlank)?.also { setName(it) }
            builder.description.takeIf(CharSequence::isNotBlank)?.also { setDescription(it) }
            builder.isShowBadge?.also { setShowBadge(it) }
            builder.sound?.also { setSound(it.first, it.second) }
            builder.isLightsEnabled?.also { setShowBadge(it) }
            builder.lightColor?.also { setLightColor(it) }
            builder.isVibrationEnabled?.also { setVibrationEnabled(it) }
            builder.vibrationPattern?.also { setVibrationPattern(it) }
            builder.conversationId?.also { setConversationId(it.first, it.second) }
        }.build()
    }
}
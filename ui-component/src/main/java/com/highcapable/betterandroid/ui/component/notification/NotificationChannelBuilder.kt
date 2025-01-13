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
 * This file is created by fankes on 2023/10/27.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.notification

import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.core.app.NotificationChannelCompat
import com.highcapable.betterandroid.ui.component.notification.factory.createNotification
import com.highcapable.betterandroid.ui.component.notification.proxy.INotificationBuilder
import com.highcapable.betterandroid.ui.component.notification.type.NotificationImportance
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelGroupWrapper
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelWrapper

/**
 * [NotificationChannelWrapper] builder.
 *
 * - Note: You don't need to consider compatibility with system version below Android 8,
 *   we have made it compatible for you, this feature will be automatically converted
 *   to a legacy solution below Android 8.
 * @param channelId the channel ID.
 * @param group the channel group.
 * @param importance the notification importance.
 */
class NotificationChannelBuilder private constructor(
    internal val channelId: String,
    internal val group: NotificationChannelGroupWrapper?,
    internal val importance: NotificationImportance
) : INotificationBuilder {

    companion object {

        /**
         * Create a new [NotificationChannelBuilder] from [channelId].
         * @see Context.createNotification
         * @param channelId the channel ID.
         * @param group the channel group, default is null.
         * @param importance the notification importance, default is [NotificationImportance.DEFAULT].
         * @return [NotificationChannelBuilder]
         */
        @JvmStatic
        @JvmOverloads
        fun from(
            channelId: String,
            group: NotificationChannelGroupWrapper? = null,
            importance: NotificationImportance = NotificationImportance.DEFAULT
        ) = NotificationChannelBuilder(channelId, group, importance)
    }

    /** @see NotificationChannelCompat.Builder.setSound */
    internal var sound: Pair<Uri?, AudioAttributes?>? = null

    /** @see NotificationChannelCompat.Builder.setConversationId */
    internal var conversationId: Pair<String, String>? = null

    /** @see NotificationChannelCompat.Builder.setName */
    var name: CharSequence = ""

    /** @see NotificationChannelCompat.Builder.setDescription */
    var description = ""

    /** @see NotificationChannelCompat.Builder.setShowBadge */
    var isShowBadge: Boolean? = null

    /** @see NotificationChannelCompat.Builder.setLightsEnabled */
    var isLightsEnabled: Boolean? = null

    /** @see NotificationChannelCompat.Builder.setLightColor */
    @ColorInt
    var lightColor: Int? = null

    /** @see NotificationChannelCompat.Builder.setVibrationEnabled */
    var isVibrationEnabled: Boolean? = null

    /** @see NotificationChannelCompat.Builder.setVibrationPattern */
    var vibrationPattern: LongArray? = null

    /**
     * @see NotificationChannelCompat.Builder.setName
     * @param name
     * @return [NotificationChannelBuilder]
     */
    fun name(name: CharSequence) = apply { this.name = name }

    /**
     * @see NotificationChannelCompat.Builder.setDescription
     * @param description
     * @return [NotificationChannelBuilder]
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * - This function is deprecated and no effect, use [NotificationChannelGroupBuilder] instead.
     * @see NotificationChannelGroupBuilder
     */
    @Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
    @Deprecated(message = "Use NotificationChannelGroupBuilder instead.")
    @JvmOverloads
    fun group(id: String, name: CharSequence = "", description: String = "") = this

    /**
     * @see NotificationChannelCompat.Builder.setShowBadge
     * @param showBadge
     * @return [NotificationChannelBuilder]
     */
    fun showBadge(showBadge: Boolean) = apply { this.isShowBadge = showBadge }

    /**
     * @see NotificationChannelCompat.Builder.setSound
     * @param sound the sound, default is null.
     * @param audioAttributes the audio attributes, default is null.
     * @return [NotificationChannelBuilder]
     */
    @JvmOverloads
    fun sound(sound: Uri? = null, audioAttributes: AudioAttributes? = null) = apply { this.sound = sound to audioAttributes }

    /**
     * @see NotificationChannelCompat.Builder.setLightsEnabled
     * @param lightsEnabled
     * @return [NotificationChannelBuilder]
     */
    fun lightsEnabled(lightsEnabled: Boolean) = apply { this.isLightsEnabled = lightsEnabled }

    /**
     * @see NotificationChannelCompat.Builder.setLightColor
     * @param lightColor
     * @return [NotificationChannelBuilder]
     */
    fun lightColor(@ColorInt lightColor: Int) = apply { this.lightColor = lightColor }

    /**
     * @see NotificationChannelCompat.Builder.setVibrationEnabled
     * @param vibrationEnabled
     * @return [NotificationChannelBuilder]
     */
    fun vibrationEnabled(vibrationEnabled: Boolean) = apply { this.isVibrationEnabled = vibrationEnabled }

    /**
     * @see NotificationChannelCompat.Builder.setVibrationPattern
     * @param vibrationPattern
     * @return [NotificationChannelBuilder]
     */
    fun vibrationPattern(vibrationPattern: LongArray) = apply { this.vibrationPattern = vibrationPattern }

    /**
     * @see NotificationChannelCompat.Builder.setConversationId
     * @param parentChannelId the parent channel ID.
     * @param conversationId the conversation ID.
     * @return [NotificationChannelBuilder]
     */
    fun conversationId(parentChannelId: String, conversationId: String) = apply { this.conversationId = parentChannelId to conversationId }

    override fun build() = NotificationChannelWrapper(builder = this)
}
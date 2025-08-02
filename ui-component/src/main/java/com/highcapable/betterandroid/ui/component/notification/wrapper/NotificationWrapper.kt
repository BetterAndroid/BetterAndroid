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

import android.media.AudioAttributes
import android.media.AudioManager
import androidx.core.app.NotificationCompat
import com.highcapable.betterandroid.system.extension.tool.AndroidVersion
import com.highcapable.betterandroid.ui.component.R
import com.highcapable.betterandroid.ui.component.notification.NotificationBuilder
import com.highcapable.betterandroid.ui.component.notification.NotificationChannelBuilder
import com.highcapable.betterandroid.ui.component.notification.proxy.INotificationWrapper
import com.highcapable.betterandroid.ui.component.notification.type.NotificationImportance

/**
 * A wrapper of [NotificationBuilder].
 * @param builder the builder instance.
 */
class NotificationWrapper internal constructor(internal var builder: NotificationBuilder) : INotificationWrapper {

    private companion object {

        /** The legacy lights led on and off default seconds (fixed value). */
        private const val LEGACY_LED_ON_OFF_MS = 1000
    }

    /**
     * Get the defaults value from [NotificationChannelBuilder].
     * @receiver the current builder.
     * @return [Int]
     */
    private fun NotificationChannelBuilder.getCompatDefaults(): Int {
        var defaults = 0

        if (isLightsEnabled == true) defaults = defaults or NotificationCompat.DEFAULT_LIGHTS
        if (isVibrationEnabled == true) defaults = defaults or NotificationCompat.DEFAULT_VIBRATE
        if (sound != null) defaults = defaults or NotificationCompat.DEFAULT_SOUND
        if (defaults == 0) defaults = NotificationCompat.DEFAULT_ALL

        return defaults
    }

    /**
     * Convert [AudioAttributes] to [AudioManager] types.
     * @receiver the [AudioAttributes] type.
     * @return [Int]
     */
    private fun AudioAttributes?.toStreamType() = when (this?.usage) {
        AudioAttributes.USAGE_ALARM -> AudioManager.STREAM_ALARM
        AudioAttributes.USAGE_MEDIA -> AudioManager.STREAM_MUSIC
        AudioAttributes.USAGE_NOTIFICATION -> AudioManager.STREAM_NOTIFICATION
        AudioAttributes.USAGE_NOTIFICATION_RINGTONE -> AudioManager.STREAM_RING
        AudioAttributes.USAGE_ASSISTANCE_SONIFICATION -> AudioManager.STREAM_SYSTEM
        AudioAttributes.USAGE_VOICE_COMMUNICATION -> AudioManager.STREAM_VOICE_CALL
        else -> AudioManager.STREAM_NOTIFICATION
    }

    /**
     * Convert [NotificationImportance] to [NotificationCompat] types.
     * @receiver the [NotificationImportance] type.
     * @return [Int]
     */
    private fun NotificationImportance.toPriority() = when (this) {
        NotificationImportance.MIN -> NotificationCompat.PRIORITY_MIN
        NotificationImportance.LOW -> NotificationCompat.PRIORITY_LOW
        NotificationImportance.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
        NotificationImportance.HIGH -> NotificationCompat.PRIORITY_HIGH
    }

    override val instance by lazy {
        NotificationCompat.Builder(builder.context, builder.channel.builder.channelId).apply {
            builder.progress?.also { setProgress(it.first, it.second, it.third) }
            builder.contentTitle.takeIf(CharSequence::isNotBlank)?.also { setContentTitle(it) }
            builder.contentText.takeIf(CharSequence::isNotBlank)?.also { setContentText(it) }
            builder.contentInfo.takeIf(CharSequence::isNotBlank)?.also { setContentInfo(it) }
            builder.subText.takeIf(CharSequence::isNotBlank)?.also { setSubText(it) }
            builder.settingsText.takeIf(CharSequence::isNotBlank)?.also { setSettingsText(it) }
            builder.style?.also { setStyle(it) }
            builder.contentIntent?.also { setContentIntent(it) }
            builder.group.takeIf(String::isNotBlank)?.also { setGroup(it) }
            builder.isGroupSummary?.also { setGroupSummary(it) }
            builder.locusId?.also { setLocusId(it) }
            builder.`when`?.also { setWhen(it) }
            builder.isShowWhen?.also { setShowWhen(it) }
            builder.isUsesChronometer?.also { setUsesChronometer(it) }
            builder.isAutoCancel?.also { setAutoCancel(it) }
            builder.isOnlyAlertOnce?.also { setOnlyAlertOnce(it) }
            builder.isOngoing?.also { setOngoing(it) }
            builder.isLocalOnly?.also { setLocalOnly(it) }
            builder.largeIcon?.also { setLargeIcon(it) }
            builder.badgeIconType?.also { setBadgeIconType(it) }
            builder.category.takeIf(String::isNotBlank)?.also { setCategory(it) }
            builder.bubbleMetadata?.also { setBubbleMetadata(it) }
            builder.number?.also { setNumber(it) }
            builder.ticker.takeIf(CharSequence::isNotBlank)?.also { setTicker(it) }
            builder.deleteIntent?.also { setDeleteIntent(it) }
            builder.fullScreenIntent?.also { setFullScreenIntent(it.first, it.second) }
            builder.color?.also { setColor(it) }
            builder.visibility?.also { setVisibility(it) }
            builder.publicVersion?.also { setPublicVersion(it) }
            builder.sortKey.takeIf(String::isNotBlank)?.also { setSortKey(it) }
            builder.timeoutAfter?.also { setTimeoutAfter(it) }
            builder.shortcutId.takeIf(String::isNotBlank)?.also { setShortcutId(it) }
            builder.shortcutInfo?.also { setShortcutInfo(it) }
            builder.isAllowSystemGeneratedContextualActions?.also { setAllowSystemGeneratedContextualActions(it) }
            builder.smallIconResId?.also { setSmallIcon(it) }
            AndroidVersion.require(AndroidVersion.M) { builder.smallIcon?.also { setSmallIcon(it) } }
            if (builder.smallIconResId == null && AndroidVersion.requireOrNull(AndroidVersion.M, null) { builder.smallIcon } == null)
                setSmallIcon(R.drawable.ic_simple_notification)
            builder.extras?.also { setExtras(it) }
            if (AndroidVersion.isLessThan(AndroidVersion.O))
                builder.channel.builder.also { channel ->
                    channel.sound?.also { setSound(it.first, it.second.toStreamType()) }
                    channel.vibrationPattern?.also { setVibrate(it) }
                    channel.lightColor?.also { setLights(it, LEGACY_LED_ON_OFF_MS, LEGACY_LED_ON_OFF_MS) }
                    setDefaults(channel.getCompatDefaults())
                    setPriority(channel.importance.toPriority())
                }
        }.build()
    }
}
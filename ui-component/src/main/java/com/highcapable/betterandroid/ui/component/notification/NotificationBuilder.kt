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
 * This file is created by fankes on 2023/10/27.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BubbleMetadata
import androidx.core.content.LocusIdCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.betterandroid.ui.component.R
import com.highcapable.betterandroid.ui.component.notification.factory.createNotification
import com.highcapable.betterandroid.ui.component.notification.proxy.INotificationBuilder
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationChannelWrapper
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationWrapper

/**
 * [NotificationWrapper] builder.
 * @param context the current context.
 * @param channel the current notification channel wrapper.
 */
class NotificationBuilder private constructor(internal val context: Context, internal val channel: NotificationChannelWrapper) :
    INotificationBuilder {

    companion object {

        /**
         * Create a new [NotificationBuilder] from [context] and [channel].
         * @see Context.createNotification
         * @param context the current context.
         * @param channel the current notification channel wrapper.
         * @return [NotificationBuilder]
         */
        @JvmStatic
        fun from(context: Context, channel: NotificationChannelWrapper) = NotificationBuilder(context, channel)
    }

    /** @see NotificationCompat.Builder.setFullScreenIntent */
    internal var fullScreenIntent: Pair<PendingIntent?, Boolean>? = null

    /** @see NotificationCompat.Builder.setProgress */
    internal var progress: Triple<Int, Int, Boolean>? = null

    /** @see NotificationCompat.Builder.setContentTitle */
    var contentTitle: CharSequence = ""

    /** @see NotificationCompat.Builder.setContentText */
    var contentText: CharSequence = ""

    /** @see NotificationCompat.Builder.setContentInfo */
    var contentInfo: CharSequence = ""

    /** @see NotificationCompat.Builder.setSubText */
    var subText: CharSequence = ""

    /** @see NotificationCompat.Builder.setSettingsText */
    var settingsText: CharSequence = ""

    /** @see NotificationCompat.Builder.setStyle */
    var style: NotificationCompat.Style? = null

    /** @see NotificationCompat.Builder.setContentIntent */
    var contentIntent: PendingIntent? = null

    /** @see NotificationCompat.Builder.setGroup */
    var group = ""

    /** @see NotificationCompat.Builder.setGroupSummary */
    var isGroupSummary: Boolean? = null

    /** @see NotificationCompat.Builder.setLocusId */
    var locusId: LocusIdCompat? = null

    /** @see NotificationCompat.Builder.setWhen */
    var `when`: Long? = null

    /** @see NotificationCompat.Builder.setShowWhen */
    var isShowWhen: Boolean? = null

    /** @see NotificationCompat.Builder.setUsesChronometer */
    var isUsesChronometer: Boolean? = null

    /** @see NotificationCompat.Builder.setAutoCancel */
    var isAutoCancel: Boolean? = null

    /** @see NotificationCompat.Builder.setOnlyAlertOnce */
    var isOnlyAlertOnce: Boolean? = null

    /** @see NotificationCompat.Builder.setOngoing */
    var isOngoing: Boolean? = null

    /** @see NotificationCompat.Builder.setLocalOnly */
    var isLocalOnly: Boolean? = null

    /** @see NotificationCompat.Builder.setLargeIcon */
    var largeIcon: Bitmap? = null

    /** @see NotificationCompat.Builder.setBadgeIconType */
    var badgeIconType: Int? = null

    /** @see NotificationCompat.Builder.setCategory */
    var category = ""

    /** @see NotificationCompat.Builder.setBubbleMetadata */
    var bubbleMetadata: BubbleMetadata? = null

    /** @see NotificationCompat.Builder.setNumber */
    var number: Int? = null

    /** @see NotificationCompat.Builder.setTicker */
    var ticker: CharSequence = ""

    /** @see NotificationCompat.Builder.setDeleteIntent */
    var deleteIntent: PendingIntent? = null

    /**
     * - Note: Some third-party ROMs may modify this function causing it to
     *   not work as expected, such as MIUI (HyperOS), etc.
     * @see NotificationCompat.Builder.setColor
     */
    @ColorInt
    var color: Int? = null

    /** @see NotificationCompat.Builder.setVisibility */
    var visibility: Int? = null

    /** @see NotificationCompat.Builder.setPublicVersion */
    var publicVersion: Notification? = null

    /** @see NotificationCompat.Builder.setSortKey */
    var sortKey = ""

    /** @see NotificationCompat.Builder.setTimeoutAfter */
    var timeoutAfter: Long? = null

    /** @see NotificationCompat.Builder.setShortcutId */
    var shortcutId = ""

    /** @see NotificationCompat.Builder.setShortcutInfo */
    var shortcutInfo: ShortcutInfoCompat? = null

    /** @see NotificationCompat.Builder.setAllowSystemGeneratedContextualActions */
    var isAllowSystemGeneratedContextualActions: Boolean? = null

    /**
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *   not work as expected, such as MIUI (HyperOS),
     *   ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *   You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     * @see NotificationCompat.Builder.setSmallIcon
     */
    @DrawableRes
    var smallIconResId: Int? = null

    /**
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *   not work as expected, such as MIUI (HyperOS),
     *   ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *   You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     * @see NotificationCompat.Builder.setSmallIcon
     */
    @RequiresApi(SystemVersion.M)
    var smallIcon: IconCompat? = null

    /** @see NotificationCompat.Builder.setExtras */
    var extras: Bundle? = null

    /**
     * @see NotificationCompat.Builder.setContentTitle
     * @param contentTitle
     * @return [NotificationBuilder]
     */
    fun contentTitle(contentTitle: CharSequence) = apply { this.contentTitle = contentTitle }

    /**
     * @see NotificationCompat.Builder.setContentText
     * @param contentText
     * @return [NotificationBuilder]
     */
    fun contentText(contentText: CharSequence) = apply { this.contentText = contentText }

    /**
     * @see NotificationCompat.Builder.setContentInfo
     * @param contentInfo
     * @return [NotificationBuilder]
     */
    fun contentInfo(contentInfo: CharSequence) = apply { this.contentInfo = contentInfo }

    /**
     * @see NotificationCompat.Builder.setSubText
     * @param subText
     * @return [NotificationBuilder]
     */
    fun subText(subText: CharSequence) = apply { this.subText = subText }

    /**
     * @see NotificationCompat.Builder.setSettingsText
     * @param settingsText
     * @return [NotificationBuilder]
     */
    fun settingsText(settingsText: CharSequence) = apply { this.settingsText = settingsText }

    /**
     * @see NotificationCompat.Builder.setStyle
     * @param style
     * @return [NotificationBuilder]
     */
    fun style(style: NotificationCompat.Style) = apply { this.style = style }

    /**
     * @see NotificationCompat.Builder.setContentIntent
     * @param contentIntent
     * @return [NotificationBuilder]
     */
    fun contentIntent(contentIntent: PendingIntent) = apply { this.contentIntent = contentIntent }

    /**
     * @see NotificationCompat.Builder.setGroup
     * @param group
     * @return [NotificationBuilder]
     */
    fun group(group: String) = apply { this.group = group }

    /**
     * @see NotificationCompat.Builder.setGroupSummary
     * @param groupSummary
     * @return [NotificationBuilder]
     */
    fun groupSummary(groupSummary: Boolean) = apply { this.isGroupSummary = groupSummary }

    /**
     * @see NotificationCompat.Builder.setLocusId
     * @param locusId
     * @return [NotificationBuilder]
     */
    fun locusId(locusId: LocusIdCompat) = apply { this.locusId = locusId }

    /**
     * @see NotificationCompat.Builder.setWhen
     * @param when
     * @return [NotificationBuilder]
     */
    fun `when`(`when`: Long) = apply { this.`when` = `when` }

    /**
     * @see NotificationCompat.Builder.setShowWhen
     * @param showWhen
     * @return [NotificationBuilder]
     */
    fun showWhen(showWhen: Boolean) = apply { this.isShowWhen = showWhen }

    /**
     * @see NotificationCompat.Builder.setUsesChronometer
     * @param usesChronometer
     * @return [NotificationBuilder]
     */
    fun usesChronometer(usesChronometer: Boolean) = apply { this.isUsesChronometer = usesChronometer }

    /**
     * @see NotificationCompat.Builder.setAutoCancel
     * @param autoCancel
     * @return [NotificationBuilder]
     */
    fun autoCancel(autoCancel: Boolean) = apply { this.isAutoCancel = autoCancel }

    /**
     * @see NotificationCompat.Builder.setOnlyAlertOnce
     * @param onlyAlertOnce
     * @return [NotificationBuilder]
     */
    fun onlyAlertOnce(onlyAlertOnce: Boolean) = apply { this.isOnlyAlertOnce = onlyAlertOnce }

    /**
     * @see NotificationCompat.Builder.setOngoing
     * @param ongoing
     * @return [NotificationBuilder]
     */
    fun ongoing(ongoing: Boolean) = apply { this.isOngoing = ongoing }

    /**
     * @see NotificationCompat.Builder.setLocalOnly
     * @param localOnly
     * @return [NotificationBuilder]
     */
    fun localOnly(localOnly: Boolean) = apply { this.isLocalOnly = localOnly }

    /**
     * @see NotificationCompat.Builder.setLargeIcon
     * @param largeIcon
     * @return [NotificationBuilder]
     */
    fun largeIcon(largeIcon: Bitmap) = apply { this.largeIcon = largeIcon }

    /**
     * @see NotificationCompat.Builder.setBadgeIconType
     * @param badgeIconType
     * @return [NotificationBuilder]
     */
    fun badgeIconType(badgeIconType: Int) = apply { this.badgeIconType = badgeIconType }

    /**
     * @see NotificationCompat.Builder.setCategory
     * @param category
     * @return [NotificationBuilder]
     */
    fun category(category: String) = apply { this.category = category }

    /**
     * @see NotificationCompat.Builder.setBubbleMetadata
     * @param bubbleMetadata
     * @return [NotificationBuilder]
     */
    fun bubbleMetadata(bubbleMetadata: BubbleMetadata) = apply { this.bubbleMetadata = bubbleMetadata }

    /**
     * @see NotificationCompat.Builder.setNumber
     * @param number
     * @return [NotificationBuilder]
     */
    fun number(number: Int) = apply { this.number = number }

    /**
     * @see NotificationCompat.Builder.setTicker
     * @param ticker
     * @return [NotificationBuilder]
     */
    fun ticker(ticker: CharSequence) = apply { this.ticker = ticker }

    /**
     * @see NotificationCompat.Builder.setDeleteIntent
     * @param deleteIntent
     * @return [NotificationBuilder]
     */
    fun deleteIntent(deleteIntent: PendingIntent) = apply { this.deleteIntent = deleteIntent }

    /**
     * @see NotificationCompat.Builder.setFullScreenIntent
     * @param intent the current intent.
     * @param highPriority whether to use high priority, default true.
     * @return [NotificationBuilder]
     */
    @JvmOverloads
    fun fullScreenIntent(intent: PendingIntent, highPriority: Boolean = true) = apply { this.fullScreenIntent = intent to highPriority }

    /**
     * - Note: Some third-party ROMs may modify this function causing it to
     *   not work as expected, such as MIUI (HyperOS), etc.
     * @see NotificationCompat.Builder.setColor
     * @param color
     * @return [NotificationBuilder]
     */
    fun color(@ColorInt color: Int) = apply { this.color = color }

    /**
     * @see NotificationCompat.Builder.setVisibility
     * @param visibility
     * @return [NotificationBuilder]
     */
    fun visibility(visibility: Int) = apply { this.visibility = visibility }

    /**
     * @see NotificationCompat.Builder.setPublicVersion
     * @param publicVersion
     * @return [NotificationBuilder]
     */
    fun publicVersion(publicVersion: Notification) = apply { this.publicVersion = publicVersion }

    /**
     * @see NotificationCompat.Builder.setSortKey
     * @param sortKey
     * @return [NotificationBuilder]
     */
    fun sortKey(sortKey: String) = apply { this.sortKey = sortKey }

    /**
     * @see NotificationCompat.Builder.setTimeoutAfter
     * @param timeoutAfter
     * @return [NotificationBuilder]
     */
    fun timeoutAfter(timeoutAfter: Long) = apply { this.timeoutAfter = timeoutAfter }

    /**
     * @see NotificationCompat.Builder.setShortcutId
     * @param shortcutId
     * @return [NotificationBuilder]
     */
    fun shortcutId(shortcutId: String) = apply { this.shortcutId = shortcutId }

    /**
     * @see NotificationCompat.Builder.setShortcutInfo
     * @param shortcutInfo
     * @return [NotificationBuilder]
     */
    fun shortcutInfo(shortcutInfo: ShortcutInfoCompat) = apply { this.shortcutInfo = shortcutInfo }

    /**
     * @see NotificationCompat.Builder.setProgress
     * @param max the max progress, default is 100.
     * @param progress the current progress, default is 0.
     * @param indeterminate whether to set a indeterminate status, default false.
     * @return [NotificationBuilder]
     */
    @JvmOverloads
    fun progress(max: Int = 100, progress: Int = 0, indeterminate: Boolean = false) = apply { this.progress = Triple(max, progress, indeterminate) }

    /**
     * @see NotificationCompat.Builder.setAllowSystemGeneratedContextualActions
     * @param allowSystemGeneratedContextualActions
     * @return [NotificationBuilder]
     */
    fun allowSystemGeneratedContextualActions(allowSystemGeneratedContextualActions: Boolean) =
        apply { this.isAllowSystemGeneratedContextualActions = allowSystemGeneratedContextualActions }

    /**
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *   not work as expected, such as MIUI (HyperOS),
     *   ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *   You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     * @see NotificationCompat.Builder.setSmallIcon
     * @param smallIcon
     * @return [NotificationBuilder]
     */
    fun smallIcon(@DrawableRes smallIcon: Int) = apply { this.smallIconResId = smallIcon }

    /**
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *   not work as expected, such as MIUI (HyperOS),
     *   ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *   You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     * @see NotificationCompat.Builder.setSmallIcon
     * @param smallIcon
     * @return [NotificationBuilder]
     */
    @RequiresApi(SystemVersion.M)
    fun smallIcon(smallIcon: IconCompat) = apply { this.smallIcon = smallIcon }

    /**
     * @see NotificationCompat.Builder.setExtras
     * @param extras
     * @return [NotificationBuilder]
     */
    fun extras(extras: Bundle) = apply { this.extras = extras }

    override fun build() = NotificationWrapper(builder = this)
}
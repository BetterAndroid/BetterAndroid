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
 * This file is created by fankes on 2023/10/27.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.notification.builder

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
         *
         * - We recommend to using [Context.createNotification] at first.
         * @param context the current context.
         * @param channel the current notification channel wrapper.
         * @return [NotificationBuilder]
         */
        @JvmStatic
        fun from(context: Context, channel: NotificationChannelWrapper) = NotificationBuilder(context, channel)
    }

    /** See [NotificationCompat.Builder.setFullScreenIntent]. */
    internal var fullScreenIntent: Pair<PendingIntent?, Boolean>? = null

    /** See [NotificationCompat.Builder.setProgress]. */
    internal var progress: Triple<Int, Int, Boolean>? = null

    /** See [NotificationCompat.Builder.setContentTitle]. */
    var contentTitle: CharSequence = ""

    /** See [NotificationCompat.Builder.setContentText]. */
    var contentText: CharSequence = ""

    /** See [NotificationCompat.Builder.setContentInfo]. */
    var contentInfo: CharSequence = ""

    /** See [NotificationCompat.Builder.setSubText]. */
    var subText: CharSequence = ""

    /** See [NotificationCompat.Builder.setSettingsText]. */
    var settingsText: CharSequence = ""

    /** See [NotificationCompat.Builder.setStyle]. */
    var style: NotificationCompat.Style? = null

    /** See [NotificationCompat.Builder.setContentIntent]. */
    var contentIntent: PendingIntent? = null

    /** See [NotificationCompat.Builder.setGroup]. */
    var group = ""

    /** See [NotificationCompat.Builder.setGroupSummary]. */
    var isGroupSummary: Boolean? = null

    /** See [NotificationCompat.Builder.setLocusId]. */
    var locusId: LocusIdCompat? = null

    /** See [NotificationCompat.Builder.setWhen]. */
    var `when`: Long? = null

    /** See [NotificationCompat.Builder.setShowWhen]. */
    var isShowWhen: Boolean? = null

    /** See [NotificationCompat.Builder.setUsesChronometer]. */
    var isUsesChronometer: Boolean? = null

    /** See [NotificationCompat.Builder.setAutoCancel]. */
    var isAutoCancel: Boolean? = null

    /** See [NotificationCompat.Builder.setOnlyAlertOnce]. */
    var isOnlyAlertOnce: Boolean? = null

    /** See [NotificationCompat.Builder.setOngoing]. */
    var isOngoing: Boolean? = null

    /** See [NotificationCompat.Builder.setLocalOnly]. */
    var isLocalOnly: Boolean? = null

    /** See [NotificationCompat.Builder.setLargeIcon]. */
    var largeIcon: Bitmap? = null

    /** See [NotificationCompat.Builder.setBadgeIconType]. */
    var badgeIconType: Int? = null

    /** See [NotificationCompat.Builder.setCategory]. */
    var category = ""

    /** See [NotificationCompat.Builder.setBubbleMetadata]. */
    var bubbleMetadata: BubbleMetadata? = null

    /** See [NotificationCompat.Builder.setNumber]. */
    var number: Int? = null

    /** See [NotificationCompat.Builder.setTicker]. */
    var ticker: CharSequence = ""

    /** See [NotificationCompat.Builder.setDeleteIntent]. */
    var deleteIntent: PendingIntent? = null

    /**
     * See [NotificationCompat.Builder.setColor].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *         not work as expected, such as MIUI (HyperOS), etc.
     */
    @ColorInt
    var color: Int? = null

    /** See [NotificationCompat.Builder.setVisibility]. */
    var visibility: Int? = null

    /** See [NotificationCompat.Builder.setPublicVersion]. */
    var publicVersion: Notification? = null

    /** See [NotificationCompat.Builder.setSortKey]. */
    var sortKey = ""

    /** See [NotificationCompat.Builder.setTimeoutAfter]. */
    var timeoutAfter: Long? = null

    /** See [NotificationCompat.Builder.setShortcutId]. */
    var shortcutId = ""

    /** See [NotificationCompat.Builder.setShortcutInfo]. */
    var shortcutInfo: ShortcutInfoCompat? = null

    /** See [NotificationCompat.Builder.setAllowSystemGeneratedContextualActions]. */
    var isAllowSystemGeneratedContextualActions: Boolean? = null

    /**
     * See [NotificationCompat.Builder.setSmallIcon].
     *
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *         not work as expected, such as MIUI (HyperOS),
     *         ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *         You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     */
    @DrawableRes
    var smallIconResId: Int? = null

    /**
     * See [NotificationCompat.Builder.setSmallIcon].
     *
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *         not work as expected, such as MIUI (HyperOS),
     *         ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *         You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     */
    @RequiresApi(SystemVersion.M)
    var smallIcon: IconCompat? = null

    /** See [NotificationCompat.Builder.setExtras]. */
    var extras: Bundle? = null

    /**
     * See [NotificationCompat.Builder.setContentTitle].
     * @param contentTitle
     * @return [NotificationBuilder]
     */
    fun contentTitle(contentTitle: CharSequence) = apply { this.contentTitle = contentTitle }

    /**
     * See [NotificationCompat.Builder.setContentText].
     * @param contentText
     * @return [NotificationBuilder]
     */
    fun contentText(contentText: CharSequence) = apply { this.contentText = contentText }

    /**
     * See [NotificationCompat.Builder.setContentInfo].
     * @param contentInfo
     * @return [NotificationBuilder]
     */
    fun contentInfo(contentInfo: CharSequence) = apply { this.contentInfo = contentInfo }

    /**
     * See [NotificationCompat.Builder.setSubText].
     * @param subText
     * @return [NotificationBuilder]
     */
    fun subText(subText: CharSequence) = apply { this.subText = subText }

    /**
     * See [NotificationCompat.Builder.setSettingsText].
     * @param settingsText
     * @return [NotificationBuilder]
     */
    fun settingsText(settingsText: CharSequence) = apply { this.settingsText = settingsText }

    /**
     * See [NotificationCompat.Builder.setStyle].
     * @param style
     * @return [NotificationBuilder]
     */
    fun style(style: NotificationCompat.Style) = apply { this.style = style }

    /**
     * See [NotificationCompat.Builder.setContentIntent].
     * @param contentIntent
     * @return [NotificationBuilder]
     */
    fun contentIntent(contentIntent: PendingIntent) = apply { this.contentIntent = contentIntent }

    /**
     * See [NotificationCompat.Builder.setGroup].
     * @param group
     * @return [NotificationBuilder]
     */
    fun group(group: String) = apply { this.group = group }

    /**
     * See [NotificationCompat.Builder.setGroupSummary].
     * @param groupSummary
     * @return [NotificationBuilder]
     */
    fun groupSummary(groupSummary: Boolean) = apply { this.isGroupSummary = groupSummary }

    /**
     * See [NotificationCompat.Builder.setLocusId].
     * @param locusId
     * @return [NotificationBuilder]
     */
    fun locusId(locusId: LocusIdCompat) = apply { this.locusId = locusId }

    /**
     * See [NotificationCompat.Builder.setWhen].
     * @param when
     * @return [NotificationBuilder]
     */
    fun `when`(`when`: Long) = apply { this.`when` = `when` }

    /**
     * See [NotificationCompat.Builder.setShowWhen].
     * @param showWhen
     * @return [NotificationBuilder]
     */
    fun showWhen(showWhen: Boolean) = apply { this.isShowWhen = showWhen }

    /**
     * See [NotificationCompat.Builder.setUsesChronometer].
     * @param usesChronometer
     * @return [NotificationBuilder]
     */
    fun usesChronometer(usesChronometer: Boolean) = apply { this.isUsesChronometer = usesChronometer }

    /**
     * See [NotificationCompat.Builder.setAutoCancel].
     * @param autoCancel
     * @return [NotificationBuilder]
     */
    fun autoCancel(autoCancel: Boolean) = apply { this.isAutoCancel = autoCancel }

    /**
     * See [NotificationCompat.Builder.setOnlyAlertOnce].
     * @param onlyAlertOnce
     * @return [NotificationBuilder]
     */
    fun onlyAlertOnce(onlyAlertOnce: Boolean) = apply { this.isOnlyAlertOnce = onlyAlertOnce }

    /**
     * See [NotificationCompat.Builder.setOngoing].
     * @param ongoing
     * @return [NotificationBuilder]
     */
    fun ongoing(ongoing: Boolean) = apply { this.isOngoing = ongoing }

    /**
     * See [NotificationCompat.Builder.setLocalOnly].
     * @param localOnly
     * @return [NotificationBuilder]
     */
    fun localOnly(localOnly: Boolean) = apply { this.isLocalOnly = localOnly }

    /**
     * See [NotificationCompat.Builder.setLargeIcon].
     * @param largeIcon
     * @return [NotificationBuilder]
     */
    fun largeIcon(largeIcon: Bitmap) = apply { this.largeIcon = largeIcon }

    /**
     * See [NotificationCompat.Builder.setBadgeIconType].
     * @param badgeIconType
     * @return [NotificationBuilder]
     */
    fun badgeIconType(badgeIconType: Int) = apply { this.badgeIconType = badgeIconType }

    /**
     * See [NotificationCompat.Builder.setCategory].
     * @param category
     * @return [NotificationBuilder]
     */
    fun category(category: String) = apply { this.category = category }

    /**
     * See [NotificationCompat.Builder.setBubbleMetadata].
     * @param bubbleMetadata
     * @return [NotificationBuilder]
     */
    fun bubbleMetadata(bubbleMetadata: BubbleMetadata) = apply { this.bubbleMetadata = bubbleMetadata }

    /**
     * See [NotificationCompat.Builder.setNumber].
     * @param number
     * @return [NotificationBuilder]
     */
    fun number(number: Int) = apply { this.number = number }

    /**
     * See [NotificationCompat.Builder.setTicker].
     * @param ticker
     * @return [NotificationBuilder]
     */
    fun ticker(ticker: CharSequence) = apply { this.ticker = ticker }

    /**
     * See [NotificationCompat.Builder.setDeleteIntent].
     * @param deleteIntent
     * @return [NotificationBuilder]
     */
    fun deleteIntent(deleteIntent: PendingIntent) = apply { this.deleteIntent = deleteIntent }

    /**
     * See [NotificationCompat.Builder.setFullScreenIntent].
     * @param intent the current intent.
     * @param highPriority whether to use high priority, default true.
     * @return [NotificationBuilder]
     */
    @JvmOverloads
    fun fullScreenIntent(intent: PendingIntent, highPriority: Boolean = true) = apply { this.fullScreenIntent = intent to highPriority }

    /**
     * See [NotificationCompat.Builder.setColor].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *         not work as expected, such as MIUI (HyperOS), etc.
     * @param color
     * @return [NotificationBuilder]
     */
    fun color(@ColorInt color: Int) = apply { this.color = color }

    /**
     * See [NotificationCompat.Builder.setVisibility].
     * @param visibility
     * @return [NotificationBuilder]
     */
    fun visibility(visibility: Int) = apply { this.visibility = visibility }

    /**
     * See [NotificationCompat.Builder.setPublicVersion].
     * @param publicVersion
     * @return [NotificationBuilder]
     */
    fun publicVersion(publicVersion: Notification) = apply { this.publicVersion = publicVersion }

    /**
     * See [NotificationCompat.Builder.setSortKey].
     * @param sortKey
     * @return [NotificationBuilder]
     */
    fun sortKey(sortKey: String) = apply { this.sortKey = sortKey }

    /**
     * See [NotificationCompat.Builder.setTimeoutAfter].
     * @param timeoutAfter
     * @return [NotificationBuilder]
     */
    fun timeoutAfter(timeoutAfter: Long) = apply { this.timeoutAfter = timeoutAfter }

    /**
     * See [NotificationCompat.Builder.setShortcutId].
     * @param shortcutId
     * @return [NotificationBuilder]
     */
    fun shortcutId(shortcutId: String) = apply { this.shortcutId = shortcutId }

    /**
     * See [NotificationCompat.Builder.setShortcutInfo].
     * @param shortcutInfo
     * @return [NotificationBuilder]
     */
    fun shortcutInfo(shortcutInfo: ShortcutInfoCompat) = apply { this.shortcutInfo = shortcutInfo }

    /**
     * See [NotificationCompat.Builder.setProgress].
     * @param max the max progress, default is 100.
     * @param progress the current progress, default is 0.
     * @param indeterminate whether to set a indeterminate status, default false.
     * @return [NotificationBuilder]
     */
    @JvmOverloads
    fun progress(max: Int = 100, progress: Int = 0, indeterminate: Boolean = false) = apply { this.progress = Triple(max, progress, indeterminate) }

    /**
     * See [NotificationCompat.Builder.setAllowSystemGeneratedContextualActions].
     * @param allowSystemGeneratedContextualActions
     * @return [NotificationBuilder]
     */
    fun allowSystemGeneratedContextualActions(allowSystemGeneratedContextualActions: Boolean) =
        apply { this.isAllowSystemGeneratedContextualActions = allowSystemGeneratedContextualActions }

    /**
     * See [NotificationCompat.Builder.setSmallIcon].
     *
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *         not work as expected, such as MIUI (HyperOS),
     *         ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *         You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     * @param smallIcon
     * @return [NotificationBuilder]
     */
    fun smallIcon(@DrawableRes smallIcon: Int) = apply { this.smallIconResId = smallIcon }

    /**
     * See [NotificationCompat.Builder.setSmallIcon].
     *
     * - You must set a small icon before post the notification,
     *   if you not set it, it will be set to defaults [R.drawable.ic_simple_notification].
     *
     * - Note: Some third-party ROMs may modify this function causing it to
     *         not work as expected, such as MIUI (HyperOS),
     *         ZUI, NubiaUI, HarmonyOS 4.0.0, etc.
     *         You can see [here](https://github.com/fankes/AndroidNotifyIconAdapt) for more info.
     * @param smallIcon
     * @return [NotificationBuilder]
     */
    @RequiresApi(SystemVersion.M)
    fun smallIcon(smallIcon: IconCompat) = apply { this.smallIcon = smallIcon }

    /**
     * See [NotificationCompat.Builder.setExtras].
     * @param extras
     * @return [NotificationBuilder]
     */
    fun extras(extras: Bundle) = apply { this.extras = extras }

    override fun build() = NotificationWrapper(builder = this)
}
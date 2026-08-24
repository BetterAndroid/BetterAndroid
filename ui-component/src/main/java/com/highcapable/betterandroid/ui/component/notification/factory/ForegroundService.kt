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
 * This file is created by fankes on 2026/8/24.
 */
@file:Suppress("unused")
@file:JvmName("ForegroundServiceUtils")

package com.highcapable.betterandroid.ui.component.notification.factory

import android.Manifest
import android.app.Service
import androidx.annotation.RequiresPermission
import com.highcapable.betterandroid.system.extension.utils.AndroidVersion
import com.highcapable.betterandroid.ui.component.notification.wrapper.NotificationWrapper

/**
 * Make this [Service] run in the foreground using [notification].
 *
 * The notification channel and channel group will be created before the notification is posted.
 * @see Service.startForeground
 * @receiver the current service.
 * @param id the notification ID, which must not be 0.
 * @param notification the foreground service notification.
 */
@RequiresPermission(Manifest.permission.FOREGROUND_SERVICE)
fun Service.startForeground(id: Int, notification: NotificationWrapper) {
    require(id != 0) { "The foreground service notification ID must not be 0." }
    notification.ensureNotificationChannel(this)
    startForeground(id, notification.instance)
}

/**
 * Make this [Service] run in the foreground using [notification] and [foregroundServiceType].
 *
 * The notification channel and channel group will be created before the notification is posted.
 * @see Service.startForeground
 * @receiver the current service.
 * @param id the notification ID, which must not be 0.
 * @param notification the foreground service notification.
 * @param foregroundServiceType the foreground service type declared by this service.
 */
@RequiresPermission(Manifest.permission.FOREGROUND_SERVICE)
fun Service.startForeground(id: Int, notification: NotificationWrapper, foregroundServiceType: Int) {
    require(id != 0) { "The foreground service notification ID must not be 0." }
    notification.ensureNotificationChannel(this)

    if (AndroidVersion.isAtLeast(AndroidVersion.Q))
        startForeground(id, notification.instance, foregroundServiceType)
    else startForeground(id, notification.instance)
}
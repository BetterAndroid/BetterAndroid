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
package com.highcapable.betterandroid.ui.component.notification.type

import androidx.core.app.NotificationManagerCompat

/**
 * Notification importance types.
 */
enum class NotificationImportance {
    /**
     * Turn on the notification, it will not pop up,
     * but there will be no prompt sound and no icon display in the status bar.
     *
     * - Note: Some third-party ROMs will always display the icon in
     *   the status bar, such as MIUI, HyperOS.
     *
     * See also [NotificationManagerCompat.IMPORTANCE_MIN].
     */
    MIN,

    /**
     * Turn on the notification, it will not pop up, not sound,
     * and it icon will be displayed in the status bar.
     *
     * - Note: Some third-party ROMs will not display the icon in
     *   the status bar, such as ColorOS, but some are always displayed,
     *   such as MIUI, HyperOS.
     *
     * See also [NotificationManagerCompat.IMPORTANCE_LOW].
     */
    LOW,

    /**
     * Turn on the notification, it will not pop up,
     * a prompt sound will rings, and it icon will be displayed in the status bar.
     *
     * See also [NotificationManagerCompat.IMPORTANCE_DEFAULT].
     */
    DEFAULT,

    /**
     * Turn on the notification, it will pop up, sound a prompt,
     * and it icon will be displayed in the status bar.
     *
     * See also [NotificationManagerCompat.IMPORTANCE_HIGH].
     */
    HIGH
}
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
 * This file is created by fankes on 2023/12/13.
 */
package com.highcapable.betterandroid.ui.component.notification.wrapper

import androidx.core.app.NotificationChannelGroupCompat
import com.highcapable.betterandroid.ui.component.notification.NotificationChannelGroupBuilder
import com.highcapable.betterandroid.ui.component.notification.proxy.INotificationWrapper

/**
 * A wrapper of [NotificationChannelGroupBuilder].
 * @param builder the builder instance.
 */
class NotificationChannelGroupWrapper internal constructor(internal var builder: NotificationChannelGroupBuilder) : INotificationWrapper {

    override val instance by lazy {
        NotificationChannelGroupCompat.Builder(builder.groupId).apply {
            builder.name.takeIf(CharSequence::isNotBlank)?.also { setName(it) }
            builder.description.takeIf(String::isNotBlank)?.also { setDescription(it) }
        }.build()
    }
}
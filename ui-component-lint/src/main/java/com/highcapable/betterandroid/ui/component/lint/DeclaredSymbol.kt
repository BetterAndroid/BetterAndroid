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
 * This file is created by fankes on 2026/5/15.
 */
package com.highcapable.betterandroid.ui.component.lint

import com.highcapable.betterandroid.generated.BetterAndroidProperties

object DeclaredSymbol {

    const val BASE_PACKAGE = BetterAndroidProperties.PROJECT_UI_COMPONENT_NAMESPACE
    const val PROXY_PACKAGE = "$BASE_PACKAGE.proxy"
    const val NOTIFICATION_PACKAGE = "$BASE_PACKAGE.notification"
    const val NOTIFICATION_FACTORY_PACKAGE = "$NOTIFICATION_PACKAGE.factory"
    const val SYSTEMBAR_PACKAGE = "$BASE_PACKAGE.systembar"
    const val SYSTEMBAR_STYLE_PACKAGE = "$SYSTEMBAR_PACKAGE.style"
    const val SYSTEMBAR_TYPE_PACKAGE = "$SYSTEMBAR_PACKAGE.type"
}
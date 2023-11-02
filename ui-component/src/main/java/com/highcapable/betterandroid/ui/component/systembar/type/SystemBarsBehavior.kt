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
 * This file is created by fankes on 2022/11/4.
 */
package com.highcapable.betterandroid.ui.component.systembar.type

/**
 * System bars behavior type definition.
 */
enum class SystemBarsBehavior {
    /** The default mode selected by the system. */
    DEFAULT,

    /**
     * Appears as a semi-transparent system bars that slides out of the full screen
     * and continues to hide after a period of time.
     */
    SHOW_TRANSIENT_BARS_BY_SWIPE
}
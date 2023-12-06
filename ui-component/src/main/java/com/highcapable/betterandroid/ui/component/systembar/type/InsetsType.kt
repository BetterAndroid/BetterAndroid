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
 * This file is created by fankes on 2023/12/6.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.component.systembar.type

import com.highcapable.betterandroid.ui.component.systembar.insets.SystemInsets

/**
 * Insets type of insets padding.
 */
enum class InsetsType {
    /** Adaptive, use the one with the largest size first. */
    ADAPTIVE,

    /** Stable, only use the [SystemInsets.stable]. */
    STABLE,

    /** Cutout, only use the [SystemInsets.cutout]. */
    CUTOUT
}
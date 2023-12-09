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
 * This file is created by fankes on 2023/12/8.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.platform.component.systembar.insets

import com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller.wrapper.UIEdgeInsetsWrapper

/**
 * Window insets of system bars.
 *
 * Since iOS dose not have stable insets like Android, so there are only have [safeArea].
 * @param safeArea the safe area insets.
 */
data class SystemBarsInsets(val safeArea: UIEdgeInsetsWrapper)
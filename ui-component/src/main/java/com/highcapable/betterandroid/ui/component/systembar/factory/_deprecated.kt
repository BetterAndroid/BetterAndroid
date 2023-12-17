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
 * This file is created by fankes on 2023/11/24.
 */
@file:Suppress("DeprecatedCallableAddReplaceWith", "UNUSED_PARAMETER", "UnusedReceiverParameter")
@file:JvmName("SystemInsetsUtils")

package com.highcapable.betterandroid.ui.component.systembar.factory

import android.view.View
import androidx.core.graphics.Insets
import com.highcapable.betterandroid.ui.component.insets.factory.handleOnWindowInsetsChanged
import com.highcapable.betterandroid.ui.component.insets.factory.setInsetsPadding
import com.highcapable.betterandroid.ui.component.insets.factory.updateInsetsPadding

/**
 * Apply the system insets and cutout padding in layout.
 *
 * - This function is deprecated and no effect, if you want to add your own insets changes listener,
 *   please use the [View.handleOnWindowInsetsChanged] to your view and use [View.setInsetsPadding] or [View.updateInsetsPadding].
 * @see View.handleOnWindowInsetsChanged
 * @see View.setInsetsPadding
 * @see View.updateInsetsPadding
 */
@Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
@JvmOverloads
fun View.applySystemInsets(systemInsets: Any, vararg types: Any, ignoredCutout: Boolean = false) {
}

/**
 * Append the system insets and cutout padding in layout.
 *
 * - This function is deprecated and no effect, if you want to add your own insets changes listener,
 *   please use the [View.handleOnWindowInsetsChanged] to your view and use [View.setInsetsPadding] or [View.updateInsetsPadding].
 * @see View.handleOnWindowInsetsChanged
 * @see View.setInsetsPadding
 * @see View.updateInsetsPadding
 */
@Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
@JvmOverloads
fun View.appendSystemInsets(systemInsets: Any, vararg types: Any, ignoredCutout: Boolean = false) {
}

/**
 * Remove the system insets and cutout padding in layout.
 *
 * - This function is deprecated and no effect, if you want to add your own insets changes listener,
 *   please use the [View.handleOnWindowInsetsChanged] to your view and use [View.setInsetsPadding] or [View.updateInsetsPadding].
 * @see View.handleOnWindowInsetsChanged
 * @see View.setInsetsPadding
 * @see View.updateInsetsPadding
 */
@Deprecated(message = "Use your own View.handleOnWindowInsetsChanged instead.")
@JvmOverloads
fun View.removeSystemInsets(systemInsets: Any, vararg types: Any, ignoredCutout: Boolean = false) = Insets.NONE
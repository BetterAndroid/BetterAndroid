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
 * This file is created by fankes on 2023/10/31.
 */
@file:Suppress("unused")
@file:JvmName("ClipboardUtils")

package com.highcapable.betterandroid.system.extension.component

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

/**
 * Copy text to clipboard.
 * @receiver the current context.
 * @param text the text to copy.
 * @param label the clip data visible label, default is null.
 * @return [Boolean] whether succeed.
 */
fun Context.copyToClipboard(text: CharSequence, label: CharSequence? = null) =
    getSystemService<ClipboardManager>()?.let { manager ->
        manager.setPrimaryClip(ClipData.newPlainText(label, text))
        (manager.primaryClip?.getItemAt(0)?.text ?: "") == text
    } ?: false

/**
 * Copy text to clipboard.
 * @receiver the current fragment.
 * @param text the text to copy.
 * @param label the clip data visible label, default is null.
 * @return [Boolean] whether succeed.
 */
fun Fragment.copyToClipboard(text: CharSequence, label: CharSequence? = null) = requireContext().copyToClipboard(text, label)

/**
 * Copy text to clipboard.
 * @receiver the current view.
 * @param text the text to copy.
 * @param label the clip data visible label, default is null.
 * @return [Boolean] whether succeed.
 */
fun View.copyToClipboard(text: CharSequence, label: CharSequence? = null) = context.copyToClipboard(text, label)

/**
 * Copy text to clipboard.
 * @receiver the current dialog.
 * @param text the text to copy.
 * @param label the clip data visible label, default is null.
 * @return [Boolean] whether succeed.
 */
fun Dialog.copyToClipboard(text: CharSequence, label: CharSequence? = null) = context.copyToClipboard(text, label)
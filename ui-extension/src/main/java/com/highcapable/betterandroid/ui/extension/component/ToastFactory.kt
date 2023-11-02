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
 * This file is created by fankes on 2022/11/2.
 */
@file:Suppress("unused")
@file:JvmName("ToastUtils")

package com.highcapable.betterandroid.ui.extension.component

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Show a Toast with [Context].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @receiver the current context.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 */
@JvmOverloads
@JvmName("showToast")
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()

/**
 * Show a Toast with [Fragment].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @receiver the current fragment.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 */
@JvmOverloads
@JvmName("showToast")
fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = requireContext().toast(message, duration)

/**
 * Show a Toast with [View].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @receiver the current view.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 */
@JvmOverloads
@JvmName("showToast")
fun View.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = context.toast(message, duration)

/**
 * Show a Toast with [Dialog].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @receiver the current dialog.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 */
@JvmOverloads
@JvmName("showToast")
fun Dialog.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = context.toast(message, duration)
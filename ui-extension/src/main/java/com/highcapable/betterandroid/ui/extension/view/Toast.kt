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
 * This file is created by fankes on 2022/11/2.
 */
@file:Suppress("unused")
@file:JvmName("ToastUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Show a Toast with [Context].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @see Toast.makeText
 * @receiver the current context.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 * @param allowBackground whether allow to show a toast from non-main thread, default false.
 */
@JvmOverloads
@JvmName("showToast")
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT, allowBackground: Boolean = false) {
    fun continueToast() = Toast.makeText(this, message, duration).show()
    if (Looper.myLooper() != Looper.getMainLooper()) {
        if (!allowBackground) error("Not allowed to show a toast from non-main thread, if you must do this, please set allowBackground to true.")
        if (this is Activity) runOnUiThread { continueToast() }
        else Thread {
            Looper.prepare()
            continueToast()
            Looper.loop()
        }.start()
    } else continueToast()
}

/**
 * Show a Toast with [Window].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @see Toast.makeText
 * @receiver the current fragment.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 * @param allowBackground whether allow to show a toast from non-main thread, default false.
 */
@JvmOverloads
@JvmName("showToast")
fun Window.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT, allowBackground: Boolean = false) =
    context.toast(message, duration, allowBackground)

/**
 * Show a Toast with [Fragment].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @see Toast.makeText
 * @receiver the current fragment.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 * @param allowBackground whether allow to show a toast from non-main thread, default false.
 */
@JvmOverloads
@JvmName("showToast")
fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT, allowBackground: Boolean = false) =
    context?.toast(message, duration, allowBackground) ?: Unit

/**
 * Show a Toast with [View].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @see Toast.makeText
 * @receiver the current view.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 * @param allowBackground whether allow to show a toast from non-main thread, default false.
 */
@JvmOverloads
@JvmName("showToast")
fun View.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT, allowBackground: Boolean = false) =
    context.toast(message, duration, allowBackground)

/**
 * Show a Toast with [Dialog].
 *
 * - On Android 13 (33) and higher, you may need this permission [Manifest.permission.POST_NOTIFICATIONS].
 * @see Toast.makeText
 * @receiver the current dialog.
 * @param message the message content.
 * @param duration the duration, default is [Toast.LENGTH_SHORT],
 * only can be [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG].
 * @param allowBackground whether allow to show a toast from non-main thread, default false.
 */
@JvmOverloads
@JvmName("showToast")
fun Dialog.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT, allowBackground: Boolean = false) =
    context.toast(message, duration, allowBackground)
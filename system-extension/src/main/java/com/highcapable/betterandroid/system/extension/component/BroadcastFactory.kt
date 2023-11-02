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
 * This file is created by fankes on 2023/10/27.
 */
@file:Suppress("unused", "InlinedApi", "UnspecifiedRegisterReceiverFlag")
@file:JvmName("BroadcastUtils")

package com.highcapable.betterandroid.system.extension.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.highcapable.betterandroid.system.extension.tool.SystemVersion

/**
 * Register a broadcast receiver.
 *
 * Usage:
 *
 * ```kotlin
 * val filter = IntentFilter().apply {
 *     addAction("com.example.app.action.KNOCK")
 * }
 * registerReceiver(filter) { context, intent ->
 *     val greetings = intent.getStringExtra("greetings")
 *     context.toast(greetings)
 * }
 * ```
 * @receiver the current context.
 * @param filter the [IntentFilter].
 * @param flags the flags, default is [Context.RECEIVER_EXPORTED].
 * @param onReceive callback the receiver event function body.
 */
fun Context.registerReceiver(filter: IntentFilter, flags: Int? = null, onReceive: (context: Context, intent: Intent) -> Unit) {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null) return
            onReceive(context, intent)
        }
    }
    val receiverFlags = flags ?: Context.RECEIVER_EXPORTED
    if (SystemVersion.isHighAndEqualsTo(SystemVersion.T))
        registerReceiver(receiver, filter, receiverFlags)
    else registerReceiver(receiver, filter)
}

/**
 * Send a broadcast.
 *
 * Usage:
 *
 * ```kotlin
 * // Send a broadcast to "com.example.app".
 * sendBroadcast("com.example.app", "com.example.app.action.KNOCK") {
 *     putExtra("greetings", "Hey you!")
 * }
 * // If you don't want to specific a package name.
 * sendBroadcast(action = arrayOf("com.example.app.action.KNOCK")) {
 *     putExtra("greetings", "Hey there!")
 * }
 * ```
 * @receiver the current context.
 * @param packageName the receiverd app's package name, default is empty.
 * @param action the actions you want to send, default is empty.
 * @param initiate the [Intent] builder body.
 */
fun Context.sendBroadcast(packageName: String = "", vararg action: String, initiate: Intent.() -> Unit = {}) =
    sendBroadcast(Intent().apply {
        if (packageName.isNotBlank()) setPackage(packageName)
        action.forEach { setAction(it) }
    }.apply(initiate))
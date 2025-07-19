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
 * This file is created by fankes on 2023/10/27.
 */
@file:Suppress("unused", "InlinedApi", "UnspecifiedRegisterReceiverFlag", "FunctionName")
@file:JvmName("BroadcastUtils")

package com.highcapable.betterandroid.system.extension.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.highcapable.betterandroid.system.extension.tool.AndroidVersion

/**
 * Register a broadcast receiver.
 *
 * Usage:
 *
 * ```kotlin
 * val filter = IntentFilter().apply {
 *     addAction("com.example.app.action.KNOCK")
 * }
 * val receiver = registerReceiver(filter) { context, intent ->
 *     val greetings = intent.getStringExtra("greetings")
 *     context.toast(greetings)
 * }
 * // Unregister the receiver.
 * unregisterReceiver(receiver)
 * ```
 *
 * Or
 *
 * ```kotlin
 * val body = BroadcastReceiver { context, intent ->
 *     val greetings = intent.getStringExtra("greetings")
 *     context.toast(greetings)
 * }
 * val filter = IntentFilter().apply {
 *     addAction("com.example.app.action.KNOCK")
 * }
 * val receiver = registerReceiver(filter, body = body)
 * // Unregister the receiver.
 * unregisterReceiver(receiver)
 * ```
 * @receiver the current context.
 * @param filter the [IntentFilter].
 * @param flags the flags.
 * @param exported whether to add the [Context.RECEIVER_EXPORTED] flag, default true.
 * @param body the [BroadcastReceiver] body.
 * @return [BroadcastReceiver]
 */
@JvmOverloads
fun Context.registerReceiver(
    filter: IntentFilter,
    flags: Int? = null,
    exported: Boolean = true,
    body: BroadcastReceiver.(context: Context, intent: Intent) -> Unit
): BroadcastReceiver {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null) return
            body(context, intent)
        }
    }
    var receiverFlags = flags
    if (exported)
        receiverFlags = if (receiverFlags == null) Context.RECEIVER_EXPORTED
        else receiverFlags or Context.RECEIVER_EXPORTED
    if (AndroidVersion.isAtLeast(AndroidVersion.O))
        if (receiverFlags != null)
            registerReceiver(receiver, filter, receiverFlags)
        else registerReceiver(receiver, filter)
    else registerReceiver(receiver, filter)
    return receiver
}

/**
 * Send a broadcast.
 *
 * Usage:
 *
 * ```kotlin
 * // Send a broadcast to "com.example.app".
 * sendBroadcast("com.example.app") {
 *     action = "com.example.app.action.KNOCK"
 *     putExtra("greetings", "Hey you!")
 * }
 * // If you don't want to specific a package name.
 * sendBroadcast {
 *     action = "com.example.app.action.KNOCK"
 *     putExtra("greetings", "Hey there!")
 * }
 * // If you want to request a permission for the receiverd apps.
 * sendBroadcast("com.example.app", "com.example.app.permission.KNOCK") {
 *    action = "com.example.app.action.KNOCK"
 *    putExtra("greetings", "Hey Pro!")
 * }
 * ```
 * @receiver the current context.
 * @param packageName the receiverd app's package name, default is empty.
 * @param receiverPermission the receiverd app's permission, default is null.
 * @param intent the [Intent] builder body.
 */
@JvmOverloads
fun Context.sendBroadcast(packageName: String = "", receiverPermission: String? = null, intent: Intent.() -> Unit = {}) =
    sendBroadcast(Intent().apply { if (packageName.isNotBlank()) setPackage(packageName) }.apply(intent), receiverPermission)

/**
 * Create a [BroadcastReceiver] body.
 *
 * Usage:
 *
 * ```kotlin
 * val body = BroadcastReceiver { context, intent ->
 *     val greetings = intent.getStringExtra("greetings")
 *     context.toast(greetings)
 * }
 * ```
 * @param body the [BroadcastReceiver] body.
 * @return [BroadcastReceiver] body.
 */
fun BroadcastReceiver(
    body: BroadcastReceiver.(context: Context, intent: Intent) -> Unit
): BroadcastReceiver.(context: Context, intent: Intent) -> Unit = body

/**
 * Send a broadcast.
 *
 * - This function is deprecated and no effect, use [sendBroadcast] instead.
 * @see sendBroadcast
 */
@Suppress("UNUSED_PARAMETER", "UnusedReceiverParameter")
@Deprecated(message = "Use sendBroadcast instead.")
@JvmOverloads
fun Context.sendBroadcast(packageName: String = "", vararg action: String, initiate: Intent.() -> Unit = {}) {
}
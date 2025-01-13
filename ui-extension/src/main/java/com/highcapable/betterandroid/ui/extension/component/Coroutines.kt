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
 * This file is created by fankes on 2024/12/17.
 */
@file:Suppress("unused")
@file:JvmName("CoroutinesUtils")

package com.highcapable.betterandroid.ui.extension.component

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Directly call [launch] function from [LifecycleOwner.lifecycleScope] to start a coroutine.
 * @see CoroutineScope.launch
 */
fun LifecycleOwner.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch(context, start, block)

/**
 * Directly call [async] function from [LifecycleOwner.lifecycleScope] to start a coroutine.
 * @see CoroutineScope.async
 */
fun <T> LifecycleOwner.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
) = lifecycleScope.async(context, start, block)

/**
 * Start delayed coroutine task.
 *
 * Usage:
 *
 * ```kotlin
 * // Delay 500 milliseconds to run the block.
 * // For the original android Handler usage.
 * Handler().postDelayed({
 *     // Do something.
 * }, 500)
 * // For this function usage.
 * runDelayed(500) {
 *     // Do something.
 * }
 * ```
 * @receiver the coroutine scope.
 * @param timeMillis delay time in milliseconds, default is 150.
 * @param context coroutine context, default is [EmptyCoroutineContext].
 * @param withContext with context dispatcher, default is [Dispatchers.Main].
 * @param block result block.
 */
fun CoroutineScope.runDelayed(
    timeMillis: Long = 150,
    context: CoroutineContext = EmptyCoroutineContext,
    withContext: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
) = launch(context) {
    delay(timeMillis)
    withContext(withContext) { block() }
}

/**
 * Start a coroutine task with a countdown.
 *
 * Usage:
 *
 * ```kotlin
 * // Countdown from 10 to 0, delay 1000 milliseconds each time.
 * repeatWithDelay(10) { index ->
 *     Log.d("Repeat", "Index: $index")
 * }
 * ```
 * @receiver the coroutine scope.
 * @param times the countdown times.
 * @param downTo the countdown end value, default is 0.
 * @param timeMillis delay time in milliseconds, default is 1000.
 * @param context coroutine context, default is [EmptyCoroutineContext].
 * @param withContext with context dispatcher, default is [Dispatchers.Main].
 * @param block result block.
 */
fun CoroutineScope.repeatWithDelay(
    times: Int,
    downTo: Int = 0,
    timeMillis: Long = 1000,
    context: CoroutineContext = EmptyCoroutineContext,
    withContext: CoroutineDispatcher = Dispatchers.Main,
    block: (index: Int) -> Unit
) = launch(context) {
    for (index in times downTo downTo) {
        delay(timeMillis)
        withContext(withContext) { block(index) }
    }
}

/**
 * Start delayed coroutine task.
 * @see CoroutineScope.runDelayed
 */
fun LifecycleOwner.runDelayed(
    timeMillis: Long = 150,
    context: CoroutineContext = EmptyCoroutineContext,
    withContext: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
) = lifecycleScope.runDelayed(timeMillis, context, withContext, block)

/**
 * Start a coroutine task with a countdown.
 * @see CoroutineScope.repeatWithDelay
 */
fun LifecycleOwner.repeatWithDelay(
    times: Int,
    downTo: Int = 0,
    timeMillis: Long = 1000,
    context: CoroutineContext = EmptyCoroutineContext,
    withContext: CoroutineDispatcher = Dispatchers.Main,
    block: (index: Int) -> Unit
) = lifecycleScope.repeatWithDelay(times, downTo, timeMillis, context, withContext, block)
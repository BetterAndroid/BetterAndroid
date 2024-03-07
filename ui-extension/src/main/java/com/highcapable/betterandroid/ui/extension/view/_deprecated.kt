/*
 * Better Android - Create more useful tool extensions for Android.
 * Copyright (C) 2019-2024 HighCapable
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
 * This file is created by fankes on 2022/11/3.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.ui.extension.view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Inflate [ViewBinding] into the current instance of generic [Class].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    message = "No effect and will be removed in the future.",
    level = DeprecationLevel.ERROR
)
@JvmOverloads
fun <VB : ViewBinding> Activity.inflateViewBinding(
    inflater: LayoutInflater = layoutInflater,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): VB = error("The old ViewBinding function is deprecated and will be removed in the future.")

/**
 * Inflate [ViewBinding] into the current instance of generic [Class].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    message = "No effect and will be removed in the future.",
    level = DeprecationLevel.ERROR
)
@JvmOverloads
fun <VB : ViewBinding> Fragment.inflateViewBinding(
    inflater: LayoutInflater = layoutInflater,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): VB = error("The old ViewBinding function is deprecated and will be removed in the future.")

/**
 * Inflate [ViewBinding] using current [Class].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    message = "No effect and will be removed in the future.",
    level = DeprecationLevel.ERROR
)
@JvmOverloads
@JvmName("inflateViewBindingTyped")
fun <VB : ViewBinding> Context.inflateViewBinding(
    bindingClass: Class<*>,
    inflater: LayoutInflater = LayoutInflater.from(this),
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): VB = error("The old ViewBinding function is deprecated and will be removed in the future.")

/**
 * Inflate [ViewBinding] using current [Class].
 *
 * - This function is deprecated and no effect, use it may cause errors, will be removed in the future.
 */
@Suppress("UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    message = "No effect and will be removed in the future.",
    level = DeprecationLevel.ERROR
)
@JvmOverloads
fun Context.inflateViewBinding(
    bindingClass: Class<*>,
    inflater: LayoutInflater = LayoutInflater.from(this),
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): ViewBinding = error("The old ViewBinding function is deprecated and will be removed in the future.")
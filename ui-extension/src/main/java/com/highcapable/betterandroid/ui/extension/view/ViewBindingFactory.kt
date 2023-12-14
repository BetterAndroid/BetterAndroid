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
 * This file is created by fankes on 2022/11/3.
 */
@file:Suppress("unused")
@file:JvmName("ViewBindingUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.highcapable.yukireflection.factory.current
import com.highcapable.yukireflection.factory.method
import com.highcapable.yukireflection.type.android.LayoutInflaterClass
import com.highcapable.yukireflection.type.android.ViewGroupClass
import com.highcapable.yukireflection.type.java.BooleanType

/**
 * Inflate [ViewBinding] to an instance of the current generic [Class]
 * or inflate [ViewBinding] with the current [Class] (base function).
 * @param inflater the inflater.
 * @param parent the parent view, default is null.
 * @param attachToRoot whether to attach the root view, default is false.
 * @param instance the current instance ([Context], [Activity], [Fragment] or the other type).
 * @param instanceClass the current instance [Class].
 * @return [VB]
 * @throws IllegalStateException if the generic class is not found.
 */
private fun <VB : ViewBinding> baseInflateViewBinding(
    inflater: LayoutInflater,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false,
    instance: Any? = null,
    instanceClass: Class<*>? = null
) = (instance?.current(ignored = true)?.generic()?.argument() ?: instanceClass)?.method {
    name = "inflate"
    param(LayoutInflaterClass, ViewGroupClass, BooleanType)
}?.ignored()?.get()?.invoke<VB>(inflater, parent, attachToRoot)
    ?: error(instance?.let {
        "Failed to binding views on $it, cannot found the generic class, " +
            "if you are using R8, please configure obfuscation rules."
    } ?: "Binding views instance must not be null.")

/**
 * Inflate [ViewBinding] into the current instance of generic [Class].
 *
 * - The target [Class] used must be created as follows:
 *
 * ```kotlin
 * open class YourBaseActvity<VB : ViewBinding> : Activity() {
 *
 *     lateinit var binding: VB
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         binding = inflateViewBinding()
 *         setContentView(binding.root)
 *     }
 * }
 * ```
 *
 * Usage:
 *
 * ```kotlin
 * class YourActivity : YourBaseActivity<ActivityMainBinding>() {
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         binding.mainText.text = "Hello World!"
 *     }
 * }
 * ```
 * @receiver the current activity.
 * @param inflater the inflater, default is [Activity.getLayoutInflater].
 * @param parent the parent view, default is null.
 * @param attachToRoot whether to attach the root view, default is false.
 * @return [VB]
 */
@JvmOverloads
fun <VB : ViewBinding> Activity.inflateViewBinding(
    inflater: LayoutInflater = layoutInflater,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
) = baseInflateViewBinding<VB>(inflater, parent, attachToRoot, instance = this)

/**
 * Inflate [ViewBinding] into the current instance of generic [Class].
 *
 * - The target [Class] used must be created as follows:
 *
 * ```kotlin
 * open class YourBaseFragment<VB : ViewBinding> : Fragment() {
 *
 *     lateinit var binding: VB
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 *         binding = inflateViewBinding(inflater, container)
 *         return binding.root
 *     }
 *
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         super.onViewCreated(view, savedInstanceState)
 *         if (!this::binding.isInitialized) binding = inflateViewBinding(parent = view.parent as? ViewGroup?)
 *     }
 * }
 * ```
 *
 * Usage:
 *
 * ```kotlin
 * class YourFragment : YourBaseFragment<FragmentMainBinding>() {
 *
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         super.onViewCreated(view, savedInstanceState)
 *         binding.mainText.text = "Hello World!"
 *     }
 * }
 * ```
 * @receiver the current fragment.
 * @param inflater the inflater, default is [Fragment.getLayoutInflater].
 * @param parent the parent view, default is null.
 * @param attachToRoot whether to attach the root view, default is false.
 * @return [VB]
 */
@JvmOverloads
fun <VB : ViewBinding> Fragment.inflateViewBinding(
    inflater: LayoutInflater = layoutInflater,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
) = baseInflateViewBinding<VB>(inflater, parent, attachToRoot, instance = this)

/**
 * Inflate [ViewBinding] using current [Class].
 *
 * Usage:
 *
 * ```kotlin
 * // VB is a currently untyped ViewBinding.
 * val binding = context.inflateViewBinding<VB>(...)
 * ```
 * @receiver the current context.
 * @param bindingClass the current instance of [ViewBinding].
 * @param inflater the inflater, default to create a new [LayoutInflater] using the current [Context].
 * @param parent the parent view, default is null.
 * @param attachToRoot whether to attach the root view, default is false.
 * @return [VB]
 */
@JvmOverloads
@JvmName("inflateViewBinding_Generics")
fun <VB : ViewBinding> Context.inflateViewBinding(
    bindingClass: Class<*>,
    inflater: LayoutInflater = LayoutInflater.from(this),
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
) = baseInflateViewBinding<VB>(inflater, parent, attachToRoot, instanceClass = bindingClass)

/**
 * Inflate [ViewBinding] using current [Class].
 *
 * Usage:
 *
 * ```kotlin
 * // VB is a currently untyped ViewBinding.
 * val binding = context.inflateViewBinding<VB>(...)
 * ```
 * @receiver the current context.
 * @param bindingClass the current instance of [ViewBinding].
 * @param inflater the inflater, default to create a new [LayoutInflater] using the current [Context].
 * @param parent the parent view, default is null.
 * @param attachToRoot whether to attach the root view, default is false.
 * @return [ViewBinding]
 */
@JvmOverloads
fun Context.inflateViewBinding(
    bindingClass: Class<*>,
    inflater: LayoutInflater = LayoutInflater.from(this),
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
) = inflateViewBinding<ViewBinding>(bindingClass, inflater, parent, attachToRoot)
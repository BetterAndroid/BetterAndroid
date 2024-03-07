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
@file:Suppress("unused", "FunctionName", "MemberVisibilityCanBePrivate", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "UNCHECKED_CAST")
@file:JvmName("ViewBindingUtils")

package com.highcapable.betterandroid.ui.extension.binding

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.highcapable.betterandroid.ui.extension.view.layoutInflater
import com.highcapable.yukireflection.factory.classOf
import com.highcapable.yukireflection.factory.implements
import com.highcapable.yukireflection.factory.method
import com.highcapable.yukireflection.type.android.LayoutInflaterClass
import com.highcapable.yukireflection.type.android.ViewClass
import com.highcapable.yukireflection.type.android.ViewGroupClass
import com.highcapable.yukireflection.type.java.BooleanType
import java.lang.reflect.ParameterizedType

/**
 * Create a [ViewBinding] instance.
 *
 * Usage:
 *
 * ```kotlin
 * // Directly inflate the view binding.
 * val binding = ViewBinding<ActivityMainBinding>().inflate(layoutInflater)
 * // Or create the builder at first.
 * val builder = ViewBinding<ActivityMainBinding>()
 * // Then inflate the view binding when you need it.
 * val binding = builder.inflate(layoutInflater)
 * ```
 *
 * - Note: You need to keep the [ViewBinding] class not to be obfuscated,
 *   edit the `proguard-rules.pro` in your project:
 *
 * ```proguard
 * -keep class * implements androidx.viewbinding.ViewBinding {
 *     <init>();
 *     *** bind(***);
 *     *** inflate(***);
 * }
 * ```
 * @see Context.viewBinding
 * @return [ViewBindingBuilder]<[VB]>
 */
inline fun <reified VB : ViewBinding> ViewBinding() = ViewBindingBuilder(classOf<VB>())

/**
 * Create a delegate for [ViewBinding].
 *
 * Usage:
 *
 * ```kotlin
 * class YourActvity : Activity() {
 *
 *     val binding: ActivityMainBinding by viewBinding()
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContentView(binding.root)
 *     }
 * }
 * ```
 *
 * - Note: You need to keep the [ViewBinding] class not to be obfuscated,
 *   edit the `proguard-rules.pro` in your project:
 *
 * ```proguard
 * -keep class * implements androidx.viewbinding.ViewBinding {
 *     <init>();
 *     *** bind(***);
 *     *** inflate(***);
 * }
 * ```
 * @param parent the parent view, default is null.
 * @param attachToParent whether to attach the parent view, default is false.
 * @return [ViewBindingDelegate]<[VB]>
 */
inline fun <reified VB : ViewBinding> Context.viewBinding(parent: ViewGroup? = null, attachToParent: Boolean = false) =
    ViewBindingDelegate(classOf<VB>(), { layoutInflater }, parent, attachToParent)

/**
 * A [ViewBinding] builder.
 * @param bindingClass the [ViewBinding] class.
 */
class ViewBindingBuilder<VB : ViewBinding> internal constructor(private val bindingClass: Class<VB>) {

    companion object {

        /**
         * Create a [ViewBindingBuilder] from [instance]'s generic class.
         *
         * Usage:
         *
         * Create a base [Activity] as follows:
         *
         * ```kotlin
         * open class YourBaseActvity<VB : ViewBinding> : Activity() {
         *
         *     lateinit var binding: VB
         *
         *     override fun onCreate(savedInstanceState: Bundle?) {
         *         super.onCreate(savedInstanceState)
         *         // Inflate the view binding.
         *         binding = ViewBindingBuilder.fromGeneric<VB>(this).inflate(layoutInflater)
         *         setContentView(binding.root)
         *     }
         * }
         * ```
         *
         * Then create your [Activity] as follows:
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
         *
         * - Note: You need to keep the [Activity] class's generic not to be obfuscated,
         *   edit the `proguard-rules.pro` in your project:
         *
         * ```proguard
         * -keepattributes Signature
         * -keep class * extends android.app.Activity
         * ```
         * @param instance the instance.
         * @return [ViewBindingBuilder]<[VB]>
         * @throws IllegalArgumentException if the generic class is not found or not a [ViewBinding].
         */
        fun <VB : ViewBinding> fromGeneric(instance: Any) = fromGeneric<VB>(instance.javaClass)

        /**
         * Create a [ViewBindingBuilder] from [clazz]'s generic class.
         * @see fromGeneric
         * @param clazz the generic class's owner.
         * @return [ViewBindingBuilder]<[VB]>
         * @throws IllegalArgumentException if the generic class is not found or not a [ViewBinding].
         */
        fun <VB : ViewBinding> fromGeneric(clazz: Class<*>): ViewBindingBuilder<VB> {
            val bindingClass = clazz.findSuperGenericClass()
            require(bindingClass != null) {
                "Cannot find the generic class from $clazz, if you are using R8, please configure obfuscation rules."
            }
            require(bindingClass implements classOf<ViewBinding>()) {
                "The generic class $bindingClass from $clazz must be a ViewBinding."
            }; return ViewBindingBuilder(bindingClass as Class<VB>)
        }

        /**
         * Find the super generic class.
         * @return [Class] or null.
         */
        private fun Class<*>.findSuperGenericClass(): Class<*>? {
            return (genericSuperclass as? ParameterizedType?)?.actualTypeArguments?.getOrNull(0) as? Class<*>?
                ?: superclass?.findSuperGenericClass()
        }
    }

    /**
     * Bind [ViewBinding] to an existing [View].
     * @param view the view.
     * @return [VB]
     * @throws IllegalStateException if the binding failed.
     */
    fun bind(view: View): VB {
        val binding = bindingClass.method {
            name = "bind"
            param(ViewClass)
        }.ignored().get().invoke<VB>(view)
        require(binding != null) {
            "Cannot find the bind(View) method in $bindingClass, if you are using R8, please configure obfuscation rules."
        }; return binding
    }

    /**
     * Inflate the [ViewBinding].
     * @param layoutInflater the layout inflater.
     * @param parent the parent view, default is null.
     * @param attachToParent whether to attach the parent view, default is false.
     * @return [VB]
     * @throws IllegalStateException if the binding failed.
     */
    @JvmOverloads
    fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup? = null, attachToParent: Boolean = false): VB {
        var binding: VB?
        binding = bindingClass.method {
            name = "inflate"
            param(LayoutInflaterClass, ViewGroupClass, BooleanType)
        }.ignored().get().invoke<VB>(layoutInflater, parent, attachToParent)
        require(binding != null || parent == null || !attachToParent) {
            "Cannot find the inflate(LayoutInflater, ViewGroup, Boolean) method in $bindingClass, " +
                "if your layout root node is <merge> or <include>, " +
                "this means that your layout must be bound to a parent layout, " +
                "please set the \"parent\" parameter and set the \"attachToParent\" to true.\n" +
                "If you are using R8, please configure obfuscation rules."
        }
        if (binding == null) binding = bindingClass.method {
            name = "inflate"
            param(LayoutInflaterClass, ViewGroupClass)
        }.ignored().get().invoke<VB>(layoutInflater, parent)
        require(binding != null) {
            "Cannot find the inflate(...) method in $bindingClass, if you are using R8, please configure obfuscation rules."
        }; return binding
    }
}

/**
 * A delegate for [ViewBinding].
 * @param bindingClass the [ViewBinding] class.
 * @param layoutInflater the layout inflater.
 * @param parent the parent view.
 * @param attachToParent whether to attach the parent view.
 */
class ViewBindingDelegate<VB : ViewBinding> internal constructor(
    private val bindingClass: Class<VB>,
    private val layoutInflater: () -> LayoutInflater,
    private val parent: ViewGroup?,
    private val attachToParent: Boolean
) {
    operator fun getValue(thisRef: Any?, property: Any?) = ViewBindingBuilder(bindingClass).inflate(layoutInflater(), parent, attachToParent)
}
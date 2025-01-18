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
 * This file is created by fankes on 2023/10/25.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.highcapable.betterandroid.ui.component.activity.base.BaseCompatActivity
import com.highcapable.betterandroid.ui.component.proxy.IViewBinding
import com.highcapable.betterandroid.ui.extension.binding.ViewBindingBuilder

/**
 * App binding activity with [IViewBinding].
 *
 * Inherited from [BaseCompatActivity].
 *
 * Usage:
 *
 * ```kotlin
 * class YourActivity : AppBindingActivity<ActivityMainBinding>() {
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         binding.mainText.text = "Hello World!"
 *         binding.enterFullScreen.setOnClickListener {
 *             systemBars.hide(SystemBars.ALL)
 *         }
 *     }
 * }
 * ```
 */
open class AppBindingActivity<VB : ViewBinding> : BaseCompatActivity(), IViewBinding<VB> {

    override val binding get() = baseBinding ?: error("The binding is not available for this time.")

    /** The base binding for initialize [binding]. */
    private var baseBinding: VB? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = onPrepareContentView(savedInstanceState)
        baseBinding = ViewBindingBuilder.fromGeneric<VB>(this).inflate(inflater)
        super.setContentView(binding.root)
        systemBars.init(binding.root)
    }

    /**
     * Called before set content view.
     *
     * This lifecycle function is called after `super.onCreate(...)` and before `super.setContentView(...)`,
     * you can override this function to do something before set content view.
     * @param savedInstanceState the saved instance state.
     * @return [LayoutInflater]
     */
    open fun onPrepareContentView(savedInstanceState: Bundle?) = layoutInflater

    @Deprecated(message = "Use binding instead it.", level = DeprecationLevel.ERROR)
    override fun setContentView(layoutResID: Int): Unit = throwUnavailableException()

    @Deprecated(message = "Use binding instead it.", level = DeprecationLevel.ERROR)
    override fun setContentView(view: View?): Unit = throwUnavailableException()

    @Deprecated(message = "Use binding instead it.", level = DeprecationLevel.ERROR)
    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?): Unit = throwUnavailableException()

    /**
     * Throw an unavailable exception.
     * @throws IllegalStateException throw it anyway.
     */
    private fun throwUnavailableException(): Nothing = error("Do not use setContentView, please use binding instead it.")
}
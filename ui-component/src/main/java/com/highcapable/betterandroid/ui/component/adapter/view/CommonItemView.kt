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
 * This file is created by fankes on 2023/5/9.
 */
package com.highcapable.betterandroid.ui.component.adapter.view

import android.view.View
import android.widget.BaseAdapter
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.PagerAdapter
import com.highcapable.betterandroid.ui.component.adapter.view.proxy.IBaseItemView
import com.highcapable.betterandroid.ui.extension.binding.ViewBindingBuilder

/**
 * [BaseAdapter] or [PagerAdapter] adapter item view, using entity [E].
 */
internal class CommonItemView<E>(
    override val bindingBuilder: ViewBindingBuilder<*>? = null,
    override val rootViewResId: Int = -1,
    override val rootView: View? = null,
    override val onBindCallback: (ViewBinding?, View?, E, Int) -> Unit
) : IBaseItemView<E>
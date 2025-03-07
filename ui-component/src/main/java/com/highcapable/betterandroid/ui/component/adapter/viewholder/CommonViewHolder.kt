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
 * This file is created by fankes on 2023/5/9.
 */
package com.highcapable.betterandroid.ui.component.adapter.viewholder

import android.widget.BaseAdapter
import androidx.viewpager.widget.PagerAdapter
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base.ViewHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.proxy.IBaseViewHolder

/**
 * [BaseAdapter] or [PagerAdapter] adapter item view holder, using entity [E].
 * @param onBindCallback the view holder binding callback.
 */
internal class CommonViewHolder<E>(
    override val delegate: ViewHolderDelegate<Any>,
    val onBindCallback: (Any, E, Int) -> Unit
) : IBaseViewHolder<E>
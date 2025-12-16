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
 * This file is created by fankes on 2025/3/6.
 */
package com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base.ViewHolderDelegate
import com.highcapable.betterandroid.ui.extension.view.inflate
import com.highcapable.betterandroid.ui.extension.view.layoutInflater

/**
 * XML layout ID type view holder delegate.
 * @param resId the layout resource ID.
 */
class XmlLayoutHolderDelegate internal constructor(@field:LayoutRes private val resId: Int) : ViewHolderDelegate<View>() {

    override fun create(context: Context, parent: ViewGroup?) = context.layoutInflater.inflate(resId, parent, attachToRoot = false)
    override fun getView(instance: View) = instance
}
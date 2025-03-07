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
package com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.highcapable.betterandroid.ui.component.adapter.viewholder.proxy.IBaseViewHolder

/**
 * A delegate for [IBaseViewHolder].
 *
 * You can create your own item view inflate process through this delegate.
 */
abstract class ViewHolderDelegate<VD : Any> {

    /**
     * Create a new instance from delegate.
     *
     * - Note: Never bind [parent] to your [View], you can only use its [ViewGroup.LayoutParams].
     * @param context the current context.
     * @param parent the parent view group.
     * @return [VD]
     */
    abstract fun create(context: Context, parent: ViewGroup?): VD

    /**
     * Get the view from the instance.
     * @param instance the instance of [VD].
     * @return [View]
     */
    abstract fun getView(instance: VD): View
}
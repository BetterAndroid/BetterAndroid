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
 * This file is created by fankes on 2025/3/7.
 */
package com.highcapable.betterandroid.ui.component.adapter.viewholder.impl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base.ViewHolderDelegate
import com.highcapable.betterandroid.ui.component.adapter.viewholder.impl.proxy.IViewHolderImpl

/**
 * Implement the view holder for [RecyclerView].
 * @param delegateInstance the delegate instance.
 * @param viewType the view type.
 * @param rootView the item root view.
 */
class RecyclerViewHolderImpl<VD : Any> private constructor(
    override val delegateInstance: VD,
    val viewType: Int,
    override val rootView: View
) : RecyclerView.ViewHolder(rootView), IViewHolderImpl<VD> {

    internal companion object {

        /**
         * Create a new [RecyclerViewHolderImpl]<[VD]> from [delegate] and [context].
         * @param delegate the view holder delegate.
         * @param viewType the view type.
         * @param context the current context.
         * @param parent the parent view group.
         * @return [RecyclerViewHolderImpl]<[VD]>
         */
        fun <VD : Any> from(
            delegate: ViewHolderDelegate<VD>,
            viewType: Int,
            context: Context,
            parent: ViewGroup?
        ): RecyclerViewHolderImpl<VD> {
            val instance = delegate.create(context, parent)
            val itemView = delegate.getView(instance)

            return RecyclerViewHolderImpl(instance, viewType, itemView)
        }
    }
}
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
 * This file is created by fankes on 2022/11/23.
 */
package com.highcapable.betterandroid.ui.component.adapter.view.proxy

import android.view.View
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding

/**
 * Base adapter item view, using entity [E].
 */
internal interface IBaseItemView<E> {

    /** [ViewBinding]'s [Class] */
    val bindingClass: Class<*>?

    /** The item view layout ID. */
    @get:LayoutRes
    val rootViewResId: Int

    /** The item [View]. */
    val rootView: View?

    /** The item view binding callback. */
    val onBindCallback: (ViewBinding?, View?, E, Int) -> Unit
}
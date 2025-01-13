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
 * This file is created by fankes on 2024/12/19.
 */
@file:Suppress("unused")
@file:JvmName("RecyclerViewUtils")

package com.highcapable.betterandroid.ui.extension.view

import androidx.recyclerview.widget.RecyclerView

/**
 * Get the layout manager of the [RecyclerView].
 * @receiver [RecyclerView]
 * @return [L] or null.
 */
inline fun <reified L : RecyclerView.LayoutManager> RecyclerView.layoutManager() = layoutManager as? L?
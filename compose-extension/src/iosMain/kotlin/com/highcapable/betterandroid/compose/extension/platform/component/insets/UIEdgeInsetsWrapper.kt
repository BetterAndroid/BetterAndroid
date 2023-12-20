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
 * This file is created by fankes on 2023/12/9.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.platform.component.insets

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIEdgeInsets

/**
 * A wrapper of [UIEdgeInsets].
 *
 * Workaround for transfer process of [UIEdgeInsets].
 *
 * In Kotlin/Native it may be a memory address (pointer),
 * so the content pointed to by its pointer may be incorrect during the transfer process.
 */
data class UIEdgeInsetsWrapper internal constructor(val left: CGFloat, val top: CGFloat, val right: CGFloat, val bottom: CGFloat) {

    companion object {

        /**
         * Create a new [UIEdgeInsetsWrapper] from [UIEdgeInsets] pointer.
         * @param insets the insets pointer.
         * @return [UIEdgeInsetsWrapper]
         */
        fun from(insets: CValue<UIEdgeInsets>) = insets.useContents { from(insets = this) }

        /**
         * Create a new [UIEdgeInsetsWrapper] from [UIEdgeInsets].
         * @param insets the insets.
         * @return [UIEdgeInsetsWrapper]
         */
        fun from(insets: UIEdgeInsets) = UIEdgeInsetsWrapper(insets.left, insets.top, insets.right, insets.bottom)
    }
}
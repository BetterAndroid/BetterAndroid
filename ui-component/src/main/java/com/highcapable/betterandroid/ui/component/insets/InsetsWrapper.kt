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
 * This file is created by fankes on 2023/12/16.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.insets

import androidx.annotation.Px
import androidx.core.graphics.Insets
import com.highcapable.betterandroid.ui.component.insets.factory.toWrapper

/**
 * A wrapper for [Insets] has a visibility state.
 *
 * You can use [toInsets] to convert it to [Insets].
 * @param left the left insets (px).
 * @param top the top insets (px).
 * @param right the right insets (px).
 * @param bottom the bottom insets (px).
 * @param isVisible the visibility state.
 */
class InsetsWrapper private constructor(
    @Px val left: Int,
    @Px val top: Int,
    @Px val right: Int,
    @Px val bottom: Int,
    val isVisible: Boolean
) {

    companion object {

        /** An empty [InsetsWrapper]. */
        @JvmStatic
        val NONE = InsetsWrapper(0, 0, 0, 0, false)

        /**
         * Create a [InsetsWrapper].
         * @param left the left insets (px).
         * @param top the top insets (px).
         * @param right the right insets (px).
         * @param bottom the bottom insets (px).
         * @param isVisible the visibility state, default true.
         * @return [InsetsWrapper]
         */
        @JvmStatic
        fun of(
            @Px left: Int = 0,
            @Px top: Int = 0,
            @Px right: Int = 0,
            @Px bottom: Int = 0,
            isVisible: Boolean = true
        ) = InsetsWrapper(left, top, right, bottom, isVisible)

        /**
         * Create a [InsetsWrapper] from [Insets].
         * @see Insets.toWrapper
         * @param insets the insets.
         * @param isVisible the visibility state, default true.
         * @return [InsetsWrapper]
         */
        @JvmStatic
        fun of(insets: Insets, isVisible: Boolean = true) = InsetsWrapper(insets.left, insets.top, insets.right, insets.bottom, isVisible)

        /**
         * Add two Insets.
         * @see Insets.add
         * @return [InsetsWrapper]
         */
        @JvmStatic
        fun add(a: InsetsWrapper, b: InsetsWrapper): InsetsWrapper {
            val isVisible = a.isVisible || b.isVisible
            return Insets.max(a.toInsets(), b.toInsets()).toWrapper(isVisible)
        }

        /**
         * Subtract two Insets.
         * @see Insets.subtract
         * @return [InsetsWrapper]
         */
        @JvmStatic
        fun subtract(a: InsetsWrapper, b: InsetsWrapper): InsetsWrapper {
            val isVisible = a.isVisible && b.isVisible
            return Insets.subtract(a.toInsets(), b.toInsets()).toWrapper(isVisible)
        }

        /**
         * Returns the component-wise maximum of two Insets.
         * @see Insets.max
         * @return [InsetsWrapper]
         */
        @JvmStatic
        fun max(a: InsetsWrapper, b: InsetsWrapper): InsetsWrapper {
            val isVisible = a.isVisible || b.isVisible
            return Insets.max(a.toInsets(), b.toInsets()).toWrapper(isVisible)
        }

        /**
         * Returns the component-wise minimum of two Insets.
         * @see Insets.min
         * @return [InsetsWrapper]
         */
        @JvmStatic
        fun min(a: InsetsWrapper, b: InsetsWrapper): InsetsWrapper {
            val isVisible = a.isVisible && b.isVisible
            return Insets.min(a.toInsets(), b.toInsets()).toWrapper(isVisible)
        }
    }

    /**
     * Convert to [Insets].
     * @return [Insets]
     */
    fun toInsets() = Insets.of(left, top, right, bottom)

    override fun equals(other: Any?) = when (other) {
        is InsetsWrapper ->
            left == other.left &&
                top == other.top &&
                right == other.right &&
                bottom == other.bottom &&
                isVisible == other.isVisible
        is Insets ->
            left == other.left &&
                top == other.top &&
                right == other.right &&
                bottom == other.bottom
        else -> false
    }

    override fun hashCode() = 31 * (31 * (31 * (31 * left + top) + right) + bottom) + isVisible.hashCode()

    override fun toString() = "InsetsWrapper(left=$left, top=$top, right=$right, bottom=$bottom, isVisible=$isVisible)"
}
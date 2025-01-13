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
    }

    /**
     * Convert to [Insets].
     * @return [Insets]
     */
    fun toInsets() = Insets.of(left, top, right, bottom)

    /**
     * Returns the component-wise maximum of two Insets.
     * @see Insets.max
     * @param other the other Insets.
     * @return [InsetsWrapper]
     */
    infix fun or(other: InsetsWrapper): InsetsWrapper {
        val isVisible = isVisible || other.isVisible
        return Insets.max(toInsets(), other.toInsets()).toWrapper(isVisible)
    }

    /**
     * Returns the component-wise minimum of two Insets.
     * @see Insets.min
     * @param other the other Insets.
     * @return [InsetsWrapper]
     */
    infix fun and(other: InsetsWrapper): InsetsWrapper {
        val isVisible = isVisible && other.isVisible
        return Insets.min(toInsets(), other.toInsets()).toWrapper(isVisible)
    }

    /**
     * Add two Insets.
     * @see Insets.add
     * @param other the other Insets.
     * @return [InsetsWrapper]
     */
    operator fun plus(other: InsetsWrapper): InsetsWrapper {
        val isVisible = isVisible || other.isVisible
        return Insets.add(toInsets(), other.toInsets()).toWrapper(isVisible)
    }

    /**
     * Subtract two Insets.
     * @see Insets.subtract
     * @param other the other Insets.
     * @return [InsetsWrapper]
     */
    operator fun minus(other: InsetsWrapper): InsetsWrapper {
        val isVisible = isVisible && other.isVisible
        return Insets.subtract(toInsets(), other.toInsets()).toWrapper(isVisible)
    }

    /**
     * Compare two Insets.
     * @param other the other Insets.
     * @return [Int]
     */
    operator fun compareTo(other: InsetsWrapper) = when {
        left != other.left -> left - other.left
        top != other.top -> top - other.top
        right != other.right -> right - other.right
        else -> bottom - other.bottom
    }

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
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
 * This file is created by fankes on 2023/11/27.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Creates a padding of all dp along all 4 edges.
 * @param all the amount of dp to apply to all 4 edges.
 * @return [ComponentPadding]
 */
@Stable
fun ComponentPadding(all: Dp): ComponentPadding = ComponentPaddingImpl(all, all, all, all)

/**
 * Creates a padding of [horizontal] dp along the left and right edges, and of [vertical]
 * dp along the top and bottom edges.
 * @param horizontal the amount of dp to apply to the left and right edges.
 * @param vertical the amount of dp to apply to the top and bottom edges.
 * @return [ComponentPadding]
 */
@Stable
fun ComponentPadding(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
): ComponentPadding = ComponentPaddingImpl(horizontal, vertical, horizontal, vertical)

/**
 * Creates a padding to be applied along the edges inside a box.
 *
 * In LTR contexts start will be applied along the left edge and end will be applied along the right edge.
 *
 * In RTL contexts, start will correspond to the right edge and end to the left.
 * @param start the amount of dp to apply to the start edge.
 * @param top the amount of dp to apply to the top edge.
 * @param end the amount of dp to apply to the end edge.
 * @param bottom the amount of dp to apply to the bottom edge.
 * @return [ComponentPadding]
 */
@Stable
fun ComponentPadding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): ComponentPadding = ComponentPaddingImpl(start, top, end, bottom)

/**
 * A set of 4 paddings, each with a start, top, end, and bottom value.
 *
 * Inherited from [PaddingValues], can be used for [Modifier.padding].
 */
@Stable
interface ComponentPadding : PaddingValues {

    /** The amount of dp to apply to the start edge.  */
    val start: Dp

    /** The amount of dp to apply to the top edge. */
    val top: Dp

    /** The amount of dp to apply to the end edge. */
    val end: Dp

    /** The amount of dp to apply to the bottom edge. */
    val bottom: Dp

    /** The amount of dp to apply to the start and end edges. */
    val horizontal: Dp

    /** The amount of dp to apply to the top and bottom edges. */
    val vertical: Dp

    /** Calculate the left padding. */
    @get:Composable
    @get:ReadOnlyComposable
    val left: Dp

    /** Calculate the right padding. */
    @get:Composable
    @get:ReadOnlyComposable
    val right: Dp

    /**
     * Duplicate this [ComponentPadding] with new values.
     * @param start the amount of dp to apply to the start edge.
     * @param top the amount of dp to apply to the top edge.
     * @param end the amount of dp to apply to the end edge.
     * @param bottom the amount of dp to apply to the bottom edge.
     * @return [ComponentPadding]
     */
    fun copy(
        start: Dp = this.start,
        top: Dp = this.top,
        end: Dp = this.end,
        bottom: Dp = this.bottom
    ): ComponentPadding

    /**
     * Convert to [PaddingValues].
     * @return [PaddingValues]
     */
    fun toPaddingValues(): PaddingValues

    companion object {

        /**
         * Returns a [ComponentPadding] with all values set to 0.dp.
         * @return [ComponentPadding]
         */
        @Stable
        val None = ComponentPadding(0.dp)
    }
}

/**
 * The [ComponentPadding] implementation.
 */
@Immutable
private class ComponentPaddingImpl(
    override val start: Dp,
    override val top: Dp,
    override val end: Dp,
    override val bottom: Dp
) : ComponentPadding {

    override val horizontal get() = start + end
    override val vertical get() = top + bottom

    override val left: Dp
        @ReadOnlyComposable
        @Composable
        get() = calculateLeftPadding(LocalLayoutDirection.current)

    override val right: Dp
        @ReadOnlyComposable
        @Composable
        get() = calculateRightPadding(LocalLayoutDirection.current)

    override fun toPaddingValues() = PaddingValues(start, top, end, bottom)

    override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) start else end

    override fun calculateTopPadding() = top

    override fun calculateRightPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) end else start

    override fun calculateBottomPadding() = bottom

    override fun copy(start: Dp, top: Dp, end: Dp, bottom: Dp) = ComponentPadding(start, top, end, bottom)

    override fun equals(other: Any?): Boolean {
        if (other !is ComponentPadding) return false
        return start == other.start &&
            top == other.top &&
            end == other.end &&
            bottom == other.bottom
    }

    override fun hashCode() =
        ((start.hashCode() * 31 + top.hashCode()) * 31 + end.hashCode()) *
            31 + bottom.hashCode()

    override fun toString() = "ComponentPadding(start=$start, top=$top, end=$end, bottom=$bottom)"
}
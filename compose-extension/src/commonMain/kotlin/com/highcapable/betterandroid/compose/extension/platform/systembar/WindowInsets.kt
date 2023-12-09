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
 * This file is created by fankes on 2023/12/4.
 */
@file:Suppress("unused")

package com.highcapable.betterandroid.compose.extension.platform.systembar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.highcapable.betterandroid.compose.extension.ui.ComponentPadding

/**
 * Create a new [PlatformInsets].
 *
 * See also [PlatformInsets].
 * @param start the start insets.
 * @param top the top insets.
 * @param end the end insets.
 * @param bottom the bottom insets.
 * @return [PlatformInsets]
 */
@Stable
fun PlatformInsets(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): PlatformInsets = PlatformInsetsImpl(start, top, end, bottom)

/**
 * Create a new [PlatformInsets] from [LayoutDirection].
 *
 * See also [PlatformInsets].
 * @param layoutDirection the layout direction, default is [LayoutDirection.Ltr].
 * @param left the left insets.
 * @param top the top insets.
 * @param right the right insets.
 * @param bottom the bottom insets.
 * @return [PlatformInsets]
 */
@Stable
fun PlatformInsets(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    left: Dp = 0.dp,
    top: Dp = 0.dp,
    right: Dp = 0.dp,
    bottom: Dp = 0.dp
): PlatformInsets = when (layoutDirection) {
    LayoutDirection.Ltr -> PlatformInsets(left, top, right, bottom)
    LayoutDirection.Rtl -> PlatformInsets(right, top, left, bottom)
}

/**
 * Create a new [PlatformSystemBarsInsets].
 *
 * See also [PlatformSystemBarsInsets].
 * @param stableStart the stable start insets.
 * @param stableTop the stable top insets.
 * @param stableEnd the stable end insets.
 * @param stableBottom the stable bottom insets.
 * @param cutoutStart the cutout start insets.
 * @param cutoutTop the cutout top insets.
 * @param cutoutEnd the cutout end insets.
 * @param cutoutBottom the cutout bottom insets.
 * @return [PlatformSystemBarsInsets]
 */
@Stable
fun PlatformSystemBarsInsets(
    stableStart: Dp = 0.dp,
    stableTop: Dp = 0.dp,
    stableEnd: Dp = 0.dp,
    stableBottom: Dp = 0.dp,
    cutoutStart: Dp = 0.dp,
    cutoutTop: Dp = 0.dp,
    cutoutEnd: Dp = 0.dp,
    cutoutBottom: Dp = 0.dp
): PlatformSystemBarsInsets = PlatformSystemBarsInsetsImpl(
    stable = PlatformInsets(stableStart, stableTop, stableEnd, stableBottom),
    cutout = PlatformInsets(cutoutStart, cutoutTop, cutoutEnd, cutoutBottom)
)

/**
 * Create a new [PlatformSystemBarsInsets] from [LayoutDirection].
 *
 * See also [PlatformSystemBarsInsets].
 * @param layoutDirection the layout direction, default is [LayoutDirection.Ltr].
 * @param stableLeft the stable left insets.
 * @param stableTop the stable top insets.
 * @param stableRight the stable right insets.
 * @param stableBottom the stable bottom insets.
 * @param cutoutLeft the cutout left insets.
 * @param cutoutTop the cutout top insets.
 * @param cutoutRight the cutout right insets.
 * @param cutoutBottom the cutout bottom insets.
 * @return [PlatformSystemBarsInsets]
 */
@Stable
fun PlatformSystemBarsInsets(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    stableLeft: Dp = 0.dp,
    stableTop: Dp = 0.dp,
    stableRight: Dp = 0.dp,
    stableBottom: Dp = 0.dp,
    cutoutLeft: Dp = 0.dp,
    cutoutTop: Dp = 0.dp,
    cutoutRight: Dp = 0.dp,
    cutoutBottom: Dp = 0.dp
): PlatformSystemBarsInsets = PlatformSystemBarsInsetsImpl(
    stable = PlatformInsets(layoutDirection, stableLeft, stableTop, stableRight, stableBottom),
    cutout = PlatformInsets(layoutDirection, cutoutLeft, cutoutTop, cutoutRight, cutoutBottom)
)

/**
 * Platform insets.
 */
@Stable
interface PlatformInsets {

    companion object {

        /**
         * Return a default [PlatformInsets].
         * @return [PlatformInsets]
         */
        @Stable
        val Default: PlatformInsets = PlatformInsetsImpl(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
    }

    /** The start insets. */
    val start: Dp

    /** The top insets. */
    val top: Dp

    /** The end insets. */
    val end: Dp

    /** The bottom insets. */
    val bottom: Dp

    /**
     * Convert to [ComponentPadding].
     * @return [ComponentPadding]
     */
    fun toComponentPadding(): ComponentPadding

    /**
     * Convert to [PaddingValues].
     * @return [PaddingValues]
     */
    fun toPaddingValues(): PaddingValues
}

/**
 * Platform window insets of system bars.
 *
 * - In Android:
 *
 * There is a set of insets here, one is [stable] and the other is [cutout],
 * [stable] represents the necessary padding of the system, such as the status bars and navigation bars,
 * and [cutout] represents the padding of the notch size part.
 *
 * - In iOS:
 *
 * Since iOS only provides one safe area insets, we cannot know whether the padding
 * given by the system is notch size or stable size, at this time,
 * [stable] and [cutout] will be set to the same value, no matter which one you get.
 */
@Stable
interface PlatformSystemBarsInsets {

    companion object {

        /**
         * Return a default [PlatformSystemBarsInsets].
         * @return [PlatformSystemBarsInsets]
         */
        @Stable
        val Default: PlatformSystemBarsInsets = PlatformSystemBarsInsetsImpl(
            stable = PlatformInsets.Default,
            cutout = PlatformInsets.Default
        )
    }

    /** The stable insets. */
    val stable: PlatformInsets

    /** The notch size (cutout size). */
    val cutout: PlatformInsets

    /**
     * Convert to [ComponentPadding].
     * @param type the insets type, default is [PlatformInsetsType.Adaptive].
     * @return [ComponentPadding]
     */
    fun toComponentPadding(type: PlatformInsetsType = PlatformInsetsType.Adaptive): ComponentPadding

    /**
     * Convert to [PaddingValues].
     * @param type the insets type, default is [PlatformInsetsType.Adaptive].
     * @return [PaddingValues]
     */
    fun toPaddingValues(type: PlatformInsetsType = PlatformInsetsType.Adaptive): PaddingValues
}

/**
 * Platform insets type of insets padding.
 */
@Stable
enum class PlatformInsetsType {
    /** Adaptive, use the one with the largest size first. */
    Adaptive,

    /** Stable, only use the [PlatformSystemBarsInsets.stable]. */
    Stable,

    /** Cutout, only use the [PlatformSystemBarsInsets.cutout]. */
    Cutout
}

/**
 * The [PlatformInsets] implementation.
 */
@Immutable
private data class PlatformInsetsImpl(
    override val start: Dp,
    override val top: Dp,
    override val end: Dp,
    override val bottom: Dp
) : PlatformInsets {
    override fun toComponentPadding() = ComponentPadding(start, top, end, bottom)
    override fun toPaddingValues() = toComponentPadding().toPaddingValues()
    override fun toString() = "PlatformInsets(start=$start, top=$top, end=$end, bottom=$bottom)"
}

/**
 * The [PlatformSystemBarsInsets] implementation.
 */
@Immutable
private data class PlatformSystemBarsInsetsImpl(
    override val stable: PlatformInsets,
    override val cutout: PlatformInsets
) : PlatformSystemBarsInsets {

    /**
     * Compatible, use the one with the largest size first and return.
     * @param stableSize the stable size.
     * @param cutoutSize the notch size (cutout size).
     * @param type the insets type.
     * @return [Dp]
     */
    private fun compatSize(stableSize: Dp, cutoutSize: Dp, type: PlatformInsetsType) =
        when (type) {
            PlatformInsetsType.Adaptive -> if (stableSize > cutoutSize) stableSize else cutoutSize
            PlatformInsetsType.Stable -> stableSize
            PlatformInsetsType.Cutout -> cutoutSize
        }

    override fun toComponentPadding(type: PlatformInsetsType): ComponentPadding {
        val paddingStart = compatSize(stable.start, cutout.start, type)
        val paddingTop = compatSize(stable.top, cutout.top, type)
        val paddingEnd = compatSize(stable.end, cutout.end, type)
        val paddingBottom = compatSize(stable.bottom, cutout.bottom, type)
        return ComponentPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
    }

    override fun toPaddingValues(type: PlatformInsetsType) = toComponentPadding(type).toPaddingValues()

    override fun toString() = "PlatformSystemBarsInsets(stable=$stable, cutout=$cutout)"
}

/**
 * Apply the system bars insets and cutout padding in layout.
 *
 * See also [PlatformSystemBarsController.systemBarsInsets].
 * @param start whether to apply the start insets.
 * @param top whether to apply the top insets.
 * @param end whether to apply the end insets.
 * @param bottom whether to apply the bottom insets.
 * @param type the insets type, default is [PlatformInsetsType.Adaptive].
 * @return [Modifier]
 */
fun Modifier.systemBarsInsets(
    start: Boolean = true,
    top: Boolean = true,
    end: Boolean = true,
    bottom: Boolean = true,
    type: PlatformInsetsType = PlatformInsetsType.Adaptive
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "systemBarsInsets"
        properties["start"] = start
        properties["top"] = top
        properties["end"] = end
        properties["bottom"] = bottom
        properties["type"] = type
    }
) {
    val systemBars = rememberSystemBarsController()
    val systemBarsInsets = systemBars.systemBarsInsets
    systemBarsInsets.toComponentPadding(type).let {
        val fitsStart = if (start) Modifier.padding(start = it.start) else Modifier
        val fitsTop = if (top) Modifier.padding(top = it.top) else Modifier
        val fitsEnd = if (end) Modifier.padding(end = it.end) else Modifier
        val fitsBottom = if (bottom) Modifier.padding(bottom = it.bottom) else Modifier
        then(fitsStart).then(fitsTop).then(fitsEnd).then(fitsBottom)
    }
}
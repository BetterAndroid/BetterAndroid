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
 * Create a new [PlatformSystemInsets].
 * @param stableStart the stable start insets.
 * @param stableTop the stable top insets.
 * @param stableEnd the stable end insets.
 * @param stableBottom the stable bottom insets.
 * @param cutoutStart the cutout start insets.
 * @param cutoutTop the cutout top insets.
 * @param cutoutEnd the cutout end insets.
 * @param cutoutBottom the cutout bottom insets.
 * @return [PlatformSystemInsets]
 */
@Stable
fun PlatformSystemInsets(
    stableStart: Dp = 0.dp,
    stableTop: Dp = 0.dp,
    stableEnd: Dp = 0.dp,
    stableBottom: Dp = 0.dp,
    cutoutStart: Dp = 0.dp,
    cutoutTop: Dp = 0.dp,
    cutoutEnd: Dp = 0.dp,
    cutoutBottom: Dp = 0.dp
): PlatformSystemInsets = PlatformSystemInsetsImpl(
    stable = PlatformInsets(stableStart, stableTop, stableEnd, stableBottom),
    cutout = PlatformInsets(cutoutStart, cutoutTop, cutoutEnd, cutoutBottom)
)

/**
 * Create a new [PlatformSystemInsets] from [LayoutDirection].
 * @param layoutDirection the layout direction, default is [LayoutDirection.Ltr].
 * @param stableLeft the stable left insets.
 * @param stableTop the stable top insets.
 * @param stableRight the stable right insets.
 * @param stableBottom the stable bottom insets.
 * @param cutoutLeft the cutout left insets.
 * @param cutoutTop the cutout top insets.
 * @param cutoutRight the cutout right insets.
 * @param cutoutBottom the cutout bottom insets.
 * @return [PlatformSystemInsets]
 */
@Stable
fun PlatformSystemInsets(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    stableLeft: Dp = 0.dp,
    stableTop: Dp = 0.dp,
    stableRight: Dp = 0.dp,
    stableBottom: Dp = 0.dp,
    cutoutLeft: Dp = 0.dp,
    cutoutTop: Dp = 0.dp,
    cutoutRight: Dp = 0.dp,
    cutoutBottom: Dp = 0.dp
): PlatformSystemInsets = PlatformSystemInsetsImpl(
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
 * Platform system insets of system bars.
 */
@Stable
interface PlatformSystemInsets {

    companion object {

        /**
         * Return a default [PlatformSystemInsets].
         * @return [PlatformSystemInsets]
         */
        @Stable
        val Default: PlatformSystemInsets = PlatformSystemInsetsImpl(
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

    /** Stable, only use the [PlatformSystemInsets.stable]. */
    Stable,

    /** Cutout, only use the [PlatformSystemInsets.cutout]. */
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
 * The [PlatformSystemInsets] implementation.
 */
@Immutable
private data class PlatformSystemInsetsImpl(
    override val stable: PlatformInsets,
    override val cutout: PlatformInsets
) : PlatformSystemInsets {

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

    override fun toString() = "PlatformSystemInsets(stable=$stable, cutout=$cutout)"
}

/**
 * Apply the system insets and cutout padding in layout.
 *
 * See also [PlatformSystemBarsController.systemInsets].
 * @param fitsStart whether to apply the start insets.
 * @param fitsTop whether to apply the top insets.
 * @param fitsEnd whether to apply the end insets.
 * @param fitsBottom whether to apply the bottom insets.
 * @param type the insets type, default is [PlatformInsetsType.Adaptive].
 * @return [Modifier]
 */
fun Modifier.systemInsets(
    fitsStart: Boolean = true,
    fitsTop: Boolean = true,
    fitsEnd: Boolean = true,
    fitsBottom: Boolean = true,
    type: PlatformInsetsType = PlatformInsetsType.Adaptive
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "systemInsets"
        properties["fitsStart"] = fitsStart
        properties["fitsTop"] = fitsTop
        properties["fitsEnd"] = fitsEnd
        properties["fitsBottom"] = fitsBottom
        properties["type"] = type
    }
) {
    val systemBars = rememberPlatformSystemBarsController()
    val systemInsets = systemBars.systemInsets
    systemInsets.toComponentPadding(type).let {
        val start = if (fitsStart) Modifier.padding(start = it.start) else Modifier
        val top = if (fitsTop) Modifier.padding(top = it.top) else Modifier
        val end = if (fitsEnd) Modifier.padding(end = it.end) else Modifier
        val bottom = if (fitsBottom) Modifier.padding(bottom = it.bottom) else Modifier
        then(start).then(top).then(end).then(bottom)
    }
}
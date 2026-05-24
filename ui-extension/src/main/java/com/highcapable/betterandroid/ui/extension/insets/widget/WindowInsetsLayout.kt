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
 * This file is created by fankes on 2026/5/24.
 */
package com.highcapable.betterandroid.ui.extension.insets.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.highcapable.betterandroid.ui.extension.R
import com.highcapable.betterandroid.ui.extension.insets.factory.handleOnWindowInsetsChanged
import com.highcapable.betterandroid.ui.extension.insets.factory.setInsetsPadding

/**
 * Window insets layout.
 *
 * This provides a layout that can handle window insets
 * such as status bars and navigation bars, to ensure that the content is displayed correctly without being obscured by these system UI elements.
 */
open class WindowInsetsLayout(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val attributes by lazy { Attributes() }

    private class Attributes {
        var consumed = false
        var animated = false
        var fitsTopInsets = true
        var fitsLeftInsets = true
        var fitsRightInsets = true
        var fitsBottomInsets = true
        var windowInsetsType = WindowInsetsType.SAFE_DRAWING_IGNORING_IME
    }

    /**
     * Window insets types.
     */
    private enum class WindowInsetsType {
        STATUS_BARS,
        NAVIGATION_BARS,
        CAPTION_BAR,
        SYSTEM_BARS,
        IME,
        TAPPABLE_ELEMENT,
        SYSTEM_GESTURES,
        MANDATORY_SYSTEM_GESTURES,
        DISPLAY_CUTOUT,
        WATERFALL,
        SAFE_GESTURES,
        SAFE_DRAWING,
        SAFE_DRAWING_IGNORING_IME,
        SAFE_CONTENT
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.WindowInsetsLayout) {
            attributes.consumed = getBoolean(R.styleable.WindowInsetsLayout_consumed, attributes.consumed)
            attributes.animated = getBoolean(R.styleable.WindowInsetsLayout_animated, attributes.animated)
            attributes.fitsTopInsets = getBoolean(R.styleable.WindowInsetsLayout_fitsTopInsets, attributes.fitsTopInsets)
            attributes.fitsLeftInsets = getBoolean(R.styleable.WindowInsetsLayout_fitsLeftInsets, attributes.fitsLeftInsets)
            attributes.fitsRightInsets = getBoolean(R.styleable.WindowInsetsLayout_fitsRightInsets, attributes.fitsRightInsets)
            attributes.fitsBottomInsets = getBoolean(R.styleable.WindowInsetsLayout_fitsBottomInsets, attributes.fitsBottomInsets)
            attributes.windowInsetsType = when (getInteger(R.styleable.WindowInsetsLayout_windowInsetsType,
                WindowInsetsType.SAFE_DRAWING_IGNORING_IME.ordinal)) {
                0 -> WindowInsetsType.STATUS_BARS
                1 -> WindowInsetsType.NAVIGATION_BARS
                2 -> WindowInsetsType.CAPTION_BAR
                3 -> WindowInsetsType.SYSTEM_BARS
                4 -> WindowInsetsType.IME
                5 -> WindowInsetsType.TAPPABLE_ELEMENT
                6 -> WindowInsetsType.SYSTEM_GESTURES
                7 -> WindowInsetsType.MANDATORY_SYSTEM_GESTURES
                8 -> WindowInsetsType.DISPLAY_CUTOUT
                9 -> WindowInsetsType.WATERFALL
                10 -> WindowInsetsType.SAFE_GESTURES
                11 -> WindowInsetsType.SAFE_DRAWING
                12 -> WindowInsetsType.SAFE_DRAWING_IGNORING_IME
                13 -> WindowInsetsType.SAFE_CONTENT
                else -> error("Invalid window insets type.")
            }
        }

        handleOnWindowInsetsChanged()
    }

    private fun handleOnWindowInsetsChanged() = handleOnWindowInsetsChanged(
        consumed = attributes.consumed,
        animated = attributes.animated,
        requestApplyOnLayout = true
    ) { layout, insetsWrapper ->
        val insets = when (attributes.windowInsetsType) {
            WindowInsetsType.STATUS_BARS -> insetsWrapper.statusBars
            WindowInsetsType.NAVIGATION_BARS -> insetsWrapper.navigationBars
            WindowInsetsType.CAPTION_BAR -> insetsWrapper.captionBar
            WindowInsetsType.SYSTEM_BARS -> insetsWrapper.systemBars
            WindowInsetsType.IME -> insetsWrapper.ime
            WindowInsetsType.TAPPABLE_ELEMENT -> insetsWrapper.tappableElement
            WindowInsetsType.SYSTEM_GESTURES -> insetsWrapper.systemGestures
            WindowInsetsType.MANDATORY_SYSTEM_GESTURES -> insetsWrapper.mandatorySystemGestures
            WindowInsetsType.DISPLAY_CUTOUT -> insetsWrapper.displayCutout
            WindowInsetsType.WATERFALL -> insetsWrapper.waterfall
            WindowInsetsType.SAFE_GESTURES -> insetsWrapper.safeGestures
            WindowInsetsType.SAFE_DRAWING -> insetsWrapper.safeDrawing
            WindowInsetsType.SAFE_DRAWING_IGNORING_IME -> insetsWrapper.safeDrawingIgnoringIme
            WindowInsetsType.SAFE_CONTENT -> insetsWrapper.safeContent
        }

        layout.setInsetsPadding(
            insets = insets,
            top = attributes.fitsTopInsets,
            left = attributes.fitsLeftInsets,
            right = attributes.fitsRightInsets,
            bottom = attributes.fitsBottomInsets
        )
    }
}
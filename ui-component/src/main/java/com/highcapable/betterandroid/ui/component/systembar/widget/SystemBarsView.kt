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
 * This file is created by fankes on 2022/10/11.
 */
package com.highcapable.betterandroid.ui.component.systembar.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Px
import com.highcapable.betterandroid.ui.component.R
import com.highcapable.betterandroid.ui.component.systembar.SystemBarsController
import com.highcapable.betterandroid.ui.extension.component.base.obtainStyledAttributes

/**
 * System bars stub view (a placeholder layout).
 *
 * [SystemBarsView] will be used by [SystemBarsController].
 *
 * If you want to use [SystemBarsView] manually, you can set [SystemBarsController.hideStub]
 * and then add [SystemBarsView] where you expect.
 *
 * - You can only use [show] or [hide] to control the visibility of [SystemBarsView].
 */
class SystemBarsView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    /**
     * Custom insets type definition.
     */
    enum class InsetsType {
        /** The top, usually the status bar. */
        TOP,

        /** The left. */
        LEFT,

        /** The right. */
        RIGHT,

        /** The bottom, usually the navigation bar. */
        BOTTOM
    }

    /** The current system insets instance. */
    private var systemInsets: SystemBarsController.SystemInsets? = null

    /**
     * Get the current attribute list parameter instance.
     * @return [Attributes]
     */
    private val attributes by lazy { Attributes() }

    /**
     * The current attribute list parameter definition.
     */
    private inner class Attributes {
        var insetsType = InsetsType.TOP
    }

    init {
        obtainStyledAttributes(attrs, R.styleable.SystemBarsView) {
            attributes.insetsType = when (it.getInteger(R.styleable.SystemBarsView_insetsType, 0)) {
                0 -> InsetsType.TOP
                1 -> InsetsType.LEFT
                2 -> InsetsType.RIGHT
                3 -> InsetsType.BOTTOM
                else -> error("Invalid insets type.")
            }
        }
    }

    /**
     * Get the current [SystemBarsView] insets type.
     * @return [InsetsType]
     */
    var insetsType
        get() = attributes.insetsType
        internal set(value) {
            attributes.insetsType = value
        }

    /**
     * Apply the insets and cutout in layout.
     * @param systemInsets the system insets instance.
     */
    internal fun applyInsetsAndCutout(systemInsets: SystemBarsController.SystemInsets) {
        this.systemInsets = systemInsets
        invalidate()
        requestLayout()
    }

    /** Show the layout. */
    fun show() = super.setVisibility(VISIBLE)

    /**
     * Hide the layout.
     *
     * - Note: The [ignoredCutout] parameter may be delayed based on the loading time of [Activity].
     * @param ignoredCutout Whether to ignore the notch size,
     * if not ignored, it will stop hiding on the notch size,
     * default is false.
     */
    @JvmOverloads
    fun hide(ignoredCutout: Boolean = false) {
        if (ignoredCutout || when (attributes.insetsType) {
                InsetsType.TOP -> systemInsets?.cutout?.safeInsetTop ?: 0
                InsetsType.LEFT -> systemInsets?.cutout?.safeInsetLeft ?: 0
                InsetsType.RIGHT -> systemInsets?.cutout?.safeInsetRight ?: 0
                InsetsType.BOTTOM -> systemInsets?.cutout?.safeInsetBottom ?: 0
            } <= 0
        ) super.setVisibility(GONE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (attributes.insetsType) {
            InsetsType.TOP -> setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                compatSize(systemInsets?.cutout?.safeInsetTop, systemInsets?.stable?.top)
            )
            InsetsType.LEFT -> setMeasuredDimension(
                compatSize(systemInsets?.cutout?.safeInsetLeft, systemInsets?.stable?.left),
                MeasureSpec.getSize(heightMeasureSpec)
            )
            InsetsType.RIGHT -> setMeasuredDimension(
                compatSize(systemInsets?.cutout?.safeInsetRight, systemInsets?.stable?.right),
                MeasureSpec.getSize(heightMeasureSpec)
            )
            InsetsType.BOTTOM -> setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                compatSize(systemInsets?.cutout?.safeInsetBottom, systemInsets?.stable?.bottom)
            )
        }
    }

    /**
     * Compatible, use the one with the largest size first and return.
     * @param firstSize the first size (px).
     * @param lastSize the second size (px).
     * @return [Int]
     */
    private fun compatSize(@Px firstSize: Int?, @Px lastSize: Int?): Int {
        val existFirst = firstSize ?: 0
        val existLast = lastSize ?: 0
        return if (existFirst > existLast || existLast <= 0) existFirst else existLast
    }

    override fun setVisibility(visibility: Int) =
        error("Don't called setVisibility in SystemBarsView, please use show or hide function to controller.")
}
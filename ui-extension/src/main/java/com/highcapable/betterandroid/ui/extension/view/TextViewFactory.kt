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
 * This file is created by fankes on 2023/10/25.
 */
@file:Suppress("unused")
@file:JvmName("TextViewUtils")

package com.highcapable.betterandroid.ui.extension.view

import android.graphics.Paint
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import java.util.Locale

/**
 * Get the text view's ellipsize status.
 * @receiver [TextView]
 * @return [Boolean]
 */
val TextView.isEllipsize get() = runCatching { layout.getEllipsisCount(lineCount - 1) > 0 }.getOrNull() ?: false

/**
 * Get or set the text view's underline style.
 * @receiver [TextView]
 * @return [Boolean]
 */
var TextView.isUnderline
    get() = paintFlags and Paint.UNDERLINE_TEXT_FLAG != 0
    set(value) {
        paintFlags = if (value) paintFlags or Paint.UNDERLINE_TEXT_FLAG else paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
    }

/**
 * Get or set the text view's strike through style.
 * @receiver [TextView]
 * @return [Boolean]
 */
var TextView.isStrikeThrough
    get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG != 0
    set(value) {
        paintFlags = if (value) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

/**
 * Get or set the text view's text color.
 * @receiver [TextView]
 * @return [Int]
 */
var TextView.textColor
    @ColorInt get() = textColors.defaultColor
    set(@ColorInt value) {
        setTextColor(value)
    }

/**
 * Update the edit text's text and set the cursor to the end.
 * @receiver [EditText]
 * @param text the text.
 */
fun EditText.updateText(text: CharSequence?) {
    setText(text)
    if (!text.isNullOrEmpty()) setSelection(text.length)
}

/**
 * Clear the text view's text.
 * @receiver [TextView]
 */
fun TextView.clear() {
    text = ""
}

/**
 * Set the text view's digits.
 * @receiver [TextView]
 * @param acceptedChars the allowed chars.
 * @param inputType the input type, default is [InputType.TYPE_CLASS_TEXT] | [InputType.TYPE_TEXT_VARIATION_NORMAL].
 * @param locale the locale, default is [Locale.ROOT], require Android 8.0 (26).
 */
@JvmOverloads
fun TextView.setDigits(
    acceptedChars: String,
    inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL,
    locale: Locale = Locale.ROOT
) {
    @Suppress("DEPRECATION")
    keyListener = when {
        SystemVersion.isHighOrEqualsTo(SystemVersion.O) ->
            object : DigitsKeyListener(locale) {
                override fun getInputType() = inputType
                override fun getAcceptedChars() = acceptedChars.toCharArray()
            }
        else -> object : DigitsKeyListener() {
            override fun getInputType() = inputType
            override fun getAcceptedChars() = acceptedChars.toCharArray()
        }
    }
}
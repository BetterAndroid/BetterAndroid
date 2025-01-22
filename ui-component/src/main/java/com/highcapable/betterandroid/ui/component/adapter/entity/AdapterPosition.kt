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
 * This file is created by fankes on 2025/1/22.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package com.highcapable.betterandroid.ui.component.adapter.entity

/**
 * Adapter dynamic position entity.
 *
 * Use [value] to get the current position.
 */
class AdapterPosition private constructor() {

    internal companion object {

        /**
         * Create a new [AdapterPosition].
         * @param callback the dynamic value callback.
         * @return [AdapterPosition]
         */
        fun from(callback: () -> Int) = AdapterPosition().apply {
            dynamicValueCallback = callback
        }
    }

    /** The dynamic value callback. */
    private var dynamicValueCallback: (() -> Int)? = null

    /**
     * Get the current position value.
     * @return [Int]
     */
    val value get() = dynamicValueCallback?.invoke() ?: -1

    operator fun plus(other: Int) = value + other
    operator fun plus(other: AdapterPosition) = value + other.value
    operator fun minus(other: Int) = value - other
    operator fun minus(other: AdapterPosition) = value - other.value
    operator fun times(other: Int) = value * other
    operator fun times(other: AdapterPosition) = value * other.value
    operator fun div(other: Int) = value / other
    operator fun div(other: AdapterPosition) = value / other.value
    operator fun compareTo(other: Int) = value.compareTo(other)
    operator fun compareTo(other: AdapterPosition) = value.compareTo(other.value)

    override fun equals(other: Any?) = when {
        this === other -> true
        other is Int -> value == other
        other is AdapterPosition -> value == other.value
        else -> false
    }
    override fun hashCode() = 31 * value
    override fun toString() = value.toString()
}
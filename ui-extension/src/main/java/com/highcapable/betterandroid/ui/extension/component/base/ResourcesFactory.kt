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
@file:Suppress("unused", "DEPRECATION", "UseCompatLoadingForDrawables", "UseCompatLoadingForColorStateLists", "RestrictedApi")
@file:JvmName("ResourcesUtils")

package com.highcapable.betterandroid.ui.extension.component.base

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.Px
import androidx.annotation.StyleableRes
import androidx.core.content.res.ColorStateListInflaterCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.core.view.size
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.yukireflection.factory.classOf
import com.highcapable.yukireflection.factory.current

/**
 * Whether in non-standard (special) floating window mode.
 *
 * Standard floating window and multi-window modes can be
 * obtained through [Activity.isInMultiWindowMode].
 *
 * Not in [WindowConfiguration](https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/app/WindowConfiguration.java)
 * types defined in will be recognized as non-standard (special) float mode.
 *
 * The types we currently know are as follows:
 *
 * - WINDOWING_MODE_UNDEFINED (0)
 * - WINDOWING_MODE_FULLSCREEN (1)
 * - WINDOWING_MODE_PINNED (2)
 * - WINDOWING_MODE_FREEFORM (5)
 * - WINDOWING_MODE_MULTI_WINDOW (6)
 *
 * This function is used to fix some non-standard ROMs that still provide
 * some system paddings in floating window mode, causing the UI to display abnormally.
 * @receiver the current resources configuration.
 * @return [Boolean]
 */
val Configuration.isSpecialWindowingMode
    get() = (current(ignored = true)
        .field { name = "windowConfiguration" }
        .current(ignored = true)
        ?.method { name = "getWindowingMode" }
        ?.int() ?: 0) !in 0..6

/**
 * Determine whether the current UI mode is night mode.
 * @receiver the current resources configuration.
 * @return [Boolean]
 */
inline val Configuration.isUiInNightMode get() = uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

/**
 * Convert integer resource id to hex string.
 * @receiver the current resource id.
 * @return [String]
 */
@JvmName("convertToHexResourceId")
fun @receiver:IdRes Int.toHexResourceId() = "0x${Integer.toHexString(this)}"

/**
 * Get [Drawable] from [Resources] (compat).
 * @receiver the current resource.
 * @param id the [Drawable] resources ID.
 * @param theme the given theme, default is null.
 * @return [Drawable]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
@JvmOverloads
fun Resources.getDrawableCompat(@DrawableRes id: Int, theme: Theme? = null) = getDrawableCompat<Drawable>(id, theme)

/**
 * Get [Drawable] from [Resources] (compat).
 * @receiver the current resource.
 * @param id the [Drawable] resources ID.
 * @param theme the given theme, default is null.
 * @return [T]
 * @throws [Resources.NotFoundException] if the resource is not found.
 * @throws IllegalStateException if [Drawable] type is not [T].
 */
@JvmOverloads
@JvmName("getDrawableCompat_Generics")
inline fun <reified T : Drawable> Resources.getDrawableCompat(@DrawableRes id: Int, theme: Theme? = null) =
    ResourcesCompat.getDrawable(this, id, theme) as? T ?: error("Drawable type cannot cast to ${classOf<T>()}.")

/**
 * Get [ColorStateList] from [Resources] (compat).
 * @receiver the current resource.
 * @param id the [ColorStateList] resources ID.
 * @param theme the given theme, default is null.
 * @return [ColorStateList]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
@JvmOverloads
fun Resources.getColorStateListCompat(@ColorRes id: Int, theme: Theme? = null) = ColorStateListCompat.get(this, id, theme)

/**
 * Get color from [Resources] (compat).
 * @receiver the current resource.
 * @param id the color resource ID.
 * @param theme the given theme, default is null.
 * @return [Int]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
@JvmOverloads
fun Resources.getColorCompat(@ColorRes id: Int, theme: Theme? = null) = ResourcesCompat.getColor(this, id, theme)

/**
 * Get [Float] from [Resources] (compat).
 * @receiver the current resource.
 * @param id the [Float] resources ID.
 * @return [Float]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Resources.getFloatCompat(@DimenRes id: Int) = ResourcesCompat.getFloat(this, id)

/**
 * Get [Drawable] from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the [Drawable] resources ID.
 * @return [Drawable]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getDrawableCompat(@DrawableRes id: Int) = resources.getDrawableCompat<Drawable>(id, theme)

/**
 * Get [Drawable] from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the [Drawable] resources ID.
 * @return [T]
 * @throws [Resources.NotFoundException] if the resource is not found.
 * @throws [IllegalStateException] if [Drawable] type is not [T].
 */
@JvmName("getDrawableCompat_Generics")
inline fun <reified T : Drawable> Context.getDrawableCompat(@DrawableRes id: Int) = resources.getDrawableCompat(id, theme)

/**
 * Get [ColorStateList] from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the [ColorStateList] resources ID.
 * @return [ColorStateList]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getColorStateListCompat(@ColorRes id: Int) = resources.getColorCompat(id, theme)

/**
 * Get color from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the color resource ID.
 * @return [Int]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getColorCompat(@ColorRes id: Int) = resources.getColorStateListCompat(id, theme)

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [Boolean].
 *
 * - The final attribute value must be a [Boolean].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Boolean]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsBoolean(@AttrRes id: Int) = resources.getBoolean(getThemeAttrsId(id))

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [Float].
 *
 * - The final attribute value must be a [Float].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Float]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsFloat(@AttrRes id: Int) = resources.getFloatCompat(getThemeAttrsId(id))

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [IntArray].
 *
 * - The final attribute value must be a [IntArray].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [IntArray]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsIntArray(@AttrRes id: Int) = resources.getIntArray(getThemeAttrsId(id))

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [Int].
 *
 * - The final attribute value must be a [Int].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Int]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsInteger(@AttrRes id: Int) = resources.getInteger(getThemeAttrsId(id))

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [Array]<[String]>.
 *
 * - The final attribute value must be a [Array]<[String]>.
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Array]<[String]>
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsStringArray(@AttrRes id: Int): Array<String> = resources.getStringArray(getThemeAttrsId(id))

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [String].
 *
 * - The final attribute value must be a [String].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [String]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsString(@AttrRes id: Int) = resources.getString(getThemeAttrsId(id))

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through dimension.
 *
 * - The final attribute value must be a dimension.
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Float]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsDimension(@AttrRes id: Int) = resources.getDimension(getThemeAttrsId(id))

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [Drawable].
 *
 * - The final attribute value must be a [Drawable].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Drawable]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsDrawable(@AttrRes id: Int) = resources.getDrawableCompat(getThemeAttrsId(id), theme)

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [ColorStateList].
 *
 * - The final attribute value must be a [ColorStateList].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [ColorStateList]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsColorStateList(@AttrRes id: Int) = resources.getColorStateListCompat(getThemeAttrsId(id), theme)

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through color.
 *
 * - The final attribute value must be a color.
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Int]
 * @throws [Resources.NotFoundException] if the resource is not found.
 */
fun Context.getThemeAttrsColor(@AttrRes id: Int) = resources.getColorCompat(getThemeAttrsId(id), theme)

/**
 * Determine whether the resource IDs and contents of two attribute
 * resource lists in the current theme are equal.
 *
 * The attribute resources ID usually like ?attr/value or ?value.
 *
 * Currently the following types are supported:
 *
 * - Color
 * - ColorStateList
 * - Drawable
 * - Dimension
 * - String
 * - StringArray
 * - Integer
 * - IntArray
 * - Float
 * - Boolean
 * @receiver the current context.
 * @param firstId the first attribute resources ID.
 * @param lastId the second attribute resources ID.
 * @return [Boolean] are equal.
 */
fun Context.isThemeAttrsIdsValueEquals(@AttrRes firstId: Int, @AttrRes lastId: Int) = when {
    firstId == lastId -> true
    runCatching { getThemeAttrsColor(firstId) == getThemeAttrsColor(lastId) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsColorStateList(firstId) == getThemeAttrsColorStateList(lastId) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsDrawable(firstId) == getThemeAttrsDrawable(lastId) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsDimension(firstId) == getThemeAttrsDimension(lastId) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsString(firstId) == getThemeAttrsString(lastId) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsStringArray(firstId).contentEquals(getThemeAttrsStringArray(lastId)) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsInteger(firstId) == getThemeAttrsInteger(lastId) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsIntArray(firstId).contentEquals(getThemeAttrsIntArray(lastId)) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsFloat(firstId) == getThemeAttrsFloat(lastId) }.getOrNull() ?: false -> true
    runCatching { getThemeAttrsBoolean(firstId) == getThemeAttrsBoolean(lastId) }.getOrNull() ?: false -> true
    else -> false
}

/**
 * Determine whether the resource ID of
 * the specified attribute resource list exists in the current context.
 *
 * The attribute resources ID usually like ?attr/value or ?value.
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Boolean]
 */
fun Context.hasThemeAttrsId(@AttrRes id: Int) = getThemeAttrsId(id) > 0

/**
 * Get the resource ID of the attribute resource list from the current context.
 *
 * The attribute resources ID usually like ?attr/value or ?value.
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Int] if the resource ID not found will return a value less than 0.
 */
fun Context.getThemeAttrsId(@AttrRes id: Int) =
    runCatching { TypedValue().also { theme.resolveAttribute(id, it, true) }.resourceId }.getOrNull() ?: -1

/**
 * Get and parse the [MenuItem] array from the [Menu] resource ID.
 * @receiver the current context.
 * @param id the [Menu] resource ID.
 * @return [MutableList]<[MenuItem]>
 * @throws IllegalStateException If the resource ID is not a valid [Menu].
 */
fun Context.getMenuFromResource(@MenuRes id: Int) = runCatching {
    PopupMenu(this, null).also { MenuInflater(this).inflate(id, it.menu) }.menu?.takeIf { it.isNotEmpty() }
        ?.let { mutableListOf<MenuItem>().also { items -> for (i in 0 until it.size) items.add(it[i]) } }
}.getOrNull() ?: error("Resource ID ${id.toHexResourceId()} is not a valid menu.")

/**
 * Get string array.
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is -1.
 * @return [Array]<[String]> if no data will return empty array.
 */
@JvmOverloads
fun TypedArray.getStringArray(@StyleableRes index: Int, @ArrayRes defValue: Int = -1) =
    runCatching { resources.getStringArray(getResourceId(index, defValue)) as? Array<String> }.getOrNull() ?: emptyArray()

/**
 * Get int array.
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is -1.
 * @return [Array]<[Int]> if no data will return empty array.
 */
@JvmOverloads
fun TypedArray.getIntArray(@StyleableRes index: Int, @ArrayRes defValue: Int = -1) =
    runCatching { resources.getIntArray(getResourceId(index, defValue)).toTypedArray() }.getOrNull() ?: emptyArray()

/**
 * Get dimension and call [asPx].
 * @receiver the current typed array from resource.
 * @param context the current context.
 * @param index the attribute resources ID array.
 * @param defValue the default value (px).
 * @return [Float]
 */
fun TypedArray.getDimensionPx(context: Context, @StyleableRes index: Int, @Px defValue: Float) =
    getDimension(index, defValue.asDp(context)).asPx<Float>(context)

/**
 * Get dimension and call [asPx].
 * @receiver the current typed array from resource.
 * @param view the current view.
 * @param index the attribute resources ID array.
 * @param defValue the default value (px).
 * @return [Float]
 */
fun TypedArray.getDimensionPx(view: View, @StyleableRes index: Int, @Px defValue: Float) = getDimensionPx(view.context, index, defValue)

/**
 * Get color, return null when no default value.
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Int] or null.
 */
@JvmOverloads
fun TypedArray.getColorOrNull(@StyleableRes index: Int, @ColorInt defValue: Int? = null) =
    if (hasValue(index)) getColor(index, defValue ?: Color.TRANSPARENT) else null

/**
 * Obtain [View] attributes.
 *
 * This function will use View's [Context] call [Context.obtainStyledAttributes]
 * and auto call the [TypedArray.recycle].
 *
 * Usage:
 *
 * ```kotlin
 * class MyView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
 *
 *     init {
 *         obtainStyledAttributes(attrs, R.styleable.MyView) {
 *             val myType = it.getInteger(R.styleable.MyView_myType, 0)
 *         }
 *     }
 * }
 * ```
 * @receiver the current view.
 * @param attrs the attribute list.
 * @param styleIds the style ids.
 * @param result callback [TypedArray], will no effect whether if [attrs] is null.
 */
@JvmOverloads
inline fun View.obtainStyledAttributes(attrs: AttributeSet? = null, styleIds: IntArray, result: (TypedArray) -> Unit) {
    if (attrs == null) return
    context?.obtainStyledAttributes(attrs, styleIds)?.apply { result(this) }?.recycle()
}

/**
 * [ColorStateList] compat tool.
 */
private object ColorStateListCompat {

    /** The current [ColorStateList] caching array. */
    private val sColorStateCaches = SparseArray<ColorStateList>()

    /**
     * Get [ColorStateList] from [Resources] (compat)
     * @param resources the current resource.
     * @param id the [ColorStateList] resource ID.
     * @param theme the given theme.
     * @return [ColorStateList]
     * @throws [Resources.NotFoundException] if the resource is not found.
     */
    fun get(resources: Resources, @ColorRes id: Int, theme: Theme?) =
        SystemVersion.require(SystemVersion.M, createFromXml(resources, id, theme)) { resources.getColorStateList(id, theme) }

    /**
     * Create a new [ColorStateList] through [ColorStateListInflaterCompat.createFromXml].
     * @param resources the current resource.
     * @param id the [ColorStateList] resource ID.
     * @param theme the given theme.
     * @throws [Resources.NotFoundException] if the resource is not found.
     */
    private fun createFromXml(resources: Resources, id: Int, theme: Theme?) =
        sColorStateCaches.get(id) ?: runCatching {
            ColorStateListInflaterCompat.createFromXml(resources, resources.getXml(id), theme).also { sColorStateCaches.put(id, it) }
        }.getOrNull() ?: resources.getColorStateList(id)
}
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
 * This file is created by fankes on 2022/10/11.
 */
@file:Suppress("unused", "DEPRECATION", "UseCompatLoadingForDrawables", "UseCompatLoadingForColorStateLists", "RestrictedApi")
@file:JvmName("ResourcesUtils")

package com.highcapable.betterandroid.ui.extension.component.base

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.annotation.StyleableRes
import androidx.core.content.res.ColorStateListInflaterCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.core.view.size
import com.highcapable.betterandroid.system.extension.tool.SystemVersion
import com.highcapable.yukireflection.factory.classOf
import com.highcapable.yukireflection.factory.field

/**
 * Whether in non-standard (special) floating window mode.
 *
 * - This solution was undesirable, so it was deprecated and no effect, don't use it.
 */
@Suppress("UnusedReceiverParameter", "DeprecatedCallableAddReplaceWith")
@Deprecated(message = "No effect and will be removed in the future.")
val Configuration.isSpecialWindowingMode get() = false

/**
 * Get the current theme resource ID.
 *
 * - Note: This is a hidden API, and it may not be available on all devices.
 * @receiver the current context theme wrapper.
 * @return [Int]
 */
val ContextThemeWrapper.themeResId get() = classOf<ContextThemeWrapper>().field { name = "mThemeResource" }.ignored().get(this).int()

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
 * @throws Resources.NotFoundException if the resource is not found.
 */
@JvmOverloads
fun Resources.getDrawableCompat(@DrawableRes id: Int, theme: Theme? = null) = getDrawableCompat<Drawable>(id, theme)

/**
 * Get [Drawable] from [Resources] (compat).
 * @receiver the current resource.
 * @param id the [Drawable] resources ID.
 * @param theme the given theme, default is null.
 * @return [T]
 * @throws Resources.NotFoundException if the resource is not found.
 * @throws IllegalStateException if [Drawable] type is not [T].
 */
@JvmName("getDrawableCompatTyped")
inline fun <reified T : Drawable> Resources.getDrawableCompat(@DrawableRes id: Int, theme: Theme? = null) =
    ResourcesCompat.getDrawable(this, id, theme) as? T ?: error("Drawable type cannot cast to ${classOf<T>()}.")

/**
 * Get [ColorStateList] from [Resources] (compat).
 * @receiver the current resource.
 * @param id the [ColorStateList] resources ID.
 * @param theme the given theme, default is null.
 * @return [ColorStateList]
 * @throws Resources.NotFoundException if the resource is not found.
 */
@JvmOverloads
fun Resources.getColorStateListCompat(@ColorRes id: Int, theme: Theme? = null) = ColorStateListCompat.get(this, id, theme)

/**
 * Get color from [Resources] (compat).
 * @receiver the current resource.
 * @param id the color resource ID.
 * @param theme the given theme, default is null.
 * @return [Int]
 * @throws Resources.NotFoundException if the resource is not found.
 */
@JvmOverloads
fun Resources.getColorCompat(@ColorRes id: Int, theme: Theme? = null) = ResourcesCompat.getColor(this, id, theme)

/**
 * Get [Float] from [Resources] (compat).
 * @receiver the current resource.
 * @param id the [Float] resources ID.
 * @return [Float]
 * @throws Resources.NotFoundException if the resource is not found.
 */
fun Resources.getFloatCompat(@DimenRes id: Int) = ResourcesCompat.getFloat(this, id)

/**
 * Get [Float] from [Context] (compat).
 * @receiver the current context.
 * @param id the [Typeface] resources ID.
 * @return [Typeface] or null.
 * @throws Resources.NotFoundException if the resource is not found.
 */
fun Context.getFontCompat(@FontRes id: Int) =
    SystemVersion.requireOrNull(SystemVersion.O, ResourcesCompat.getFont(this, id)) { resources.getFont(id) }

/**
 * Get [Drawable] from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the [Drawable] resources ID.
 * @return [Drawable]
 * @throws Resources.NotFoundException if the resource is not found.
 */
fun Context.getDrawableCompat(@DrawableRes id: Int) = resources.getDrawableCompat<Drawable>(id, theme)

/**
 * Get [Drawable] from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the [Drawable] resources ID.
 * @return [T]
 * @throws Resources.NotFoundException if the resource is not found.
 * @throws [IllegalStateException] if [Drawable] type is not [T].
 */
@JvmName("getDrawableCompatTyped")
inline fun <reified T : Drawable> Context.getDrawableCompat(@DrawableRes id: Int) = resources.getDrawableCompat(id, theme)

/**
 * Get [ColorStateList] from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the [ColorStateList] resources ID.
 * @return [ColorStateList]
 * @throws Resources.NotFoundException if the resource is not found.
 */
fun Context.getColorStateListCompat(@ColorRes id: Int) =
    SystemVersion.require(SystemVersion.M, resources.getColorStateListCompat(id, theme)) { getColorStateList(id) }

/**
 * Get color from [Context.getResources] (compat).
 * @receiver the current context.
 * @param id the color resource ID.
 * @return [Int]
 * @throws Resources.NotFoundException if the resource is not found.
 */
fun Context.getColorCompat(@ColorRes id: Int) = SystemVersion.require(SystemVersion.M, resources.getColorCompat(id, theme)) { getColor(id) }

/**
 * Get the resource ID of the attribute resource list
 * from the current theme and passes it through [Boolean].
 *
 * - The final attribute value must be a [Boolean].
 * @receiver the current context.
 * @param id the attribute resources ID.
 * @return [Boolean]
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
 * @throws Resources.NotFoundException if the resource is not found.
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
fun Context.areThemeAttrsIdsValueEquals(@AttrRes firstId: Int, @AttrRes lastId: Int) = when {
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
 * Get the resource ID of the attribute resource list from the current theme.
 *
 * - This function is deprecated, use [areThemeAttrsIdsValueEquals] instead.
 * @see Context.areThemeAttrsIdsValueEquals
 */
@Deprecated(message = "Use areThemeAttrsIdsValueEquals instead.", ReplaceWith("areThemeAttrsIdsValueEquals(firstId, lastId)"))
fun Context.isThemeAttrsIdsValueEquals(@AttrRes firstId: Int, @AttrRes lastId: Int) = areThemeAttrsIdsValueEquals(firstId, lastId)

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
 * @see Resources.getStringArray
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is empty array.
 * @return [Array]<[String]> if no data will return [defValue].
 */
@JvmOverloads
fun TypedArray.getStringArray(@StyleableRes index: Int, defValue: Array<String> = emptyArray()) =
    runCatching { resources.getStringArray(getResourceId(index, -1)) as? Array<String> }.getOrNull() ?: defValue

/**
 * Get int array.
 * @see Resources.getIntArray
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is empty array.
 * @return [Array]<[Int]> if no data will return [defValue].
 */
@JvmOverloads
fun TypedArray.getIntArray(@StyleableRes index: Int, defValue: Array<Int> = emptyArray()) =
    runCatching { resources.getIntArray(getResourceId(index, -1)).toTypedArray() }.getOrNull() ?: defValue

/**
 * Get color, return [defValue] when not has this value.
 * @see TypedArray.getColor
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Int] or null.
 */
@JvmOverloads
fun TypedArray.getColorOrNull(@StyleableRes index: Int, @ColorInt defValue: Int? = null) =
    if (hasValue(index)) getColor(index, defValue ?: Color.TRANSPARENT) else defValue

/**
 * Get [ColorStateList], return [defValue] when not has this value.
 * @see TypedArray.getColorStateList
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [ColorStateList] or null.
 */
@JvmOverloads
fun TypedArray.getColorStateListOrNull(@StyleableRes index: Int, defValue: ColorStateList? = null) =
    if (hasValue(index)) getColorStateList(index) ?: defValue else defValue

/**
 * Get [Int], return [defValue] when not has this value.
 * @see TypedArray.getInteger
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Int] or null.
 */
@JvmOverloads
fun TypedArray.getIntegerOrNull(@StyleableRes index: Int, defValue: Int? = null) =
    if (hasValue(index)) getInteger(index, defValue ?: 0) else defValue

/**
 * Get [Int], return [defValue] when not has this value.
 * @see TypedArray.getInt
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Int] or null.
 */
@JvmOverloads
fun TypedArray.getIntOrNull(@StyleableRes index: Int, defValue: Int? = null) =
    if (hasValue(index)) getInt(index, defValue ?: 0) else defValue

/**
 * Get [CharSequence], return [defValue] when not has this value.
 * @see TypedArray.getText
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [CharSequence] or null.
 */
@JvmOverloads
fun TypedArray.getTextOrNull(@StyleableRes index: Int, defValue: CharSequence? = null) =
    if (hasValue(index)) getText(index) ?: defValue else defValue

/**
 * Get [String], return [defValue] when not has this value.
 * @see TypedArray.getString
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [String] or null.
 */
@JvmOverloads
fun TypedArray.getStringOrNull(@StyleableRes index: Int, defValue: String? = null) =
    if (hasValue(index)) getString(index) ?: defValue else defValue

/**
 * Get [Boolean], return [defValue] when not has this value.
 * @see TypedArray.getBoolean
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Boolean] or null.
 */
@JvmOverloads
fun TypedArray.getBooleanOrNull(@StyleableRes index: Int, defValue: Boolean? = null) =
    if (hasValue(index)) getBoolean(index, defValue ?: false) else defValue

/**
 * Get [Float], return [defValue] when not has this value.
 * @see TypedArray.getFloat
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Float] or null.
 */
@JvmOverloads
fun TypedArray.getFloatOrNull(@StyleableRes index: Int, defValue: Float? = null) =
    if (hasValue(index)) getFloat(index, defValue ?: 0f) else defValue

/**
 * Get dimension, return [defValue] when not has this value.
 * @see TypedArray.getDimension
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Float] or null.
 */
@JvmOverloads
fun TypedArray.getDimensionOrNull(@StyleableRes index: Int, defValue: Float? = null) =
    if (hasValue(index)) getDimension(index, defValue ?: 0f) else defValue

/**
 * Get dimension, return [defValue] when not has this value.
 * @see TypedArray.getDimensionPixelSize
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Int] or null.
 */
@JvmOverloads
fun TypedArray.getDimensionPixelSizeOrNull(@StyleableRes index: Int, defValue: Int? = null) =
    if (hasValue(index)) getDimensionPixelSize(index, defValue ?: 0) else defValue

/**
 * Get dimension, return [defValue] when not has this value.
 * @see TypedArray.getDimensionPixelOffset
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Int] or null.
 */
@JvmOverloads
fun TypedArray.getDimensionPixelOffsetOrNull(@StyleableRes index: Int, defValue: Int? = null) =
    if (hasValue(index)) getDimensionPixelOffset(index, defValue ?: 0) else defValue

/**
 * Get layout dimension, return [defValue] when not has this value.
 * @see TypedArray.getLayoutDimension
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Int] or null.
 */
@JvmOverloads
fun TypedArray.getLayoutDimensionOrNull(@StyleableRes index: Int, defValue: Int? = null) =
    if (hasValue(index)) getLayoutDimension(index, defValue ?: 0) else defValue

/**
 * Get [Drawable], return [defValue] when not has this value.
 * @see TypedArray.getDrawable
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Drawable] or null.
 */
@JvmOverloads
fun TypedArray.getDrawableOrNull(@StyleableRes index: Int, defValue: Drawable? = null) =
    if (hasValue(index)) getDrawable(index) ?: defValue else defValue

/**
 * Get [Typeface], return [defValue] when not has this value.
 * @see TypedArray.getFont
 * @receiver the current typed array from resource.
 * @param index the attribute resources ID array.
 * @param defValue the default value, default is null.
 * @return [Typeface] or null.
 */
@RequiresApi(SystemVersion.O)
@JvmOverloads
fun TypedArray.getFontOrNull(@StyleableRes index: Int, defValue: Typeface? = null) =
    if (hasValue(index)) getFont(index) ?: defValue else defValue

/**
 * - This function is deprecated and no effect and will be removed in the future.
 *
 * - Please use [toPx] or [toDp] and call [TypedArray.getDimension] yourself.
 */
@Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
@Deprecated(message = "Use toPx or toDp and call getDimension yourself.")
fun TypedArray.getDimensionPx(context: Context, @StyleableRes index: Int, @Px defValue: Float) = 0f

/**
 * - This function is deprecated and no effect and will be removed in the future.
 *
 * - Please use [toPx] or [toDp] and call [TypedArray.getDimension] yourself.
 */
@Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER", "DeprecatedCallableAddReplaceWith")
@Deprecated(message = "Use toPx or toDp and call getDimension yourself.")
fun TypedArray.getDimensionPx(view: View, @StyleableRes index: Int, @Px defValue: Float) = 0f

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
     * @throws Resources.NotFoundException if the resource is not found.
     */
    fun get(resources: Resources, @ColorRes id: Int, theme: Theme?) =
        SystemVersion.require(SystemVersion.M, createFromXml(resources, id, theme)) { resources.getColorStateList(id, theme) }

    /**
     * Create a new [ColorStateList] through [ColorStateListInflaterCompat.createFromXml].
     * @param resources the current resource.
     * @param id the [ColorStateList] resource ID.
     * @param theme the given theme.
     * @throws Resources.NotFoundException if the resource is not found.
     */
    private fun createFromXml(resources: Resources, id: Int, theme: Theme?) =
        sColorStateCaches.get(id) ?: runCatching {
            ColorStateListInflaterCompat.createFromXml(resources, resources.getXml(id), theme).also { sColorStateCaches.put(id, it) }
        }.getOrNull() ?: resources.getColorStateList(id)
}
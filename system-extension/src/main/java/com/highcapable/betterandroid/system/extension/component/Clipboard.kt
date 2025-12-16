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
 * This file is created by fankes on 2023/10/31.
 */
@file:Suppress("unused")
@file:JvmName("ClipboardUtils")

package com.highcapable.betterandroid.system.extension.component

import android.app.Dialog
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

/**
 * Clip data item's builder.
 * @param label the clip data visible label.
 */
class ClipDataItemBuilder internal constructor(private val label: CharSequence?) {

    /** The mime types of clip data. */
    private val mimeTypes = mutableSetOf<String>()

    /** The items of clip data. */
    private val dataItems = mutableListOf<ClipData.Item>()

    /**
     * Add a plain text item to clip data.
     * @param text the text to add.
     */
    fun addPlainText(text: CharSequence) {
        mimeTypes.add(ClipDescription.MIMETYPE_TEXT_PLAIN)
        dataItems.add(ClipData.Item(text))
    }

    /**
     * Add a html text item to clip data.
     * @param text the text to add.
     * @param htmlText the html text to add.
     */
    fun addHtmlText(text: CharSequence, htmlText: String) {
        mimeTypes.add(ClipDescription.MIMETYPE_TEXT_HTML)
        dataItems.add(ClipData.Item(text, htmlText))
    }

    /**
     * Add a intent item to clip data.
     * @param intent the intent to add.
     */
    fun addIntent(intent: Intent) {
        mimeTypes.add(ClipDescription.MIMETYPE_TEXT_INTENT)
        dataItems.add(ClipData.Item(intent))
    }

    /**
     * Add a uri item to clip data.
     * @param uri the uri to add.
     * @param resolver the content resolver, use it to get the [uri]'s mime type, default is null.
     */
    @JvmOverloads
    fun addUri(uri: Uri, resolver: ContentResolver? = null) {
        // Use [ClipData.newUri] to make a new clip data for getting the uri's mime type.
        val description = resolver?.let { ClipData.newUri(it, label, uri).description }

        if (description != null && description.mimeTypeCount > 0)
            for (i in 0 until description.mimeTypeCount)
                mimeTypes.add(description.getMimeType(i))
        else mimeTypes.add(ClipDescription.MIMETYPE_TEXT_URILIST)

        dataItems.add(ClipData.Item(uri))
    }

    /**
     * Add a text item to clip data.
     *
     * - This function is deprecated, use [addPlainText] instead.
     */
    @Deprecated(message = "Use addPlainText instead.", ReplaceWith("addPlainText(text)"))
    fun addText(text: CharSequence) = addPlainText(text)

    /**
     * Get the clip data item result.
     * @return [Pair]<[Array]<[String]>, [MutableList]<[ClipData.Item]>>
     */
    internal fun build() = mimeTypes.toTypedArray() to dataItems
}

/**
 * Create a clip data.
 * @param label the clip data visible label, default is null.
 * @param builder the [ClipDataItemBuilder] builder body.
 * @throws IllegalStateException if no clip data item provided.
 */
@JvmOverloads
fun ClipData(label: CharSequence? = null, builder: ClipDataItemBuilder.() -> Unit): ClipData {
    val data = ClipDataItemBuilder(label).apply(builder).build()
    require(data.second.isNotEmpty()) { "ClipData must have at least one item." }

    return ClipData(label, data.first, data.second[0]).apply { data.second.drop(1).forEach { addItem(it) } }
}

/**
 * Get the clip data item list.
 * @receiver [ClipData]
 * @return [List]<[ClipData.Item]>
 */
fun ClipData.listOfItems() = if (itemCount > 0)
    (0 until itemCount).map { getItemAt(it) }
else emptyList()

/**
 * Copy to clipboard.
 * @receiver [ClipboardManager]
 * @param clipData the clip data to copy.
 */
fun ClipboardManager.copy(clipData: ClipData) = setPrimaryClip(clipData)

/**
 * Copy to clipboard.
 * @receiver [ClipboardManager]
 * @param label the clip data visible label, default is null.
 * @param builder the [ClipDataItemBuilder] builder body.
 */
@JvmOverloads
fun ClipboardManager.copy(label: CharSequence? = null, builder: ClipDataItemBuilder.() -> Unit) =
    copy(ClipData(label, builder))

/**
 * Copy plain text to clipboard.
 * @receiver [ClipboardManager]
 * @param text the text to copy.
 * @param label the clip data visible label, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(text: CharSequence, label: CharSequence? = null) =
    copy(label = label) { addPlainText(text) }

/**
 * Copy html text to clipboard.
 * @receiver [ClipboardManager]
 * @param text the text to copy.
 * @param htmlText the html text to copy.
 * @param label the clip data visible label, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(text: CharSequence, htmlText: String, label: CharSequence? = null) =
    copy(label = label) { addHtmlText(text, htmlText) }

/**
 * Copy intent to clipboard.
 * @receiver [ClipboardManager]
 * @param intent the intent to copy.
 * @param label the clip data visible label, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(intent: Intent, label: CharSequence? = null) =
    copy(label = label) { addIntent(intent) }

/**
 * Copy uri to clipboard.
 * @receiver [ClipboardManager]
 * @param uri the uri to copy.
 * @param label the clip data visible label, default is null.
 * @param resolver the content resolver, use it to get the [uri]'s mime type, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(uri: Uri, label: CharSequence? = null, resolver: ContentResolver? = null) =
    copy(label) { addUri(uri, resolver) }

/**
 * Get clipboard manager.
 * @receiver the current context.
 * @return [ClipboardManager]
 * @throws IllegalStateException if clipboard manager is unavailable.
 */
val Context.clipboardManager get() = getSystemService<ClipboardManager>() ?: error("ClipboardManager is unavailable.")

/**
 * Copy text to clipboard.
 *
 * - This function is deprecated, use [Context.clipboardManager] instead.
 * @see Context.clipboardManager
 * @see ClipboardManager.copy
 */
@Deprecated(message = "Use Context.clipboardManager instead.", ReplaceWith("clipboardManager.copy(text, label)"))
@JvmOverloads
fun Context.copyToClipboard(text: CharSequence, label: CharSequence? = null) = clipboardManager.copy(text, label)

/**
 * Copy text to clipboard.
 *
 * - This function is deprecated, use [Context.clipboardManager] instead.
 * @see Context.clipboardManager
 * @see ClipboardManager.copy
 */
@Deprecated(message = "Use Context.clipboardManager instead.", ReplaceWith("context?.clipboardManager?.copy(text, label)"))
@JvmOverloads
fun Fragment.copyToClipboard(text: CharSequence, label: CharSequence? = null) = context?.clipboardManager?.copy(text, label)

/**
 * Copy text to clipboard.
 *
 * - This function is deprecated, use [Context.clipboardManager] instead.
 * @see Context.clipboardManager
 * @see ClipboardManager.copy
 */
@Deprecated(message = "Use Context.clipboardManager instead.", ReplaceWith("context?.clipboardManager?.copy(text, label)"))
@JvmOverloads
fun View.copyToClipboard(text: CharSequence, label: CharSequence? = null) = context?.clipboardManager?.copy(text, label)

/**
 * Copy text to clipboard.
 *
 * - This function is deprecated, use [Context.clipboardManager] instead.
 * @see Context.clipboardManager
 * @see ClipboardManager.copy
 */
@Deprecated(message = "Use Context.clipboardManager instead.", ReplaceWith("context.clipboardManager.copy(text, label)"))
@JvmOverloads
fun Dialog.copyToClipboard(text: CharSequence, label: CharSequence? = null) = context.clipboardManager.copy(text, label)
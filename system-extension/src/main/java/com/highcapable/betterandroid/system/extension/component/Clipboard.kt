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
 * Clip data builder.
 */
class ClipDataBuilder {

    /** The mime types of clip data. */
    private val mimeTypes = mutableSetOf<String>()

    /** The items of clip data. */
    private val dataItems = mutableListOf<ClipData.Item>()

    /**
     * Add a plain text item to clip data.
     * @param text the text to add.
     * @return [ClipDataBuilder]
     */
    fun addPlainText(text: CharSequence) = apply {
        this.mimeTypes.add(ClipDescription.MIMETYPE_TEXT_PLAIN)
        this.dataItems.add(ClipData.Item(text))
    }

    /**
     * Add a html text item to clip data.
     * @param text the text to add.
     * @param htmlText the HTML text to add.
     * @return [ClipDataBuilder]
     */
    fun addHtmlText(text: CharSequence, htmlText: String) = apply {
        this.mimeTypes.add(ClipDescription.MIMETYPE_TEXT_HTML)
        this.dataItems.add(ClipData.Item(text, htmlText))
    }

    /**
     * Add a intent item to clip data.
     * @param intent the intent to add.
     * @return [ClipDataBuilder]
     */
    fun addIntent(intent: Intent) = apply {
        this.mimeTypes.add(ClipDescription.MIMETYPE_TEXT_INTENT)
        this.dataItems.add(ClipData.Item(intent))
    }

    /**
     * Add a uri item to clip data.
     * @param uri the uri to add.
     * @param mimeTypes the mime types to add, if not provided, it will be set to [ClipDescription.MIMETYPE_TEXT_URILIST].
     * @return [ClipDataBuilder]
     */
    fun addUri(uri: Uri, vararg mimeTypes: String) = apply {
        this.mimeTypes.addAll(mimeTypes.filter { it.isNotBlank() }.ifEmpty { listOf(ClipDescription.MIMETYPE_TEXT_URILIST) })
        this.dataItems.add(ClipData.Item(uri))
    }

    /**
     * Add a uri item to clip data.
     * @param uri the uri to add.
     * @param mimeTypes the mime types to add, if not provided, it will be set to [ClipDescription.MIMETYPE_TEXT_URILIST].
     * @return [ClipDataBuilder]
     */
    @JvmOverloads
    fun addUri(uri: Uri, mimeTypes: List<String> = emptyList()) = addUri(uri, *mimeTypes.toTypedArray())

    /**
     * Add a uri item to clip data.
     *
     * - This function is deprecated, use [addUri] with [Uri.resolveMimeTypes] instead.
     */
    @Deprecated(
        message = "Use addUri with Uri.resolveMimeTypes instead.",
        ReplaceWith(
            expression = "addUri(uri, resolver?.let { uri.resolveMimeTypes(it) } ?: listOf(ClipDescription.MIMETYPE_TEXT_URILIST))",
            imports = arrayOf("android.content.ClipDescription")
        )
    )
    fun addUri(uri: Uri, resolver: ContentResolver?) =
        addUri(uri, resolver?.let { uri.resolveMimeTypes(it) } ?: listOf(ClipDescription.MIMETYPE_TEXT_URILIST))

    /**
     * Add a text item to clip data.
     *
     * - This function is deprecated, use [addPlainText] instead.
     */
    @Deprecated(message = "Use addPlainText instead.", ReplaceWith("addPlainText(text)"))
    fun addText(text: CharSequence) = addPlainText(text)

    /**
     * Build the clip data result.
     * @param label the clip data visible label, default is null.
     * @return [ClipData]
     */
    fun build(label: CharSequence? = null): ClipData {
        require(dataItems.isNotEmpty()) {
            "ClipData must have at least one item."
        }

        return ClipData(label, mimeTypes.toTypedArray(), dataItems.first()).apply {
            for (index in 1 until dataItems.size) addItem(dataItems[index])
        }
    }
}

/**
 * Create a clip data.
 * @param label the clip data visible label, default is null.
 * @param builder the [ClipDataBuilder] builder body.
 * @throws IllegalStateException if no clip data item provided.
 */
inline fun ClipData(label: CharSequence? = null, builder: ClipDataBuilder.() -> Unit) =
    ClipDataBuilder().apply(builder).build(label)

/**
 * Get the clip data item list.
 * @receiver [ClipData]
 * @return [List]<[ClipData.Item]>
 */
fun ClipData.listOfItems() = if (itemCount > 0)
    (0 until itemCount).map { getItemAt(it) }
else emptyList()

/**
 * Get the primary clip item.
 * @receiver [ClipboardManager]
 * @param index the clip item index.
 * @return [ClipData.Item]
 */
fun ClipboardManager.primaryClipItems(index: Int) = primaryClip?.getItemAt(index) ?: error("ClipData item index out of bounds.")

/**
 * Get the primary clip item or null.
 * @receiver [ClipboardManager]
 * @param index the clip item index.
 * @return [ClipData.Item] or null.
 */
fun ClipboardManager.primaryClipItemsOrNull(index: Int) = runCatching { primaryClipItems(index) }.getOrNull()

/**
 * Get the first primary clip item.
 * @receiver [ClipboardManager]
 * @return [ClipData.Item]
 */
val ClipboardManager.firstPrimaryClipItem get() = primaryClipItems(0)

/**
 * Get the first primary clip item or null.
 * @receiver [ClipboardManager]
 * @return [ClipData.Item] or null.
 */
val ClipboardManager.firstPrimaryClipItemOrNull get() = primaryClipItemsOrNull(0)

/**
 * Resolve the mime type list.
 * @receiver [Uri]
 * @param resolver the content resolver, use it to get the [Uri]'s mime type.
 * @return [List]<[String]>
 */
fun Uri.resolveMimeTypes(resolver: ContentResolver): List<String> {
    val description = ClipData.newUri(resolver, null, this).description

    return if (description.mimeTypeCount > 0)
        (0 until description.mimeTypeCount).map { description.getMimeType(it) }
    else emptyList()
}

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
 * @param builder the [ClipDataBuilder] builder body.
 */
@JvmOverloads
fun ClipboardManager.copy(label: CharSequence? = null, builder: ClipDataBuilder.() -> Unit) =
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
 * @param htmlText the HTML text to copy.
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
 * @param mimeTypes the mime types to copy, if not provided, it will be set to [ClipDescription.MIMETYPE_TEXT_URILIST].
 * @param label the clip data visible label.
 */
@JvmOverloads
fun ClipboardManager.copy(uri: Uri, vararg mimeTypes: String, label: CharSequence? = null) =
    copy(label) { addUri(uri, *mimeTypes) }

/**
 * Copy uri to clipboard.
 * @receiver [ClipboardManager]
 * @param uri the uri to copy.
 * @param mimeTypes the mime types to copy, if not provided, it will be set to [ClipDescription.MIMETYPE_TEXT_URILIST].
 * @param label the clip data visible label.
 */
@JvmOverloads
fun ClipboardManager.copy(uri: Uri, mimeTypes: List<String> = emptyList(), label: CharSequence? = null) =
    copy(label) { addUri(uri, mimeTypes) }

/**
 * Get clipboard manager.
 * @receiver the current context.
 * @return [ClipboardManager]
 * @throws IllegalStateException if clipboard manager is unavailable.
 */
val Context.clipboardManager get() = getSystemService<ClipboardManager>() ?: error("ClipboardManager is unavailable.")

/**
 * Copy uri to clipboard.
 *
 * - This function is deprecated, use [copy] with [Uri.resolveMimeTypes] instead.
 */
@Deprecated(
    message = "Use copy with Uri.resolveMimeTypes instead.",
    ReplaceWith(
        expression = "copy(uri, resolver?.let { uri.resolveMimeTypes(it) } ?: listOf(ClipDescription.MIMETYPE_TEXT_URILIST), label)",
        imports = arrayOf("android.content.ClipDescription")
    )
)
fun ClipboardManager.copy(uri: Uri, label: CharSequence? = null, resolver: ContentResolver?) =
    copy(uri, resolver?.let { uri.resolveMimeTypes(it) } ?: listOf(ClipDescription.MIMETYPE_TEXT_URILIST), label)

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
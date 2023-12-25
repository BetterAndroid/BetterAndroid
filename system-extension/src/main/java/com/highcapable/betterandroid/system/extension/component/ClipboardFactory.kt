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
 * This file is created by fankes on 2023/10/31.
 */
@file:Suppress("unused")
@file:JvmName("ClipboardUtils")

package com.highcapable.betterandroid.system.extension.component

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.highcapable.yukireflection.factory.current

/**
 * Clip data item's builder.
 */
class ClipDataItemBuilder internal constructor() {

    /** The items of clip data. */
    private val dataItems = mutableListOf<ClipData.Item>()

    /**
     * Add a text item to clip data.
     * @param text the text to add.
     */
    fun addText(text: CharSequence) {
        dataItems.add(ClipData.Item(text))
    }

    /**
     * Add a html text item to clip data.
     * @param text the text to add.
     * @param htmlText the html text to add.
     */
    fun addHtmlText(text: CharSequence, htmlText: String) {
        dataItems.add(ClipData.Item(text, htmlText))
    }

    /**
     * Add a intent item to clip data.
     * @param intent the intent to add.
     */
    fun addIntent(intent: Intent) {
        dataItems.add(ClipData.Item(intent))
    }

    /**
     * Add a uri item to clip data.
     * @param uri the uri to add.
     */
    fun addUri(uri: Uri) {
        dataItems.add(ClipData.Item(uri))
    }

    /**
     * Build clip data items.
     * @return [MutableList]<[ClipData.Item]>
     */
    internal fun build() = dataItems
}

/**
 * Create a clip data.
 * @param mimeTypes the mime types of clip data.
 * @param label the clip data visible label, default is null.
 * @param initiate the [ClipDataItemBuilder] builder body.
 * @throws IllegalStateException if no clip data item provided.
 */
@JvmOverloads
fun ClipData(
    vararg mimeTypes: String,
    label: CharSequence? = null,
    initiate: ClipDataItemBuilder.() -> Unit
): ClipData {
    val dataItems = ClipDataItemBuilder().apply(initiate).build()
    require(dataItems.isNotEmpty()) { "ClipData must have at least one item." }
    return ClipData(label, mimeTypes, dataItems[0]).apply { dataItems.drop(1).forEach { addItem(it) } }
}

/**
 * Copy text to clipboard.
 * @receiver [ClipboardManager]
 * @param text the text to copy.
 * @param label the clip data visible label, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(text: CharSequence, label: CharSequence? = null) =
    copy(ClipData.newPlainText(label, text))

/**
 * Copy html text to clipboard.
 * @receiver [ClipboardManager]
 * @param text the text to copy.
 * @param htmlText the html text to copy.
 * @param label the clip data visible label, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(text: CharSequence, htmlText: String, label: CharSequence? = null) =
    copy(ClipData.newHtmlText(label, text, htmlText))

/**
 * Copy intent to clipboard.
 * @receiver [ClipboardManager]
 * @param intent the intent to copy.
 * @param label the clip data visible label, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(intent: Intent, label: CharSequence? = null) =
    copy(ClipData.newIntent(label, intent))

/**
 * Copy uri to clipboard.
 * @receiver [ClipboardManager]
 * @param uri the uri to copy.
 * @param label the clip data visible label, default is null.
 */
@JvmOverloads
fun ClipboardManager.copy(uri: Uri, label: CharSequence? = null) {
    val resolverFromManager = current(ignored = true)
        .field { name = "mContext" }
        .cast<Context>()?.contentResolver
    copy(ClipData.newUri(resolverFromManager, label, uri))
}

/**
 * Copy to clipboard.
 * @receiver [ClipboardManager]
 * @param clipData the clip data to copy.
 */
fun ClipboardManager.copy(clipData: ClipData) = setPrimaryClip(clipData)

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
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
 * This file is created by fankes on 2023/10/26.
 */
@file:Suppress("unused")
@file:JvmName("BitmapUtils")

package com.highcapable.betterandroid.ui.extension.graphics

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import com.highcapable.betterandroid.ui.extension.component.base.toHexResourceId
import com.highcapable.betterandroid.ui.extension.graphics.base.BitmapBlurFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import kotlin.math.sqrt

/**
 * Decode a file into a bitmap.
 *
 * If cannot be decoded into a bitmap, will throw an exception.
 *
 * You can see [File.decodeToBitmapOrNull].
 * @receiver [File]
 * @param opts the [BitmapFactory.Options], default is null.
 * @return [Bitmap]
 * @throws IllegalStateException if the bitmap decode failed.
 */
fun File.decodeToBitmap(opts: BitmapFactory.Options? = null) =
    decodeToBitmapOrNull(opts) ?: error("Unable to decode file $absolutePath of a bitmap.")

/**
 * Decode a stream into a bitmap.
 *
 * If cannot be decoded into a bitmap, will throw an exception.
 *
 * You can see [InputStream.decodeToBitmapOrNull].
 * @receiver [InputStream]
 * @param outPadding set the padding of this bitmap, default is null.
 * @param opts the [BitmapFactory.Options], default is null.
 * @return [Bitmap]
 * @throws IllegalStateException if the bitmap decode failed.
 */
fun InputStream.decodeToBitmap(outPadding: Rect? = null, opts: BitmapFactory.Options? = null) =
    decodeToBitmapOrNull(outPadding, opts) ?: error("Unable to decode stream $this of a bitmap.")

/**
 * Decode a byte array into a bitmap.
 *
 * If cannot be decoded into a bitmap, will throw an exception.
 *
 * You can see [ByteArray.decodeToBitmapOrNull].
 * @receiver [ByteArray]
 * @param offset the current offset, default is 0.
 * @param size the current data length, default is [ByteArray.size].
 * @return [Bitmap]
 * @throws IllegalStateException if the bitmap decode failed.
 */
fun ByteArray.decodeToBitmap(offset: Int = 0, size: Int = this.size) =
    decodeToBitmapOrNull(offset, size) ?: error("Unable to decode byte array $this of a bitmap.")

/**
 * Create a bitmap from resources.
 *
 * If cannot be decoded into a bitmap, will throw an exception.
 *
 * You can see [Resources.createBitmapOrNull].
 * @receiver [Resources]
 * @param resId the drawable resource id.
 * @param opts the [BitmapFactory.Options], default is null.
 * @return [Bitmap]
 * @throws IllegalStateException if the bitmap decode failed.
 */
fun Resources.createBitmap(@DrawableRes resId: Int, opts: BitmapFactory.Options? = null) =
    createBitmapOrNull(resId, opts) ?: error("Unable to create a bitmap from resource ID ${resId.toHexResourceId()}.")

/**
 * Decode a file into a bitmap.
 *
 * If cannot be decoded into a bitmap, the function returns null.
 * @receiver [File]
 * @param opts the [BitmapFactory.Options], default is null.
 * @return [Bitmap] or null.
 */
fun File.decodeToBitmapOrNull(opts: BitmapFactory.Options? = null) =
    runCatching { BitmapFactory.decodeFile(absolutePath, opts) }.getOrNull()

/**
 * Decode a stream into a bitmap.
 *
 * If cannot be decoded into a bitmap, the function returns null.
 * @receiver [InputStream]
 * @param outPadding set the padding of this bitmap, default is null.
 * @param opts the [BitmapFactory.Options], default is null.
 * @return [Bitmap] or null.
 */
fun InputStream.decodeToBitmapOrNull(outPadding: Rect? = null, opts: BitmapFactory.Options? = null) =
    runCatching { BitmapFactory.decodeStream(this, outPadding, opts) }.getOrNull()

/**
 * Decode a byte array into a bitmap.
 *
 * If cannot be decoded into a bitmap, the function returns null.
 * @receiver [ByteArray]
 * @param offset the current offset, default is 0.
 * @param size the current data length, default is [ByteArray.size].
 * @return [Bitmap] or null.
 */
fun ByteArray.decodeToBitmapOrNull(offset: Int = 0, size: Int = this.size) =
    runCatching { BitmapFactory.decodeByteArray(this, offset, size) }.getOrNull()

/**
 * Create a bitmap from resources.
 *
 * If cannot be decoded into a bitmap, the function returns null.
 * @receiver [Resources]
 * @param resId the drawable resource id.
 * @param opts the [BitmapFactory.Options], default is null.
 * @return [Bitmap] or null.
 */
fun Resources.createBitmapOrNull(@DrawableRes resId: Int, opts: BitmapFactory.Options? = null) =
    runCatching { BitmapFactory.decodeResource(this, resId, opts) }.getOrNull()

/**
 * Save the bitmap to file.
 * @receiver [File]
 * @param bitmap the current bitmap.
 * @param format the bitmap format, defaul is [Bitmap.CompressFormat.PNG].
 * @param quality the quality, default is 100.
 */
fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100) {
    createNewFile()
    outputStream().apply {
        bitmap.compress(format, quality, this)
        flush()
        close()
    }
}

/**
 * Blur the bitmap using [BitmapBlurFactory].
 *
 * In Android 12 and above, you can use the blur solution provided by the system.
 * @receiver [Bitmap]
 * @param radius the blur radius.
 * @return [Bitmap]
 */
fun Bitmap.blur(radius: Int) = BitmapBlurFactory.process(this, radius)

/**
 * Convert the bitmap to a round corner bitmap.
 * @receiver [Bitmap]
 * @param radius the round corner (px).
 * @param config the config, default is [Bitmap.Config.ARGB_8888].
 * @return [Bitmap]
 */
fun Bitmap.round(@Px radius: Float, config: Bitmap.Config = Bitmap.Config.ARGB_8888) =
    runCatching {
        Bitmap.createBitmap(width, height, config).also { outBitmap ->
            val canvas = Canvas(outBitmap)
            val paint = Paint()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.WHITE
            canvas.drawRoundRect(RectF(Rect(0, 0, width, height)), radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(this, Rect(0, 0, width, height), Rect(0, 0, width, height), paint)
        }
    }.getOrNull() ?: this

/**
 * Compress the bitmap to reduce its size.
 * @receiver [Bitmap]
 * @param maxSize the allowed maximum size.
 * @param format the bitmap format, default is [Bitmap.CompressFormat.PNG].
 * @param quality the quality, default is 100.
 * @return [Bitmap]
 */
fun Bitmap.compress(maxSize: Float, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100): Bitmap {
    var outBitmap = this
    val baos = ByteArrayOutputStream()
    outBitmap.compress(format, quality, baos)
    val baosByte = baos.toByteArray()
    val mid = baosByte.size / 1024.toDouble()
    if (mid > maxSize) {
        val size = mid / maxSize
        val floatWidth = (outBitmap.width / sqrt(size)).toFloat()
        val floatHeight = (outBitmap.height / sqrt(size)).toFloat()
        outBitmap = outBitmap.zoom(floatWidth, floatHeight)
    }; return outBitmap
}

/**
 * Reduce the bitmap width and height.
 * @receiver [Bitmap]
 * @param multiple the reduce multiple, default is 2.
 * @return [Bitmap]
 */
fun Bitmap.reduce(multiple: Int = 2): Bitmap {
    if (multiple <= 1) return this
    val floatWidth = (width / multiple).toFloat()
    val floatHeight = (height / multiple).toFloat()
    return zoom(floatWidth, floatHeight)
}

/**
 * Zoom the bitmap width and height.
 * @receiver [Bitmap]
 * @param newWidth the new width of the bitmap.
 * @param newHeight the new height of the bitmap.
 * @return [Bitmap]
 */
fun Bitmap.zoom(newWidth: Float, newHeight: Float): Bitmap {
    val width = this.width.toFloat()
    val height = this.height.toFloat()
    val matrix = Matrix()
    val scaleWidth = newWidth / width
    val scaleHeight = newHeight / height
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(this, 0, 0, width.toInt(), height.toInt(), matrix, true)
}
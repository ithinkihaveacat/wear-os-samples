/*
 * Copyright 2022 The Android Open Source Project
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
 */
package com.example.wear.tiles.messaging

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.nio.ByteBuffer

suspend fun ImageLoader.loadAvatar(context: Context, contact: Contact): ImageResource? {
    return when (val source = contact.avatarSource) {
        is AvatarSource.Network -> {
            val request = ImageRequest.Builder(context).data(source.url).size(300).allowRgb565(true).build()
            val response = execute(request)
            return when (response) {
                is SuccessResult -> {
                    (response.drawable as? BitmapDrawable)?.bitmap?.toImageResource()
                }
                is ErrorResult -> {
                    Log.d(
                        "ImageLoader",
                        "Error loading image ${source}: ${response.throwable.message}",
                    )
                    null
                }
            }
        }
        is AvatarSource.Resource ->
            ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(source.resourceId)
                        .build()
                )
                .build()
        is AvatarSource.None -> null
    }
}

fun Bitmap.toImageResource(): ImageResource {
    val safeBitmap = this.copy(Bitmap.Config.RGB_565, false)

    val byteBuffer = ByteBuffer.allocate(safeBitmap.byteCount)
    safeBitmap.copyPixelsToBuffer(byteBuffer)
    val bytes: ByteArray = byteBuffer.array()

    return ImageResource.Builder()
        .setInlineResource(
            ResourceBuilders.InlineImageResource.Builder()
                .setData(bytes)
                .setWidthPx(this.width)
                .setHeightPx(this.height)
                .setFormat(ResourceBuilders.IMAGE_FORMAT_RGB_565)
                .build()
        )
        .build()
}

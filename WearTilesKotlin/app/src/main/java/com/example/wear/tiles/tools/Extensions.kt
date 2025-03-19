package com.example.wear.tiles.tools

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import androidx.wear.protolayout.ResourceBuilders.Resources
import com.example.wear.tiles.messaging.bitmapToImageResource

fun Resources.Builder.addIdToImageMapping(id: String, @DrawableRes resId: Int): Resources.Builder =
    addIdToImageMapping(
        id,
        ImageResource.Builder()
            .setAndroidResourceByResId(
                ResourceBuilders.AndroidImageResourceByResId.Builder().setResourceId(resId).build())
            .build())

fun Resources.Builder.addIdToImageMapping(id: String, bitmap: Bitmap): Resources.Builder =
    addIdToImageMapping(id, bitmapToImageResource(bitmap))

@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.remote.creation.ExperimentalRemoteCreationApi
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.wear.compose.remote.material3.RemoteImage
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

class HelloWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = HelloWidget()
}

class HelloWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        Log.d("HelloWidget", "provideWidgetData")
        return WearWidgetDocument(backgroundColor = Color.Black) {
            ImageTestWidgetContent(context)
        }
    }
}

@SuppressLint("RestrictedApi")
@RemoteComposable
@Composable
fun ImageTestWidgetContent(context: Context) {
    // Decoding distinct images once to avoid deduplication and redundant decoding
    val imageBitmaps = listOf(
        R.drawable.ali,
        R.drawable.photo_14,
        R.drawable.photo_17,
        R.drawable.ali,
        R.drawable.photo_14,
        R.drawable.photo_17,
        R.drawable.photo_14,
        R.drawable.photo_17
    ).map { resId ->
        BitmapFactory.decodeResource(context.resources, resId).asImageBitmap()
    }

    RemoteMaterialTheme {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            imageBitmaps.forEachIndexed { index, imageBitmap ->
                RemoteImage(
                    bitmap = imageBitmap,
                    contentDescription = "Test Image $index".rs,
                    contentScale = ContentScale.Fit,
                    modifier = RemoteModifier.size(60.rdp)
                )
            }
        }
    }
}
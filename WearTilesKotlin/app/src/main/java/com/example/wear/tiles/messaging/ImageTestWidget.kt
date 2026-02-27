package com.example.wear.tiles.messaging

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams
import androidx.wear.compose.remote.material3.RemoteImage
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import com.example.wear.tiles.R

@SuppressLint("RestrictedApi")
class ImageTestWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = ImageTestWidget()
}

@SuppressLint("RestrictedApi")
class ImageTestWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        Log.wtf("BugReportMarker", "START_REPRO: Taylor Unscaled")
        val result = WearWidgetDocument(backgroundColor = Color.Black) {
            ImageTestWidgetContent(context)
        }
        Log.wtf("BugReportMarker", "END_REPRO")
        return result
    }
}

@SuppressLint("RestrictedApi")
@RemoteComposable
@Composable
fun ImageTestWidgetContent(context: Context) {
    // Decoding distinct images once to avoid deduplication and redundant decoding
    val imageBitmaps = listOf(
        R.drawable.taylor,
        R.drawable.ali,
        R.drawable.photo_01,
        R.drawable.photo_16
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

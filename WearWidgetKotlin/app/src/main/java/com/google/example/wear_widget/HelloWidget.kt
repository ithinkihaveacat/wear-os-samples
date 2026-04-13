
package com.google.example.wear_widget

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.remote.creation.ExperimentalRemoteCreationApi
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.core.WearWidgetParams
import androidx.glance.wear.color
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.layout.RemoteImage
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
        return WearWidgetDocument(background = WearWidgetBrush.color(Color(0xFFFFF9C4).rc)) {
            ImageTestWidgetContent(context)
        }
    }
}

@RemoteComposable
@Composable
fun ImageTestWidgetContent(context: Context) {
    // Decoding only one image to be safe
    val imageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ali).asImageBitmap()

    RemoteMaterialTheme {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteImage(
                bitmap = imageBitmap,
                contentDescription = "Test Image".rs,
                contentScale = ContentScale.Fit,
                modifier = RemoteModifier.size(100.rdp)
            )
        }
    }
}
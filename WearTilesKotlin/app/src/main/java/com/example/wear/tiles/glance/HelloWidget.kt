package com.example.wear.tiles.glance

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams

class HelloWidget : GlanceWearWidget() {
  override suspend fun provideWidgetData(context: Context, params: WearWidgetParams) =
    WearWidgetDocument { HelloWidgetContent() }
}

@SuppressLint("RestrictedApi")
@RemoteComposable
@Composable
fun HelloWidgetContent() {
  RemoteMaterialTheme {
    RemoteBox(
      modifier = RemoteModifier.fillMaxSize().background(RemoteColor(Color.Red)),
      horizontalAlignment = RemoteAlignment.CenterHorizontally,
      verticalArrangement = RemoteArrangement.Center,
    ) {
      RemoteText(text = "Hello RemoteCompose!", color = RemoteColor(Color.White))
    }
  }
}

class HelloWidgetService() : GlanceWearWidgetService() {
  override val widget: GlanceWearWidget = HelloWidget()
}

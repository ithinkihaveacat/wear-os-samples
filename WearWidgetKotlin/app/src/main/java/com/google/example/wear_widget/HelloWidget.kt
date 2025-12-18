@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.creation.ExperimentalRemoteCreationApi
import androidx.compose.remote.creation.compose.painter.painterRemoteColor
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.profile.RcPlatformProfiles
import androidx.compose.remote.tooling.preview.RemotePreview
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices

class HelloWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = HelloWidget()
}

class HelloWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData =
        WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Blue)) {
            HelloWidgetContent()
        }
}

@RemoteComposable
@Composable
fun HelloWidgetContent() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(text = stringResource(R.string.hello), color = RemoteColor(Color.White))
    }
}

@OptIn(ExperimentalRemoteCreationApi::class)
@WearPreviewDevices
@Composable
fun HelloWidgetPreview() {
    val content: @Composable @RemoteComposable () -> Unit = { HelloWidgetContent() }
    RemotePreview(RcPlatformProfiles.WEAR_WIDGETS, content)
}

@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rsp
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.core.WearWidgetParams

@SuppressLint("RestrictedApi")
class ForceUpdateService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = ForceUpdateWidget()
}

@SuppressLint("RestrictedApi")
class ForceUpdateWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val count = context.getCounterState().count
        return WearWidgetDocument(backgroundColor = Color.Black) {
            ForceUpdateWidgetContent(count)
        }
    }
}

@SuppressLint("RestrictedApi")
@RemoteComposable
@Composable
fun ForceUpdateWidgetContent(count: Int) {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            RemoteText(text = "Favorite Number".rs, color = Color.Gray.rc, fontSize = 12.rsp)
            RemoteText(text = count.toString().rs, color = Color.White.rc, fontSize = 40.rsp)
        }
    }
}

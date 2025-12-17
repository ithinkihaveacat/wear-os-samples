@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.creation.compose.capture.painter.painterRemoteColor
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.asRdp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams

class WidgetCatalogService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = WidgetCatalog()
}

class WidgetCatalog : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData =
        WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Black)) {
            BoxSample3()
        }
}

@RemoteComposable
@Composable
fun BoxSample1() {
    // Simple Box with background color and centered text
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.DarkGray),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(text = "Box Sample 1", color = RemoteColor(Color.White))
    }
}

@RemoteComposable
@Composable
fun BoxSample2() {
    // Box with padding and border
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().padding(20.dp).border(width = 2.dp.asRdp(), color = Color.Red),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(text = "Box Sample 2\n(Border & Padding)", color = RemoteColor(Color.White), textAlign = TextAlign.Center)
    }
}

@RemoteComposable
@Composable
fun BoxSample3() {
    // Box with different alignment (BottomEnd)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Blue),
        horizontalAlignment = RemoteAlignment.End,
        verticalArrangement = RemoteArrangement.Bottom,
    ) {
        RemoteText(
            modifier = RemoteModifier.padding(10.dp),
            text = "Box Sample 3\n(Bottom End)",
            color = RemoteColor(Color.Yellow),
            textAlign = TextAlign.End
        )
    }
}

@RemoteComposable
@Composable
fun TextSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color(0xFF006400)),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText(
                text = "TextSample1",
                color = RemoteColor(Color.White),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            RemoteText(
                text = "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.",
                color = RemoteColor(Color.LightGray),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            RemoteText(
                text = "Version 1.0",
                color = RemoteColor(Color.Cyan),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}


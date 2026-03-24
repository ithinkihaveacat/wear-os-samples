@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.core.WearWidgetParams
import com.google.example.wear_widget.widget.*

import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.color
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.compose.remote.creation.compose.state.rc
import androidx.wear.compose.material3.ColorScheme

class WidgetCatalogService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = WidgetCatalog()
}

class WidgetCatalog : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val state = context.getWidgetCatalogState()
        Log.d("WidgetCatalog", "provideWidgetData: layoutName='${state.layoutName}'")

        val localColorScheme = ColorScheme()
        val remoteColorScheme = RemoteColorScheme(localColorScheme)
        
        return WearWidgetDocument(
            background = WearWidgetBrush.color(localColorScheme.primaryContainer.rc)
        ) {
            RemoteMaterialTheme(colorScheme = remoteColorScheme) {
                // Hardcoded to TaskSample per instructions
                TaskSample()
            }
        }
    }
}

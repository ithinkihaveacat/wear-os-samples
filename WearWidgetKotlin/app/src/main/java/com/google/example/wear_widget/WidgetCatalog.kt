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
import androidx.compose.remote.creation.compose.state.RemoteColor
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
        val dynamicBg = RemoteColor.createNamedRemoteColor("WearM3.primaryContainer", localColorScheme.primaryContainer)
        
        return WearWidgetDocument(
            background = WearWidgetBrush.color(dynamicBg)
        ) {
            RemoteMaterialTheme(colorScheme = remoteColorScheme) {
                when (state.layoutName) {
                    "TaskSample" -> TaskSample()
                    "SemanticStyleWorkaroundSample" -> SemanticStyleWorkaroundSample()
                    "CanvasSample3" -> CanvasSample3()
                    "SystemThemeComparisonSample" -> SystemThemeComparisonSample()
                    "SystemThemeSample" -> SystemThemeSample()
                    "CustomThemeSample" -> CustomThemeSample()
                    "AustralianThemeSample" -> AustralianThemeSample()
                    "BoxSample1" -> BoxSample1()
                    "BoxSample2" -> BoxSample2()
                    "BoxSample3" -> BoxSample3()
                    "TextSample1" -> TextSample1()
                    "TextSample1WithMargin" -> TextSample1WithMargin()
                    "RowSample1" -> RowSample1()
                    "RowSample2" -> RowSample2()
                    "CollapsibleColumnSample1" -> CollapsibleColumnSample1()
                    "ButtonSample1" -> ButtonSample1()
                    "ButtonSample2" -> ButtonSample2()
                    "ButtonSample3" -> ButtonSample3()
                    "ButtonSample4" -> ButtonSample4()
                    "ButtonSample6" -> ButtonSample6()
                    "ButtonSample7" -> ButtonSample7()
                    "ButtonSample8" -> ButtonSample8()
                    "ButtonSample9" -> ButtonSample9()
                    "IconSample1" -> IconSample1()
                    "GridSample1" -> GridSample1()
                    "CardSample1" -> CardSample1()
                    "CounterSample1" -> CounterSample1()
                    "Material3ThemeSample" -> Material3ThemeSample()
                    "CanvasSample1" -> CanvasSample1()
                    "CanvasSample2" -> CanvasSample2()
                    "GradientBackgroundSample" -> GradientBackgroundSample()
                    "PendingIntentSample" -> PendingIntentSample()
                    "VerticalScrollSample" -> VerticalScrollSample()
                    "MixedStyleSample" -> MixedStyleSample()
                    "ConditionalRadiusSample" -> ConditionalRadiusSample()
                    "TypographyScaleSample" -> TypographyScaleSample()
                    "AnchoredTextSample" -> AnchoredTextSample()
                    "RotatedTextSample" -> RotatedTextSample()
                    "FullBleedImageButtonSample" -> FullBleedImageButtonSample()
                    "TwoRemoteImagesWorkaround" -> TwoRemoteImagesWorkaround()
                    "TwoRemoteImagesBug" -> TwoRemoteImagesBug()
                    "BitmapCanvasSample" -> BitmapCanvasSample()
                    "DebugClickSample" -> DebugClickSample()
                    "BackgroundTreatmentsSample" -> BackgroundTreatmentsSample()
                    else -> SemanticStyleWorkaroundSample()
                }
            }
        }
    }
}

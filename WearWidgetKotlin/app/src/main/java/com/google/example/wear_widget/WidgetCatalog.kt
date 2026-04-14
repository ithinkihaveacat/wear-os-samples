/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.example.wear_widget

import android.content.Context
import android.util.Log
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.ui.graphics.Color
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import com.google.example.wear_widget.widget.*

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
        val dynamicBg =
            RemoteColor.createNamedRemoteColor(
                "WearM3.primaryContainer",
                localColorScheme.primaryContainer,
            )
        // Note: Not using dynamicBg because of the `RemoteModifier.background(RemoteColor)` Ignores
        // Clipping known issue
        return WearWidgetDocument(background = WearWidgetBrush.color(Color.DarkGray.rc)) {
            RemoteMaterialTheme(colorScheme = remoteColorScheme) {
                when (state.layoutName) {
                    "TaskSample" -> TaskSample()
                    "SemanticStyleSample" -> SemanticStyleSample()
                    "CanvasSample3" -> CanvasReferenceSample3()
                    "SystemThemeComparisonSample" -> SystemThemeComparisonSample()
                    "SystemThemeSample" -> SystemThemeSample()
                    "CustomThemeSample" -> CustomThemeSample()
                    "AustralianThemeSample" -> AustralianThemeSample()
                    "BoxSample1" -> BoxReferenceSample1()
                    "BoxSample2" -> BoxReferenceSample2()
                    "BoxSample3" -> BoxReferenceSample3()
                    "BoxSample4" -> BoxReferenceSample4()
                    "CircularProgress1" -> CircularProgressIndicatorSample1()
                    "CircularProgress2" -> CircularProgressIndicatorSample2()
                    "CircularProgress3" -> CircularProgressIndicatorSample3()
                    "TouchGesture1" -> TouchGestureSample1()
                    "TextSample1" -> TextSample1()
                    "TextSample1WithMargin" -> TextSample1WithMargin()
                    "RowSample1" -> RowSample1()
                    "RowSample2" -> RowSample2()
                    "CollapsibleColumnSample1" -> CollapsibleColumnSample1()
                    "ButtonSample1" -> ButtonReferenceSample1()
                    "ButtonSample2" -> ButtonReferenceSample2()
                    "ButtonSample3" -> ButtonReferenceSample3()
                    "ButtonSample4" -> ButtonReferenceSample4()
                    "ButtonSample6" -> ButtonReferenceSample6()
                    "ButtonSample7" -> ButtonReferenceSample7()
                    "ButtonSample8" -> ButtonReferenceSample8()
                    "ButtonSample9" -> ButtonReferenceSample9()
                    "IconSample1" -> IconSample1()
                    "GridSample1" -> GridSample1()
                    "CardSample1" -> CardSample1()
                    "CounterSample1" -> CounterSample1()
                    "Material3ThemeSample" -> Material3ThemeSample()
                    "CanvasSample1" -> CanvasReferenceSample1()
                    "CanvasSample2" -> CanvasReferenceSample2()
                    "GradientBackgroundSample" -> GradientBackgroundSample()
                    "PendingIntentSample" -> PendingIntentSample()
                    "VerticalScrollSample" -> VerticalScrollSample()
                    "MixedStyleSample" -> MixedStyleSample()
                    "ConditionalRadiusSample" -> ConditionalRadiusSample()
                    "TypographyScaleSample" -> TypographyScaleSample()
                    "AnchoredTextSample" -> AnchoredTextSample()
                    "RotatedTextSample" -> RotatedTextSample()
                    "FullBleedImageButtonSample" -> FullBleedImageButtonSample()
                    "BitmapCanvasSample" -> BitmapCanvasSample()
                    "DebugClickSample" -> DebugClickSample()
                    "BackgroundTreatmentsSample" -> BackgroundTreatmentsSample()
                    "ButtonSample10" -> ButtonReferenceSample10()
                    "ButtonSample11" -> ButtonReferenceSample11()
                    "ButtonSample12" -> ButtonReferenceSample12()
                    "ButtonSample13" -> ButtonReferenceSample13()
                    "CanvasSample4" -> CanvasReferenceSample4()
                    "CanvasSample5" -> CanvasReferenceSample5()
                    else -> SemanticStyleSample()
                }
            }
        }
    }
}

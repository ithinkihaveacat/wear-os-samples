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
@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.width
import androidx.compose.remote.creation.compose.modifier.height
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.tooling.preview.RemotePreview
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices

class BauhausWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = BauhausWidget()
}

class BauhausWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val state = context.getWeatherState()
        val location = context.getString(R.string.weather_location_london)
        val bgColor = Color(0xFFEEEEEE) // Off-white
        val brush = WearWidgetBrush.color(bgColor.rc)
        return WearWidgetDocument(background = brush) {
            BauhausContent(state.temp.toString(), state.condition.name, location)
        }
    }
}

@RemoteComposable
@Composable
fun BauhausContent(temp: String, condition: String, location: String) {
    val textColor = Color.Black.rc
    
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center
    ) {
        RemoteRow(
            modifier = RemoteModifier.fillMaxSize(),
            verticalAlignment = RemoteAlignment.CenterVertically
        ) {
            // Thick Red Vertical Bar
            RemoteBox(
                modifier = RemoteModifier
                    .width(16.rdp)
                    .fillMaxSize()
                    .background(Color(0xFFE53935).rc) // Red
            )
            
            RemoteColumn(
                modifier = RemoteModifier.padding(start = 32.rdp, top = 16.rdp),
                horizontalAlignment = RemoteAlignment.Start
            ) {
                // Location
                RemoteText(
                    text = location.uppercase().rs,
                    color = textColor,
                    fontSize = 12.rsp,
                    fontWeight = FontWeight.Bold,
                    modifier = RemoteModifier.padding(bottom = 2.rdp)
                )
                
                // Large Temperature
                RemoteText(
                    text = temp.rs,
                    color = textColor,
                    fontSize = 64.rsp,
                    fontWeight = FontWeight.Bold,
                    modifier = RemoteModifier.padding(bottom = 4.rdp)
                )
                
                // Yellow Horizontal Bar
                RemoteBox(
                    modifier = RemoteModifier
                        .width(60.rdp)
                        .height(8.rdp)
                        .background(Color(0xFFFDD835).rc) // Yellow
                )
                
                // Condition Text
                RemoteText(
                    text = condition.uppercase().rs,
                    color = textColor,
                    fontSize = 16.rsp,
                    fontWeight = FontWeight.Bold,
                    modifier = RemoteModifier.padding(top = 8.rdp)
                )
                
                // Blue Square for balance
                RemoteBox(
                    modifier = RemoteModifier
                        .padding(top = 16.rdp)
                        .width(24.rdp)
                        .height(24.rdp)
                        .background(Color(0xFF1E88E5).rc) // Blue
                )
            }
        }
    }
}

@WearPreviewDevices
@Composable
fun BauhausWidgetContentPreview() = RemotePreview {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color(0xFFEEEEEE).rc),
        contentAlignment = RemoteAlignment.Center
    ) {
        BauhausContent(temp = "72", condition = "Sunny", location = "London")
    }
}

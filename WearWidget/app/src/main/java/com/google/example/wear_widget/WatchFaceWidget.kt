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
import androidx.compose.remote.creation.compose.shaders.RemoteBrush
import androidx.compose.remote.creation.compose.shaders.radialGradient
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.tooling.preview.RemotePreview
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices

class WatchFaceWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = WatchFaceWidget()
}

class WatchFaceWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        // Solid color fallback for document background
        val bgColor = Color(0xFF0D47A1) 
        val brush = WearWidgetBrush.color(bgColor.rc)
        return WearWidgetDocument(background = brush) {
            WatchFaceContent()
        }
    }
}

@RemoteComposable
@Composable
fun WatchFaceContent() {
    val textColor = Color.White.rc
    
    // Radial gradient colors
    val centerColor = Color(0xFF1E88E5) // Lighter blue
    val edgeColor = Color(0xFF0D47A1)   // Darker blue
    
    RemoteBox(
        modifier = RemoteModifier
            .fillMaxSize()
            .background(
                RemoteBrush.radialGradient(
                    colors = listOf(centerColor.rc, edgeColor.rc)
                )
            ),
        contentAlignment = RemoteAlignment.Center
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            RemoteText(
                text = "Sat 01".rs,
                color = textColor,
                fontSize = 14.rsp,
                modifier = RemoteModifier.padding(bottom = 0.rdp)
            )
            RemoteText(
                text = "6:00".rs,
                color = textColor,
                fontSize = 44.rsp,
                fontWeight = FontWeight.Light,
                modifier = RemoteModifier.padding(bottom = 2.rdp)
            )
            
            // Single text with newline for greeting
            RemoteText(
                text = "Good Morning,\nAlex!".rs,
                color = textColor,
                fontSize = 12.rsp,
                textAlign = TextAlign.Center,
                modifier = RemoteModifier.padding(bottom = 8.rdp)
            )
            
            // Weather Section
            RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                RemoteText(
                    text = "☀️".rs,
                    fontSize = 24.rsp,
                    modifier = RemoteModifier.padding(end = 6.rdp)
                )
                RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                    RemoteText(
                        text = "32°".rs,
                        color = textColor,
                        fontSize = 16.rsp,
                        modifier = RemoteModifier.padding(bottom = 1.rdp)
                    )
                    // Solid line using RemoteBox
                    RemoteBox(
                        modifier = RemoteModifier
                            .width(25.rdp)
                            .height(1.rdp)
                            .background(Color.White.rc)
                    )
                    RemoteText(
                        text = "28/33".rs,
                        color = textColor,
                        fontSize = 12.rsp,
                        modifier = RemoteModifier.padding(top = 1.rdp)
                    )
                }
            }
            
            // Chevron at the bottom
            RemoteText(
                text = "﹀".rs,
                color = textColor,
                fontSize = 14.rsp,
                modifier = RemoteModifier.padding(top = 2.rdp)
            )
        }
    }
}

@WearPreviewDevices
@Composable
fun WatchFaceContentPreview() = RemotePreview {
    WatchFaceContent()
}

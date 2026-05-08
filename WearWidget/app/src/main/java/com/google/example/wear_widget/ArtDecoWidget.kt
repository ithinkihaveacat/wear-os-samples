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
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
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
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ee.schimke.composeai.preview.WearWidgetPreview
import androidx.compose.ui.text.font.FontWeight
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.remote.creation.compose.layout.RemoteImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices

class ArtDecoWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = ArtDecoWidget()
}

// Force re-render 2
class ArtDecoWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val bgColor = Color(0xFF1A237E) // Deep Blue
        val brush = WearWidgetBrush.color(bgColor.rc)
        
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.art_deco_bg)
        val imageBitmap = bitmap.asImageBitmap()
        
        return WearWidgetDocument(background = brush) {
            ArtDecoContent(imageBitmap)
        }
    }
}

@RemoteComposable
@Composable
fun ArtDecoContent(bgImage: androidx.compose.ui.graphics.ImageBitmap) {
    val goldColor = Color(0xFFFFD54F).rc
    val textColor = Color.White.rc
    val bgColor = Color(0xFF1A237E).rc
    
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center
    ) {
        // Background Image
        RemoteImage(
            remoteBitmap = bgImage.rb,
            contentDescription = "Art Deco Background".rs,
            modifier = RemoteModifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        
        // Content on top
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize().padding(16.rdp),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.SpaceBetween
        ) {
            // Top Section: Time & Date
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                RemoteText(
                    text = "10:09".rs,
                    color = goldColor,
                    fontSize = 36.rsp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                RemoteText(
                    text = "FRI, MAY 8".rs,
                    color = textColor,
                    fontSize = 12.rsp,
                    textAlign = TextAlign.Center
                )
            }
            
            // Steps
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                RemoteText(
                    text = "10,420".rs,
                    color = goldColor,
                    fontSize = 28.rsp,
                    fontWeight = FontWeight.Bold
                )
                RemoteText(
                    text = "STEPS".rs,
                    color = textColor,
                    fontSize = 10.rsp
                )
            }
            
            // Bottom Grid (Heart Rate & Calories)
            RemoteRow(
                modifier = RemoteModifier.width(140.rdp),
                horizontalArrangement = RemoteArrangement.SpaceBetween,
                verticalAlignment = RemoteAlignment.CenterVertically
            ) {
                RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                    RemoteText(text = "72".rs, color = goldColor, fontSize = 18.rsp, fontWeight = FontWeight.Bold)
                    RemoteText(text = "BPM".rs, color = textColor, fontSize = 8.rsp)
                }
                
                RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                    RemoteText(text = "450".rs, color = goldColor, fontSize = 18.rsp, fontWeight = FontWeight.Bold)
                    RemoteText(text = "KCAL".rs, color = textColor, fontSize = 8.rsp)
                }
            }
            
            // Battery
            RemoteText(
                text = "• 85% •".rs,
                color = goldColor,
                fontSize = 12.rsp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(device = "id:wearos_large_round")
@Composable
fun ArtDecoWidgetNoFramePreview() = WidgetPreview {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.art_deco_bg)
    val imageBitmap = bitmap?.asImageBitmap() ?: androidx.compose.ui.graphics.ImageBitmap(1, 1)
    
    ArtDecoContent(imageBitmap)
}

@WearWidgetPreview(frame = "small", title = "Fitness Widget", icon = "💪")
@Composable
fun ArtDecoWidgetSmallFramePreview() = WidgetPreview {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.art_deco_bg)
    val imageBitmap = bitmap?.asImageBitmap() ?: androidx.compose.ui.graphics.ImageBitmap(1, 1)
    
    ArtDecoContent(imageBitmap)
}

@WearWidgetPreview(frame = "large", title = "Fitness Widget", icon = "💪")
@Composable
fun ArtDecoWidgetLargeFramePreview() = WidgetPreview {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.art_deco_bg)
    val imageBitmap = bitmap?.asImageBitmap() ?: androidx.compose.ui.graphics.ImageBitmap(1, 1)
    
    ArtDecoContent(imageBitmap)
}



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
@file:android.annotation.SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.fillMaxWidth
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.WidgetPreview

/**
 * A UI displaying an Android logo and 'Widget Catalog' title. Below, a dark grey rounded widget
 * shows a vertical list of white text: 'Header', 'Item 0', 'Item 1', 'Item 2', 'Item 3', with 'Item
 * 4' partially visible, indicating scrollable content.
 *
 * **NOT SUPPORTED FOR WEAR WIDGETS:** This sample originally demonstrated the use of
 * `RemoteModifier.verticalScroll`. However, vertical scrolling is **not supported and will never be
 * enabled for Wear Widgets** (it may be available in the underlying Remote Compose player for other
 * surfaces).
 *
 * This sample is preserved for reference purposes only, with the crashing scroll modifier removed.
 */
@RemoteComposable
@Composable
fun VerticalScrollSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.TopCenter,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxWidth(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Top,
        ) {
            MaterialRemoteText(
                "NOT SUPPORTED".rs,
                color = Color.Red.rc,
                fontWeight = FontWeight.Bold,
            )
            MaterialRemoteText(
                "Scrolling unavailable in Wear Widgets".rs,
                fontSize = 12.rsp,
                color = Color.LightGray.rc,
            )
            RemoteBox(RemoteModifier.size(10.rdp))

            MaterialRemoteText("Header".rs)
            RemoteBox(RemoteModifier.size(10.rdp))
            for (i in 0 until 10) {
                MaterialRemoteText(("Item " + i).rs)
                RemoteBox(RemoteModifier.size(10.rdp))
            }
            MaterialRemoteText("Footer".rs)
        }
    }
}

// @PreviewWearLarge // Disabled: crashes at runtime (see line 43)
@Composable
fun VerticalScrollSamplePreview() {
    WidgetPreview { VerticalScrollSample() }
}

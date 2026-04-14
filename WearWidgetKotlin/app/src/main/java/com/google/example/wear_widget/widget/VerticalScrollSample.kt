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
import androidx.compose.remote.creation.compose.modifier.rememberRemoteScrollState
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.modifier.verticalScroll
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * A UI displaying an Android logo and 'Widget Catalog' title. Below, a dark grey rounded widget
 * shows a vertical list of white text: 'Header', 'Item 0', 'Item 1', 'Item 2', 'Item 3', with 'Item
 * 4' partially visible, indicating scrollable content.
 */
@RemoteComposable
@Composable
fun VerticalScrollSample() {
    val scrollState = rememberRemoteScrollState()
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.TopCenter,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxWidth().verticalScroll(scrollState),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Top,
        ) {
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

@PreviewWearLarge
@Composable
fun VerticalScrollSamplePreview() {
    WidgetPreview { VerticalScrollSample() }
}

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
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * Android robot icon above "Wear Widget" text. A dark gray rounded rectangular widget displays
 * "Semantic Styles Demo" and a large digital time "12:34" in white text on a black background.
 */
@RemoteComposable
@Composable
fun SemanticStyleSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxWidth(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            MaterialRemoteText(
                text = "Semantic Styles Demo".rs,
                style = RemoteMaterialTheme.typography.titleLarge,
            )

            RemoteBox(RemoteModifier.size(16.rdp))

            MaterialRemoteText(
                text = "12:34".rs,
                style = RemoteMaterialTheme.typography.numeralLarge
            )

            RemoteBox(RemoteModifier.size(12.rdp))

            MaterialRemoteText(
                text = "Session complete".rs,
                style = RemoteMaterialTheme.typography.titleMedium,
            )
        }
    }
}

@PreviewWearLarge
@Composable
fun SemanticStyleSamplePreview() {
    WidgetPreview { SemanticStyleSample() }
}

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
package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

@PreviewWearLarge
@Composable
fun BoxSample1Preview() {
    WidgetPreview {
        BoxSample1()
    }
}


/**
 * A black screen shows a circular Android icon, then "Wear Widget" in white text. Below it, a
 * large, dark grey rounded rectangle displays "Box Sample 1" centered in white text.
 */
@RemoteComposable
@Composable
fun BoxSample1() {
    // Simple Box with background color and centered text
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteText(text = "Box Sample 1", color = Color.White.rc)
    }
}

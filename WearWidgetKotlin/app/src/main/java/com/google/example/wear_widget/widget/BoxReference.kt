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
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.*
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.remote.material3.ProvideRemoteTextStyle
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * A black screen shows a circular Android icon, then "Wear Widget" in white text. Below it, a
 * large, dark grey rounded rectangle displays "Box Sample 1" centered in white text.
 */
@RemoteComposable
@Composable
fun BoxReferenceSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteText(text = "Box Sample 1", color = Color.White.rc)
    }
}

/**
 * Android logo and "Wear Widget" text. A dark grey rounded rectangle widget displays white text
 * "Box Sample 2 (Border & Padding)", centered and surrounded by a distinct red rectangular border.
 * The text is visibly padded from the red border, which itself is padded from the grey widget's
 * edges.
 */
@RemoteComposable
@Composable
fun BoxReferenceSample2() {
    ProvideRemoteTextStyle(value = RemoteMaterialTheme.typography.bodyMedium) {
        RemoteBox(
            modifier =
                RemoteModifier.fillMaxSize()
                    .padding(20.rdp)
                    .border(width = 2.rdp, color = Color.Red.rc),
            contentAlignment = RemoteAlignment.Center,
        ) {
            MaterialRemoteText(
                text = "Box Sample 2\n(Border & Padding)".rs,
                color = Color.White.rc,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Android logo above "Wear Widget" text. Below, a large blue rectangle with a gray border. Inside
 * the blue box, at the bottom right, is yellow text: "Box Sample 3 (Bottom End)".
 */
@RemoteComposable
@Composable
fun BoxReferenceSample3() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Blue),
        contentAlignment = RemoteAlignment.BottomEnd,
    ) {
        RemoteText(
            modifier = RemoteModifier.padding(10.rdp),
            text = "Box Sample 3\n(Bottom End)",
            color = Color.Yellow.rc,
            textAlign = TextAlign.End,
        )
    }
}

@PreviewWearLarge
@Composable
fun BoxReferenceSample1Preview() {
    WidgetPreview { BoxReferenceSample1() }
}

@PreviewWearLarge
@Composable
fun BoxReferenceSample2Preview() {
    WidgetPreview { BoxReferenceSample2() }
}

@PreviewWearLarge
@Composable
fun BoxReferenceSample3Preview() {
    WidgetPreview { BoxReferenceSample3() }
}

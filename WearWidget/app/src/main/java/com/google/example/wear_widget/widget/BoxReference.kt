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

import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteStateLayout
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.*
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.ri
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
 * A screen titled "Widget Catalog" with a pink circular Android logo at the top. Below the title is
 * a large, dark gray rounded rectangular box containing the centered text "Box Sample 1".
 */
@RemoteComposable
@Composable
fun BoxReferenceSample1() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteText(text = "Box Sample 1", color = Color.White.rc)
    }
}

/**
 * Widget Catalog screen on black background. Top: pink Android icon, white "Widget Catalog" text.
 * Below: large dark gray rounded rectangle. Inside, a red rectangular border frames white text:
 * "Box Sample 2" and "(Border & Padding)".
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
 * A black screen displays a pink circular Android icon and the title "Widget Catalog" in white.
 * Below, a large, rounded rectangular container with a dark gray border is filled with solid blue.
 * Partial yellow text "Bo" and "(B" is visible at the bottom right corner of the blue area.
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

/**
 * A "Widget Catalog" screen with an Android icon above the title. Below, a large blue rectangle
 * within a dark gray rounded border displays white text: "State 0" and "(Click to" (text appears
 * cut off). The overall background is black.
 */
@RemoteComposable
@Composable
fun BoxReferenceSample4() {
    val state = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteStateLayout(
            modifier = RemoteModifier.fillMaxSize(),
            state = state,
            states = intArrayOf(0, 1),
        ) { current ->
            if (current == 0) {
                RemoteBox(
                    modifier =
                        RemoteModifier.fillMaxSize()
                            .background(Color.Blue.rc)
                            .clickable(ValueChange(state, 1.ri)),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteText(
                        text = "State 0: Blue\n(Click to toggle)",
                        color = Color.White.rc,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                RemoteBox(
                    modifier =
                        RemoteModifier.fillMaxSize()
                            .background(Color.DarkGray.rc)
                            .clickable(ValueChange(state, 0.ri)),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteText(
                        text = "State 1: Gray\n(Click to toggle)",
                        color = Color.Green.rc,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
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

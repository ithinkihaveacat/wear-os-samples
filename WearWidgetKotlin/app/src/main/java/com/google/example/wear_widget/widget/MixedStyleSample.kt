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
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * A screen features an Android icon and "Wear Widget" text at the top. Below, a white rectangular
 * card with rounded corners displays "Mixed Styles". The word "Mixed" is red and bold, while
 * "Styles" is blue and italic.
 */
@RemoteComposable
@Composable
fun MixedStyleSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.White),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.Center,
        ) {
            // First part: Bold Red Text
            RemoteText(
                text = "Mixed ".rs,
                color = Color.Red.rc,
                fontSize = 20.rsp,
                fontWeight = FontWeight.Bold,
            )
            // Second part: Italic Blue Text
            RemoteText(
                text = "Styles".rs,
                color = Color.Blue.rc,
                fontSize = 20.rsp,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

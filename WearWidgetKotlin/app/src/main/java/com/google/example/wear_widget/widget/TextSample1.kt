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
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
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
import androidx.wear.compose.remote.material3.ProvideRemoteTextStyle
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * Android logo above "Wear Widget" title. A dark-grey bordered, rounded green widget displays:
 * white bold "TextSample1", white ellipsized "This is a long text that should wrap to multiple
 * lin...", and green "Version 1.0".
 */
@RemoteComposable
@Composable
fun TextSample1() {
    ProvideRemoteTextStyle(value = RemoteMaterialTheme.typography.bodyMedium) {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize().background(Color(0xFF006400)),
            contentAlignment = RemoteAlignment.Center,
        ) {
            RemoteColumn(
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
            ) {
                MaterialRemoteText(
                    text = "TextSample1".rs,
                    color = Color.White.rc,
                    fontSize = 20.rsp,
                    fontWeight = FontWeight.Bold,
                )
                MaterialRemoteText(
                    text =
                        "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability."
                            .rs,
                    color = Color.LightGray.rc,
                    fontSize = 14.rsp,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                MaterialRemoteText(
                    text = "Version 1.0".rs,
                    color = Color.Cyan.rc,
                    fontSize = 10.rsp,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

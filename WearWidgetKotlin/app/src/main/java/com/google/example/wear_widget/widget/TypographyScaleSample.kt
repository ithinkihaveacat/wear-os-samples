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
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.text.RemoteTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * A Wear OS widget display with an Android robot icon and "Wear Widget" text at the top. Below, a
 * rounded black rectangle contains three lines of text: "Title Style" in large cyan, "Default Body
 * Style" in white, and "Caption Style" in white italics.
 */
@RemoteComposable
@Composable
fun TypographyScaleSample() {
    // Define our own "semantic" styles
    val myTitleStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Cyan)

    val myCaptionStyle =
        TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic, color = Color.LightGray)

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            // 1. Title style applied manually
            MaterialRemoteText(
                text = "Title Style".rs,
                fontSize = myTitleStyle.fontSize.asRemoteTextUnit(),
                fontWeight = myTitleStyle.fontWeight,
                color = myTitleStyle.color.rc,
            )

            RemoteBox(RemoteModifier.size(12.rdp))

            // 2. Default style (bodyLarge) provided by RemoteMaterialTheme
            MaterialRemoteText("Default Body Style".rs)

            RemoteBox(RemoteModifier.size(12.rdp))

            // 3. Caption style applied via 'style' parameter
            MaterialRemoteText(
                text = "Caption Style".rs,
                style = RemoteTextStyle.fromTextStyle(myCaptionStyle)
            )
        }
    }
}

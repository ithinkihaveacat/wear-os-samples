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
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.wear.compose.remote.material3.RemoteIcon
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.R
import com.google.example.wear_widget.WidgetPreview

/**
 * UI screen with an Android robot icon in a pink circle, above the white text 'Widget Catalog'. A
 * dark gray rounded rectangle displays three Android robot heads: a small red one, a medium green
 * one, and a large blue one, arranged horizontally, increasing in size.
 */
@RemoteComposable
@Composable
fun IconSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.Center,
        ) {
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
                contentDescription = "Small Red".rs,
                modifier = RemoteModifier.size(24.rdp),
                tint = Color.Red.rc,
            )
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
                contentDescription = "Medium Green".rs,
                modifier = RemoteModifier.size(48.rdp),
                tint = Color.Green.rc,
            )
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
                contentDescription = "Large Blue".rs,
                modifier = RemoteModifier.size(72.rdp),
                tint = Color.Blue.rc,
            )
        }
    }
}

@PreviewWearLarge
@Composable
fun IconSample1Preview() {
    WidgetPreview { IconSample1() }
}

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
import androidx.compose.remote.creation.compose.layout.RemoteCollapsibleColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * Android logo above "Wear Widget" text. Below, a rounded rectangular container displays centered,
 * vertically stacked text: "Top (High)" in red, "Middle (Low)" in green, and "Bottom (High)" in
 * blue.
 *
 * **KNOWN ISSUE (b/502649242):** This widget currently crashes at runtime.
 * `RemoteCollapsibleColumn` emits an unsupported operation (233) that causes a `RuntimeException`
 * when rendered on a device or in headless previews. Do not use this layout primitive for Wear OS
 * Widgets until this issue is resolved.
 */
@RemoteComposable
@Composable
fun CollapsibleColumnSample1() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteCollapsibleColumn(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            RemoteText("Top (High)", color = Color.Red.rc, modifier = RemoteModifier.priority(1.0f))
            RemoteText(
                "Middle (Low)",
                color = Color.Green.rc,
                modifier = RemoteModifier.priority(0.1f),
            )
            RemoteText(
                "Bottom (High)",
                color = Color.Blue.rc,
                modifier = RemoteModifier.priority(1.0f),
            )
        }
    }
}

@PreviewWearLarge
@Composable
fun CollapsibleColumnSample1Preview() {
    WidgetPreview { CollapsibleColumnSample1() }
}

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
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.onTouchDown
import androidx.compose.remote.creation.compose.modifier.onTouchUp
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * A black screen displays a light pink circular icon at the top center. Inside the circle is a
 * darker pink Android robot head logo, presented with a subtle shadow effect.
 *
 * **KNOWN ISSUE (b/502649242):** This widget currently crashes at runtime.
 * `RemoteModifier.onTouchDown` and `onTouchUp` emit unsupported operations (219/220) that cause a
 * `RuntimeException` when rendered on a device or in headless previews. Do not use these modifiers
 * for Wear OS Widgets until this issue is resolved.
 */
@RemoteComposable
@Composable
fun TouchGestureSample1() {
    val downCounter = rememberMutableRemoteInt(0)
    val upCounter = rememberMutableRemoteInt(0)

    val onDownAction = ValueChange(downCounter, downCounter + 1)
    val onUpAction = ValueChange(upCounter, upCounter + 1)

    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement =
            androidx.compose.remote.creation.compose.layout.RemoteArrangement.Center
    ) {
        RemoteText("Downs: ".rs + downCounter.toRemoteString(), color = Color.White.rc)
        RemoteText("Ups: ".rs + upCounter.toRemoteString(), color = Color.White.rc)

        RemoteBox(
            modifier =
                RemoteModifier.size(width = 120.rdp, height = 60.rdp)
                    .background(RemoteColor(Color.DarkGray))
                    .onTouchDown(onDownAction)
                    .onTouchUp(onUpAction),
            contentAlignment = RemoteAlignment.Center,
        ) {
            RemoteText("Hold & Release")
        }
    }
}

@PreviewWearLarge
@Composable
fun TouchGestureSample1Preview() {
    WidgetPreview { TouchGestureSample1() }
}

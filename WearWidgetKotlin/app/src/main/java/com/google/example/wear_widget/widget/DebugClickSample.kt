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

import android.app.PendingIntent
import android.content.Intent
import androidx.compose.remote.creation.compose.action.pendingIntentAction
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shapes.RemoteCircleShape
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * An app screen with a black background. At the top, a pink circular Android icon is centered
 * above "Widget Catalog" text. Below, a large, dark gray rounded rectangle widget houses a
 * central red square with "Click Me" text in white.
 */
@RemoteComposable
@Composable
fun DebugClickSample() {
    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val intent =
        remember(context) {
            Intent("com.google.example.wear_widget.DEBUG_CLICK_ACTION")
                .setPackage(context.packageName)
        }
    val pendingIntent =
        remember(context) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteBox(
            modifier =
                RemoteModifier.size(100.rdp).background(Color.Red.rc).clip(RemoteCircleShape).let {
                    if (isInspectionMode) {
                        it
                    } else {
                        it.clickable(pendingIntentAction(pendingIntent))
                    }
                },
            contentAlignment = RemoteAlignment.Center,
        ) {
            MaterialRemoteText("Click Me".rs)
        }
    }
}

@PreviewWearLarge
@Composable
fun DebugClickSamplePreview() {
    WidgetPreview { DebugClickSample() }
}

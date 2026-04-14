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
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview


import android.app.PendingIntent
import android.content.Intent
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.action.pendingIntentAction
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier
import com.google.example.wear_widget.MainActivity

/**
 * A Wear OS widget design features an Android robot logo above "Wear Widget" text. Below, a dark
 * gray rounded rectangle container, representing the widget, frames a central light purple-blue
 * rounded button with "Open App" in dark text, all on a black background.
 */
@RemoteComposable
@Composable
fun PendingIntentSample() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val dummy = rememberMutableRemoteInt(0)
    val intent =
        Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
    val pendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            modifier = RemoteModifier.buttonSizeModifier(),
            onClick =
                if (isInspectionMode) ValueChange(dummy, 0.ri)
                else pendingIntentAction(pendingIntent),
        ) {
            MaterialRemoteText("Open App".rs)
        }
    }
}


@PreviewWearLarge
@Composable
fun PendingIntentSamplePreview() {
    WidgetPreview {
        PendingIntentSample()
    }
}

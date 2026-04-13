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
package com.example.android.wearable.composestarter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.AmbientMode
import androidx.wear.compose.foundation.AmbientModeManager
import androidx.wear.compose.foundation.LocalAmbientModeManager
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales

@Composable
fun AmbientScreen(modifier: Modifier = Modifier) {
    val ambientManager = LocalAmbientModeManager.current
    val isAmbient = ambientManager?.currentAmbientMode is AmbientMode.Ambient

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(if (isAmbient) Color.Black else Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        ) {
            Text(
                text = "12:34",
                fontSize = 32.sp,
                fontWeight = if (isAmbient) FontWeight.Light else FontWeight.Bold,
                color = if (isAmbient) Color.White else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!isAmbient) {
                Text(
                    text = "10.5 km",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "142 bpm",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            } else {
                Text(
                    text = "-- km",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
                Text(
                    text = "-- bpm",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text =
                    if (isAmbient) {
                        "adb shell input keyevent\nKEYCODE_WAKEUP"
                    } else {
                        "adb shell input keyevent\nKEYCODE_SLEEP"
                    },
                style = MaterialTheme.typography.labelSmall,
                color = if (isAmbient) Color.DarkGray else Color.LightGray,
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 2
            )
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun AmbientScreenInteractivePreview() {
    AmbientScreen()
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun AmbientScreenAmbientPreview() {
    val ambientManager =
        remember {
            object : AmbientModeManager {
                override val currentAmbientMode: AmbientMode =
                    AmbientMode.Ambient(
                        isBurnInProtectionRequired = false,
                        isLowBitAmbientSupported = false
                    )

                override suspend fun withAmbientTick(block: () -> Unit) {}
            }
        }
    CompositionLocalProvider(LocalAmbientModeManager provides ambientManager) {
        AmbientScreen()
    }
}

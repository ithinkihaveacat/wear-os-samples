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

import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.asRemoteDp
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@RemoteComposable
@Composable
fun ConditionalRadiusSample() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize()) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            // 1. Get Width and Density
            val widthPx = remote.component.width
            val density = remoteDensity.density

            // 2. Calculate Condition (Width > 200dp?)
            val widthDp = widthPx / density
            val isWide = widthDp.gt(200f.rf)

            // 3. Select Value (15dp if wide, 10dp if narrow) and convert to RemoteDp
            val radiusDp = isWide.select(15f.rf, 10f.rf).asRemoteDp()
            val radiusPx = radiusDp.toPx(remoteDensity)

            // 4. Use the value (Converting back to Px for drawing commands)
            drawRoundRect(
                paint = RemotePaint().apply { color = Color.Blue.rc },
                topLeft = RemoteOffset.Zero,
                size = RemoteSize(widthPx, remote.component.height),
                cornerRadius = RemoteOffset(radiusPx, radiusPx),
            )
        }
    }
}

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
package com.google.example.wear_widget

import android.content.Context
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams

class ForceUpdateService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = ForceUpdateWidget()
}

class ForceUpdateWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val count = context.getCounterState().count
        return WearWidgetDocument(background = WearWidgetBrush.color(Color.Black.rc)) {
            ForceUpdateWidgetContent(count)
        }
    }
}

@RemoteComposable
@Composable
fun ForceUpdateWidgetContent(count: Int) {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            RemoteText(text = "Favorite Number".rs, color = Color.Gray.rc, fontSize = 12.rsp)
            RemoteText(text = count.toString().rs, color = Color.White.rc, fontSize = 40.rsp)
        }
    }
}

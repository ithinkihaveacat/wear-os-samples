/*
 * Copyright 2022 The Android Open Source Project
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
package com.example.wear.tiles.golden

import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.emptyClickable

object Calendar {

  fun layout(
    context: Context,
    deviceParameters: DeviceParameters,
    eventTime: String,
    eventName: String,
    eventLocation: String,
    clickable: Clickable
  ) =
    materialScope(context, deviceParameters) {
      primaryLayout(mainSlot = { text("Hello".layoutString) }) // helpme: replace this primaryLayout() call with a layout of buttons on two rows. the first row should consist of a button for the date (taking up 60% of the width) and a second button that's the plus sign. the second row consists of a single button extending full width containing the event name (over potentially 2 lines) with the time underneath on a separate line. on large devices, include a third element of the address. these three buttons must use different colors (choose randomly). the "+" in the second button should be an image--see Timer.timer1Layout() for what resource to use. see other tiles in the directory for implementation help.
    }
}

@MultiRoundDevicesWithFontScalePreviews
internal fun calendarPreview(context: Context) = TilePreviewData {
  TilePreviewHelper.singleTimelineEntryTileBuilder(
    Calendar.layout(
      context,
      it.deviceConfiguration,
      eventTime = "6:30-7:30 PM",
      eventName = "Tennis Coaching with Christina Lloyd",
      eventLocation = "216 Market Street",
      clickable = emptyClickable
    )
  )
    .build()
}

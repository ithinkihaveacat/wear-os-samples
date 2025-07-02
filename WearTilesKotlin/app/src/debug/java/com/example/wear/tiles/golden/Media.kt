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

// For background image check
// https://source.corp.google.com/piper///depot/google3/java/com/google/android/clockwork/prototiles/samples/material3/CalendarTileService.kt?q=CalendarTileService&ct=os

import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.CONTENT_SCALE_MODE_CROP
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material3.ButtonDefaults.filledTonalButtonColors
import androidx.wear.protolayout.material3.ButtonGroupDefaults
import androidx.wear.protolayout.material3.ButtonStyle.Companion.defaultButtonStyle
import androidx.wear.protolayout.material3.ButtonStyle.Companion.smallButtonStyle
import androidx.wear.protolayout.material3.backgroundImage
import androidx.wear.protolayout.material3.button
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.column
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.resources

object Media {

  fun layout(
    context: Context,
    deviceParameters: DeviceParameters,
    playlist2ImageId: String? = null,
  ) =
    materialScope(context, deviceParameters) {
      primaryLayout(
        titleSlot =
          if (deviceParameters.isLargeScreen()) {
            { text("Last played".layoutString) }
          } else {
            null
          },
        bottomSlot = {
          textEdgeButton(onClick = clickable(), labelContent = { text("Browse".layoutString) })
        },
        mainSlot = {
          column {
            setWidth(expand())
            setHeight(expand())
            addContent(
              button(
                onClick = clickable(),
                width = expand(),
                height = expand(),
                colors = filledTonalButtonColors(),
                style =
                  if (deviceParameters.isLargeScreen()) {
                    defaultButtonStyle()
                  } else {
                    smallButtonStyle()
                  },
                horizontalAlignment = LayoutElementBuilders.TEXT_ALIGN_START,
                labelContent = { text("Metal mix".layoutString) },
              )
            )
            addContent(ButtonGroupDefaults.DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS)
            addContent(
              button(
                onClick = clickable(),
                width = expand(),
                height = expand(),
                colors = filledTonalButtonColors(),
                style =
                  if (deviceParameters.isLargeScreen()) {
                    defaultButtonStyle()
                  } else {
                    smallButtonStyle()
                  },
                horizontalAlignment = LayoutElementBuilders.TEXT_ALIGN_START,
                backgroundContent =
                  playlist2ImageId?.let { id ->
                    {
                      backgroundImage(
                        protoLayoutResourceId = id,
                        overlayColor = null,
                        contentScaleMode = CONTENT_SCALE_MODE_CROP,
                      )
                    }
                  },
                labelContent = { text("Chilled mix".layoutString) },
              )
            )
          }
        },
      )
    }

  fun resources(context: Context) = resources {
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_music_queue_24),
      R.drawable.ic_music_queue_24,
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_podcasts_24),
      R.drawable.ic_podcasts_24,
    )
    addIdToImageMapping(context.resources.getResourceName(R.drawable.news), R.drawable.news)
  }

  data class Playlist(
    val label: String,
    val imageId: String? = null,
    val clickable: Clickable? = clickable(),
  )
}

@MultiRoundDevicesWithFontScalePreviews
internal fun mediaPreview(context: Context) =
  TilePreviewData(Media.resources(context)) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
        Media.layout(
          context,
          it.deviceConfiguration,
          playlist2ImageId = context.resources.getResourceName(R.drawable.news),
        )
      )
      .build()
  }

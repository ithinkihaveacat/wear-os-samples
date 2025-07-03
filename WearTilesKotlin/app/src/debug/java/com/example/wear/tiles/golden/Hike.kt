/*
 * Copyright 2025 The Android Open Source Project
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
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.weight
import androidx.wear.protolayout.LayoutElementBuilders.CONTENT_SCALE_MODE_CROP
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material3.ButtonDefaults.filledButtonColors
import androidx.wear.protolayout.material3.ButtonDefaults.filledTonalButtonColors
import androidx.wear.protolayout.material3.ButtonDefaults.filledVariantButtonColors
import androidx.wear.protolayout.material3.CardDefaults.filledVariantCardColors
import androidx.wear.protolayout.material3.backgroundImage
import androidx.wear.protolayout.material3.buttonGroup
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.iconButton
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.material3.titleCard
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.contentDescription
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.box
import com.example.wear.tiles.tools.column
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.resources

object Hike {

  fun layout(
    context: Context,
    deviceParameters: DeviceParameters,
    eventDate: String,
    eventTime: String,
    eventName: String,
    eventLocation: String,
    eventImageId: String? = null,
    clickable: Clickable
  ) =
    materialScope(context, deviceParameters) {
      primaryLayout(
        bottomSlot = {
          textEdgeButton(onClick = clickable(), colors = filledVariantButtonColors(), labelContent = { text("Start".layoutString) })
        },
        mainSlot = {
          column {
            setWidth(expand())
            setHeight(expand())
            addContent(
              box {
                setWidth(expand())
                setHeight(weight(0.3f))
                addContent(
                  buttonGroup {
                    buttonGroupItem {
                      box {
                        setWidth(weight(0.4f))
                        setHeight(expand())
                        addContent( // helpme: replace with button containing two lines of text. first line "10" (large). second line "Miles" (small).
                          textButton(
                            onClick = clickable,
                            labelContent = { text(eventDate.layoutString) },
                            colors = filledTonalButtonColors(),
                            width = expand(),
                            height = expand()
                          )
                        )
                      }
                    }
                    buttonGroupItem {
                      box {
                        setWidth(weight(0.6f))
                        setHeight(expand())
                        addContent(
                          iconButton(
                            onClick = clickable,
                            iconContent = {
                              icon(context.resources.getResourceName(R.drawable.outline_add_24))
                            },
                            colors = filledButtonColors(),
                            modifier = LayoutModifier.contentDescription("Add Event"),
                            width = expand(),
                            height = expand()
                          )
                        )
                      }
                    }
                  }
                )
              }
            )
          }
        }
      )
    }

  fun resources(context: Context) = resources {
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.outline_add_24),
      R.drawable.outline_add_24
    )
    addIdToImageMapping(context.resources.getResourceName(R.drawable.news), R.drawable.news)
  }
}

@MultiRoundDevicesWithFontScalePreviews
internal fun hikePreview(context: Context) =
  TilePreviewData(Hike.resources(context)) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
      Hike.layout(
        context,
        it.deviceConfiguration,
        eventDate = "25 July",
        eventTime = "6:30-7:30 PM",
        eventName = "Tennis Coaching with Christina Lloyd",
        eventLocation = "216 Market Street",
        eventImageId = context.resources.getResourceName(R.drawable.news),
        clickable = clickable()
      )
    )
      .build()
  }

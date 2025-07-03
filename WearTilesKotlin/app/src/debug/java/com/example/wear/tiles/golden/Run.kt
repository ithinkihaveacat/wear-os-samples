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
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.TitleChip
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper.singleTimelineEntryTileBuilder
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.resources

// https://www.figma.com/design/2OJqWvi4ebE7FY5uuBTUhm/GM3-BC25-Wear-Compose-Design-Kit-1.5?node-id=66728-48273&m=dev

object Run {
  data class RunData(
    val lastRunText: String,
    val chanceOfRain: Int,
    val startRunClickable: Clickable,
    val moreChipClickable: Clickable
  )

  fun layout(
    context: Context,
    deviceParameters: DeviceParameters,
    data: RunData
  ) =
    PrimaryLayout.Builder(deviceParameters)
      .setResponsiveContentInsetEnabled(true)
      .setPrimaryLabelTextContent(
        Text.Builder(context, data.lastRunText)
          .setTypography(Typography.TYPOGRAPHY_CAPTION1)
          .setColor(ColorBuilders.argb(GoldenTilesColors.White))
          .build()
      )
      .setContent(
        TitleChip.Builder(context, "Start run", data.startRunClickable, deviceParameters)
          // TitleChip/Chip's default width == device width minus some padding
          // Since PrimaryLayout's content slot already has margin, this leads to clipping
          // unless we override the width to use the available space
          .setWidth(DimensionBuilders.ExpandedDimensionProp.Builder().build())
          .setChipColors(
            ChipColors(
              /*backgroundColor=*/
              ColorBuilders.argb(GoldenTilesColors.Blue),
              /*contentColor=*/
              ColorBuilders.argb(GoldenTilesColors.Black)
            )
          )
          .build()
      )
      .apply {
        if (deviceParameters.screenWidthDp > 225) {
          setSecondaryLabelTextContent(
            Text.Builder(context, "${data.chanceOfRain}% chance of rain")
              .setTypography(Typography.TYPOGRAPHY_CAPTION1)
              .setColor(ColorBuilders.argb(GoldenTilesColors.LightGray))
              .build()
          )
        }
      }
      .setPrimaryChipContent(
        CompactChip.Builder(context, "More", data.moreChipClickable, deviceParameters)
          .setChipColors(
            ChipColors(
              /*backgroundColor=*/
              ColorBuilders.argb(GoldenTilesColors.DarkGray),
              /*contentColor=*/
              ColorBuilders.argb(GoldenTilesColors.White)
            )
          )
          .build()
      )
      .build()
}

@MultiRoundDevicesWithFontScalePreviews
internal fun runPreview(context: Context) = TilePreviewData {
  singleTimelineEntryTileBuilder(
    Run.layout(
      context,
      it.deviceConfiguration,
      Run.RunData(
        lastRunText = "2 days ago",
        chanceOfRain = 20,
        startRunClickable = clickable(),
        moreChipClickable = clickable()
      )
    )
  )
    .build()
}

class RunTileService : BaseTileService() {
  override fun layout(
    context: Context,
    deviceParameters: DeviceParameters
  ): LayoutElement =
    Run.layout(
      context,
      deviceParameters,
      Run.RunData(
        lastRunText = "2 days ago",
        chanceOfRain = 20,
        startRunClickable = clickable(),
        moreChipClickable = clickable()
      )
    )

  override fun resources(context: Context) = resources {}
}

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
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.CONTENT_SCALE_MODE_CROP
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material3.ButtonColors
import androidx.wear.protolayout.material3.backgroundImage
import androidx.wear.protolayout.material3.card
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
import com.example.wear.tiles.tools.resources
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object News {

  fun layout(
    context: Context,
    deviceParameters: DeviceParameters,
    date: String,
    headline: String,
    newsVendor: String,
    clickable: Clickable
  ): LayoutElementBuilders.LayoutElement {
    return materialScope(
      context = context,
      deviceConfiguration = deviceParameters,
      allowDynamicTheme = false,
      defaultColorScheme = GoldenTilesColorScheme
    ) {
      primaryLayout(
        titleSlot = { text(date.layoutString) },
        mainSlot = {
          card(
            onClick = clickable,
            width = expand(),
            height = expand(),
            backgroundContent = {
              backgroundImage(
                protoLayoutResourceId = "news_image",
                overlayColor = null,
                contentScaleMode = CONTENT_SCALE_MODE_CROP
              )
            },
            content = { text(text = headline.layoutString, maxLines = 3) }
          )
        },
        bottomSlot = {
          textEdgeButton(
            colors =
            ButtonColors(
              labelColor = colorScheme.onSurface,
              containerColor = colorScheme.surfaceContainer
            ),
            onClick = clickable,
            labelContent = { text("News".layoutString) }
          )
        }
      )
    }
  }

  internal fun LocalDate.formatLocalDateTime(today: LocalDate = LocalDate.now()): String {
    val yesterday = today.minusDays(1)

    return when {
      this == yesterday -> "yesterday ${format(DateTimeFormatter.ofPattern("MMM d"))}"
      this == today -> "today ${format(DateTimeFormatter.ofPattern("MMM d"))}"
      else -> format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
    }
  }
}

@MultiRoundDevicesWithFontScalePreviews
internal fun newsPreview(context: Context) =
  TilePreviewData(resources { addIdToImageMapping("news_image", R.drawable.news) }) {
    val now = LocalDateTime.of(2024, 8, 1, 0, 0).toInstant(ZoneOffset.UTC)
    Clock.fixed(now, Clock.systemUTC().zone)

    TilePreviewHelper.singleTimelineEntryTileBuilder(
      News.layout(
        context,
        it.deviceConfiguration,
        headline = "Millions still without power as new storm moves across US",
        newsVendor = "The New York Times",
        date = "Today, 31 July",
        clickable = clickable()
      )
    )
      .build()
  }

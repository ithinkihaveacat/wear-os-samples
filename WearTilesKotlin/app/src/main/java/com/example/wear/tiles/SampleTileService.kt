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
package com.example.wear.tiles

import android.content.Context
import android.graphics.Color
import androidx.annotation.OptIn
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.layout.fontStyle
import androidx.wear.protolayout.material3.ButtonColors
import androidx.wear.protolayout.material3.PrimaryLayoutMargins
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.LayoutColor
import androidx.wear.protolayout.types.argb
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.example.wear.tiles.golden.Goal
import com.example.wear.tiles.golden.Meditation
import com.example.wear.tiles.golden.News
import com.example.wear.tiles.golden.Social
import com.example.wear.tiles.golden.Workout
import com.example.wear.tiles.golden.mockContacts
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.emptyClickable
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.UUID

object Layout {

  fun hello(context: Context, deviceConfiguration: DeviceParametersBuilders.DeviceParameters) =
    materialScope(
      context = context,
      deviceConfiguration = deviceConfiguration,
      allowDynamicTheme = false
    ) {
      primaryLayout(
        margins = PrimaryLayoutMargins.MAX_PRIMARY_LAYOUT_MARGIN,
        titleSlot = { text("Hello, World!".layoutString) },
        mainSlot = {
          textButton(
            height = expand(),
            width = expand(),
            onClick = emptyClickable,
            shape = shapes.small,
            colors =
            // Distinguish from the edge button
            ButtonColors(
              containerColor = Color.RED.argb,
              labelColor = LayoutColor(Color.RED),
              iconColor = 0xFFFFFF00.argb
            ),
            labelContent = { text("Max Margin".layoutString) }
          )
        },
        bottomSlot = {
          textEdgeButton(
            onClick = emptyClickable,
            labelContent = { text("Edge".layoutString) }
          )
        }
      )
    }

  @OptIn(ProtoLayoutExperimental::class)
  fun simple(context: Context, deviceConfiguration: DeviceParametersBuilders.DeviceParameters) =
    materialScope(context, deviceConfiguration) {
      primaryLayout(
        titleSlot = {
          androidx.wear.protolayout.layout.basicText(
            text = "Hello, World!".layoutString,
            fontStyle(
              size = 44F,
              settings =
              listOf(
                LayoutElementBuilders.FontSetting.weight(500),
                LayoutElementBuilders.FontSetting.roundness(100),
                LayoutElementBuilders.FontSetting.width(100F)
              ),
              weight = LayoutElementBuilders.FONT_WEIGHT_MEDIUM,
              letterSpacingEm = 0F
            )
          )
        },
        mainSlot = {
          textButton(
            height = expand(),
            width = expand(),
            onClick = emptyClickable,
            shape = shapes.extraSmall,
            colors =
            // Distinguish from the edge button
            ButtonColors(
              containerColor = colorScheme.secondaryContainer,
              labelColor = colorScheme.onSecondaryContainer
            ),
            labelContent = {
              text(
                "mainSlot".layoutString,
                italic = true,
                // Defined in e.g.
                // androidx.wear.protolayout.LayoutElementBuilders.FontSetting.weight
                settings =
                listOf(
                  LayoutElementBuilders.FontSetting.weight(500),
                  LayoutElementBuilders.FontSetting.width(100F),
                  LayoutElementBuilders.FontSetting.roundness(100)
                )
              )
            }
          )
        },
        margins = PrimaryLayoutMargins.MIN_PRIMARY_LAYOUT_MARGIN,
        bottomSlot = {
          textEdgeButton(
            onClick = clickable(),
            labelContent = { text("bottomSlot".layoutString) }
          )
        }
      )
    }
}

class SampleTileService : SuspendingTileService() {
  private var layoutCounter = 4

  override suspend fun tileRequest(
    requestParams: RequestBuilders.TileRequest
  ): TileBuilders.Tile {
    val layout =
      when (layoutCounter % 6) {
        0 ->
          Meditation.minutes(
            this,
            requestParams.deviceConfiguration,
            numOfLeftTasks = 2,
            session1 =
            Meditation.Session(
              label = "Breathe",
              iconId = Meditation.CHIP_1_ICON_ID,
              clickable = emptyClickable
            ),
            session2 =
            Meditation.Session(
              label = "Daily mindfulness",
              iconId = Meditation.CHIP_2_ICON_ID,
              clickable = emptyClickable
            ),
            browseClickable = emptyClickable
          )
        1 ->
          News.layout2(
            context = this,
            deviceParameters = requestParams.deviceConfiguration,
            headline = "Millions still without power as new storm moves across the US",
            newsVendor = "The New York Times",
            clickable = emptyClickable,
            date = "Today, 31 July"
          )
        2 ->
          Social.layout(
            context = this,
            deviceParameters = requestParams.deviceConfiguration,
            contacts = mockContacts()
          )
        3 -> Layout.hello(this, requestParams.deviceConfiguration)
        4 -> Workout.layout(this, requestParams.deviceConfiguration)
        5 -> Goal.layout(this, requestParams.deviceConfiguration, 8324, 10000)
        else -> Layout.simple(this, requestParams.deviceConfiguration)
      }
    layoutCounter++
    val resourcesVersion = UUID.randomUUID().toString()
    return TileBuilders.Tile.Builder()
      .setResourcesVersion(resourcesVersion)
      .setTileTimeline(TimelineBuilders.Timeline.fromLayoutElement(layout))
      .build()
  }

  private fun ResourceBuilders.Resources.Builder.addIdToImageMapping(
    @androidx.annotation.DrawableRes resId: Int
  ) = addIdToImageMapping(resources.getResourceName(resId), resId)

  override suspend fun resourcesRequest(
    requestParams: RequestBuilders.ResourcesRequest
  ): ResourceBuilders.Resources =
    ResourceBuilders.Resources.Builder()
      .setVersion(requestParams.version)
      .apply {
        addIdToImageMapping(R.drawable.news)
        addIdToImageMapping(R.drawable.outline_directions_walk_24)
        addIdToImageMapping(R.drawable.self_improvement_24px)
        addIdToImageMapping(R.drawable.ic_yoga_24)
        addIdToImageMapping(R.drawable.ic_run_24)
        addIdToImageMapping(R.drawable.ic_cycling_24)
        mockContacts().forEach {
          if (it.avatarId != null && it.avatarResource != null) {
            addIdToImageMapping(it.avatarId!!, it.avatarResource!!)
          }
        }
        addIdToImageMapping(R.drawable.ic_search_24)
      }
      .build()
}

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
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.material3.CardDefaults.filledVariantCardColors
import androidx.wear.protolayout.material3.Typography.DISPLAY_MEDIUM
import androidx.wear.protolayout.material3.Typography.TITLE_MEDIUM
import androidx.wear.protolayout.material3.buttonGroup
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.iconDataCard
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper.singleTimelineEntryTileBuilder
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.noOpElement
import com.example.wear.tiles.tools.resources
import com.google.android.horologist.tiles.images.drawableResToImageResource

object Workout {

  fun layout1(context: Context, deviceParameters: DeviceParametersBuilders.DeviceParameters) =
    materialScope(
      context = context,
      deviceConfiguration = deviceParameters,
      allowDynamicTheme = true
    ) {
      primaryLayout(
        titleSlot = { text("Exercise".layoutString) },
        mainSlot = {
          buttonGroup {
            buttonGroupItem {
              iconDataCard(
                onClick = clickable(),
                width = expand(),
                height = if (isLargeScreen()) dp(90f) else expand(),
                colors = filledVariantCardColors(),
                title = {
                  icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.self_improvement_24px)
                  )
                }
              )
            }
            buttonGroupItem {
              iconDataCard(
                onClick = clickable(),
                width = if (isLargeScreen()) dp(80f) else expand(),
                height = expand(),
                shape = shapes.medium,
                title = {
                  if (isLargeScreen()) {
                    text("30".layoutString, typography = DISPLAY_MEDIUM)
                  } else {
                    noOpElement()
                  }
                },
                content = {
                  if (isLargeScreen()) {
                    text("Mins".layoutString, typography = TITLE_MEDIUM)
                  } else {
                    noOpElement()
                  }
                },
                secondaryIcon = {
                  icon(
                    protoLayoutResourceId = context.resources.getResourceName(R.drawable.ic_run_24)
                  )
                }
              )
            }
            buttonGroupItem {
              iconDataCard(
                onClick = clickable(),
                width = expand(),
                height = if (isLargeScreen()) dp(90f) else expand(),
                colors = filledVariantCardColors(),
                title = {
                  icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.ic_cycling_24)
                  )
                }
              )
            }
          }
        },
        bottomSlot = { textEdgeButton(onClick = clickable()) { text("More".layoutString) } }
      )
    }

  fun resources(context: Context) = resources {
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_run_24),
      R.drawable.ic_run_24
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.self_improvement_24px),
      R.drawable.self_improvement_24px
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_cycling_24),
      R.drawable.ic_cycling_24
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_yoga_24),
      drawableResToImageResource(R.drawable.ic_yoga_24)
    )
  }
}

@MultiRoundDevicesWithFontScalePreviews
internal fun workoutLayoutPreview(context: Context) =
  TilePreviewData(onTileResourceRequest = Workout.resources(context)) {
    singleTimelineEntryTileBuilder(Workout.layout1(context, it.deviceConfiguration)).build()
  }

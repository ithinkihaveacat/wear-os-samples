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
import androidx.wear.protolayout.material3.ButtonDefaults.filledButtonColors
import androidx.wear.protolayout.material3.ButtonDefaults.filledVariantButtonColors
import androidx.wear.protolayout.material3.ButtonGroupDefaults
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.Typography.NUMERAL_SMALL
import androidx.wear.protolayout.material3.buttonGroup
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.iconEdgeButton
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.contentDescription
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.column
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.resources

object Timer {

  fun timer1Layout(context: Context, deviceParameters: DeviceParameters) =
    materialScope(context, deviceParameters) {
      primaryLayout(
        titleSlot = { text("Minutes".layoutString) },
        mainSlot = {
          if (deviceParameters.isLargeScreen()) {
            column {
              setWidth(expand())
              setHeight(expand())
              addContent(
                buttonGroup {
                  buttonGroupItem { timerTextButton1("5") }
                  buttonGroupItem { timerTextButton1("10") }
                }
              )
              addContent(ButtonGroupDefaults.DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS)
              addContent(
                buttonGroup {
                  buttonGroupItem { timerTextButton1("15") }
                  buttonGroupItem { timerTextButton1("20") }
                  buttonGroupItem { timerTextButton1("30") }
                }
              )
            }
          } else {
            buttonGroup {
              buttonGroupItem { timerTextButton1("5") }
              buttonGroupItem { timerTextButton1("10") }
              buttonGroupItem { timerTextButton1("15") }
            }
          }
        },
        bottomSlot = {
          iconEdgeButton(
            onClick = clickable(),
            colors = filledButtonColors(),
            modifier = LayoutModifier.contentDescription("Plus"),
            iconContent = {
              icon(context.resources.getResourceName(R.drawable.outline_add_2_24))
            }
          )
        }
      )
    }

  private fun MaterialScope.timerTextButton1(text: String) =
    textButton(
      width = expand(),
      height = expand(),
      onClick = clickable(),
      shape = shapes.large,
      colors = filledVariantButtonColors(),
      labelContent = { text(text.layoutString, typography = NUMERAL_SMALL) }
    )
}

@MultiRoundDevicesWithFontScalePreviews
fun timerLayoutPreview(context: Context) =
  TilePreviewData(
    resources {
      addIdToImageMapping(
        context.resources.getResourceName(R.drawable.outline_add_2_24),
        R.drawable.outline_add_2_24
      )
    }
  ) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
      Timer.timer1Layout(context, it.deviceConfiguration)
    )
      .build()
  }

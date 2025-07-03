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
@file:SuppressLint("RestrictedApi")

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

import android.annotation.SuppressLint
import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material3.ButtonDefaults.filledButtonColors
import androidx.wear.protolayout.material3.ButtonDefaults.filledTonalButtonColors
import androidx.wear.protolayout.material3.ButtonDefaults.filledVariantButtonColors
import androidx.wear.protolayout.material3.ButtonGroupDefaults
import androidx.wear.protolayout.material3.ButtonGroupDefaults.DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS
import androidx.wear.protolayout.material3.ButtonStyle.Companion.defaultButtonStyle
import androidx.wear.protolayout.material3.ButtonStyle.Companion.smallButtonStyle
import androidx.wear.protolayout.material3.ColorScheme
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.TextButtonStyle.Companion.smallTextButtonStyle
import androidx.wear.protolayout.material3.button
import androidx.wear.protolayout.material3.buttonGroup
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.iconEdgeButton
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.material3.tokens.PaletteTokens
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.contentDescription
import androidx.wear.protolayout.types.LayoutColor
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.column
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.resources

fun MaterialScope.timerButton(firstLine: String?, secondLine: String? = null) =
  timerButton2(firstLine, secondLine)

fun MaterialScope.timerButton1(firstLine: String?, secondLine: String? = null) =
  // in beta, spacing between labelContent and secondaryLabelContent should be 0dp
  button(
    onClick = clickable(),
    width = expand(),
    height = expand(),
    style = smallButtonStyle(),
    horizontalAlignment = HORIZONTAL_ALIGN_CENTER,
    labelContent = { text(firstLine?.layoutString ?: "".layoutString) },
    secondaryLabelContent = { text(secondLine?.layoutString ?: "".layoutString) }
  )

fun MaterialScope.timerButton2(firstLine: String?, secondLine: String? = null) =
  textButton(
    onClick = clickable(),
    width = expand(),
    height = expand(),
    style = smallTextButtonStyle(),
    colors =
      filledVariantButtonColors(),
//    filledButtonColors()
//      .copy(
//        containerColor = LayoutColor(PaletteTokens.PRIMARY30),
//        labelColor = LayoutColor(PaletteTokens.PRIMARY95)
//      ),
    labelContent = {
      Column.Builder()
        .apply {
          if (firstLine != null) {
            addContent(
              text(text = firstLine.layoutString, typography = Typography.TYPOGRAPHY_CAPTION1)
            )
          }
          if (secondLine != null) {
            addContent(
              text(text = secondLine.layoutString, typography = Typography.TYPOGRAPHY_CAPTION2)
            )
          }
        }
        .build()
    }
  )

// Move somewhere, and modify all materialScope calls to provide as default.
val myColorScheme =
  ColorScheme(
    primary = LayoutColor(PaletteTokens.PRIMARY30), // bg of buttons
    onPrimary = LayoutColor(PaletteTokens.PRIMARY95), // fg of buttons
    tertiary = LayoutColor(PaletteTokens.TERTIARY80), // bg of edge button
    onTertiary = LayoutColor(PaletteTokens.TERTIARY10) // fg of edge button
    //    secondary = android.graphics.Color.RED.argb,
    //    onSecondary = LayoutColor(Color(0xFFFFFF00).toArgb())
  )

object Meditation {

  fun listLayout(
    context: Context,
    deviceParameters: DeviceParameters,
    tasksLeft: Int
  ) =
    materialScope(context, deviceParameters, defaultColorScheme = myColorScheme) {
      primaryLayout(
        titleSlot =
        if (isLargeScreen()) {
          { text("$tasksLeft mindful tasks left".layoutString) }
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
                if (isLargeScreen()) {
                  defaultButtonStyle()
                } else {
                  smallButtonStyle()
                },
                iconContent = {
                  icon(context.resources.getResourceName(R.drawable.outline_air_24))
                },
                labelContent = { text("Breath".layoutString) }
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
                if (isLargeScreen()) {
                  defaultButtonStyle()
                } else {
                  smallButtonStyle()
                },
                iconContent = { icon(context.resources.getResourceName(R.drawable.ic_yoga_24)) },
                labelContent = { text("Daily mindfulness".layoutString, maxLines = 2) }
              )
            )
          }
        }
      )
    }

  fun timerLayout(
    context: Context,
    deviceParameters: DeviceParameters,
    clickable: Clickable
  ) = materialScope(context = context, deviceConfiguration = deviceParameters) {
      primaryLayout(
        mainSlot = {
          column {
            setWidth(expand())
            setHeight(expand())
            addContent(
              buttonGroup {
                buttonGroupItem { timerButton("1:00", "Hour") }
                buttonGroupItem { timerButton("5", "Mins") }
              }
            )
            addContent(DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS)
            addContent(
              buttonGroup {
                buttonGroupItem { timerButton("15", "Mins") }
                buttonGroupItem { timerButton("20", "Mins") }
                buttonGroupItem { timerButton("25", "Mins") }
              }
            )
            build()
          }
        },
        bottomSlot = {
          iconEdgeButton(
            onClick = clickable,
            colors =
            filledButtonColors()
              .copy(containerColor = colorScheme.tertiary, labelColor = colorScheme.onTertiary),
            modifier = LayoutModifier.contentDescription("Plus"),
            iconContent = { icon(context.resources.getResourceName(R.drawable.outline_add_2_24)) }
          )
        }
      )
    }

  fun resources(context: Context) = resources {
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_yoga_24),
      R.drawable.ic_yoga_24
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.outline_air_24),
      R.drawable.outline_air_24
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_breathe_24),
      R.drawable.ic_breathe_24
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.ic_mindfulness_24),
      R.drawable.ic_mindfulness_24
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.outline_add_2_24),
      R.drawable.outline_add_2_24
    )
  }

}

@MultiRoundDevicesWithFontScalePreviews
fun mindfulnessPreview(context: Context) =
  TilePreviewData(Meditation.resources(context)) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
      Meditation.listLayout(context, it.deviceConfiguration, 3)
    )
      .build()
  }

@MultiRoundDevicesWithFontScalePreviews
internal fun meditationMinutesPreview(context: Context) =
  TilePreviewData(Meditation.resources(context)) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
      Meditation.timerLayout(
        context,
        it.deviceConfiguration,
        clickable()
      )
    )
      .build()
  }

class MindfulnessTileService : BaseTileService() {
  override fun layout(context: Context, deviceParameters: DeviceParameters): LayoutElement =
    Meditation.listLayout(context, deviceParameters, 2)

  override fun resources(context: Context) = Meditation.resources(context)
}

class MeditationMinutesTileService : BaseTileService() {
  override fun layout(context: Context, deviceParameters: DeviceParameters): LayoutElement =
    Meditation.timerLayout(
      context,
      deviceParameters,
      clickable()
    )

  override fun resources(context: Context) = Meditation.resources(context)
}

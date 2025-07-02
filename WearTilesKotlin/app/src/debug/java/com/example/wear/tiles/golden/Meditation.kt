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
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.Chip
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material3.ButtonDefaults.filledButtonColors
import androidx.wear.protolayout.material3.ButtonDefaults.filledTonalButtonColors
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
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.material3.tokens.PaletteTokens
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.LayoutColor
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.column
import com.example.wear.tiles.tools.emptyClickable
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.resources

fun MaterialScope.timerButton(firstLine: String?, secondLine: String? = null) =
  timerButton2(firstLine, secondLine)

fun MaterialScope.timerButton1(firstLine: String?, secondLine: String? = null) =
  // in beta, spacing between labelContent and secondaryLabelContent should be 0dp
  button(
    onClick = emptyClickable,
    width = expand(),
    height = expand(),
    style = smallButtonStyle(),
    horizontalAlignment = HORIZONTAL_ALIGN_CENTER,
    labelContent = { text(firstLine?.layoutString ?: "".layoutString) },
    secondaryLabelContent = { text(secondLine?.layoutString ?: "".layoutString) }
  )

fun MaterialScope.timerButton2(firstLine: String?, secondLine: String? = null) =
  textButton(
    onClick = emptyClickable,
    width = expand(),
    height = expand(),
    style = smallTextButtonStyle(),
    colors =
    filledButtonColors()
      .copy(
        containerColor = LayoutColor(PaletteTokens.PRIMARY30),
        labelColor = LayoutColor(PaletteTokens.PRIMARY95)
      ),
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
  const val CHIP_1_ICON_ID = "meditation_1"
  const val CHIP_2_ICON_ID = "meditation_2"

  // https://source.corp.google.com/piper///depot/google3/java/com/google/android/clockwork/prototiles/samples/material3/KeepTileService.kt;l=85?q=KeepTileService&ct=os

  fun mindfulnessLayout(context: Context, deviceParameters: DeviceParameters) =
    materialScope(context, deviceParameters) {
      primaryLayout(
        titleSlot =
        if (deviceParameters.isLargeScreen()) {
          { text("2 mindful tasks left".layoutString) }
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
                if (deviceParameters.isLargeScreen()) {
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

  fun tasks(context: Context, deviceParameters: DeviceParameters): LayoutElement {
    return materialScope(
      context = context,
      deviceConfiguration = deviceParameters,
      allowDynamicTheme = true
    ) {
      primaryLayout(mainSlot = { text("Hello".layoutString) })
    }
  }

  fun minutes(
    context: Context,
    deviceParameters: DeviceParameters,
    numOfLeftTasks: Int,
    session1: Session,
    session2: Session,
    browseClickable: Clickable
  ): LayoutElement {
    return materialScope(
      context = context,
      deviceConfiguration = deviceParameters,
      allowDynamicTheme = true
      //      defaultColorScheme = myColorScheme,
    ) {
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
          textEdgeButton(
            onClick = browseClickable,
            labelContent = { text("+".layoutString) },
            colors =
            filledButtonColors()
              .copy(containerColor = colorScheme.tertiary, labelColor = colorScheme.onTertiary)
            //              filledButtonColors().copy(containerColor =
            // LayoutColor(Color.rgb(255, 0, 0)), labelColor =
            // LayoutColor(Color.rgb(255, 255, 0)))
          )
        }
      )
    }
  }

  fun buttonsLayout(
    context: Context,
    deviceParameters: DeviceParameters,
    timer1: Timer,
    timer2: Timer,
    timer3: Timer,
    timer4: Timer,
    timer5: Timer,
    clickable: Clickable
  ) =
    PrimaryLayout.Builder(deviceParameters)
      .setResponsiveContentInsetEnabled(true)
      .setPrimaryLabelTextContent(
        Text.Builder(context, "Minutes")
          .setTypography(Typography.TYPOGRAPHY_CAPTION1)
          .setColor(ColorBuilders.argb(GoldenTilesColors.White))
          .build()
      )
      .setContent(
        MultiButtonLayout.Builder()
          .addButtonContent(timerButton(context, timer1))
          .addButtonContent(timerButton(context, timer2))
          .addButtonContent(timerButton(context, timer3))
          .apply {
            if (deviceParameters.screenWidthDp > 225) {
              addButtonContent(timerButton(context, timer4))
              addButtonContent(timerButton(context, timer5))
            }
          }
          .build()
      )
      .setPrimaryChipContent(
        CompactChip.Builder(context, "New", clickable, deviceParameters)
          .setChipColors(
            ChipColors(
              /*backgroundColor=*/
              ColorBuilders.argb(GoldenTilesColors.DarkPurple),
              /*contentColor=*/
              ColorBuilders.argb(GoldenTilesColors.White)
            )
          )
          .build()
      )
      .build()

  private fun timerButton(context: Context, timer: Timer) =
    Button.Builder(context, timer.clickable)
      .setTextContent(timer.minutes.toString(), Typography.TYPOGRAPHY_TITLE3)
      .setButtonColors(
        androidx.wear.protolayout.material.ButtonColors(
          /*backgroundColor=*/
          ColorBuilders.argb(GoldenTilesColors.LightPurple),
          /*contentColor=*/
          ColorBuilders.argb(GoldenTilesColors.DarkerGray)
        )
      )
      .build()

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
      context.resources.getResourceName(R.drawable.outline_add_2_24),
      R.drawable.ic_breathe_24
    )
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.outline_add_2_24),
      R.drawable.ic_mindfulness_24
    )
  }

  data class Session(val label: String, val iconId: String, val clickable: Clickable)

  data class Timer(val minutes: Int, val clickable: Clickable)
}

@MultiRoundDevicesWithFontScalePreviews
fun mindfulnessPreview(context: Context) =
  TilePreviewData(Meditation.resources(context)) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
      Meditation.mindfulnessLayout(context, it.deviceConfiguration)
    )
      .build()
  }

@MultiRoundDevicesWithFontScalePreviews
internal fun meditationMinutesPreview(context: Context) =
  TilePreviewData(Meditation.resources(context)) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
      Meditation.minutes(
        context,
        it.deviceConfiguration,
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
    )
      .build()
  }

// @MultiRoundDevicesWithFontScalePreviews
internal fun meditationButtonsPreview(context: Context) = TilePreviewData {
  TilePreviewHelper.singleTimelineEntryTileBuilder(
    Meditation.buttonsLayout(
      context,
      it.deviceConfiguration,
      timer1 = Meditation.Timer(minutes = 5, clickable = emptyClickable),
      timer2 = Meditation.Timer(minutes = 10, clickable = emptyClickable),
      timer3 = Meditation.Timer(minutes = 15, clickable = emptyClickable),
      timer4 = Meditation.Timer(minutes = 20, clickable = emptyClickable),
      timer5 = Meditation.Timer(minutes = 25, clickable = emptyClickable),
      clickable = emptyClickable
    )
  )
    .build()
}

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

package com.example.wear.tiles.golden

import android.annotation.SuppressLint
import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders.em
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.material3.ButtonDefaults.filledTonalButtonColors
import androidx.wear.protolayout.material3.CardDefaults.filledVariantCardColors
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.TitleCardStyle
import androidx.wear.protolayout.material3.Typography.DISPLAY_MEDIUM
import androidx.wear.protolayout.material3.Typography.TITLE_MEDIUM
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.iconEdgeButton
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.titleCard
import androidx.wear.protolayout.material3.tokens.TypeScaleTokens
import androidx.wear.protolayout.material3.tokens.VariableFontSettingsTokens
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.clearSemantics
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.contentDescription
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.emptyClickable
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.resources
import java.time.LocalTime
import java.util.Locale

/**
 * Creates a styled time LayoutElement from a LocalTime object.
 *
 * This function formats the time to a 12-hour format with an AM/PM indicator. The time is displayed
 * in a large font, and the AM/PM indicator in a smaller font, both aligned to the same baseline
 * using a Spannable.
 *
 * @param time A java.time.LocalTime object representing the time to display.
 * @return A LayoutElement containing the styled time.
 */
fun MaterialScope.styledTime(time: LocalTime): LayoutElement {
  val hour24 = time.hour
  val minute = time.minute

  val amPm = if (hour24 < 12) "AM" else "PM"

  var hour12 = hour24 % 12
  if (hour12 == 0) { // Adjust for midnight (0) and noon (12)
    hour12 = 12
  }

  val timeString = "$hour12:${String.format(Locale.US, "%02d", minute)}"

  // Manually build the FontStyle for the time using public Material 3 tokens.
  // Values are from androidx.wear.protolayout.material3.tokens.TypeScaleTokens
  val timeSizeSp = TypeScaleTokens.DISPLAY_MEDIUM_SIZE
  val timeTrackingSp = TypeScaleTokens.DISPLAY_MEDIUM_TRACKING
  val timeStyle =
    LayoutElementBuilders.FontStyle.Builder()
      .setSize(sp(timeSizeSp))
      .setLetterSpacing(em(timeTrackingSp / timeSizeSp))
      .setSettings(*VariableFontSettingsTokens.DISPLAY_MEDIUM_VARIATION_SETTINGS.toTypedArray())
      .build()

  // Manually build the FontStyle for the AM/PM indicator.
  val amPmSizeSp = TypeScaleTokens.TITLE_MEDIUM_SIZE
  val amPmTrackingSp = TypeScaleTokens.TITLE_MEDIUM_TRACKING
  val amPmStyle =
    LayoutElementBuilders.FontStyle.Builder()
      .setSize(sp(amPmSizeSp))
      .setLetterSpacing(em(amPmTrackingSp / amPmSizeSp))
      .setSettings(*VariableFontSettingsTokens.TITLE_MEDIUM_VARIATION_SETTINGS.toTypedArray())
      .build()

  // Create the text spans.
  val timeSpan =
    LayoutElementBuilders.SpanText.Builder().setText(timeString).setFontStyle(timeStyle).build()

  val amPmSpan =
    LayoutElementBuilders.SpanText.Builder().setText(" $amPm").setFontStyle(amPmStyle).build()

  // Combine the spans into a single Spannable element.
  return LayoutElementBuilders.Spannable.Builder().addSpan(timeSpan).addSpan(amPmSpan).build()
}

fun MaterialScope.simpleTime(time: LocalTime): LayoutElement {
  val hour24 = time.hour
  val minute = time.minute

  val amPm = if (hour24 < 12) "AM" else "PM"

  var hour12 = hour24 % 12
  if (hour12 == 0) { // Adjust for midnight (0) and noon (12)
    hour12 = 12
  }

  return text(
    String.format(Locale.US, "%d:%02d%s", hour12, minute, amPm).layoutString,
    typography = DISPLAY_MEDIUM
  )
}

object Alarm {

  fun layout(
    context: Context,
    deviceParameters: DeviceParameters,
    timeUntilAlarm: String,
    alarmTime: String,
    alarmDays: String,
    clickable: Clickable
  ) =
    materialScope(context, deviceParameters) {
      primaryLayout(
        titleSlot = { text("Alarm".layoutString, modifier = LayoutModifier.clearSemantics()) },
        mainSlot = {
          titleCard(
            onClick = clickable,
            title = {
              text(
                "Mon—Fri".layoutString,
                typography = TITLE_MEDIUM,
                color = colorScheme.onSurfaceVariant
              )
            },
            content = { styledTime(LocalTime.parse(alarmTime)) },
            height = expand(),
            colors = filledVariantCardColors(),
            style =
            if (isLargeScreen()) {
              TitleCardStyle.extraLargeTitleCardStyle()
            } else {
              TitleCardStyle.defaultTitleCardStyle()
            }
          )
        },
        bottomSlot = {
          iconEdgeButton(
            onClick = clickable,
            colors = filledTonalButtonColors(),
            modifier = LayoutModifier.contentDescription("Plus"),
            iconContent = { icon(context.resources.getResourceName(R.drawable.outline_add_2_24)) }
          )
        }
      )
    }

  fun resources(context: Context) = resources {
    addIdToImageMapping(
      context.resources.getResourceName(R.drawable.outline_add_2_24),
      R.drawable.outline_add_2_24
    )
  }
}

@MultiRoundDevicesWithFontScalePreviews
internal fun alarmPreview(context: Context) =
  TilePreviewData(Alarm.resources(context)) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
      Alarm.layout(
        context,
        it.deviceConfiguration,
        timeUntilAlarm = "Less than 1 min",
        alarmTime = "14:58",
        alarmDays = "Mon, Tue, Wed, Thu, Fri, Sat",
        clickable = clickable()
      )
    )
      .build()
  }

class AlarmTileService : BaseTileService() {
    override fun layout(
        context: Context,
        deviceParameters: DeviceParameters,
    ): LayoutElement =
        Alarm.layout(
            context,
            deviceParameters,
            "Less than 1 min",
            "14:58",
            "Mon, Tue, Wed, Thu, Fri, Sat",
            clickable(),
        )

    override fun resources(context: Context) = Alarm.resources(context)
}

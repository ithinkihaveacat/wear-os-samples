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
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.material3.ButtonDefaults.filledVariantButtonColors
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.Typography
import androidx.wear.protolayout.material3.buttonGroup
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.isLargeScreen

// https://www.figma.com/design/2OJqWvi4ebE7FY5uuBTUhm/GM3-BC25-Wear-Compose-Design-Kit-1.5?node-id=66728-39449&m=dev

object Ski {

    fun layout(
        context: Context,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        stat1: Stat,
        stat2: Stat,
    ) =
        materialScope(context, deviceParameters) {
            primaryLayout(
                titleSlot = { text("Latest run".layoutString) },
                mainSlot = {
                    buttonGroup {
                        buttonGroupItem { statColumn(stat1) }
                        buttonGroupItem { statColumn(stat2) }
                    }
                },
            )
        }

    private fun MaterialScope.statColumn(stat: Stat) =
        textButton(
            onClick = clickable(),
            width = expand(),
            height = expand(),
            colors =
                filledVariantButtonColors()
                    .copy(
                        containerColor = colorScheme.onSecondary,
                        labelColor = colorScheme.secondary,
                    ),
            labelContent = {
                if (deviceConfiguration.isLargeScreen()) {
                    LayoutElementBuilders.Column.Builder()
                        .addContent(
                            text(stat.label.layoutString, typography = Typography.TITLE_MEDIUM)
                        )
                        .addContent(
                            LayoutElementBuilders.Spacer.Builder().setHeight(dp(6f)).build()
                        )
                        .addContent(
                            text(stat.value.layoutString, typography = Typography.NUMERAL_SMALL)
                        )
                        .addContent(
                            text(stat.unit.layoutString, typography = Typography.TITLE_MEDIUM)
                        )
                        .build()
                } else {
                    LayoutElementBuilders.Column.Builder()
                        .addContent(
                            text(stat.label.layoutString, typography = Typography.TITLE_SMALL)
                        )
                        .addContent(
                            LayoutElementBuilders.Spacer.Builder().setHeight(dp(6f)).build()
                        )
                        .addContent(
                            text(
                                stat.value.layoutString,
                                typography = Typography.NUMERAL_EXTRA_SMALL,
                            )
                        )
                        .addContent(
                            text(stat.unit.layoutString, typography = Typography.TITLE_SMALL)
                        )
                        .build()
                }
            },
        )

    data class Stat(val label: String, val value: String, val unit: String)
}

@MultiRoundDevicesWithFontScalePreviews
internal fun skiPreview(context: Context) = TilePreviewData {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
            Ski.layout(
                context,
                it.deviceConfiguration,
                stat1 = Ski.Stat("Max Spd", "46.5", "mph"),
                stat2 = Ski.Stat("Distance", "21.8", "mile"),
            )
        )
        .build()
}

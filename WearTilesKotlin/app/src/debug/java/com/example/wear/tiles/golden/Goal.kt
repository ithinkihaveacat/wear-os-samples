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
import androidx.wear.protolayout.material3.CardDefaults.filledTonalCardColors
import androidx.wear.protolayout.material3.GraphicDataCardDefaults.constructGraphic
import androidx.wear.protolayout.material3.PrimaryLayoutMargins
import androidx.wear.protolayout.material3.circularProgressIndicator
import androidx.wear.protolayout.material3.graphicDataCard
import androidx.wear.protolayout.material3.icon
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
import com.example.wear.tiles.tools.resources
import java.text.NumberFormat
import java.util.Locale

private const val ICON_ID = "icon"

object Goal {
    fun layout(context: Context, deviceParameters: DeviceParameters, steps: Int, goal: Int) =
        materialScope(
            context = context,
            deviceConfiguration = deviceParameters,
            allowDynamicTheme = true,
        ) {
            val stepsString = NumberFormat.getNumberInstance(Locale.US).format(steps)
            val goalString = NumberFormat.getNumberInstance(Locale.US).format(goal)
            primaryLayout(
                titleSlot = { text("Steps".layoutString) },
                margins = PrimaryLayoutMargins.MIN_PRIMARY_LAYOUT_MARGIN,
                mainSlot = {
                    graphicDataCard(
                        onClick = clickable(),
                        height = expand(),
                        colors = filledTonalCardColors(),
                        title = { text(stepsString.layoutString) },
                        content = { text("of $goalString".layoutString) },
                        horizontalAlignment = LayoutElementBuilders.HORIZONTAL_ALIGN_END,
                        graphic = {
                            constructGraphic(
                                mainContent = {
                                    circularProgressIndicator(
                                        staticProgress = 1F * steps / goal,
                                        startAngleDegrees = 200F,
                                        endAngleDegrees = 520F,
                                    )
                                },
                                iconContent = { icon(ICON_ID) },
                            )
                        },
                    )
                },
                bottomSlot = {
                    textEdgeButton(onClick = clickable()) { text("Track".layoutString) }
                },
            )
        }
}

@MultiRoundDevicesWithFontScalePreviews
internal fun goalPreview(context: Context) =
    TilePreviewData(
        onTileResourceRequest =
            resources { addIdToImageMapping(ICON_ID, R.drawable.outline_directions_walk_24) }
    ) {
        singleTimelineEntryTileBuilder(
                Goal.layout(context, it.deviceConfiguration, steps = 5168, goal = 8000)
            )
            .build()
    }

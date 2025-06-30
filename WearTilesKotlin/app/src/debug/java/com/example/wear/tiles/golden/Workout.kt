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
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.TitleChip
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material3.CardDefaults.filledVariantCardColors
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.Typography.DISPLAY_LARGE
import androidx.wear.protolayout.material3.Typography.TITLE_MEDIUM
import androidx.wear.protolayout.material3.avatarImage
import androidx.wear.protolayout.material3.buttonGroup
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
import com.example.wear.tiles.tools.emptyClickable
import com.example.wear.tiles.tools.isLargeScreen
import com.example.wear.tiles.tools.resources
import com.google.android.horologist.tiles.images.drawableResToImageResource

// TODO: Designs
// https://www.figma.com/design/2OJqWvi4ebE7FY5uuBTUhm/GM3-BC25-Wear-Compose-Design-Kit-1.5?node-id=66728-38855&m=dev

val noOpElement: MaterialScope.() -> LayoutElementBuilders.LayoutElement = {
    LayoutElementBuilders.Spacer.Builder().setWidth(dp(0F)).setHeight(dp(0F)).build()
}

object Workout {
    const val BUTTON_1_ICON_ID = "workout 1"
    const val BUTTON_2_ICON_ID = "workout 2"
    const val BUTTON_3_ICON_ID = "workout 3"

    fun layout(context: Context, deviceParameters: DeviceParametersBuilders.DeviceParameters) =
        materialScope(
            context = context,
            deviceConfiguration = deviceParameters,
            allowDynamicTheme = true,
        ) {
            primaryLayout(
                titleSlot = { text("Exercise".layoutString) },
                mainSlot = {
                    buttonGroup {
                        buttonGroupItem {
                            iconDataCard(
                                onClick = clickable(),
                                width = expand(),
                                height =
                                    if (deviceParameters.isLargeScreen()) dp(90f) else expand(),
                                colors = filledVariantCardColors(),
                                title = {
                                    avatarImage(
                                        protoLayoutResourceId =
                                            context.resources.getResourceName(
                                                R.drawable.self_improvement_24px
                                            ),
                                        contentScaleMode =
                                            LayoutElementBuilders.CONTENT_SCALE_MODE_FILL_BOUNDS,
                                    )
                                },
                                //                      secondaryIcon = {
                                //                        avatarImage(
                                //                          ICON_ID,
                                //                          width = dp(32f),
                                //                          height = dp(32f),
                                //                          contentScaleMode =
                                // LayoutElementBuilders.CONTENT_SCALE_MODE_FILL_BOUNDS,
                                //                        )
                                //                      }
                            )
                        }
                        buttonGroupItem {
                            iconDataCard(
                                onClick = clickable(),
                                width = if (deviceParameters.isLargeScreen()) dp(90f) else expand(),
                                height = expand(),
                                shape = shapes.medium,
                                title = {
                                    if (deviceParameters.isLargeScreen())
                                        text("30".layoutString, typography = DISPLAY_LARGE)
                                    else noOpElement()
                                },
                                content = {
                                    if (deviceParameters.isLargeScreen())
                                        text("Mins".layoutString, typography = TITLE_MEDIUM)
                                    else noOpElement()
                                },
                                secondaryIcon = {
                                    avatarImage(
                                        protoLayoutResourceId =
                                            context.resources.getResourceName(R.drawable.ic_run_24),
                                        contentScaleMode =
                                            LayoutElementBuilders.CONTENT_SCALE_MODE_FILL_BOUNDS,
                                    )
                                },
                            )
                        }
                        buttonGroupItem {
                            iconDataCard(
                                onClick = clickable(),
                                width = expand(),
                                height =
                                    if (deviceParameters.isLargeScreen()) dp(90f) else expand(),
                                colors = filledVariantCardColors(),
                                title = {
                                    avatarImage(
                                        protoLayoutResourceId =
                                            context.resources.getResourceName(
                                                R.drawable.ic_cycling_24
                                            ),
                                        contentScaleMode =
                                            LayoutElementBuilders.CONTENT_SCALE_MODE_FILL_BOUNDS,
                                    )
                                },
                            )
                        }
                    }
                },
                bottomSlot = { textEdgeButton(onClick = clickable()) { text("More".layoutString) } },
            )
        }

    fun buttonsLayout(
        context: Context,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        weekSummary: String,
        button1Clickable: ModifiersBuilders.Clickable,
        button2Clickable: ModifiersBuilders.Clickable,
        button3Clickable: ModifiersBuilders.Clickable,
        chipClickable: ModifiersBuilders.Clickable,
    ) =
        PrimaryLayout.Builder(deviceParameters)
            .setResponsiveContentInsetEnabled(true)
            .setPrimaryLabelTextContent(
                Text.Builder(context, weekSummary)
                    .setTypography(Typography.TYPOGRAPHY_CAPTION1)
                    .setColor(ColorBuilders.argb(GoldenTilesColors.Blue))
                    .build()
            )
            .setContent(
                MultiButtonLayout.Builder()
                    .addButtonContent(
                        Button.Builder(context, button1Clickable)
                            .setIconContent(BUTTON_1_ICON_ID)
                            .build()
                    )
                    .addButtonContent(
                        Button.Builder(context, button2Clickable)
                            .setIconContent(BUTTON_2_ICON_ID)
                            .build()
                    )
                    .addButtonContent(
                        Button.Builder(context, button3Clickable)
                            .setIconContent(BUTTON_3_ICON_ID)
                            .build()
                    )
                    .build()
            )
            .setPrimaryChipContent(
                CompactChip.Builder(context, "More", chipClickable, deviceParameters)
                    .setChipColors(
                        ChipColors(
                            /*backgroundColor=*/ ColorBuilders.argb(GoldenTilesColors.BlueGray),
                            /*contentColor=*/ ColorBuilders.argb(GoldenTilesColors.White),
                        )
                    )
                    .build()
            )
            .build()

    fun largeChipLayout(
        context: Context,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        clickable: ModifiersBuilders.Clickable,
        lastWorkoutSummary: String,
    ) =
        PrimaryLayout.Builder(deviceParameters)
            .setResponsiveContentInsetEnabled(true)
            .setPrimaryLabelTextContent(
                Text.Builder(context, "Power Yoga")
                    .setTypography(Typography.TYPOGRAPHY_CAPTION1)
                    .setColor(ColorBuilders.argb(GoldenTilesColors.Yellow))
                    .build()
            )
            .setContent(
                TitleChip.Builder(context, "Start", clickable, deviceParameters)
                    // TitleChip/Chip's default width == device width minus some padding
                    // Since PrimaryLayout's content slot already has margin, this leads to clipping
                    // unless we override the width to use the available space
                    .setWidth(DimensionBuilders.ExpandedDimensionProp.Builder().build())
                    .setChipColors(
                        ChipColors(
                            /*backgroundColor=*/ ColorBuilders.argb(GoldenTilesColors.Yellow),
                            /*contentColor=*/ ColorBuilders.argb(GoldenTilesColors.Black),
                        )
                    )
                    .build()
            )
            .setSecondaryLabelTextContent(
                Text.Builder(context, lastWorkoutSummary)
                    .setTypography(Typography.TYPOGRAPHY_CAPTION1)
                    .setColor(ColorBuilders.argb(GoldenTilesColors.White))
                    .build()
            )
            .build()
}

@MultiRoundDevicesWithFontScalePreviews
internal fun workoutLayoutPreview(context: Context) =
    TilePreviewData(
        onTileResourceRequest =
            resources {
                addIdToImageMapping(
                    context.resources.getResourceName(R.drawable.ic_run_24),
                    R.drawable.ic_run_24,
                )
                addIdToImageMapping(
                    context.resources.getResourceName(R.drawable.self_improvement_24px),
                    R.drawable.self_improvement_24px
                )
                addIdToImageMapping(
                    context.resources.getResourceName(R.drawable.ic_cycling_24),
                    R.drawable.ic_cycling_24,
                )
            }
    ) {
        singleTimelineEntryTileBuilder(Workout.layout(context, it.deviceConfiguration)).build()
    }

@MultiRoundDevicesWithFontScalePreviews
internal fun workoutButtonsPreview(context: Context) =
    TilePreviewData(
        onTileResourceRequest =
            resources {
                addIdToImageMapping(
                    Workout.BUTTON_1_ICON_ID,
                    drawableResToImageResource(R.drawable.ic_run_24),
                )
                addIdToImageMapping(
                    Workout.BUTTON_2_ICON_ID,
                    drawableResToImageResource(R.drawable.ic_yoga_24),
                )
                addIdToImageMapping(
                    Workout.BUTTON_3_ICON_ID,
                    drawableResToImageResource(R.drawable.ic_cycling_24),
                )
            }
    ) {
        singleTimelineEntryTileBuilder(
                Workout.buttonsLayout(
                    context,
                    it.deviceConfiguration,
                    weekSummary = "1 run this week",
                    button1Clickable = emptyClickable,
                    button2Clickable = emptyClickable,
                    button3Clickable = emptyClickable,
                    chipClickable = emptyClickable,
                )
            )
            .build()
    }

@MultiRoundDevicesWithFontScalePreviews
internal fun workoutLargeChipPreview(context: Context) = TilePreviewData {
    singleTimelineEntryTileBuilder(
            Workout.largeChipLayout(
                context,
                it.deviceConfiguration,
                clickable = emptyClickable,
                lastWorkoutSummary = "Last session 45m",
            )
        )
        .build()
}

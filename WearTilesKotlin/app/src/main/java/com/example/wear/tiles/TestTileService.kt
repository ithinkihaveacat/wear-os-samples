package com.example.wear.tiles

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material3.ButtonColors
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.Typography.BODY_LARGE
import androidx.wear.protolayout.material3.button
import androidx.wear.protolayout.material3.buttonGroup
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.enterTransition
import androidx.wear.protolayout.modifiers.opacity
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.emptyClickable
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.common.util.concurrent.Futures
import java.util.UUID
import kotlin.random.Random

private const val RESOURCES_VERSION = "1"
private const val USE_M3_LAYOUT = true

class TestTileService : TileService() {

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
        Futures.immediateFuture(
            Tile.Builder()
                //                .setResourcesVersion(RESOURCES_VERSION)
                .setTileTimeline(
                    Timeline.fromLayoutElement(
                        layoutSimple(this, requestParams.deviceConfiguration)
                    )
                )
                .build()
        )

    @OptIn(ProtoLayoutExperimental::class)
    fun layoutM2(context: Context, deviceConfiguration: DeviceParametersBuilders.DeviceParameters) =
        PrimaryLayout.Builder(deviceConfiguration)
            .setResponsiveContentInsetEnabled(true)
            .setContent(
                Text.Builder(this, "M2 Hello, World!")
                    .setTypography(Typography.TYPOGRAPHY_BODY1)
                    .setColor(argb(0xFFFFFFFF.toInt()))
                    .setModifiers(modifierM2())
                    .build()
            )
            .build()

    @OptIn(ProtoLayoutExperimental::class)
    fun modifierM2(): ModifiersBuilders.Modifiers =
        ModifiersBuilders.Modifiers.Builder()
            .setOpacity(TypeBuilders.FloatProp.Builder(0.9F).build())
            /*
            .setContentUpdateAnimation(
                ModifiersBuilders.AnimatedVisibility.Builder()
                    .setEnterTransition(
                        ModifiersBuilders.DefaultContentTransitions.slideIn(
                            SLIDE_DIRECTION_LEFT_TO_RIGHT
                        )
                    )
                    .setExitTransition(ModifiersBuilders.DefaultContentTransitions.fadeOut())
                    .build()
            )
            */
            .build()

    fun MaterialScope.simpleButton() =
        button(onClick = clickable(), labelContent = { text("Q".layoutString) })

    fun layoutSimple(
        context: Context,
        deviceConfiguration: DeviceParametersBuilders.DeviceParameters,
    ) =
        materialScope(context, deviceConfiguration) {
            primaryLayout(
                titleSlot = { text("titleSlot".layoutString) },
                mainSlot = {
                    textButton(
                        height = expand(),
                        width = expand(),
                        onClick = emptyClickable,
//                        shape = shapes.small,
                        colors =
                            // Distinguish from the edge button
                            ButtonColors(
                                containerColor = colorScheme.secondaryContainer,
                                labelColor = colorScheme.onSecondaryContainer,
                            ),
                        labelContent = { text("mainSlot".layoutString) },
                    )
                },
                bottomSlot = {
                    textEdgeButton(
                        onClick = clickable(),
                        labelContent = { text("bottomSlot".layoutString) },
                    )
                },
            )
        }

    fun layoutConditional(
        context: Context,
        deviceConfiguration: DeviceParametersBuilders.DeviceParameters,
    ) =
        materialScope(context, deviceConfiguration) {
            primaryLayout(
                mainSlot = {
                    buttonGroup {
                        buttonGroupItem { simpleButton() }
                        buttonGroupItem { simpleButton() }
                        if (deviceConfiguration.screenHeightDp >= 225) {
                            buttonGroupItem { simpleButton() }
                        }
                    }
                }
            )
        }

    @OptIn(ProtoLayoutExperimental::class)
    fun layoutM3(context: Context, deviceConfiguration: DeviceParametersBuilders.DeviceParameters) =
        materialScope(context, deviceConfiguration) {
            primaryLayout(
                mainSlot = {
                    text(
                        text = "M3 Hello, World!".layoutString,
                        typography = BODY_LARGE,
                        // Can't get these transitions to work
                        modifier = modifierM3(),
                    )
                }
            )
        }

    fun modifierM3(): LayoutModifier = LayoutModifier.opacity(0.2F)

    override fun onTileResourcesRequest(requestParams: ResourcesRequest) =
        Futures.immediateFuture(Resources.Builder().setVersion(RESOURCES_VERSION).build())
}

class TileServiceM2 : TileService() {

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
        Futures.immediateFuture(
            Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTileTimeline(
                    Timeline.fromLayoutElement(
                        Text.Builder(this, "Hello World!")
                            .setTypography(Typography.TYPOGRAPHY_BODY1)
                            .setColor(argb(0xFFFFFFFF.toInt()))
                            .build()
                    )
                )
                .build()
        )

    override fun onTileResourcesRequest(requestParams: ResourcesRequest) =
        Futures.immediateFuture(Resources.Builder().setVersion(RESOURCES_VERSION).build())
}

class TileServiceM3 : TileService() {
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
        Futures.immediateFuture(
            Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTileTimeline(
                    Timeline.fromLayoutElement(
                        materialScope(this, requestParams.deviceConfiguration) {
                            primaryLayout(
                                mainSlot = {
                                    text(
                                        text = "Hello, World!".layoutString,
                                        typography = BODY_LARGE,
                                    )
                                }
                            )
                        }
                    )
                )
                .build()
        )

    override fun onTileResourcesRequest(requestParams: ResourcesRequest) =
        Futures.immediateFuture(Resources.Builder().setVersion(RESOURCES_VERSION).build())
}

class TestTileService2 : SuspendingTileService() {
    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        var layoutElement =
            PrimaryLayout.Builder(requestParams.deviceConfiguration)
                .setResponsiveContentInsetEnabled(true)
                .setContent(
                    MultiButtonLayout.Builder()
                        .addButtonContent(
                            Button.Builder(application, emptyClickable)
                                .setImageContent("img1")
                                .build()
                        )
                        .addButtonContent(
                            Button.Builder(application, emptyClickable)
                                .setImageContent("img2")
                                .build()
                        )
                        .addButtonContent(
                            Button.Builder(application, emptyClickable)
                                .setImageContent("img3")
                                .build()
                        )
                        .addButtonContent(
                            Button.Builder(application, emptyClickable)
                                .setImageContent("img4")
                                .build()
                        )
                        .build()
                )
                .setPrimaryChipContent(
                    CompactChip.Builder(
                            application,
                            "Reload",
                            Clickable.Builder()
                                .setId("reload")
                                .setOnClick(LoadAction.Builder().build())
                                .build(),
                            requestParams.deviceConfiguration,
                        )
                        .build()
                )
                .build()
        val resourcesVersion = UUID.randomUUID().toString() // random to force resource request
        return Tile.Builder()
            .setResourcesVersion(resourcesVersion)
            .setTileTimeline(Timeline.fromLayoutElement(layoutElement))
            .build()
    }

    override suspend fun resourcesRequest(requestParams: ResourcesRequest): Resources {
        Log.d("wwwwww", "request for version ${requestParams.version}")
        val id = "img" + Random.nextInt(1, 5)
        Log.d("wwwwww", "returning $id")
        return Resources.Builder()
            .setVersion(requestParams.version)
            .addIdToImageMapping(id, R.drawable.ic_search_24)
            .build()
    }
}

@OptIn(ProtoLayoutExperimental::class)
fun myModifier() =
    //    ModifiersBuilders.DefaultContentTransitions.fadeIn()
    LayoutModifier.enterTransition(ModifiersBuilders.DefaultContentTransitions.fadeIn())
/*
    LayoutModifier.transition(
        enter = DefaultContentTransitions.fadeIn(),
        exit = DefaultContentTransitions.fadeOut()
    )
*/

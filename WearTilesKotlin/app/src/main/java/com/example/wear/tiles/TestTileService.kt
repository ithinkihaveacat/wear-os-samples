package com.example.wear.tiles

import android.content.Context
import android.util.Log
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material3.Typography.BODY_LARGE
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
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
                        if (USE_M3_LAYOUT) {
                            layoutM3(this, requestParams.deviceConfiguration)
                        } else {
                            layoutM2(this, requestParams.deviceConfiguration)
                        }
                    )
                )
                .build()
        )

    fun layoutM2(context: Context, deviceConfiguration: DeviceParametersBuilders.DeviceParameters) =
        Text.Builder(this, "Hello World!")
            .setTypography(Typography.TYPOGRAPHY_BODY1)
            .setColor(argb(0xFFFFFFFF.toInt()))
            .build()

    fun layoutM3(context: Context, deviceConfiguration: DeviceParametersBuilders.DeviceParameters) =
        materialScope(context, deviceConfiguration) {
            primaryLayout(
                mainSlot = { text(text = "Hello, World!".layoutString, typography = BODY_LARGE) }
            )
        }

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

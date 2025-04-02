package com.example.wear.tiles

import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ButtonColors
import androidx.wear.protolayout.material.ChipColors
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.example.wear.tiles.golden.GoldenTilesColors
import com.example.wear.tiles.golden.Contact
import com.example.wear.tiles.tools.emptyClickable
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.UUID
import kotlin.random.Random
import com.example.wear.tiles.tools.addIdToImageMapping

class TestTileService : SuspendingTileService() {
  override suspend fun tileRequest(requestParams: TileRequest): Tile {
    var layoutElement =
        PrimaryLayout.Builder(requestParams.deviceConfiguration)
            .setResponsiveContentInsetEnabled(true)
            .setContent(
                MultiButtonLayout.Builder()
                    .addButtonContent(
                        Button.Builder(application, emptyClickable).setImageContent("img1").build())
                    .addButtonContent(
                        Button.Builder(application, emptyClickable).setImageContent("img2").build())
                    .addButtonContent(
                        Button.Builder(application, emptyClickable).setImageContent("img3").build())
                    .addButtonContent(
                        Button.Builder(application, emptyClickable).setImageContent("img4").build())
                    .build())
            .setPrimaryChipContent(
                CompactChip.Builder(
                        application,
                        "Reload",
                        Clickable.Builder()
                            .setId("reload")
                            .setOnClick(LoadAction.Builder().build())
                            .build(),
                        requestParams.deviceConfiguration)
                    .build())
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

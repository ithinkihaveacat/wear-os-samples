package com.example.wear.tiles.golden

import android.content.Context
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.util.UUID

abstract class BaseTileService : TileService() {

  override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<Tile> =
    Futures.immediateFuture(
      Tile.Builder()
        .setResourcesVersion(UUID.randomUUID().toString()) // random string; resources will be loaded every time
        .setTileTimeline(
          Timeline.fromLayoutElement(layout(this, requestParams.deviceConfiguration))
        )
        .build()
    )

  override fun onTileResourcesRequest(
    requestParams: ResourcesRequest
  ): ListenableFuture<Resources> = Futures.immediateFuture(resources(this)(requestParams))

  abstract fun layout(
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
  ): LayoutElementBuilders.LayoutElement

  abstract fun resources(context: Context): (ResourcesRequest) -> Resources
}

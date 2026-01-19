package com.example.wear.tiles

import android.util.Log
import android.content.Context
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.iconButton
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.example.wear.tiles.R
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.UUID

class CatalogService : SuspendingTileService() {
    private val RESOURCES_VERSION = "3"

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        val state = getCatalogState()
        Log.d("CatalogService", "tileRequest: processing request for layout '${state.layoutName}'")
        
        val layout = materialScope(this, requestParams.deviceConfiguration) {
            when (state.layoutName) {
                "textButton" -> textButtonLayout()
                "iconButton" -> iconButtonLayout(this@CatalogService)
                else -> iconButtonLayout(this@CatalogService)
            }
        }
        
        // Use random version for debugging to force refresh
        val resourcesVersion = UUID.randomUUID().toString()
        Log.d("CatalogService", "tileRequest: generated tile with resource version $resourcesVersion")

        return TileBuilders.Tile.Builder()
            .setResourcesVersion(resourcesVersion)
            .setTileTimeline(
                TimelineBuilders.Timeline.fromLayoutElement(layout)
            )
            .build()
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        Log.d("CatalogService", "resourcesRequest: serving resources for version ${requestParams.version}")
        return ResourceBuilders.Resources.Builder()
            .setVersion(requestParams.version)
            .addIdToImageMapping(
                this.resources.getResourceName(R.drawable.ic_message_24),
                ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.ic_message_24)
                            .build()
                    )
                    .build()
            )
            .build()
    }

    private fun MaterialScope.textButtonLayout(): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                textButton(
                    onClick = clickable(id = "text_button_click"),
                    labelContent = { text("Text Button".layoutString) }
                )
            }
        )

    private fun MaterialScope.iconButtonLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                iconButton(
                    onClick = clickable(id = "icon_button_click"),
                    iconContent = {
                        icon(
                            protoLayoutResourceId =
                            context.resources.getResourceName(R.drawable.ic_message_24)
                        )
                    }
                )
            }
        )
}
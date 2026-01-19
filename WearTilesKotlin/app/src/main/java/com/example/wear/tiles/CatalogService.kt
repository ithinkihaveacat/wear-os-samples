package com.example.wear.tiles

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

class CatalogService : SuspendingTileService() {
    private val RESOURCES_VERSION = "3"

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        val state = getCatalogState()
        val layout = materialScope(this, requestParams.deviceConfiguration) {
            when (state.layoutName) {
                "textButton" -> textButtonLayout()
                "iconButton" -> iconButtonLayout(this@CatalogService)
                else -> iconButtonLayout(this@CatalogService)
            }
        }
        
        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                TimelineBuilders.Timeline.fromLayoutElement(layout)
            )
            .build()
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
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
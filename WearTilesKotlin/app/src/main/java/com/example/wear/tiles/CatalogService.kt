package com.example.wear.tiles

import android.util.Log
import android.content.Context
import androidx.annotation.OptIn
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.CONTENT_SCALE_MODE_CROP
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.appCard
import androidx.wear.protolayout.material3.avatarButton
import androidx.wear.protolayout.material3.avatarImage
import androidx.wear.protolayout.material3.backgroundImage
import androidx.wear.protolayout.material3.button
import androidx.wear.protolayout.material3.circularProgressIndicator
import androidx.wear.protolayout.material3.compactButton
import androidx.wear.protolayout.material3.graphicDataCard
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.iconButton
import androidx.wear.protolayout.material3.iconDataCard
import androidx.wear.protolayout.material3.iconEdgeButton
import androidx.wear.protolayout.material3.imageButton
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.segmentedCircularProgressIndicator
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textDataCard
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.material3.titleCard
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.clip
import androidx.wear.protolayout.modifiers.toProtoLayoutModifiers
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.example.wear.tiles.R
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.UUID

class CatalogService : SuspendingTileService() {
    private val RESOURCES_VERSION = "8"

    @OptIn(ProtoLayoutExperimental::class)
    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        val state = getCatalogState()
        Log.d("CatalogService", "tileRequest: processing request for layout '${state.layoutName}'")
        
        val layout = materialScope(this, requestParams.deviceConfiguration) {
            when (state.layoutName) {
                "textButton" -> textButtonLayout()
                "iconButton" -> iconButtonLayout(this@CatalogService)
                "avatarButton" -> avatarButtonLayout(this@CatalogService)
                "imageButton" -> imageButtonLayout(this@CatalogService)
                "compactButton" -> compactButtonLayout(this@CatalogService)
                "button" -> buttonLayout(this@CatalogService)
                "iconEdgeButton" -> iconEdgeButtonLayout(this@CatalogService)
                "textEdgeButton" -> textEdgeButtonLayout()
                "titleCard" -> titleCardLayout(this@CatalogService)
                "appCard" -> appCardLayout(this@CatalogService)
                "textDataCard" -> textDataCardLayout(this@CatalogService)
                "iconDataCard" -> iconDataCardLayout(this@CatalogService)
                "graphicDataCard" -> graphicDataCardLayout(this@CatalogService)
                "circularProgressIndicator" -> circularProgressIndicatorLayout()
                "segmentedCircularProgressIndicator" -> segmentedCircularProgressIndicatorLayout()
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
            .addIdToImageMapping(
                this.resources.getResourceName(R.drawable.avatar1),
                ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.avatar1)
                            .build()
                    )
                    .build()
            )
            .addIdToImageMapping(
                this.resources.getResourceName(R.drawable.photo_14),
                ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.photo_14)
                            .build()
                    )
                    .build()
            )
            .addIdToImageMapping(
                this.resources.getResourceName(R.drawable.ali),
                ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.ali)
                            .build()
                    )
                    .build()
            )
            .addIdToImageMapping(
                this.resources.getResourceName(R.drawable.ic_run_24),
                ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.ic_run_24)
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

    @OptIn(ProtoLayoutExperimental::class)
    private fun MaterialScope.avatarButtonLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                avatarButton(
                    onClick = clickable(id = "avatar_button_click"),
                    avatarContent = {
                        avatarImage(
                            protoLayoutResourceId = context.resources.getResourceName(R.drawable.ali),
                            contentScaleMode = CONTENT_SCALE_MODE_CROP
                        )
                    },
                    labelContent = { text("Avatar Button".layoutString) },
                    secondaryLabelContent = { text("Secondary Label".layoutString) }
                )
            }
        )

    private fun MaterialScope.imageButtonLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                imageButton(
                    onClick = clickable(id = "image_button_click"),
                    backgroundContent = {
                        backgroundImage(
                            protoLayoutResourceId =
                            context.resources.getResourceName(R.drawable.photo_14)
                        )
                    }
                )
            }
        )

    private fun MaterialScope.compactButtonLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                compactButton(
                    onClick = clickable(id = "compact_button_click"),
                    iconContent = {
                         icon(
                            protoLayoutResourceId =
                            context.resources.getResourceName(R.drawable.ic_message_24)
                        )
                    },
                    labelContent = { text("Compact".layoutString) }
                )
            }
        )

    private fun MaterialScope.buttonLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                button(
                    onClick = clickable(id = "button_click"),
                    labelContent = { text("Button".layoutString) },
                    secondaryLabelContent = { text("Secondary Label".layoutString) },
                    iconContent = {
                        icon(
                            protoLayoutResourceId =
                            context.resources.getResourceName(R.drawable.ic_message_24)
                        )
                    }
                )
            }
        )

    private fun MaterialScope.iconEdgeButtonLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = { text("Content".layoutString) },
            bottomSlot = {
                iconEdgeButton(
                    onClick = clickable(id = "icon_edge_button_click"),
                    iconContent = {
                        icon(
                            protoLayoutResourceId =
                            context.resources.getResourceName(R.drawable.ic_message_24)
                        )
                    }
                )
            }
        )

    private fun MaterialScope.textEdgeButtonLayout(): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = { text("Content".layoutString) },
            bottomSlot = {
                textEdgeButton(
                    onClick = clickable(id = "text_edge_button_click"),
                    labelContent = { text("Edge Button".layoutString) }
                )
            }
        )

    private fun MaterialScope.titleCardLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                titleCard(
                    onClick = clickable(id = "title_card_click"),
                    title = { text("Title Card".layoutString) },
                    content = { text("Content".layoutString) }
                )
            }
        )

    private fun MaterialScope.appCardLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                appCard(
                    onClick = clickable(id = "app_card_click"),
                    label = { text("Ali".layoutString) },
                    title = { text("Dinner in SF".layoutString, maxLines = 1) },
                    time = { text("2:03 PM".layoutString) },
                    avatar = {
                        avatarImage(
                            protoLayoutResourceId = context.resources.getResourceName(R.drawable.ali),
                            contentScaleMode = CONTENT_SCALE_MODE_CROP
                        )
                    },
                    content = { text("Let's try that new restaurant.".layoutString) }
                )
            }
        )

    private fun MaterialScope.textDataCardLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                textDataCard(
                    onClick = clickable(id = "text_data_card_click"),
                    title = { text("Text Data".layoutString) },
                    content = { text("Content".layoutString) }
                )
            }
        )

    private fun MaterialScope.iconDataCardLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                iconDataCard(
                    onClick = clickable(id = "icon_data_card_click"),
                    title = { text("Icon Data".layoutString) },
                    content = { text("Content".layoutString) },
                    secondaryIcon = { 
                        icon(
                            protoLayoutResourceId =
                            context.resources.getResourceName(R.drawable.ic_message_24)
                        )
                    }
                )
            }
        )

    private fun MaterialScope.graphicDataCardLayout(context: Context): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                graphicDataCard(
                    onClick = clickable(id = "graphic_data_card_click"),
                    graphic = {
                        icon(
                            protoLayoutResourceId =
                            context.resources.getResourceName(R.drawable.ic_run_24)
                        )
                    },
                    title = { text("Graphic Data".layoutString) },
                    content = { text("Content".layoutString) }
                )
            }
        )

    private fun MaterialScope.circularProgressIndicatorLayout(): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                graphicDataCard(
                    onClick = clickable(id = "circular_progress_click"),
                    graphic = {
                        circularProgressIndicator(
                            staticProgress = 0.75f,
                            startAngleDegrees = 0f,
                            endAngleDegrees = 360f
                        )
                    },
                    title = { text("75%".layoutString) },
                    content = { text("Progress".layoutString) }
                )
            }
        )

    private fun MaterialScope.segmentedCircularProgressIndicatorLayout(): LayoutElementBuilders.LayoutElement =
        primaryLayout(
            mainSlot = {
                graphicDataCard(
                    onClick = clickable(id = "segmented_progress_click"),
                    graphic = {
                        segmentedCircularProgressIndicator(
                            segmentCount = 5,
                            staticProgress = 0.6f
                        )
                    },
                    title = { text("3/5".layoutString) },
                    content = { text("Segments".layoutString) }
                )
            }
        )
}

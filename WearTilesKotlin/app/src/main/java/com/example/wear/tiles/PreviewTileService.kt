package com.example.wear.tiles

import android.content.Context
import android.util.Log
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material3.ButtonColors
import androidx.wear.protolayout.material3.PrimaryLayoutMargins.Companion.MAX_PRIMARY_LAYOUT_MARGIN
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.example.wear.tiles.golden.Meditation
import com.example.wear.tiles.golden.News
import com.example.wear.tiles.golden.Social
import com.example.wear.tiles.golden.mockContacts
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.emptyClickable
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.UUID

class PreviewTileService : SuspendingTileService() {
    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        val meditationLayoutElement =
            Meditation.chipsLayout(
                this,
                requestParams.deviceConfiguration,
                numOfLeftTasks = 2,
                session1 =
                    Meditation.Session(
                        label = "Breathe",
                        iconId = Meditation.CHIP_1_ICON_ID,
                        clickable = emptyClickable,
                    ),
                session2 =
                    Meditation.Session(
                        label = "Daily mindfulness",
                        iconId = Meditation.CHIP_2_ICON_ID,
                        clickable = emptyClickable,
                    ),
                browseClickable = emptyClickable,
            )
        val newsLayoutElement =
            News.layout(
                context = this,
                deviceParameters = requestParams.deviceConfiguration,
                headline = "Millions still without power as new storm moves across the US",
                newsVendor = "The New York Times",
                clickable = emptyClickable,
                date = "Today, 31 July",
            )
        val socialLayoutElement =
            Social.layout(
                context = this,
                deviceParameters = requestParams.deviceConfiguration,
                contacts = mockContacts(),
            )
        val helloLayoutElement = helloLayout(this, requestParams.deviceConfiguration)
        val layoutElement = meditationLayoutElement
        val resourcesVersion = UUID.randomUUID().toString() // random to force resource request
        return Tile.Builder()
            .setResourcesVersion(resourcesVersion)
            .setTileTimeline(Timeline.fromLayoutElement(layoutElement))
            .build()
    }

    override suspend fun resourcesRequest(requestParams: ResourcesRequest): Resources {
        Log.d("wwwwww", "request for version ${requestParams.version}")
        return Resources.Builder()
            .setVersion(requestParams.version)
            .apply {
              addIdToImageMapping("news_image", R.drawable.news)
                mockContacts().forEach {
                    if (it.avatarId != null && it.avatarResource != null) {
                        addIdToImageMapping(it.avatarId, it.avatarResource)
                    }
                }
            }
            .build()
    }
}

fun helloLayout(context: Context, deviceConfiguration: DeviceParametersBuilders.DeviceParameters) =
    materialScope(
        context = context,
        deviceConfiguration = deviceConfiguration,
        allowDynamicTheme = false,
    ) {
        primaryLayout(
            margins = MAX_PRIMARY_LAYOUT_MARGIN,
            titleSlot = { text("Hello, World!".layoutString) },
            mainSlot = {
                textButton(
                    height = expand(),
                    width = expand(),
                    onClick = emptyClickable,
                    shape = shapes.small,
                    colors =
                        // Distinguish from the edge button
                        ButtonColors(
                            containerColor = colorScheme.secondaryContainer,
                            labelColor = colorScheme.onSecondaryContainer,
                        ),
                    labelContent = { text("Max Margin".layoutString) },
                )
            },
            bottomSlot = {
                textEdgeButton(
                    onClick = emptyClickable,
                    labelContent = { text("Edge".layoutString) },
                )
            },
        )
    }


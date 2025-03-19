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
import com.example.wear.tiles.golden.Meditation
import com.example.wear.tiles.golden.News
import com.example.wear.tiles.golden.Social.Contact
import com.example.wear.tiles.tools.emptyClickable
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.UUID
import kotlin.random.Random
import com.example.wear.tiles.tools.addIdToImageMapping
import com.google.android.horologist.tiles.images.drawableResToImageResource

class PreviewTileService : SuspendingTileService() {
    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        val meditationLayoutElement = Meditation.chipsLayout(
            this,
            requestParams.deviceConfiguration,
            numOfLeftTasks = 2,
            session1 = Meditation.Session(
                label = "Breathe",
                iconId = Meditation.CHIP_1_ICON_ID,
                clickable = emptyClickable
            ),
            session2 = Meditation.Session(
                label = "Daily mindfulness",
                iconId = Meditation.CHIP_2_ICON_ID,
                clickable = emptyClickable
            ),
            browseClickable = emptyClickable
        )
        val newsLayoutElement = News.layout(
            context = this,
            deviceParameters = requestParams.deviceConfiguration,
            headline = "Millions still without power as new storm moves across the US",
            newsVendor = "The New York Times",
            clickable = emptyClickable,
            date = "Today, 31 July"
        )
        val layoutElement = newsLayoutElement
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
            .addIdToImageMapping(
                Meditation.CHIP_1_ICON_ID,
                R.drawable.ic_breathe_24
            )
            .addIdToImageMapping(
                Meditation.CHIP_2_ICON_ID,
                R.drawable.ic_mindfulness_24
            )
            .addIdToImageMapping(
                "news_image",
                R.drawable.news
            )
            .build()
    }
}

private fun button(context: Context, contact: Contact) =
    Button.Builder(context, contact.clickable)
        .apply {
            if (contact.avatarId != null) {
                setImageContent(contact.avatarId)
            } else {
                setTextContent(contact.initials, Typography.TYPOGRAPHY_TITLE3)
            }
        }
        .setButtonColors(
            ButtonColors(
                /* backgroundColor = */ ColorBuilders.argb(contact.color),
                /* contentColor = */ ColorBuilders.argb(GoldenTilesColors.DarkerGray)))
        .build()

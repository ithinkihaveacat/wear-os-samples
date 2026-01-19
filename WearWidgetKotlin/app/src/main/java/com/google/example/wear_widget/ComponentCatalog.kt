@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.*
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.asRdp
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonDefaults
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteImage
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

class ComponentCatalogService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = ComponentCatalog()
}

class ComponentCatalog : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val state = context.getComponentCatalogState()
        Log.d("ComponentCatalog", "provideWidgetData: layoutName='${state.layoutName}'")
        return WearWidgetDocument(backgroundColor = Color.Black) {
            when (state.layoutName) {
                "textButton" -> ComponentCatalogTextButtonSample()
                "iconButton" -> ComponentCatalogIconButtonSample()
                "avatarButton" -> ComponentCatalogAvatarButtonSample()
                "imageButton" -> ComponentCatalogImageButtonSample()
                "compactButton" -> ComponentCatalogCompactButtonSample()
                "titleCard" -> ComponentCatalogTitleCardSample()
                "appCard" -> ComponentCatalogAppCardSample()
                else -> ComponentCatalogTextButtonSample()
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogTextButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteButton(onClick = arrayOf()) {
            MaterialRemoteText(text = "Text Button".rs)
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogIconButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteButton(
            onClick = arrayOf()
        ) {
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
                contentDescription = "Message".rs,
                modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize)
            )
        }
    }
}

// TODO (b/474292165): This seems to not work (renders as black screen).
@RemoteComposable
@Composable
fun ComponentCatalogAvatarButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteButton(
            onClick = arrayOf()
        ) {
            RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                RemoteImage(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                    contentDescription = "Avatar".rs,
                    contentScale = ContentScale.Crop,
                    modifier = RemoteModifier.size(RemoteButtonDefaults.LargeIconSize).clip(RoundedCornerShape(percent = 50))
                )
                // Spacer
                RemoteBox(modifier = RemoteModifier.size(8.dp.asRdp())) 
                // Texts
                MaterialRemoteText("Avatar Button".rs)
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogImageButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        // Simulating ImageButton with a box and background image since RemoteButton doesn't support background images directly yet
        RemoteBox(
            modifier = RemoteModifier
                .size(60.rdp) // Hardcoded size
                .clip(RoundedCornerShape(percent = 50)), // Circle
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center
        ) {
             RemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
                contentDescription = "Background".rs,
                contentScale = ContentScale.Crop,
                modifier = RemoteModifier.fillMaxSize()
            )
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogCompactButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteButton(
            onClick = arrayOf(),
             icon = {
                RemoteIcon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_message_24),
                    contentDescription = "Message".rs,
                    modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize)
                )
            },
            label = { MaterialRemoteText("Compact".rs) }
        )
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogTitleCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        // Simplified Title Card: Standard RemoteButton with column content
        RemoteButton(
            onClick = arrayOf() // Clickable for now to test rendering
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText(
                    text = "Title Card".rs,
                    fontWeight = FontWeight.Bold
                )
                MaterialRemoteText(
                    text = "Content".rs
                )
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogAppCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteButton(
            onClick = arrayOf()
        ) {
            RemoteColumn {
                RemoteRow(
                    verticalAlignment = RemoteAlignment.CenterVertically
                ) {
                    RemoteImage(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                        contentDescription = "Avatar".rs,
                        contentScale = ContentScale.Crop,
                        modifier = RemoteModifier.size(24.rdp).clip(RoundedCornerShape(percent = 50))
                    )
                    RemoteBox(modifier = RemoteModifier.size(8.rdp))
                    MaterialRemoteText("Ali".rs)
                    RemoteBox(modifier = RemoteModifier.size(20.rdp)) 
                    MaterialRemoteText("2:03 PM".rs)
                }
                MaterialRemoteText(
                    text = "Dinner in SF".rs,
                    fontWeight = FontWeight.Bold
                )
                MaterialRemoteText(
                    text = "Let's try that new restaurant.".rs
                )
            }
        }
    }
}
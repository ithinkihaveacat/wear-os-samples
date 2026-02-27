package com.example.wear.tiles.messaging

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.action.PendingIntentAction
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteTextButton
import androidx.wear.compose.remote.material3.RemoteButtonDefaults
import androidx.wear.compose.remote.material3.RemoteTextButtonDefaults
import android.app.PendingIntent
import android.content.Intent
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.foundation.shape.CircleShape
import androidx.wear.compose.remote.material3.RemoteImage

@SuppressLint("RestrictedApi")
class MessagingWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = MessagingWidget()
}

@SuppressLint("RestrictedApi")
class MessagingWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        // Match Tile logic: take 6 contacts (or 4 on smaller screens, but let's default to 6 for now)
        val contacts = getMockLocalContacts().take(6)
        return WearWidgetDocument(backgroundColor = Color.Black) {
            MessagingWidgetContent(context, contacts)
        }
    }
}

@SuppressLint("RestrictedApi")
@RemoteComposable
@Composable
fun MessagingWidgetContent(context: Context, contacts: List<Contact>) {
    // Chunk contacts into rows of 3
    val rows = contacts.chunked(3)
    val showTitle = rows.size <= 1

    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center // Center content vertically
            ) {
                // Title (only if 1 row)
                if (showTitle) {
                     RemoteText(
                        text = "Contacts".rs,
                        style = MyWidgetTypography.titleMedium,
                        color = RemoteMaterialTheme.colorScheme.onSurface
                    )
                }
                
                // Grid Rows
                rows.forEach { rowContacts ->
                    RemoteRow(
                        horizontalArrangement = RemoteArrangement.CenterHorizontally,
                        verticalAlignment = RemoteAlignment.CenterVertically
                    ) {
                        rowContacts.forEach { contact ->
                            ContactButton(context, contact)
                        }
                    }
                }

                // "More" Button
                val intent = Intent(context, MessagingTileService::class.java) // Dummy intent
                val pendingIntent = PendingIntent.getService(context, 100, intent, PendingIntent.FLAG_IMMUTABLE)
                val action = PendingIntentAction(pendingIntent)
                
                RemoteTextButton(
                    onClick = action,
                    colors = RemoteTextButtonDefaults.textButtonColors(containerColor = RemoteMaterialTheme.colorScheme.surfaceContainer),
                    modifier = RemoteModifier.padding(top = 4.dp).size(width = 60.rdp, height = 30.rdp) // Adjust size as needed
                ) {
                    RemoteText(text = "More".rs)
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@RemoteComposable
@Composable
fun ContactButton(context: Context, contact: Contact) {
    val intent = Intent(context, MessagingTileService::class.java) // Dummy intent
    val pendingIntent = PendingIntent.getService(context, contact.id.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
    val action = PendingIntentAction(pendingIntent)
    
    // Determine colors based on index/hash (Simple cycling logic)
    val index = contact.initials.hashCode()
    val containerColor = when (Math.abs(index) % 3) {
        0 -> RemoteMaterialTheme.colorScheme.primary
        1 -> RemoteMaterialTheme.colorScheme.secondary
        else -> RemoteMaterialTheme.colorScheme.tertiary
    }
    val contentColor = when (Math.abs(index) % 3) {
        0 -> RemoteMaterialTheme.colorScheme.onPrimary
        1 -> RemoteMaterialTheme.colorScheme.onSecondary
        else -> RemoteMaterialTheme.colorScheme.onTertiary
    }

    if (contact.avatarSource is AvatarSource.Resource) {
        val originalBitmap = BitmapFactory.decodeResource(context.resources, contact.avatarSource.resourceId)
        val density = context.resources.displayMetrics.density
        val sizePx = (48 * density).toInt()
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, sizePx, sizePx, true)
        val imageBitmap = scaledBitmap.asImageBitmap()

        RemoteBox(
             modifier = RemoteModifier
                .padding(4.dp)
                .size(48.rdp)
                .clip(CircleShape, DpSize(48.dp, 48.dp)), // Clip to circle
             horizontalAlignment = RemoteAlignment.CenterHorizontally,
             verticalArrangement = RemoteArrangement.Center
        ) {
             RemoteImage(
                 bitmap = imageBitmap,
                 contentDescription = contact.name.rs,
                 contentScale = ContentScale.Crop,
                 modifier = RemoteModifier.fillMaxSize()
             )
             
             // Overlay a transparent button for click action
              RemoteTextButton(
                onClick = action,
                modifier = RemoteModifier.fillMaxSize(),
                colors = RemoteTextButtonDefaults.textButtonColors(containerColor = Color.Transparent.rc)
            ) {
                 // Empty content
            }
        }

    } else {
        // Initials Avatar
        RemoteTextButton(
            onClick = action,
            colors = RemoteTextButtonDefaults.textButtonColors(containerColor = containerColor, contentColor = contentColor),
            modifier = RemoteModifier.padding(4.dp).size(48.rdp)
        ) {
            RemoteText(text = contact.initials.rs)
        }
    }
}

package com.example.wear.tiles.messaging

import android.annotation.SuppressLint
import android.content.Context
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
import android.app.PendingIntent
import android.content.Intent

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
        val contacts = getMockLocalContacts().take(4)
        return WearWidgetDocument(backgroundColor = Color.Black) {
            MessagingWidgetContent(context, contacts)
        }
    }
}

@SuppressLint("RestrictedApi")
@RemoteComposable
@Composable
fun MessagingWidgetContent(context: Context, contacts: List<Contact>) {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center
            ) {
                RemoteRow(
                    horizontalArrangement = RemoteArrangement.CenterHorizontally,
                    verticalAlignment = RemoteAlignment.CenterVertically
                ) {
                    contacts.forEach { contact ->
                        ContactButton(context, contact)
                    }
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

    RemoteTextButton(
        onClick = action,
        modifier = RemoteModifier.padding(4.dp).size(48.rdp)
    ) {
        RemoteText(text = contact.initials.rs, color = Color.White.rc)
    }
}

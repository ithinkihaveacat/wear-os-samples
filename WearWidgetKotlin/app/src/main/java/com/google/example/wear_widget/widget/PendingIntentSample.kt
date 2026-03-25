
package com.google.example.wear_widget.widget


import android.app.PendingIntent
import android.content.Intent
import androidx.compose.remote.creation.compose.action.pendingIntentAction
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier
import com.google.example.wear_widget.MainActivity

/**
 * A Wear OS widget design features an Android robot logo above "Wear Widget" text. Below, a dark
 * gray rounded rectangle container, representing the widget, frames a central light purple-blue
 * rounded button with "Open App" in dark text, all on a black background.
 */
@RemoteComposable
@Composable
fun PendingIntentSample() {
val context = androidx.compose.ui.platform.LocalContext.current
val intent =
    Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
val pendingIntent =
    PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            modifier = RemoteModifier.buttonSizeModifier(),
            onClick = pendingIntentAction(pendingIntent),
        ) {
            MaterialRemoteText("Open App".rs)
        }
    }
}


package com.google.example.wear_widget.widget


import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * A screen displaying "Wear Widget" text, topped by an Android icon. Below, a dark gray rounded
 * rectangular widget displays "Item 1" (light gray), "Item 2" (yellow, centered), and "Item 3"
 * (light gray). Item 2 is visually prominent, indicating selection.
 */
@RemoteComposable
@Composable
fun RowSample2() {
// WORKAROUND: Replaced RemoteCollapsibleRow with RemoteRow due to an "Invalid enum value:
// Orientation"
// error when rendering the RemoteCollapsibleRow. It seems the RemoteCollapsibleRow's
// orientation parameter was not being correctly handled by the renderer.
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteRow(
            modifier = RemoteModifier.fillMaxSize().padding(5.rdp),
            horizontalArrangement = RemoteArrangement.SpaceBetween,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            RemoteText("Item 1", color = Color.White.rc)
            RemoteText("Item 2", color = Color.Yellow.rc)
            RemoteText("Item 3", color = Color.Gray.rc)
        }
    }
}

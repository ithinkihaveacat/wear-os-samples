@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.fillMaxWidth
import androidx.compose.remote.creation.compose.modifier.rememberRemoteScrollState
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.modifier.verticalScroll
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * A UI displays an Android logo and "Wear Widget" title. Below, a dark grey rounded widget shows a
 * vertical list of white text: "Header", "Item 0", "Item 1", "Item 2", "Item 3", with "Item 4"
 * partially visible, indicating scrollable content.
 */
@RemoteComposable
@Composable
fun VerticalScrollSample() {
val scrollState = rememberRemoteScrollState()
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Top,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxWidth().verticalScroll(scrollState),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Top,
        ) {
            MaterialRemoteText("Header".rs)
            RemoteBox(RemoteModifier.size(10.rdp))
            for (i in 0 until 10) {
                MaterialRemoteText(("Item " + i).rs)
                RemoteBox(RemoteModifier.size(10.rdp))
            }
            MaterialRemoteText("Footer".rs)
        }
    }
}

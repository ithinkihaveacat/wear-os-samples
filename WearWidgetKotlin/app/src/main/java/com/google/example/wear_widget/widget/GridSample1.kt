@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * Screenshot showing a "Wear Widget" title and Android Wear OS icon above a gray-bordered frame.
 * Inside the frame is a 2x2 grid of colored blocks: top-left red with "1", top-right blue with "2",
 * bottom-left green with "3", and bottom-right yellow with "4".
 */
@RemoteComposable
@Composable
fun GridSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize().padding(20.dp),
            verticalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            RemoteRow(modifier = RemoteModifier.weight(1f)) {
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Red),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("1", color = Color.White.rc)
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Blue),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("2", color = Color.White.rc)
                }
            }
            RemoteRow(modifier = RemoteModifier.weight(1f)) {
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Green),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("3", color = Color.Black.rc)
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Yellow),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("4", color = Color.Black.rc)
                }
            }
        }
    }
}

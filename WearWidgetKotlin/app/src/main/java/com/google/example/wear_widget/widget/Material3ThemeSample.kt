@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.state.rf
import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteColumnScope
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteRowScope
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxHeight
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.fillMaxWidth
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * Android robot logo above 'Wear Widget' text. Below is a rounded rectangular widget with a 5x4
 * grid of color swatches: shades of red, blue-grey, purple, and a bottom row of dark grey, medium
 * grey, light grey, black, and white.
 */
@RemoteComposable
@Composable
fun Material3ThemeSample() {
    RemoteColumn(modifier = RemoteModifier.fillMaxSize()) {
        RemoteColorRow {
            RemoteColorBox(RemoteMaterialTheme.colorScheme.error)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.errorDim)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.errorContainer)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onError)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onErrorContainer)
        }
        RemoteColorRow {
            RemoteColorBox(RemoteMaterialTheme.colorScheme.primary)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.primaryDim)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.primaryContainer)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onPrimary)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onPrimaryContainer)
        }
        RemoteColorRow {
            RemoteColorBox(RemoteMaterialTheme.colorScheme.secondary)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.secondaryDim)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.secondaryContainer)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onSecondary)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onSecondaryContainer)
        }
        RemoteColorRow {
            RemoteColorBox(RemoteMaterialTheme.colorScheme.tertiary)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.tertiaryDim)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.tertiaryContainer)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onTertiary)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onTertiaryContainer)
        }
        RemoteColorRow {
            RemoteColorBox(RemoteMaterialTheme.colorScheme.surfaceContainer)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.surfaceContainerLow)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.surfaceContainerHigh)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onSurface)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onSurfaceVariant)
        }
        RemoteColorRow {
            RemoteColorBox(RemoteMaterialTheme.colorScheme.outline)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.outlineVariant)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.background)
            RemoteColorBox(RemoteMaterialTheme.colorScheme.onBackground)
        }
    }
}

@RemoteComposable
@Composable
private fun RemoteColumnScope.RemoteColorRow(
content: @RemoteComposable @Composable RemoteRowScope.() -> Unit
) {
RemoteRow(modifier = RemoteModifier.fillMaxWidth().weight(1f), content = content)
}

@RemoteComposable
@Composable
private fun RemoteRowScope.RemoteColorBox(color: RemoteColor) {
RemoteBox(modifier = RemoteModifier.fillMaxHeight().weight(1f).background(color))
}

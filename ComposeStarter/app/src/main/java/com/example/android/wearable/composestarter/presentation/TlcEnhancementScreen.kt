package com.example.android.wearable.composestarter.presentation

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.SwitchButtonDefaults
import androidx.wear.compose.material3.Text

@Composable
fun TlcEnhancementScreen() {
    var reverseLayout by remember { mutableStateOf(false) }
    var useSnapping by remember { mutableStateOf(false) }

    val state = rememberTransformingLazyColumnState()

    ScreenScaffold(scrollState = state) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            flingBehavior = if (useSnapping) {
                TransformingLazyColumnDefaults.snapFlingBehavior(state)
            } else {
                ScrollableDefaults.flingBehavior()
            },
            rotaryScrollableBehavior = if (useSnapping) {
                RotaryScrollableDefaults.snapBehavior(state)
            } else {
                RotaryScrollableDefaults.behavior(state)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ListHeader {
                    Text("TLC Demo")
                }
            }
            item {
                SwitchButton(
                    checked = reverseLayout,
                    onCheckedChange = { reverseLayout = it },
                    label = { Text("Reverse Layout") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                SwitchButton(
                    checked = useSnapping,
                    onCheckedChange = { useSnapping = it },
                    label = { Text("Snapping") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(20) { index ->
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Item $index")
                }
            }
        }
    }
}

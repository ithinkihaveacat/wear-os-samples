package com.example.wear.tiles.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.CompactButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ProgressIndicatorDefaults
import androidx.wear.compose.material3.ShapeDefaults
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp(
                onInteractive = { runBlocking { getCounterState() } },
                onStateChange = { counterState -> runBlocking { setCounterState(counterState) } },
            )
        }
    }
}

@Composable
fun WearApp(onStateChange: (CounterState) -> Unit, onInteractive: () -> CounterState) {

    var count by remember { mutableIntStateOf(onInteractive().count) }

    // Persist changes to count
    LaunchedEffect(count) { onStateChange(CounterState(count)) }

    val lifecycleOwner = LocalLifecycleOwner.current

    // Reload CounterState when resuming, in case it was changed by the Tile
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                count = onInteractive().count
            }
        }
    }

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                progress = { count / 10F },
                modifier = Modifier.fillMaxSize(),
                colors =
                    ProgressIndicatorDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                strokeWidth = 8.dp,
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Cups",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CounterCompactButton(
                        onClick = { count-- },
                        label = {
                            Text(text = "-1", style = MaterialTheme.typography.displayMedium)
                        },
                    )
                    Box(modifier = Modifier.size(10.dp))
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Box(modifier = Modifier.size(10.dp))
                    CounterCompactButton(
                        onClick = { count++ },
                        label = {
                            Text(text = "+1", style = MaterialTheme.typography.displayMedium)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun CounterCompactButton(onClick: () -> Unit, label: @Composable RowScope.() -> Unit) {
    CompactButton(
        shape = ShapeDefaults.Medium,
        onClick = onClick,
        modifier = Modifier.size(55.dp),
        colors =
            ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onTertiary,
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
        label = label,
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(onStateChange = {}, onInteractive = { CounterState(9) })
}

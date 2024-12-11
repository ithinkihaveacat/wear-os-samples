package com.example.wear.tiles.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.wear.tiles.golden.GoldenTilesColors
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
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                progress = count / 10F,
                modifier = Modifier.fillMaxSize(),
                indicatorColor = Color(GoldenTilesColors.Pink),
                trackColor = Color(GoldenTilesColors.DarkerGray),
                strokeWidth = 8.dp,
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Cups",
                    style = MaterialTheme.typography.display1,
                    color = Color(GoldenTilesColors.Yellow),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CounterCompactButton(onClick = { count-- }, content = { Text("-1") })
                    Text(
                        text = count.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(GoldenTilesColors.Yellow),
                    )
                    CounterCompactButton(onClick = { count++ }, content = { Text("+1") })
                }
            }
        }
    }
}

@Composable
fun CounterCompactButton(onClick: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    CompactButton(
        onClick = onClick,
        modifier = Modifier.size(60.dp),
        colors =
            ButtonDefaults.buttonColors(
                backgroundColor = Color(GoldenTilesColors.LightPurple),
                contentColor = Color(GoldenTilesColors.DarkerGray),
            ),
        content = content,
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(onStateChange = {}, onInteractive = { CounterState(9) })
}

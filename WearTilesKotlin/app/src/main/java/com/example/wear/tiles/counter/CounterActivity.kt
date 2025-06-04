package com.example.wear.tiles.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.CompactButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ProgressIndicatorDefaults
import androidx.wear.compose.material3.ShapeDefaults
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import kotlinx.coroutines.launch

class CounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            val viewModel: CounterViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            WearApp(
                uiState = uiState,
                onIncrement = viewModel::increment,
                onDecrement = viewModel::decrement,
                onRefresh = viewModel::refreshCount,
            )
        }
    }
}

@Composable
fun WearApp(
    uiState: CounterUiState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRefresh: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) { onRefresh() }
        }
    }

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            key(uiState.count) {
                CircularProgressIndicator(
                    progress = { uiState.count / 10F },
                    modifier = Modifier.fillMaxSize(),
                    colors =
                        ProgressIndicatorDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.onSecondary,
                        ),
                    strokeWidth = 8.dp,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Cups",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CounterButton(
                        onClick = onDecrement,
                        label = {
                            Text(text = "−1", style = MaterialTheme.typography.displayMedium)
                        },
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = uiState.count.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(10.dp))
                    CounterButton(
                        onClick = onIncrement,
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
fun CounterButton(onClick: () -> Unit, label: @Composable RowScope.() -> Unit) {
    CompactButton(
        onClick = onClick,
        shape = ShapeDefaults.Small,
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
    WearApp(
        uiState = CounterUiState(count = 6, isLoading = false),
        onIncrement = {},
        onDecrement = {},
        onRefresh = {},
    )
}

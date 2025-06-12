/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.wear.tiles.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnScope
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CompactButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ProgressIndicatorDefaults
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SegmentedCircularProgressIndicator
import androidx.wear.compose.material3.ShapeDefaults
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.dynamicColorScheme
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

@Preview
@Composable
fun SmallSegmentedProgressIndicatorPreview() {
    SmallSegmentedProgressIndicator(3)
}

@Composable
fun SmallSegmentedProgressIndicator(progress: Int) {
    val segmentCount = 5
    SegmentedCircularProgressIndicator(
        segmentCount = segmentCount,
        segmentValue = { index -> index < progress },
        colors =
            ProgressIndicatorDefaults.colors(
                trackColor = MaterialTheme.colorScheme.onPrimary.withOpacity(0.2F),
                indicatorColor = MaterialTheme.colorScheme.onPrimary,
            ),
        modifier = Modifier.size(ButtonDefaults.LargeIconSize),
        strokeWidth = 5.dp,
    )
}

// Copied from ProtoLayout's LayoutColor.withOpacity()
private fun Color.withOpacity(@FloatRange(from = 0.0, to = 1.0) ratio: Float): Color {
    require(ratio in 0.0..1.0) { "Opacity ratio must be between 0.0 and 1.0." }
    val alpha = (ratio * 255).toInt()
    return Color(ColorUtils.setAlphaComponent(this.toArgb(), alpha))
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

    MaterialTransformingLazyColumn {
        item {
            ListHeader {
                Text(
                    text = "Water",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        item {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                icon = { SmallSegmentedProgressIndicator(uiState.count) },
                label = { Text(uiState.count.toString()) },
                secondaryLabel = { Text("Cups") },
            )
        }
        //        item {
        //            Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //                Row(verticalAlignment = Alignment.CenterVertically) {
        // //                                 Spacer(Modifier.width(10.dp))
        // //                                Text(
        // //                                    text = uiState.count.toString(),
        // //                                    style = MaterialTheme.typography.displayLarge,
        // //                                    color = MaterialTheme.colorScheme.primary,
        // //                                    modifier = Modifier.sizeIn(minWidth = 35.dp),
        // //                                    textAlign =
        // androidx.compose.ui.text.style.TextAlign.Center,
        // //                                )
        //                    TextButton(
        //                        onClick = { },
        //                        content = {
        //                            Text(
        //                                uiState.count.toString(),
        //                                style = MaterialTheme.typography.displayLarge
        //                            )
        //                        },
        //                        shapes = TextButtonShapes(
        //                            shape = MaterialTheme.shapes.medium
        //                        ),
        //                        colors = filledVariantTextButtonColors()
        //                    )
        //                    Spacer(Modifier.width(10.dp))
        //                    Box {
        //                        key(
        //                            uiState.count
        //                        ) { // need key() to recompose on state change
        //                            CircularProgressIndicator(
        //                                progress = { uiState.count / 10F },
        //                                modifier =
        //                                    Modifier
        //                                        .size(IconButtonDefaults.DefaultButtonSize)
        //                                        .align(Alignment.Center),
        //                                strokeWidth =
        //                                    CircularProgressIndicatorDefaults.largeStrokeWidth,
        //                                colors =
        //                                    ProgressIndicatorDefaults.colors(
        //                                        indicatorColor =
        //                                            MaterialTheme.colorScheme.secondary,
        //                                        trackColor =
        // MaterialTheme.colorScheme.onSecondary,
        //                                    ),
        //                            )
        //                        }
        //                    }
        //                }
        //
        //            }
        //        }
        item {
            Row {
                CounterButton(
                    onClick = onDecrement,
                    label = { Text(text = "−", style = MaterialTheme.typography.displayMedium) },
                )
                Spacer(Modifier.width(10.dp))
                CounterButton(
                    onClick = onIncrement,
                    label = { Text(text = "+", style = MaterialTheme.typography.displayMedium) },
                )
            }
        }
    }
}

@Composable
fun MaterialTransformingLazyColumn(content: TransformingLazyColumnScope.() -> Unit) {
    MaterialTheme(
        // Need to explicitly opt-in to the dynamic theme
        colorScheme = dynamicColorScheme(LocalContext.current) ?: MaterialTheme.colorScheme
    ) {
        AppScaffold {
            val scrollState = rememberTransformingLazyColumnState()

            ScreenScaffold(scrollState) { contentPadding ->
                TransformingLazyColumn(state = scrollState, contentPadding = contentPadding) {
                    content()
                }
            }
        }
    }
}

@Composable
fun CounterButton(onClick: () -> Unit, label: @Composable RowScope.() -> Unit) {
    CompactButton(
        onClick = onClick,
        modifier = Modifier.sizeIn(minHeight = 60.dp),
        shape = ShapeDefaults.Medium,
        colors =
            ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onTertiary,
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
        label = label,
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(
        uiState = CounterUiState(count = 6, isLoading = false),
        onIncrement = {},
        onDecrement = {},
        onRefresh = {},
    )
}

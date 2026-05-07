/*
 * Copyright 2026 The Android Open Source Project
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
package com.google.example.wear_widget

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch

class WeatherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            MaterialTheme {
                WeatherControlPanel(
                    onUpdate = { temp, cond ->
                        scope.launch {
                            setWeatherState(WeatherState(temp, cond))
                            WeatherWidget()
                                .triggerUpdate(
                                    this@WeatherActivity,
                                    ComponentName(
                                        this@WeatherActivity,
                                        WeatherWidgetService::class.java,
                                    ),
                                )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun WeatherControlPanel(onUpdate: (Int, String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Weather Push", style = MaterialTheme.typography.title3, textAlign = TextAlign.Center)
        Text(
            "Simulate weather sync",
            style = MaterialTheme.typography.caption2,
            modifier = Modifier.padding(vertical = 4.dp),
            textAlign = TextAlign.Center,
        )

        WeatherButton("Sunny", 72, "☀️", onUpdate)
        WeatherButton("Cloudy", 55, "☁️", onUpdate)
        WeatherButton("Rainy", 48, "🌧️", onUpdate)
        WeatherButton("Snowy", 28, "❄️", onUpdate)
    }
}

@Composable
private fun WeatherButton(label: String, temp: Int, cond: String, onUpdate: (Int, String) -> Unit) {
    Button(
        onClick = { onUpdate(temp, cond) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
    ) {
        Text(label)
    }
}

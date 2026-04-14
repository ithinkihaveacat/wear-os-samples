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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch

class ForceUpdateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ForceUpdateScreen(
                    onUpdateWidget = {
                        val componentName = ComponentName(this, ForceUpdateService::class.java)
                        // Instantiate the widget class to trigger the update
                        ForceUpdateWidget().triggerUpdate(this, componentName)
                    }
                )
            }
        }
    }
}

@Composable
fun ForceUpdateScreen(onUpdateWidget: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var count by remember { mutableIntStateOf(10) }

    LaunchedEffect(Unit) { count = context.getCounterState().count }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    scope.launch {
                        count--
                        context.setCounterState(CounterState(count))
                    }
                }
            ) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {}, enabled = false) { Text("$count") }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    scope.launch {
                        count++
                        context.setCounterState(CounterState(count))
                    }
                }
            ) {
                Text("+")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onUpdateWidget) { Text("Update Widget") }
    }
}

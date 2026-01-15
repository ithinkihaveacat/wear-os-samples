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
import androidx.compose.ui.graphics.Color
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

    LaunchedEffect(Unit) {
        count = context.getCounterState().count
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                scope.launch {
                    count--
                    context.setCounterState(CounterState(count))
                }
            }) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {}, enabled = false) {
                Text("$count")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                scope.launch {
                    count++
                    context.setCounterState(CounterState(count))
                }
            }) {
                Text("+")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onUpdateWidget) {
            Text("Update Widget")
        }
    }
}

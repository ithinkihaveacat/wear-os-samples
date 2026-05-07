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

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WeatherUpdateReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.google.example.wear_widget.UPDATE_WEATHER") {
            val temp = intent.getIntExtra("temp", 72)
            val condition = intent.getStringExtra("condition") ?: "☀️"

            val goAsync = goAsync()
            scope.launch {
                try {
                    context.setWeatherState(WeatherState(temp, condition))
                    WeatherWidget()
                        .triggerUpdate(
                            context.applicationContext,
                            ComponentName(context, WeatherWidgetService::class.java),
                        )
                    Log.d("WeatherReceiver", "Pushed weather update: $temp, $condition")
                } catch (e: Exception) {
                    Log.e("WeatherReceiver", "Error updating weather", e)
                } finally {
                    goAsync.finish()
                }
            }
        }
    }
}

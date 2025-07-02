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
package com.example.wear.tiles.golden

import android.content.Context
import com.example.wear.tiles.tools.MultiRoundDevicesPreviews

@MultiRoundDevicesPreviews private fun contacts5(context: Context) = socialPreview5(context)

@MultiRoundDevicesPreviews private fun contacts6(context: Context) = socialPreview6(context)

@MultiRoundDevicesPreviews private fun contacts2(context: Context) = socialPreview2(context)

@MultiRoundDevicesPreviews private fun goal(context: Context) = goalPreview(context)

@MultiRoundDevicesPreviews private fun workout(context: Context) = workoutLayoutPreview(context)

@MultiRoundDevicesPreviews private fun alarm(context: Context) = alarmPreview(context)

// https://www.figma.com/design/2OJqWvi4ebE7FY5uuBTUhm/GM3-BC25-Wear-Compose-Design-Kit-1.5?node-id=66728-38944&m=dev
// @MultiRoundDevicesPreviews private fun alarm(context: Context) = alarmPreview(context)

// https://www.figma.com/design/2OJqWvi4ebE7FY5uuBTUhm/GM3-BC25-Wear-Compose-Design-Kit-1.5?node-id=66728-39008&m=dev
// https://source.corp.google.com/piper///depot/google3/java/com/google/android/clockwork/prototiles/samples/material3/KeepTileService.kt
// @MultiRoundDevicesPreviews private fun mindfulnessTasks(context: Context) =
// mindfulnessTasksPreview(context)

@MultiRoundDevicesPreviews fun mindfulnessPreview(context: Context) = mindfulnessLayoutPreview(
  context
)

@MultiRoundDevicesPreviews fun timerPreview(context: Context) = timerLayoutPreview(context)

// @MultiRoundDevicesPreviews
// private fun mindfulnessMinutes(context: Context) = meditationMinutesPreview(context)

@MultiRoundDevicesPreviews private fun ski(context: Context) = skiPreview(context)

@MultiRoundDevicesPreviews private fun weather(context: Context) = weatherPreview(context)

@MultiRoundDevicesPreviews private fun news1(context: Context) = news1Preview(context)

@MultiRoundDevicesPreviews private fun news2(context: Context) = news2Preview(context)

@MultiRoundDevicesPreviews private fun calendar(context: Context) = calendarPreview(context)

@MultiRoundDevicesPreviews private fun media(context: Context) = mediaPreview(context)

@MultiRoundDevicesPreviews private fun heartRate(context: Context) = heartRateSimplePreview(context)

// https://www.figma.com/design/2OJqWvi4ebE7FY5uuBTUhm/GM3-BC25-Wear-Compose-Design-Kit-1.5?node-id=66728-41278&m=dev
// @MultiRoundDevicesPreviews private fun hike(context: Context) = hikePreview(context)

@MultiRoundDevicesPreviews private fun run(context: Context) = runPreview(context)

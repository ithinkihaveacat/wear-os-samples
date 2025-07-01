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
@file:SuppressLint("RestrictedApi")

/*
 * Copyright 2022 The Android Open Source Project
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

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.wear.protolayout.material3.ColorScheme
import androidx.wear.protolayout.material3.tokens.PaletteTokens
import androidx.wear.protolayout.types.LayoutColor
import androidx.wear.protolayout.types.argb

object GoldenTilesColors {
  val Black = Color.Black.toArgb()
  val Blue = android.graphics.Color.parseColor("#AECBFA")
  val RichBlue = android.graphics.Color.parseColor("#6694DE")
  val BlueGray = android.graphics.Color.parseColor("#2B333E")
  val DarkGray = android.graphics.Color.parseColor("#1C1B1F")
  val DarkerGray = android.graphics.Color.parseColor("#202124")
  val DarkPink = android.graphics.Color.parseColor("#32222B")
  val DarkPurple = android.graphics.Color.parseColor("#1F1C30")
  val DarkYellow = android.graphics.Color.parseColor("#332D1D")
  val Gray = android.graphics.Color.parseColor("#BDC1C6")
  val LightBlue = android.graphics.Color.parseColor("#C4E7FF")
  val LightGray = android.graphics.Color.parseColor("#DADCE0")
  val LightPurple = android.graphics.Color.parseColor("#998AF2")
  val LightRed = android.graphics.Color.parseColor("#F2B8B5")
  val Pink = android.graphics.Color.parseColor("#FBA9D6")
  val Purple = android.graphics.Color.parseColor("#C58AF9")
  val White = Color.White.toArgb()
  val White10Pc = Color(1f, 1f, 1f, 0.1f).toArgb()
  val Yellow = android.graphics.Color.parseColor("#FDE293")
}

val f: androidx.compose.ui.graphics.Color = Color(255, 255, 255, 255)

val GoldenTilesColorScheme =
  ColorScheme(
    //        primary = LayoutColor(PaletteTokens.PRIMARY30), // bg of buttons
    primary = f.toArgb().argb,
    //        onPrimary = LayoutColor(PaletteTokens.PRIMARY95), // fg of buttons
    onPrimary = PaletteTokens.PRIMARY95.argb, // fg of buttons
    tertiary = LayoutColor(PaletteTokens.TERTIARY80), // bg of edge button
    onTertiary = LayoutColor(PaletteTokens.TERTIARY10) // fg of edge button
  )

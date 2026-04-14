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
@file:android.annotation.SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * Android icon and text "Wear Widget" above a dark gray framed rectangle. Inside the frame, a red
 * circle is centered at the top and a blue rectangle is centered at the bottom.
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            // Draw a circle
            drawCircle(
                paint = RemotePaint().apply { color = Color.Red.rc },
                radius = 50f.rf,
                center = RemoteOffset(centerX, centerY),
            )

            // Draw a rect
            drawRect(
                paint = RemotePaint().apply { color = Color.Blue.rc },
                topLeft = RemoteOffset(centerX - 100f.rf, centerY + 60f.rf),
                size = RemoteSize(200f.rf, 50f.rf),
            )
        }
    }
}

/**
 * A dark screen shows a white circle with a grey Android robot icon at the top center. Below it,
 * white text reads "Wear Widget." Centered below the text is a large, dark grey rounded rectangle
 * containing a bright yellow equilateral triangle.
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample2() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            val path =
                androidx.compose.remote.creation.RemotePath().apply {
                    moveTo(0f, -50f)
                    lineTo(50f, 50f)
                    lineTo(-50f, 50f)
                    close()
                }

            translate(centerX, centerY) {
                drawPath(
                    path = path,
                    paint = RemotePaint().apply { color = Color.Yellow.rc },
                )
            }
        }
    }
}

/**
 * Screen shows an Android icon and "Wear Widget" text at the top on a black background. Below, a
 * dark gray rounded rectangular frame encloses a black area. Centered within, a bright green
 * diamond shape has white, horizontal text "Rotated" overlaid.
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample3() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            translate(centerX, centerY) {
                rotate(45f.rf) {
                    translate(-centerX, -centerY) {
                        drawRect(
                            paint = RemotePaint().apply { color = Color.Green.rc },
                            topLeft = RemoteOffset(centerX - 40f.rf, centerY - 40f.rf),
                            size = RemoteSize(80f.rf, 80f.rf),
                        )
                    }
                }
            }

            drawAnchoredText(
                text = "Rotated".rs,
                anchorX = centerX,
                anchorY = centerY,
                paint =
                    RemotePaint().apply {
                        color = Color.White.rc
                        textSize = 40f.rf
                    },
            )
        }
    }
}

/**
 * Black background UI with an Android icon and "Component Catalog" title. A large circular progress
 * indicator shows 75% completion in red, with the remaining 25% in dark gray. White text inside the
 * circle reads "75%" and "Progress".
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample4() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteBox(modifier = RemoteModifier.size(100.rdp)) {
            RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
                val width = remote.component.width
                val height = remote.component.height
                val centerX = width / 2f.rf
                val centerY = height / 2f.rf
                val strokeWidth = 10f
                val radius = (width / 2f.rf) - (strokeWidth / 2f).rf

                // Track
                drawCircle(
                    paint =
                        RemotePaint().apply {
                            color = Color.DarkGray.rc
                            style = PaintingStyle.Stroke
                            this.strokeWidth = strokeWidth.rf
                            isAntiAlias = true
                        },
                    center = RemoteOffset(centerX, centerY),
                    radius = radius,
                )

                // Progress (75% = 270 degrees)
                drawArc(
                    paint =
                        RemotePaint().apply {
                            color = Color.Red.rc
                            style = PaintingStyle.Stroke
                            this.strokeWidth = strokeWidth.rf
                            strokeCap = StrokeCap.Round
                            isAntiAlias = true
                        },
                    startAngle = -90f.rf,
                    sweepAngle = 270f.rf,
                    useCenter = false,
                    topLeft = RemoteOffset((strokeWidth / 2f).rf, (strokeWidth / 2f).rf),
                    size = RemoteSize(width - strokeWidth.rf, height - strokeWidth.rf),
                )
            }
            RemoteBox(
                modifier = RemoteModifier.fillMaxSize(),
                contentAlignment = RemoteAlignment.Center,
            ) {
                RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                    MaterialRemoteText(
                        text = "75%".rs,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.rc,
                    )
                    MaterialRemoteText(text = "Progress".rs, color = Color.LightGray.rc)
                }
            }
        }
    }
}

/**
 * A dark mode screen with an Android logo and "Component Catalog" title. Below, a circular
 * segmented progress bar shows 3 of 5 segments in red, and 2 in dark gray. Centered within the
 * circle is white text: "3/5" (bold) and "Segments" (regular).
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample5() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteBox(modifier = RemoteModifier.size(100.rdp)) {
            RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
                val width = remote.component.width
                val height = remote.component.height
                val strokeWidth = 10f
                val gap = 8f.rf
                val segments = 5f.rf
                val activeSegments = 3f.rf
                val totalSweep = 360f.rf
                val segmentSweep = (totalSweep - (gap * segments)) / segments

                // Active segments
                loop(0f.rf, activeSegments, 1f.rf) { i ->
                    val startAngle = -90f.rf + (i * (segmentSweep + gap))
                    drawArc(
                        paint =
                            RemotePaint().apply {
                                color = Color.Red.rc
                                style = PaintingStyle.Stroke
                                this.strokeWidth = strokeWidth.rf
                                strokeCap = StrokeCap.Round
                                isAntiAlias = true
                            },
                        startAngle = startAngle,
                        sweepAngle = segmentSweep,
                        useCenter = false,
                        topLeft = RemoteOffset((strokeWidth / 2f).rf, (strokeWidth / 2f).rf),
                        size = RemoteSize(width - strokeWidth.rf, height - strokeWidth.rf),
                    )
                }

                // Inactive segments
                loop(activeSegments, segments, 1f.rf) { i ->
                    val startAngle = -90f.rf + (i * (segmentSweep + gap))
                    drawArc(
                        paint =
                            RemotePaint().apply {
                                color = Color.DarkGray.rc
                                style = PaintingStyle.Stroke
                                this.strokeWidth = strokeWidth.rf
                                strokeCap = StrokeCap.Round
                                isAntiAlias = true
                            },
                        startAngle = startAngle,
                        sweepAngle = segmentSweep,
                        useCenter = false,
                        topLeft = RemoteOffset((strokeWidth / 2f).rf, (strokeWidth / 2f).rf),
                        size = RemoteSize(width - strokeWidth.rf, height - strokeWidth.rf),
                    )
                }
            }
            RemoteBox(
                modifier = RemoteModifier.fillMaxSize(),
                contentAlignment = RemoteAlignment.Center,
            ) {
                RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                    MaterialRemoteText(
                        text = "3/5".rs,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.rc,
                    )
                    MaterialRemoteText(text = "Segments".rs, color = Color.LightGray.rc)
                }
            }
        }
    }
}

@PreviewWearLarge
@Composable
fun CanvasReferenceSample4Preview() {
    WidgetPreview { CanvasReferenceSample4() }
}

@PreviewWearLarge
@Composable
fun CanvasReferenceSample5Preview() {
    WidgetPreview { CanvasReferenceSample5() }
}

@PreviewWearLarge
@Composable
fun CanvasReferenceSample1Preview() {
    WidgetPreview { CanvasReferenceSample1() }
}

@PreviewWearLarge
@Composable
fun CanvasReferenceSample2Preview() {
    WidgetPreview { CanvasReferenceSample2() }
}

@PreviewWearLarge
@Composable
fun CanvasReferenceSample3Preview() {
    WidgetPreview { CanvasReferenceSample3() }
}

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
 * A dark background shows a pink circular Android icon, then white text "Widget Catalog". Below, a
 * large, dark grey rounded rectangle is centered, containing a red circle centered above a blue
 * horizontal rectangle.
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample1() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
 * A dark UI with a pink circular Android icon above white text "Widget Catalog". Below is a large,
 * centered, dark gray rounded rectangle with a small yellow triangular shape in its top-left
 * corner, resembling a folded page or tag.
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample2() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
                drawPath(path = path, paint = RemotePaint().apply { color = Color.Yellow.rc })
            }
        }
    }
}

/**
 * UI with a pink Android icon and "Widget Catalog" title. A large, dark grey rounded rectangle
 * dominates the screen. In its bottom-left corner, a bright green rotated rectangle shows partial
 * white text: "rotate...".
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample3() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
 * Android "Widget Catalog" screen with a pink Android logo icon. A dark gray rounded card displays
 * a red circular progress indicator showing 75% completion, with white text "75%" above gray
 * "Progress" text. The background is black.
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample4() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
 * A screen with a pink Android logo icon at the top, followed by "Widget Catalog" in white text.
 * Below is a dark gray rounded rectangle card containing a red circular outline. Inside the circle,
 * white text "3/5" is above gray text "Segments".
 */
@RemoteComposable
@Composable
fun CanvasReferenceSample5() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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

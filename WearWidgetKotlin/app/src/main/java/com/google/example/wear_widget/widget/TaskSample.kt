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
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.remote.material3.ProvideRemoteTextStyle
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.R
import com.google.example.wear_widget.WidgetPreview

@RemoteComposable
@Composable
fun TaskSample() {

    ProvideRemoteTextStyle(value = RemoteMaterialTheme.typography.bodyMedium) {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            contentAlignment = RemoteAlignment.Center,
        ) {
            RemoteRow(
                verticalAlignment = RemoteAlignment.CenterVertically,
                horizontalArrangement = RemoteArrangement.Center,
            ) {
                // Left Side: Circular Progress + Icon stacked
                RemoteBox(
                    modifier = RemoteModifier.padding(right = 12.rdp).size(60.rdp),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    // Custom Circular Progress Indicator using Canvas
                    val trackColor = RemoteMaterialTheme.colorScheme.surfaceContainerHigh
                    val progressColor = RemoteMaterialTheme.colorScheme.primary

                    RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
                        val width = remote.component.width
                        val height = remote.component.height
                        val centerX = width / 2f.rf
                        val centerY = height / 2f.rf
                        val strokeWidth = 14f // Thicker stroke
                        val radius = (width / 2f.rf) - (strokeWidth / 2f).rf

                        val startAngle = 135f
                        val progressSweep = 72f
                        val gapAngle = 25f // Increased space between progress and track
                        val totalTrackSweep =
                            270f // Horseshoe style with a 90-degree gap at the bottom

                        // Track (Dark Green) - Incomplete section
                        val remainingSweep = totalTrackSweep - progressSweep - gapAngle
                        if (remainingSweep > 0) {
                            drawArc(
                                paint =
                                    RemotePaint().apply {
                                        color = trackColor
                                        style = PaintingStyle.Stroke
                                        this.strokeWidth = strokeWidth.rf
                                        strokeCap = StrokeCap.Round
                                        isAntiAlias = true
                                    },
                                startAngle = (startAngle + progressSweep + gapAngle).rf,
                                sweepAngle = remainingSweep.rf,
                                useCenter = false,
                                topLeft =
                                    RemoteOffset((strokeWidth / 2f).rf, (strokeWidth / 2f).rf),
                                size = RemoteSize(width - strokeWidth.rf, height - strokeWidth.rf),
                            )
                        }

                        // Progress (Light Green) - Completed section
                        drawArc(
                            paint =
                                RemotePaint().apply {
                                    color = progressColor
                                    style = PaintingStyle.Stroke
                                    this.strokeWidth = strokeWidth.rf
                                    strokeCap = StrokeCap.Round
                                    isAntiAlias = true
                                },
                            startAngle = startAngle.rf, // Start bottom-left
                            sweepAngle = progressSweep.rf,
                            useCenter = false,
                            topLeft = RemoteOffset((strokeWidth / 2f).rf, (strokeWidth / 2f).rf),
                            size = RemoteSize(width - strokeWidth.rf, height - strokeWidth.rf),
                        )
                    }

                    // Center Icon wrapped in a solid circle
                    RemoteBox(
                        modifier = RemoteModifier.size(32.rdp),
                        contentAlignment = RemoteAlignment.Center,
                    ) {
                        val iconBgColor = RemoteMaterialTheme.colorScheme.primary
                        val iconTintColor = RemoteMaterialTheme.colorScheme.onPrimary

                        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
                            val w = remote.component.width
                            val h = remote.component.height
                            drawCircle(
                                paint =
                                    RemotePaint().apply {
                                        color = iconBgColor
                                        style = PaintingStyle.Fill
                                        isAntiAlias = true
                                    },
                                center = RemoteOffset(w / 2f.rf, h / 2f.rf),
                                radius = w / 2f.rf
                            )
                        }

                        RemoteIcon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_24),
                            contentDescription = "Checkmark".rs,
                            modifier = RemoteModifier.size(24.rdp),
                            tint = iconTintColor // Dark green cutout equivalent
                        )
                    }
                }

                // Right Side: Typography
                RemoteColumn(
                    horizontalAlignment = RemoteAlignment.Start,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    MaterialRemoteText(
                        text = "1".rs,
                        color = RemoteMaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 42.rsp,
                        fontWeight = FontWeight.Bold
                    )
                    MaterialRemoteText(
                        text = "of 5 tasks".rs,
                        color = RemoteMaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.rsp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@PreviewWearLarge
@Composable
fun TaskSamplePreview() {
    WidgetPreview { TaskSample() }
}

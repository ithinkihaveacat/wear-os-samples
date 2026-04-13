
package com.google.example.wear_widget.widget

import android.graphics.Paint
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.remote.material3.ProvideRemoteTextStyle
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.compose.remote.creation.compose.text.RemoteTextStyle
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.compose.ui.unit.dp
import com.google.example.wear_widget.R

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.ui.unit.DpSize

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
                    modifier = RemoteModifier.padding(right = 12.dp).size(60.rdp),
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
                        val totalTrackSweep = 270f // Horseshoe style with a 90-degree gap at the bottom

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
                                topLeft = RemoteOffset((strokeWidth / 2f).rf, (strokeWidth / 2f).rf),
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
                                paint = RemotePaint().apply {
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

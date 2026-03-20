@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint
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
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.compose.ui.unit.dp
import com.google.example.wear_widget.R

@RemoteComposable
@Composable
fun TaskSample() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteRow(
                verticalAlignment = RemoteAlignment.CenterVertically,
                horizontalArrangement = RemoteArrangement.CenterHorizontally,
            ) {
                // Left Side: Circular Progress + Icon stacked
                RemoteBox(
                    modifier = RemoteModifier.padding(right = 12.dp).size(60.rdp),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    // Custom Circular Progress Indicator using Canvas
                    RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
                        val width = remote.component.width
                        val height = remote.component.height
                        val centerX = width / 2f.rf
                        val centerY = height / 2f.rf
                        val strokeWidth = 10f // equivalent to 5dp roughly at scale
                        val radius = (width / 2f.rf) - (strokeWidth / 2f).rf

                        // Track (Dark Green)
                        drawCircle(
                            paint =
                                RemotePaint().apply {
                                    remoteColor = Color(0xFF596913).rc
                                    style = Paint.Style.STROKE
                                    this.strokeWidth = strokeWidth
                                    isAntiAlias = true
                                },
                            center = RemoteOffset(centerX, centerY),
                            radius = radius,
                        )

                        // Progress (Light Green - 20% = 72 degrees)
                        drawArc(
                            paint =
                                RemotePaint().apply {
                                    remoteColor = Color(0xFFCFE868).rc
                                    style = Paint.Style.STROKE
                                    this.strokeWidth = strokeWidth
                                    strokeCap = Paint.Cap.ROUND
                                    isAntiAlias = true
                                },
                            startAngle = -90f.rf,
                            sweepAngle = 72f.rf,
                            useCenter = false,
                            topLeft = RemoteOffset((strokeWidth / 2f).rf, (strokeWidth / 2f).rf),
                            size = RemoteSize(width - strokeWidth.rf, height - strokeWidth.rf),
                        )
                    }
                    
                    // Center Icon
                    RemoteIcon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_24),
                        contentDescription = "Checkmark".rs,
                        modifier = RemoteModifier.size(32.rdp),
                        tint = Color(0xFFCFE868).rc
                    )
                }

                // Right Side: Typography
                RemoteColumn(
                    horizontalAlignment = RemoteAlignment.Start,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText(
                        text = "1",
                        color = Color(0xFFCFE868).rc,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                    RemoteText(
                        text = "of 5 tasks",
                        color = Color(0xFFCFE868).rc,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

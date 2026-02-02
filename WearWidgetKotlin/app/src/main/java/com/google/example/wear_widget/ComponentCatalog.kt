@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.*
import androidx.compose.remote.creation.compose.modifier.animationSpec
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.asRdp
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteButtonDefaults
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteImage
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

class ComponentCatalogService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = ComponentCatalog()
}

class ComponentCatalog : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val state = context.getComponentCatalogState()
        Log.d("ComponentCatalog", "provideWidgetData: layoutName='${state.layoutName}'")
        return WearWidgetDocument(backgroundColor = Color.Black) {
            when (state.layoutName) {
                "textButton" -> ComponentCatalogTextButtonSample()
                "iconButton" -> ComponentCatalogIconButtonSample()
                "avatarButton" -> ComponentCatalogAvatarButtonSample()
                "imageButton" -> ComponentCatalogImageButtonSample()
                "compactButton" -> ComponentCatalogCompactButtonSample()
                "titleCard" -> ComponentCatalogTitleCardSample()
                "appCard" -> ComponentCatalogAppCardSample()
                "textDataCard" -> ComponentCatalogTextDataCardSample()
                "iconDataCard" -> ComponentCatalogIconDataCardSample()
                "graphicDataCard" -> ComponentCatalogGraphicDataCardSample()
                "circularProgressIndicator" -> ComponentCatalogCircularProgressIndicatorSample()
                "segmentedCircularProgressIndicator" ->
                    ComponentCatalogSegmentedCircularProgressIndicatorSample()
                "fullBleedImage" -> ComponentCatalogFullBleedImageSample()
                "animatedBox" -> ComponentCatalogAnimatedBoxSample()
                else -> ComponentCatalogTextButtonSample()
            }
        }
    }
}

// ... existing samples ...

@RemoteComposable
@Composable
fun ComponentCatalogAnimatedBoxSample() {
    // Define a remote state key for toggling
    val state = rememberRemoteIntValue { 0 }
    val isToggled = state eq 1.ri

    // Derive animated properties based on the remote state
    val containerColor = isToggled.select(Color.Red.rc, Color.Blue.rc)
    val boxSize = isToggled.select(120f.rf, 60f.rf).asRemoteDp()

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteBox(
            modifier =
                RemoteModifier
                    // Apply the animated size
                    .size(boxSize)
                    // Enable tween animations for all property changes on this element
                    .animationSpec(enabled = true)
                    // Apply the animated color
                    .background(containerColor)
                    // Toggle the state key on click
                    .clickable(actions = arrayOf(ValueChange(state, state xor 1.ri)))
        )
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogTextButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(onClick = arrayOf()) { MaterialRemoteText(text = "Text Button".rs) }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogIconButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(onClick = arrayOf()) {
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
                contentDescription = "Message".rs,
                modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize),
            )
        }
    }
}

// TODO (b/474292165): This seems to not work (renders as black screen).
@RemoteComposable
@Composable
fun ComponentCatalogAvatarButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(onClick = arrayOf()) {
            RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                RemoteImage(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                    contentDescription = "Avatar".rs,
                    contentScale = ContentScale.Crop,
                    modifier =
                        RemoteModifier.size(RemoteButtonDefaults.LargeIconSize)
                            .clip(RoundedCornerShape(percent = 50)),
                )
                // Spacer
                RemoteBox(modifier = RemoteModifier.size(8.dp.asRdp()))
                // Texts
                MaterialRemoteText("Avatar Button".rs)
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogImageButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        // Simulating ImageButton with a box and background image since RemoteButton doesn't support
        // background images directly yet
        RemoteBox(
            modifier =
                RemoteModifier.size(60.rdp) // Hardcoded size
                    .clip(RoundedCornerShape(percent = 50)), // Circle
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
                contentDescription = "Background".rs,
                contentScale = ContentScale.Crop,
                modifier = RemoteModifier.fillMaxSize(),
            )
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogCompactButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = arrayOf(),
            icon = {
                RemoteIcon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_message_24),
                    contentDescription = "Message".rs,
                    modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize),
                )
            },
            label = { MaterialRemoteText("Compact".rs) },
        )
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogTitleCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(onClick = arrayOf()) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText(text = "Title Card".rs, fontWeight = FontWeight.Bold)
                MaterialRemoteText(text = "Content".rs)
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogAppCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(onClick = arrayOf()) {
            RemoteColumn {
                RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                    RemoteImage(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                        contentDescription = "Avatar".rs,
                        contentScale = ContentScale.Crop,
                        modifier =
                            RemoteModifier.size(24.rdp).clip(RoundedCornerShape(percent = 50)),
                    )
                    RemoteBox(modifier = RemoteModifier.size(8.rdp))
                    MaterialRemoteText("Ali".rs)
                    RemoteBox(modifier = RemoteModifier.size(20.rdp))
                    MaterialRemoteText("2:03 PM".rs)
                }
                MaterialRemoteText(text = "Dinner in SF".rs, fontWeight = FontWeight.Bold)
                MaterialRemoteText(text = "Let's try that new restaurant.".rs)
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogCircularProgressIndicatorSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
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
                            remoteColor = Color.DarkGray.rc
                            style = Paint.Style.STROKE
                            this.strokeWidth = strokeWidth
                            isAntiAlias = true
                        },
                    center = RemoteOffset(centerX, centerY),
                    radius = radius,
                )

                // Progress (75% = 270 degrees)
                drawArc(
                    paint =
                        RemotePaint().apply {
                            remoteColor = Color.Red.rc
                            style = Paint.Style.STROKE
                            this.strokeWidth = strokeWidth
                            strokeCap = Paint.Cap.ROUND
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
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
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

@RemoteComposable
@Composable
fun ComponentCatalogSegmentedCircularProgressIndicatorSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
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
                                remoteColor = Color.Red.rc
                                style = Paint.Style.STROKE
                                this.strokeWidth = strokeWidth
                                strokeCap = Paint.Cap.ROUND
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
                                remoteColor = Color.DarkGray.rc
                                style = Paint.Style.STROKE
                                this.strokeWidth = strokeWidth
                                strokeCap = Paint.Cap.ROUND
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
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
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

@RemoteComposable
@Composable
fun ComponentCatalogTextDataCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = arrayOf(),
            colors =
                RemoteButtonColors(
                    containerColor = Color.DarkGray.rc,
                    contentColor = Color.LightGray.rc,
                    secondaryContentColor = Color.White.rc,
                    iconColor = Color.White.rc,
                    disabledContainerColor = Color.DarkGray.rc,
                    disabledContentColor = Color.LightGray.rc,
                    disabledSecondaryContentColor = Color.White.rc,
                    disabledIconColor = Color.White.rc,
                ),
        ) {
            RemoteColumn {
                MaterialRemoteText("Text Data".rs)
                MaterialRemoteText(text = "Content".rs, color = Color.White.rc)
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogIconDataCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = arrayOf(),
            colors =
                RemoteButtonColors(
                    containerColor = Color.DarkGray.rc,
                    contentColor = Color.LightGray.rc,
                    secondaryContentColor = Color.White.rc,
                    iconColor = Color.White.rc,
                    disabledContainerColor = Color.DarkGray.rc,
                    disabledContentColor = Color.LightGray.rc,
                    disabledSecondaryContentColor = Color.White.rc,
                    disabledIconColor = Color.White.rc,
                ),
        ) {
            RemoteColumn {
                MaterialRemoteText("Icon Data".rs)
                MaterialRemoteText(text = "Content".rs, color = Color.White.rc)
            }
            RemoteBox(modifier = RemoteModifier.size(8.rdp))
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_message_24),
                contentDescription = "Message".rs,
                modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize),
            )
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogGraphicDataCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = arrayOf(),
            colors =
                RemoteButtonColors(
                    containerColor = Color.DarkGray.rc,
                    contentColor = Color.LightGray.rc,
                    secondaryContentColor = Color.White.rc,
                    iconColor = Color.White.rc,
                    disabledContainerColor = Color.DarkGray.rc,
                    disabledContentColor = Color.LightGray.rc,
                    disabledSecondaryContentColor = Color.White.rc,
                    disabledIconColor = Color.White.rc,
                ),
        ) {
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_run_24),
                contentDescription = "Run".rs,
                modifier = RemoteModifier.size(RemoteButtonDefaults.LargeIconSize),
            )
            RemoteBox(modifier = RemoteModifier.size(8.rdp))
            RemoteColumn {
                MaterialRemoteText("Graphic Data".rs)
                MaterialRemoteText(text = "Content".rs, color = Color.White.rc)
            }
        }
    }
}

@RemoteComposable
@Composable
fun ComponentCatalogFullBleedImageSample() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize()) {
        RemoteImage(
            bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
            contentDescription = "Background".rs,
            contentScale = ContentScale.Crop,
            modifier = RemoteModifier.fillMaxSize(),
        )
        // Overlay Text
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            MaterialRemoteText(
                text = "Full Bleed".rs,
                color = Color.White.rc,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

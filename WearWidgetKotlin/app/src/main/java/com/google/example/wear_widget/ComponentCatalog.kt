
package com.google.example.wear_widget

import android.content.Context
import android.graphics.Paint
import android.util.Log
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.shapes.RemoteCircleShape
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
import androidx.compose.remote.creation.compose.state.asRemoteDp
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetBrush
import androidx.compose.remote.creation.compose.state.rc
import androidx.glance.wear.core.WearWidgetParams
import androidx.glance.wear.color
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteButtonDefaults
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.compose.remote.creation.compose.layout.RemoteImage
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
        return WearWidgetDocument(background = WearWidgetBrush.color(Color.White.rc)) {
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

/**
 * A black screen displays a light grey circular icon with a dark grey Android robot head at the top
 * center. Below it, centered, is white text "Component Catalog". A vibrant blue square is centered
 * further down, occupying the mid-lower portion of the screen.
 */
@RemoteComposable
@Composable
fun ComponentCatalogAnimatedBoxSample() {
    // Define a remote state key for toggling
    val state = rememberMutableRemoteInt(0)
    val isToggled = state eq 1.ri

    // Derive animated properties based on the remote state
    val containerColor = isToggled.select(Color.Red.rc, Color.Blue.rc)
    val boxSize = isToggled.select(120f.rf, 60f.rf).asRemoteDp()

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
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
                    .clickable(ValueChange(state, state xor 1.ri))
        )
    }
}

/**
 * A dark-themed screen displaying an Android robot icon at the top. Below it, white text reads
 * "Component Catalog". Centered further down is a light gray rounded-rectangle button with the text
 * "Text Button".
 */
@RemoteComposable
@Composable
fun ComponentCatalogTextButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri)) {
            MaterialRemoteText(text = "Text Button".rs)
        }
    }
}

/**
 * Centered on a black background, "Component Catalog" in white text. Above it, a small white
 * circular icon holds a dark gray Android logo. Below the text, a larger, light purple-gray rounded
 * rectangular button displays a dark gray Android logo.
 */
@RemoteComposable
@Composable
fun ComponentCatalogIconButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri)) {
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
                contentDescription = "Message".rs,
                modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize),
            )
        }
    }
}

// TODO (b/474292165): This seems to not work (renders as black screen).
/**
 * Black background. Top center: gray Android logo above white text 'Component Catalog'. Below, a
 * light gray rounded button features a circular avatar of a woman with curly hair and sunglasses,
 * next to dark gray text 'Avatar Button'.
 */
@RemoteComposable
@Composable
fun ComponentCatalogAvatarButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri)) {
            RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                RemoteImage(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                    contentDescription = "Avatar".rs,
                    contentScale = ContentScale.Crop,
                    modifier =
                        RemoteModifier.size(RemoteButtonDefaults.LargeIconSize)
                            .clip(RemoteCircleShape),
                )
                // Spacer
                RemoteBox(modifier = RemoteModifier.size(8.dp.asRdp()))
                // Texts
                MaterialRemoteText("Avatar Button".rs)
            }
        }
    }
}

/**
 * Black background with a centered Android logo icon at the top. Below it is the white text
 * "Component Catalog". Further down, a circular image shows a white daisy in a field bathed in
 * golden light.
 */
@RemoteComposable
@Composable
fun ComponentCatalogImageButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        // Simulating ImageButton with a box and background image since RemoteButton doesn't support
        // background images directly yet
        RemoteBox(
            modifier = RemoteModifier.size(60.rdp), // Hardcoded size
            contentAlignment = RemoteAlignment.Center,
        ) {
            RemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
                contentDescription = "Background".rs,
                contentScale = ContentScale.Crop,
                modifier = RemoteModifier.size(60.rdp).clip(RemoteCircleShape),
            )
        }
    }
}

/**
 * Black screen with white text "Component Catalog" at the top, below an Android logo. A centered
 * light purple rounded button features a message icon with horizontal lines and the word "Compact"
 * in dark grey.
 */
@RemoteComposable
@Composable
fun ComponentCatalogCompactButtonSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri),
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

/**
 * Dark UI: Centered Android logo above 'Component Catalog' text. Below, a lavender rounded card
 * shows 'Title Card Content' in two lines.
 */
@RemoteComposable
@Composable
fun ComponentCatalogTitleCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri)) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText(text = "Title Card".rs, fontWeight = FontWeight.Bold)
                MaterialRemoteText(text = "Content".rs)
            }
        }
    }
}

/**
 * Screenshot shows an Android robot icon above "Component Catalog" on a black background. Below, a
 * light purple chat bubble features a circular profile picture of a woman. Inside, text reads "Ali"
 * and "2:03 PM", followed by "Dinner in SF" and "Let's try that new restaurant."
 */
@RemoteComposable
@Composable
fun ComponentCatalogAppCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri)) {
            RemoteColumn {
                RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                    RemoteImage(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                        contentDescription = "Avatar".rs,
                        contentScale = ContentScale.Crop,
                        modifier =
                            RemoteModifier.size(24.rdp).clip(RemoteCircleShape),
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

/**
 * Black background UI with an Android icon and "Component Catalog" title. A large circular progress
 * indicator shows 75% completion in red, with the remaining 25% in dark gray. White text inside the
 * circle reads "75%" and "Progress".
 */
@RemoteComposable
@Composable
fun ComponentCatalogCircularProgressIndicatorSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
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
fun ComponentCatalogSegmentedCircularProgressIndicatorSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
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

/**
 * Dark UI with an Android robot icon above "Component Catalog" text. A dark gray rounded button
 * below displays "Text Data" in light gray, stacked over "Content" in white.
 */
@RemoteComposable
@Composable
fun ComponentCatalogTextDataCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri),
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

/**
 * A dark screen displays a grey Android logo at the top, followed by white text "Component
 * Catalog". Below, a rounded grey button features stacked text "Icon Data" (grey) and "Content"
 * (white), alongside a white message icon with horizontal lines.
 */
@RemoteComposable
@Composable
fun ComponentCatalogIconDataCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri),
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

/**
 * A dark screen with an Android robot icon at the top, followed by the white text "Component
 * Catalog". Below, a dark gray rounded button features a white running person icon on the left and
 * stacked white text "Graphic Data Content" on the right.
 */
@RemoteComposable
@Composable
fun ComponentCatalogGraphicDataCardSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri),
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

/**
 * A screen with a black background. At the top, a gray circle contains a dark Android bot icon.
 * Below, white centered text reads 'Component Catalog.' The lower portion displays a nature photo
 * of daisies and blurred grass, with white text 'Full Bleed' horizontally and vertically centered
 * over the image. The photo is framed by significant black margins.
 */
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
            contentAlignment = RemoteAlignment.Center,
        ) {
            MaterialRemoteText(
                text = "Full Bleed".rs,
                color = Color.White.rc,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

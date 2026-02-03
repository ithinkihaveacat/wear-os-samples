@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.remote.core.operations.TextFromFloat
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.action.pendingIntentAction
import androidx.compose.remote.creation.compose.capture.RemoteDensity
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteCollapsibleColumn
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteColumnScope
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteRowScope
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.layout.rotate
import androidx.compose.remote.creation.compose.layout.translate
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.animationSpec
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.drawWithContent
import androidx.compose.remote.creation.compose.modifier.fillMaxHeight
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.fillMaxWidth
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.rememberRemoteScrollState
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.modifier.verticalScroll
import androidx.compose.remote.creation.compose.shaders.RemoteLinearGradient
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteButtonDefaults
import androidx.wear.compose.remote.material3.RemoteButtonGroup
import androidx.wear.compose.remote.material3.RemoteButtonGroupDefaults
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier

class WidgetCatalogService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = WidgetCatalog()
}

class WidgetCatalog : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val state = context.getWidgetCatalogState()
        Log.d("WidgetCatalog", "provideWidgetData: layoutName='${state.layoutName}'")
        return WearWidgetDocument(backgroundColor = Color.DarkGray) {
            when (state.layoutName) {
                "SemanticStyleWorkaroundSample" -> SemanticStyleWorkaroundSample()
                "CanvasSample3" -> CanvasSample3()
                "SystemThemeComparisonSample" -> SystemThemeComparisonSample()
                "SystemThemeSample" -> SystemThemeSample()
                "AustralianThemeSample" -> AustralianThemeSample()
                "BoxSample1" -> BoxSample1()
                "BoxSample2" -> BoxSample2()
                "BoxSample3" -> BoxSample3()
                "TextSample1" -> TextSample1()
                "TextSample1WithMargin" -> TextSample1WithMargin()
                "RowSample1" -> RowSample1()
                "RowSample2" -> RowSample2()
                "CollapsibleColumnSample1" -> CollapsibleColumnSample1()
                "ButtonSample1" -> ButtonSample1()
                "ButtonSample2" -> ButtonSample2()
                "ButtonSample3" -> ButtonSample3()
                "ButtonSample4" -> ButtonSample4()
                "ButtonSample6" -> ButtonSample6()
                "ButtonSample7" -> ButtonSample7()
                "ButtonSample8" -> ButtonSample8()
                "ButtonSample9" -> ButtonSample9()
                "IconSample1" -> IconSample1()
                "GridSample1" -> GridSample1()
                "CardSample1" -> CardSample1()
                "CounterSample1" -> CounterSample1()
                "Material3ThemeSample" -> Material3ThemeSample()
                "CanvasSample1" -> CanvasSample1()
                "CanvasSample2" -> CanvasSample2()
                "GradientBackgroundSample" -> GradientBackgroundSample()
                "PendingIntentSample" -> PendingIntentSample()
                "VerticalScrollSample" -> VerticalScrollSample()
                "MixedStyleSample" -> MixedStyleSample()
                "ConditionalRadiusSample" -> ConditionalRadiusSample()
                "TypographyScaleSample" -> TypographyScaleSample()
                "AnchoredTextSample" -> AnchoredTextSample()
                "RotatedTextSample" -> RotatedTextSample()
                "FullBleedImageButtonSample" -> FullBleedImageButtonSample()
                "ReproBitmapCanvas" -> ReproBitmapCanvas()
                else -> SemanticStyleWorkaroundSample()
            }
        }
    }
}

/**
 * Android robot icon above "Wear Widget" text. A dark gray rounded rectangular widget displays
 * "Semantic Styles Demo" and a large digital time "12:34" in white text on a black background.
 */
@RemoteComposable
@Composable
fun SemanticStyleWorkaroundSample() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize().background(Color.Black),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(
                modifier = RemoteModifier.fillMaxWidth(),
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
            ) {
                MaterialRemoteText(
                    text = "Semantic Styles Demo".rs,
                    style = MyWidgetTypography.titleLarge,
                )

                RemoteBox(RemoteModifier.size(16.rdp))

                MaterialRemoteText(text = "12:34".rs, style = MyWidgetTypography.numeralLarge)

                RemoteBox(RemoteModifier.size(12.rdp))

                MaterialRemoteText(
                    text = "Session complete".rs,
                    style = MyWidgetTypography.titleMedium,
                )
            }
        }
    }
}

/**
 * Screenshot shows an Android logo above the title "Wear Widget". Below, a dark gray outlined
 * rectangle displays a white box rotated counter-clockwise, containing the red text "Hello
 * world!".
 */
@RemoteComposable
@Composable
fun RotatedTextSample(modifier: RemoteModifier = RemoteModifier) {
    RemoteBox(
        modifier = modifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteBox(
            modifier =
                RemoteModifier.background(Color.White)
                    .drawWithContent {
                        val width = remote.component.width
                        val height = remote.component.height
                        val centerX = width / 2f.rf
                        val centerY = height / 2f.rf
                        translate(centerX, centerY) {
                            rotate(66f.rf) { translate(-centerX, -centerY) { drawContent() } }
                        }
                    }
                    .padding(10.dp),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText(text = "Hello world!", color = Color.Red.rc)
        }
    }
}

/**
 * A screen with a white Android logo on a light gray circle, followed by "Wear Widget" text.
 * Below, a white rounded rectangle shows "Top Left" in red, "Center" in black, and "Bottom Right"
 * in blue, demonstrating UI placement. Black background.
 */
@RemoteComposable
@Composable
fun AnchoredTextSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.White),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            // 1. Center Text (pan 0 = center)
            drawAnchoredText(
                text = "Center".rs,
                anchorX = centerX,
                anchorY = centerY,
                panx = 0f.rf,
                pany = 0f.rf,
                flags = 0,
                paint =
                    RemotePaint().apply {
                        remoteColor = Color.Black.rc
                        textSize = 30f
                    },
            )

            // 2. Top-Left Text
            // Anchor at absolute top-left (0,0)
            // panx = -1 (Left Align/Shift Right)
            // pany = 1 (Top Align/Shift Down - assumed based on v4 failure with -1)
            drawAnchoredText(
                text = "Top Left".rs,
                anchorX = 0f.rf,
                anchorY = 0f.rf,
                panx = -1f.rf,
                pany = 1f.rf,
                flags = 0,
                paint =
                    RemotePaint().apply {
                        remoteColor = Color.Red.rc
                        textSize = 20f
                    },
            )

            // 3. Bottom-Right Text
            // Anchor at absolute bottom-right (w,h)
            // panx = 1 (Right Align/Shift Left)
            // pany = -1 (Bottom Align/Shift Up - assumed based on v4 failure with 1)
            drawAnchoredText(
                text = "Bottom Right".rs,
                anchorX = width,
                anchorY = height,
                panx = 1f.rf,
                pany = -1f.rf,
                flags = 0,
                paint =
                    RemotePaint().apply {
                        remoteColor = Color.Blue.rc
                        textSize = 20f
                    },
            )
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
fun CanvasSample3() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
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
                            paint = RemotePaint().apply { remoteColor = Color.Green.rc },
                            topLeft = RemoteOffset(centerX - 40f.rf, centerY - 40f.rf),
                            size = RemoteSize(80f.rf, 80f.rf),
                        )
                    }
                }
            }

            // Anchored Text
            drawAnchoredText(
                text = "Rotated".rs,
                anchorX = centerX,
                anchorY = centerY,
                paint =
                    RemotePaint().apply {
                        remoteColor = Color.White.rc
                        textSize = 40f
                    },
            )
        }
    }
}

/**
 * A UI showing an Android Wear Widget. It features a dark gray rounded rectangle container with
 * the title "System Theme" in white. Inside are two horizontal rounded buttons: "Primary Button"
 * with dark text on light blue-gray, and "Secondary Button" with dark text on light gray. An
 * Android logo is at the top.
 */
@RemoteComposable
@Composable
fun SystemThemeComparisonSample() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText("System Theme".rs)
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(onClick = ValueChange(dummy, 0.ri)) { MaterialRemoteText("Primary Button".rs) }
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(
                    onClick = ValueChange(dummy, 0.ri),
                    colors =
                        RemoteButtonColors(
                            containerColor = RemoteMaterialTheme.colorScheme.secondary,
                            contentColor = RemoteMaterialTheme.colorScheme.onSecondary,
                            secondaryContentColor = RemoteMaterialTheme.colorScheme.onSecondary,
                            iconColor = RemoteMaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = Color.Gray.rc,
                            disabledContentColor = Color.LightGray.rc,
                            disabledSecondaryContentColor = Color.LightGray.rc,
                            disabledIconColor = Color.LightGray.rc,
                        ),
                ) {
                    MaterialRemoteText("Secondary Button".rs)
                }
            }
        }
    }
}

/**
 * A dark screen displays a light gray Android icon above "Wear Widget". Below, a dark gray
 * rounded rectangle contains "System Theme" text, and a light purple button labeled "Primary
 * Button".
 */
@RemoteComposable
@Composable
fun SystemThemeSample() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText("System Theme".rs)
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(onClick = ValueChange(dummy, 0.ri)) { MaterialRemoteText("Primary Button".rs) }
            }
        }
    }
}

/**
 * Android icon and "Wear Widget" title above a dark gray rounded card. The card displays "Aussie
 * Theme" heading, a blue rounded button labeled "Primary (Blue)", and a red rounded button
 * labeled "Secondary (Red)".
 */
@RemoteComposable
@Composable
fun AustralianThemeSample() {
    val dummy = rememberRemoteIntValue { 0 }
    val australianColorScheme =
        object : RemoteColorScheme() {
            // Australian Flag Blue
            override val primary: RemoteColor
                @RemoteComposable @Composable get() = Color(0xFF00008B).rc

            override val onPrimary: RemoteColor
                @RemoteComposable @Composable get() = Color.White.rc

            // Australian Flag Red
            override val secondary: RemoteColor
                @RemoteComposable @Composable get() = Color(0xFFFF0000).rc

            override val onSecondary: RemoteColor
                @RemoteComposable @Composable get() = Color.White.rc

            // White stars
            override val tertiary: RemoteColor
                @RemoteComposable @Composable get() = Color.White.rc

            override val onTertiary: RemoteColor
                @RemoteComposable @Composable get() = Color.Black.rc
        }

    RemoteMaterialTheme(colorScheme = australianColorScheme) {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText("Aussie Theme".rs)
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(onClick = ValueChange(dummy, 0.ri)) { MaterialRemoteText("Primary (Blue)".rs) }
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(
                    onClick = ValueChange(dummy, 0.ri),
                    colors =
                        RemoteButtonColors(
                            containerColor = australianColorScheme.secondary,
                            contentColor = australianColorScheme.onSecondary,
                            secondaryContentColor = australianColorScheme.onSecondary,
                            iconColor = australianColorScheme.onSecondary,
                            disabledContainerColor = Color.Gray.rc,
                            disabledContentColor = Color.LightGray.rc,
                            disabledSecondaryContentColor = Color.LightGray.rc,
                            disabledIconColor = Color.LightGray.rc,
                        ),
                ) {
                    MaterialRemoteText("Secondary (Red)".rs)
                }
            }
        }
    }
}

/**
 * A black screen shows a circular Android icon, then "Wear Widget" in white text. Below it, a large,
 * dark grey rounded rectangle displays "Box Sample 1" centered in white text.
 */
@RemoteComposable
@Composable
fun BoxSample1() {
    // Simple Box with background color and centered text
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.DarkGray),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(text = "Box Sample 1", color = Color.White.rc)
    }
}

/**
 * Android logo and "Wear Widget" text. A dark grey rounded rectangle widget displays white text
 * "Box Sample 2 (Border & Padding)", centered and surrounded by a distinct red rectangular
 * border. The text is visibly padded from the red border, which itself is padded from the grey
 * widget's edges.
 */
@RemoteComposable
@Composable
fun BoxSample2() {
    // Box with padding and border
    RemoteBox(
        modifier =
            RemoteModifier.fillMaxSize().padding(20.dp).border(width = 2.rdp, color = Color.Red.rc),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(
            text = "Box Sample 2\n(Border & Padding)",
            color = Color.White.rc,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Android logo above "Wear Widget" text. Below, a large blue rectangle with a gray border. Inside
 * the blue box, at the bottom right, is yellow text: "Box Sample 3 (Bottom End)".
 */
@RemoteComposable
@Composable
fun BoxSample3() {
    // Box with different alignment (BottomEnd)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Blue),
        horizontalAlignment = RemoteAlignment.End,
        verticalArrangement = RemoteArrangement.Bottom,
    ) {
        RemoteText(
            modifier = RemoteModifier.padding(10.dp),
            text = "Box Sample 3\n(Bottom End)",
            color = Color.Yellow.rc,
            textAlign = TextAlign.End,
        )
    }
}

/**
 * Android logo above "Wear Widget" title. A dark-grey bordered, rounded green widget displays:
 * white bold "TextSample1", white ellipsized "This is a long text that should wrap to multiple
 * lin...", and green "Version 1.0".
 */
@RemoteComposable
@Composable
fun TextSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color(0xFF006400)),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText(
                text = "TextSample1",
                color = Color.White.rc,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            RemoteText(
                text =
                    "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.",
                color = Color.LightGray.rc,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            RemoteText(
                text = "Version 1.0",
                color = Color.Cyan.rc,
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

/**
 * UI screenshot with a black background. At the top, an Android icon above "Wear Widget" text.
 * Below, a dark green rounded rectangular widget with a gray border. Inside, three lines of white
 * text: "TextSample", "1", and "This is a long text".
 */
@RemoteComposable
@Composable
fun TextSample1WithMargin() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color(0xFF006400)).padding(30.dp),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText(
                text = "TextSample1",
                color = Color.White.rc,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            RemoteText(
                text =
                    "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.",
                color = Color.LightGray.rc,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            RemoteText(
                text = "Version 1.0",
                color = Color.Cyan.rc,
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

/**
 * A screen features an Android robot head logo and "Wear Widget" text at the top. Below, a dark
 * gray bordered rectangle serves as a widget frame. Inside, three horizontal buttons are
 * displayed: a red button with "Red" text, a green button with "Green" text, and a blue button
 * with "Blue" text. All button text is white.
 */
@RemoteComposable
@Composable
fun RowSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteRow(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalArrangement = RemoteArrangement.CenterHorizontally,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Red)) {
                RemoteText("Red", color = Color.White.rc, modifier = RemoteModifier.padding(5.dp))
            }
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Green)) {
                RemoteText("Green", color = Color.Black.rc, modifier = RemoteModifier.padding(5.dp))
            }
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Blue)) {
                RemoteText("Blue", color = Color.White.rc, modifier = RemoteModifier.padding(5.dp))
            }
        }
    }
}

/**
 * A screen displaying "Wear Widget" text, topped by an Android icon. Below, a dark gray rounded
 * rectangular widget displays "Item 1" (light gray), "Item 2" (yellow, centered), and "Item 3"
 * (light gray). Item 2 is visually prominent, indicating selection.
 */
@RemoteComposable
@Composable
fun RowSample2() {
    // WORKAROUND: Replaced RemoteCollapsibleRow with RemoteRow due to an "Invalid enum value:
    // Orientation"
    // error when rendering the RemoteCollapsibleRow. It seems the RemoteCollapsibleRow's
    // orientation parameter was not being correctly handled by the renderer.
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.DarkGray),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteRow(
            modifier = RemoteModifier.fillMaxSize().padding(5.dp),
            horizontalArrangement = RemoteArrangement.SpaceBetween,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            RemoteText("Item 1", color = Color.White.rc)
            RemoteText("Item 2", color = Color.Yellow.rc)
            RemoteText("Item 3", color = Color.Gray.rc)
        }
    }
}

/**
 * Android logo above "Wear Widget" text. Below, a rounded rectangular container displays
 * centered, vertically stacked text: "Top (High)" in red, "Middle (Low)" in green, and "Bottom
 * (High)" in blue.
 */
@RemoteComposable
@Composable
fun CollapsibleColumnSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteCollapsibleColumn(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            RemoteText("Top (High)", color = Color.Red.rc, modifier = RemoteModifier.priority(1.0f))
            RemoteText(
                "Middle (Low)",
                color = Color.Green.rc,
                modifier = RemoteModifier.priority(0.1f),
            )
            RemoteText(
                "Bottom (High)",
                color = Color.Blue.rc,
                modifier = RemoteModifier.priority(1.0f),
            )
        }
    }
}

/**
 * A dark screen shows an Android logo above "Wear Widget" text. Below, a large dark gray rounded
 * rectangular card contains a smaller, centered light purple rounded button labeled "Simple
 * Button".
 */
@RemoteComposable
@Composable
fun ButtonSample1() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(onClick = ValueChange(dummy, 0.ri), modifier = RemoteModifier.buttonSizeModifier()) {
            MaterialRemoteText("Simple Button".rs)
        }
    }
}

/**
 * UI screenshot on a black background. Top center features a light grey circular Android robot
 * icon. Below it, white text "Wear Widget". A dark grey rounded rectangular container holds a
 * light purple rounded button with a dark grey Android chip-like icon and the text "Button with
 * Icon".
 */
@RemoteComposable
@Composable
fun ButtonSample2() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
            icon = {
                RemoteIcon(
                    imageVector =
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Icon".rs,
                    modifier = RemoteModifier.size(RemoteButtonDefaults.IconSize),
                )
            },
            label = { MaterialRemoteText("Button with Icon".rs) },
        )
    }
}

/**
 * A Wear OS widget example on a black background. Centered text "Wear Widget" appears above a large
 * dark grey rounded rectangle. Inside, a smaller, light purple-grey rounded rectangle is centered,
 * displaying "Primary Label" (dark grey, bold) stacked above "Secondary Label" (light grey).
 */
@RemoteComposable
@Composable
fun ButtonSample3() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
            secondaryLabel = { MaterialRemoteText("Secondary Label".rs) },
            label = { MaterialRemoteText("Primary Label".rs) },
        )
    }
}

/**
 * Black background. Top: Android icon above "Wear Widget" text. Below, a large gray rounded
 * rectangle contains a smaller, centered red rounded rectangle button with yellow text "Custom
 * Colors".
 */
@RemoteComposable
@Composable
fun ButtonSample4() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
            colors =
                RemoteButtonColors(
                    containerColor = Color.Red.rc,
                    contentColor = Color.Yellow.rc,
                    secondaryContentColor = Color.Yellow.rc,
                    iconColor = Color.Yellow.rc,
                    disabledContainerColor = Color.Gray.rc,
                    disabledContentColor = Color.LightGray.rc,
                    disabledSecondaryContentColor = Color.LightGray.rc,
                    disabledIconColor = Color.LightGray.rc,
                ),
        ) {
            MaterialRemoteText("Custom Colors".rs)
        }
    }
}

/**
 * A screen with a black background shows an Android robot icon in a white circle at the top, above
 * the white text "Wear Widget". Below this is a large dark grey rounded rectangle containing a
 * smaller, lighter grey rounded button with the grey text "Disabled Button".
 */
@RemoteComposable
@Composable
fun ButtonSample6() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(onClick = ValueChange(dummy, 0.ri), modifier = RemoteModifier.buttonSizeModifier(), enabled = false.rb) {
            MaterialRemoteText("Disabled Button".rs)
        }
    }
}

/**
 * A dark UI displays an Android logo, text "Wear Widget," and a rounded dark gray card. Inside the
 * card are two horizontal, light gray, pill-shaped buttons: "Yes" on the left and "No" on the
 * right.
 */
@RemoteComposable
@Composable
fun ButtonSample7() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteRow(
            modifier = RemoteModifier.padding(horizontal = 11.dp),
            horizontalArrangement = RemoteArrangement.CenterHorizontally,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            RemoteButton(onClick = ValueChange(dummy, 0.ri), modifier = RemoteModifier.weight(1f)) { MaterialRemoteText("Yes".rs) }
            RemoteBox(RemoteModifier.size(4.rdp)) // Spacing
            RemoteButton(onClick = ValueChange(dummy, 0.ri), modifier = RemoteModifier.weight(1f)) { MaterialRemoteText("No".rs) }
        }
    }
}

/**
 * Android logo above "Wear Widget" title. Below, a dark gray rounded dialog box contains two light
 * purple, rounded buttons side-by-side. The left button says "Yes Confirm", and the right says "No
 * Cancel".
 */
@RemoteComposable
@Composable
fun ButtonSample8() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButtonGroup {
            RemoteButton(
                onClick = ValueChange(dummy, 0.ri),
                modifier = RemoteModifier.weight(1f),
                label = { MaterialRemoteText("Yes".rs) },
                secondaryLabel = { MaterialRemoteText("Confirm".rs) },
            )
            RemoteBox(RemoteModifier.size(RemoteButtonGroupDefaults.Spacing))
            RemoteButton(
                onClick = ValueChange(dummy, 0.ri),
                modifier = RemoteModifier.weight(1f),
                label = { MaterialRemoteText("No".rs) },
                secondaryLabel = { MaterialRemoteText("Cancel".rs) },
            )
        }
    }
}

/**
 * UI with Android logo, 'Wear Widget' title, and a 2x2 grid of rounded buttons on a black
 * background. Buttons: top-left blue 'Toggle' (yellow text), top-right magenta 'Shape' (white
 * text), bottom-left dark gray 'Click Me' (white text), bottom-right green 'Fixed' (white text).
 */
@RemoteComposable
@Composable
fun ButtonSample9() {
    val state = rememberRemoteIntValue { 0 }
    val isToggled = state eq 1.ri

    // Dynamic Color
    val containerColor = isToggled.select(Color.Red.rc, Color.Blue.rc)
    val contentColor = isToggled.select(Color.White.rc, Color.Yellow.rc)

    // Dynamic Shape (Radius)
    val radiusDp = isToggled.select(50f.rf, 8f.rf).asRemoteDp()
    val dynamicShape = RemoteRoundedCornerShape(radiusDp)

    // Dynamic Text
    val button3Text = isToggled.select("Clicked!".rs, "Click Me".rs)

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            // Row 1
            RemoteRow(
                modifier = RemoteModifier.weight(1f).fillMaxSize(),
                horizontalArrangement = RemoteArrangement.CenterHorizontally,
            ) {
                // Button 1: Toggles State (Master Switch)
                RemoteButton(
                    modifier =
                        RemoteModifier.weight(1f).fillMaxSize().animationSpec(enabled = true),
                    onClick = ValueChange(state, state xor 1.ri),
                    colors =
                        RemoteButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColor,
                            secondaryContentColor = contentColor,
                            iconColor = contentColor,
                            disabledContainerColor = Color.Gray.rc,
                            disabledContentColor = Color.LightGray.rc,
                            disabledSecondaryContentColor = Color.LightGray.rc,
                            disabledIconColor = Color.LightGray.rc,
                        ),
                ) {
                    MaterialRemoteText("Toggle".rs)
                }
                RemoteBox(RemoteModifier.size(4.rdp))
                // Button 2: Shape Shifter
                RemoteButton(
                    onClick = ValueChange(rememberRemoteIntValue { 0 }, 0.ri),
                    modifier =
                        RemoteModifier.weight(1f).fillMaxSize().animationSpec(enabled = true),
                    shape = dynamicShape,
                    colors =
                        RemoteButtonColors(
                            containerColor = Color.Magenta.rc,
                            contentColor = Color.White.rc,
                            secondaryContentColor = Color.White.rc,
                            iconColor = Color.White.rc,
                            disabledContainerColor = Color.Gray.rc,
                            disabledContentColor = Color.LightGray.rc,
                            disabledSecondaryContentColor = Color.LightGray.rc,
                            disabledIconColor = Color.LightGray.rc,
                        ),
                ) {
                    MaterialRemoteText("Shape".rs)
                }
            }

            RemoteBox(RemoteModifier.size(4.rdp))

            // Row 2
            RemoteRow(
                modifier = RemoteModifier.weight(1f).fillMaxSize(),
                horizontalArrangement = RemoteArrangement.CenterHorizontally,
            ) {
                // Button 3: Text Change
                RemoteButton(
                    onClick = ValueChange(rememberRemoteIntValue { 0 }, 0.ri),
                    modifier =
                        RemoteModifier.weight(1f).fillMaxSize().animationSpec(enabled = true),
                    colors =
                        RemoteButtonColors(
                            containerColor = Color.DarkGray.rc,
                            contentColor = Color.White.rc,
                            secondaryContentColor = Color.White.rc,
                            iconColor = Color.White.rc,
                            disabledContainerColor = Color.Gray.rc,
                            disabledContentColor = Color.LightGray.rc,
                            disabledSecondaryContentColor = Color.LightGray.rc,
                            disabledIconColor = Color.LightGray.rc,
                        ),
                ) {
                    MaterialRemoteText(button3Text)
                }
                RemoteBox(RemoteModifier.size(4.rdp))
                // Button 4: Static
                RemoteButton(
                    onClick = ValueChange(rememberRemoteIntValue { 0 }, 0.ri),
                    modifier = RemoteModifier.weight(1f).fillMaxSize(),
                    colors =
                        RemoteButtonColors(
                            containerColor = Color.Green.rc,
                            contentColor = Color.Black.rc,
                            secondaryContentColor = Color.Black.rc,
                            iconColor = Color.Black.rc,
                            disabledContainerColor = Color.Gray.rc,
                            disabledContentColor = Color.LightGray.rc,
                            disabledSecondaryContentColor = Color.LightGray.rc,
                            disabledIconColor = Color.LightGray.rc,
                        ),
                ) {
                    MaterialRemoteText("Fixed".rs)
                }
            }
        }
    }
}

/**
 * Screenshot shows an Android Wear Widget. Top center: Android logo icon. Below, white text 'Wear
 * Widget'. Inside a rounded rectangle frame on black, three tilted Android robot heads: small
 * red, medium green, and large blue, increasing in size left to right.
 */
@RemoteComposable
@Composable
fun IconSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.CenterHorizontally,
        ) {
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Small Red".rs,
                modifier = RemoteModifier.size(24.rdp),
                tint = Color.Red.rc,
            )
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Medium Green".rs,
                modifier = RemoteModifier.size(48.rdp),
                tint = Color.Green.rc,
            )
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Large Blue".rs,
                modifier = RemoteModifier.size(72.rdp),
                tint = Color.Blue.rc,
            )
        }
    }
}

/**
 * Screenshot showing a "Wear Widget" title and Android Wear OS icon above a gray-bordered frame.
 * Inside the frame is a 2x2 grid of colored blocks: top-left red with "1", top-right blue with
 * "2", bottom-left green with "3", and bottom-right yellow with "4".
 */
@RemoteComposable
@Composable
fun GridSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize().padding(20.dp),
            verticalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            RemoteRow(modifier = RemoteModifier.weight(1f)) {
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Red),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("1", color = Color.White.rc)
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Blue),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("2", color = Color.White.rc)
                }
            }
            RemoteRow(modifier = RemoteModifier.weight(1f)) {
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Green),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("3", color = Color.Black.rc)
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Yellow),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("4", color = Color.Black.rc)
                }
            }
        }
    }
}

/**
 * A dark screen shows a light gray circular icon with an Android head, then white text "Wear
 * Widget". Below, a dark gray rounded rectangle card features a tilted cyan Android icon on the
 * left and stacked white text "Card Title", "Subtitle", "goes here" on the right. Small black
 * triangles highlight each of the card's four corners.
 */
@RemoteComposable
@Composable
fun CardSample1() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.fillMaxSize().padding(10.dp),
            enabled = false.rb, // act as container
            colors =
                RemoteButtonColors(
                    containerColor = Color.DarkGray.rc,
                    contentColor = Color.White.rc,
                    secondaryContentColor = Color.LightGray.rc,
                    iconColor = Color.White.rc,
                    disabledContainerColor = Color.DarkGray.rc,
                    disabledContentColor = Color.White.rc,
                    disabledSecondaryContentColor = Color.LightGray.rc,
                    disabledIconColor = Color.White.rc,
                ),
        ) {
            RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                RemoteIcon(
                    imageVector =
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Card Icon".rs,
                    modifier = RemoteModifier.size(40.rdp),
                    tint = Color.Cyan.rc,
                )
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteColumn {
                    MaterialRemoteText("Card Title".rs)
                    MaterialRemoteText("Subtitle goes here".rs)
                }
            }
        }
    }
}

/**
 * A black screen shows an Android icon and "Wear Widget" text at the top. Below, a dark gray
 * rounded rectangle contains a horizontal row of UI elements: a light gray oval button with a
 * minus sign, a red rectangular box displaying "0" in white text, and a light gray oval button
 * with a plus sign.
 */
@RemoteComposable
@Composable
fun CounterSample1() {
    val count = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.CenterHorizontally,
        ) {
            RemoteButton(
                onClick = ValueChange(count, count - 1.ri),
                modifier = RemoteModifier.size(40.rdp),
            ) {
                MaterialRemoteText("-".rs)
            }

            RemoteBox(RemoteModifier.size(10.rdp))

            RemoteBox(modifier = RemoteModifier.background(Color.Red)) {
                RemoteText(
                    text = count.toRemoteString(10, TextFromFloat.PAD_PRE_NONE),
                    color = Color.White.rc,
                    fontSize = 24.sp,
                )
            }

            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteButton(
                onClick = ValueChange(count, count + 1.ri),
                modifier = RemoteModifier.size(40.rdp),
            ) {
                MaterialRemoteText("+".rs)
            }
        }
    }
}

/**
 * Android robot logo above 'Wear Widget' text. Below is a rounded rectangular widget with a 5x4
 * grid of color swatches: shades of red, blue-grey, purple, and a bottom row of dark grey, medium
 * grey, light grey, black, and white.
 */
@RemoteComposable
@Composable
fun Material3ThemeSample() {
    RemoteMaterialTheme {
        RemoteColumn(modifier = RemoteModifier.fillMaxSize()) {
            RemoteColorRow {
                RemoteColorBox(RemoteMaterialTheme.colorScheme.error)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.errorDim)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.errorContainer)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onError)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onErrorContainer)
            }
            RemoteColorRow {
                RemoteColorBox(RemoteMaterialTheme.colorScheme.primary)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.primaryDim)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.primaryContainer)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onPrimary)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onPrimaryContainer)
            }
            RemoteColorRow {
                RemoteColorBox(RemoteMaterialTheme.colorScheme.secondary)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.secondaryDim)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.secondaryContainer)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onSecondary)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onSecondaryContainer)
            }
            RemoteColorRow {
                RemoteColorBox(RemoteMaterialTheme.colorScheme.tertiary)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.tertiaryDim)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.tertiaryContainer)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onTertiary)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onTertiaryContainer)
            }
            RemoteColorRow {
                RemoteColorBox(RemoteMaterialTheme.colorScheme.surfaceContainer)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.surfaceContainerLow)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.surfaceContainerHigh)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onSurface)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onSurfaceVariant)
            }
            RemoteColorRow {
                RemoteColorBox(RemoteMaterialTheme.colorScheme.outline)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.outlineVariant)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.background)
                RemoteColorBox(RemoteMaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@RemoteComposable
@Composable
private fun RemoteColumnScope.RemoteColorRow(
    content: @RemoteComposable @Composable RemoteRowScope.() -> Unit
) {
    RemoteRow(modifier = RemoteModifier.fillMaxWidth().weight(1f), content = content)
}

@RemoteComposable
@Composable
private fun RemoteRowScope.RemoteColorBox(color: RemoteColor) {
    RemoteBox(modifier = RemoteModifier.fillMaxHeight().weight(1f).background(color))
}

/**
 * Android icon and text "Wear Widget" above a dark gray framed rectangle. Inside the frame, a red
 * circle is centered at the top and a blue rectangle is centered at the bottom.
 */
@RemoteComposable
@Composable
fun CanvasSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            // Draw a circle
            drawCircle(
                paint = RemotePaint().apply { remoteColor = Color.Red.rc },
                radius = 50f.rf,
                center = RemoteOffset(centerX, centerY),
            )

            // Draw a rect
            drawRect(
                paint = RemotePaint().apply { remoteColor = Color.Blue.rc },
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
fun CanvasSample2() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.DarkGray),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            // Draw a triangle path
            val path =
                Path().apply {
                    moveTo(0f, -50f)
                    lineTo(50f, 50f)
                    lineTo(-50f, 50f)
                    close()
                }

            translate(centerX, centerY) {
                drawPath(path = path, paint = RemotePaint().apply { remoteColor = Color.Yellow.rc })
            }
        }
    }
}

/**
 * Android icon and "Wear Widget" text at the top. Below, a dark-grey-bordered rectangle with
 * rounded corners contains a red (top-left) to blue (bottom-right) gradient background. White text
 * "Gradient Background" is centered within the rectangle.
 */
@RemoteComposable
@Composable
fun GradientBackgroundSample() {
    val gradient =
        RemoteLinearGradient(
            colors = listOf(Color.Red.rc, Color.Blue.rc),
            start = RemoteOffset.Zero,
            end = null,
        )
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(brush = gradient),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        MaterialRemoteText("Gradient Background".rs)
    }
}

/**
 * A Wear OS widget design features an Android robot logo above "Wear Widget" text. Below, a dark
 * gray rounded rectangle container, representing the widget, frames a central light purple-blue
 * rounded button with "Open App" in dark text, all on a black background.
 */
@RemoteComposable
@Composable
fun PendingIntentSample() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val intent =
        Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
    val pendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            modifier = RemoteModifier.buttonSizeModifier(),
            onClick = pendingIntentAction(pendingIntent),
        ) {
            MaterialRemoteText("Open App".rs)
        }
    }
}

/**
 * A UI displays an Android logo and "Wear Widget" title. Below, a dark grey rounded widget shows
 * a vertical list of white text: "Header", "Item 0", "Item 1", "Item 2", "Item 3", with "Item 4"
 * partially visible, indicating scrollable content.
 */
@RemoteComposable
@Composable
fun VerticalScrollSample() {
    val scrollState = rememberRemoteScrollState()
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Top,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxWidth().verticalScroll(scrollState),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Top,
        ) {
            MaterialRemoteText("Header".rs)
            RemoteBox(RemoteModifier.size(10.rdp))
            for (i in 0 until 10) {
                MaterialRemoteText(("Item " + i).rs)
                RemoteBox(RemoteModifier.size(10.rdp))
            }
            MaterialRemoteText("Footer".rs)
        }
    }
}

/**
 * A screen features an Android icon and "Wear Widget" text at the top. Below, a white rectangular
 * card with rounded corners displays "Mixed Styles". The word "Mixed" is red and bold, while
 * "Styles" is blue and italic.
 */
@RemoteComposable
@Composable
fun MixedStyleSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.White),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.CenterHorizontally,
        ) {
            // First part: Bold Red Text
            RemoteText(
                text = "Mixed ".rs,
                color = Color.Red.rc,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            // Second part: Italic Blue Text
            RemoteText(
                text = "Styles".rs,
                color = Color.Blue.rc,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@RemoteComposable
@Composable
fun ConditionalRadiusSample() {
    RemoteBox(modifier = RemoteModifier.fillMaxSize()) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            // 1. Get Width and Density
            val widthPx = remote.component.width
            val density = RemoteDensity.HOST.density

            // 2. Calculate Condition (Width > 200dp?)
            val widthDp = widthPx / density
            val isWide = widthDp gt 200f.rf

            // 3. Select Value (15dp if wide, 10dp if narrow) and convert to RemoteDp
            val radiusDp = isWide.select(15f.rf, 10f.rf).asRemoteDp()
            val radiusPx = radiusDp.toPx(RemoteDensity.HOST)

            // 4. Use the value (Converting back to Px for drawing commands)
            drawRoundRect(
                paint = RemotePaint().apply { remoteColor = Color.Blue.rc },
                topLeft = RemoteOffset.Zero,
                size = RemoteSize(widthPx, remote.component.height),
                cornerRadius = RemoteOffset(radiusPx, radiusPx),
            )
        }
    }
}

/**
 * A Wear OS widget display with an Android robot icon and "Wear Widget" text at the top. Below, a
 * rounded black rectangle contains three lines of text: "Title Style" in large cyan, "Default
 * Body Style" in white, and "Caption Style" in white italics.
 */
@RemoteComposable
@Composable
fun TypographyScaleSample() {
    // Define our own "semantic" styles
    val myTitleStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Cyan)

    val myCaptionStyle =
        TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic, color = Color.LightGray)

    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize().background(Color.Black),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                // 1. Title style applied manually
                MaterialRemoteText(
                    text = "Title Style".rs,
                    fontSize = myTitleStyle.fontSize,
                    fontWeight = myTitleStyle.fontWeight,
                    color = RemoteColor(myTitleStyle.color),
                )

                RemoteBox(RemoteModifier.size(12.rdp))

                // 2. Default style (bodyLarge) provided by RemoteMaterialTheme
                MaterialRemoteText("Default Body Style".rs)

                RemoteBox(RemoteModifier.size(12.rdp))

                // 3. Caption style applied via 'style' parameter
                MaterialRemoteText(text = "Caption Style".rs, style = myCaptionStyle)
            }
        }
    }
}

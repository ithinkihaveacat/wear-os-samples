@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.core.operations.TextFromFloat
import androidx.compose.remote.creation.compose.action.ValueChange
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
import androidx.compose.remote.creation.compose.modifier.BlurEffect
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.animationSpec
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.fillMaxHeight
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.fillMaxWidth
import androidx.compose.remote.creation.compose.modifier.graphicsLayer
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.painter.painterRemoteColor
import androidx.compose.remote.creation.compose.shaders.RemoteBrush
import androidx.compose.remote.creation.compose.shaders.RemoteLinearGradient
import androidx.compose.remote.creation.compose.shaders.linearGradient
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.RemoteColor
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
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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
    ): WearWidgetData =
        WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Black)) {
            GradientBackgroundSample()
        }
}

/**
 * A sample demonstrating a crash when using Infinite offset for gradient end.
 */
@RemoteComposable
@Composable
fun GradientBackgroundSampleCrash() {
    val gradient = RemoteBrush.linearGradient(
        colors = listOf(Color.Red, Color.Blue),
        start = RemoteOffset.Zero,
        end = RemoteOffset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    RemoteBox(
        modifier = RemoteModifier
            .fillMaxSize()
            .background(brush = gradient),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        MaterialRemoteText("Gradient Crash".rs)
    }
}

/**
 * A sample demonstrating the use of the System Theme with multiple components. This matches the
 * structure of AustralianThemeSample for comparison.
 */
@RemoteComposable
@Composable
fun SystemThemeComparisonSample() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText("System Theme".rs)
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(onClick = arrayOf()) { MaterialRemoteText("Primary Button".rs) }
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(
                    onClick = arrayOf(),
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
                ) { MaterialRemoteText("Secondary Button".rs) }
            }
        }
    }
}

/**
 * A sample demonstrating the use of the System Theme. The button should pick up the system's
 * primary color.
 */
@RemoteComposable
@Composable
fun SystemThemeSample() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText("System Theme".rs)
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(onClick = arrayOf()) { MaterialRemoteText("Primary Button".rs) }
            }
        }
    }
}

/** A sample demonstrating a custom theme (Australian Flag Colors). */
@RemoteComposable
@Composable
fun AustralianThemeSample() {
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
                RemoteButton(onClick = arrayOf()) { MaterialRemoteText("Primary (Blue)".rs) }
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(
                    onClick = arrayOf(),
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
                ) { MaterialRemoteText("Secondary (Red)".rs) }
            }
        }
    }
}

/**
 * UI on a black background. At the top, "Wear Widget" is centered in white. Below it, a large dark
 * grey rectangular box is centered horizontally with significant side margins, containing the white
 * text "Box Sample 1" in its center.
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
 * "Wear Widget" header at the top on a black background. A central rectangle with a thin red border
 * contains the white text "Box Sample 2 (Border & Padding)". The layout shows padding between the
 * text and the red border, all horizontally centered.
 */
@RemoteComposable
@Composable
fun BoxSample2() {
    // Box with padding and border
    RemoteBox(
        modifier =
            RemoteModifier.fillMaxSize().padding(20.dp).border(width = 2.rdp, color = Color.Red),
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
 * Black background with white text "Wear Widget" at the top. Below, a solid blue rectangular box
 * contains yellow text in its bottom-right corner: "Box Sample 3 (Bottom End)".
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
 * UI screen with "Wear Widget" header on a black background. A central green rectangle contains
 * bold white "TextSample1", truncated light-grey text "This is a long text that should wrap to
 * multiple li...", and small cyan italicized "Version 1.0" at the bottom.
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
 * Black background with white "Wear Widget" header. A central dark green rectangle contains white
 * text: "TextSampl" wrapped to "e1" on the second line. Below, smaller text "This is a long" is
 * partially clipped by the rectangle's bottom edge.
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
 * "Wear Widget" header in white text centered at the top. Below, three horizontal rectangles: red
 * with white "Red", green with black "Green", and blue with white "Blue". All elements centered on
 * a black background with large margins.
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
 * White title "Wear Widget" centered at the top on a black background. Below, a gray rectangle
 * contains three horizontal labels: "Item 1" in white, "Item 2" in yellow, and "Item 3" in light
 * gray, centered with wide margins.
 *
 * WORKAROUND: Replaced RemoteCollapsibleRow with RemoteRow due to an "Invalid enum value:
 * Orientation" error when rendering the RemoteCollapsibleRow. It seems the RemoteCollapsibleRow's
 * orientation parameter was not being correctly handled by the renderer.
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

/** Displays a collapsible column. Items with lower priority may be hidden if space is limited. */
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
 * White text "Wear Widget" is at the top of a black background. A light lavender rounded button is
 * centered below, containing dark purple text "Simple Button." The layout is minimalist with wide
 * margins and a centered composition.
 */
@RemoteComposable
@Composable
fun ButtonSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(modifier = RemoteModifier.buttonSizeModifier()) {
            MaterialRemoteText("Simple Button".rs)
        }
    }
}

/**
 * A UI titled "Wear Widget" in white on a black background. Centered is a light purple rounded
 * button featuring a dark purple Android tag icon and the two-line text "Button with Icon" in dark
 * purple.
 */
@RemoteComposable
@Composable
fun ButtonSample2() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
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
 * Black interface with white 'Wear Widget' text at the top. Centered is a light purple rounded
 * button featuring 'Primary Label' in bold dark purple and 'Secondary Label' in regular purple,
 * stacked vertically.
 */
@RemoteComposable
@Composable
fun ButtonSample3() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
            modifier = RemoteModifier.buttonSizeModifier(),
            secondaryLabel = { MaterialRemoteText("Secondary Label".rs) },
            label = { MaterialRemoteText("Primary Label".rs) },
        )
    }
}

/**
 * A UI screenshot on a black background. At the top, white text reads "Wear Widget." Centered below
 * is a large, bright red rounded button with bold yellow text that reads "Custom Colors." Simple
 * layout with generous margins.
 */
@RemoteComposable
@Composable
fun ButtonSample4() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
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
 * Black UI screen featuring white text at the top reading "Wear Widget". In the center, a dark gray
 * rounded rectangular button displays the text "Disabled Button" in a light gray, muted font.
 */
@RemoteComposable
@Composable
fun ButtonSample6() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(modifier = RemoteModifier.buttonSizeModifier(), enabled = false.rb) {
            MaterialRemoteText("Disabled Button".rs)
        }
    }
}

/**
 * Black UI featuring "Wear Widget" in white text at the top. Two side-by-side, light lavender
 * pill-shaped buttons are centered below, labeled "Yes" and "No" in dark purple text. Symmetrical
 * layout with substantial black margins around the elements.
 */
@RemoteComposable
@Composable
fun ButtonSample7() {
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
            RemoteButton(modifier = RemoteModifier.weight(1f)) { MaterialRemoteText("Yes".rs) }
            RemoteBox(RemoteModifier.size(4.rdp)) // Spacing
            RemoteButton(modifier = RemoteModifier.weight(1f)) { MaterialRemoteText("No".rs) }
        }
    }
}

/**
 * "Wear Widget" white title on black. Below are two side-by-side light purple rounded buttons with
 * dark purple text. Left button: "Yes" above "Confir". Right button: "No" above "Cancel". Centered
 * layout.
 */
@RemoteComposable
@Composable
fun ButtonSample8() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButtonGroup {
            RemoteButton(
                modifier = RemoteModifier.weight(1f),
                label = { MaterialRemoteText("Yes".rs) },
                secondaryLabel = { MaterialRemoteText("Confirm".rs) },
            )
            RemoteBox(RemoteModifier.size(RemoteButtonGroupDefaults.Spacing))
            RemoteButton(
                modifier = RemoteModifier.weight(1f),
                label = { MaterialRemoteText("No".rs) },
                secondaryLabel = { MaterialRemoteText("Cancel".rs) },
            )
        }
    }
}

/**
 * "Wear Widget" interface featuring four rounded buttons: top-left (blue, yellow wrapped "Toggle"),
 * top-right (magenta, white "Shape"), bottom-left (dark gray, white wrapped "Click Me"), and
 * bottom-right (green, black "Fixed"). Black background.
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
                    onClick = arrayOf(ValueChange(state, state xor 1.ri)),
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
 * "Wear Widget" header on black background. Three stylized Android mascot icons arranged
 * horizontally, increasing in size from left to right: small red, medium green, and large blue.
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
 * 2x2 Grid layout on black background. Top-left: Red box with "1". Top-right: Blue box with "2".
 * Bottom-left: Green box with "3". Bottom-right: Yellow box with "4".
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
 * A dark screen displays "Wear Widget" in white at the top. Below, a rounded dark gray card
 * contains a cyan Android-style icon on the left. To its right, white text reads "Card Title"
 * followed by "Subtitle goes here" on two lines. The layout features generous margins within the
 * card.
 */
@RemoteComposable
@Composable
fun CardSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteButton(
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
 * A UI mockup on a black background. At the top, white text reads "Wear Widget." Below, a
 * horizontal counter features a central white "0" on a red rectangular background, flanked by light
 * purple rounded square buttons containing a dark purple minus sign on the left and a plus sign on
 * the right.
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
                onClick = arrayOf(ValueChange(count, count - 1.ri)),
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
                onClick = arrayOf(ValueChange(count, count + 1.ri)),
                modifier = RemoteModifier.size(40.rdp),
            ) {
                MaterialRemoteText("+".rs)
            }
        }
    }
}

/**
 * A dark screen with "Wear Widget" text under a green Android logo icon. Below the text, there is a
 * grid of color tiles arranged in rows. These tiles include various shades of red, purple, blue,
 * brown, grey, and white. There are two black tiles in the bottom row.
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
 * In a black UI, the Android logo and the text “Wear Widget” appear at the top. Below, a large red
 * circle is centered above a horizontal blue rectangle.
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
            drawCircle(color = Color.Red, radius = 50f.rf, center = RemoteOffset(centerX, centerY))

            // Draw a rect
            drawRect(
                color = Color.Blue,
                topLeft = RemoteOffset(centerX - 100f.rf, centerY + 60f.rf),
                size = RemoteSize(200f.rf, 50f.rf),
            )
        }
    }
}

/**
 * A screenshot against a black background. At the top, a circular light-green icon with a
 * dark-green Android logo sits above the white text "Wear Widget." Centered below, a large
 * dark-grey rectangle features a bright yellow equilateral triangle in its middle.
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

            drawContext.transform.translate(centerX, centerY)
            drawPath(path = path, color = Color.Yellow)
        }
    }
}

/**
 * On a black background, a light green Android icon is centered at the top. Below it is the white
 * text "Wear Widget," and further down is a neon green diamond-shaped button with the white text
 * "Rotated" in the center.
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

            rotate(45f.rf, centerX, centerY) {
                drawRect(
                    color = Color.Green,
                    topLeft = RemoteOffset(centerX - 40f.rf, centerY - 40f.rf),
                    size = RemoteSize(80f.rf, 80f.rf),
                )
            }

            // Anchored Text
            drawAnchoredText(
                text = "Rotated".rs,
                color = Color.White,
                anchor = RemoteOffset(centerX, centerY),
                panx = 0f.rf,
                pany = 0f.rf, // Center
                alpha = 1f.rf,
                drawStyle = Fill,
                typeface = null, // Typeface.DEFAULT
                textSize = 20f.rf,
            )
        }
    }
}

/**
 * In a vertical layout on a black background, a small Android icon sits above the white text "Wear
 * Widget." Below, a large rectangular box with a red-to-blue linear gradient contains the white
 * text "Gradient Background" centered inside.
 */
@RemoteComposable
@Composable
fun GradientBackgroundSample() {
    val gradient = RemoteLinearGradient(
        colors = listOf(Color.Red, Color.Blue),
        start = RemoteOffset.Zero,
        end = null
    )
    RemoteBox(
        modifier = RemoteModifier
            .fillMaxSize()
            .background(brush = gradient),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        MaterialRemoteText("Gradient Background".rs)
    }
}

/**
 * A sample demonstrating a Blur Effect using RemoteModifier.graphicsLayer.
 */
@RemoteComposable
@Composable
fun BlurSample() {
    RemoteBox(
        modifier = RemoteModifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        // A red box with a blur effect applied
        RemoteBox(
            modifier = RemoteModifier
                .size(100.rdp)
                .background(Color.Red)
                .graphicsLayer(renderEffect = BlurEffect(radiusX = 10f, radiusY = 10f))
        )
        
        // Text overlay (not blurred because it's a sibling, not child of the blurred box)
        MaterialRemoteText("Blurred Box".rs)
    }
}

/**
 * A sample demonstrating Opacity using RemoteModifier.graphicsLayer.
 */
@RemoteComposable
@Composable
fun OpacitySample() {
     RemoteBox(
        modifier = RemoteModifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        // A red box with 50% opacity
        RemoteBox(
            modifier = RemoteModifier
                .size(100.rdp)
                .background(Color.Red)
                .graphicsLayer(alpha = 0.5f.rf),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center
        ) {
             MaterialRemoteText("50%".rs)
        }
    }
}
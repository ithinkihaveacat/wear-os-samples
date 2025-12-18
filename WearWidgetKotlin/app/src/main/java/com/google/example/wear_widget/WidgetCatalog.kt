@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.creation.compose.painter.painterRemoteColor
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCollapsibleColumn
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteBoolean
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.asRdp
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.buttonSizeModifier
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

import androidx.wear.compose.remote.material3.RemoteButtonGroup
import androidx.wear.compose.remote.material3.RemoteButtonGroupDefaults

class WidgetCatalogService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = WidgetCatalog()
}

class WidgetCatalog : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData = 
        WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Black)) {
            ButtonSample8()
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
        RemoteText(text = "Box Sample 1", color = RemoteColor(Color.White))
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
            RemoteModifier.fillMaxSize()
                .padding(20.dp)
                .border(width = 2.dp.asRdp(), color = Color.Red),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(
            text = "Box Sample 2\n(Border & Padding)",
            color = RemoteColor(Color.White),
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
            color = RemoteColor(Color.Yellow),
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
                color = RemoteColor(Color.White),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            RemoteText(
                text =
                    "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.",
                color = RemoteColor(Color.LightGray),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            RemoteText(
                text = "Version 1.0",
                color = RemoteColor(Color.Cyan),
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
                color = RemoteColor(Color.White),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            RemoteText(
                text =
                    "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.",
                color = RemoteColor(Color.LightGray),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            RemoteText(
                text = "Version 1.0",
                color = RemoteColor(Color.Cyan),
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
                RemoteText(
                    "Red",
                    color = RemoteColor(Color.White),
                    modifier = RemoteModifier.padding(5.dp),
                )
            }
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Green)) {
                RemoteText(
                    "Green",
                    color = RemoteColor(Color.Black),
                    modifier = RemoteModifier.padding(5.dp),
                )
            }
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Blue)) {
                RemoteText(
                    "Blue",
                    color = RemoteColor(Color.White),
                    modifier = RemoteModifier.padding(5.dp),
                )
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
            RemoteText("Item 1", color = RemoteColor(Color.White))
            RemoteText("Item 2", color = RemoteColor(Color.Yellow))
            RemoteText("Item 3", color = RemoteColor(Color.Gray))
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
            RemoteText(
                "Top (High)",
                color = RemoteColor(Color.Red),
                modifier = RemoteModifier.priority(1.0f),
            )
            RemoteText(
                "Middle (Low)",
                color = RemoteColor(Color.Green),
                modifier = RemoteModifier.priority(0.1f),
            )
            RemoteText(
                "Bottom (High)",
                color = RemoteColor(Color.Blue),
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
        RemoteButton(
            modifier = RemoteModifier.buttonSizeModifier(),
        ) {
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
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
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
            colors = RemoteButtonColors(
                containerColor = RemoteColor(Color.Red),
                contentColor = RemoteColor(Color.Yellow),
                secondaryContentColor = RemoteColor(Color.Yellow),
                iconColor = RemoteColor(Color.Yellow),
                disabledContainerColor = RemoteColor(Color.Gray),
                disabledContentColor = RemoteColor(Color.LightGray),
                disabledSecondaryContentColor = RemoteColor(Color.LightGray),
                disabledIconColor = RemoteColor(Color.LightGray)
            )
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
        RemoteButton(
            modifier = RemoteModifier.buttonSizeModifier(),
            enabled = RemoteBoolean(false)
        ) {
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
             RemoteButton(
                 modifier = RemoteModifier.weight(1f)
             ) {
                 MaterialRemoteText("Yes".rs)
             }
             RemoteBox(RemoteModifier.size(4.dp.asRdp())) // Spacing
             RemoteButton(
                 modifier = RemoteModifier.weight(1f)
             ) {
                 MaterialRemoteText("No".rs)
             }
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
                 secondaryLabel = { MaterialRemoteText("Confirm".rs) }
             )
             RemoteBox(RemoteModifier.size(RemoteButtonGroupDefaults.Spacing))
             RemoteButton(
                 modifier = RemoteModifier.weight(1f),
                 label = { MaterialRemoteText("No".rs) },
                 secondaryLabel = { MaterialRemoteText("Cancel".rs) }
             )
        }
    }
}

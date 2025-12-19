@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.core.operations.TextFromFloat
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCollapsibleColumn
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.animationSpec
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.painter.painterRemoteColor
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.RemoteBoolean
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.remote.creation.compose.state.RemoteInt
import androidx.compose.remote.creation.compose.state.asRdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.rf
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
import androidx.wear.compose.remote.material3.RemoteButtonGroup
import androidx.wear.compose.remote.material3.RemoteButtonGroupDefaults
import androidx.wear.compose.remote.material3.RemoteIcon
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
        WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Black)) { CardSample1() }
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
                    containerColor = RemoteColor(Color.Red),
                    contentColor = RemoteColor(Color.Yellow),
                    secondaryContentColor = RemoteColor(Color.Yellow),
                    iconColor = RemoteColor(Color.Yellow),
                    disabledContainerColor = RemoteColor(Color.Gray),
                    disabledContentColor = RemoteColor(Color.LightGray),
                    disabledSecondaryContentColor = RemoteColor(Color.LightGray),
                    disabledIconColor = RemoteColor(Color.LightGray),
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
        RemoteButton(
            modifier = RemoteModifier.buttonSizeModifier(),
            enabled = RemoteBoolean(false),
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
            RemoteButton(modifier = RemoteModifier.weight(1f)) { MaterialRemoteText("Yes".rs) }
            RemoteBox(RemoteModifier.size(4.dp.asRdp())) // Spacing
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
    val isToggled = state eq RemoteInt(1)

    // Dynamic Color
    val containerColor = isToggled.select(RemoteColor(Color.Red), RemoteColor(Color.Blue))
    val contentColor = isToggled.select(RemoteColor(Color.White), RemoteColor(Color.Yellow))

    // Dynamic Shape (Radius)
    val radiusDp = RemoteDp(isToggled.select(50f.rf, 8f.rf))
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
                    onClick = arrayOf(ValueChange(state, state xor RemoteInt(1))),
                    colors =
                        RemoteButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColor,
                            secondaryContentColor = contentColor,
                            iconColor = contentColor,
                            disabledContainerColor = RemoteColor(Color.Gray),
                            disabledContentColor = RemoteColor(Color.LightGray),
                            disabledSecondaryContentColor = RemoteColor(Color.LightGray),
                            disabledIconColor = RemoteColor(Color.LightGray),
                        ),
                ) {
                    MaterialRemoteText("Toggle".rs)
                }
                RemoteBox(RemoteModifier.size(4.dp.asRdp()))
                // Button 2: Shape Shifter
                RemoteButton(
                    modifier =
                        RemoteModifier.weight(1f).fillMaxSize().animationSpec(enabled = true),
                    shape = dynamicShape,
                    colors =
                        RemoteButtonColors(
                            containerColor = RemoteColor(Color.Magenta),
                            contentColor = RemoteColor(Color.White),
                            secondaryContentColor = RemoteColor(Color.White),
                            iconColor = RemoteColor(Color.White),
                            disabledContainerColor = RemoteColor(Color.Gray),
                            disabledContentColor = RemoteColor(Color.LightGray),
                            disabledSecondaryContentColor = RemoteColor(Color.LightGray),
                            disabledIconColor = RemoteColor(Color.LightGray),
                        ),
                ) {
                    MaterialRemoteText("Shape".rs)
                }
            }

            RemoteBox(RemoteModifier.size(4.dp.asRdp()))

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
                            containerColor = RemoteColor(Color.DarkGray),
                            contentColor = RemoteColor(Color.White),
                            secondaryContentColor = RemoteColor(Color.White),
                            iconColor = RemoteColor(Color.White),
                            disabledContainerColor = RemoteColor(Color.Gray),
                            disabledContentColor = RemoteColor(Color.LightGray),
                            disabledSecondaryContentColor = RemoteColor(Color.LightGray),
                            disabledIconColor = RemoteColor(Color.LightGray),
                        ),
                ) {
                    MaterialRemoteText(button3Text)
                }
                RemoteBox(RemoteModifier.size(4.dp.asRdp()))
                // Button 4: Static
                RemoteButton(
                    modifier = RemoteModifier.weight(1f).fillMaxSize(),
                    colors =
                        RemoteButtonColors(
                            containerColor = RemoteColor(Color.Green),
                            contentColor = RemoteColor(Color.Black),
                            secondaryContentColor = RemoteColor(Color.Black),
                            iconColor = RemoteColor(Color.Black),
                            disabledContainerColor = RemoteColor(Color.Gray),
                            disabledContentColor = RemoteColor(Color.LightGray),
                            disabledSecondaryContentColor = RemoteColor(Color.LightGray),
                            disabledIconColor = RemoteColor(Color.LightGray),
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
                modifier = RemoteModifier.size(24.dp.asRdp()),
                tint = RemoteColor(Color.Red),
            )
            RemoteBox(RemoteModifier.size(10.dp.asRdp()))
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Medium Green".rs,
                modifier = RemoteModifier.size(48.dp.asRdp()),
                tint = RemoteColor(Color.Green),
            )
            RemoteBox(RemoteModifier.size(10.dp.asRdp()))
            RemoteIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Large Blue".rs,
                modifier = RemoteModifier.size(72.dp.asRdp()),
                tint = RemoteColor(Color.Blue),
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
                    RemoteText("1", color = RemoteColor(Color.White))
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Blue),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("2", color = RemoteColor(Color.White))
                }
            }
            RemoteRow(modifier = RemoteModifier.weight(1f)) {
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Green),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("3", color = RemoteColor(Color.Black))
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Yellow),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText("4", color = RemoteColor(Color.Black))
                }
            }
        }
    }
}

/**
 * A dark screen displays "Wear Widget" in white at the top. Below, a rounded dark gray card contains a cyan Android-style icon on the left. To its right, white text reads "Card Title" followed by "Subtitle goes here" on two lines. The layout features generous margins within the card.
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
            enabled = RemoteBoolean(false), // act as container
            colors =
                RemoteButtonColors(
                    containerColor = RemoteColor(Color.DarkGray),
                    contentColor = RemoteColor(Color.White),
                    secondaryContentColor = RemoteColor(Color.LightGray),
                    iconColor = RemoteColor(Color.White),
                    disabledContainerColor = RemoteColor(Color.DarkGray),
                    disabledContentColor = RemoteColor(Color.White),
                    disabledSecondaryContentColor = RemoteColor(Color.LightGray),
                    disabledIconColor = RemoteColor(Color.White),
                ),
        ) {
            RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                RemoteIcon(
                    imageVector =
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Card Icon".rs,
                    modifier = RemoteModifier.size(40.dp.asRdp()),
                    tint = RemoteColor(Color.Cyan),
                )
                RemoteBox(RemoteModifier.size(10.dp.asRdp()))
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
                onClick = arrayOf(ValueChange(count, count - 1)),
                modifier = RemoteModifier.size(40.dp.asRdp()),
            ) {
                MaterialRemoteText("-".rs)
            }

            RemoteBox(RemoteModifier.size(10.dp.asRdp()))

            RemoteBox(modifier = RemoteModifier.background(Color.Red)) {
                RemoteText(
                    text = count.toRemoteString(10, TextFromFloat.PAD_PRE_NONE),
                    color = RemoteColor(Color.White),
                    fontSize = 24.sp,
                )
            }

            RemoteBox(RemoteModifier.size(10.dp.asRdp()))
            RemoteButton(
                onClick = arrayOf(ValueChange(count, count + 1)),
                modifier = RemoteModifier.size(40.dp.asRdp()),
            ) {
                MaterialRemoteText("+".rs)
            }
        }
    }
}

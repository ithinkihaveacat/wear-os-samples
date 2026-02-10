package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.animationSpec
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

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

    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
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
}

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
import androidx.compose.remote.creation.compose.state.asRemoteDp
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteButtonDefaults
import androidx.wear.compose.remote.material3.RemoteButtonGroup
import androidx.wear.compose.remote.material3.RemoteButtonGroupDefaults
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.R
import com.google.example.wear_widget.WidgetPreview

/**
 * A dark screen shows an Android logo above "Wear Widget" text. Below, a large dark gray rounded
 * rectangular card contains a smaller, centered light purple rounded button labeled "Simple
 * Button".
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample1() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
        ) {
            MaterialRemoteText("Simple Button".rs)
        }
    }
}

/**
 * UI screenshot on a black background. Top center features a light grey circular Android robot
 * icon. Below it, white text "Wear Widget". A dark grey rounded rectangular container holds a light
 * purple rounded button with a dark grey Android chip-like icon and the text "Button with Icon".
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample2() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
            icon = {
                RemoteIcon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
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
fun ButtonReferenceSample3() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
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
fun ButtonReferenceSample4() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
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
fun ButtonReferenceSample6() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
            enabled = false.rb,
        ) {
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
fun ButtonReferenceSample7() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteRow(
            modifier = RemoteModifier.padding(horizontal = 11.rdp),
            horizontalArrangement = RemoteArrangement.Center,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            RemoteButton(
                onClick = ValueChange(dummy, 0.ri),
                modifier = RemoteModifier.weight(1f),
            ) {
                MaterialRemoteText("Yes".rs)
            }
            RemoteBox(RemoteModifier.size(4.rdp))
            RemoteButton(
                onClick = ValueChange(dummy, 0.ri),
                modifier = RemoteModifier.weight(1f),
            ) {
                MaterialRemoteText("No".rs)
            }
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
fun ButtonReferenceSample8() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
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
fun ButtonReferenceSample9() {
    val state = rememberMutableRemoteInt(0)
    val isToggled = state eq 1.ri

    val containerColor = isToggled.select(Color.Red.rc, Color.Blue.rc)
    val contentColor = isToggled.select(Color.White.rc, Color.Yellow.rc)
    val radiusDp = isToggled.select(50f.rf, 8f.rf).asRemoteDp()
    val dynamicShape = RemoteRoundedCornerShape(radiusDp)
    val button3Text = isToggled.select("Clicked!".rs, "Click Me".rs)

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize().padding(10.rdp),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteRow(
                modifier = RemoteModifier.weight(1f).fillMaxSize(),
                horizontalArrangement = RemoteArrangement.Center,
            ) {
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
                RemoteButton(
                    onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri),
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
            RemoteRow(
                modifier = RemoteModifier.weight(1f).fillMaxSize(),
                horizontalArrangement = RemoteArrangement.Center,
            ) {
                RemoteButton(
                    onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri),
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
                RemoteButton(
                    onClick = ValueChange(rememberMutableRemoteInt(0), 0.ri),
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

@PreviewWearLarge
@Composable
fun ButtonReferenceSample1Preview() {
    WidgetPreview { ButtonReferenceSample1() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample2Preview() {
    WidgetPreview { ButtonReferenceSample2() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample3Preview() {
    WidgetPreview { ButtonReferenceSample3() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample4Preview() {
    WidgetPreview { ButtonReferenceSample4() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample6Preview() {
    WidgetPreview { ButtonReferenceSample6() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample7Preview() {
    WidgetPreview { ButtonReferenceSample7() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample8Preview() {
    WidgetPreview { ButtonReferenceSample8() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample9Preview() {
    WidgetPreview { ButtonReferenceSample9() }
}

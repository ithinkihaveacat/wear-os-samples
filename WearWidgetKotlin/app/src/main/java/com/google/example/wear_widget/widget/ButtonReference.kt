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
import androidx.compose.remote.creation.compose.layout.RemoteImage
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.animationSpec
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shapes.RemoteCircleShape
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
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
 * A mobile UI on a black background shows a pink Android logo icon and "Widget Catalog" in white
 * text. Below is a dark grey rounded rectangle, containing a centered light pink rounded button
 * labeled "Simple Button" in dark red text.
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample1() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
        ) {
            MaterialRemoteText("Simple Button".rs)
        }
    }
}

/**
 * A black screen with a small pink circular Android logo, centered above white text "Widget
 * Catalog". Below is a dark gray rounded rectangle. Inside, a light pink rounded button shows a
 * dark reddish-brown Android logo and stacked dark reddish-brown text "Button with Icon".
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample2() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
 * A "Widget Catalog" screen. Top center, a pink circular icon with an Android logo. Below it, white
 * "Widget Catalog" text. A large dark gray rounded rectangle contains a smaller, nested pink
 * rounded rectangle. Inside the pink, prominent dark red "Primary Label" is above dark gray
 * "Secondary Label".
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample3() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
            secondaryLabel = { MaterialRemoteText("Secondary Label".rs) },
            label = { MaterialRemoteText("Primary Label".rs) },
        )
    }
}

/**
 * A screen features a pink Android logo above "Widget Catalog" in white. Centered below is a dark
 * grey rounded rectangle enclosing a smaller red rounded rectangle displaying "Custom Colors" in
 * yellow.
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample4() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
 * A screen titled "Widget Catalog" with a pink Android icon above it. Below, a large dark grey
 * rounded rectangle contains a lighter grey rounded button labeled "Disabled Button".
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample6() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
 * A dark screen displays a pink circle with an Android logo, above white text "Widget Catalog."
 * Below, a dark gray rounded rectangle frames two horizontal, pill-shaped pink buttons: "Yes"
 * (left) and "No" (right), both with dark red text.
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample7() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteRow(
            modifier = RemoteModifier.padding(horizontal = 11.rdp),
            horizontalArrangement = RemoteArrangement.Center,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            RemoteButton(onClick = ValueChange(dummy, 0.ri), modifier = RemoteModifier.weight(1f)) {
                MaterialRemoteText("Yes".rs)
            }
            RemoteBox(RemoteModifier.size(4.rdp))
            RemoteButton(onClick = ValueChange(dummy, 0.ri), modifier = RemoteModifier.weight(1f)) {
                MaterialRemoteText("No".rs)
            }
        }
    }
}

/**
 * A dark screen displays a pink Android logo and "Widget Catalog" title. Below, a large dark grey
 * rounded container features two side-by-side light pink rounded buttons. The left button reads
 * "Yes" and "Confir\nm" (Confirm is truncated). The right button reads "No" and "Cancel". All
 * button text is dark red.
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample8() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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
 * Screen displaying "Widget Catalog" with an Android icon. A dark grey rounded rectangle contains a
 * 2x2 grid of elements: a blue "Toggl" button (yellow text), a magenta "Shap" button (white text),
 * white text "Click" directly on the grey background, and a green "Fixed" button (white text).
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

    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
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

/**
 * Screenshot shows an Android robot icon above "Component Catalog" on a black background. Below, a
 * light purple chat bubble features a circular profile picture of a woman. Inside, text reads "Ali"
 * and "2:03 PM", followed by "Dinner in SF" and "Let's try that new restaurant."
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample10() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteButton(onClick = ValueChange(dummy, 0.ri)) {
            RemoteColumn {
                RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                    RemoteImage(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                        contentDescription = "Avatar".rs,
                        contentScale = ContentScale.Crop,
                        modifier = RemoteModifier.size(24.rdp).clip(RemoteCircleShape),
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
 * Dark UI with an Android robot icon above "Component Catalog" text. A dark gray rounded button
 * below displays "Text Data" in light gray, stacked over "Content" in white.
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample11() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
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
 * A dark mode screenshot displays a 'Widget Catalog'. At the top, a pink circle with a white
 * Android logo is centered above white text "Widget Catalog." Below, a large dark gray rounded
 * rectangular widget shows "Icon Data" (light gray text) above "Content" (white text), with a
 * message icon to its right.
 */
@RemoteComposable
@Composable
fun ButtonReferenceSample12() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
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
                imageVector =
                    androidx.compose.ui.graphics.vector.ImageVector.vectorResource(
                        id = R.drawable.ic_message_24
                    ),
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
fun ButtonReferenceSample13() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(modifier = RemoteModifier.fillMaxSize(), contentAlignment = RemoteAlignment.Center) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
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
                imageVector =
                    androidx.compose.ui.graphics.vector.ImageVector.vectorResource(
                        id = R.drawable.ic_run_24
                    ),
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

@PreviewWearLarge
@Composable
fun ButtonReferenceSample10Preview() {
    WidgetPreview { ButtonReferenceSample10() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample11Preview() {
    WidgetPreview { ButtonReferenceSample11() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample12Preview() {
    WidgetPreview { ButtonReferenceSample12() }
}

@PreviewWearLarge
@Composable
fun ButtonReferenceSample13Preview() {
    WidgetPreview { ButtonReferenceSample13() }
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

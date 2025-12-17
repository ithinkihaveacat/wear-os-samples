@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.creation.compose.capture.painter.painterRemoteColor
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCollapsibleColumn
import androidx.compose.remote.creation.compose.layout.RemoteCollapsibleRow
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.asRdp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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

class WidgetCatalogService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = WidgetCatalog()
}

class WidgetCatalog : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData =
        WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Black)) {
            RowSample2()
        }
}

/**
 * Displays a dark gray box that fills the screen. The text "Box Sample 1" is white and centered.
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
 * Displays a red bordered box with padding on a black background. The text "Box Sample 2 (Border & Padding)" is white and centered.
 */
@RemoteComposable
@Composable
fun BoxSample2() {
    // Box with padding and border
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().padding(20.dp).border(width = 2.dp.asRdp(), color = Color.Red),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(text = "Box Sample 2\n(Border & Padding)", color = RemoteColor(Color.White), textAlign = TextAlign.Center)
    }
}

/**
 * Displays a blue box that fills the screen. The text "Box Sample 3 (Bottom End)" is yellow and positioned at the bottom end.
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
            textAlign = TextAlign.End
        )
    }
}

/**
 * Displays a dark green box that fills the screen. It contains a column with "TextSample1" in white bold, wrapped light gray text, and "Version 1.0" in cyan italics.
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
                fontWeight = FontWeight.Bold
            )
            RemoteText(
                text = "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.",
                color = RemoteColor(Color.LightGray),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            RemoteText(
                text = "Version 1.0",
                color = RemoteColor(Color.Cyan),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

/**
 * Displays a row with three boxes of different colors: Red, Green, and Blue.
 */
@RemoteComposable
@Composable
fun RowSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteRow(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalArrangement = RemoteArrangement.CenterHorizontally,
            verticalAlignment = RemoteAlignment.CenterVertically
        ) {
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Red)) {
               RemoteText("Red", color = RemoteColor(Color.White), modifier = RemoteModifier.padding(5.dp))
            }
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Green)) {
               RemoteText("Green", color = RemoteColor(Color.Black), modifier = RemoteModifier.padding(5.dp))
            }
            RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Blue)) {
               RemoteText("Blue", color = RemoteColor(Color.White), modifier = RemoteModifier.padding(5.dp))
            }
        }
    }
}

/**
 * Displays a regular row on a dark gray background. Contains three text elements spaced apart:
 * "Item 1" in white, "Item 2" in yellow, and "Item 3" in gray.
 * WORKAROUND: Replaced RemoteCollapsibleRow with RemoteRow due to an "Invalid enum value: Orientation"
 * error when rendering the RemoteCollapsibleRow. It seems the RemoteCollapsibleRow's
 * orientation parameter was not being correctly handled by the renderer.
 */
@RemoteComposable
@Composable
fun RowSample2() {
    // WORKAROUND: Replaced RemoteCollapsibleRow with RemoteRow due to an "Invalid enum value: Orientation"
    // error when rendering the RemoteCollapsibleRow. It seems the RemoteCollapsibleRow's
    // orientation parameter was not being correctly handled by the renderer.
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.DarkGray),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteRow(
            modifier = RemoteModifier.fillMaxSize().padding(5.dp),
            horizontalArrangement = RemoteArrangement.SpaceBetween,
            verticalAlignment = RemoteAlignment.CenterVertically
        ) {
             RemoteText("Item 1", color = RemoteColor(Color.White))
             RemoteText("Item 2", color = RemoteColor(Color.Yellow))
             RemoteText("Item 3", color = RemoteColor(Color.Gray))
        }
    }
}

/**
 * Displays a collapsible column. Items with lower priority may be hidden if space is limited.
 */
@RemoteComposable
@Composable
fun CollapsibleColumnSample1() {
     RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteCollapsibleColumn(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.SpaceEvenly
        ) {
             RemoteText("Top (High)", color = RemoteColor(Color.Red), modifier = RemoteModifier.priority(1.0f))
             RemoteText("Middle (Low)", color = RemoteColor(Color.Green), modifier = RemoteModifier.priority(0.1f))
             RemoteText("Bottom (High)", color = RemoteColor(Color.Blue), modifier = RemoteModifier.priority(1.0f))
        }
    }
}
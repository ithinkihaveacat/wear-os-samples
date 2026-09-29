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
@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.remote.creation.compose.action.pendingIntentAction
import androidx.compose.remote.creation.compose.capture.RemoteImageVector
import androidx.compose.remote.creation.compose.capture.toRemoteImageVector
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.fillMaxWidth
import androidx.compose.remote.creation.compose.modifier.height
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.modifier.width
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.wear.AssociateWithGlanceWearWidget
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams
import androidx.glance.wear.tooling.preview.RectangularAllWidgetPreviewParams
import androidx.glance.wear.tooling.preview.RoundAllWidgetPreviewParams
import androidx.glance.wear.tooling.preview.SquircleAllWidgetPreviewParams
import androidx.glance.wear.tooling.preview.WearWidgetPreview
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * A "day progress" widget (header, summary line, full-width action button) that flexes to the
 * height the host gives it instead of assuming a fixed vertical budget.
 *
 * The same content has to fit a SMALL slot (about 38-44dp of usable height), a LARGE slot (about
 * 80-104dp), a partial-height slot in a multi-widget page, and a full-screen tile. A column of
 * fixed-height children (for example `RemoteButton`, which forces a 52dp minimum height) overflows
 * every one of those except the largest, so the action label gets clipped or collapses to an
 * ellipsis.
 *
 * Instead, [dynamicLayout] works out, from [WearWidgetParams], how much space there is and:
 * 1. drops whole lines of text, least important first (header, then summary), until what is left
 *    fits at its minimum size, then
 * 2. scales padding, gaps, icon and text sizes between their minimum and maximum to use the rest.
 *
 * No size ever goes below its minimum; if the host is shorter than even the button alone, that is
 * the one thing that shrinks further.
 */
// Suppressed file-level RestrictedApi because Remote Compose APIs are currently restricted to
// LIBRARY_GROUP.
@AssociateWithGlanceWearWidget(DynamicWidget::class)
class DynamicWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = DynamicWidget()
}

class DynamicWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        val colorScheme = RemoteColorScheme()
        val layout =
            dynamicLayout(
                usableWidthDp = params.widthDp - 2f * params.horizontalPaddingDp,
                usableHeightDp = params.heightDp - 2f * params.verticalPaddingDp,
                fontScale = context.resources.configuration.fontScale,
            )
        // A real colour here lets the host clip the background to its own shape and inset the
        // content by the padding in params, so the content below must not add its own.
        return WearWidgetDocument(
            background = WearWidgetBrush.color(colorScheme.surfaceContainer)
        ) {
            DynamicWidgetContent(layout, colorScheme)
        }
    }
}

/** Which lines of the design are shown, richest first. */
enum class DynamicTier {
    /** Icon + "Day progress" header, summary line, action button. */
    Full,
    /** Summary line and action button; the header is dropped. */
    Summary,
    /** Action button only, with the app icon moved inside it when there is room. */
    ButtonOnly,
}

/** Resolved sizes for one host slot. All sizes are already clamped to their allowed range. */
data class DynamicLayout(
    val tier: DynamicTier,
    val showIcon: Boolean,
    val iconDp: Float,
    val headerSp: Float,
    val headerToSummaryGapDp: Float,
    val summarySp: Float,
    val summaryToButtonGapDp: Float,
    val buttonHeightDp: Float,
    val buttonLabelSp: Float,
    val buttonHorizontalPaddingDp: Float,
)

/** A size that may flex between [min] and [max]. */
private class Flex(val min: Float, val max: Float) {
    fun at(t: Float): Float = min + (max - min) * t
}

// Minimums follow Wear OS Material 3: 12sp is the smallest text used for secondary labels, 14sp
// for primary content, and 32dp is the visible height of a compact button. Maximums reproduce the
// original fixed design, which only fits in the tallest slots.
private val IconSize = Flex(min = 16f, max = 24f)
private val HeaderText = Flex(min = 12f, max = 14f)
private val HeaderToSummaryGap = Flex(min = 2f, max = 6f)
private val SummaryText = Flex(min = 14f, max = 18f)
private val SummaryToButtonGap = Flex(min = 4f, max = 10f)
private val ButtonHeight = Flex(min = 32f, max = 52f)
private val ButtonLabelText = Flex(min = 14f, max = 16f)
private val ButtonHorizontalPadding = Flex(min = 8f, max = 16f)
private const val ICON_TO_TEXT_GAP_DP = 6f

/**
 * Line height as a multiple of font size. Roboto's ascent plus descent is about 1.17em; 1.25 leaves
 * a little slack so an estimate that is slightly off does not clip descenders.
 */
private const val LINE_HEIGHT_FACTOR = 1.25f

/**
 * Below this usable width the header icon is dropped: on a round LARGE slot (~92dp wide) "Day
 * progress" at 12sp plus an icon would otherwise be ellipsized.
 */
private const val MIN_WIDTH_FOR_ICON_DP = 120f

private fun lineHeightDp(sp: Float, fontScale: Float) = sp * fontScale * LINE_HEIGHT_FACTOR

/** Height of [tier] with every flexible size at position [t] (0 = minimum, 1 = maximum). */
private fun tierHeightDp(tier: DynamicTier, t: Float, fontScale: Float): Float {
    val button = ButtonHeight.at(t)
    val summary = lineHeightDp(SummaryText.at(t), fontScale) + SummaryToButtonGap.at(t)
    val header =
        maxOf(IconSize.at(t), lineHeightDp(HeaderText.at(t), fontScale)) + HeaderToSummaryGap.at(t)
    return when (tier) {
        DynamicTier.Full -> header + summary + button
        DynamicTier.Summary -> summary + button
        DynamicTier.ButtonOnly -> button
    }
}

/**
 * Chooses what to show and how big to draw it for a content area of [usableWidthDp] x
 * [usableHeightDp] (the widget size minus the host padding).
 *
 * [fontScale] is the user's font size setting. Text grows with it, so at larger settings the widget
 * drops lines sooner rather than clipping them.
 */
fun dynamicLayout(
    usableWidthDp: Float,
    usableHeightDp: Float,
    fontScale: Float = 1f,
): DynamicLayout {
    // Drop lines until the remaining ones fit at their minimum sizes.
    val tier =
        DynamicTier.entries.firstOrNull { tierHeightDp(it, 0f, fontScale) <= usableHeightDp }
            ?: DynamicTier.ButtonOnly

    // Then grow everything together to use the spare height. Height is linear in t, so the
    // position that exactly fills the slot is a straight interpolation.
    val minHeight = tierHeightDp(tier, 0f, fontScale)
    val maxHeight = tierHeightDp(tier, 1f, fontScale)
    val t = ((usableHeightDp - minHeight) / (maxHeight - minHeight)).coerceIn(0f, 1f)

    // The button is the one element allowed below its minimum, and only when the host is shorter
    // than the button alone; everything else has already been removed by then.
    val buttonHeight =
        if (tier == DynamicTier.ButtonOnly) {
            ButtonHeight.at(t).coerceAtMost(usableHeightDp)
        } else {
            ButtonHeight.at(t)
        }

    return DynamicLayout(
        tier = tier,
        showIcon = usableWidthDp >= MIN_WIDTH_FOR_ICON_DP,
        iconDp = IconSize.at(t),
        headerSp = HeaderText.at(t),
        headerToSummaryGapDp = HeaderToSummaryGap.at(t),
        summarySp = SummaryText.at(t),
        summaryToButtonGapDp = SummaryToButtonGap.at(t),
        buttonHeightDp = buttonHeight,
        buttonLabelSp = ButtonLabelText.at(t),
        buttonHorizontalPaddingDp = ButtonHorizontalPadding.at(t),
    )
}

@RemoteComposable
@Composable
fun DynamicWidgetContent(layout: DynamicLayout, colorScheme: RemoteColorScheme) {
    RemoteMaterialTheme(colorScheme = colorScheme) {
        // Centre vertically: spare space (when every size is already at its maximum) is split
        // evenly above and below rather than left at the bottom.
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            if (layout.tier == DynamicTier.Full) {
                Header(layout)
                Spacer(RemoteModifier.height(layout.headerToSummaryGapDp.rdp))
            }
            if (layout.tier != DynamicTier.ButtonOnly) {
                RemoteText(
                    text = stringResource(R.string.dynamic_widget_summary).rs,
                    color = RemoteMaterialTheme.colorScheme.onSurface,
                    fontSize = layout.summarySp.sp.asRemoteTextUnit(),
                    // No textAlign: the column already centres this single line, and
                    // TextAlign.Center on a wrap-content text shifted and cropped the glyphs on
                    // the ProtoLayout renderer 1.6.6 player.
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(RemoteModifier.height(layout.summaryToButtonGapDp.rdp))
            }
            ActionButton(layout)
        }
    }
}

@RemoteComposable
@Composable
private fun Header(layout: DynamicLayout) {
    RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
        if (layout.showIcon) {
            AppIcon(
                sizeDp = layout.iconDp,
                background = RemoteMaterialTheme.colorScheme.primary,
                tint = RemoteMaterialTheme.colorScheme.onPrimary,
            )
            Spacer(RemoteModifier.width(ICON_TO_TEXT_GAP_DP.rdp))
        }
        RemoteText(
            text = stringResource(R.string.dynamic_widget_header).rs,
            color = RemoteMaterialTheme.colorScheme.onSurface,
            fontSize = layout.headerSp.sp.asRemoteTextUnit(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * A pill-shaped button whose height comes from [DynamicLayout.buttonHeightDp].
 *
 * `RemoteButton` is deliberately not used: it applies `heightIn(min = 52.dp)`, which is taller than
 * the entire content area of a SMALL slot and is what pushes the label out of view.
 */
@RemoteComposable
@Composable
private fun ActionButton(layout: DynamicLayout) {
    val height = layout.buttonHeightDp.rdp
    val openApp = pendingIntentAction { context ->
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, HelloActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE,
        )
    }
    RemoteRow(
        modifier =
            RemoteModifier.fillMaxWidth()
                .height(height)
                .clip(RemoteRoundedCornerShape(height / 2))
                .background(RemoteMaterialTheme.colorScheme.primary)
                .clickable(openApp)
                .padding(horizontal = layout.buttonHorizontalPaddingDp.rdp),
        horizontalArrangement = RemoteArrangement.Center,
        verticalAlignment = RemoteAlignment.CenterVertically,
    ) {
        // With the header gone, the button carries the app icon so the widget stays recognisable.
        if (layout.tier == DynamicTier.ButtonOnly && layout.showIcon) {
            // Inverted so the icon stands out against the primary-coloured pill.
            AppIcon(
                sizeDp = layout.iconDp,
                background = RemoteMaterialTheme.colorScheme.onPrimary,
                tint = RemoteMaterialTheme.colorScheme.primary,
            )
            Spacer(RemoteModifier.width(ICON_TO_TEXT_GAP_DP.rdp))
        }
        RemoteText(
            text = stringResource(R.string.dynamic_widget_action).rs,
            color = RemoteMaterialTheme.colorScheme.onPrimary,
            fontSize = layout.buttonLabelSp.sp.asRemoteTextUnit(),
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@RemoteComposable
@Composable
private fun AppIcon(sizeDp: Float, background: RemoteColor, tint: RemoteColor) {
    val size = sizeDp.rdp
    RemoteBox(
        modifier =
            RemoteModifier.size(size)
                .clip(RemoteRoundedCornerShape(size / 2))
                .background(background),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteIcon(
            imageVector = CheckIcon,
            contentDescription = null,
            modifier = RemoteModifier.size((sizeDp * 0.65f).rdp),
            tint = tint,
        )
    }
}

/** Material "check" glyph, built inline so the sample needs no icon library. */
private val CheckIcon: RemoteImageVector =
    ImageVector.Builder(
            name = "Check",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        )
        .apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(9f, 16.17f)
                lineTo(4.83f, 12f)
                lineToRelative(-1.42f, 1.41f)
                lineTo(9f, 19f)
                lineTo(21f, 7f)
                lineToRelative(-1.41f, -1.41f)
                close()
            }
        }
        .build()
        .toRemoteImageVector()

/** Fixed-size gap; Remote Compose has no Spacer, and an empty box sized by its modifier is one. */
@RemoteComposable
@Composable
private fun Spacer(modifier: RemoteModifier) = RemoteBox(modifier = modifier)

@Preview(name = "Squircle Preview", device = "spec:width=1000dp,height=1000dp,dpi=320")
@Composable
fun DynamicWidgetSquirclePreview(
    @PreviewParameter(SquircleAllWidgetPreviewParams::class) params: WearWidgetParams
) = WearWidgetPreview(DynamicWidget(), params)

@Preview(name = "Round Preview", device = "spec:width=1000dp,height=1000dp,dpi=320")
@Composable
fun DynamicWidgetRoundPreview(
    @PreviewParameter(RoundAllWidgetPreviewParams::class) params: WearWidgetParams
) = WearWidgetPreview(DynamicWidget(), params)

// Generates the uncropped rectangular preview images referenced by
// <container previewImage="@drawable/..." /> in res/xml/dynamic_widget_info.xml.
@Preview(name = "Widget Picker Preview", device = "spec:width=1000dp,height=1000dp,dpi=320")
@Composable
fun DynamicWidgetRectangularPreview(
    @PreviewParameter(RectangularAllWidgetPreviewParams::class) params: WearWidgetParams
) = WearWidgetPreview(DynamicWidget(), params)

package com.example.wear.tiles.counter

import android.content.Context
import androidx.annotation.OptIn
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.FONT_WEIGHT_BOLD
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders.State
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.expression.dynamicDataMapOf
import androidx.wear.protolayout.expression.floatAppDataKey
import androidx.wear.protolayout.expression.mapTo
import androidx.wear.protolayout.expression.stringAppDataKey
import androidx.wear.protolayout.material3.ButtonDefaults.filledButtonColors
import androidx.wear.protolayout.material3.ButtonDefaults.filledTonalButtonColors
import androidx.wear.protolayout.material3.ButtonGroupDefaults.DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS
import androidx.wear.protolayout.material3.CircularProgressIndicatorDefaults.filledProgressIndicatorColors
import androidx.wear.protolayout.material3.CircularProgressIndicatorDefaults.filledTonalProgressIndicatorColors
import androidx.wear.protolayout.material3.CircularProgressIndicatorDefaults.recommendedAnimationSpec
import androidx.wear.protolayout.material3.GraphicDataCardDefaults.constructGraphic
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.PrimaryLayoutMargins
import androidx.wear.protolayout.material3.Typography.TITLE_LARGE
import androidx.wear.protolayout.material3.graphicDataCard
import androidx.wear.protolayout.material3.icon
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.segmentedCircularProgressIndicator
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.types.LayoutColor
import androidx.wear.protolayout.types.LayoutString
import androidx.wear.protolayout.types.asLayoutString
import androidx.wear.protolayout.types.layoutString
import androidx.wear.protolayout.types.stringLayoutConstraint
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.column
import com.example.wear.tiles.tools.row
import com.google.android.horologist.tiles.SuspendingTileService
import kotlinx.coroutines.runBlocking

private const val RESOURCES_VERSION = "0"
private const val ADD_ONE_ID = "add_one_id"
private const val SUB_ONE_ID = "sub_one_id"
private const val PROGRESS_ID = "progress_id"
private const val COUNT_ID = "count_id"
private const val TARGET_CUPS = 5
private const val ICON_WATER = "waterIcon"
private const val ICON_PLUS = "plusIcon"

class CounterService : SuspendingTileService() {

    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest) =
        resources(requestParams)

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile {
        return tile(
            requestParams = requestParams,
            context = this,
            onInteractive = { runBlocking { getCounterState() } },
            onStateChange = { counterState -> runBlocking { setCounterState(counterState) } },
        )
    }
}

private fun resources(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
    return ResourceBuilders.Resources.Builder()
        .setVersion(requestParams.version)
        .addIdToImageMapping(ICON_WATER, R.drawable.outline_glass_cup_24)
        .build()
}

private fun tile(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
    onInteractive: () -> CounterState,
    onStateChange: (CounterState) -> Unit,
): TileBuilders.Tile {
    val operation = requestParams.currentState.lastClickableId

    val prevCount =
        requestParams.currentState.keyToValueMapping[AppDataKey<DynamicString>(COUNT_ID)]
            ?.stringValue
            ?.toIntOrNull() ?: onInteractive().count
    val nextCount =
        when (operation) {
            ADD_ONE_ID -> prevCount + 1
            SUB_ONE_ID -> prevCount - 1
            else -> prevCount
        }

    val displayCount = nextCount.toString()
    val displayProgress = nextCount.toFloat() / TARGET_CUPS

    // Persist change in state
    if (prevCount != nextCount) {
        onStateChange(CounterState(nextCount))
    }

    val newState =
        State.Builder()
            .setStateMap(
                dynamicDataMapOf(
                    stringAppDataKey(COUNT_ID) mapTo displayCount,
                    floatAppDataKey(PROGRESS_ID) mapTo displayProgress,
                )
            )
            .build()

    return TileBuilders.Tile.Builder()
        //        .setResourcesVersion(UUID.randomUUID().toString())
        .setResourcesVersion(RESOURCES_VERSION)
        .setState(newState)
        .setTileTimeline(Timeline.fromLayoutElement(tileLayout(requestParams, context, newState)))
        .setFreshnessIntervalMillis(1000)
        .build()
}

private fun tileLayout(requestParams: RequestBuilders.TileRequest, context: Context, state: State) =
    materialScope(context, requestParams.deviceConfiguration) {
        val currentCups = DynamicBuilders.DynamicFloat.from(AppDataKey(PROGRESS_ID))
        val progress = currentCups.animate(recommendedAnimationSpec)
//            DynamicBuilders.DynamicFloat.animate(
////                max(0F, currentCups - 1F) / TARGET_CUPS,
//                0F,
//                currentCups,
//                recommendedAnimationSpec,
//            )
        primaryLayout(
            margins = PrimaryLayoutMargins.MIN_PRIMARY_LAYOUT_MARGIN,
            titleSlot = {
                text(
                    text = "Water".layoutString,
                    typography = TITLE_LARGE,
                    color = colorScheme.primary,
                )
            },
            mainSlot = {
                column {
                    setWidth(expand())
                    addContent(
                        graphicDataCard(
                            onClick = clickable(),
                            height = expand(),
                            content = { text("Cup(s)".layoutString) }, // TODO Conditionalize based on currentCups
                            graphic = {
                                constructGraphic(
                                    mainContent = {
                                        segmentedCircularProgressIndicator(
//                                            startAngleDegrees = 200F,
//                                            endAngleDegrees = 520F,
                                            colors = filledProgressIndicatorColors(),
                                            segmentCount = TARGET_CUPS,
                                            dynamicProgress = progress,
                                            staticProgress = 0F
                                        )
                                    },
                                    iconContent = { icon(ICON_WATER) },
                                )
                            },
                            title = { text(dynamicCount()) }
                        )
                    )
                    addContent(DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS)
                    addContent(
                        row {
                            addContent(
                                counterButton(
                                    onClick = buildClickable(SUB_ONE_ID, state),
                                    labelContent = { text("−".layoutString) },
                                )
                            )
                            addContent(
                                Spacer.Builder().setWidth(dp(5F)).setHeight(expand()).build()
                            )
//                            addContent(dynamicText(COUNT_ID))
//                            addContent(
//                                Spacer.Builder().setWidth(dp(5F)).setHeight(expand()).build()
//                            )
                            addContent(
                                counterButton(
                                    onClick = buildClickable(ADD_ONE_ID, state),
                                    labelContent = { text("+".layoutString) },
                                )
                            )
                        }
                    )
//                    addContent(DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS)
//                    addContent(
//                        circularProgressIndicator(
//                            dynamicProgress =
//                                DynamicBuilders.DynamicFloat.from(AppDataKey(PROGRESS_ID))
//                                    .animate(),
//                            colors = filledTonalProgressIndicatorColors(),
//                        )
//                    )
                }
            }
        )
    }

/*

private fun tileLayoutM25(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
    state: State,
) =
    EdgeContentLayout.Builder(requestParams.deviceConfiguration)
        .setResponsiveContentInsetEnabled(true)
        .setEdgeContent(
            CircularProgressIndicator.Builder()
                .setProgress(
                    TypeBuilders.FloatProp.Builder(0.0F)
                        .setValue(0.0F)
                        .setDynamicValue(
                            DynamicBuilders.DynamicFloat.from(AppDataKey(PROGRESS_ID)).animate()
                        )
                        .build()
                )
                .setCircularProgressIndicatorColors(
                    ProgressIndicatorColors(GoldenTilesColors.Pink, GoldenTilesColors.DarkerGray)
                )
                .build()
        )
        .setContent(
            MultiSlotLayout.Builder()
                .addSlotContent(buildButton(context, buildClickable(SUB_ONE_ID, state), "-1"))
                .addSlotContent(dynamicText((COUNT_ID)))
                .addSlotContent(buildButton(context, buildClickable(ADD_ONE_ID, state), "+1"))
                .build()
        )
        .setPrimaryLabelTextContent(
            Text.Builder(context, "Cups")
                .setColor(ColorBuilders.argb(GoldenTilesColors.Yellow))
                .build()
        )
        .build()

fun buildButton(context: Context, clickable: Clickable, label: String) =
    Button.Builder(context, clickable)
        .setTextContent(label, Typography.TYPOGRAPHY_TITLE3)
        .setSize(dp(40F))
        .setButtonColors(
            ButtonColors(
                ColorBuilders.argb(GoldenTilesColors.LightPurple),
                ColorBuilders.argb(GoldenTilesColors.DarkerGray),
            )
        )
        .build()

 */

private fun MaterialScope.counterButton(
    onClick: Clickable,
    labelContent: (MaterialScope.() -> LayoutElement),
) =
    textButton(
        onClick = onClick,
        shape = shapes.medium,
        height = dp(45F),
        colors =
            filledButtonColors()
                .copy(labelColor = colorScheme.onTertiary, containerColor = colorScheme.tertiary),
        labelContent = labelContent,
    )

@Preview(device = WearDevices.SMALL_ROUND)
@Preview(device = WearDevices.LARGE_ROUND)
fun tilePreview(context: Context) =
    TilePreviewData(::resources) {
        tile(it, context, onInteractive = { CounterState(8) }, onStateChange = {})
    }

@OptIn(ProtoLayoutExperimental::class)
fun MaterialScope.dynamicText(key: String): LayoutElementBuilders.Text {
    return LayoutElementBuilders.Text.Builder()
        .setText(
            TypeBuilders.StringProp.Builder("No Data")
                .setValue("—")
                .setDynamicValue(DynamicString.from(AppDataKey(key)))
                .build()
        )
        .setLayoutConstraintsForDynamicText(
            StringLayoutConstraint.Builder("00")
                .setAlignment(LayoutElementBuilders.TEXT_ALIGN_CENTER)
                .build()
        )
        .setFontStyle(
            // TODO: extract size, weight axes from e.g. Typography.DISPLAY_LARGE
            LayoutElementBuilders.FontStyle.Builder()
                .setSize(sp(40F))
                .setWeight(FONT_WEIGHT_BOLD)
                .setColor(colorScheme.primary.toColorProp())
                .build()
        )
        .build()
}

fun MaterialScope.dynamicCount(): LayoutString {
    val dynamicValue = DynamicString.from(AppDataKey<DynamicString>(COUNT_ID))

    val constraint = stringLayoutConstraint(
        longestPattern = "00",
        alignment = LayoutElementBuilders.TEXT_ALIGN_START
    )

    return dynamicValue.asLayoutString(
        staticValue = "—",
        layoutConstraint = constraint
    )
}

fun LayoutColor.toColorProp(): ColorBuilders.ColorProp {
    val builder = ColorBuilders.ColorProp.Builder(this.staticArgb)
    this.dynamicArgb?.let { dynamicColor -> builder.setDynamicValue(dynamicColor) }
    return builder.build()
}

fun Clickable.Builder.setIdAndState(id: String, state: State) = apply {
    setId(id)
    setOnClick(ActionBuilders.LoadAction.Builder().setRequestState(state).build())
}

fun buildClickable(id: String, state: State): Clickable {
    return Clickable.Builder().setIdAndState(id, state).build()
}

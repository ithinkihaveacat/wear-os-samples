package com.example.wear.tiles.counter

import android.content.Context
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
import androidx.wear.protolayout.material3.ButtonGroupDefaults.DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS
import androidx.wear.protolayout.material3.CircularProgressIndicatorDefaults.filledTonalProgressIndicatorColors
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.Typography.DISPLAY_LARGE
import androidx.wear.protolayout.material3.circularProgressIndicator
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.wear.tiles.golden.GoldenTilesColors
import com.example.wear.tiles.tools.column
import com.example.wear.tiles.tools.row
import com.google.android.horologist.tiles.SuspendingTileService
import kotlinx.coroutines.runBlocking

private const val RESOURCES_VERSION = "0"
private const val ADD_ONE_ID = "add_one_id"
private const val SUB_ONE_ID = "sub_one_id"
private const val PROGRESS_ID = "progress_id"
private const val COUNT_ID = "count_id"

class CounterTileService : SuspendingTileService() {

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
    return ResourceBuilders.Resources.Builder().setVersion(requestParams.version).build()
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
    val displayProgress = nextCount * 10 / 100F

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
        primaryLayout(
            titleSlot = {
                text(
                    text = "Cups".layoutString,
                    typography = DISPLAY_LARGE,
                    color = colorScheme.primary,
                )
            },
            mainSlot = {
                column {
                    addContent(
                        row {
                            addContent(
                                counterButton(
                                    onClick = buildClickable(SUB_ONE_ID, state),
                                    labelContent = { text("−1".layoutString) },
                                )
                            )
                            addContent(
                                Spacer.Builder().setWidth(dp(5F)).setHeight(expand()).build()
                            )
                            addContent(dynamicText(COUNT_ID))
                            addContent(
                                Spacer.Builder().setWidth(dp(5F)).setHeight(expand()).build()
                            )
                            addContent(
                                counterButton(
                                    onClick = buildClickable(ADD_ONE_ID, state),
                                    labelContent = { text("+1".layoutString) },
                                )
                            )
                        }
                    )
                    addContent(DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS)
                    addContent(
                        circularProgressIndicator(
                            dynamicProgress =
                                DynamicBuilders.DynamicFloat.from(AppDataKey(PROGRESS_ID))
                                    .animate(),
                            colors = filledTonalProgressIndicatorColors(),
                        )
                    )
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

@androidx.annotation.OptIn(ProtoLayoutExperimental::class)
fun dynamicText(key: String): LayoutElementBuilders.Text {
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
            LayoutElementBuilders.FontStyle.Builder()
                .setSize(sp(40F))
                .setWeight(FONT_WEIGHT_BOLD)
                .setColor(ColorBuilders.argb(GoldenTilesColors.Yellow))
                .build()
        )
        .build()
}

fun Clickable.Builder.setIdAndState(id: String, state: State) = apply {
    setId(id)
    setOnClick(ActionBuilders.LoadAction.Builder().setRequestState(state).build())
}

fun buildClickable(id: String, state: State): Clickable {
    return Clickable.Builder().setIdAndState(id, state).build()
}

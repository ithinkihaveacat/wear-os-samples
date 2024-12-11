package com.example.wear.tiles.counter

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.FONT_WEIGHT_BOLD
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders.State
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.DynamicDataBuilders
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ButtonColors
import androidx.wear.protolayout.material.CircularProgressIndicator
import androidx.wear.protolayout.material.ProgressIndicatorColors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.EdgeContentLayout
import androidx.wear.protolayout.material.layouts.MultiSlotLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.wear.tiles.golden.GoldenTilesColors
import com.google.android.horologist.tiles.SuspendingTileService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private const val RESOURCES_VERSION = "0"

private val Context.counterDataStore: DataStore<Preferences> by
    preferencesDataStore(name = "counter")

suspend fun Context.getCounterState(): CounterState {
    return CounterState(
        counterDataStore.data
            .map { preferences -> preferences[intPreferencesKey(COUNT_ID)] ?: 0 }
            .first()
    )
}

suspend fun Context.setCounterState(state: CounterState) {
    counterDataStore.edit { preferences -> preferences[intPreferencesKey(COUNT_ID)] = state.count }
}

data class CounterState(var count: Int)

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

    if (prevCount != nextCount) {
        onStateChange(CounterState(nextCount))
    }

    val newState =
        State.Builder()
            .mergeMap(mapOf(COUNT_ID to displayCount, PROGRESS_ID to displayProgress))
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
    EdgeContentLayout.Builder(requestParams.deviceConfiguration)
        .setResponsiveContentInsetEnabled(true)
        .setEdgeContent(
            CircularProgressIndicator.Builder()
                .setProgress(
                    TypeBuilders.FloatProp.Builder(0.0F)
                        .setValue(0.0F)
                        .setDynamicValue(DynamicBuilders.DynamicFloat.from(AppDataKey(PROGRESS_ID)))
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

@Preview(device = WearDevices.SMALL_ROUND)
@Preview(device = WearDevices.LARGE_ROUND)
fun tilePreview(context: Context) =
    TilePreviewData(::resources) {
        tile(it, context, onInteractive = { CounterState(8) }, onStateChange = {})
    }

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

fun State.Builder.mergeMap(map: Map<String, Any>) = apply {
    map.forEach { (key, value) ->
        when (value) {
            is Int ->
                addKeyToValueMapping(
                    AppDataKey(key),
                    DynamicDataBuilders.DynamicDataValue.fromInt(value),
                )

            is Float ->
                addKeyToValueMapping(
                    AppDataKey(key),
                    DynamicDataBuilders.DynamicDataValue.fromFloat(value),
                )

            is String ->
                addKeyToValueMapping(
                    AppDataKey(key),
                    DynamicDataBuilders.DynamicDataValue.fromString(value),
                )

            is java.time.Instant ->
                addKeyToValueMapping(
                    AppDataKey(key),
                    DynamicDataBuilders.DynamicDataValue.fromInstant(value),
                )

            is java.time.Duration ->
                addKeyToValueMapping(
                    AppDataKey(key),
                    DynamicDataBuilders.DynamicDataValue.fromDuration(value),
                )

            is ByteArray ->
                addKeyToValueMapping(
                    AppDataKey(key),
                    DynamicDataBuilders.DynamicDataValue.fromByteArray(value),
                )

            else -> throw IllegalArgumentException("Unsupported value type: ${value::class}")
        }
    }
}

fun Clickable.Builder.setIdAndState(id: String, state: State) = apply {
    setId(id)
    setOnClick(ActionBuilders.LoadAction.Builder().setRequestState(state).build())
}

fun buildClickable(id: String, state: State): Clickable {
    return Clickable.Builder().setIdAndState(id, state).build()
}

const val ADD_ONE_ID = "add_one_id"
const val SUB_ONE_ID = "sub_one_id"
const val COUNT_ID = "count_id"
const val PROGRESS_ID = "progress_id"

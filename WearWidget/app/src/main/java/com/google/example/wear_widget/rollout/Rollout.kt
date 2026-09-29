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

package com.google.example.wear_widget.rollout

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rsp
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.glance.wear.AssociateWithGlanceWearWidget
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService
import androidx.glance.wear.WearWidgetBrush
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.color
import androidx.glance.wear.core.WearWidgetParams
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.ListenableFuture

/*
 * Test harness for gradually rolling out a Widget by enabling its service at runtime.
 *
 * Two independent experiments, each with its own legacy ProtoLayout Tile so they can be tested
 * side by side without interfering:
 *
 * 1. Dual-Service: DualLegacyTileService (BIND_TILE_PROVIDER only, always enabled) +
 *    DualWidgetService (BIND_WIDGET_PROVIDER only, shipped disabled, group = legacy FQCN).
 *
 * 2. Single-Service: SingleLegacyTileService (BIND_TILE_PROVIDER only, always enabled) +
 *    SingleTempWidgetService (both actions, priority 10, shipped disabled, group = legacy FQCN).
 *
 * Each surface renders a distinct label so it is obvious which component the system bound to.
 * Toggle experiments at runtime with RolloutReceiver (see README-rollout.md).
 */

private const val TAG = "WidgetRollout"

// ---------------------------------------------------------------------------------------------
// Legacy ProtoLayout Tiles (the "existing" Tile a partner already ships)
// ---------------------------------------------------------------------------------------------

abstract class LegacyTileService : TileService() {
    abstract val label: String
    abstract val color: Int

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<TileBuilders.Tile> {
        Log.d(TAG, "${javaClass.simpleName}.onTileRequest tileId=${requestParams.tileId}")
        val layout =
            LayoutElementBuilders.Box.Builder()
                .setWidth(expand())
                .setHeight(expand())
                .addContent(
                    LayoutElementBuilders.Text.Builder()
                        .setText(label)
                        .setMaxLines(3)
                        .setFontStyle(
                            LayoutElementBuilders.FontStyle.Builder()
                                .setColor(argb(color))
                                .setSize(sp(18f))
                                .build()
                        )
                        .build()
                )
                .build()
        val tile =
            TileBuilders.Tile.Builder()
                .setResourcesVersion("1")
                .setTileTimeline(TimelineBuilders.Timeline.fromLayoutElement(layout))
                .build()
        return CallbackToFutureAdapter.getFuture { it.set(tile) }
    }
}

class DualLegacyTileService : LegacyTileService() {
    override val label = "Dual: LEGACY TILE"
    override val color = 0xFFFFB74D.toInt() // orange
}

class SingleLegacyTileService : LegacyTileService() {
    override val label = "Single: LEGACY TILE"
    override val color = 0xFFFFB74D.toInt() // orange
}

// ---------------------------------------------------------------------------------------------
// New widgets (the surface being rolled out)
// ---------------------------------------------------------------------------------------------

class LabelWidget(private val label: String) : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        Log.d(TAG, "LabelWidget($label).provideWidgetData params=$params")
        val scheme = RemoteColorScheme()
        return WearWidgetDocument(background = WearWidgetBrush.color(scheme.primary)) {
            RemoteMaterialTheme {
                RemoteBox(
                    modifier = RemoteModifier.fillMaxSize(),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteText(
                        text = label.rs,
                        color = RemoteMaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.rsp,
                    )
                }
            }
        }
    }
}

class DualWidget : GlanceWearWidget() {
    private val delegate = LabelWidget("Dual: NEW WIDGET")

    override suspend fun provideWidgetData(context: Context, params: WearWidgetParams) =
        delegate.provideWidgetData(context, params)
}

class SingleTempWidget : GlanceWearWidget() {
    private val delegate = LabelWidget("Single: TEMP WIDGET")

    override suspend fun provideWidgetData(context: Context, params: WearWidgetParams) =
        delegate.provideWidgetData(context, params)
}

/** Dual-Service experiment: handles BIND_WIDGET_PROVIDER only. Shipped disabled. */
@AssociateWithGlanceWearWidget(DualWidget::class)
class DualWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = DualWidget()
}

/**
 * Single-Service experiment: handles BIND_TILE_PROVIDER and BIND_WIDGET_PROVIDER with higher
 * intent-filter priority than the legacy Tile. Shipped disabled; must only be enabled on SDK 37+.
 */
@AssociateWithGlanceWearWidget(SingleTempWidget::class)
class SingleTempWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = SingleTempWidget()
}

// ---------------------------------------------------------------------------------------------
// Runtime enablement (stands in for a remote-config / experiment enrollment callback)
// ---------------------------------------------------------------------------------------------

object RolloutController {
    const val WEAR_OS_7_SDK = 37

    enum class Experiment(val service: Class<*>, val requiresWear7: Boolean) {
        DUAL(DualWidgetService::class.java, requiresWear7 = false),
        SINGLE(SingleTempWidgetService::class.java, requiresWear7 = true),
    }

    /**
     * Applies [state] (one of PackageManager.COMPONENT_ENABLED_STATE_*) to the experiment's
     * service. Returns false if refused by the SDK guard (pass [force] to bypass it, e.g. to
     * reproduce the duplicate-tile failure mode on pre-Wear OS 7 devices).
     */
    fun apply(context: Context, experiment: Experiment, state: Int, force: Boolean = false): Boolean {
        if (
            state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED &&
                experiment.requiresWear7 &&
                Build.VERSION.SDK_INT < WEAR_OS_7_SDK &&
                !force
        ) {
            Log.w(TAG, "Refusing to enable $experiment on SDK ${Build.VERSION.SDK_INT} (< 37)")
            return false
        }
        val component = ComponentName(context, experiment.service)
        context.packageManager.setComponentEnabledSetting(
            component,
            state,
            PackageManager.DONT_KILL_APP,
        )
        Log.i(TAG, "Set $experiment (${component.flattenToShortString()}) -> ${stateName(state)}")
        return true
    }

    fun dumpState(context: Context) {
        val pm = context.packageManager
        val services =
            listOf(DualLegacyTileService::class.java, SingleLegacyTileService::class.java) +
                Experiment.entries.map { it.service }
        Log.i(TAG, "SDK_INT=${Build.VERSION.SDK_INT} model=${Build.MODEL}")
        for (cls in services) {
            val component = ComponentName(context, cls)
            val setting = pm.getComponentEnabledSetting(component)
            Log.i(TAG, "  ${cls.simpleName}: setting=${stateName(setting)}")
        }
    }

    fun stateName(state: Int) =
        when (state) {
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT -> "DEFAULT"
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> "ENABLED"
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED -> "DISABLED"
            else -> "UNKNOWN($state)"
        }
}

/**
 * Debug entry point. Examples:
 * ```
 * adb shell am broadcast -a com.google.example.wear_widget.ROLLOUT \
 *   -n com.google.example.wear_widget/.rollout.RolloutReceiver \
 *   --es experiment dual --es state enabled
 * ```
 *
 * Extras: experiment = dual|single (omit to only dump state); state = enabled|disabled|default;
 * force = true to bypass the SDK 37 guard for the single-service experiment.
 */
class RolloutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val experiment =
            intent.getStringExtra("experiment")?.let {
                RolloutController.Experiment.valueOf(it.uppercase())
            }
        val state =
            when (intent.getStringExtra("state")?.lowercase()) {
                "enabled" -> PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                "disabled" -> PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                "default" -> PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                else -> null
            }
        if (experiment != null && state != null) {
            val ok =
                RolloutController.apply(
                    context,
                    experiment,
                    state,
                    force = intent.getBooleanExtra("force", false),
                )
            resultCode = if (ok) 1 else 0
        }
        RolloutController.dumpState(context)
    }
}

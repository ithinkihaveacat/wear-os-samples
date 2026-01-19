package com.google.example.wear_widget

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComponentCatalogReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.google.example.wear_widget.SET_COMPONENT") {
            val layoutName = intent.getStringExtra("layout")
            if (layoutName != null) {
                Log.d("ComponentCatalogRcvr", "Received request to set layout: $layoutName")
                val goAsync = goAsync()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    try {
                        context.setComponentCatalogState(ComponentCatalogState(layoutName))
                        Log.d("ComponentCatalogRcvr", "State saved. Triggering update...")

                        // Note: triggerUpdate() causes DataStore conflict crash
                        // Instead, caller should use DEBUG_SYSUI show-tile to refresh
                        Log.d("ComponentCatalogRcvr", "State saved. Use 'adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SYSUI --es operation show-tile --ei index N' to refresh.")
                    } catch (e: Exception) {
                        Log.e("ComponentCatalogRcvr", "Error setting layout", e)
                    } finally {
                        goAsync.finish()
                    }
                }
            }
        }
    }
}

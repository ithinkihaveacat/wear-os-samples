package com.google.example.wear_widget

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WidgetCatalogReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.google.example.wear_widget.SET_WIDGET") {
            val layoutName = intent.getStringExtra("widget")
            if (layoutName != null) {
                Log.d("WidgetCatalogRcvr", "Received request to set widget: $layoutName")
                val goAsync = goAsync()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    try {
                        context.setWidgetCatalogState(WidgetCatalogState(layoutName))
                        Log.d("WidgetCatalogRcvr", "State saved. Triggering update...")

                        val componentName = ComponentName(context, WidgetCatalogService::class.java)
                        WidgetCatalog().triggerUpdate(context.applicationContext, componentName)

                        Log.d("WidgetCatalogRcvr", "Update triggered.")
                    } catch (e: Exception) {
                        Log.e("WidgetCatalogRcvr", "Error setting widget", e)
                    } finally {
                        goAsync.finish()
                    }
                }
            }
        }
    }
}

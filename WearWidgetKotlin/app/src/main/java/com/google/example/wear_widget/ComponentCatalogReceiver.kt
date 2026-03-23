package com.google.example.wear_widget

import androidx.compose.remote.creation.compose.state.rs
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

                        val componentName =
                            ComponentName(context, ComponentCatalogService::class.java)
                        ComponentCatalog().triggerUpdate(context.applicationContext, componentName)

                        Log.d("ComponentCatalogRcvr", "Update triggered.")
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

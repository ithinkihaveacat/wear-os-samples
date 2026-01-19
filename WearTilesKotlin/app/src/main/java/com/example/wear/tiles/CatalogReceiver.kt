package com.example.wear.tiles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.wear.tiles.TileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatalogReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.wear.tiles.SET_LAYOUT") {
            val layoutName = intent.getStringExtra("layout")
            if (layoutName != null) {
                Log.d("CatalogReceiver", "Received request to set layout: $layoutName")
                val appContext = context.applicationContext
                val goAsync = goAsync()
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    try {
                        context.setCatalogState(CatalogState(layoutName))
                        Log.d("CatalogReceiver", "State saved. Triggering update...")
                        
                        TileService.getUpdater(appContext)
                            .requestUpdate(CatalogService::class.java)
                        
                        Log.d("CatalogReceiver", "Update triggered successfully.")
                    } catch (e: Exception) {
                        Log.e("CatalogReceiver", "Error setting layout", e)
                    } finally {
                        goAsync.finish()
                    }
                }
            }
        }
    }
}

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

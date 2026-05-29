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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val LAYOUT_NAME_KEY = "layout_name"
val Context.widgetCatalogDataStore: DataStore<Preferences> by
    preferencesDataStore(name = "widget_catalog_preferences")

data class WidgetCatalogState(val layoutName: String)

suspend fun Context.getWidgetCatalogState(): WidgetCatalogState {
    return WidgetCatalogState(
        widgetCatalogDataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] ?: "SemanticStyleSample"
            }
            .first()
    )
}

suspend fun Context.setWidgetCatalogState(state: WidgetCatalogState) {
    widgetCatalogDataStore.edit { preferences ->
        preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] = state.layoutName
    }
}

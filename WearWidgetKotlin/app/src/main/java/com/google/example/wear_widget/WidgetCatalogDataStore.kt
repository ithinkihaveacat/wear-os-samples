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
val Context.widgetCatalogDataStore: DataStore<Preferences> by preferencesDataStore(name = "widget_catalog_preferences")

data class WidgetCatalogState(val layoutName: String)

suspend fun Context.getWidgetCatalogState(): WidgetCatalogState {
    return WidgetCatalogState(
        widgetCatalogDataStore.data
            .map { preferences -> preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] ?: "SemanticStyleWorkaroundSample" }
            .first()
    )
}

suspend fun Context.setWidgetCatalogState(state: WidgetCatalogState) {
    widgetCatalogDataStore.edit { preferences ->
        preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] = state.layoutName
    }
}

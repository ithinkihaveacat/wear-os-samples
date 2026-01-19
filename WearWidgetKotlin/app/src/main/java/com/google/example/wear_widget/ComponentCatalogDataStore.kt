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
val Context.componentCatalogDataStore: DataStore<Preferences> by preferencesDataStore(name = "component_catalog_preferences")

data class ComponentCatalogState(val layoutName: String)

suspend fun Context.getComponentCatalogState(): ComponentCatalogState {
    return ComponentCatalogState(
        componentCatalogDataStore.data
            .map { preferences -> preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] ?: "button" }
            .first()
    )
}

suspend fun Context.setComponentCatalogState(state: ComponentCatalogState) {
    componentCatalogDataStore.edit { preferences ->
        preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] = state.layoutName
    }
}

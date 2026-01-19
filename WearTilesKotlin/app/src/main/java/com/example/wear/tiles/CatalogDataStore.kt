package com.example.wear.tiles

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val LAYOUT_NAME_KEY = "layout_name"
val Context.catalogDataStore: DataStore<Preferences> by preferencesDataStore(name = "catalog_preferences")

data class CatalogState(val layoutName: String)

suspend fun Context.getCatalogState(): CatalogState {
    return CatalogState(
        catalogDataStore.data
            .map { preferences -> preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] ?: "iconButton" }
            .first()
    )
}

suspend fun Context.setCatalogState(state: CatalogState) {
    catalogDataStore.edit { preferences ->
        preferences[stringPreferencesKey(LAYOUT_NAME_KEY)] = state.layoutName
    }
}

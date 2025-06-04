package com.example.wear.tiles.counter

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val COUNT_ID = "count_id"

private val Context.counterDataStore: DataStore<Preferences> by
    preferencesDataStore(name = "counter")

suspend fun Context.getCounterState(): CounterState {
    return CounterState(
        counterDataStore.data
            .map { preferences -> preferences[intPreferencesKey(COUNT_ID)] ?: 0 }
            .first()
    )
}

suspend fun Context.setCounterState(state: CounterState) {
    counterDataStore.edit { preferences -> preferences[intPreferencesKey(COUNT_ID)] = state.count }
}

data class CounterState(var count: Int)

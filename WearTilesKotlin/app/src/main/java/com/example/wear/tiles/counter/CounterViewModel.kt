/*
 * Copyright 2025 The Android Open Source Project
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
package com.example.wear.tiles.counter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CounterUiState(val count: Int = 0, val isLoading: Boolean = false)

class CounterViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CounterUiState(isLoading = true))
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    init {
        refreshCount()
    }

    fun refreshCount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val app = getApplication<Application>()
                val currentCount = app.getCounterState().count
                _uiState.update { currentState ->
                    currentState.copy(count = currentCount, isLoading = false)
                }
            } catch (_: Exception) {
                _uiState.update { currentState -> currentState.copy(isLoading = false) }
            }
        }
    }

    fun increment() {
        viewModelScope.launch {
            val newCount = _uiState.value.count + 1
            _uiState.update { it.copy(count = newCount) }
            getApplication<Application>().setCounterState(CounterState(newCount))
        }
    }

    fun decrement() {
        viewModelScope.launch {
            val currentCount = _uiState.value.count
            if (currentCount > 0) {
                val newCount = currentCount - 1
                _uiState.update { it.copy(count = newCount) }
                getApplication<Application>().setCounterState(CounterState(newCount))
            }
        }
    }
}

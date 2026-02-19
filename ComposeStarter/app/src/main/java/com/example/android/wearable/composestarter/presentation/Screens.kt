package com.example.android.wearable.composestarter.presentation

sealed class Screen {
    data object Landing : Screen()
    data object List : Screen()
    data object Tlc : Screen()
}

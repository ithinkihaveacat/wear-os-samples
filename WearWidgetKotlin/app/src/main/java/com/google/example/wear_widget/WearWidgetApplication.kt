package com.google.example.wear_widget

import android.app.Application
import androidx.compose.remote.creation.compose.ExperimentalRemoteCreationComposeApi

class WearWidgetApplication : Application() {
    @OptIn(ExperimentalRemoteCreationComposeApi::class)
    override fun onCreate() {
        super.onCreate()
    }
}

package com.google.example.wear_widget

import android.app.Application
import androidx.compose.remote.creation.compose.ExperimentalRemoteCreationComposeApi
import androidx.compose.remote.creation.compose.RemoteComposeCreationComposeFlags

class WearWidgetApplication : Application() {
    @OptIn(ExperimentalRemoteCreationComposeApi::class)
    override fun onCreate() {
        super.onCreate()
        RemoteComposeCreationComposeFlags.isRemoteApplierEnabled = false
    }
}

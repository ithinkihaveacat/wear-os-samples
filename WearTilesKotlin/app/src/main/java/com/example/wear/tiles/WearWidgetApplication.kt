package com.example.wear.tiles

import android.app.Application
import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.RemoteComposeCreationComposeFlags
import androidx.compose.remote.creation.compose.ExperimentalRemoteCreationComposeApi

@OptIn(ExperimentalRemoteCreationComposeApi::class)
@SuppressLint("RestrictedApi")
class WearWidgetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RemoteComposeCreationComposeFlags.isRemoteApplierEnabled = false
    }
}

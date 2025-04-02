package com.example.wear.tiles.tools

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.wear.compose.material3.ColorScheme as ComposeColorScheme
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.material3.ColorScheme as ProtoColorScheme
import androidx.wear.protolayout.types.argb
import com.example.wear.tiles.messaging.bitmapToImageResource
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters

fun Resources.Builder.addIdToImageMapping(id: String, @DrawableRes resId: Int): Resources.Builder =
  addIdToImageMapping(
    id,
    ImageResource.Builder()
      .setAndroidResourceByResId(
        ResourceBuilders.AndroidImageResourceByResId.Builder().setResourceId(resId).build()
      )
      .build(),
  )

fun Resources.Builder.addIdToImageMapping(id: String, bitmap: Bitmap): Resources.Builder =
  addIdToImageMapping(id, bitmapToImageResource(bitmap))

fun column(builder: Column.Builder.() -> Unit) = Column.Builder().apply(builder).build()

fun image(builder: LayoutElementBuilders.Image.Builder.() -> Unit) =
  LayoutElementBuilders.Image.Builder().apply(builder).build()

fun ComposeColorScheme.toProtoColorScheme(): ProtoColorScheme {
  return ProtoColorScheme(
    primary = this.primary.toArgb().argb,
    primaryDim = this.primaryDim.toArgb().argb,
    primaryContainer = this.primaryContainer.toArgb().argb,
    onPrimary = this.onPrimary.toArgb().argb,
    onPrimaryContainer = this.onPrimaryContainer.toArgb().argb,
    secondary = this.secondary.toArgb().argb,
    secondaryDim = this.secondaryDim.toArgb().argb,
    secondaryContainer = this.secondaryContainer.toArgb().argb,
    onSecondary = this.onSecondary.toArgb().argb,
    onSecondaryContainer = this.onSecondaryContainer.toArgb().argb,
    tertiary = this.tertiary.toArgb().argb,
    tertiaryDim = this.tertiaryDim.toArgb().argb,
    tertiaryContainer = this.tertiaryContainer.toArgb().argb,
    onTertiary = this.onTertiary.toArgb().argb,
    onTertiaryContainer = this.onTertiaryContainer.toArgb().argb,
    surfaceContainerLow = this.surfaceContainerLow.toArgb().argb,
    surfaceContainer = this.surfaceContainer.toArgb().argb,
    surfaceContainerHigh = this.surfaceContainerHigh.toArgb().argb,
    onSurface = this.onSurface.toArgb().argb,
    onSurfaceVariant = this.onSurfaceVariant.toArgb().argb,
    outline = this.outline.toArgb().argb,
    outlineVariant = this.outlineVariant.toArgb().argb,
    background = this.background.toArgb().argb,
    onBackground = this.onBackground.toArgb().argb,
    error = this.error.toArgb().argb,
    errorDim = this.errorDim.toArgb().argb,
    errorContainer = this.errorContainer.toArgb().argb,
    onError = this.onError.toArgb().argb,
    onErrorContainer = this.onErrorContainer.toArgb().argb,
  )
}

fun ProtoColorScheme.toComposeColorScheme(): ComposeColorScheme {
  return ComposeColorScheme(
    primary = Color(this.primary.staticArgb),
    primaryDim = Color(this.primary.staticArgb),
    primaryContainer = Color(this.primaryContainer.staticArgb),
    onPrimary = Color(this.onPrimary.staticArgb),
    onPrimaryContainer = Color(this.onPrimaryContainer.staticArgb),
    secondary = Color(this.secondary.staticArgb),
    secondaryDim = Color(this.secondaryDim.staticArgb),
    secondaryContainer = Color(this.secondaryContainer.staticArgb),
    onSecondary = Color(this.secondary.staticArgb),
    onSecondaryContainer = Color(this.onSecondaryContainer.staticArgb),
    tertiary = Color(this.tertiary.staticArgb),
    tertiaryDim = Color(this.tertiaryDim.staticArgb),
    tertiaryContainer = Color(this.tertiaryContainer.staticArgb),
    onTertiary = Color(this.onTertiary.staticArgb),
    onTertiaryContainer = Color(this.onTertiaryContainer.staticArgb),
    surfaceContainerLow = Color(this.surfaceContainerLow.staticArgb),
    surfaceContainer = Color(this.surfaceContainer.staticArgb),
    surfaceContainerHigh = Color(this.surfaceContainerHigh.staticArgb),
    onSurface = Color(this.onSurface.staticArgb),
    onSurfaceVariant = Color(this.onSurfaceVariant.staticArgb),
    outline = Color(this.outline.staticArgb),
    outlineVariant = Color(this.outlineVariant.staticArgb),
    background = Color(this.background.staticArgb),
    onBackground = Color(this.onBackground.staticArgb),
    error = Color(this.error.staticArgb),
    errorDim = Color(this.errorDim.staticArgb),
    errorContainer = Color(this.errorContainer.staticArgb),
    onError = Color(this.onError.staticArgb),
    onErrorContainer = Color(this.onErrorContainer.staticArgb),
  )
}

fun DeviceParameters.isLargeScreen() = screenWidthDp >= 225

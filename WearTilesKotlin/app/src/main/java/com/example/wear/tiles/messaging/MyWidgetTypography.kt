package com.example.wear.tiles.messaging

import androidx.wear.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

/**
 * A local copy of the semantic typography styles from androidx.wear.compose.material3.Typography.
 *
 * This provides temporary access to standard text styles while the
 * RemoteMaterialTheme.typography API (b/478828032) has internal visibility.
 */
object MyWidgetTypography {
    private val defaultTypography = Typography()

    // Expose all standard text styles
    val displayLarge: TextStyle get() = defaultTypography.displayLarge
    val displayMedium: TextStyle get() = defaultTypography.displayMedium
    val displaySmall: TextStyle get() = defaultTypography.displaySmall
    val titleLarge: TextStyle get() = defaultTypography.titleLarge
    val titleMedium: TextStyle get() = defaultTypography.titleMedium
    val titleSmall: TextStyle get() = defaultTypography.titleSmall
    val labelLarge: TextStyle get() = defaultTypography.labelLarge
    val labelMedium: TextStyle get() = defaultTypography.labelMedium
    val labelSmall: TextStyle get() = defaultTypography.labelSmall
    val bodyLarge: TextStyle get() = defaultTypography.bodyLarge
    val bodyMedium: TextStyle get() = defaultTypography.bodyMedium
    val bodySmall: TextStyle get() = defaultTypography.bodySmall
    val bodyExtraSmall: TextStyle get() = defaultTypography.bodyExtraSmall
    val numeralExtraLarge: TextStyle get() = defaultTypography.numeralExtraLarge
    val numeralLarge: TextStyle get() = defaultTypography.numeralLarge
    val numeralMedium: TextStyle get() = defaultTypography.numeralMedium
    val numeralSmall: TextStyle get() = defaultTypography.numeralSmall
    val numeralExtraSmall: TextStyle get() = defaultTypography.numeralExtraSmall
}

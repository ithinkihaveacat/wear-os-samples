/*
 * Copyright 2024 The Android Open Source Project
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

package com.google.example.wear_widget // Adjust to your package

import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.ui.text.TextStyle
import androidx.wear.compose.material3.Typography

/**
 * A local copy of the semantic typography styles from androidx.wear.compose.material3.Typography.
 *
 * This object provides access to the default text styles, allowing them to be used as a
 * workaround for the known issue (b/478828032) where semantic styles are not directly
 * exposed by RemoteMaterialTheme.typography.
 *
 * Usage:
 * MaterialRemoteText(
 *     text = "My Title".rs,
 *     style = MyWidgetTypography.titleLarge
 * )
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

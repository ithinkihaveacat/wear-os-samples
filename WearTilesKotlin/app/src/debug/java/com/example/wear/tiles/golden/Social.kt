/*
 * Copyright 2022 The Android Open Source Project
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
package com.example.wear.tiles.golden

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.CONTENT_SCALE_MODE_CROP
import androidx.wear.protolayout.LayoutElementBuilders.FontSetting
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.layout.basicText
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ButtonColors
import androidx.wear.protolayout.material.Typography as MaterialTypography
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material3.ButtonDefaults.filledButtonColors
import androidx.wear.protolayout.material3.ButtonGroupDefaults.DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS
import androidx.wear.protolayout.material3.CardDefaults.filledCardColors
import androidx.wear.protolayout.material3.CardDefaults.filledTonalCardColors
import androidx.wear.protolayout.material3.CardDefaults.filledVariantCardColors
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.Typography
import androidx.wear.protolayout.material3.backgroundImage
import androidx.wear.protolayout.material3.buttonGroup
import androidx.wear.protolayout.material3.card
import androidx.wear.protolayout.material3.imageButton
import androidx.wear.protolayout.material3.materialScope
import androidx.wear.protolayout.material3.primaryLayout
import androidx.wear.protolayout.material3.text
import androidx.wear.protolayout.material3.textButton
import androidx.wear.protolayout.material3.textDataCard
import androidx.wear.protolayout.material3.textEdgeButton
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.clickable
import androidx.wear.protolayout.modifiers.clip
import androidx.wear.protolayout.modifiers.padding
import androidx.wear.protolayout.modifiers.toProtoLayoutModifiers
import androidx.wear.protolayout.types.LayoutString
import androidx.wear.protolayout.types.layoutString
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.example.wear.tiles.R
import com.example.wear.tiles.tools.MultiRoundDevicesWithFontScalePreviews
import com.example.wear.tiles.tools.addIdToImageMapping
import com.example.wear.tiles.tools.emptyClickable
import com.example.wear.tiles.tools.fontStyle
import com.example.wear.tiles.tools.image
import com.example.wear.tiles.tools.isLargeScreen

fun Context.mockContacts(): List<Contact> {
  return listOf(
    Contact(
      initials = "MS",
      avatarId = resources.getResourceName(R.drawable.resized),
      avatarResource = R.drawable.resized,
    ),
    Contact(initials = "AB", avatarId = null, avatarResource = null),
    Contact(
      initials = "WW",
      avatarId = resources.getResourceName(R.drawable.avatar_bg_3),
      avatarResource = R.drawable.avatar_bg_3,
    ),
    Contact(initials = "CD", avatarId = null, avatarResource = null),
    Contact(
      initials = "AD",
      avatarId = resources.getResourceName(R.drawable.avatar_bg_2),
      avatarResource = R.drawable.avatar_bg_2,
    ),
    Contact(initials = "EF", avatarId = null, avatarResource = null),
  )
}

data class Contact(
  val initials: String,
  val clickable: Clickable = clickable(),
  val avatarId: String?,
  @DrawableRes val avatarResource: Int?,
)

fun MaterialScope.contactCardImage(resource: String): LayoutElement = contactCardImage1(resource)

val i =
  LayoutElementBuilders.Image.Builder()
    .setHeight(expand())
    .setWidth(expand())
    .setResourceId("foo")
    .setContentScaleMode(CONTENT_SCALE_MODE_CROP)
    .build()

//  .setModifiers(LayoutModifier.clip(shapes.full).toProtoLayoutModifiers())

fun MaterialScope.contactCardImage1(resource: String): LayoutElement {
  return image {
    setHeight(expand())
    setWidth(expand())
    setModifiers(LayoutModifier.clip(shapes.full).toProtoLayoutModifiers())
    setResourceId(resource)
    setContentScaleMode(CONTENT_SCALE_MODE_CROP)
  }
}

fun MaterialScope.contactCardImage2(resource: String): LayoutElement {
  return card(
    onClick = emptyClickable,
    height = expand(),
    width = expand(),
    backgroundContent = {
      backgroundImage(
        protoLayoutResourceId = resource,
        // Need to specify these (but not when using imageButton()) because card() sets the
        // defaultBackgroundImageStyle of the MaterialScope.
        modifier = LayoutModifier.clip(shapes.full),
        overlayColor = null,
        contentScaleMode = CONTENT_SCALE_MODE_CROP,
      )
    },
  ) {
    // card() needs a LayoutElement; construct an empty one
    LayoutElementBuilders.Spacer.Builder()
      .setWidth(DimensionBuilders.dp(0f))
      .setHeight(DimensionBuilders.dp(0f))
      .build()
  }
}

fun MaterialScope.contactCardImage3(resource: String): LayoutElement {
  return imageButton(
    height = expand(),
    width = expand(),
    onClick = emptyClickable,
    backgroundContent = { backgroundImage(protoLayoutResourceId = resource) },
  )
}

fun MaterialScope.contactCardText(s: String, resource: String = ""): LayoutElement {
  val colors =
    listOf(filledVariantCardColors(), filledTonalCardColors(), filledCardColors())[s.hashCode() % 3]
  return textDataCard(
    onClick = emptyClickable,
    title = { text(text = s.layoutString, typography = Typography.TITLE_MEDIUM) },
    height = expand(),
    colors = colors,
  )
}

@OptIn(ProtoLayoutExperimental::class)
fun MaterialScope.contactButton(contact: Contact): LayoutElement {
  if (contact.avatarId != null) {
    return image {
      setHeight(expand())
      setWidth(expand())
      setModifiers(LayoutModifier.clip(shapes.full).toProtoLayoutModifiers())
      setResourceId(contact.avatarId)
      setContentScaleMode(CONTENT_SCALE_MODE_CROP)
    }
  } else {
    val colors =
      listOf(
        filledButtonColors()
          .copy(labelColor = colorScheme.onPrimary, containerColor = colorScheme.primaryDim),
        filledButtonColors()
          .copy(labelColor = colorScheme.onSecondary, containerColor = colorScheme.secondaryDim),
        filledButtonColors()
          .copy(labelColor = colorScheme.onTertiary, containerColor = colorScheme.tertiaryDim),
      )[contact.initials.hashCode() % 3]
    return textButton(
      onClick = clickable(),
      labelContent = {
        basicText(
          text = contact.initials.layoutString,
          fontStyle = fontStyle {
            setColor(colors.labelColor.prop)
            setSettings(FontSetting.width(60F))
            setSize(DimensionBuilders.sp(26F))
            setWeight(LayoutElementBuilders.FONT_WEIGHT_MEDIUM)
          }
        )
      },
      width = expand(),
      height = expand(),
      contentPadding = padding(horizontal = 4F, vertical = 2F),
      colors = colors,
    )
  }
}

object Social1 {

  fun layout(
    context: Context,
    deviceParameters: DeviceParameters,
    contacts: List<Contact>,
  ): LayoutElement {
    return materialScope(
      context = context,
      deviceConfiguration = deviceParameters,
      allowDynamicTheme = true,
    ) {
      val displayedContacts = contacts.take(6).take(if (deviceParameters.isLargeScreen()) 6 else 4)

      val (row1, row2) =
        displayedContacts.run {
          when (count()) {
            1 -> Pair(subList(0, 1), null) // 1 | 0 split
            2 -> Pair(subList(0, 2), null) // 2 | 0 split
            3 -> Pair(subList(0, 2), subList(2, 3)) // 2 | 1 split
            4 -> Pair(subList(0, 2), subList(2, 4)) // 2 | 2 split
            5 -> Pair(subList(0, 3), subList(3, 5)) // 3 | 2 split
            6 -> Pair(subList(0, 3), subList(3, 6)) // 3 | 3 split
            else ->
              throw IllegalArgumentException(
                "Unsupported contact count: ${count()}. Expected 1 to 6."
              )
          }
        }

      primaryLayout(
        mainSlot = {
          column {
            setWidth(expand())
            setHeight(expand())
            addContent(buttonGroup { row1.forEach { buttonGroupItem { contactButton(it) } } })
            if (!row2.isNullOrEmpty()) {
              addContent(DEFAULT_SPACER_BETWEEN_BUTTON_GROUPS)
              addContent(buttonGroup { row2.forEach { buttonGroupItem { contactButton(it) } } })
            }
          }
        },
        bottomSlot = {
          textEdgeButton(
            onClick = clickable(),
            labelContent = { text("More".layoutString) },
            colors =
              filledButtonColors()
                .copy(
                  containerColor = colorScheme.surfaceContainer,
                  labelColor = colorScheme.onSurface,
                ),
          )
        },
      )
    }
  }
}

object Social2 {

  fun layout(context: Context, deviceParameters: DeviceParameters, contacts: List<Contact>) =
    PrimaryLayout.Builder(deviceParameters)
      .setResponsiveContentInsetEnabled(true)
      .setContent(
        MultiButtonLayout.Builder()
          .apply {
            contacts.take(if (deviceParameters.screenWidthDp > 225) 6 else 4).forEach { contact ->
              addButtonContent(button(context, contact))
            }
          }
          .build()
      )
      .build()

  private fun button(context: Context, contact: Contact) =
    Button.Builder(context, contact.clickable)
      .apply {
        if (contact.avatarId != null) {
          setImageContent(contact.avatarId)
        } else {
          setTextContent(contact.initials, MaterialTypography.TYPOGRAPHY_TITLE3)
        }
      }
      .setButtonColors(
        ButtonColors(
          /* backgroundColor = */ ColorBuilders.argb(contact.color),
          /* contentColor = */ ColorBuilders.argb(GoldenTilesColors.DarkerGray),
        )
      )
      .build()

  data class Contact(
    val initials: String,
    @ColorInt val color: Int = GoldenTilesColors.LightBlue,
    val clickable: Clickable,
    val avatarId: String?,
  )
}

@MultiRoundDevicesWithFontScalePreviews
internal fun socialPreview(context: Context) =
  TilePreviewData(
    resources {
      context.mockContacts().forEach {
        if (it.avatarId != null && it.avatarResource != null)
          addIdToImageMapping(it.avatarId, it.avatarResource)
      }
    }
  ) {
    TilePreviewHelper.singleTimelineEntryTileBuilder(
        Social1.layout(context, it.deviceConfiguration, context.mockContacts())
      )
      .build()
  }

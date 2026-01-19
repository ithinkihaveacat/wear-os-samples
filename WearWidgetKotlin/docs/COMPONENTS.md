# Wear OS Component Catalog: Tiles vs. Widgets

This document provides a visual catalog of UI components for Wear OS, comparing the implementation between Tiles (using `androidx.wear.tiles:tiles-material`) and Widgets (using `androidx.wear.compose.remote:remote-material3`). Its purpose is to help developers map components from one system to the other.

The sample code for Tiles can be found in the [`WearTilesKotlin` project](../WearTilesKotlin/), while the Widget code is in the [`WearWidgetKotlin` project](../WearWidgetKotlin/).

**Note:** The Widget component implementations shown here are AI-generated translations from their Tile counterparts. While they serve as a useful starting point, they may not always represent the most optimal or elegant solution.

## Component Mappings

This section shows components that have a direct or close equivalent in both Tiles and Widgets.

### textButton

| Tile | Widget |
| :--- | :--- |
| ![textButton](../WearTilesKotlin/screenshots/components/textButton.png) | ![textButton](../WearWidgetKotlin/screenshots/components/textButton.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        textButton(
            onClick = clickable(id = "text_button_click"),
            labelContent = { text("Text Button".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteButton(onClick = arrayOf()) {
    MaterialRemoteText(text = "Text Button".rs)
}
``` |

### iconButton

| Tile | Widget |
| :--- | :--- |
| ![iconButton](../WearTilesKotlin/screenshots/components/iconButton.png) | ![iconButton](../WearWidgetKotlin/screenshots/components/iconButton.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        iconButton(
            onClick = clickable(id = "icon_button_click"),
            iconContent = {
                icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.ic_message_24)
                )
            }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf()
) {
    RemoteIcon(
        imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
        contentDescription = "Message".rs,
        modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize)
    )
}
``` |

### avatarButton

| Tile | Widget |
| :--- | :--- |
| ![avatarButton](../WearTilesKotlin/screenshots/components/avatarButton.png) | ![avatarButton](../WearWidgetKotlin/screenshots/components/avatarButton.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        avatarButton(
            onClick = clickable(id = "avatar_button_click"),
            avatarContent = {
                avatarImage(
                    protoLayoutResourceId = context.resources.getResourceName(R.drawable.ali),
                    contentScaleMode = CONTENT_SCALE_MODE_CROP
                )
            },
            labelContent = { text("Avatar Button".layoutString) },
            secondaryLabelContent = { text("Secondary Label".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf()
) {
    RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
        RemoteImage(
            bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
            contentDescription = "Avatar".rs,
            contentScale = ContentScale.Crop,
            modifier = RemoteModifier.size(RemoteButtonDefaults.LargeIconSize).clip(RoundedCornerShape(percent = 50))
        )
        RemoteBox(modifier = RemoteModifier.size(8.dp.asRdp())) 
        MaterialRemoteText("Avatar Button".rs)
    }
}
``` |

### imageButton

| Tile | Widget |
| :--- | :--- |
| ![imageButton](../WearTilesKotlin/screenshots/components/imageButton.png) | ![imageButton](../WearWidgetKotlin/screenshots/components/imageButton.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        imageButton(
            onClick = clickable(id = "image_button_click"),
            backgroundContent = {
                backgroundImage(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.photo_14)
                )
            }
        )
    }
)
``` | ```kotlin
RemoteBox(
    modifier = RemoteModifier
        .size(60.rdp)
        .clip(RoundedCornerShape(percent = 50)),
    horizontalAlignment = RemoteAlignment.CenterHorizontally,
    verticalArrangement = RemoteArrangement.Center
) {
     RemoteImage(
        bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
        contentDescription = "Background".rs,
        contentScale = ContentScale.Crop,
        modifier = RemoteModifier.fillMaxSize()
    )
}
``` |

### compactButton

| Tile | Widget |
| :--- | :--- |
| ![compactButton](../WearTilesKotlin/screenshots/components/compactButton.png) | ![compactButton](../WearWidgetKotlin/screenshots/components/compactButton.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        compactButton(
            onClick = clickable(id = "compact_button_click"),
            iconContent = {
                 icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.ic_message_24)
                )
            },
            labelContent = { text("Compact".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf(),
     icon = {
        RemoteIcon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_message_24),
            contentDescription = "Message".rs,
            modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize)
        )
    },
    label = { MaterialRemoteText("Compact".rs) }
)
``` |

### titleCard

| Tile | Widget |
| :--- | :--- |
| ![titleCard](../WearTilesKotlin/screenshots/components/titleCard.png) | ![titleCard](../WearWidgetKotlin/screenshots/components/titleCard.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        titleCard(
            onClick = clickable(id = "title_card_click"),
            title = { text("Title Card".layoutString) },
            content = { text("Content".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf(),
    modifier = RemoteModifier.padding(horizontal = 10.dp)
) {
    RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
        MaterialRemoteText(
            text = "Title Card".rs,
            fontWeight = FontWeight.Bold
        )
        MaterialRemoteText(
            text = "Content".rs
        )
    }
}
``` |

### appCard

| Tile | Widget |
| :--- | :--- |
| ![appCard](../WearTilesKotlin/screenshots/components/appCard.png) | ![appCard](../WearWidgetKotlin/screenshots/components/appCard.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        appCard(
            onClick = clickable(id = "app_card_click"),
            label = { text("Ali".layoutString) },
            title = { text("Dinner in SF".layoutString, maxLines = 1) },
            time = { text("2:03 PM".layoutString) },
            avatar = {
                avatarImage(
                    protoLayoutResourceId = context.resources.getResourceName(R.drawable.ali),
                    contentScaleMode = CONTENT_SCALE_MODE_CROP
                )
            },
            content = { text("Let's try that new restaurant.".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf(),
    modifier = RemoteModifier.padding(horizontal = 10.dp)
) {
    RemoteColumn(modifier = RemoteModifier.padding(8.dp)) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            modifier = RemoteModifier.fillMaxWidth()
        ) {
            RemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                contentDescription = "Avatar".rs,
                contentScale = ContentScale.Crop,
                modifier = RemoteModifier.size(24.rdp).clip(RoundedCornerShape(percent = 50))
            )
            RemoteBox(modifier = RemoteModifier.size(8.rdp))
            MaterialRemoteText("Ali".rs)
            RemoteBox(modifier = RemoteModifier.size(20.rdp)) 
            MaterialRemoteText("2:03 PM".rs)
        }
        MaterialRemoteText(
            text = "Dinner in SF".rs,
            fontWeight = FontWeight.Bold
        )
        MaterialRemoteText(
            text = "Let's try that new restaurant.".rs
        )
    }
}
``` |

### textDataCard

| Tile | Widget |
| :--- | :--- |
| ![textDataCard](../WearTilesKotlin/screenshots/components/textDataCard.png) | ![textDataCard](../WearWidgetKotlin/screenshots/components/textDataCard.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        textDataCard(
            onClick = clickable(id = "text_data_card_click"),
            title = { text("Text Data".layoutString) },
            content = { text("Content".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf(),
    colors = RemoteButtonColors(
        containerColor = Color.DarkGray.rc,
        contentColor = Color.LightGray.rc,
        secondaryContentColor = Color.White.rc,
        iconColor = Color.White.rc,
        disabledContainerColor = Color.DarkGray.rc,
        disabledContentColor = Color.LightGray.rc,
        disabledSecondaryContentColor = Color.White.rc,
        disabledIconColor = Color.White.rc
    )
) {
    RemoteColumn {
        MaterialRemoteText("Text Data".rs)
        MaterialRemoteText(
            text = "Content".rs,
            color = Color.White.rc
        )
    }
}
``` |

### iconDataCard

| Tile | Widget |
| :--- | :--- |
| ![iconDataCard](../WearTilesKotlin/screenshots/components/iconDataCard.png) | ![iconDataCard](../WearWidgetKotlin/screenshots/components/iconDataCard.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        iconDataCard(
            onClick = clickable(id = "icon_data_card_click"),
            title = { text("Icon Data".layoutString) },
            content = { text("Content".layoutString) },
            secondaryIcon = {
                icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.ic_message_24)
                )
            }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf(),
    colors = RemoteButtonColors(
        containerColor = Color.DarkGray.rc,
        contentColor = Color.LightGray.rc,
        secondaryContentColor = Color.White.rc,
        iconColor = Color.White.rc,
        disabledContainerColor = Color.DarkGray.rc,
        disabledContentColor = Color.LightGray.rc,
        disabledSecondaryContentColor = Color.White.rc,
        disabledIconColor = Color.White.rc
    )
) {
    RemoteColumn {
        MaterialRemoteText("Icon Data".rs)
        MaterialRemoteText(
            text = "Content".rs,
            color = Color.White.rc
        )
    }
    RemoteBox(modifier = RemoteModifier.size(8.rdp))
    RemoteIcon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_message_24),
        contentDescription = "Message".rs,
        modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize)
    )
}
``` |

### graphicDataCard

| Tile | Widget |
| :--- | :--- |
| ![graphicDataCard](../WearTilesKotlin/screenshots/components/graphicDataCard.png) | ![graphicDataCard](../WearWidgetKotlin/screenshots/components/graphicDataCard.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        graphicDataCard(
            onClick = clickable(id = "graphic_data_card_click"),
            graphic = {
                icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.ic_run_24)
                )
            },
            title = { text("Graphic Data".layoutString) },
            content = { text("Content".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteButton(
    onClick = arrayOf(),
    colors = RemoteButtonColors(
        containerColor = Color.DarkGray.rc,
        contentColor = Color.LightGray.rc,
        secondaryContentColor = Color.White.rc,
        iconColor = Color.White.rc,
        disabledContainerColor = Color.DarkGray.rc,
        disabledContentColor = Color.LightGray.rc,
        disabledSecondaryContentColor = Color.White.rc,
        disabledIconColor = Color.White.rc
    )
) {
    RemoteIcon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_run_24),
        contentDescription = "Run".rs,
        modifier = RemoteModifier.size(RemoteButtonDefaults.LargeIconSize)
    )
    RemoteBox(modifier = RemoteModifier.size(8.rdp))
    RemoteColumn {
        MaterialRemoteText("Graphic Data".rs)
        MaterialRemoteText(
            text = "Content".rs,
            color = Color.White.rc
        )
    }
}
``` |

### circularProgressIndicator

| Tile | Widget |
| :--- | :--- |
| ![circularProgressIndicator](../WearTilesKotlin/screenshots/components/circularProgressIndicator.png) | ![circularProgressIndicator](../WearWidgetKotlin/screenshots/components/circularProgressIndicator.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        graphicDataCard(
            onClick = clickable(id = "circular_progress_click"),
            graphic = {
                circularProgressIndicator(
                    staticProgress = 0.75f,
                    startAngleDegrees = 0f,
                    endAngleDegrees = 360f
                )
            },
            title = { text("75%".layoutString) },
            content = { text("Progress".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteBox(
    modifier = RemoteModifier.size(100.rdp),
) {
    RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
        val width = remote.component.width
        val height = remote.component.height
        val centerX = width / 2f.rf
        val centerY = height / 2f.rf
        val strokeWidth = 10f.rf
        val radius = (width / 2f.rf) - (strokeWidth / 2f.rf)

        // Track
        drawCircle(
            paint = RemotePaint().apply {
                remoteColor = Color.DarkGray.rc
                style = Paint.Style.STROKE
                this.strokeWidth = strokeWidth.toFloat()
                isAntiAlias = true
            },
            center = RemoteOffset(centerX, centerY),
            radius = radius
        )

        // Progress (75% = 270 degrees)
        drawArc(
            paint = RemotePaint().apply {
                remoteColor = Color.Red.rc
                style = Paint.Style.STROKE
                this.strokeWidth = strokeWidth.toFloat()
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
            },
            startAngle = -90f.rf,
            sweepAngle = 270f.rf,
            useCenter = false,
            topLeft = RemoteOffset(strokeWidth / 2f.rf, strokeWidth / 2f.rf),
            size = RemoteSize(width - strokeWidth, height - strokeWidth)
        )
    }
    RemoteBox(
         modifier = RemoteModifier.fillMaxSize(),
         horizontalAlignment = RemoteAlignment.CenterHorizontally,
         verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            MaterialRemoteText(
                text = "75%".rs,
                fontWeight = FontWeight.Bold,
                color = Color.White.rc
            )
            MaterialRemoteText(
                text = "Progress".rs,
                color = Color.LightGray.rc
            )
        }
    }
}
``` |

### segmentedCircularProgressIndicator

| Tile | Widget |
| :--- | :--- |
| ![segmentedCircularProgressIndicator](../WearTilesKotlin/screenshots/components/segmentedCircularProgressIndicator.png) | ![segmentedCircularProgressIndicator](../WearWidgetKotlin/screenshots/components/segmentedCircularProgressIndicator.png) |
| ```kotlin
primaryLayout(
    mainSlot = {
        graphicDataCard(
            onClick = clickable(id = "segmented_progress_click"),
            graphic = {
                segmentedCircularProgressIndicator(
                    segmentCount = 5,
                    staticProgress = 0.6f
                )
            },
            title = { text("3/5".layoutString) },
            content = { text("Segments".layoutString) }
        )
    }
)
``` | ```kotlin
RemoteBox(
    modifier = RemoteModifier.size(100.rdp),
) {
    RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
        val width = remote.component.width
        val height = remote.component.height
        val strokeWidth = 10f.rf
        val gap = 8f.rf
        val segments = 5f.rf
        val activeSegments = 3f.rf
        val totalSweep = 360f.rf
        val segmentSweep = (totalSweep - (gap * segments)) / segments

        // Active segments
        loop(0f.rf, activeSegments, 1f.rf) { i ->
            val startAngle = -90f.rf + (i * (segmentSweep + gap))
            drawArc(
                paint = RemotePaint().apply {
                    remoteColor = Color.Red.rc
                    style = Paint.Style.STROKE
                    this.strokeWidth = strokeWidth.toFloat()
                    strokeCap = Paint.Cap.ROUND
                    isAntiAlias = true
                },
                startAngle = startAngle,
                sweepAngle = segmentSweep,
                useCenter = false,
                topLeft = RemoteOffset(strokeWidth / 2f.rf, strokeWidth / 2f.rf),
                size = RemoteSize(width - strokeWidth, height - strokeWidth)
            )
        }

        // Inactive segments
        loop(activeSegments, segments, 1f.rf) { i ->
            val startAngle = -90f.rf + (i * (segmentSweep + gap))
            drawArc(
                paint = RemotePaint().apply {
                    remoteColor = Color.DarkGray.rc
                    style = Paint.Style.STROKE
                    this.strokeWidth = strokeWidth.toFloat()
                    strokeCap = Paint.Cap.ROUND
                    isAntiAlias = true
                },
                startAngle = startAngle,
                sweepAngle = segmentSweep,
                useCenter = false,
                topLeft = RemoteOffset(strokeWidth / 2f.rf, strokeWidth / 2f.rf),
                size = RemoteSize(width - strokeWidth, height - strokeWidth)
            )
        }
    }
    RemoteBox(
         modifier = RemoteModifier.fillMaxSize(),
         horizontalAlignment = RemoteAlignment.CenterHorizontally,
         verticalArrangement = RemoteArrangement.Center
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            MaterialRemoteText(
                text = "3/5".rs,
                fontWeight = FontWeight.Bold,
                color = Color.White.rc
            )
            MaterialRemoteText(
                text = "Segments".rs,
                color = Color.LightGray.rc
            )
        }
    }
}
``` |

## Tile-Only Components

These components are available in Tiles but do not have a direct one-to-one equivalent in the currently available Widget components.

### button

![button](../WearTilesKotlin/screenshots/components/button.png)

A general-purpose button with icon, primary, and secondary labels.

```kotlin
primaryLayout(
    mainSlot = {
        button(
            onClick = clickable(id = "button_click"),
            labelContent = { text("Button".layoutString) },
            secondaryLabelContent = { text("Secondary Label".layoutString) },
            iconContent = {
                icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.ic_message_24)
                )
            }
        )
    }
)
```

### iconEdgeButton

![iconEdgeButton](../WearTilesKotlin/screenshots/components/iconEdgeButton.png)

A button with an icon, intended to be placed at the edge of the screen (e.g., bottom).

```kotlin
primaryLayout(
    mainSlot = { text("Content".layoutString) },
    bottomSlot = {
        iconEdgeButton(
            onClick = clickable(id = "icon_edge_button_click"),
            iconContent = {
                icon(
                    protoLayoutResourceId =
                    context.resources.getResourceName(R.drawable.ic_message_24)
                )
            }
        )
    }
)
```

### textEdgeButton

![textEdgeButton](../WearTilesKotlin/screenshots/components/textEdgeButton.png)

A button with text, intended to be placed at the edge of the screen.

```kotlin
primaryLayout(
    mainSlot = { text("Content".layoutString) },
    bottomSlot = {
        textEdgeButton(
            onClick = clickable(id = "text_edge_button_click"),
            labelContent = { text("Edge Button".layoutString) }
        )
    }
)
```

## Widget-Only Components

These components or concepts are unique to Widgets and do not have a direct equivalent in Tiles.

### fullBleedImage

![fullBleedImage](../WearWidgetKotlin/screenshots/components/fullBleedImage.png)

A background image filling the component with overlaid text.

```kotlin
RemoteBox(
    modifier = RemoteModifier.fillMaxSize()
) {
    RemoteImage(
        bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
        contentDescription = "Background".rs,
        contentScale = ContentScale.Crop,
        modifier = RemoteModifier.fillMaxSize()
    )
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
         MaterialRemoteText(
            text = "Full Bleed".rs,
            color = Color.White.rc,
            fontWeight = FontWeight.Bold
        )
    }
}
```

### animatedBox

![animatedBox](../WearWidgetKotlin/screenshots/components/animatedBox.png)

A box that animates between sizes and colors when clicked, demonstrating stateful, animated widgets.

```kotlin
@RemoteComposable
@Composable
fun ComponentCatalogAnimatedBoxSample() {
    val state = rememberRemoteIntValue { 0 }
    val isToggled = state eq 1.ri

    val containerColor = isToggled.select(Color.Red.rc, Color.Blue.rc)
    val boxSize = isToggled.select(120f.rf, 60f.rf).asRemoteDp()

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteBox(
            modifier = RemoteModifier
                .size(boxSize)
                .animationSpec(enabled = true)
                .background(containerColor)
                .clickable(
                    actions = arrayOf(ValueChange(state, state xor 1.ri))
                )
        )
    }
}
```

# Wear Widget UI Components Catalog

This catalog provides a visual reference and code samples for various UI components supported by Wear Widgets (Remote Compose).

## textButton

![textButton](textButton.png)

A simple button with a text label.

```kotlin
RemoteButton(onClick = arrayOf()) {
    MaterialRemoteText(text = "Text Button".rs)
}
```

## iconButton

![iconButton](iconButton.png)

A button containing an icon.

```kotlin
RemoteButton(
    onClick = arrayOf()
) {
    RemoteIcon(
        imageVector = ImageVector.vectorResource(id = R.drawable.android_24px),
        contentDescription = "Message".rs,
        modifier = RemoteModifier.size(RemoteButtonDefaults.SmallIconSize)
    )
}
```

## avatarButton

![avatarButton](avatarButton.png)

A button with a rounded avatar image and text.

```kotlin
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
        // Spacer
        RemoteBox(modifier = RemoteModifier.size(8.dp.asRdp())) 
        // Texts
        MaterialRemoteText("Avatar Button".rs)
    }
}
```

## imageButton

![imageButton](imageButton.png)

A button with a background image.

```kotlin
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
```

## compactButton

![compactButton](compactButton.png)

A compact button variant.

```kotlin
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
```

## titleCard

![titleCard](titleCard.png)

A simple card with a title and content text.

```kotlin
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
```

## appCard

![appCard](appCard.png)

A card displaying application-specific content, such as a message notification.

```kotlin
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
```

## textDataCard

![textDataCard](textDataCard.png)

A rounded card displaying labeled text data.

```kotlin
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
```

## iconDataCard

![iconDataCard](iconDataCard.png)

A rounded card displaying text data alongside an icon.

```kotlin
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
```

## graphicDataCard

![graphicDataCard](graphicDataCard.png)

A rounded card displaying a large graphic icon and text content.

```kotlin
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
```

## circularProgressIndicator

![circularProgressIndicator](circularProgressIndicator.png)

A custom drawn circular progress indicator using `RemoteCanvas`.

```kotlin
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
```

## segmentedCircularProgressIndicator

![segmentedCircularProgressIndicator](segmentedCircularProgressIndicator.png)

A segmented circular progress indicator drawn with `RemoteCanvas`.

```kotlin
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
```

## fullBleedImage

![fullBleedImage](fullBleedImage.png)

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
    // Overlay Text
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

## animatedBox

![animatedBox](animatedBox.png)

A box that animates between sizes and colors when clicked.

```kotlin
@RemoteComposable
@Composable
fun ComponentCatalogAnimatedBoxSample() {
    // Define a remote state key for toggling
    val state = rememberRemoteIntValue { 0 }
    val isToggled = state eq 1.ri

    // Derive animated properties based on the remote state
    val containerColor = isToggled.select(Color.Red.rc, Color.Blue.rc)
    val boxSize = isToggled.select(120f.rf, 60f.rf).asRemoteDp()

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteBox(
            modifier = RemoteModifier
                // Apply the animated size
                .size(boxSize)
                // Enable tween animations for all property changes on this element
                .animationSpec(enabled = true)
                // Apply the animated color
                .background(containerColor)
                // Toggle the state key on click
                .clickable(
                    actions = arrayOf(ValueChange(state, state xor 1.ri))
                )
        )
    }
}
```
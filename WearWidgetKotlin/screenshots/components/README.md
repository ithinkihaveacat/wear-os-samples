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

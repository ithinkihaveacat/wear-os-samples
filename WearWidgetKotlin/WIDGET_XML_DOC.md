# Wear Widget XML Configuration Documentation

This document describes the format and implications of the `wearwidget-provider` XML configuration used by `androidx.glance.wear`.

## XML Format Specification

The configuration file is typically located at `res/xml/widget_info.xml` and referenced in the `AndroidManifest.xml` via meta-data:

```xml
<meta-data
    android:name="androidx.glance.wear.widget.provider"
    android:resource="@xml/widget_info" />
```

### `<wearwidget-provider>` (Root Tag)

| Attribute | Description | Default |
| :--- | :--- | :--- |
| `label` | Human-readable name of the widget. | Service label from Manifest |
| `description` | Description of the widget's purpose. | Service description |
| `icon` | Drawable resource for the widget icon. | Service icon |
| `group` | A unique string identifying a group of related widget providers. | Fully qualified class name |
| `preferredType` | The default container type to use if none is specified. | `SMALL` (2) |
| `configIntentAction` | Intent action to launch a configuration Activity. | `null` |

### `<container>` (Child Tag)

Defines a supported size for the widget. At least one `<container>` is required.

| Attribute | Description | Valid Values |
| :--- | :--- | :--- |
| `type` | The size type of the container. | `SMALL` (2), `LARGE` (1) |
| `previewImage` | Static drawable shown in the picker before the widget loads. | Drawable resource |
| `label` | (Optional) Size-specific label override. | Root label |
| `description` | (Optional) Size-specific description override. | Root description |

## Key Findings & Implications

### 1. Surface Differentiation
*   **Tiles (Legacy):** When binding via `androidx.wear.tiles.action.BIND_TILE_PROVIDER`, the system requests `containerType=0` (FULLSCREEN). Tiles largely ignore the `group` and `preferredType` attributes in the XML.
*   **Widgets (New):** When binding via `androidx.glance.wear.action.BIND_WIDGET_PROVIDER`, the system respects the XML configuration for `SMALL` and `LARGE` types.

### 2. Grouping Behavior
The `group` attribute allows the system to treat multiple services as different versions of the same widget. This is intended for seamless migration or providing different implementations for the same content without duplicating entries in the user's carousel.

### 3. Container Types
*   **SMALL (2):** Typically represents a smaller, glanceable slot on the watch face or home screen.
*   **LARGE (1):** Represents a larger area with more detail.
*   **FULLSCREEN (0):** Reserved for legacy Tile compatibility. It **cannot** be declared in the `<container>` tag in XML (the parser will throw an exception).

### 4. Previews
The `previewImage` is highly recommended. The system uses this to provide immediate visual feedback in the widget/tile picker while the actual widget data is being fetched or rendered for the first time.

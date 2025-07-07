# Rules and Requirements

- Use functions from the `androidx.wear.protolayout.material3` package where
  possible. It is very unlikely that you should be generating any code that uses
  the `androidx.wear.protolayout.material` (no "3") library; please flag this if that
  appears to be required.
- After making a change to a file, run the build-apk tool to see if there are
  any errors, and resolve any that appear.
- Use `LayoutModifier.*` functions instead of the `Modifiers.Builder().*` functions. Use
  `.toProtoLayoutModifiers()` if you need to convert a layout to a different type.
- Look through `app/src/main/java/com/example/wear/tiles/tools/Extensions.kt` for
  helper functions that may be useful, and feel free to add any similar
  functions to make new code more readable.
- Regarding colors, do not write code like
  `ButtonDefaults.filledVariantButtonColors(colorScheme.primary)`! Just use
  `filledVariantButtonColors()` or similar as imported from
  `androidx.wear.protolayout.material3.ButtonDefaults.filledVariantButtonColors`.
- Prefer `MaterialScope.*` scope functions over `Builder()` style constructions.
- `LayoutModifier` does not have a `weight()` function. To arrange for e.g. two
  elements to have different widths, place them inside a box and use e.g.
  `.setWidth(weight(0.7f))`.
- Tiles modifiers, though them may look like Compose or Wear Compose modifiers, are different. When
  working with modifiers, take care that you are correctly generating Tiles modifier code.

# Useful Resources

The full source code to the tiles and protolayout libraries is available in the `.context/*`
directory. For example the source code for the class
`androidx.wear.protolayout.material3.iconEdgeButton` is found in the file
`.context/proto/androidx/wear/protolayout/material3/EdgeButton.kt`. The source code for the class
`android.wear.tiles.TileService` is found in the file
`.context/tiles/androidx/wear/tiles/TileService.java`.

The `.context/samples` directory contains some code to produce layouts. This code is
missing the support infrastructure (i.e. the service to create the layouts,
etc.), but the layout function calls are syntactically correct. You may wish to
consult the `.context/samples/*.kt` files when dealing with layout
issues or when need to see some examples of how a function is invoked.

If you need to verify the visual result of a code change, feel free to take a screenshot, and then
ask me questions about the result. For example, you could ask, "Is the text in the screenshot at
`/tmp/screenshot-AAAAA.png` version larger or smaller than the screenshot at
`/tmp/screenshot-BBBBB.png`. Provide filenames when making this request.

# Reference Information

## Tile Previews

A TileService is associated with a static preview image via a `<meta-data>` entry
in its AndroidManifest.xml declaration.

The system uses a predefined name attribute to identify the preview metadata and
the resource attribute to reference the drawable. As this is a standard Android
drawable resource, you can use resource qualifiers to provide different preview
images for different device configurations. For example, you could provide a
preview for larger screens by placing it in a `drawable-w225dp` directory. The
system will automatically select the appropriate preview image based on the
device's characteristics.

```xml
<!-- AndroidManifest.xml -->

<service android:name=".golden.WeatherTileService" android:label="@string/tile_label_weather"
  android:permission="com.google.android.wearable.permission.BIND_TILE_PROVIDER"
  android:exported="true">

  <!-- Identifies the service as a Tile Provider -->
  <intent-filter>
    <action android:name="androidx.wear.tiles.action.BIND_TILE_PROVIDER" />
  </intent-filter>

  <!-- Links the Tile Provider to a static drawable for its preview -->

  <meta-data android:name="androidx.wear.tiles.PREVIEW"
    android:resource="@drawable/tile_preview_weather" />

</service>
```

# Processes and howto guides

## How to Regenerate or Update Tile Previews

1. **Identify Tile Services** Determine the application's tile services using
   one of the following methods:

- **Method A (Static Analysis):** Review the `app/src/main/AndroidManifest.xml`
  file to locate all `<service>` declarations that include an intent filter for
  the `BIND_TILE_PROVIDER` action.

- **Method B (Live Query):** Install the application on a device and run the
  following `adb` command, replacing `your.package.name` with the app's actual
  package name. This queries the device for all installed tile provider services
  belonging to your app.

  ```shell
  adb shell cmd package query-services -a androidx.wear.tiles.action.BIND_TILE_PROVIDER --brief | grep your.package.name
  ```

2. **Build and Install** If not already done, compile the latest version of the
   application and install it onto a connected Wear OS device or emulator.

3. **Generate and Update Previews for Each Tile** For each tile service
   identified in Step 1, perform the following sequence:

   a. **Check for an Existing Preview** In `AndroidManifest.xml`, inspect the
   `<service>` entry. If a `<meta-data>` tag with the name
   `androidx.wear.tiles.PREVIEW` already exists, note the drawable name
   specified in its `android:resource` attribute (e.g.,
   `@drawable/tile_preview_existing`). This is the file you will overwrite.

   b. **Add the Tile to the Device** Use the appropriate developer tool to add
   an instance of the tile to the device's tile carousel.

   c. **Display the Tile** Navigate the device's UI to bring the newly added
   tile into full view on the screen.

   d. **Take a Screenshot** Capture a screenshot of the device's display.

   e. **Save or Overwrite the Preview Image** Save the screenshot into the
   project's `app/src/main/res/drawable/` directory.

- If an existing preview file was identified in step 3a, overwrite it with the
  new screenshot.
- Otherwise, save the screenshot under a new, descriptive name (e.g.,
  `tile_preview_servicename.png`).

f. **Update the Manifest (if necessary)**

    - If you overwrote an existing file, no manifest change is needed.
    - If you created a new file, add the corresponding `<meta-data>` tag to the
      `<service>` declaration, linking to the new drawable resource.

g. **Clean Up** Remove the tile instance from the device's carousel.

4. **Verify Changes** After processing all tiles, rebuild the application to
   confirm that the manifest is correct and all preview resources are included
   properly.

# Agent Task: Set up Wear OS Remote Demos Standalone App

Your task is to extract the Wear OS integration demos from the AndroidX
monorepo, transform them into a standalone, buildable Android application, and
generate a visual gallery (screenshots) of all 11 component demo screens.

You should aim to minimally modify the original source code.

______________________________________________________________________

## Step 1: Download the Sample Code

Execute the following commands to download and extract the raw Kotlin source
files:

```bash
mkdir -p wear-remote-demos && cd wear-remote-demos
curl -sSL "https://android.googlesource.com/platform/frameworks/support/+archive/refs/heads/androidx-main/wear/compose/remote/integration-tests/demos.tar.gz" | tar -xz
```

______________________________________________________________________

## Step 2: Transform into a Standalone App

Configure a standard Gradle build system and Android Manifest to make this a
compilable Wear OS application. You can use any compatible versions of AGP,
Kotlin, and Compose libraries that work for you.

> [!TIP] **Critical Workaround (Minimal Source Modification)**: The downloaded
> code contains a utility file
> `src/main/java/androidx/wear/compose/remote/integration/demos/components/RemoteDemoItem.kt`
> which by default configures a custom typeface resolver using AOSP `testutils`
> classes that are not published.
>
> To avoid the complex process of copying these unpublished `testutils` files
> and resources locally, you can **minimally modify `RemoteDemoItem.kt`** to
> disable this custom typeface resolver:
>
> 1. Remove the imports from
>    `androidx.compose.remote.player.compose.test.utils.*`.
> 1. Remove the call to `configureTypefaceResolver(player, context)` inside
>    `RemoteDocumentPlayer`'s `update` block.
> 1. Delete the `configureTypefaceResolver` function definition at the bottom of
>    the file.
>
> This allows the app to build cleanly and render previews using default system
> fonts.

______________________________________________________________________

## Step 3: Capture Component Screenshots

Once the application builds cleanly, generate a visual gallery containing a
descriptive screenshot or PNG image for each of the 11 widget demo screens.

You may achieve this using any preferred solution, such as:

- Running the app on an emulator/device and capturing screenshots.
- Using a preview rendering tool (like the `compose-preview` plugin) to render
  `@Preview` annotations directly to PNG files.

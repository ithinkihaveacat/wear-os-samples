WatchFace Sample (Kotlin)
===============================
Demonstrates watch faces using the androidX libraries (Kotlin).

> [!IMPORTANT]  
> As of July 10, 2024, watch faces must use the [Watch Face Format][1] ([sample][3]) in order to be installed on new watches that launch with Wear OS 5 pre-installed. Learn more about the user-facing changes in this [Help Center][2] article.
>
> Additionally, starting in early 2025 (specific date to be announced during Q4 2024), all new watch faces published on Google Play must use the Watch Face Format.

Introduction
------------
The AndroidX watch face libraries allow you to develop a watch face service in Kotlin. However,
[WFF][1] is strongly recommended in preference to this approach.

Steps to build in Android Studio
--------------------------------
Because a watch face only contains a service, that is, there is no Activity, you first need to turn
off the launch setting that opens an Activity on your device.

To do that (and once the project is open) go to Run -> Edit Configurations. Select the **app**
module and the **General** tab. In the Launch Options, change **Launch:** to **Nothing**. This will
allow you to successfully install the watch face on the Wear device.

When installed, you will need to select the watch face in the watch face picker, i.e., the watch
face will not launch on its own like a regular app.

Screenshots
-------------

<img src="screenshots/analog-face.png" width="400" alt="Analog Watchface"/>
<img src="screenshots/analog-watch-side-config-all.png" width="400" alt="Analog Watchface Config"/>
<img src="screenshots/analog-watch-side-config-1.png" width="400" alt="Analog Watchface Config"/>
<img src="screenshots/analog-watch-side-config-2.png" width="400" alt="Analog Watchface"/>

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the "gradlew build" command or
use "Import Project" in Android Studio.

Support
-------

- Stack Overflow: https://stackoverflow.com/questions/tagged/wear-os

If you've found an error in this sample, please file an issue:
https://github.com/android/wear-os-samples

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.

[1]: https://developer.android.com/training/wearables/wff
[2]: https://support.google.com/wearos/thread/284572445
[3]: https://github.com/android/wear-os-samples/tree/main/WatchFaceFormat

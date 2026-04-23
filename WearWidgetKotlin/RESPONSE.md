Hi! I followed up on your new proposal to remove the `@Config` annotations and rely on `robolectric.properties`. I tested it with a pure build and clean environment, but unfortunately, it **still fails** with the `ClassNotFoundException: android.app.Application` on a clean build.

#### Environment & Commits
- **OS**: Linux
- **`wear-os-samples`**: Branch `fix/revert-render-previews-workaround` (workaround removed).
- **`compose-ai-tools`**: Branch `fix/robolectric-classpath-discovery` (plus I manually removed `@Config` from `CircularClipTest` and `PreviewWrapperTest` as well to be thorough). Built and published to `mavenLocal` as `0.7.6-SNAPSHOT` using **JDK 17**.
- **Consumer Runtime**: Clean build in `wear-os-samples` executed with **JDK 21**.

#### Results
Even after removing the `@Config` and `@GraphicsMode` annotations from all test files in the project to bypass the `AnnotationParser` issue, the build still fails during `:app:renderPreviews` discovery with:

```
RobolectricRenderTest > initializationError FAILED
    java.lang.TypeNotPresentException at Method.java:779
        Caused by: java.lang.NoClassDefFoundError at ClassLoader.java:-2
            Caused by: java.lang.ClassNotFoundException at BuiltinClassLoader.java:641
```
The cause remains `Caused by: java.lang.ClassNotFoundException: android.app.Application`.

#### Conclusion
It seems that removing the annotations from our test classes is not enough to prevent the JVM from attempting to resolve `android.app.Application`. The issue likely triggers whenever the `org.robolectric.annotation.Config` class itself is loaded or inspected by the runner, as its default values reference `android.app.Application`. 

Since `android.jar` is still on the application classpath rather than the bootstrap classpath for this plain `Test` task, the strict classloading rules on this Linux setup continue to block it.

I will stick with the `afterEvaluate` classpath injection workaround for now to keep generating previews.

### Summary of Investigation and Environment Matrix

Hi everyone! I am the AI assistant pair-programming with @ithinkihaveacat on the environment where this issue reproduces consistently. To help clarify the discussion and ensure we can reproduce the results, here is a summary of the work done so far and the environment matrix we are dealing with.

#### The Environment Matrix
We have identified three distinct machine configurations involved in this discussion:
1. **Machine 1 (Linux, JDK 25 default, JDK 21/17 used for builds)**: **Broken.** This is my environment. It consistently throws `ClassNotFoundException: android.app.Application` during test discovery on clean builds unless the workaround is used.
2. **Machine 2 (macOS)**: **Works.** Tests run and previews render successfully without the classloader issue.
3. **Machine 3 (Another Linux environment)**: **Works.** A different Linux setup where the issue also did not reproduce.

#### Summary of the Problem
The core issue is that the custom `renderPreviews` task (a plain Gradle `Test` task) fails to resolve `android.app.Application` when JUnit uses reflection to inspect annotations (specifically `@Config`) during the test discovery phase. This happens before Robolectric starts and is sensitive to strict classloader delegation rules on newer JDKs.

#### Summary of Fix Attempts

Here is a chronological summary of the attempts made to solve this without resorting to the consumer-side workaround:

*   **Attempt 1: Wiring `javaLauncher` to Project Toolchain**
    *   *Goal*: Prevent the task from falling back to the system default JDK 25 and force it to use JDK 21.
    *   *Result*: **Failed on Machine 1.** Forcing the Test Worker to use JDK 21 did not solve the reflection issue during discovery.
*   **Attempt 2: Injecting `android.jar` into `bootstrapClasspath`**
    *   *Goal*: Make platform classes visible to the core reflection APIs globally by treating them as system classes.
    *   *Result*: **Failed.** It resolved the discovery issue but broke Robolectric at runtime with `RuntimeException("Stub!")` because the raw stubs were loaded before Robolectric could apply its shadows.
*   **Attempt 3: Relying on `mockable-android.jar`**
    *   *Goal*: Use AGP's safe mockable jar instead of the raw stub jar on the classpath.
    *   *Result*: **Failed on Machine 1.** The class was still not visible to `AnnotationParser` on the standard classpath during discovery.
*   **Attempt 4: Removing Annotations and using `robolectric.properties`**
    *   *Goal*: Bypass `AnnotationParser` entirely for `@Config` by removing the annotations from bytecode and moving settings to a properties file.
    *   *Result*: **Failed on Machine 1.** Even with annotations removed from all test classes in the project, the loading of the `Config` class itself or related infrastructure still triggered the resolution failure.

#### Current Conclusion
The problem appears to be a strict classloader delegation behavior specific to the JVM implementation on **Machine 1** (Linux with JDK 25 base). It refuses to load platform-like classes from the application classpath during reflection when requested by system classes.

For now, the `afterEvaluate` classpath injection workaround remains the only viable solution to generate previews in my environment.

package org.conscrypt;

import java.security.Provider;

/**
 * Dummy implementation of OpenSSLProvider to bypass Conscrypt native library loading in Robolectric tests.
 *
 * Motivation:
 * Conscrypt (which Robolectric initializes by default) bundles a native BoringSSL library.
 * Loading that native library can fail with UnsatisfiedLinkError on systems where the system's
 * dynamic linker (or version dependencies) conflicts with the bundled libraries.
 *
 * Providing this stub in `src/test/java/org/conscrypt/` avoids linking against the native framework.
 */
public class OpenSSLProvider extends Provider {
    public OpenSSLProvider() {
        super("Conscrypt", 1.0, "Dummy Conscrypt Provider");
    }
}

package org.conscrypt;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Dummy implementation of OkHostnameVerifier to satisfy Robolectric's dependencies
 * when bypassing the standard Conscrypt classpath.
 *
 * It is required to sit in the `org.conscrypt` namespace so that Robolectric initializes
 * smoothly without expecting signed JAR consistency.
 */
public class OkHostnameVerifier implements HostnameVerifier {
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();
    
    public static OkHostnameVerifier getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

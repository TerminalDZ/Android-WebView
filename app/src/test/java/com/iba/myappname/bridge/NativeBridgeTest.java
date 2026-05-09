package com.iba.myappname.bridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Compile-time and contract sanity checks for {@link NativeBridge}.
 *
 * <p>Behavioral tests that touch Activity/Vibrator services live in
 * androidTest because they require an instrumentation context. These pure
 * checks just guard the JS-facing API shape from accidental drift.</p>
 */
public class NativeBridgeTest {

    @Test
    public void jsNamespaceIsStable() {
        assertEquals("IBA", NativeBridge.JS_NAMESPACE);
    }

    @Test
    public void allJsEntryPointsExist() throws NoSuchMethodException {
        Class<NativeBridge> clazz = NativeBridge.class;
        // If any of these names changes, callers in the field break silently — fail fast.
        assertNotNull(clazz.getDeclaredMethod("toast", String.class));
        assertNotNull(clazz.getDeclaredMethod("longToast", String.class));
        assertNotNull(clazz.getDeclaredMethod("share", String.class, String.class));
        assertNotNull(clazz.getDeclaredMethod("openExternal", String.class));
        assertNotNull(clazz.getDeclaredMethod("copy", String.class));
        assertNotNull(clazz.getDeclaredMethod("vibrate", long.class));
        assertNotNull(clazz.getDeclaredMethod("deviceInfo"));
        assertNotNull(clazz.getDeclaredMethod("reload"));
        assertNotNull(clazz.getDeclaredMethod("close"));
    }
}

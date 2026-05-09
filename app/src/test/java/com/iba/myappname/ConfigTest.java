package com.iba.myappname;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Smoke tests over {@link Config} invariants. Catches mistakes such as a
 * placeholder URL accidentally shipping in a release build, or contradictory
 * orientation strings that would silently fall through to the default.
 */
public class ConfigTest {

    @Test
    public void homeUrlIsHttps() {
        assertNotNull(Config.HOME_URL);
        assertTrue("HOME_URL must use HTTPS for production safety",
                Config.HOME_URL.startsWith("https://"));
    }

    @Test
    public void hostHasNoScheme() {
        assertNotNull(Config.HOST);
        assertFalse("HOST must not contain a scheme prefix",
                Config.HOST.startsWith("http://") || Config.HOST.startsWith("https://"));
    }

    @Test
    public void orientationValuesAreValid() {
        assertOrientation(Config.PHONE_ORIENTATION);
        assertOrientation(Config.TABLET_ORIENTATION);
    }

    @Test
    public void splashTimeoutIsSane() {
        assertTrue("SPLASH_TIMEOUT must be non-negative", Config.SPLASH_TIMEOUT >= 0);
        assertTrue("SPLASH_TIMEOUT > 30s is almost certainly a mistake",
                Config.SPLASH_TIMEOUT <= 30_000);
    }

    @Test
    public void splashScalePercentageInRange() {
        assertTrue(Config.SCALE_SPLASH_IMAGE >= 0);
        assertTrue(Config.SCALE_SPLASH_IMAGE <= 100);
    }

    @Test
    public void rateAndFacebookCountersAreNonNegative() {
        assertTrue(Config.RATE_DAYS_UNTIL_PROMPT >= 0);
        assertTrue(Config.RATE_LAUNCHES_UNTIL_PROMPT >= 0);
        assertTrue(Config.FACEBOOK_DAYS_UNTIL_PROMPT >= 0);
        assertTrue(Config.FACEBOOK_LAUNCHES_UNTIL_PROMPT >= 0);
    }

    @Test
    public void linkHandlingOptionsAreInRange() {
        assertInRange(Config.EXTERNAL_LINK_HANDLING_OPTIONS, 0, 2);
        assertInRange(Config.SPECIAL_LINK_HANDLING_OPTIONS, 0, 2);
        assertInRange(Config.QR_CODE_URL_OPTIONS, 0, 4);
    }

    @Test
    public void downloadableExtensionsAreNotEmpty() {
        assertNotNull(Config.downloadableExtension);
        assertFalse("Downloader needs at least one extension",
                Config.downloadableExtension.isEmpty());
    }

    @Test
    public void offlineColorIsHex() {
        assertNotNull(Config.OFFLINE_SCREEN_BACKGROUND_COLOR);
        assertTrue(Config.OFFLINE_SCREEN_BACKGROUND_COLOR.startsWith("#"));
    }

    private static void assertOrientation(String value) {
        assertTrue("Orientation must be one of: portrait, landscape, auto. Got: " + value,
                "portrait".equals(value) || "landscape".equals(value) || "auto".equals(value));
    }

    private static void assertInRange(int value, int low, int high) {
        assertTrue(value + " out of range [" + low + ", " + high + "]",
                value >= low && value <= high);
    }
}

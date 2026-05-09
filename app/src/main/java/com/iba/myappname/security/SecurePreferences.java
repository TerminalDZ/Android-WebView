package com.iba.myappname.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.security.GeneralSecurityException;
import java.io.IOException;

/**
 * Thin wrapper around {@link EncryptedSharedPreferences} that gracefully falls
 * back to a regular {@link SharedPreferences} when the secure provider is
 * unavailable (e.g. on emulators without a hardware-backed KeyStore).
 *
 * <p>Use for sensitive values such as auth tokens, push IDs, or purchase
 * receipts. Do <b>not</b> use for high-volume non-sensitive data — encryption
 * has a small but non-zero per-write cost.</p>
 */
public final class SecurePreferences {

    private static final String TAG = "SecurePreferences";
    private static final String DEFAULT_FILE = "iba_secure_prefs";

    private SecurePreferences() {}

    @NonNull
    public static SharedPreferences get(@NonNull Context context) {
        return get(context, DEFAULT_FILE);
    }

    @NonNull
    public static SharedPreferences get(@NonNull Context context, @NonNull String fileName) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context.getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    context.getApplicationContext(),
                    fileName,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.w(TAG, "EncryptedSharedPreferences unavailable, falling back to plain prefs", e);
            return context.getApplicationContext()
                    .getSharedPreferences(fileName + "_plain", Context.MODE_PRIVATE);
        }
    }

    public static void putString(@NonNull Context context, @NonNull String key, @Nullable String value) {
        get(context).edit().putString(key, value).apply();
    }

    @Nullable
    public static String getString(@NonNull Context context, @NonNull String key, @Nullable String fallback) {
        return get(context).getString(key, fallback);
    }

    public static void remove(@NonNull Context context, @NonNull String key) {
        get(context).edit().remove(key).apply();
    }

    public static void clearAll(@NonNull Context context) {
        get(context).edit().clear().apply();
    }
}

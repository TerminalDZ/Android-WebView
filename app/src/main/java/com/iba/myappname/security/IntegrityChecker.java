package com.iba.myappname.security;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.android.play.core.integrity.IntegrityTokenResponse;
import com.iba.myappname.Config;

import java.security.SecureRandom;

/**
 * Thin wrapper around the Google Play Integrity API.
 *
 * <p>Returns a signed token that the backend can verify with Google's
 * decryption endpoint. The verification step belongs on a server you control —
 * never trust the token's claims on-device alone.</p>
 */
public final class IntegrityChecker {

    private static final String TAG = "IntegrityChecker";

    public interface Callback {
        void onToken(@NonNull String token);

        void onError(@NonNull Throwable error);
    }

    private IntegrityChecker() {}

    public static void requestToken(@NonNull Context context, @NonNull Callback callback) {
        if (!Config.USE_PLAY_INTEGRITY || Config.PLAY_INTEGRITY_CLOUD_PROJECT_NUMBER <= 0L) {
            callback.onError(new IllegalStateException(
                    "Play Integrity disabled. Set Config.USE_PLAY_INTEGRITY=true and provide a Cloud Project Number."));
            return;
        }
        try {
            String nonce = generateNonce();
            IntegrityManager manager = IntegrityManagerFactory.create(context.getApplicationContext());
            IntegrityTokenRequest request = IntegrityTokenRequest.builder()
                    .setNonce(nonce)
                    .setCloudProjectNumber(Config.PLAY_INTEGRITY_CLOUD_PROJECT_NUMBER)
                    .build();
            manager.requestIntegrityToken(request)
                    .addOnSuccessListener((IntegrityTokenResponse resp) -> callback.onToken(resp.token()))
                    .addOnFailureListener(callback::onError);
        } catch (Throwable t) {
            Log.w(TAG, "Play Integrity request failed", t);
            callback.onError(t);
        }
    }

    /** 32-byte cryptographic nonce, URL-safe Base64. */
    @NonNull
    private static String generateNonce() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return android.util.Base64.encodeToString(
                bytes,
                android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING);
    }
}

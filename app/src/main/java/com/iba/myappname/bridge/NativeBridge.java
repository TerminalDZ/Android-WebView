package com.iba.myappname.bridge;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.iba.myappname.R;
import com.iba.myappname.security.SecurePreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executor;

/**
 * Native bridge exposed to the WebView under {@code window.IBA}.
 *
 * <p>From JavaScript:</p>
 * <pre>
 *   window.IBA.toast("Hello");
 *   window.IBA.share("https://example.com", "Check this out");
 *   window.IBA.vibrate(120);
 *   window.IBA.copy("text to copy");
 *   const info = JSON.parse(window.IBA.deviceInfo());
 *   window.IBA.openExternal("https://google.com");
 *   window.IBA.requestBiometric();        // result delivered to onBiometricResult(success)
 *   window.IBA.saveBase64ImageToGallery("data:image/png;base64,...", "filename.png");
 *   window.IBA.startVoiceSearch();        // result delivered to onVoiceResult(text)
 *   const token = window.IBA.firebaseToken();
 * </pre>
 *
 * <p>All methods are thread-confined: they marshal to the UI thread when they
 * touch UI/system services. The bridge holds {@link WeakReference}s to the
 * Activity/WebView to avoid leaking after configuration changes.</p>
 */
public final class NativeBridge {

    public static final String JS_NAMESPACE = "IBA";
    private static final String TAG = "NativeBridge";

    /** SharedPreferences key used to cache the most recently fetched FCM token. */
    public static final String FCM_TOKEN_KEY = "fcm_token";

    private final WeakReference<Activity> activityRef;
    private final WeakReference<WebView> webViewRef;

    public NativeBridge(@NonNull Activity activity, @NonNull WebView webView) {
        this.activityRef = new WeakReference<>(activity);
        this.webViewRef = new WeakReference<>(webView);
    }

    /** Attach this bridge to its WebView (call once after JS is enabled). */
    public static void attach(@NonNull Activity activity, @NonNull WebView webView) {
        webView.addJavascriptInterface(new NativeBridge(activity, webView), JS_NAMESPACE);
    }

    // ---------------------------------------------------------------------
    //  UI utilities
    // ---------------------------------------------------------------------

    @JavascriptInterface
    public void toast(final String message) {
        runOnUi(activity ->
                Toast.makeText(activity, message == null ? "" : message, Toast.LENGTH_SHORT).show());
    }

    @JavascriptInterface
    public void longToast(final String message) {
        runOnUi(activity ->
                Toast.makeText(activity, message == null ? "" : message, Toast.LENGTH_LONG).show());
    }

    // ---------------------------------------------------------------------
    //  Sharing & external apps
    // ---------------------------------------------------------------------

    @JavascriptInterface
    public void share(final String url, final String title) {
        runOnUi(activity -> {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(Intent.EXTRA_TEXT, url == null ? "" : url);
            send.putExtra(Intent.EXTRA_SUBJECT, title == null ? "" : title);
            activity.startActivity(Intent.createChooser(send, title == null ? "Share" : title));
        });
    }

    @JavascriptInterface
    public void openExternal(final String url) {
        if (url == null || url.isEmpty()) return;
        runOnUi(activity -> {
            Intent view = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                activity.startActivity(view);
            } catch (Exception e) {
                Log.w(TAG, "openExternal failed for " + url, e);
            }
        });
    }

    // ---------------------------------------------------------------------
    //  Clipboard
    // ---------------------------------------------------------------------

    @JavascriptInterface
    public void copy(final String text) {
        runOnUi(activity -> {
            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("IBA WebView", text == null ? "" : text));
            }
        });
    }

    // ---------------------------------------------------------------------
    //  Haptic feedback
    // ---------------------------------------------------------------------

    @JavascriptInterface
    @SuppressWarnings("deprecation")
    public void vibrate(final long durationMs) {
        Activity activity = activityRef.get();
        if (activity == null) return;
        long ms = Math.max(1L, Math.min(durationMs, 5_000L));
        Vibrator vibrator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vm = (VibratorManager) activity.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vm == null ? null : vm.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(ms);
        }
    }

    // ---------------------------------------------------------------------
    //  Device info
    // ---------------------------------------------------------------------

    @JavascriptInterface
    public String deviceInfo() {
        Activity activity = activityRef.get();
        JSONObject json = new JSONObject();
        try {
            json.put("platform", "android");
            json.put("sdkInt", Build.VERSION.SDK_INT);
            json.put("release", Build.VERSION.RELEASE);
            json.put("manufacturer", Build.MANUFACTURER);
            json.put("model", Build.MODEL);
            json.put("brand", Build.BRAND);
            json.put("device", Build.DEVICE);
            json.put("locale", Locale.getDefault().toString());
            json.put("language", Locale.getDefault().getLanguage());
            json.put("region", Locale.getDefault().getCountry());
            json.put("timezone", TimeZone.getDefault().getID());
            if (activity != null) {
                json.put("packageName", activity.getPackageName());
                json.put("androidId", Settings.Secure.getString(
                        activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                int nightFlag = activity.getResources().getConfiguration().uiMode
                        & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
                json.put("darkMode", nightFlag == android.content.res.Configuration.UI_MODE_NIGHT_YES);
            }
        } catch (JSONException e) {
            Log.w(TAG, "deviceInfo encode error", e);
        }
        return json.toString();
    }

    /** Convenience for sites that just want the BCP-47 locale tag. */
    @JavascriptInterface
    public String locale() {
        return Locale.getDefault().toLanguageTag();
    }

    // ---------------------------------------------------------------------
    //  WebView control
    // ---------------------------------------------------------------------

    @JavascriptInterface
    public void reload() {
        runOnUi(activity -> {
            WebView webView = webViewRef.get();
            if (webView != null) webView.reload();
        });
    }

    @JavascriptInterface
    public void close() {
        runOnUi(Activity::finish);
    }

    // ---------------------------------------------------------------------
    //  Push token (cached by MyFirebaseMessagingService → AlertManager)
    // ---------------------------------------------------------------------

    @JavascriptInterface
    @Nullable
    public String firebaseToken() {
        Activity activity = activityRef.get();
        if (activity == null) return null;
        // Same key MyFirebaseMessagingService writes to via AlertManager.
        return activity.getSharedPreferences("rate_prefs", Context.MODE_PRIVATE)
                .getString("FirebaseToken", null);
    }

    // ---------------------------------------------------------------------
    //  Biometric prompt
    // ---------------------------------------------------------------------

    /**
     * Trigger the system biometric prompt. The result is dispatched back to JS
     * by calling {@code window.onBiometricResult(success, errCode, errMessage)}.
     */
    @JavascriptInterface
    public void requestBiometric() {
        runOnUi(activity -> {
            if (!(activity instanceof AppCompatActivity)) {
                dispatchBiometricResult(false, -1, "Host activity is not AppCompatActivity");
                return;
            }
            BiometricManager manager = BiometricManager.from(activity);
            int can = manager.canAuthenticate(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                            | BiometricManager.Authenticators.DEVICE_CREDENTIAL
                            | BiometricManager.Authenticators.BIOMETRIC_WEAK);
            if (can != BiometricManager.BIOMETRIC_SUCCESS) {
                dispatchBiometricResult(false, can, "Biometric not available");
                return;
            }
            Executor executor = ContextCompat.getMainExecutor(activity);
            BiometricPrompt prompt = new BiometricPrompt(
                    (AppCompatActivity) activity, executor,
                    new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            dispatchBiometricResult(true, 0, "");
                        }

                        @Override
                        public void onAuthenticationError(int errCode, @NonNull CharSequence errString) {
                            dispatchBiometricResult(false, errCode, errString.toString());
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            dispatchBiometricResult(false, -2, "Authentication failed");
                        }
                    });
            BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(activity.getString(R.string.prompt_info_title))
                    .setSubtitle(activity.getString(R.string.prompt_info_subtitle))
                    .setDescription(activity.getString(R.string.prompt_info_description))
                    .setNegativeButtonText(activity.getString(R.string.prompt_info_cancel))
                    .setConfirmationRequired(false)
                    .build();
            prompt.authenticate(info);
        });
    }

    private void dispatchBiometricResult(boolean success, int code, @NonNull String message) {
        WebView webView = webViewRef.get();
        if (webView == null) return;
        String escaped = message.replace("\\", "\\\\").replace("'", "\\'");
        String js = String.format(
                Locale.US,
                "if(window.onBiometricResult){window.onBiometricResult(%b,%d,'%s');}",
                success, code, escaped);
        webView.post(() -> webView.evaluateJavascript(js, null));
    }

    // ---------------------------------------------------------------------
    //  Save image to gallery from a base64 data URL or remote URL
    // ---------------------------------------------------------------------

    @JavascriptInterface
    public boolean saveBase64ImageToGallery(final String dataUrl, final String displayName) {
        Activity activity = activityRef.get();
        if (activity == null || dataUrl == null) return false;
        try {
            int comma = dataUrl.indexOf(',');
            String b64 = comma >= 0 ? dataUrl.substring(comma + 1) : dataUrl;
            byte[] bytes = Base64.decode(b64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap == null) return false;
            return saveBitmap(activity, bitmap, displayName);
        } catch (Exception e) {
            Log.w(TAG, "saveBase64ImageToGallery failed", e);
            return false;
        }
    }

    private boolean saveBitmap(@NonNull Activity activity, @NonNull Bitmap bitmap, @Nullable String displayName) {
        String fileName = (displayName == null || displayName.isEmpty())
                ? ("iba_" + System.currentTimeMillis() + ".png")
                : displayName;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/IBA");
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }
        ContentResolver resolver = activity.getContentResolver();
        Uri itemUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (itemUri == null) return false;
        try (OutputStream out = resolver.openOutputStream(itemUri)) {
            if (out == null) return false;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            Log.w(TAG, "Bitmap compress failed", e);
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            resolver.update(itemUri, values, null, null);
        }
        return true;
    }

    // ---------------------------------------------------------------------
    //  Voice search
    // ---------------------------------------------------------------------

    /**
     * Launches the speech-to-text picker. The recognized text comes back via
     * {@code window.onVoiceResult(text)}. Requires the host Activity to call
     * {@link #handleVoiceResult(WebView, String)} from its
     * {@code onActivityResult} (or an ActivityResult contract).
     */
    @JavascriptInterface
    public void startVoiceSearch() {
        runOnUi(activity -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
            try {
                activity.startActivityForResult(intent, REQ_VOICE_SEARCH);
            } catch (Exception e) {
                Log.w(TAG, "Voice search unavailable", e);
                toast("Voice recognition is not available on this device");
            }
        });
    }

    public static final int REQ_VOICE_SEARCH = 8714;

    /**
     * Helper for the host Activity: forward voice recognition results back to JS.
     */
    public static void handleVoiceResult(@NonNull WebView webView, @Nullable String text) {
        if (text == null) return;
        String escaped = text.replace("\\", "\\\\").replace("'", "\\'");
        String js = "if(window.onVoiceResult){window.onVoiceResult('" + escaped + "');}";
        webView.post(() -> webView.evaluateJavascript(js, null));
    }

    // ---------------------------------------------------------------------
    //  Persistent key/value storage (encrypted)
    // ---------------------------------------------------------------------

    @JavascriptInterface
    public void secureSet(final String key, final String value) {
        Activity activity = activityRef.get();
        if (activity == null || key == null) return;
        SecurePreferences.putString(activity, key, value);
    }

    @JavascriptInterface
    @Nullable
    public String secureGet(final String key) {
        Activity activity = activityRef.get();
        if (activity == null || key == null) return null;
        return SecurePreferences.getString(activity, key, null);
    }

    @JavascriptInterface
    public void secureRemove(final String key) {
        Activity activity = activityRef.get();
        if (activity == null || key == null) return;
        SecurePreferences.remove(activity, key);
    }

    // ---------------------------------------------------------------------
    //  Internals
    // ---------------------------------------------------------------------

    private void runOnUi(@NonNull UiAction action) {
        Activity activity = activityRef.get();
        if (activity == null) return;
        activity.runOnUiThread(() -> {
            try {
                action.run(activity);
            } catch (Exception e) {
                Log.w(TAG, "Bridge action error", e);
            }
        });
    }

    @FunctionalInterface
    private interface UiAction {
        void run(@NonNull Activity activity);
    }
}

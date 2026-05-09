package com.iba.myappname.webview;

import android.content.Context;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;

import com.iba.myappname.Config;

import java.io.File;

/**
 * Builds a {@link WebViewAssetLoader} that serves the app's bundled assets
 * folder (and a writable internal directory for cached web responses) under
 * the virtual host {@code https://appassets.iba/}.
 *
 * <p>Sites can reference these for offline fallback assets:</p>
 * <pre>
 *   &lt;link rel="stylesheet" href="https://appassets.iba/assets/local-html/styles.css"&gt;
 *   &lt;script src="https://appassets.iba/cache/bundle.js"&gt;&lt;/script&gt;
 * </pre>
 *
 * <p>Note: {@link WebViewAssetLoader.InternalStoragePathHandler} restricts the
 * directory to a path under {@code filesDir} or {@code cacheDir} — and the
 * directory must already exist when the handler is constructed.</p>
 */
public final class WebAssetLoaderFactory {

    private static final String TAG = "WebAssetLoader";
    private static final String CACHE_SUBDIR = "iba_web_cache";

    private WebAssetLoaderFactory() {}

    @NonNull
    public static WebViewAssetLoader create(@NonNull Context context) {
        WebViewAssetLoader.Builder builder = new WebViewAssetLoader.Builder()
                .setDomain(Config.ASSET_LOADER_DOMAIN)
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(context));

        File cacheRoot = new File(context.getFilesDir(), CACHE_SUBDIR);
        try {
            if (cacheRoot.exists() || cacheRoot.mkdirs()) {
                builder.addPathHandler("/cache/",
                        new WebViewAssetLoader.InternalStoragePathHandler(context, cacheRoot));
            } else {
                Log.w(TAG, "Could not create cache dir " + cacheRoot + ", /cache/ handler disabled");
            }
        } catch (Exception e) {
            // Defensive: any IllegalArgumentException from the handler must not crash the host.
            Log.w(TAG, "InternalStoragePathHandler init failed; /cache/ handler disabled", e);
        }
        return builder.build();
    }

    /** Convenience: hand off interception decisions to the loader. */
    @Nullable
    public static WebResourceResponse intercept(
            @NonNull WebViewAssetLoader loader,
            @NonNull WebResourceRequest request) {
        return loader.shouldInterceptRequest(request.getUrl());
    }
}

# IBA WebView

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="IBA WebView Logo" width="120" />
</p>

<p align="center">
  <b>A production-ready Android WebView template — turn any responsive website or web app into a native Android app, no coding required.</b>
</p>

<p align="center">
  <img alt="minSdk" src="https://img.shields.io/badge/minSdk-23-blue" />
  <img alt="targetSdk" src="https://img.shields.io/badge/targetSdk-34-brightgreen" />
  <img alt="AGP" src="https://img.shields.io/badge/AGP-8.2.2-success" />
  <img alt="Gradle" src="https://img.shields.io/badge/Gradle-8.2-success" />
  <img alt="JDK" src="https://img.shields.io/badge/JDK-17-orange" />
</p>

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Project Structure](#project-structure)
4. [Quick Start](#quick-start)
5. [Configuration Reference (`Config.java`)](#configuration-reference-configjava)
6. [Customization](#customization)
7. [JavaScript Native Bridge](#javascript-native-bridge)
8. [Push Notifications](#push-notifications)
9. [Ads Integration](#ads-integration)
10. [In-App Billing](#in-app-billing)
11. [Deep Linking](#deep-linking)
12. [Security Hardening](#security-hardening)
13. [Permissions](#permissions)
14. [Testing](#testing)
15. [Continuous Integration](#continuous-integration)
16. [Troubleshooting](#troubleshooting)
17. [Author](#author)

---

## Overview

**IBA WebView** is a feature-rich Android template that wraps any web app or website inside a native Android container, optimized using the **Chromium** rendering engine. It is designed for non-developers and developers alike — most apps can be built by editing only `Config.java` and a handful of resource files.

### Core idea

- Drop your website URL into `HOME_URL` inside `Config.java`, or set `USE_LOCAL_HTML_FOLDER` to bundle local HTML files.
- The app handles everything else: splash screen, caching, offline mode, push notifications, ads, biometric authentication, etc.

### Identity

| Property                 | Value                               |
| ------------------------ | ----------------------------------- |
| Java package (namespace) | `com.iba.myappname`                 |
| Application ID           | `com.iba.webview`                   |
| App label                | `@string/app_name` (default: `IBA`) |
| Min SDK                  | 23 (Android 6.0 Marshmallow)        |
| Target / Compile SDK     | 34 (Android 14)                     |
| Java version             | 17                                  |
| Android Gradle Plugin    | 8.2.2                               |
| Gradle                   | 8.2                                 |

> The original WebViewGold/`com.webviewgold.*` namespace has been fully removed. All license assets (`WebViewGold.app`, `LicenseCheck.app`) have been deleted; the codebase is now fully under the `IBA` brand.

---

## Features

### Core WebView

- ✅ Chromium-powered WebView with optimized performance
- ✅ Full HTML5 support (audio, video fullscreen, geolocation, file uploads)
- ✅ Local HTML folder support (`assets/local-html/`) with online fallback
- ✅ Smart cache mechanism with optional clear-on-startup / clear-on-exit
- ✅ Custom User-Agent override
- ✅ Hardware acceleration toggle
- ✅ Mixed-content (HTTP/HTTPS) handling
- ✅ Cookie sync (manual + auto)

### UX & UI

- ✅ Animated splash screen (GIF) with configurable duration & scaling
- ✅ Progress spinner / loading indicator
- ✅ Pull-to-refresh (`SwipeRefreshLayout`)
- ✅ Swipe-left/right gesture navigation
- ✅ Native offline screen with "Try again" button
- ✅ Native dialog system (first-run, rate-app, follow-on-Facebook)
- ✅ Dark mode toggle
- ✅ Status bar text color override (light/dark)
- ✅ Hide navigation bar in landscape (immersive mode)
- ✅ Configurable max text zoom
- ✅ Multi-language support (English, German, Spanish out of the box)
- ✅ RTL support enabled
- ✅ Themed (monochrome) launcher icon for Android 13+

### Native Integrations

- 🔐 Biometric authentication (fingerprint / face unlock via `androidx.biometric`)
- 📡 NFC read & write (NDEF text records)
- 📷 QR code scanner (ZXing `ScanContract` — modern AndroidX result API)
- 📍 Geolocation / GPS with foreground service for background tracking
- 📞 `tel:` / `mailto:` / `sms:` intent handling
- 📦 File downloads (PDF, DOCX, MP3, MP4, EPUB, …) via Android `DownloadManager`
- 📤 File uploads with `FilePicker` & camera capture (image, video, audio)
- 🔗 Deep linking (App Links + custom URI schemes)
- 🛡️ Anti-screenshot (`FLAG_SECURE`)
- 🛡️ SSL hardening (block self-signed / faulty certs, HTTPS-only network security config)
- 🛡️ Root detection & block

### Bridge, Storage & Offline

- 🌐 **JavaScript ↔ Native Bridge** — `window.IBA` API for web → native calls (toast, share, vibrate, biometric, gallery save, voice search, secure storage)
- 🔒 **Encrypted SharedPreferences** (`SecurePreferences`) for tokens and receipts
- 📴 **PWA-style asset loader** — serve bundled assets via `https://appassets.iba/`
- 🎨 **Theme-color sync** — read `<meta name="theme-color">` from page and apply to status bar
- 🌗 **Material 3 Dynamic Color** (Android 12+, opt-in)
- 🛡️ **Play Integrity API** for stronger root / tamper detection
- 📥 **Notification inbox** — every push is persisted to a Room database

### Monetization

- 💰 AdMob banner + interstitial (with click/tap counter trigger)
- 💰 Facebook Audience Network banner + interstitial
- 💰 AdSense for web ads
- 💰 Rewarded ads (optional override)
- 💳 Google Play Billing v7 for in-app purchases

### Push Notifications

- 🔔 OneSignal v4
- 🔔 Firebase Cloud Messaging with topic subscriptions
- 🔔 Pushwoosh
- All three providers support deep-linking via `url` payload

---

## Project Structure

```
Android-WebView/
├── .github/
│   ├── workflows/android.yml             # CI: lint, tests, debug APK, release AAB
│   └── dependabot.yml                    # Automated dependency updates
├── app/
│   ├── google-services.json              # Firebase config (replace with your own)
│   ├── proguard-rules.pro                # R8 / ProGuard rules for all SDKs
│   ├── build.gradle                      # App-level Gradle (AGP 8 ready)
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── assets/
│       │   │   ├── custom.css            # Inject CSS into all pages
│       │   │   ├── custom.js             # Inject JS into all pages
│       │   │   └── local-html/           # Bundled HTML fallback
│       │   ├── java/com/iba/myappname/
│       │   │   ├── Config.java           # ⭐ Main configuration file
│       │   │   ├── MainActivity.java     # WebView host
│       │   │   ├── BaseActivity.java     # Biometric prompt base class
│       │   │   ├── SplashScreenActivity.java
│       │   │   ├── SplashScreen.java
│       │   │   ├── CustomWebView.java
│       │   │   ├── WebViewApp.java       # Application class (SDK init)
│       │   │   ├── AlertManager.java
│       │   │   ├── GPSService.java
│       │   │   ├── MyFirebaseMessagingService.java
│       │   │   ├── MyLifecycleObserver.java
│       │   │   ├── GenericFileProvider.java
│       │   │   ├── biometric/BiometricPromptUtils.java
│       │   │   ├── bridge/NativeBridge.java          # JS ↔ Native bridge
│       │   │   ├── notifications/                    # Room inbox: Entity, DAO, Database
│       │   │   ├── security/
│       │   │   │   ├── SecurePreferences.java        # EncryptedSharedPreferences wrapper
│       │   │   │   └── IntegrityChecker.java         # Play Integrity API helper
│       │   │   ├── webview/
│       │   │   │   └── WebAssetLoaderFactory.java    # PWA-style asset loader
│       │   │   └── nfc/MyNfcActivity.java
│       │   └── res/
│       │       ├── mipmap-anydpi-v26/    # Adaptive + monochrome icons
│       │       ├── values/               # strings, colors, dimens, themes
│       │       ├── values-de/            # German translations
│       │       ├── values-es/            # Spanish translations
│       │       └── xml/
│       │           ├── network_security_config.xml
│       │           ├── backup_rules.xml
│       │           ├── data_extraction_rules.xml
│       │           ├── file_path.xml
│       │           └── nfc_tech_filter.xml
│       └── test/                         # JVM unit tests
│           └── java/com/iba/myappname/
│               ├── ConfigTest.java
│               └── bridge/NativeBridgeTest.java
├── lint-baseline.xml                     # Lint baseline (new warnings fail CI)
├── gradle/wrapper/                       # Gradle 8.2 wrapper
├── build.gradle                          # Project-level Gradle
├── settings.gradle                       # Modules: app
├── gradle.properties                     # JVM, AndroidX, R8 full mode
└── README.md                         # ← you are here
```

---

## Quick Start

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android device or emulator running API 23+

### Steps

```bash
# 1) Clone the repo
git clone https://github.com/TerminalDZ/Android-WebView.git
cd Android-WebView

# 2) Open in Android Studio
#    File → Open → select the Android-WebView folder

# 3) Edit Config.java with your URL
#    app/src/main/java/com/iba/myappname/Config.java
#    └─ HOST = "www.yourdomain.com";
#    └─ HOME_URL = "https://www.yourdomain.com";

# 4) Replace google-services.json (only if FIREBASE_PUSH_ENABLED = true)
#    Download yours from https://console.firebase.google.com

# 5) Build & run (the `iba` flavor is the default tenant)
./gradlew assembleIbaDebug         # → app/build/outputs/apk/iba/debug/app-iba-debug.apk
./gradlew installIbaDebug          # builds and installs on connected device
./gradlew bundleIbaRelease         # produces signed AAB for the Play Store
./gradlew testIbaDebugUnitTest     # JVM unit tests
./gradlew lintIbaDebug             # lint with baseline enforcement
```

### Bare-minimum configuration

Edit only **two lines** in `Config.java` to launch your first build:

```java
public static final String HOST     = "www.yourdomain.com";          // no http(s)://
public static       String HOME_URL = "https://www.yourdomain.com";  // full URL
```

---

## Configuration Reference (`Config.java`)

> The full configuration table — every flag previously documented in the original `Doc/index.htm`.

### Core URL & Display

| Option                                      | Type                                    | Description                                                               |
| ------------------------------------------- | --------------------------------------- | ------------------------------------------------------------------------- |
| `HOST`                                      | String **(required)**                   | Domain host without `https://` / `http://` (e.g. `"www.example.org"`).    |
| `HOME_URL`                                  | String **(required)**                   | Full WebView URL including `https://` (e.g. `"https://www.example.org"`). |
| `USE_LOCAL_HTML_FOLDER`                     | boolean                                 | `true` → use local `assets/local-html/index.html` instead of remote URL.  |
| `FALLBACK_USE_LOCAL_HTML_FOLDER_IF_OFFLINE` | boolean                                 | Use local HTML when offline, remote when online.                          |
| `ACTIVATE_PROGRESS_BAR`                     | boolean                                 | Show/hide the loading spinner.                                            |
| `PHONE_ORIENTATION`                         | `"portrait"` / `"landscape"` / `"auto"` | Phone orientation.                                                        |
| `TABLET_ORIENTATION`                        | `"portrait"` / `"landscape"` / `"auto"` | Tablet orientation.                                                       |
| `USER_AGENT`                                | String                                  | Custom UA, leave empty for default Android UA.                            |
| `HARDWARE_ACCELERATION`                     | boolean                                 | GPU rendering on/off. May increase memory.                                |
| `APPEND_LANG_CODE`                          | boolean                                 | Append `?webview_language=EN` to the URL.                                 |
| `AUTO_REFRESH_ENABLED`                      | boolean                                 | Refresh WebView when app returns from background.                         |

### Link Handling

| Option                           | Type        | Description                                          |
| -------------------------------- | ----------- | ---------------------------------------------------- |
| `EXTERNAL_LINK_HANDLING_OPTIONS` | int (0/1/2) | 0 = in-app, 1 = in-app tab, 2 = external browser.    |
| `SPECIAL_LINK_HANDLING_OPTIONS`  | int (0/1/2) | For links with `target="_blank"` or ending with `#`. |
| `BROWSER_WHITELIST`              | String[]    | Domains that **always** open externally.             |
| `BROWSER_BLACKLIST`              | String[]    | Domains that **never** open externally.              |
| `ALWAYS_OPEN_IN_INAPP_TAB`       | String[]    | URLs forced into in-app tab.                         |
| `NEVER_OPEN_IN_INAPP_TAB`        | String[]    | URLs that must not open in in-app tab.               |
| `QR_CODE_URL_OPTIONS`            | int (0–4)   | Where to open scanned QR URLs.                       |

### Cache & Storage

| Option                   | Type    | Description                           |
| ------------------------ | ------- | ------------------------------------- |
| `CLEAR_CACHE_ON_STARTUP` | boolean | Wipe cache & cookies at every launch. |
| `CLEAR_CACHE_ON_EXIT`    | boolean | Wipe cache & cookies on app exit.     |

### Deep Linking

| Option                                     | Type    | Description                         |
| ------------------------------------------ | ------- | ----------------------------------- |
| `IS_DEEP_LINKING_ENABLED`                  | boolean | Enable deep links / App Links.      |
| `OPEN_NOTIFICATION_URLS_IN_SYSTEM_BROWSER` | boolean | Push deep links → external browser. |

### Splash Screen

| Option                    | Type             | Description                                         |
| ------------------------- | ---------------- | --------------------------------------------------- |
| `SPLASH_SCREEN_ACTIVATED` | boolean          | Show splash on launch.                              |
| `SPLASH_TIMEOUT`          | int (ms)         | Splash duration in milliseconds.                    |
| `REMAIN_SPLASH_OPTION`    | boolean          | Keep splash until URL fully loads.                  |
| `SCALE_SPLASH_IMAGE`      | double `[0–100]` | Splash image size relative to smallest screen edge. |

### Visuals & Behavior

| Option                             | Type       | Description                                                                 |
| ---------------------------------- | ---------- | --------------------------------------------------------------------------- |
| `blackStatusBarText`               | boolean    | `true` for dark text, `false` for light.                                    |
| `enableBioMetricAuth`              | boolean    | Require fingerprint / face unlock at app start.                             |
| `PREVENT_SLEEP`                    | boolean    | Keep screen awake while app is open.                                        |
| `ENABLE_SWIPE_NAVIGATE`            | boolean    | Swipe left/right ↔ back/forward.                                            |
| `ENABLE_PULL_REFRESH`              | boolean    | Swipe down to refresh.                                                      |
| `HIDE_VERTICAL_SCROLLBAR`          | boolean    | —                                                                           |
| `HIDE_HORIZONTAL_SCROLLBAR`        | boolean    | —                                                                           |
| `ENABLE_ZOOM`                      | boolean    | Pinch-to-zoom.                                                              |
| `DISABLE_DARK_MODE`                | boolean    | Force light mode.                                                           |
| `HIDE_NAVIGATION_BAR_IN_LANDSCAPE` | boolean    | Immersive landscape.                                                        |
| `MAX_TEXT_ZOOM`                    | int        | Cap text zoom. (`0` = disabled, default = 100, large = 115, largest = 130). |
| `EXIT_APP_BY_BACK_BUTTON_ALWAYS`   | boolean    | Always exit on back button.                                                 |
| `EXIT_APP_BY_BACK_BUTTON_HOMEPAGE` | boolean    | Exit only when on home page.                                                |
| `EXIT_APP_DIALOG`                  | boolean    | Confirm before exit.                                                        |
| `OFFLINE_SCREEN_BACKGROUND_COLOR`  | hex String | e.g. `"#ffffff"`.                                                           |
| `LANDSCAPE_FULLSCREEN_VIDEO`       | boolean    | Force landscape on fullscreen video.                                        |
| `LINK_DRAG_AND_DROP`               | boolean    | Enable link drag-and-drop.                                                  |

### Security

| Option                                   | Type    | Description                                                          |
| ---------------------------------------- | ------- | -------------------------------------------------------------------- |
| `PREVENT_SCREEN_CAPTURE`                 | boolean | Block screenshots / screen recording.                                |
| `UUID_ENHANCE_WEBVIEW_URL`               | boolean | Append `?uuid=…` to first request.                                   |
| `BLOCK_SELF_SIGNED_AND_FAULTY_SSL_CERTS` | boolean | Reject invalid SSL certs.                                            |
| `BLOCK_ROOTED_DEVICES`                   | boolean | Disable app on rooted devices (heuristic check).                     |
| `USE_PLAY_INTEGRITY`                     | boolean | Enable Google Play Integrity API for stronger tamper detection.      |
| `PLAY_INTEGRITY_CLOUD_PROJECT_NUMBER`    | long    | Cloud Project Number from Google Cloud Console (required if enabled).|

### Native Bridge & Web Integration

| Option                       | Type    | Description                                                                     |
| ---------------------------- | ------- | ------------------------------------------------------------------------------- |
| `ENABLE_NATIVE_BRIDGE`       | boolean | Expose `window.IBA` to JavaScript ([details below](#javascript-native-bridge)). |
| `ENABLE_ASSET_LOADER`        | boolean | Serve bundled assets via `https://appassets.iba/assets/` (PWA-style).           |
| `ASSET_LOADER_DOMAIN`        | String  | Virtual host for the asset loader.                                              |
| `SYNC_THEME_COLOR_FROM_WEB`  | boolean | Read `<meta name="theme-color">` and apply to status bar after each page load.  |
| `USE_MATERIAL3_DYNAMIC_COLOR`| boolean | Apply Material 3 dynamic color overlay on Android 12+.                          |

### Native Dialogs

| Option                           | Type    | Description                              |
| -------------------------------- | ------- | ---------------------------------------- |
| `SHOW_FIRSTRUN_DIALOG`           | boolean | Welcome dialog at first launch.          |
| `SHOW_RATE_DIALOG`               | boolean | "Rate this app" dialog.                  |
| `SHOW_FACEBOOK_DIALOG`           | boolean | "Follow us on Facebook" dialog.          |
| `RATE_DAYS_UNTIL_PROMPT`         | int     | Days before rate dialog appears.         |
| `RATE_LAUNCHES_UNTIL_PROMPT`     | int     | Launches before rate dialog appears.     |
| `FACEBOOK_DAYS_UNTIL_PROMPT`     | int     | Days before Facebook dialog appears.     |
| `FACEBOOK_LAUNCHES_UNTIL_PROMPT` | int     | Launches before Facebook dialog appears. |
| `FACEBOOK_URL`                   | String  | Your Facebook page URL.                  |
| `ALLOW_IMAGE_DOWNLOAD`           | boolean | Show "Save image" on long-press.         |

### Push Notifications

| Option                              | Description                                                                  |
| ----------------------------------- | ---------------------------------------------------------------------------- |
| `PUSH_ENABLED`                      | OneSignal on/off (set ID in `app/build.gradle`).                             |
| `PUSH_ENHANCE_WEBVIEW_URL`          | Append `?onesignal_push_id=…`.                                               |
| `PUSH_RELOAD_ON_USERID`             | Reload WebView when OneSignal returns user ID.                               |
| `FIREBASE_PUSH_ENABLED`             | Firebase FCM on/off (replace `google-services.json`).                        |
| `firebasechanneltopic`              | FCM topic to auto-subscribe (`"NONE"` to skip).                              |
| `FIREBASE_PUSH_ENHANCE_WEBVIEW_URL` | Append `?firebase_push_id=…`.                                                |
| `PUSHWOOSH_ENABLED`                 | Pushwoosh on/off (set credentials in `AndroidManifest.xml` + `strings.xml`). |
| `PUSHWOOSH_ENHANCE_WEBVIEW_URL`     | Append `?pushwoosh_id=…`.                                                    |

### Ads

| Option                            | Description                                             |
| --------------------------------- | ------------------------------------------------------- |
| `SHOW_ADSENSE_AD`                 | AdSense web ads (independent of AdMob).                 |
| `SHOW_BANNER_AD`                  | AdMob banner.                                           |
| `SHOW_FULL_SCREEN_AD`             | AdMob interstitial.                                     |
| `SHOW_AD_AFTER_X`                 | Number of website clicks before interstitial.           |
| `SHOW_AD_AFTER_X_TAPS`            | Number of screen taps before interstitial.              |
| `INCREMENT_WITH_TAPS`             | Count screen taps toward `SHOW_AD_AFTER_X`.             |
| `INCREMENT_WITH_REDIRECTS`        | Count URL redirects toward `SHOW_AD_AFTER_X`.           |
| `USE_FACEBOOK_ADS`                | Replace AdMob with Facebook Audience Network.           |
| `ENABLE_REWARDED_ADS`             | Enable rewarded ads (required even for manual).         |
| `USE_REWARDED_ADS_WHERE_POSSIBLE` | Substitute interstitials with rewarded ads.             |
| `AD_TRIGGER_URLS`                 | URL substrings that trigger ads (e.g. `"thanks.html"`). |

### Login Helpers

| Option                           | Description                         |
| -------------------------------- | ----------------------------------- |
| `GOOGLE_LOGIN_HELPER_TRIGGERS`   | URL prefixes during Google login.   |
| `FACEBOOK_LOGIN_HELPER_TRIGGERS` | URL prefixes during Facebook login. |
| `HOME_URL_LOGOUT`                | URL to clear cookies on logout.     |

### Manual Cookie Sync

| Option                        | Description                               |
| ----------------------------- | ----------------------------------------- |
| `MANUAL_COOKIE_SYNC`          | Force cookie sync more often than 30 s.   |
| `COOKIE_SYNC_TIME`            | Sync interval in milliseconds.            |
| `MANUAL_COOKIE_SYNC_TRIGGERS` | URL prefixes to sync (empty = all pages). |

### File Downloader

| Option                  | Description                                                                                                   |
| ----------------------- | ------------------------------------------------------------------------------------------------------------- |
| `downloadableExtension` | List of extensions to trigger downloads (`.pdf`, `.docx`, `.mp3`, `.mp4`, `.epub`, `.pptx`, `.xlsx`, `.wav`). |

### Permission Toggles

> Match these with `AndroidManifest.xml` to fully strip a permission.

| Option               | Description                   |
| -------------------- | ----------------------------- |
| `requireLocation`    | Disable location/GPS APIs.    |
| `requireStorage`     | Disable download/upload APIs. |
| `requireCamera`      | Disable camera APIs.          |
| `requireRecordAudio` | Disable mic APIs.             |

---

## Customization

### 1) Change app name & strings

Edit `app/src/main/res/values/strings.xml`:

```xml
<string name="app_name" translatable="false">My Awesome App</string>
<string name="first_launch_title">Welcome!</string>
<string name="rate_title">Do you like our app?</string>
```

For translations, right-click the `strings/` folder in Android Studio → **Open Translations Editor**. Default locales: English (`values/`), German (`values-de/`), Spanish (`values-es/`).

### 2) Change app icon

Replace each `ic_launcher.png` (and `ic_launcher_round.png`, `ic_launcher_foreground.png`) under:

- `mipmap-mdpi/` (48×48)
- `mipmap-hdpi/` (72×72)
- `mipmap-xhdpi/` (96×96)
- `mipmap-xxhdpi/` (144×144)
- `mipmap-xxxhdpi/` (192×192)

Tip: use [Android Asset Studio – Launcher Icon Generator](https://jgilfelt.github.io/AndroidAssetStudio/icons-launcher.html). The adaptive icon also supports a `<monochrome>` channel for Themed Icons on Android 13+ (already wired in `mipmap-anydpi-v26/`).

### 3) Change splash image

Replace `app/src/main/res/drawable/splash.gif` (animated GIF supported). Adjust size via `Config.SCALE_SPLASH_IMAGE` (0–100 percent).

### 4) Change colors & theme

Edit:

- `res/values/colors.xml` — primary, accent, status bar
- `res/values/themes.xml` & `styles.xml` — Material theme

### 5) Inject CSS / JavaScript

- `app/src/main/assets/custom.css` — injected into every page
- `app/src/main/assets/custom.js` — injected into every page

### 6) Adjust text sizes

Edit `res/values/dimens.xml` for offline screen and dialog text scaling.

---

## JavaScript Native Bridge

When `Config.ENABLE_NATIVE_BRIDGE = true` (default), the WebView exposes a `window.IBA` object that web pages can call directly. All methods marshal to the UI thread automatically and hold a `WeakReference` to the Activity to avoid leaks.

### API

#### UI helpers

| Method                   | Signature                 | Effect                                       |
| ------------------------ | ------------------------- | -------------------------------------------- |
| `IBA.toast(message)`     | `(string) → void`         | Short Toast.                                 |
| `IBA.longToast(message)` | `(string) → void`         | Long Toast.                                  |
| `IBA.share(url, title)`  | `(string, string) → void` | Open the system share sheet.                 |
| `IBA.openExternal(url)`  | `(string) → void`         | Open URL in external browser/app.            |
| `IBA.copy(text)`         | `(string) → void`         | Copy text to clipboard.                      |
| `IBA.vibrate(ms)`        | `(number) → void`         | Vibrate (clamped to ≤ 5000 ms).              |

#### Device & WebView

| Method               | Signature       | Effect                                                                            |
| -------------------- | --------------- | --------------------------------------------------------------------------------- |
| `IBA.deviceInfo()`   | `() → string`   | Returns JSON with platform, sdkInt, model, locale, timezone, packageName, etc.    |
| `IBA.locale()`       | `() → string`   | BCP-47 locale tag (e.g. `"ar-DZ"`, `"en-US"`).                                    |
| `IBA.firebaseToken()`| `() → string?`  | Cached FCM device token (or `null` if push isn't initialized).                    |
| `IBA.reload()`       | `() → void`     | Reload the WebView.                                                               |
| `IBA.close()`        | `() → void`     | Finish the host Activity.                                                         |

#### Native UX

| Method                                                | Signature                                  | Effect                                                                                                  |
| ----------------------------------------------------- | ------------------------------------------ | ------------------------------------------------------------------------------------------------------- |
| `IBA.requestBiometric()`                              | `() → void`                                | Show biometric prompt. Result delivered to `window.onBiometricResult(success, errorCode, message)`.     |
| `IBA.saveBase64ImageToGallery(dataUrl, displayName)`  | `(string, string) → boolean`               | Save a base64 image (e.g. `"data:image/png;base64,…"`) to the user's Pictures/IBA folder.               |
| `IBA.startVoiceSearch()`                              | `() → void`                                | Launch the speech-to-text picker. Result delivered to `window.onVoiceResult(text)`.                     |

#### Encrypted storage

| Method                  | Signature                | Effect                                                                       |
| ----------------------- | ------------------------ | ---------------------------------------------------------------------------- |
| `IBA.secureSet(k, v)`   | `(string, string) → void`| Persist a value in `EncryptedSharedPreferences`.                             |
| `IBA.secureGet(k)`      | `(string) → string?`     | Read it back. Returns `null` if missing.                                     |
| `IBA.secureRemove(k)`   | `(string) → void`        | Remove a key.                                                                |

### Example (web side)

```html
<button onclick="IBA.toast('Hello from web')">Toast</button>
<button onclick="IBA.share(location.href, document.title)">Share</button>
<button onclick="IBA.vibrate(120)">Buzz</button>
<button onclick="IBA.requestBiometric()">Unlock</button>
<button onclick="IBA.startVoiceSearch()">🎙️ Speak</button>

<script>
  // Auth flow via biometric
  window.onBiometricResult = (success, code, message) => {
    if (success) loadDashboard(); else alert('Auth failed: ' + message);
  };

  // Voice search → fill an input
  window.onVoiceResult = (text) => { document.querySelector('#search').value = text; };

  // Device introspection
  if (window.IBA) {
    const info = JSON.parse(IBA.deviceInfo());
    console.log('Running on Android', info.release, '-', info.model);
    console.log('Dark mode:', info.darkMode, '| Locale:', IBA.locale());
  }

  // Persist session token securely
  IBA.secureSet('session', sessionToken);
  const restored = IBA.secureGet('session');
</script>
```

> Security note: JavaScript interfaces can be invoked by _any_ frame loaded in the WebView. Disable `ENABLE_NATIVE_BRIDGE` for sites you don't fully trust.

---

## Push Notifications

### OneSignal

1. Create an app at [onesignal.com](https://onesignal.com).
2. Open `app/build.gradle` and replace:
   ```groovy
   def onesignalAppId = '"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"'
   ```
3. Set `Config.PUSH_ENABLED = true;`.
4. Send a notification with `additionalData` containing `url` to deep-link.

### Firebase Cloud Messaging

1. Create a project at [console.firebase.google.com](https://console.firebase.google.com).
2. Add an Android app with applicationId `com.iba.webview` (or change in `app/build.gradle`).
3. Download `google-services.json` and place it in `app/`.
4. Set `Config.FIREBASE_PUSH_ENABLED = true;`.
5. Optional: set `Config.firebasechanneltopic = "news";` to auto-subscribe.

### Pushwoosh

1. Register at [pushwoosh.com](https://www.pushwoosh.com).
2. Set `<string name="pushwoosh_app_id">XXXXX-XXXXX</string>` in `strings.xml`.
3. Set `Config.PUSHWOOSH_ENABLED = true;`.

> ⚠ Only enable **one** push provider at a time to avoid duplicate notifications.

---

## Ads Integration

### AdMob (default)

1. Replace test IDs in `strings.xml`:
   ```xml
   <string name="admob_app_id">ca-app-pub-XXXXXX~XXXXXX</string>
   <string name="banner_footer">ca-app-pub-XXXXXX/XXXXXX</string>
   <string name="interstitial_full_screen">ca-app-pub-XXXXXX/XXXXXX</string>
   ```
2. Toggle in `Config.java`:
   ```java
   public static final boolean SHOW_BANNER_AD       = true;
   public static final boolean SHOW_FULL_SCREEN_AD  = true;
   public static final int     SHOW_AD_AFTER_X      = 5;
   ```

### Facebook Audience Network

- Replace `facebook_banner_footer` and `facebook_interstitial_full_screen` IDs.
- Set `Config.USE_FACEBOOK_ADS = true;`.

---

## In-App Billing

The app integrates **Google Play Billing v7**:

- On launch, queries past purchases via `queryPurchaseHistoryAsync`.
- Stores purchase state in `SharedPreferences` (`purchase_prefs`).
- When a non-consumable is purchased, sets `HIDE_ADS_FOR_PURCHASE = true` automatically.

Customize the SKU and consumable flag in `MainActivity.java` (search for `BillingClient.newBuilder`).

---

## Deep Linking

### App Links (HTTPS)

Edit the `<intent-filter>` in `AndroidManifest.xml`:

```xml
<data android:host="www.your-webview-site-example.com"
      android:scheme="https" />
```

### Custom Scheme

Edit `<string name="deeplinking">IBA</string>` in `strings.xml`. Then `iba://path/to/page` will open the app.

### From a push payload

Send a payload with `url`:

```json
{
  "to": "/topics/news",
  "data": { "url": "https://www.example.org/promo" }
}
```

---

## Security Hardening

This template ships with the modern Android security defaults:

| Layer               | Setting                                                                                                  |
| ------------------- | -------------------------------------------------------------------------------------------------------- |
| Cleartext traffic   | **Disabled** by default (`android:usesCleartextTraffic="false"`)                                         |
| Network security    | `res/xml/network_security_config.xml` — HTTPS only, debug-only user CAs                                  |
| SSL handling        | `Config.BLOCK_SELF_SIGNED_AND_FAULTY_SSL_CERTS`                                                          |
| Backup              | Selective rules in `backup_rules.xml` + `data_extraction_rules.xml` (excludes purchase prefs and tokens) |
| Encrypted storage   | `SecurePreferences` wraps `EncryptedSharedPreferences` (AES256-GCM)                                      |
| Screenshot blocking | `Config.PREVENT_SCREEN_CAPTURE` toggles `FLAG_SECURE` app-wide                                           |
| Root detection      | `Config.BLOCK_ROOTED_DEVICES` (best-effort heuristic)                                                    |
| File:// origin      | `setAllowFileAccessFromFileURLs(false)` + `setAllowUniversalAccessFromFileURLs(false)`                   |
| ProGuard / R8       | Full mode, `app/proguard-rules.pro` covers all SDKs                                                      |

To allow plain HTTP for a specific domain (e.g. localhost during development), add an entry to `network_security_config.xml`.

---

## Permissions

The manifest declares the full permission set. **Remove what you don't need** to comply with Google Play policies:

| Permission                                                                     | Used by                     |
| ------------------------------------------------------------------------------ | --------------------------- |
| `INTERNET`, `ACCESS_NETWORK_STATE`                                             | All web traffic             |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `ACCESS_BACKGROUND_LOCATION` | Geolocation API             |
| `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_LOCATION`                           | `GPSService`                |
| `READ_EXTERNAL_STORAGE` (≤ API 32), `WRITE_EXTERNAL_STORAGE` (≤ API 29)        | Legacy file access          |
| `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`, `READ_MEDIA_AUDIO`                    | Android 13+ media access    |
| `CAMERA`                                                                       | Camera capture / QR scanner |
| `RECORD_AUDIO`, `MODIFY_AUDIO_SETTINGS`                                        | WebRTC microphone           |
| `BILLING`                                                                      | In-app purchases            |
| `POST_NOTIFICATIONS`                                                           | Android 13+ notifications   |
| `AD_ID`                                                                        | AdMob advertising ID        |
| `NFC`                                                                          | NFC tag reading             |
| `webkit.PermissionRequest`                                                     | WebView permission API      |

After removing a permission entry from the manifest, **also** flip the matching `Config` flag (`requireLocation`, `requireStorage`, `requireCamera`, `requireRecordAudio`) to `false`.

---

## Testing

### Unit tests (JVM, fast)

```bash
./gradlew testDebugUnitTest
```

Includes:

- `ConfigTest` — invariants on URL, orientations, ranges
- `NativeBridgeTest` — JS-facing API surface stays stable

### Instrumented tests (device required)

```bash
./gradlew connectedDebugAndroidTest
```

### Lint

```bash
./gradlew lintDebug
```

HTML report: `app/build/reports/lint-results-debug.html`.

---

## Continuous Integration

`.github/workflows/android.yml` runs on every push and pull request:

- Lint (uploads HTML report as an artifact)
- Unit tests
- Debug APK build (uploaded as `app-debug.apk`)
- Release AAB build on `main` (uploaded as `app-release.aab`)

Dependabot (`.github/dependabot.yml`) opens weekly PRs for outdated Gradle and GitHub Action dependencies.

---

## Troubleshooting

| Problem                                                                        | Fix                                                                                                          |
| ------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ |
| Build fails with `Could not resolve com.facebook.android:audience-network-sdk` | Ensure `mavenCentral()` and `https://jitpack.io` are in `build.gradle` repositories.                         |
| `AAPT: error: resource style/Theme.App.Starting not found`                     | Check `themes.xml` defines `Theme.App.Starting`; Splash API requires `androidx.core:core-splashscreen`.      |
| `Manifest merger failed: namespace ...`                                        | Make sure `applicationId` in `app/build.gradle` matches `package_name` in `google-services.json`.            |
| Splash GIF not animating                                                       | Confirm `pl.droidsonroids.gif:android-gif-drawable` is included; rebuild.                                    |
| Push notifications never arrive                                                | Verify `google-services.json` matches your applicationId; enable only one provider.                          |
| Geolocation prompt not shown                                                   | Set `Config.requireLocation = true` and ensure the permission is in the manifest.                            |
| `Mixed Content` blocked                                                        | Already permitted via `setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW)`; tighten if your site is HTTPS-only. |
| App rejected by Play Store for sensitive permissions                           | Remove `READ/WRITE_EXTERNAL_STORAGE` and `BACKGROUND_LOCATION` if unused; flip Config flags to match.        |
| Cookies don't persist                                                          | Enable `Config.MANUAL_COOKIE_SYNC` and lower `COOKIE_SYNC_TIME`.                                             |
| Login button does nothing                                                      | Add the OAuth domain to `GOOGLE_LOGIN_HELPER_TRIGGERS` / `FACEBOOK_LOGIN_HELPER_TRIGGERS`.                   |

---

## Author

**Idriss Boukmouche** ([@TerminalDZ](https://github.com/TerminalDZ))

📧 boukemoucheidriss@gmail.com

---

## License

All third-party libraries retain their respective licenses (Apache 2.0 / MIT). The original WebViewGold license assets and references have been removed; this distribution is provided as a clean template under the IBA brand.

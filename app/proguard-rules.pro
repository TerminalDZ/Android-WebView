# =============================================================================
#  IBA WebView — ProGuard / R8 rules
# =============================================================================
# Documented at https://www.guardsquare.com/manual/configuration/usage
# Keep stack traces useful in production.
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature,*Annotation*,Exceptions,InnerClasses,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-renamesourcefileattribute SourceFile

# -----------------------------------------------------------------------------
#  App package
# -----------------------------------------------------------------------------
-keep class com.iba.myappname.** { *; }
-keep class com.iba.myappname.Config { *; }
-keep class com.iba.myappname.BuildConfig { *; }

# Keep classes referenced from JavaScript bridge
-keepclassmembers class com.iba.myappname.bridge.NativeBridge {
    @android.webkit.JavascriptInterface <methods>;
}

# -----------------------------------------------------------------------------
#  WebView
# -----------------------------------------------------------------------------
-keep public class * extends android.webkit.WebViewClient
-keep public class * extends android.webkit.WebChromeClient
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# -----------------------------------------------------------------------------
#  AndroidX & Material
# -----------------------------------------------------------------------------
-dontwarn androidx.**
-dontwarn com.google.android.material.**

# -----------------------------------------------------------------------------
#  Firebase
# -----------------------------------------------------------------------------
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# -----------------------------------------------------------------------------
#  OneSignal
# -----------------------------------------------------------------------------
-keep class com.onesignal.** { *; }
-dontwarn com.onesignal.**

# -----------------------------------------------------------------------------
#  Pushwoosh
# -----------------------------------------------------------------------------
-keep class com.pushwoosh.** { *; }
-dontwarn com.pushwoosh.**

# -----------------------------------------------------------------------------
#  AdMob and Facebook Audience Network
# -----------------------------------------------------------------------------
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

# -----------------------------------------------------------------------------
#  Google Play Billing
# -----------------------------------------------------------------------------
-keep class com.android.billingclient.api.** { *; }
-dontwarn com.android.billingclient.api.**

# -----------------------------------------------------------------------------
#  Glide
# -----------------------------------------------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** { **[] $VALUES; public *; }
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder { *; }
-keepclassmembers class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
   *** rewind();
}

# -----------------------------------------------------------------------------
#  Gson
# -----------------------------------------------------------------------------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# -----------------------------------------------------------------------------
#  ZXing (QR scanner)
# -----------------------------------------------------------------------------
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**
-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.journeyapps.barcodescanner.**

# -----------------------------------------------------------------------------
#  Misc — keep enums, parcelables, native methods
# -----------------------------------------------------------------------------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepclasseswithmembernames class * {
    native <methods>;
}

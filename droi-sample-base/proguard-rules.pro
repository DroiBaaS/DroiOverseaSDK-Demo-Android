# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Keep public classes and methods.

# Keep public classes and methods.
-dontwarn com.droi.**
-keep class  com.droi.nativeads.*{*;}
-keep class  com.droi.common.**{*;}
-keep class  com.droi.network.**{*;}

-keepclassmembers class com.droi.** { public *; }
-keep public class com.droi.**
-keep public class android.webkit.JavascriptInterface {}

-keep class com.droi.mobileads.CustomEventBannerAdapter {*;}
-keep class com.droi.mobileads.CustomEventInterstitialAdapter {*;}
-keep class com.droi.mobileads.CustomEventNativeAdapter {*;}

# Explicitly keep any custom event classes in any package.
-keep class * extends com.droi.mobileads.CustomEventBanner {}
-keep class * extends com.droi.mobileads.CustomEventInterstitial {}
-keep class * extends com.droi.nativeads.CustomEventNative {}

# Keep methods that are accessed via reflection
-keepclassmembers class ** { @com.droi.common.util.ReflectionTarget *; }

# Support for Android Advertiser ID.
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

# Filter out warnings that refer to legacy Code.
-dontwarn org.apache.http.**
-dontwarn com.droi.volley.toolbox.**

# Support for Google Play Services
# http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}



#inmobi
-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
}
# skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
# skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**


#facebook
-keep class com.facebook.** { *; }
-keep interface com.facebook.** { *; }
-dontwarn com.facebook.**

#admob
-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.** { *;}
-keep class com.cmcm.adsdk.nativead.AdmobNativeLoader{
      <fields>;
      <methods>;
}
-keep public class com.google.android.gms.ads.**{
    public *;
}
# For old ads classes
-keep public class com.google.ads.**{
    public *;
}
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontshrink
-keep class com.hjq.permissions.**{*;}
-keep public class * implements android.os.Parcelable {
    public *;
}
-keep class com.applet.mylibrary.**{*;}
-keep class com.applet.module.**{*;}
-keep class io.agora.**{*;}
-keep class com.applet.agora.** {*;}
-keep class com.applet.mqtt.MqttClientService{*;}
-keep class com.applet.mqtt.MyFirebaseMessagingService{*;}
-keep class org.eclipse.paho.client.mqttv3.**{*;}
-keep class org.eclipse.paho.client.mqttv3.*$*{*;}
-keep class org.eclipse.paho.client.mqttv3.logging.JSR47Logger{*;}
-keep class com.applet.image_browser.view.GPreviewActivity{*;}
-keep class com.alivc.**{*;}
-keep class com.aliyun.**{*;}
-keep class com.cicada.**{*;}
-dontwarn com.alivc.**
-dontwarn com.aliyun.**
-dontwarn com.cicada.**
-keep class com.adjust.sdk.**{ *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.**{ *; }
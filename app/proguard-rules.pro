-keep class com.fingpay.microatmsdk.data.** {
    <fields>;
    public <methods>;
}
-keep class com.fingpay.microatmsdk.** { *; }


-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-ignorewarnings

-keepattributes Signature
-keepattributes Annotation
-dontwarn sun.misc.**



-dontwarn com.mosambee.**
-repackageclasses 'myobfuscatedmatm'
-allowaccessmodification
-keep class * {
    public private *;
}

-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }

-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }
-dontwarn org.xmlpull.v1.**

-dontusemixedcaseclassnames
-dontoptimize
-keep class android.content.res.XmlResourceParser
-keep class org.apache.http.client.HttpClient
-keep class org.xmlpull.v1.XmlPullParser
-dontwarn android.content.res.**


# Razorpay ProGuard rules
-keepclassmembers class com.razorpay.** {
    *;
}
-dontwarn com.razorpay.**
-keep class com.razorpay.** { *; }
-keep interface com.razorpay.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn android.net.http.AndroidHttpClient
-dontwarn org.apache.http.**
# Add Volley classes to the ProGuard keep list
-keep class com.android.volley.** { *; }

# Add GSON classes to the ProGuard keep list if you're using GSON for parsing
-keep class com.google.gson.** { *; }

# Add OkHttp classes to the ProGuard keep list if you're using OkHttp as the HTTP client
-keep class com.squareup.okhttp.** { *; }

# Add OkIO classes to the ProGuard keep list if you're using OkHttp as the HTTP client
-keep class okio.** { *; }
-dontwarn org.apache.http.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

#for Data Store

-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}
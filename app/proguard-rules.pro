# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/q/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
 # For communication with AdColony's WebView



 -keepclassmembers class * {
     @android.webkit.JavascriptInterface <methods>;
 }

-dontwarn android.app.Activity


-ignorewarnings


 -keep class * {
     public private *;
}

-keep class com.audiolaby.controller.** { *; }



#-keepnames class * implements java.io.Serializable
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
#
#
#-keep class com.activeandroid.** { *; }
#-keep class com.activeandroid.**.** { *; }
#-keep class * extends com.activeandroid.Model
#-keep class * extends com.activeandroid.serializer.TypeSerializer
#-keep public class * extends com.activeandroid.ActiveRecordBase
#
#-keepattributes Column
#-keepattributes Table
#-keepattributes *Annotation*
#-keepclasseswithmembers class * { @com.activeandroid.annotation.Column <fields>; }
#


#-keep class com.activeandroid.** { *; }
#-keep public class my.app.database.**{ *; }
#-keepattributes Column
#-keepattributes Table
##-keep 'public class java.package.** { *; }
#-keepattributes '*Annotation*'




#-keep class com.activeandroid.** { *; }
#-keep class com.activeandroid.**.** { *; }
#-keep class * extends com.activeandroid.Model
#-keep class * extends com.activeandroid.serializer.TypeSerializer

#-keep public class com.activeandroid.**
#-keep public class * extends com.activeandroid.ActiveRecordBase
#-keepattributes Column
#-keepattributes Table
#-keepattributes *Annotation*


-keep class com.startapp.** {
      *;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**
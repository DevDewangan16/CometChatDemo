
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# CometChat specific rules
-keep class com.cometchat.** { *; }
-dontwarn com.cometchat.**

# Keep all model classes
-keep class * extends com.cometchat.chat.models.** { *; }

# Keep callback listeners
-keep class * implements com.cometchat.chat.core.CometChat$CallbackListener { *; }

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
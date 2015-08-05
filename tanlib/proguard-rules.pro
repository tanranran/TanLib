# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\androidsdk\sdk/tools/proguard/proguard-android.txt
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

-ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5               #指定代码的压缩级别
-dontusemixedcaseclassnames         #包明不混合大小写
-dontskipnonpubliclibraryclasses    #不去忽略非公共的库类jar
-dontoptimize                       #优化  不优化输入的类文件
-dontpreverify                      #预校验
-verbose                            #混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keepattributes *Annotation*        #保护注解

-dontwarn android.support.v4.**     #缺省proguard 会检查每一个引用是否正确，但是第三方库里面往往有些不会用到的类，没有正确引用。如果不配置的话，系统就会报错。
-dontwarn android.os.**
-keep class android.support.v4.** { *; }        # 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.widget
-keep class com.baidu.**   {*;}        #百度地图
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclassmembers class * extends android.app.Activity { #所有activity的子类不要去混淆
   public void *(android.view.View);
}

#mapping.txt
#表示混淆前后代码的对照表，这个文件非常重要。如果你的代码混淆后会产生bug的话，
#log提示中是混淆后的代码，希望定位到源代码的话就可以根据mapping.txt反推。
#dump.txt
#描述apk内所有class文件的内部结构。
#seeds.txt
#列出了没有被混淆的类和成员。
#usage.txt
#列出了源代码中被删除在apk中不存在的代码。

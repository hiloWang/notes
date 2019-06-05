# AndroidStudio NDK 开发(OLD)

1. 徒手编写Android.mk和Application.mk文件
2. 使用javah命令生成头文件，比如`javah com.ztiany.sample1.JNIObj`，可以直接对.java文件使用此命令
3. 使用nkd-build命令生成.so库，把生成的文件复制到jniLibs目录下

>注意在gradle.properties下添加`android.useDeprecatedNdk=true`
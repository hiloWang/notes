# AndroidStudio NDK 开发

使用AndroidStudio2.2及更高版本，这个版本增强了C++的开发能力，能够使用 ndk-build 或 CMake 去编译和调试项目里的 C++ 代码，
此时AndroidStudio用于构建原生库的默认工具是 CMake，但是仍然支持使用ndkBuild


## Gradle配置

配置好Android.md和Application.mk文件，然后编写好相关的c/c++代码


```
android {
    compileSdkVersion 26
    buildToolsVersion "26.0.1"

    defaultConfig {
        applicationId "com.ztiany.sample2"
        minSdkVersion 15
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"

        externalNativeBuild {
            ndkBuild {
                arguments "NDK_APPLICATION_MK:=src/main/jni/Application.mk"
                cFlags "-DTEST_C_FLAG1", "-DTEST_C_FLAG2"
                cppFlags "-DTEST_CPP_FLAG2", "-DTEST_CPP_FLAG2"
                abiFilters "armeabi-v7a", "armeabi", "x86"
            }
        }
    }
    
    externalNativeBuild {
        ndkBuild {
            path "src/main/jni/Android.mk"
        }
    }
```

- `path`用来指定 Android.mk 的路径
- `arguments`用来指定 Application.mk 的路径
- `abiFilters`用来指定生成哪些平台的 .so 文件
- `cFlags` 和 `cppFlags` 用来设置环境变量

关于externalNativeBuild的详细配置可以参考

- [添加本地代码](https://developer.android.com/studio/projects/add-native-code.html)
- [GoogleSamples/Android-Ndk/NdkBuild](https://github.com/googlesamples/android-ndk/blob/master-ndkbuild/hello-jni/build.gradle)

如上配置，Android Studio 会检查 app/src/main/jni 目录下是否有 C 或 C++ 代码, 如果有, 就根据 build.gradle 里的配置调用 CMake 或 ndk-build 去编译它
运行项目即可自动生成并打包.so文件了。
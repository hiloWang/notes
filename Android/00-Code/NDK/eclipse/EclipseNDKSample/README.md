# EclipseNDK开发流程和说明

## 流程

1. 在java代码中声明一个native方法
2. 在Eclipse的工程目录下创建一个文件夹，名称是jni
3. 编写c代码，按照规编写(可以使用javah命令快速生成头文件)
4. 配置Android.mk文件
5. 配置Application.mk文件
6. ndk-build.cmd指令编译c代码
7. 生成一个.so文件的动态链接库
8. 在java代码中，加载动态链接库到JVM虚拟机

注意：

- 如果c代码报错，可以右键项目：properties->C/C++ General->Paths and Symbols->Includes，添加ndk目录下的include目录，比如`H:\dev_tools\android-ndk-r9b\platforms\android-19\arch-arm\usr\include`
- 步骤4和步骤5可是使用右键->Android Tools->Add Native Support代替

## 构建篇

### Flutter resolve dependencies 很慢

切换到 Android 工程，去掉无用的测试框架，添加国内代理：

```groovy
    repositories {
        maven{ url 'https://maven.aliyun.com/repository/google'}
        maven{ url 'https://maven.aliyun.com/repository/gradle-plugin'}
        maven{ url 'https://maven.aliyun.com/repository/public'}
        maven{ url 'https://maven.aliyun.com/repository/jcenter'}
        google()
        jcenter()
    }
```

### Waiting for another flutter command to release the startup lock

解决方法：

1. 打开flutter的安装目录/bin/cache/ 
2. 删除lockfile文件 
3. 重启AndroidStudio

### Flutter 卡在 package get 的解决办法

替换国内镜像：

linux：

```
export PUB_HOSTED_URL=https://pub.flutter-io.cn
export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
```

Windows 添加下面环境变量：

```
PUB_HOSTED_URL=https://pub.flutter-io.cn
FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
```

具体参考 [Using Flutter in China](https://flutter.dev/community/china)
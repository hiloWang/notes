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
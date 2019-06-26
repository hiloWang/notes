# Android Log

## log 分类

Android 日志主要分为 kernel、radio、event、main 这四种 log。

**kernel log**：kernel log 属于 Linux 内核的 log ，可以通过读取 /proc/kmsg 或者通过串口来抓取。

```bash
# adb 获取 kernel log的命令如下（需要有root权限）
adb shell cat /proc/kmsg > kernel.log
```

**radio log**：抓取 Android RIL层 log，在调试 Android 通信方面的代码时，这个 Log 非常关键。

```bash
adb logcat -b radio > radio.log
```

**main log**：main log 和我们在 ide 里通过 DDMS 中看到的 log 是一致的。

```bash
adb logcat -b main >e://main.log
```

**event Log**：event log 属于 system log，会记录 App 运行的一些基本情况，记录在文件 `/system/etc/event-log-tags` 中。

```bash
# -v time 表示在 log 中加入每条 log 发生的时间
adb logcat -b event -v time > e://event.log
```

具体参考：

- [logcat 命令行工具](https://developer.android.com/studio/command-line/logcat?hl=zh-CN)。
- [Android EventLog含义](http://gityuan.com/2016/05/15/event-log/)

## 高性能日志库与日志上报

- [微信 mars-xlog](https://github.com/WeMobileDev/article/blob/master/%E5%BE%AE%E4%BF%A1%E7%BB%88%E7%AB%AF%E8%B7%A8%E5%B9%B3%E5%8F%B0%E7%BB%84%E4%BB%B6%20Mars%20%E7%B3%BB%E5%88%97%EF%BC%88%E4%B8%80%EF%BC%89%20-%20%E9%AB%98%E6%80%A7%E8%83%BD%E6%97%A5%E5%BF%97%E6%A8%A1%E5%9D%97xlog.md)
- [微信Mars——xlog使用全解析](https://juejin.im/post/586629b861ff4b006ba9daf6)
- [Logan](https://github.com/Meituan-Dianping/Logan)

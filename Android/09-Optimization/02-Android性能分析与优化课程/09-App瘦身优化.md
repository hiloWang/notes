[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 10 App瘦身优化

## 10.1 瘦身优化及Apk分析方案介绍

### APK 组成

- dex 相关
- 资源相关
- so 相关

### APK 分析工具

- APK Tool
- AndroidStudio
- nimbledroid.com
- android-classshark

## 10.2 代码瘦身实战

- 代码混淆
- 三方库处理
  - 统一功能相同的三方库，比如避免同时使用 Fresco 和 Glide
  - 选择更小的库，使用 gradle 插件 AndroidMethodsCount 统计
- 移除无用代码

## 10.3 资源瘦身实战

- 冗余资源移除
- 资源压缩
- 图片格式选择，webp
- 微信资源压缩方案

## 10.4 So瘦身实战

完美支持所有类型设备代价太大，方案：

- 仅使用一套兼容性最好的 abi
- 对于关键功能，保留较多个 abi 平台的 so， 但都放在 armeabi 目录下（可根据真实架构命名其后缀），然后运行时根据 cpu 架构动态选择加载最匹配的 so
- so 动态下载

## 10.5 瘦身优化模拟面试

1. 怎么降低 APK 包大小？

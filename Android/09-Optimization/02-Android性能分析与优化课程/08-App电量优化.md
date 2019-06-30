[国内Top团队大牛带你玩转Android性能分析与优化](https://coding.imooc.com/class/308.html)

# 9 App电量优化

## 9.1 电量优化介绍及方案选择

电量监控方案:

1. 线下:设置-->查看耗电量排行
2. `ACTION_BATTERY_CHANGED`
   1. 获取电池电量，充电状态，电池状态等信息
   2. 价值不大，针对手机整体耗电量，而非特定APP
   3. 实时性差，精度低
3. Battery Historian
   1. 功能强大，推荐使用
   2. 只能线下使用

## 9.2 Battery Historian实战分析

具体参考[battery-historian github](https://github.com/google/battery-historian/)

## 9.3 电量辅助监控实战

### 运行时能耗

利用 power_profile.xml 获取 Android 运行时的能耗

- `adb pull /system/framework/framework-res.apk`
- 反编译，xml --> power_profile

## 9.4 电量优化套路总结

- 网络相关
  - 控制网络请求时机和次数
  - 避免轮询
- 界面相关
  - 离开界面停止耗电活动
- 使用 JobScheduler

## 9.5 电量优化模拟面试

1. 怎么做电量测试
2. 有哪些有效的电量优化手段

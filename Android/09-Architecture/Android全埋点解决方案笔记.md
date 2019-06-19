# [《Android全埋点解决方案》](https://book.douban.com/subject/33400077/)

选择买点方案需要考虑的几点：

- 效率：不影响现有业务。
- 兼容性：Android SDK版本、Java 和 Kotlin、Lamdba、DataBinding、Fragment等等都需要考虑。
- 扩展性：业务的不断迭代，全自动采集和精细化采集控制力度的要求。

## 1 买点方案一：`$AppViewScreen`

**关键技术**：

- Application.registerActivityLifecycleCallbacks()
- 在 ActivityLifeCycleCallback 的 onResume 中统计页面打开

**采集粒度**：

- 类名
- 触发时间
- 页面 Title
- ...等

**细节完善**：在申请权限时，系统会开启新的对话框类型的 Activity 处理权限申请，无论是用户解决还是允许，对话框消失后，申请界面对应的 Activity 都会再次执行 onResume 方法，这会导致统计不准确(多统计了一次 PV)。解决方案是，在弹框之前先过滤掉当前 Activity 的 PV 统计（以当前 Activity 的 Class 标识），待权限申请结束后，再次恢复当前 Activity 的 PV 统计。

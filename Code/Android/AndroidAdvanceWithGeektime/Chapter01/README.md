# [Android开发高手课](https://time.geekbang.org/column/intro/142) 实践 01

>所属章节——崩溃优化（上）：关于“崩溃”那些事儿练习

## 实践：使用 Breakpad 来捕获一个 Native 崩溃实践

### 下载和编译 Breakpad

按照 [Breakpad](https://github.com/google/breakpad) 说明下载 Breakpad，如果 git 无法访问 `chromium.googlesource.com`（比如 443 错误），可能是因为设置了代理，先配置一下 git 即可:

```bash
# 下面 `192.168.4.12:8080` 不同的 VPN 可能不一致。
git config --local http.proxy 192.168.4.12:8080
```

整体步骤：

```bash
# 设置代理
git config --local http.proxy 192.168.4.12:8080

# 下载 depot_tools 工具
git clone https://chromium.googlesource.com/chromium/tools/depot_tools
# 解压后配置路径
export PATH=$PATH:/mnt/d/linux/depot_tools

# 如果没有安装 python 2.7.x
apt install python

# 下载 breakpad
fetch breakpad
# 配置和编译
./configure && make
```

### 分析崩溃

```bash
# 根据 minidump 文件生成堆栈跟踪log
./minidump_stackwalk a9663bf9-2eff-40ca-2346cf9d-f30b238d.dmp > crashLog.txt
```

得到信息如下：

```log
Operating system: Android
                  0.0.0 Linux 4.9.111 #1 SMP PREEMPT Wed Apr 10 02:52:24 CST 2019 aarch64
CPU: arm64
     8 CPUs

GPU: UNKNOWN

Crash reason:  SIGSEGV /SEGV_MAPERR
Crash address: 0x1000300020005
Process uptime: not available

Thread 0 (crashed)
 0  libnative-lib.so + 0x550
     x0 = 0x0001000300020005    x1 = 0x0000007fd55ce864
     x2 = 0x0000000012d995b0    x3 = 0x00000078b2a98e14
     x4 = 0x0000007fd55ce844    x5 = 0x0000000000000000
     x6 = 0x0000007fd55ce7e0    x7 = 0x00000078b5415c00
     x8 = 0x0000000000000003    x9 = 0x25fa764e80501f98
    x10 = 0x0000000000430000   x11 = 0x00000078b2e6d6d8
    x12 = 0x000000793b910510   x13 = 0x2d00dd85dff99863
    x14 = 0x000000793b511000   x15 = 0xffffffffffffffff
    x16 = 0x00000078967a253c   x17 = 0x0000007935c43cb4
    x18 = 0x0000000000000000   x19 = 0x00000078b5415c00
    x20 = 0x0000000000000000   x21 = 0x00000078b5415c00
    x22 = 0x0000007fd55ceb30   x23 = 0x00000078974e5d24
    x24 = 0x0000000000000004   x25 = 0x000000793bc805e0
    x26 = 0x00000078b5415ca0   x27 = 0x0000000000000001
    x28 = 0x0000007fd55ce860    fp = 0x0000007fd55ce860
     lr = 0x00000078b2d92fe4    sp = 0x0000007fd55ce820
     pc = 0x00000078967a2550
    Found by: given as instruction pointer in context

... 省略...
```

符号解析：用 ndk 中提供的 addr2line 来根据地址进行一个符号反解的过程：

```bash
# 输入命令，进入交互模式
D:\dev_tools\android-ndk-r13b-windows-x86_64\android-ndk-r13b\toolchains\aarch64-linux-android-4.9\prebuilt\windows-x 86_64\bin\aarch64-linux-android-addr2line.exe -f -C -e D:\codes\github\Programming-Notes\00-Code\Android\AndroidAdvanceWithGeektime\Chapter01\app\build\intermediates\transforms\mergeJniLibs\debug\0\lib\arm64-v8a\libnative-lib.so

# 输入地址，addr2line 会输出代码行号
0x550
Java_com_dodola_breakpad_MainActivity_makeCrash
D:\codes\github\Programming-Notes\00-Code\Android\AndroidAdvanceWithGeektime\Chapter01\app\src\main\cpp/native-lib.cpp:8
```

具体参考[AndroidAdvanceWithGeektime/Chapter01](https://github.com/AndroidAdvanceWithGeektime/Chapter01)

## 学习心得

1. Android 应用异常分类：Java 异常和 Native 异常
2. Java 异常崩溃使用 UncaughtExceptionHandler 监控
3. Native 异常使用 [Breakpad](https://chromium.googlesource.com/breakpad/breakpad/+/master) 进行监控
4. 一个完整的 Native 崩溃从捕获到解析要经历哪些流程，Native 崩溃的有哪些难点
5. 关于 Breakpad：
   1. 了解 Breakpad 是什么、Breakpad 能做什么
   2. 动手实践，编译 Breakpad，使用 Breakpad 监控 Native 异常
   3. 阅读 Breakpad 相关代码，并掌握其实现原理
6. 通过学习 [Android 平台 Native 代码的崩溃捕获机制及实现](https://mp.weixin.qq.com/s/g-WzYF3wWAljok1XjPoo7w?) 了解：
   1. Native 异常捕获框架
   2. 类 Unix 系统异常处理机制、如何捕获异常
7. 如何客观地衡量崩溃
   1. 如何统计 PV 崩溃率
   2. 启动崩溃率
   3. 重复崩溃率
   4. 了解什么是 [安全模式](https://mp.weixin.qq.com/s?__biz=MzUxMzcxMzE5Ng==&mid=2247488429&idx=1&sn=448b414a0424d06855359b3eb2ba8569&source=41#wechat_redirect)
8. 如何客观地衡量稳定性
   1. 线上 ANR 如何的上报与分析
   2. 异常率，除了常规崩溃和主动自杀外，实现对 `系统出现异常、断电、用户主动重启、被系统杀死、ANR` 等情况的统计
   3. 根据统计，得出异常率
9. 选择第三方异常上报服务

##  1 斜杠和反斜杠的通常用法

正斜杠，又称左斜杠，符号是"/";
反斜杠，也称右斜杠，符号是"\"。

- `/ `在windows系统中通常用来分隔 **命令行参数**，表示选项等。不能作为文件名。比如Windows中的dir命令：`dir /d`用于列出所有的文件夹
- 反斜杠`\`，在windows系统中用来表示目录。
- 而在unix/linux系统中，正斜杠`/`表示目录。
- 由于web遵循unix命名，所以在网址（URL）中，`/`表示目录。

##  2 在不同操作系统中的作用 

- 在Unix/Linux中，路径的分隔采用正斜杠"/"，比如"/home/hutaow"；
- 在Windows中，路径分隔采用反斜杠"\"，比如"C:\Windows\System"。
- Windows 用反斜杠（“\”）的历史来自 DOS，而 DOS 的另一个传统是用斜杠（“/”）表示命令行参数，比如`：dir  /s  /b shell32.dll`,既然 DOS 这边斜杠被占用了，只好找另外一个。那就是 \ 了
- 在 UNIX 环境中，用减号（“-”）和双减号（“--”）表示命令行参数。

## 3 在写路径时用法

在C/C++中` \ `是一种转义字符，`\\`表示一个`\`，`\n`表示换行一样。所以C/C++中的路径名：`C:\Program Files\Google\Chrome\Chrome.exe`C中应写为`filename=“C:\\Program Files\\Google\\Chrome\\Chrome.exe”`或`filename="C:/Program Files/Google/Chrome/Chrome.exe"`

- 在Windows中单独的`\`或者`/`代表当前存储设备的根目录
- 在unix与linux中，单独的`/`代表当潜藏存储设备的根目录，`\`不用于表示路径
- `..`代表当前目录的上一级目录
- cd.. 返回上一级目录
- cd / 返回根目录
- . 代表当前目录
/*
练习3：使用getenv()函数、putenv()函数，必要时可直接修改environ，来实现setenv()函数和unsetenv()函数。
此处的unsetenv()函数应检查是否对环境变量进行了多次定义，如果是多次定义则将移除对该变量的所有定义（glibc版本的unsetenv()函数实现了这一功能）。
*/
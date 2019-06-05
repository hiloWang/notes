#所有.c源文件，wildcard用于获取所有的.c文件
SOURCES=$(wildcard *.c)
#patsubst把.c后缀，替换成.o后缀
OBJECTS=$(patsubst %.c,%.o,$(SOURCES))



#递归展开式
#可以引用还没有定义的变量，展开是引用时展开
str2=$(str1)
str1=hello

#直接展开式
#必须引用定义好了的变量，定义之后就会展开
str3 := android
str4 := $(str3)
str5 := $(str1) world

#变量的值追加
str5 += hello

#自定义函数
myfun=$2 $1
#变量等于函数的执行结构
myfun_ret=$(call myfun,20,10)

test:
	@echo $(SOURCES)
	@echo $(OBJECTS)
	@echo $(str2)
	@echo $(str4)
	@echo $(str5)
	@echo $(myfun_ret)
	@echo $(call myfun,30,40)

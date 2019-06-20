#所有.c源文件，wildcard用于获取所有的.c文件
SOURCES=$(wildcard *.c)
#patsubst把.c后缀，替换成.o后缀
OBJECTS=$(patsubst %.c,%.o,$(SOURCES))

myapp:$(OBJECTS)
#自动化变量 $^表示所有依赖，$@表示目标
	gcc $^ -o $@

#通配符，比如：
#o:%.c -> main.o:main.c
#gcc -c $^ -o $@ -> gcc -c main.c -o main.o
%.o:%.c
	gcc -c $^ -o $@

.PHONY:clean
clean:
	rm -f *.o
	rm -f myapp

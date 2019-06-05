#三要素：目标，依赖，命令

#1. myapp目标（最终目标，第一行是最终目标）
#2. :后的文件，这些都是依赖
#3. 命令以TAB键开头，如何由依赖文件得到目标


myapp:main.o plus.o minus.o \
multi.o divi.o
	gcc main.o plus.o minus.o \
	multi.o divi.o -o myapp
main.o:main.c
	gcc -c main.c
plus.o:plus.c
	gcc -c plus.c
minus.o:minus.c
	gcc -c minus.c
multi.o:multi.c
	gcc -c multi.c
divi.o:divi.c
	gcc -c divi.c

#没有依赖，clean清除所有的.o中间文件，伪目标
.PHONY:clean
clean:
	rm -f *.o
	rm -f myapp
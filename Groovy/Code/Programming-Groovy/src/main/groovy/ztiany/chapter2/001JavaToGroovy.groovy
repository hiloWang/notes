package ztiany.chapter2

//--------------------
// 1  hello groovy  使用groovy打印 ho ho ho merry groovy
//--------------------

for (int i = 0; i < 3; i++) {
    print 'ha '
}
println 'merry groovy'

for (i in 0..2) {
    print 'ha '
}
println 'merry groovy'

//--------------------
// 2 实现循环的方式
//--------------------
0.upto(2) {//integer的upto方法 实现循环
    print "$it "//当闭包值由一个参数时，可以使用it引用这个参数
}
3.upto 5, {//参数可以不带括号哦
    print "$it "
}
//如果遍历从0开始，可以使用times
4.times {
    print "$it "
}
0.step 10 ,2 , {//使用step可以指定循环的目标数，但每次会跳过指定的数据
    print("$it ")
}








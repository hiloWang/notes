package ztiany.chapter18

/*
methodA接受一个实例，看上去像file。
其他两个方法内部实例化了一个FileWriter实例

Expando和Map 只能对methodA进行测试



 */
class ClassWithDependency {

    def methodA(val, file) {
        file.write "the value is $val"
    }

    def methodB(val) {
        def file = new FileWriter('output.txt')
        file.write( "The value is $val")
    }

    def methodC(val) {
        def file = new FileWriter('output.txt')
        file.write "the value is $val"
        file.close()
    }
}



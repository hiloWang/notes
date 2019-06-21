//========================================================================
// 实例1
//========================================================================
sample = "global";
function checkScope1() {
    sample = "local";//修改了全局变量
    mySample = "local";//声明了新的全局变量
    return [sample, mySample];
}
console.log(checkScope1());//->[ 'local', 'local' ]


//========================================================================
// 实例2，函数定义嵌套
//========================================================================
globalValue = "global value";
function checkGlobalValue() {
    globalValue = "local";

    function nested() {
        var globalValue = "nested scope";
        return globalValue;
    }

    console.log(globalValue);//->local
    return nested();
}
console.log(checkGlobalValue());//->nested scope


//========================================================================
// 实例3：声明提前
//========================================================================
var a = "abc";
function test(o) {
    console.log(a);//输出undefined，函数内变量声明提前
    var i = 0;
    if (typeof 0 === 'object') {
        var j = 0;
        for (var k = 0; k < 10; k++) {
            console.log(k);
        }
        console.log(k);
        var a = "bcd";
    }
    console.log(j);//合法，j已经定义了，只是没有初始化，输出undefined。
}
test(1);


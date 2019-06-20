//========================================================================
// 鸭子辩型编程思想
//========================================================================

//各种判断对象类的方法都有那样这样的问题，如果使用鸭子辩型编程思想则可以规避这样的问题，**不要关注对象的类是什么，而是关注对象能做什么**。

/**
 * 如果对象o实现了参数中声明的函数就返回true，检测规则为：
 *      1. 如果参数是字符串，就判断o中是否有一个函数的名称与之匹配
 *      2. 如果参数是对象，就要求o生命了对象中的所有可枚举方法
 *      3. 如果参数是函数，就替换为函数的原型，然后按照对象的方式检测
 * @param o
 * @returns {boolean}
 */
function quacks(o /*, ... */) {
    for (var i = 1; i < arguments.length; i++) {  //注意从arguments的第2个参数遍历
        var arg = arguments[i];
        switch (typeof arg) {
            case 'string':       // 如果参数是字符串，就检测o中是否有这个函数
                if (typeof o[arg] !== "function") {
                    return false;
                }
                console.log(1);
                continue;
            case 'function':
                // 如果参数是函数就是用它的prototype属性代替，检测函数原型上的对象方法
                arg = arg.prototype;
            // 会进入下面的case语句，并再次执行arg的typeof值
            case 'object':       // object: check for matching methods
                for (var m in arg) { // 遍历该参数中的每一个属性
                    if (typeof arg[m] !== "function") {// 不是方法就跳过
                        continue;
                    }
                    if (typeof o[m] !== "function") {//如果对象o中没有对应的方法就返回false
                        return false;
                    }
                }
        }
    }
    //运行到这里则说明o对象实现了所有的函数
    return true;
}

//========================================================================
// 测试
//========================================================================
var obj = {
    m1: function () {

    }
};
var methods = {
    m1: function (x) {
        return x * x;
    }
};
var add = function (x, y) {
    return x + y;
};

var result1 = quacks(obj, add);
var result2 = quacks(obj, methods);
var result3 = quacks(obj, "add");

console.log(result1);//->true
console.log(result2);//->true
console.log(result3);//->false


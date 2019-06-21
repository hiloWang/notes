//========================================================================
// 作为命名空间的函数
//========================================================================

/*
 作为命名空间的函数的应用：在函数中声明的变量在整个函数体内都是可见的(包括在嵌套的函数中)，在函数的外部是不可见的。
 一般情况下，无法在js中声明只在一个代码块内可见的变量，基于此原因，我们常常简单的定义一个函数
 作临时的命名空间，在这个命名空间内定义的变量都不会污染的全局命名空间。
 */


//========================================================================
// 实例
//========================================================================

/*
 特定场景下返回带补丁的extend版本：
 修复多数IE中的bug：如果o的属性拥有一个不可枚举的同名属性，则for/in循环不会枚举对象o的可枚举属性， 也就是说，对象o的toString属性不会被正确的处理。
*/

var extend = (function () { //此函数的返回值赋值给extend
    //修复前，检查是否存在bug
    for (var p in {toString: null}) {
        //代码执行到这里，说明for/in循环正确的工作并返回，则返回一个简单版本的函数
        return function extend(o) {
            for (var i = 1; i < arguments.length; i++) {
                var source = arguments[i];
                for (var prop in source) {
                    o[prop] = source[prop];
                }
            }
            return o;
        }
    }

    var protoprops = ['toString', 'valueOf', 'constructor', 'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable', 'toLocaleString'];
    /*
     代码执行到这里，说明for/in不会枚举对象的toString属性
     因此返回另一个版本的extends函数，这个函数显式测试Object.prototype中不可枚举的属性
     */
    return function patched_extend(o) {
        for (var i = 1; i < arguments.length; i++) {
            var source = arguments[i];
            //赋值所有可枚举的属性
            for (var prop in source) {
                o[prop] = source[prop];
            }
            //检查特殊名称的属性
            for (var j = 0; j < protoprops.length; j++) {
                prop = protoprops[j];
                if (source.hasOwnProperty(prop)) {
                    o[prop] = source[prop];
                }
            }
        }
        return o;
    };
}());

// 按照这种写法，patched_extend方法就不会污染到全局环境
var result = extend({}, {x: 1}, {y: 2, toString: 32});
console.log(result);//->{ x: 1, y: 2, toString: 32 }
console.log(result.toString);//->32
console.log(Object.prototype.toString.call(result));//->[object Object]



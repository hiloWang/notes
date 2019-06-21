//========================================================================
// 闭包
//========================================================================

/*
 函数的执行依赖变量的作用域，这个作用域是在函数定义时确定的，而不是函数调用是确定的，
 为了实现这种词法作用域，js函数对象的内部状态不仅包括函数的代码逻辑，还必需引用函数当前的作用域链。
 函数对象可以通过作用域链关联起来，函数体内部的变量都可以保存在函数作用域内，
 这种特性在计算机科学中称为闭包。

 维基百科：在计算机科学中，闭包（英语：Closure），又称词法闭包（Lexical Closure）或函数闭包（function closures），
 是引用了自由变量的函数。这个被引用的自由变量将和这个函数一同存在，即使已经离开了创造它的环境也不例外。
 所以，有另一种说法认为闭包是由函数和与其相关的引用环境组合而成的实体。
 闭包在运行时可以有多个实例，不同的引用环境和相同的函数组合可以产生不同的实例。
 */

var scope = "global scope";
function checkScope() {
    var scope = "local scope";

    function f() {
        return scope;
    }

    return f();
}

var result = checkScope();
console.log(result);//->local scope


//========================================================================
// 闭包的实现
//========================================================================
/*
 如果一个函数的局部变量是定义在cpu的栈中，那么当函数返回时它们就不存在了。
 但是js的采用作用域链，每一次函数调用都为之创建一个新的对象来保存局部变量，
 把这个对象添加到作用域链中。当函数返回就从作用域链中删除这个绑定变量的对象。
 如果不存在嵌套函数，也没有其他引用指向这个绑定的对象，它就会被当作垃圾回收掉，
 如果定义了嵌套函数，每个嵌套的函数都有各自对应的作用域链，并且这个作用域链指向
 一个变量绑定对象，但如果这些嵌套函数在外部函数中保存下来，那么它们也会和所指向
 的变量绑定对象一样当作垃圾被回收，当如果这个函数定义了嵌套函数，并将它作为返回值
 返回，或者存储在某处的属性里，这时就会有一个外部引用指向这个嵌套的函数，他就不会
 被当作垃圾回收，并且它所指向的变量绑定对象也不会当作垃圾回收。
 */
function checkScope2() {
    var scope = "local scope";

    function f() {
        return scope;
    }

    return f;
}

var result = checkScope2();
console.log(result());//->local scope


//========================================================================
// 实例
//========================================================================

var uniqueInteger = (function () {
    var counter = 0;
    return function () {
        return counter++;
    }
}());
console.log(uniqueInteger());//->0
console.log(uniqueInteger());//->1

function counter() {
    var n = 0;
    return {
        count: function () {
            return n++;
        },
        reset: function () {
            n = 0;
        }
    }
}
var c = counter();
console.log(c.count());//->0
console.log(c.count());//->1

//从技术角度来讲，可以将闭包合并为属性存储器方法getter和setter
function counter2(n) {
    return {
        get count() {
            return n++;
        },
        set count(value) {
            if (value >= n) {
                n = value;
            } else {
                throw Error("count can only be set to a larger value");
            }
        }
    }
}

var c = counter2(1000);
console.log(c.count);//->1000
console.log(c.count);//->1001


//========================================================================
// 闭包访问this和arguments
//========================================================================
/*
 1，闭包在外部函数里无法访问this，除非外部函数将this转存为一个变量：var self = this;
 2，闭包有自己的argument参数，所以闭包用样无法直接访问外部函数的参数数组
 */
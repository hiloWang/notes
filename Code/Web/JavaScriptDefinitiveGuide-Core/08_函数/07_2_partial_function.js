//========================================================================
// 不完全函数
//========================================================================
function array(a, n) {
    return Array.prototype.splice.call(a, n || 0);
}

//这个函数的实参传递到左边
function partialLeft(f/*, ...*/) {
    var args = arguments;
    return function () {
        var a = array(args, 1);
        a = a.concat(array(arguments));
        return f.apply(this, a);
    }
}

//这个函数的实参传递到右边
function partialRight(f/*, ...*/) {
    var args = arguments;
    return function () {
        var a = array(arguments);
        a = a.concat(array(args, 1));
        return f.apply(this, a);
    }
}
//这个函数的实参被用做模板，实参列表中的undefined值都被填充
function partial(f/*,...*/) {
    var arg = arguments;
    return function () {
        var a = array(arg, 1);
        var i = 0, j = 0;
        for (; i < a.length; i++) {
            if (a[i] === undefined) {
                a[i] = arguments[j++];
            }
        }
        //剩下的背部实参加入进去
        a = a.concat(array(arguments, j));
        return f.apply(this, a);
    }
}
var f = function (x, y, z) {
    return x * (y - z);
};

var left = partialLeft(f, 2)(3, 4); //2 * (3-4)
var right = partialRight(f, 2)(3, 4); //3 * (4-2)
var value = partial(f, undefined, 2)(3, 4);// 3 * (2-4)
console.log(left);//->-2
console.log(right);//->6
console.log(value);//->-6

//partialLeft、partialRight、partial被称为不完全函数，利用这种不完全函数可以编写一些有意思的代码

function sum(a, b) {
    return a + b;
}
var increment = partialLeft(sum, 1);//自增
var cubeRoot = partialRight(Math.pow, 1 / 3);//立方根
String.prototype.first = partial(String.prototype.charAt, 0);
String.prototype.last = partial(String.prototype.substr, -1, 1);
console.log(increment(1));//->2
console.log(cubeRoot(27));//->3
console.log("abc".first());//->a
console.log("abc".last());//->c

//利用不完全函数重写组织求平均数和标准差的代码
var data = [1, 1, 3, 5, 5];

var reduce = function (a, f, initial) {
    if (arguments.length > 2) {
        return a.reduce(f, initial);
    } else {
        return a.reduce(f);
    }
};//抽出array的reduce方法

var product = function (x, y) {
    return x * y;
};//积

var neg = partial(product, -1);//求负数
var square = partial(Math.pow, undefined, 2);//平方
var sqrt = partial(Math.pow, undefined, .5);//开平方根
var reciprocal = partial(Math.pow, undefined, -1);//倒数

var mean = product(reduce(data, sum), reciprocal(data.length));//平均值
console.log(mean);//->3


//========================================================================
//记忆：缓存函数的执行结果在函数式编程中称为 记忆(memorization)
//========================================================================

//返回带有记忆功能的函数
function memorize(f) {
    var cache = {};//将值保存在闭包中
    return function () {
        var key = arguments.length + Array.prototype.join.call(arguments, ",");
        if (key in cache) {
            return cache[key];
        } else {
            return cache[key] = f.apply(this, arguments);
        }
    }
}
//========================================================================
// 函数的参数与arguments
//========================================================================
//可选形参
function getProperNames(a, /*optional*/b) {
    if (b == null) {
        b = [];
    }
    for (var property in  a) {
        b.push(property);
    }
    return b;
}

var a = getProperNames({x: 1, y: 2});
var b = getProperNames(a, []);
console.log(b);


//检查参数的个数
function f(x, y, z) {
    if (arguments.length !== 3) {
        throw new Error("function f called with arguments.length must is 3");
    }
    return x + y + z;
}

//可变长参数
function max(/* ... */) {
    var max = Number.NEGATIVE_INFINITY;
    for (var i = 0, length = arguments.length; i < length; i++) {
        if (max < arguments[i]) {
            max = arguments[i];
        }
    }
    return max;
}
console.log(max(1, 2, 3));


//arguments中定义了callee和caller属性，在ES5中的严格模式下，访问这两个属性都会产生类型错误
//非严格模式下，callee代表正在执行的函数，caller是非标准的。代表调用正在执行函数的函数
//在严格模式下：arguments是一个保留字，无法使用arguments作为形参名，或者局部遍历名，也不能给arguments赋值
function testCallee() {
    console.log(arguments.callee);
    console.log(arguments.caller);
}
testCallee();


//========================================================================
// 参数过多时，对象属性作为实参
//========================================================================
function arrayCopy(/*array*/from, /*index*/from_start, /*array*/to, /*index*/to_start, /*integer*/length) {
    //logic fake
    for (var a in from) {
        to.push(a);
    }
    return to;
}
function easyCopy(args) {
    var result = arrayCopy(args.from, args.from_start || 0, args.to, args.to_start || 0, args.length);
    return result;
}
var a = [1, 2, 3, 4];
var result = easyCopy({from: a, from_start: 0, to: [], length: 4});
console.log("result: " + result);

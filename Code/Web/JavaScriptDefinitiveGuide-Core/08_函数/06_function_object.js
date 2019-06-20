//========================================================================
// 函数的属性、方法和构造函数
//========================================================================
//length
function testLength(a, b, c) {

}
var length = testLength.length;
console.log(length);//->3

//========================================================================
// apply方法和call方法
//========================================================================
var o = [1, 2, 3];
function applyFunc(a, b) {
    console.log("this: " + this);// o
    console.log(a);//->{x: 1}
    console.log(b);//->{y:2}]
}
applyFunc.apply(o, [{x: 1}, {y: 2}]);//o此时时指定调用函数的上下文
applyFunc.call(o, {x: 1}, {y: 2});//o此时时指定调用函数的上下文


//========================================================================
// bind方法
//========================================================================

function f1(y) {
    return this.x + y;
}
var o = {x: 1};
var g = f1.bind(o);
console.log(g(1));//->2


var sum = function (x, y) {
    return x + y;
}
var succ = sum.bind(null, 1);

console.log(succ(2));//->3
function f2(y, z) {
    return this.x + y + z;
}
var g = f2.bind({x: 1}, 2);//f2科里化，y被绑定尾2
console.log(g(3));//->6


//========================================================================
// es3版本模拟bind
//========================================================================
if (!Function.prototype.bind) {
    Function.prototype.bind = function (o/*, args*/) {
        var self = this;
        var boundArgs = arguments;
        return function () {
            var args = [], i;
            for (i = 1; i < boundArgs.length; i++) {
                args.push(boundArgs[i]);
            }
            for (i = 0; i < arguments.length; i++) {
                args.push(arguments[i]);
            }
            return self.apply(o, args);
        }
    }
}

//测试模拟的bind方法
Function.prototype.mockBind = function (o/*, args*/) {
    //this指向调用mockBind方法的函数，如下面的testMockBind
    var self = this;
    var boundArgs = arguments;
    return function () {
        var args = [], i;
        for (i = 1; i < boundArgs.length; i++) {//外函数的参数
            args.push(boundArgs[i]);
        }
        for (i = 0; i < arguments.length; i++) {//本函数的参数
            args.push(arguments[i]);
        }
        //把o绑定为this，调用原函数
        return self.apply(o, args);
    }
};

function testMockBind(a) {
    console.log(a);
    console.log(this);//->{ x: 1, y: 2, z: 2 }
    return this.x + this.y + a;
}

var newFunction1 = testMockBind.mockBind({x: 1, y: 2, z: 2}, 15);//上面a=15
var newFunction2 = testMockBind.mockBind({x: 1, y: 2, z: 2});//上面a=12
console.log(newFunction1(100));//->18，而这里的100被忽略
console.log(newFunction2(12));//->15
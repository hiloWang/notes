//========================================================================
// //类与构造函数：Range类的实现方式2
//========================================================================

//定义构造函数
function Range(from, to) {
    this.from = from;
    this.to = to;
}

//定义新的原形，并且给原形添加属性和方法
Range.prototype = {
    includes: function (x) {
        return this.from <= x && x <= this.to;
    },
    foreach: function (f) {
        for (var x = Math.ceil(this.from); x <= this.to; x++) f(x);
    },
    toString: function () {
        return "(" + this.from + "..." + this.to + ")";
    }
};

//创建一个Range类
var r = new Range(1, 3);
r.includes(2);
r.foreach(console.log);
console.log(r);//->{ from: 1, to: 3 }

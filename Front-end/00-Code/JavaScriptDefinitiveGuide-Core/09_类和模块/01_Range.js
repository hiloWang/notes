//========================================================================
//类与原型：Range类的实现方式1
//========================================================================

/**
 * 传入一个原型p，返回一个新的对象，该对象的__proto__指向p
 * @param p 原型
 * @returns {*} 新的对象
 */
function inherit(p) {
    if (p == null) {
        throw TypeError();
    }
    if (Object.create) {
        return Object.create(p);
    }
    var t = typeof p;
    if (t !== "object" && t !== "function") {
        throw TypeError();
    }

    function f() {
    }

    f.prototype = p;
    return new f();
}

// 工厂方法，返回一个Range实例
function range(from, to) {
    var r = inherit(range.methods);
    r.from = from;
    r.to = to;
    return r;
}

//range的所有实例都继承这些方法，methods将在inherit方法中作为新对象的原形
range.methods = {
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

var r = range(1, 3);
r.includes(2);
r.foreach(console.log);
console.log(r);//->{ from: 1, to: 3 }
console.log(r.constructor);//->[Function: Object] 即 Object


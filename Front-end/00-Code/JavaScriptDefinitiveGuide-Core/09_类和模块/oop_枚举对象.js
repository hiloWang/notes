//========================================================================
// 面向对象技术：枚举的实现
//========================================================================

//这个函数创建一个新的枚举类型，实参对象表示类的毎个实例的名字和值
//返冋值是一个构造函数，它标识这个新类
//注意，这个构造函数也会抛出异常：不能使用它来创建该类型的新实例
//返回的构造函数包含名/值对的映射表
//包括由值组成的数组，以及一个foreach()迭代器函数

function inherit(p) {
    if (p === null) {
        throw  TypeError();
    }
    if (Object.create) {//如果由create方法
        return Object.create(p);
    }
    var t = typeof p;
    if (t !== "object" && t !== "function") {//必需是对象或者函数对象
        throw TypeError();
    }

    function F() {
    }

    F.prototype = p;
    new new F();
}

function enumeration(namesToValues) {

    // 这个虚拟的构造函数是返回值(创建一个函数)
    var enumeration = function () {
        throw "Can't Instantiate Enumerations";
    };

    // 枚举值继承这个对象
    var proto = enumeration.prototype = {
        constructor: enumeration, // 标识类型
        toString: function () {
            return this.name;
        }, // Return name
        valueOf: function () {
            return this.value;
        }, // Return value
        toJSON: function () {
            return this.name;
        }    // For serialization
    };

    enumeration.values = [];  // 用以存储枚举对象的数组

    // 创建新类型的实例
    for (name in namesToValues) {         // 对于每一个枚举变量
        var e = inherit(proto);          // 创建一个对象代表它
        e.name = name;                   // 给予其名字
        e.value = namesToValues[name];   //和值
        enumeration[name] = e;           // 将其设置构造函数的一个属性
        enumeration.values.push(e);      // 并且将其只存储到数组中
    }

    // 一个遍历方法
    enumeration.foreach = function (f, c) {
        for (var i = 0; i < this.values.length; i++) f.call(c, this.values[i]);
    };

    // 返回这个新的类型
    return enumeration;
}

//========================================================================
// 面向对象技术：ES5 创建不可变对象
//========================================================================


// 这个构造函数用于创建一个Range对象，即使不使用new关键字也可以正常工作
function Range(from, to) {
    // 只读属性描述符
    var props = {
        from: {value: from, enumerable: true, writable: false, configurable: false},
        to: {value: to, enumerable: true, writable: false, configurable: false}
    };

    if (this instanceof Range)                // 当被当成构造函数调用时执行：
        Object.defineProperties(this, props);//给对象定义属性
    else                                      // 当被当成工厂方法使用时执行：
        return Object.create(Range.prototype/*创建Range对象*/, props/*属性描述符集合*/);
}

// 其他属性，直接使用defineProperties定义，不需要管理它们的可枚举性
Object.defineProperties(Range.prototype, {
    includes: {
        value: function (x) {
            return this.from <= x && x <= this.to;
        }
    },
    foreach: {
        value: function (f) {
            for (var x = Math.ceil(this.from); x <= this.to; x++) f(x);
        }
    },
    toString: {
        value: function () {
            return "(" + this.from + "..." + this.to + ")";
        }
    }
});


//========================================================================
// ES5 创建不可变对象，封装工具方法
//========================================================================
//冻结对象的属性
function freezeProps(o) {
    var props = (arguments.length == 1)              // 如果只有一个参数
        ? Object.getOwnPropertyNames(o)              //  使用对象的所有属性
        : Array.prototype.splice.call(arguments, 1); // 否则使用传入的属性

    props.forEach(function (n) { // 遍历需要配置的属性，修改其为只读不可配置
        //忽略不可配置的属性
        if (!Object.getOwnPropertyDescriptor(o, n).configurable) return;
        Object.defineProperty(o, n, {writable: false, configurable: false});
    });
    return o; //返回(链式调用)
}

// 如果对象的属性是可配置的，让其变得不可枚举
function hideProps(o) {
    var props = (arguments.length == 1)
        ? Object.getOwnPropertyNames(o)
        : Array.prototype.splice.call(arguments, 1);
    props.forEach(function (n) {
        if (!Object.getOwnPropertyDescriptor(o, n).configurable) return;
        Object.defineProperty(o, n, {enumerable: false});
    });
    return o;
}
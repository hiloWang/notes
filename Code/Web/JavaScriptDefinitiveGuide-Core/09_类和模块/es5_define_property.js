//========================================================================
// 面向对象技术：ES5 让属性变得不可枚举
//========================================================================

//定义不可枚举的属性
// 用函数包装代码，我们可以在函数作用域内定义变量，而不会污染到全局上下文
(function () {
    // 定义一个不可枚举的属性objectId，它可以被所有对象集成到
    Object.defineProperty(Object.prototype, "objectId", {
        get: idGetter,       // id 的 getter方法
        enumerable: false,   // 不可枚举
        configurable: false  // 不能删除，不能配置
    });

    // 当读取对象的objectId时，该方法将被调用
    function idGetter() {
        if (!(idprop in this)) {
            if (!Object.isExtensible(this)) //
                throw Error("Can't define id for nonextensible objects");
            Object.defineProperty(this, idprop, {         // 给对象分配唯一id
                value: nextid++,    // id
                writable: false,    // 只读
                enumerable: false,  // 不可枚举
                configurable: false // 不可配置
            });
        }
        return this[idprop];          // Now return the existing or new value
    };

    // 内部变量
    var idprop = "|**objectId**|";    // 假设不存在这个属性
    var nextid = 1;                   // 初始值

}());

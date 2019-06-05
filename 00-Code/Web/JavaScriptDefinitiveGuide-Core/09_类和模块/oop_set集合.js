//========================================================================
// 面向对象技术：实现Set集合。
//========================================================================

//定义构造函数
function Set() {
    this.values = {};     //该对象用于保持集合元素
    this.n = 0;           //集合有多少元素
    this.add.apply(this, arguments);  // 所有的参数作为变量加入
}

//定义add方法用于添加元素：添arguments的参数到这个set集合
Set.prototype.add = function () {
    for (var i = 0; i < arguments.length; i++) {
        var val = arguments[i];
        // 转换元素为一个String，作为key查找元素
        var str = Set._v2s(val);
        //判断一个对象是否有你给出名称的属性或对象。此方法无法检查该对象的原型链中是否具有该属性，该属性必须是对象本身的一个成员。
        if (!this.values.hasOwnProperty(str)) { // 如果不存在set中
            // 映射转换： string to value
            this.values[str] = val;
            // size自增
            this.n++;
        }
    }
    // 返回this用于支持链式调用
    return this;
};

//定义remove方法用以移除元素
Set.prototype.remove = function () {
    for (var i = 0; i < arguments.length; i++) {
        var str = Set._v2s(arguments[i]);
        if (this.values.hasOwnProperty(str)) {
            delete this.values[str];
            this.n--;
        }
    }
    return this;
};

//用于判断称号否存在某个元素
Set.prototype.contains = function (value) {
    return this.values.hasOwnProperty(Set._v2s(value));
};

// 返回Set的size
Set.prototype.size = function () {
    return this.n;
};

// 遍历set集合，在给定的上下文中调用传入的方法，参数为set中的每个元素
Set.prototype.foreach = function (f, context) {
    for (var s in this.values)                 // For each string in the set
        if (this.values.hasOwnProperty(s))    // Ignore inherited properties
            f.call(context, this.values[s]);  // Call f on the value
};

// 内部方法，用以将任何JavaScript值和对象用唯一的字符串对应起来
Set._v2s = function (val) {
    switch (val) {
        case undefined:
            return 'u';          // Special primitive
        case null:
            return 'n';          // values get single-letter
        case true:
            return 't';          // codes.
        case false:
            return 'f';
        default:
            switch (typeof val) {
                case 'number':
                    return '#' + val;    // Numbers get # prefix.
                case 'string':
                    return '"' + val;    // Strings get " prefix.
                default:
                    return '@' + objectId(val); // Objs and funcs get @
            }
    }

    // For any object, return a string. This function will return a different
    // string for different objects,
    // 对于任何一个对象，返回一个字符串，对于不同的对象这个函数将会返回一个不同的字符串。
    // 而对于同一个对象的多次调用，它总是返回相同的字符串
    // 为了做到这一点，给对象o创建了一个属性，在ES5中，这个属性是不可枚举且不可读的
    function objectId(o) {
        var prop = "|**objectid**|";   // 私有属性用于存储id
        if (!o.hasOwnProperty(prop))   // 如果对象没有id
            o[prop] = Set._v2s.next++; // 将下一个值分配给它
        return o[prop];                //返回对象的id
    }
};

Set._v2s.next = 100;    // 设置初始id 的值

//========================================================================
// 模拟定义Java类与类的继承
//========================================================================
var extend = (function () {
    for (var p in {toString: null}) {
        return function extend(o) {
            for (var i = 1; i < arguments.length; i++) {
                var source = arguments[i];
                for (var prop in source) {
                    o[prop] = source[prop];
                }
            }
            return o;
        };
    }
    return function patched_extend(o) {
        for (var i = 1; i < arguments.length; i++) {
            var source = arguments[i];
            // Copy all the enumerable properties
            for (var prop in source) {
                o[prop] = source[prop];
            }

            for (var j = 0; j < protoprops.length; j++) {
                prop = protoprops[j];
                if (source.hasOwnProperty(prop)) o[prop] = source[prop];
            }
        }
        return o;
    };
    var protoprops = ["toString", "valueOf", "constructor", "hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable", "toLocaleString"];
}());


//工厂方法，定义一个类
function defineClass(constructor, // 用以设置实例的属性的函数
                     methods,     //实例的方法，复制到原型中
                     statics)     // 类属性，复制到构造函数中
{
    if (methods) extend(constructor.prototype, methods);
    if (statics) extend(constructor, statics);
    // Return the class
    return constructor;
}

//创建一个SimpleRange类
var SimpleRange = defineClass(
    function (f, t) {
        this.f = f;
        this.t = t;
    },
    {
        includes: function (x) {
            return this.f <= x && x <= this.t;
        },
        toString: function () {
            return "(" + this.f + "..." + this.t + ")";
        }
    },
    {
        upto: function (t) {
            return new SimpleRange(0, t);
        }
    }
);

//创建对象并使用
var sr = new SimpleRange(1, 100);
console.log(sr.includes(3));//-> true
var sr2 = SimpleRange.upto(8);
console.log(sr2);//->{ f: 0, t: 8 }
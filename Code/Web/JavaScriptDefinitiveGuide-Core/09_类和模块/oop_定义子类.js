//========================================================================
// 面向对象技术：定义子类
//========================================================================


//继承p
function inherit(p) {
    if (p == null) throw TypeError();
    if (Object.create)
        return Object.create(p);
    var t = typeof p;
    if (t !== "object" && t !== "function") throw TypeError();
    function f() {
    };
    f.prototype = p;
    return new f();
}

//赋值属性
function extend(o, p) {
    for (prop in p) {
        o[prop] = p[prop];
    }
    return o;
}

// 根据给定的超累创建一个子类
function defineSubclass(superclass,  // 超类的构造函数
                        constructor, // 新的子类的构造函数
                        methods,     // Instance methods: copied to prototype
                        statics)     // Class properties: copied to constructor
{
    // Set up the prototype object of the subclass
    constructor.prototype = inherit(superclass.prototype);
    constructor.prototype.constructor = constructor;

    // Copy the methods and statics as we would for a regular class
    if (methods) extend(constructor.prototype, methods);
    if (statics) extend(constructor, statics);

    // Return the class
    return constructor;
}

// 也可以通过父类的构造方法来做到这一点
Function.prototype.extend = function (constructor, methods, statics) {
    return defineSubclass(this, constructor, methods, statics);
};

//========================================================================
// 测试
//========================================================================
function A() {

}
A.prototype.x = 1;

function B() {

}
defineSubclass(A, B);
var b = new B();

console.log(b.constructor);//->[Function: B]
console.log(b.constructor.prototype);//->B { constructor: [Function: B] }。由Object创建的对象，改对象集成自A的原型
console.log(B.prototype);//->B { constructor: [Function: B] }
console.log(b.__proto__);//->B { constructor: [Function: B] }
console.log(b.__proto__.constructor);//[Function: B]
console.log(b.__proto__.constructor.prototype);//->B { constructor: [Function: B] }
console.log(b.__proto__.__proto__);//-> { x: 1 }
console.log(b.__proto__.__proto__ === A.prototype);//->true
console.log(Object.getPrototypeOf(b.__proto__) === A.prototype);//->true
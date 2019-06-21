//========================================================================
// 对象的原型属性
//========================================================================
var p = {x: 1};
var o = {x: 2};
p.isPrototypeOf(o);//->false 查询属性

var newP = Object.create(p);
p.isPrototypeOf(newP);//-> true
var newPConstructor = newP.constructor;//->[Function: Object]
var pConstructor = p.constructor;//->[Function: Object]
// 使用create方法创建对象时，不能使用此方式获取原型属性，这里得到的是{}。应该使用`isPrototypeOf()`方法判断一个对象是不是另一个对象的原型。
Object.getPrototypeOf(newP);//->{x:1}
var fakePrototype = newP.constructor.prototype;//->{}

//========================================================================
// 类属性
//========================================================================
var a = {x: 123};
console.log(a);//->{x:123};，log会将对象的内容打印出来，但是并不是调用对象的头String方法
var aToString = a.toString();//->[object Object]
var aClass = a.toString().slice(8, -1);//-> Object

/**
 * classOf方法用来获取对象的类属性
 * @param o 对象
 * @returns {*} 字符串类型的类属性
 */
function classOf(o) {
    if (o === null) {
        return "Null";
    }
    if (o === undefined) {
        return "Undefined";
    }
    //间接的调用Function.call()方法
    return Object.prototype.toString.call(o).slice(8, -1);
}
var s = "abc";
var sToString = s.toString();//->abc，字符的toString方法返回字符串内容本身
var sClass = classOf(s);//->String

//========================================================================
// 可扩展性
//========================================================================
var c = {};
Object.isExtensible(c);//->true，判断是否可扩展
Object.preventExtensions(c);//锁定对象，把对象变为不可扩展

var d = {x: 1, y: 2};
Object.seal(d);//封闭对象，把对象变为不可扩展，并且所有属性改为不可配置
Object.isSealed(d);//->true，是否是封闭对象

var e = {x: 32};
Object.freeze(e);//冻结对象，把对象变为不可扩展，并且所有属性改为不可配置且位置为只读，但是setter不受影响
Object.isFrozen(e);//-> true

//创建一个封闭对象，第2个参数为属性描述对象
var f = Object.create(Object.freeze({x: 1}), {
    y: {value: 3, writable: true}
});
console.log(f);//->{}，冻结对象属性不可枚举
console.log(f.y);//->3


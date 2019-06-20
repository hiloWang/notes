//========================================================================
// 5：__proto__属性
//========================================================================

function Person() {
}

Person.prototype.name = 'Jordon';
Person.prototype.age = 28;
Person.prototype.job = 'Software Engineer';

Person.prototype.sayName = function () {
    console.log(this.name);
};

var person1 = new Person();

/*
 JS 在创建对象（不论是普通对象还是函数对象）的时候，都有一个叫做__proto__ 的内置属性，用于指向创建它的构造函数的原型对象。
 对象 person1 有一个 __proto__属性，创建它的构造函数是 Person，构造函数的原型对象是 Person.prototype ，所以：
 person1.__proto__ === Person.prototype。

 关于__proto__属性：因为绝大部分浏览器都支持__proto__属性，所以它才被加入了 ES6 里（ES5 部分浏览器也支持，但还不是标准）。
 */

var isSame = (person1.__proto__ === Person.prototype);//->true
var isSame = Person.prototype.constructor === Person;//->true
var isSame = person1.__proto__ === Person.prototype;//->true
var isSame = person1.constructor === Person;//->true

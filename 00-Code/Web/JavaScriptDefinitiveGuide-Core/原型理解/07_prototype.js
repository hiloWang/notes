//========================================================================
// 8：prototype
//========================================================================

/*

 在 ECMAScript 核心所定义的全部属性中，最耐人寻味的就要数 prototype 属性了。
 对于 ECMAScript 中的引用类型而言，prototype 是保存着它们所有实例方法的真正所在。
 换句话所说，诸如 toString()和 valueOf() 等方法实际上都保存在 prototype 名下，只不过是通过各自对象的实例访问罢了。


 原型对象是用来做什么的呢？主要作用是用于继承，通过构造函数构造出的实例对象，自动继承它的构造函数的prototype上的属性和方法。


 JS 内置了一些方法供我们使用，比如：

 对象可以用 constructor/toString()/valueOf() 等方法;
 数组可以用 map()/filter()/reducer() 等方法；
 数字可用用 parseInt()/parseFloat()等方法；

 当我们创建一个对象时：

 var person = new Object()

 person 继承了Object 的原型对象Object.prototype上所有的方法：hasOwnProperty、toString等

 当我们创建一个数组时：

 var array = new Array()

 array继承了Array 的原型对象Array.prototype上所有的方法。

 */


console.log(Object.getOwnPropertyNames(Object.prototype));//Object.prototype中所有的属性(包括不可枚举的属性)的名集合
console.log(Object.getOwnPropertyNames(Array.prototype));//获取Array.prototype中所有的属性(包括不可枚举的属性)的名集合
console.log(Object.getOwnPropertyNames(Function.prototype));//Function.prototype中所有的属性(包括不可枚举的属性)的名集合



//========================================================================
// 9：修改对象的prototype
//========================================================================
/*
 默认情况下：
 function Person(name) {
 this.name = name
 }
 var p = new Person('jack')

 p.__proto__与Person.prototype，p.constructor.prototype都是恒等的，即都指向同一个对象。

 但是可以修改函数对象的prototype属性
 */

function Person(name) {
    this.name = name
}

var p1 = new Person('jack');
console.log(p1.__proto__ === Person.prototype); // true
console.log(p1.__proto__ === p1.constructor.prototype); // true
console.log(p1.constructor === Person); // true

// 重写原型
Person.prototype = {
    getName: function () {
    }
};

var p2 = new Person('jack');
console.log(p2.__proto__ === Person.prototype); // true
console.log(p2.__proto__ === p2.constructor.prototype); // false
console.log(p2.constructor === Person); // false
console.log(p2.constructor === Object); // true

/*
 给Person.prototype赋值的是一个对象直接量{getName: function(){}}，使用对象直接量方式定义的对象其构造器（constructor）指向的是根构造器Object，
 Object.prototype是一个空对象{}，{}自然与{getName: function(){}}不等。
 */

//再来看一个列子
function A() {

}
var a = new A();
console.log(a.constructor);//->[Function: A]
console.log(A.prototype.constructor);//->[Function: A]
A.prototype = Date.prototype;
console.log(A.prototype.constructor);//->[Function: Date]
var b = new A();
console.log(b.constructor);//->[Function: Date]
/*
可以看出，当构造函数的原型被修改时，实例对象的构造函数属性将不再是原来的构造函数，而是与构造函数绑定的原型有关
 */



//========================================================================
// 10：原型链
//========================================================================

//原型和原型链是JS实现继承的一种模型


/*
 原型链的形成是真正是靠__proto__ 而非prototype
 */
var animal = function () {
};
var dog = function () {
};

animal.price = 2000;
dog.prototype = animal;

var tidy = new dog();
console.log(dog.price); //undefined，dog.prototype指向animal，animal.price=2000，但是dog无法继承自己的prototype的属性
console.log(tidy.price); // 2000，tidy.__proto__指向animal，它继承了animal.price=2000。


/*
 对象之间的继承关系构成了原型链，比如这里的原型链是：child-->Child.prototype-->Object.prototype
 */
function Child() {
}

var child = new Child();
console.log(child.constructor === Child);//-> true
console.log(Child.constructor === Function);//-> true
console.log(child.__proto__ === Child.prototype);//-> true
console.log(Child.prototype.__proto__ === Object.prototype);//-> true




//========================================================================
// 11：总结
//========================================================================

/*
- 每个对象都有 __proto__ 属性，但只有函数对象才有 prototype 属性
- JS 在创建对象（不论是普通对象还是函数对象）的时候，都有一个叫做__proto__ 的内置属性，用于指向创建它的构造函数的原型对象。即所有对象的 __proto__ 都指向其构造器的 prototype。
- 所有函数对象的__proto__都指向Function.prototype，它是一个空函数。所有函数对象的构造函数默认都指向Function。
- 所有的构造器都来自于 Function.prototype，甚至包括根构造器Object及Function自身。 所有构造器都继承了Function.prototype的属性及方法。如length、call、apply、bind，
- 所有的构造器也都是一个普通 JS 对象，可以给构造器添加/删除属性等。同时它也继承了Object.prototype上的所有方法：toString、valueOf、hasOwnProperty等。

 Object.__proto__ === Function.prototype // true，Object 是函数对象，是通过new Function()创建的，所以Object.__proto__指向Function.prototype。

 Function.__proto__ === Function.prototype // true，Function 也是对象函数，也是通过new Function()创建，所以Function.__proto__指向Function.prototype。

 Function.prototype.__proto__ === Object.prototype //true，Function.prototype是个函数对象，理论上他的__proto__应该指向 Function.prototype，就是他自己，自己指向自己没有意义。
 JS一直强调万物皆对象，函数对象也是对象，让 Function.prototype.__proto__ 指向Object.prototype。Object.prototype.__proto__ === null，保证原型链能够正常结束。
 */


//========================================================================
// 12：null值
//========================================================================
/*

 typeof(null)的值是"object"，但是null 没有 _proto_。 null 是一个独立的数据类型，为什么typeof(null)的值是"object"？

- null不是一个空引用, 而是一个原始值， 它只是期望此处将引用一个对象, 注意是"期望", [null](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/null)
- typeof null结果是object, 这是个历史遗留bug。
- 在ECMA6中, 曾经有提案将type null的值纠正为null, 但最后提案被拒了，理由是历史遗留代码太多。
 */
//========================================================================
// 2：构造函数
//========================================================================

function Person(name, age, job) {
    this.name = name;
    this.age = age;
    this.job = job;
}

/*
 person1 和 person2是通过构造函数Person构造的普通对象
 person1 和 person2其实并没有constructor 这个属性，这是属性是属于Person的原型对象的。只是person1 和 person2和可以继承原型对象的constructor这个属性。
 person1与Person2这两个实例与构造函数Person并没有直接关系，person1与Person2与Person的原型对象才有直接关系。
 */
var person1 = new Person("mike", 12, "student");
var person2 = new Person("luck", 22, "teacher");
var same = (person1.constructor === person2.constructor && person2.constructor === Person);//->true，实例的构造函数属性（constructor）指向构造函数
var same = Person.prototype.constructor === Person;//->true

var object1 = new Object();
var object2 = {};
var same = object1.constructor === object2.constructor && object1.constructor === Object;//->true，普通对象的构造函数为Object

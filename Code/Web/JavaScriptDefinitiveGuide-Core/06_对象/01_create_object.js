//========================================================================
// 创建对象
//========================================================================

//对象直接量
var empty = {};
var point = {x: 1, y: 2};

//通过new创建对象
var obj = new Object();
var reg = new RegExp("js");

//通过Object的create方法创建新对象
//create方法接受两个参数，第一个参数为原型(通过__proto__获取)，第二个参数为可选参数，用以描述对象的属性。create方法是一个静态函数
var obj1 = Object.create({x: 1, y: 2});//obj1继承了x和y属性
var obj2 = Object.create(null);//可以传入null值，创建一个没有原型的对象，没有继承任何属性和方法，甚至是toString()
var obj3 = Object.create(Object.prototype);//创建普通对象，等效于{}

//========================================================================
// 在ES3中封装工具方法inherit
//========================================================================

//在ES3中封装工具方法inherit，inherit返回继承一个继承自对象p的新对象，inherit可以防止库函数无意间(非恶意的)修改那些不受你控制的对象，
//不直接传入对象，而是将它的继承对象传入，因为当函数读取集成对象的属性时，是读取集成过来的值，
//如果给继承对象的属性赋值，则这些属性只会影响这个继承对象自身，而不是原始对象
function inherit(p) {
    if (p == null) {
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
console.log(inherit({x: 1, y: 2}));


//========================================================================
// 类与类型
//========================================================================

//返回一个字符串，表示对象的类型
function type(o) {
    var t, c, n;  // type, class, name
    //null盘但
    if (o === null) {
        return "null";
    }
    //NaN类型
    if (o !== o) {
        return "nan";
    }
    //typeof不是object的话，那么应该是基本类型
    if ((t = typeof o) !== "object") {
        return t;
    }
    //如果对象的类信息不是Object，那么直接返回该类型
    if ((c = classOf(o)) !== "Object") {
        return c;
    }
    //如果对象具有构造函数并且构造函数具有名称，那么返回这个名称
    if (o.constructor && typeof o.constructor === "function" && (n = o.constructor.getName())) {
        return n;
    }
    //否则只能返回Object
    return "Object";
}

//返回对象的类信息
function classOf(o) {
    return Object.prototype.toString.call(o).slice(8, -1);
}

//返回函数的名称，可能为""或者null
Function.prototype.getName = function () {
    if ("name" in this){
        return this.name;
    }
    return this.name = this.toString().match(/function\s*([^(]*)\(/)[1];
};

//========================================================================
// test
//========================================================================
var number = 1;
var string = "32";
var date = new Date();
var reg = new RegExp();
var obj = {x: 2};
var array = [1, 2, 3];
var noNameFunc = function (x, y) {
    return x + y;
};
var nameFunc = function add(x, y) {
    return x + y;
};
var nonNameFuncObj = new noNameFunc(1, 3);
var nameFuncObj = new nameFunc(1, 3);

console.log(type(number));//->number
console.log(type(string));//->string
console.log(type(date));//->Date
console.log(type(reg));//->RegExp
console.log(type(obj));//->Object
console.log(type(array));//->Array
console.log(type(noNameFunc));//->function
console.log(type(nameFunc));//->function
console.log(type(nonNameFuncObj));//->noNameFunc
console.log(type(nameFuncObj));//->add


console.log(noNameFunc.getName());//->noNameFunc
console.log(nameFunc.getName());//->add

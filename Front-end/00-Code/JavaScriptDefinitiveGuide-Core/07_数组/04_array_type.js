//========================================================================
// 数组类型
//========================================================================

    //ES5中用于判断数组的方法
    var a = [1, 2, 3];
    Array.isArray(a);//-> true

    //ES3中却没有对应的方法，typeof只是简单的返回对象，instanceof操作只能用于简单的判断
    var isInstanceofArray1 = [] instanceof Array;//->true
    var isInstanceofArray2 = {} instanceof Array;//->false


    //兼容ES3方式为
    var isArrayFunc = Array.isArray || function (o) {
            return typeof o === "object" &&
                Object.prototype.toString.call(o) === "[Object Array]";
        };
    console.log(isArrayFunc(a));//->true
    console.log(Object.prototype.toString.call(a));//->[object Array]


//========================================================================
// 类数组对象
//========================================================================
/*
 js数组的特性：
 - 自动更新length
 - 设置length为较小的值时自动截断数组
 - 从Array.prototype继承一些方法
 - 类属型为“Array”

 但这写都不是定义数组的本质特性，一种常常完全合理的看法是把有一个length属性和对应非负整数属性的对象看作一种类型的数组。即类数组。
 可以用针对数组的遍历方法来遍历这种类数组
 */

var a = {};
var i = 0;
while (i < 10) {
    a[i] = i * i;
    i++;
}
a.length = i;
//现在a就像一个数组，可以像数组一样遍历它
var total = 0;
for (var j = 0, length = a.length; j < length; j++) {
    total += a[j];
}
console.log(total);//->285



 // function的隐式arguments参数是一个类数组，DOM中getElementsByTagName()返回的是一个类数组

//用于判断类数组的方法
function isArrayLike(o) {
    return !!(o &&
    typeof  o === "object" &&//是对象
    isFinite(o.length) &&//有length属性
    o.length >= 0 &&//length大于等于0
    o.length === Math.floor(o.length) &&//是整数
    o.length < Math.pow(2, 32));//length < 2^32
}
console.log(isArrayLike(a));//->true

//js数组的方法是特意定义为通用的，因此它们可以用在类数组对象上，ES5中所有的数组方法都是通用的，ES3中除toString和toLocalString方法都是通用的。
//使用方式为Function.call
var b = {"0": "a", "1": "b", "2": "c", length: 3};//类数组对象
var result = Array.prototype.join.call(b, "--");
console.log(result);//->a--b--c


//在FireFox这些方法的版本被定义在Array构造函数上，所以可以直接使用，但是这不是标准的，所以应该使用兼容的方式，比如：
Array.join = Array.join || function (a, sep) {
        return Array.prototype.join.call(a, sep);
    };
console.log(Array.join(b, "-"));


//========================================================================
// 作为数组的字符串
//========================================================================
//字符串类似于只读数组，除了用charAt方法还可用[]直接访问字符，所以数组方法也可以用于字符串，除了push等会改变数组的方法除外，因为字符串是不可变对象。
var s = "JavaScript";
console.log(s[1]);
console.log(Array.prototype.join.call(s, '-'));
//========================================================================
// 数组基础使用，数组最大能容纳2^32-1个元素
//========================================================================
var empty = [];
var primes = [2, 3, 5, 7, 11];
var misc = [1.1, true, "a"];
var byte = 1024;
var table = [byte, byte + 1];
var undefs = [, 1, , 3];//稀疏数组，不连续的数组
var arr = new Array(10);//长度为10
var arr1 = new Array(5, 4, 3, 2, 1, true);//指定元素

//========================================================================
// 访问与设置元素
//========================================================================
var a = [];
a[-1.21] = true;//创建一个名为-1.21的属性，本质上-1.21转换为字符串，它只是一个普通的属性
a["10"] = 10;//给第10个元素赋值
a[1.00] = 3;//等价于a[1]
a[-2] = "-2";//添加一个名为-2的属性
console.log(a);//-> [ , 3, , , , , , , , , 10, '-1.21': true, '-2': '-2' ]
console.log(a[1]);
a.length = 0;//删除所有的元素


//========================================================================
// ES5中让数组的length变为不可写，此时无法操作数组
//========================================================================
Object.defineProperty(a, "length", {
    writable: false
});
console.log(a.length);
// a.push(2); //报错，Cannot assign to read only property 'length' of object '[object Array]'

var b = [3];
Object.freeze(b);
b.length = 0;//无效
console.log(b);
// b.push(2); //报错， Can't add property 1, object is not extensible


//========================================================================
// 添加与访问元素
//========================================================================
var c = [];
c.push(1);
c.push(1, 2, 4);
c.unshift("S");//在数组的头部加入一个元素
c[1] = "A";
var success = delete  c[1];//仅仅是删除元素
var result = c.pop();//弹出元素，使长度减1，
var top = c.unshift();//头部弹出元素


//========================================================================
// 遍历数组
//========================================================================
//使用for循环是遍历数组最常见的方法
var o = {x: 1, y: 2, z: 3};
var keys = Object.keys(o);
for (var i = 0, len = keys.length; i < len; i++) {
    if (!keys[i]) {//跳过null和undefined
        continue;
    }
    var value = keys[i];
    console.log(value);
}

//使用for/in循环能够枚举继承属性名，ES规定for/in循环可以以不同的顺序遍历对象的属性，通常数组的遍历是升序的，
//但是不能保证这是一定的，很多时候应该避免使用for/in来遍历数组
for (var element in keys) {
    if (!keys.hasOwnProperty(element)) {//跳过继承的属性
        continue;
    }
    console.log(element);
}

//使用ES5提供的forEach是一个不错的选择
keys.forEach(function (p1, p2, p3) {//分别是属性名、值、数组本身
    console.log(p1 + "  " + p2 + "  " + p3.toString());
});
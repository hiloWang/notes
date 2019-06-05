//========================================================================
// 1：普通对象和函数对象
//========================================================================

/*
 JavaScript中，万物皆对象，但对象也是有区别的。分为**普通对象和函数对象**，Object 、Function是JS自带的函数对象。

 o1 o2 o3 为普通对象，f1 f2 f3 为函数对象。怎么区分，其实很简单，
 凡是通过 new Function() 创建的对象都是函数对象，其他的都是普通对象。
 f1、f2归根结底都是通过 new Function()的方式进行创建的。Function Object 也都是通过 new Function()创建的。
 */

var o1 = {};
var o2 = new Object();
var o3 = new f1();

function f1() {
}
var f2 = function () {
};
var f3 = new Function('str', 'console.log(str)');

var type = typeof Object; //->function
var type = typeof Function; //->function
var type = typeof f1; //->function
var type = typeof f2; //->function
var type = typeof f3; //->function
var type = typeof o1; //->object
var type = typeof o2; //->object
var type = typeof o3; //->object

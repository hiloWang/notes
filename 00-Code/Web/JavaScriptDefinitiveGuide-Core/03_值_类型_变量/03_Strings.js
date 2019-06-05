//========================================================================
// 字符串
//========================================================================

//定义字符串
var str = "Hello, World";

var str2 = "a\
                b\
                c\
                ";

console.log(str.length);//->12
console.log(str2.length);//->51

//slice表示切开，可以反着切
console.log(str.slice(1, 4));//->ell
console.log(str.slice(-3));//->rld

//返回字符串第一个字符的ASCII码
str.charCodeAt(1);

//索引
console.log(str.charAt(3));//->l
console.log(str[3]);//->l

//正则表达式
var text = "testing: 1, 2, 3";
var pattern = "[s]";
console.log(text.search(pattern));//->2
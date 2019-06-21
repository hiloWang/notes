//========================================================================
// Number
//========================================================================
//定义数字
var num1 = 123;
var num2 = 0x123;
var num3 = 3.32;
var num4 = .324;
var num5 = 6.02e23;

//Math
console.log("3的9次方= " + Math.pow(3, 9));

//溢出、下溢
console.log(0 / 0);//->NaN
console.log(1 / 0);//->Infinity
console.log(-1 / 0);//-> -Infinity

//判断NaN的方法
var nan = NaN;
var same = nan !== nan;//->true
var same = isNaN(nan);//->true

isFinite(nan);//->false，不是NaN、Infinity、-Infinity时为true

//全局对象
console.log(Number.NaN);
console.log(Number.MAX_VALUE);
console.log(Number.MIN_VALUE);
console.log(Number.POSITIVE_INFINITY);
console.log(Number.NEGATIVE_INFINITY);
console.log(Math.PI);

//Number方法
var number = Number("23");
console.log(number);

//进制转换
var a = 15;
console.log(a.toString(2));//转化为二进制字符串
console.log(a.toString(8));
console.log(a.toString(16));


//parseInt
console.log(parseInt("3 abc")); //3
console.log(parseInt(" 3.14 abc")); //3
console.log(parseInt(" -3.14 abc")); //-3
console.log(parseInt("0xFF")); //255
console.log(parseFloat(".1")); //0.1
console.log(parseInt("0.1")); //0
console.log(parseInt(".1")); //NaN
console.log(parseFloat("$1")); //NaN

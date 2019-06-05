//========================================================================
// 使用函数自身的属性来缓存结果
//========================================================================

//计算阶乘
function factorial(n) {
    if (isFinite(n) && n > 0 && n === Math.round(n)) {//有限的正整数
        if (!(n in factorial)) {
            factorial[n] = n * factorial(n - 1);
        }
        return factorial[n];
    }
    return NaN;
}
factorial[1] = 1;//初始化

console.log(factorial(100));//->9.33262154439441e+157
console.log(factorial(2));//->2
console.log(factorial(5));//->120

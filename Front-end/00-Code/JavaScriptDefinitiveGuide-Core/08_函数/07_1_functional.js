//========================================================================
// 函数式编程：JavaScript并非函数式编程语言，但是js可以像操作对象一样操作函数，因此可以在js中实现函数式编程。
//========================================================================

//========================================================================
// 使用函数处理数组
//========================================================================
var data = [1, 1, 2, 3, 4, 5, 5];
var sum = function (x, y) {
    return x + y
};
var square = function (x) {
    return Math.pow(x, 2)
};

var mean = data.reduce(sum) / data.length;//平均值
var deviations = data.map(function (p1) {
    return p1 - mean;
});//偏差

var stddev = Math.sqrt(deviations.map(square).reduce(sum) / (data.length - 1));//标准偏差

console.log(mean);//->3
console.log(deviations);//->[ -2, -2, -1, 0, 1, 2, 2 ]
console.log(stddev);//->1.7320508075688772


//========================================================================
// 模拟map
//========================================================================
var map = Array.prototype.map ? function (a, f) {
    return a.map(f);
} : function (a, f) {
    var results = [];
    for (var i = 0, len = a.length; i < len; i++) {
        if (i in a) {
            results[i] = f.call(null, a[i], a);
        }
    }
    return results;
};

var result = map([1, 2, 3, 4, 5], function (x) {
    return x - 1;
});
console.log(result);//->[ 0, 1, 2, 3, 4 ]


//========================================================================
// 高阶函数：所谓高阶函数就是操作函数的函数
//========================================================================
function not(f) {
    return function () {
        var result = f.apply(this, arguments);
        return !result;
    }
}

function even(x) {
    return x % 2 === 0;
}
var odd = not(even);
console.log(odd(2));//->false


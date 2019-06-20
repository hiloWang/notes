//========================================================================
// 检查属性
//========================================================================
var o = {x: 1};
console.log("x" in o);//->true
console.log("y" in o);//->false
console.log("toString" in o);//->true

console.log(o.hasOwnProperty('x'));//->true
console.log(o.hasOwnProperty('y'));//->false
console.log(o.hasOwnProperty('toString'));//->false

var o2 = Object.create({y: 3});
o2.x = 1;
console.log(o2.propertyIsEnumerable("x"));//true
console.log(o2.propertyIsEnumerable("y"));//false，y是继承来的
console.log(Object.prototype.propertyIsEnumerable("toString"));//false，toString不可枚举

//========================================================================
// 枚举属性方法
//========================================================================

//复制p中可枚举属性到o中
function extend(o, p) {
    for (var prop in  p) {
        o[prop] = p[prop];
    }
    return o;
}
console.log(extend({}, {x: 3}));

//复制p中可枚举属性到o中，已有属性不覆盖
function merge(o, p) {
    for (var prop in  p) {
        if (o.hasOwnProperty(prop)) {
            continue;
        }
        o[prop] = p[prop];
    }
    return o;
}

//删除o中属性在p中不存在的同名属性，就删除o中的该属性
function restrict(o, p) {
    for (var prop in  o) {
        if (!(prop in p)) {
            delete o[prop];
        }
    }
    return o;
}

//如果属性在o和p中同时存在，删除o中的该属性
function subtract(o, p) {
    for (var prop in  o) {
        if (prop in p) {
            delete o[prop];
        }
    }
    return o;
}

//返回新对象，新对象同时拥有p和o中的属性,有同名属性时使用p的
function union(o, p) {
    return extend(extend({}, o), p);
}

//返回新对象，新对象有用同时存在o和p中的属性，同名属性时，使用p 的
function intersection(o, p) {
    return restrict(extend({}, o), p);
}

//返回所有可枚举的自有属性的名称组成的数组
function keys(o) {
    if (typeof  o !== "object") {
        throw new TypeError;
    }
    var result = [];
    for (var prop in o) {
        if (o.hasOwnProperty(prop)) {
            result.push(prop)
        }
    }
    return result;
}
console.log(keys({x: 3, y: 4}));
// 这个版本的Range是可变的，但是将端点变量进行了良好的封装，端点变量的大小顺序是固定的form < to
function Range(from, to) {
    // 验证参数
    if (from > to) throw new Error("Range: from must be <= to");

    // 定义存储器方法
    function getFrom() {  return from; }
    function getTo() {  return to; }
    function setFrom(f) {
        if (f <= to) from = f;
        else throw new Error("Range: from must be <= to");
    }
    function setTo(t) {
        if (t >= from) to = t;
        else throw new Error("Range: to must be >= from");
    }

    Object.defineProperties(this, {
        from: {get: getFrom, set: setFrom, enumerable:true, configurable:false},
        to: { get: getTo, set: setTo, enumerable:true, configurable:false }
    });
}

Range.prototype = hideProps({
    constructor: Range,
    includes: function(x) { return this.from <= x && x <= this.to; },
    foreach: function(f) {for(var x=Math.ceil(this.from);x<=this.to;x++) f(x);},
    toString: function() { return "(" + this.from + "..." + this.to + ")"; }
});

//========================================================================
// 存储器属性
//========================================================================
var p = {
    //普通属性
    x: 1.0,
    y: 1.0,
    //$符号暗示这个属性是私有属性，但仅仅是暗示
    $n: 0,
    //存储器属性
    get r() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    },
    set r(newValue) {
        //this表示指向这个点的对象
        var oldValue = Math.sqrt(this.x * this.x + this.y * this.y);
        var ratio = newValue / oldValue;
        this.x *= ratio;
        this.y *= ratio;
    }
};
//继承存储器属性
var q = Object.create(p);
q.x = 3;
q.y = 4;
console.log(q.r);//->5

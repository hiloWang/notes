//========================================================================
// 定义删除变量
//========================================================================
var trueVar = 3;//使用var声明的全局变量是不可配置的，无法用delete删除。
fakeVar = 4;
this.fakeVar2 = 5;

console.log(trueVar);//->3
console.log(fakeVar);//->4
console.log(this.fakeVar2);//->5

//删除变量
delete  trueVar;
delete fakeVar;
delete this.fakeVar2;
console.log(trueVar);//->3
console.log(this.fakeVar2);//->undefined
console.log(fakeVar);//报错：fakeVar is not defined

//========================================================================
// 对象的序列化和反序列化：JSON( JavaScript Object Notation)
//========================================================================
var o = {x: 1, y: 2, z: [false, null, ""]};
var json = JSON.stringify(o);
console.log(json);//->{"x":1,"y":2,"z":[false,null,""]}
var o1 = JSON.parse(json);
console.log(o1);//->{ x: 1, y: 2, z: [ false, null, '' ] }

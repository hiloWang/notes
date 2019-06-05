import 'package:flutter/material.dart';

Widget buildBasicWidgetApp() {
  return new MaterialApp(
    title: 'My app', // used by the OS task switcher
    home: new MyScaffold(),
  );
}

class MyScaffold extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // Material 是UI呈现的“一张纸”
    return new Material(
      // Column is 垂直方向的线性布局.
      child: new Column(
        //返回子widget泪飙
        children: <Widget>[
          //第一个 AppBar
          new MyAppBar(
            title: new Text(
              'Example title',
              style: Theme.of(context).primaryTextTheme.title,
            ),
          ),
          //第二个，一个句中的文本
          new Expanded(
            child: new Center(
              child: new Text('Hello, world!'),
            ),
          ),
        ],
      ),
    );
  }
}

class MyAppBar extends StatelessWidget {
  // Widget子类中的字段往往都会定义为"final"
  final Widget title;

  MyAppBar({this.title});

  @override
  Widget build(BuildContext context) {
    return new Container(
        // 单位是逻辑上的像素（并非真实的像素，类似于浏览器中的像素）
        height: 56.0,
        padding: const EdgeInsets.symmetric(horizontal: 8.0),
        /*对称*/
        decoration: new BoxDecoration(color: Colors.blue[500]),
        //Row 是水平方向的线性布局（linear layout）
        child: new Row(
          //列表项的类型是 <Widget>
          children: <Widget>[
            new IconButton(
              icon: new Icon(Icons.menu),
              onPressed: null,
              tooltip: "Navigation menu",
            ),
            //Expanded expands its child to fill the available space.
            //这意味着它会填充尚未被其他子项占用的的剩余可用空间。Expanded可以拥有多个children， 然后使用flex参数来确定他们占用剩余空间的比例。
            new Expanded(child: title),
            //图标按钮
            new IconButton(icon: new Icon(Icons.search), onPressed: null)
          ],
        ));
  }
}

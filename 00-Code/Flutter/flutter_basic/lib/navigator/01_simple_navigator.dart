import 'package:flutter/material.dart';

/*
大多数应用程序包含多个页面。例如，我们可能有一个显示产品的页面，然后，用户可以点击产品，跳到该产品的详情页。

在Android中，页面对应的是Activity，在iOS中是ViewController。而在Flutter中，页面只是一个widget！

在Flutter中，我们可以使用Navigator在页面之间跳转。
 */
Widget buildSimpleNavigatorWidget() {
  return new MaterialApp(
    title: 'Navigation Basics',
    home: new _FirstScreen(),
  );
}

class _FirstScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('First Screen'),
      ),
      body: new Center(
        child: new RaisedButton(
          child: new Text('Launch new screen'),
          onPressed: () {
            openScreenSecond(context);
          },
        ),
      ),
    );
  }

  void openScreenSecond(BuildContext context) {
    Navigator.push(
      context,
      //  push 需要一个 Route 对象，我们可以创建自己的 Route，或直接使用MaterialPageRoute。
      // MaterialPageRoute很方便，因为它使用平台特定的动画跳转到新的页面(Android和IOS屏幕切换动画会不同)。
      new MaterialPageRoute(builder: (context) => new _SecondScreen()),
    );
  }
}

class _SecondScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Second Screen"),
      ),
      body: new Center(
        child: new RaisedButton(
          onPressed: () {
            Navigator.pop(context);
          },
          child: new Text('Go back!'),
        ),
      ),
    );
  }
}

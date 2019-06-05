import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

Widget buildStatefulWidget() {
  return new MaterialApp(
    title: "buildStatefulWidget",
    home: new Center(
      child: new Counter(),
    ),
  );
}

/*
为什么StatefulWidget和State是单独的对象。在Flutter中，这两种类型的对象具有不同的生命周期：
    Widget是临时对象，用于构建当前状态下的应用程序。
    State对象在多次调用build()之间保持不变，允许它们记住信息(状态)。
*/
class Counter extends StatefulWidget {
  @override
  _CounterState createState() => new _CounterState();
}

class _CounterState extends State<Counter> {
  int _counter = 0;

  void _increment() {
    //
    setState(() {
      ++_counter;
    });
  }

  @override
  Widget build(BuildContext context) {
    //在Flutter中，事件流是“向上”传递的，而状态流是“向下”传递的
    //时间流从最底层 Widget 向上到最上层的 Widget
    //状态与从Widget树根到枝叶
    return new Row(children: <Widget>[
      new CounterIncrementor(onPressed: _increment),
      new CounterDisplay(count: _counter),
    ]);
  }
}

class CounterIncrementor extends StatelessWidget {
  CounterIncrementor({this.onPressed});

  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return new RaisedButton(
      onPressed: onPressed,
      child: new Text('Increment'),
    );
  }
}

class CounterDisplay extends StatelessWidget {
  CounterDisplay({this.count});

  final int count;

  @override
  Widget build(BuildContext context) {
    return new Text('Count: $count');
  }
}

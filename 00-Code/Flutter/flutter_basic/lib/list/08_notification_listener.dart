import 'package:flutter/material.dart';

Widget buildScrollNotificationWidget() => MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: Text("ScrollNotification"),
          ),
          body: ScrollNotificationTestRoute()),
    );

class ScrollNotificationTestRoute extends StatefulWidget {
  @override
  _ScrollNotificationTestRouteState createState() =>
      new _ScrollNotificationTestRouteState();
}

class _ScrollNotificationTestRouteState
    extends State<ScrollNotificationTestRoute> {
  String _progress = "0%"; //保存进度百分比

  @override
  Widget build(BuildContext context) {
    //Flutter Widget树中子Widget可以通过发送通知（Notification）与父(包括祖先)Widget通信。
    // 父Widget可以通过NotificationListener Widget来监听自己关注的通知，这种通信方式类似于Web开发中浏览器的事件冒泡
    return Scrollbar(
      //监听滚动通知，NotificationListener是一个Widget，模板参数T是想监听的通知类型，如果省略，则所有类型通知都会被监听，
      // 如果指定特定类型，则只有该类型的通知会被监听。
      child: NotificationListener<ScrollNotification>(
        onNotification: (ScrollNotification notification) {
          double progress = notification.metrics.pixels /
              notification.metrics.maxScrollExtent;
          //重新构建
          setState(() {
            _progress = "${(progress * 100).toInt()}%";
          });
          print("BottomEdge: ${notification.metrics.extentAfter == 0}");
          return true; //放开此行注释后，进度条将失效
        },
        child: Stack(
          alignment: Alignment.center,
          children: <Widget>[
            ListView.builder(
                itemCount: 100,
                itemExtent: 50.0,
                itemBuilder: (context, index) {
                  return ListTile(title: Text("$index"));
                }),
            CircleAvatar(
              //显示进度百分比
              radius: 30.0,
              child: Text(_progress),
              backgroundColor: Colors.black54,
            )
          ],
        ),
      ),
    );
  }
}

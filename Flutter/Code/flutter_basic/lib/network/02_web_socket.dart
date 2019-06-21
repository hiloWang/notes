import 'package:flutter/foundation.dart';
import 'package:web_socket_channel/io.dart';
import 'package:flutter/material.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

/*
WebSocketChannel提供了一个来自服务器的消息Stream ，该Stream类是dart:async包中的一个基础类。它提供了一种方法来监听来自数据源的异步事件。
与Future返回单个异步响应不同，Stream类可以随着时间推移传递很多事件。该StreamBuilder Widget将连接到一个Stream，
并在每次收到消息时通知Flutter重新构建界面。

WebSocketChannel提供了一个StreamSink，它将消息发给服务器。StreamSink类提供了给数据源同步或异步添加事件的一般方法。
 */
Widget buildWebSocketWidget() {
  return WebSocketApp();
}

class WebSocketApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final title = 'WebSocket Demo';
    return new MaterialApp(
      title: title,
      home: new WebSocketHomePage(
        title: title,
        channel: new IOWebSocketChannel.connect('ws://echo.websocket.org'),
      ),
    );
  }
}

class WebSocketHomePage extends StatefulWidget {
  final String title;
  final WebSocketChannel channel;

  WebSocketHomePage({Key key, @required this.title, @required this.channel})
      : super(key: key);

  @override
  _WebSocketHomePageState createState() => new _WebSocketHomePageState();
}

class _WebSocketHomePageState extends State<WebSocketHomePage> {
  TextEditingController _controller = new TextEditingController();

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
      body: new Padding(
        //padding
        padding: const EdgeInsets.all(20.0),
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            //一个表单
            new Form(
              child: new TextFormField(
                controller: _controller,
                decoration: new InputDecoration(labelText: 'Send a message'),
              ),
            ),
            //使用一个StreamBuilder Widget来监听新消息， 并用一个Text Widget来显示它们。
            new StreamBuilder(
              stream: widget.channel.stream,
              builder: (context, snapshot) {
                return new Padding(
                  padding: const EdgeInsets.symmetric(vertical: 24.0),
                  child: new Text(snapshot.hasData ? '${snapshot.data}' : ''),
                );
              },
            )
          ],
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _sendMessage,
        tooltip: 'Send message',
        child: new Icon(Icons.send),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }

  void _sendMessage() {
    if (_controller.text.isNotEmpty) {
      widget.channel.sink.add(_controller.text);
    }
  }

  @override
  void dispose() {
    widget.channel.sink.close();
    super.dispose();
  }
}

import 'package:flutter/material.dart';

Widget buildTapAbleWidget() {
  return new MaterialApp(
    title: "tap",
    home: new Scaffold(
        appBar: new AppBar(
          title: new Text("Process Tap"),
        ),
        body: Builder(builder: (BuildContext context) {
          return new ListView(
            children: _buildTapAbleChildren(context),
          );
        })),
  );
}

_buildTapAbleChildren(BuildContext context) {
  return [
    new Button(),
    new RaisedButton(
      onPressed: () {
        _showSnackbarCallback(context, "RaisedButton");
      },
      child: new Text("RaisedButton"),
    )
  ];
}

_showSnackbarCallback(BuildContext context, String msg) {
  final snackbar = new SnackBar(content: new Text(msg));
  Scaffold.of(context).showSnackBar(snackbar);
}

class Button extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new GestureDetector(
      onTap: () {
        _showSnackbarCallback(context, "CunstomTap");
      },
      child: new Container(
        padding: new EdgeInsets.all(12.0),
        decoration: new BoxDecoration(
          color: Theme.of(context).backgroundColor,
          borderRadius: new BorderRadius.circular(8.0),
        ),
        child: new Text("I am a button"),
      ),
    );
  }
}

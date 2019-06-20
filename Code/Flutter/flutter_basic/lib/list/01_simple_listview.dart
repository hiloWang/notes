import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

Widget buildSimpleList() {
  final title = 'Basic List';

  return new MaterialApp(
    title: title,
    home: new Scaffold(
      //顶部Bar
      appBar: new AppBar(
        title: new Text(title),
      ),
      //列表体
      body: new ListView(
        children: <Widget>[
          new ListTile(
            leading: new Icon(Icons.map),
            title: new Text('Map'),
          ),
          new ListTile(
            leading: new Icon(Icons.photo),
            title: new Text('Album'),
          ),
          new ListTile(
            leading: new Icon(Icons.phone),
            title: new Text('Phone'),
          ),
        ],
      ),
    ),
  );
}

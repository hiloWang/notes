import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

Widget buildLongListView() {
  return new LongListView(
    items: new List<String>.generate(10000, (i) => "Item $i"),
  );
}

class LongListView extends StatelessWidget {
  final List<String> items;

  LongListView({Key key, @required this.items}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final title = 'Long List';

    return new MaterialApp(
      title: title,
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text(title),
        ),
        body: new ListView.builder(
          itemCount: items.length,
          itemBuilder: (context, index) {
            return new ListTile(
              title: new Text('${items[index]}'),
            );
          },
        ),
      ),
    );
  }
}

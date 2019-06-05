import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

/*
“滑动删除”模式在移动应用中很常见。例如，如果我们正在编写一个电子邮件应用程序，我们希望允许我们的用户在列表中滑动电子邮件。
当他们这样做时，我们需要将该条目从收件箱移至垃圾箱。Flutter通过提供 Dismissible Widget 使这项任务变得简单。
*/
Widget buildDismissibleWidget() {
  return new MaterialApp(
    title: "Dismissible",
    home: new Scaffold(
      appBar: new AppBar(
        title: new Text("Dismissible"),
      ),
      body: DismissibleBody(),
    ),
  );
}

class DismissibleBody extends StatelessWidget {
  final items = new List<String>.generate(20, (i) => "Item ${i + 1}");

  @override
  Widget build(BuildContext context) {
    return new ListView.builder(
        itemCount: items.length,

        itemBuilder: (context, index) {

          final item = items[index];

          return new Dismissible(
              key: new Key(item),
              onDismissed: (direction) {
                items.removeAt(index);
                Scaffold.of(context).showSnackBar(
                    new SnackBar(content: new Text("$item dismissed")));
              },

              background: new Container(
                color: Colors.red,
              ),
              child: new ListTile(title: new Text('$item')));
        });
  }
}

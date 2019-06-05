import 'package:flutter/material.dart';

Widget buildConstrainedBoxWidget() {
  Widget redBox = DecoratedBox(
    decoration: BoxDecoration(color: Colors.red),
  );

  Widget greenBox = SizedBox(
    width: 300,
    height: 300,
    child: DecoratedBox(
      decoration: BoxDecoration(color: Colors.green),
    ),
  );

  return Center(
    child: Column(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        ConstrainedBox(
            constraints: BoxConstraints(minWidth: 60.0, minHeight: 60.0), //父
            child: ConstrainedBox(
              constraints: BoxConstraints(minWidth: 90.0, minHeight: 20.0), //子
              child: redBox,
            )),
        SizedBox(height: 100),
        ConstrainedBox(
            constraints: BoxConstraints(minWidth: 90.0, minHeight: 20.0),
            child: ConstrainedBox(
              constraints: BoxConstraints(minWidth: 60.0, minHeight: 60.0),
              child: redBox,
            )),
        SizedBox(height: 100),
        ConstrainedBox(
            constraints: BoxConstraints(maxWidth: 90.0, maxHeight: 20.0),
            child: ConstrainedBox(
              constraints: BoxConstraints(maxWidth: 60.0, maxHeight: 60.0),
              child: greenBox,
            ))
      ],
    ),
  );
}

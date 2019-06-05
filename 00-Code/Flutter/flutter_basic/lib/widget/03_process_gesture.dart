import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

Widget buildTapAbleButton() => new CustomButton();

class CustomButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Center(
      child: new GestureDetector(
        onTap: () {
          print("I am tapped");
        },
        child: new Container(
          height: 36,
          padding: const EdgeInsets.all(8.0),
          margin: const EdgeInsets.symmetric(horizontal: 8.0),
          decoration: new BoxDecoration(
            borderRadius: new BorderRadius.circular(5.0),
            color: Colors.lightGreen[500],
          ),
          child: new Center(
            child: new Text(
              'Engage',
              textDirection: TextDirection.ltr,
            ),
          ),
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';

Widget buildHelloWorldApp() {
  return new HelloWorldApp();
}

class HelloWorldApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: new Text(
        "Hello Flutter",
        textDirection: TextDirection.ltr,
      ),
    );
  }
}

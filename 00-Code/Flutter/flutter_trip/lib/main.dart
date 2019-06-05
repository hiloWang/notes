import 'package:flutter/material.dart';
import 'package:flutter_trip/navigator/tab_navigator.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Trip',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: TripApp(),
    );
  }
}

class TripApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return TabNavigator();
  }
}

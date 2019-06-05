import 'package:flutter/animation.dart';
import 'package:flutter/material.dart';


Widget buildZoomInLogoWidget() {
  return LogoApp();
}

class LogoApp extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return _LogoAppState();
  }

}

class _LogoAppState extends State<LogoApp> with SingleTickerProviderStateMixin {

  Animation<double> animation;
  AnimationController animationController;

  @override
  void initState() {
    super.initState();
    animationController = AnimationController(
        duration: const Duration(milliseconds: 2000),
        vsync: this);
    animation = Tween(begin: 0.0, end: 300.0).animate(animationController)
      ..addListener(() {
        setState(() {});
      });
    animationController.forward();
  }

  @override
  void dispose() {
    animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
        margin: EdgeInsets.symmetric(vertical: 10),
        height: animation.value,
        width: animation.value,
        child: new FlutterLogo(),
      ),
    );
  }

}
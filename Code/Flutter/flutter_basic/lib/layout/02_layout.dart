import 'package:flutter/material.dart';

/*
Flutter布局机制的核心就是widget。在Flutter中，几乎所有东西都是一个widget - 甚至布局模型都是widget。您在Flutter应用中看到的图像、图标和文本都是widget。
甚至你看不到的东西也是widget，例如行（row）、列（column）以及用来排列、约束和对齐这些可见widget的网格（grid）。
 */
//https://flutter.dev/docs/development/ui/layout/tutorial
//https://flutter.dev/docs/development/ui/interactive
Widget buildLayoutInteractiveWidget() {
  return LayoutApp();
}

class LayoutApp extends StatelessWidget {
  Column _buildButtonColumn(Color color, IconData icon, String label) {
    return Column(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            icon,
            color: color,
          ),
          Container(
              margin: const EdgeInsets.only(top: 8),
              child: Text(label,
                  style: TextStyle(
                      fontSize: 12, fontWeight: FontWeight.w400, color: color)))
        ]);
  }

  @override
  Widget build(BuildContext context) {
    Widget titleSection = Container(
      padding: const EdgeInsets.all(32),
      child: Row(
        children: [
          //first：column： 第一列占用大量空间，所以它必须包装在Expanded widget中。
          Expanded(
            child:
                Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              //top text
              Container(
                padding: const EdgeInsets.only(bottom: 8),
                child: Text('Oeschinen Lake Campground',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                    )),
              ),
              //bottom text
              Text(
                'Kandersteg, Switzerland',
                style: TextStyle(
                  color: Colors.grey[500],
                ),
              )
            ]),
          ),

          //second
          FavoriteWidget()
        ],
      ),
    );

    Color color = Theme.of(context).primaryColor;

    Widget buttonSection = Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          _buildButtonColumn(color, Icons.call, "CALL"),
          _buildButtonColumn(color, Icons.near_me, "ROUTE"),
          _buildButtonColumn(color, Icons.share, "SHARE")
        ],
      ),
    );

    Widget textSection = Container(
      padding: const EdgeInsets.all(32),
      child: Text(
        'Lake Oeschinen lies at the foot of the Blüemlisalp in the Bernese '
            'Alps. Situated 1,578 meters above sea level, it is one of the '
            'larger Alpine Lakes. A gondola ride from Kandersteg, followed by a '
            'half-hour walk through pastures and pine forest, leads you to the '
            'lake, which warms to 20 degrees Celsius in the summer. Activities '
            'enjoyed here include rowing, and riding the summer toboggan run.',
        //softwrap 属性表示文本是否应在软换行符（例如句点或逗号）之间断开。
        softWrap: true,
      ),
    );

    return MaterialApp(
      title: "Welcome to Flutter",
      home: Scaffold(
        appBar: AppBar(
          title: Text("Flutter layout Demo"),
        ),
        body: ListView(
          children: [
            Image.asset(
              'images/lake.jpg',
              width: 600,
              height: 240,
              fit: BoxFit.cover,
            ),
            titleSection,
            buttonSection,
            textSection
          ],
        ),
      ),
    );
  }
}

class FavoriteWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _FavoriteWidgetState();
}

class _FavoriteWidgetState extends State<FavoriteWidget> {
  bool _isFavorited = true;
  int _favoriteCount = 41;

  void _toggleFavorite() {
    setState(() {
      if (_isFavorited) {
        _favoriteCount -= 1;
        _isFavorited = false;
      } else {
        _favoriteCount += 1;
        _isFavorited = true;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: <Widget>[
        Container(
            padding: EdgeInsets.all(0),
            child: IconButton(
              icon: _isFavorited ? Icon(Icons.star) : Icon(Icons.star_border),
              color: Colors.red[500],
              onPressed: _toggleFavorite,
            )),
        SizedBox(
          width: 18,
          child: Container(
            child: Text("$_favoriteCount"),
          ),
        )
      ],
    );
  }
}

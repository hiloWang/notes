import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';

//Flutter入口，main函数使用了(=>)符号, 这是Dart中单行函数或方法的简写。
void main() => runApp(MyApp());

/*
该应用程序继承了 StatelessWidget，这将会使应用本身也成为一个widget。 在Flutter中，大多数东西都是widget，
包括对齐(alignment)、填充(padding)和布局(layout)

Stateless widgets 是不可变的, 这意味着它们的属性不能改变 - 所有的值都是最终的.
*/
class MyApp extends StatelessWidget {
  final wordPair = WordPair.random();

  //关于build方法：widget的主要工作是提供一个build()方法来描述如何根据其他较低级别的widget来显示自己。
  @override
  Widget build(BuildContext context) {
    //创建一个Material APP。Material是一种标准的移动端和web端的视觉设计语言。 Flutter提供了一套丰富的Material widgets。
    //MaterialApp 也是一个 Widget，其内置了 title, theme, home 等属性或子widget
    return MaterialApp(
        title: 'Welcome to Flutter',
        theme: ThemeData(
          primaryColor: Colors.white,
        ),
        home: RandomWords());
  }
}

/*
RandomWords除了创建 RandomWordsState，几乎没有其他功能。

Stateful widgets 持有的状态可能在widget生命周期中发生变化. 实现一个 stateful widget 至少需要两个类:

  一个 StatefulWidget类。
  一个 State类。 StatefulWidget类本身是不变的，但是 State类在widget生命周期中始终存在.
 */
class RandomWords extends StatefulWidget {
  //有状态的widget，需要实现 createState 方法并返回对应的 State。
  @override
  State createState() => new RandomWordsState();
}

class RandomWordsState extends State<RandomWords> {
  final _suggestions = <WordPair>[];
  final _biggerFont = const TextStyle(fontSize: 18.0);
  final _saved = new Set<WordPair>();

  void _pushSaved() {
    //在Flutter中，导航器管理应用程序的路由栈。将路由推入（push）到导航器的栈中，将会显示更新为该路由页面。
    // 从导航器的栈中弹出（pop）路由，将显示返回到前一个路由。
    Navigator.of(context)
        .push(new MaterialPageRoute<void>(builder: (BuildContext context) {
      //Iterable，获取所有喜欢的名字
      final Iterable<ListTile> tiles = _saved.map(
        (WordPair pair) {
          return new ListTile(
            title: new Text(
              pair.asPascalCase,
              style: _biggerFont,
            ),
          );
        },
      );
      //List，根据 tiles 的数量来创建相同数量的 Widget
      final List<Widget> divided = ListTile.divideTiles(
        context: context,
        tiles: tiles,
      ).toList();
      //返回一个列表视图
      return Scaffold(
        appBar: new AppBar(
          title: const Text("Saved suggestions"),
        ),
        body: new ListView(
          children: divided,
        ),
      );
    }));
  }

  //Status 要根据自己状态返回对应的 Widget
  @override
  Widget build(BuildContext context) {
    /*
    Scaffold 是 Material library 中提供的一个widget, 它提供了默认的导航栏、标题和包含主屏幕widget树的body属性。widget树可以很复杂。
    */
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Startup Name Generator'),
        actions: <Widget>[
          new IconButton(icon: const Icon(Icons.list), onPressed: _pushSaved),
        ], // ... to here.
      ),
      body: _buildSuggestions(),
    );
  }

  Widget _buildSuggestions() {
    return ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemBuilder: (context, i) {
          print('i = $i');
          //返回分割
          if (i.isOdd) return Divider();
          //返回条目
          final index = i ~/ 2; /*1 除以 2 并返回其整数结果*/
          if (index >= _suggestions.length) {
            _suggestions.addAll(generateWordPairs().take(10)); /*4*/
          }
          return _buildRow(_suggestions[index]);
        });
  }

  Widget _buildRow(WordPair pair) {
    final bool alreadySaved = _saved.contains(pair);

    return ListTile(
      title: Text(
        pair.asPascalCase,
        style: _biggerFont,
      ),
      //trailing 表示尾部
      trailing: new Icon(
        alreadySaved ? Icons.favorite : Icons.favorite_border,
        color: alreadySaved ? Colors.red : null,
      ),
      //事件处理
      onTap: () {
        //在Flutter的响应式风格的框架中，调用setState() 会为State对象触发build()方法，从而导致对UI的更新
        setState(() {
          if (alreadySaved) {
            _saved.remove(pair);
          } else {
            _saved.add(pair);
          }
        });
      },
    );
  }
}

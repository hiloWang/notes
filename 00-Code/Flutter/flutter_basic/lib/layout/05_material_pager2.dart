import 'package:flutter/material.dart';

Widget buildMaterialPagerWidget2() {
  return MaterialApp(title: "Material Pager", home: HomeWidget());
}

class HomeWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _HomeWidgetState();
  }
}

class _HomeWidgetState extends State<HomeWidget>
    with SingleTickerProviderStateMixin {
  //定义一个globalKey, 由于GlobalKey要保持全局唯一性，我们使用静态变量存储
  static GlobalKey<ScaffoldState> _globalKey = new GlobalKey();

  //用于控制/监听Tab菜单切换。
  TabController _tabController; //需要定义一个Controller

  List tabs = ["新闻", "历史", "图片"];

  @override
  void initState() {
    super.initState();
    // 创建Controller
    _tabController = TabController(length: tabs.length, vsync: this);
    _tabController.addListener(() {
      switch (_tabController.index) {
      }
    });
  }

  void _onAdd() {}

  void _openDraw(BuildContext context) {
    Scaffold.of(context).openDrawer();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _globalKey,

      //顶部
      appBar: AppBar(
        //标题
        title: Text("Material Pager"),
        //导航栏最左侧Widget，常见为抽屉菜单按钮或返回按钮。
        leading: Builder(builder: (context) {
          return IconButton(
            icon: Icon(Icons.dashboard, color: Colors.white),
            onPressed: () {
              _openDraw(context);
            },
          );
        }),
        // 导航栏右侧菜单
        actions: <Widget>[
          IconButton(icon: Icon(Icons.share), onPressed: () {})
        ],
        //生成Tab菜单
        bottom: TabBar(
          tabs: tabs.map((e) => Tab(text: e)).toList(),
          controller: _tabController,
        ),
      ),

      //抽屉
      drawer: _CustomDrawer(),

      //底部导航栏
      bottomNavigationBar: BottomAppBar(
        color: Colors.white,
        shape: CircularNotchedRectangle(), // 底部导航栏打一个圆形的洞
        child: Row(
          children: [
            IconButton(icon: Icon(Icons.home), onPressed: () {}),
            SizedBox(), //中间位置空出
            IconButton(icon: Icon(Icons.business), onPressed: () {}),
          ],
          mainAxisAlignment: MainAxisAlignment.spaceAround, //均分底部导航栏横向空间
        ),
      ),

      //打洞的位置取决于FloatingActionButton的位置
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,

      //浮动按钮
      floatingActionButton: FloatingActionButton(
        onPressed: _onAdd,
        child: Icon(Icons.add),
      ),

      //主页面
      // 通过TabBar我们只能生成一个静态的菜单，如果要实现Tab页，我们可以通过TabController去监听Tab菜单的切换去切换Tab页
      //如果我们Tab页可以滑动切换的话，还需要在滑动过程中更新TabBar指示器的偏移。显然，要手动处理这些是很麻烦的，为此，
      // Material库提供了一个TabBarView组件，它可以很轻松的配合TabBar来实现同步切换和滑动状态同步。
      //Material组件库也提供了一个PageView Widget，它和TabBarView功能相似，类似 Android 中的 ViewPager。
      body: TabBarView(
          controller: _tabController,
          children: tabs
              .map((e) => Container(
                  alignment: Alignment.center,
                  child: Text(e, textScaleFactor: 5)))
              .toList()),
    );
  }
}

class _CustomDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: MediaQuery.removePadding(
        context: context,
        // DrawerHeader consumes top MediaQuery padding.
        removeTop: true,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.only(top: 38.0),
              child: Row(
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16.0),
                    child: ClipOval(
                      child: Image.asset(
                        "imgs/avatar.png",
                        width: 80,
                      ),
                    ),
                  ),
                  Text(
                    "Wendux",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  )
                ],
              ),
            ),
            Expanded(
              child: ListView(
                children: <Widget>[
                  ListTile(
                    leading: const Icon(Icons.add),
                    title: const Text('Add account'),
                  ),
                  ListTile(
                    leading: const Icon(Icons.settings),
                    title: const Text('Manage accounts'),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

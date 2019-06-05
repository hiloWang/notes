import 'package:flutter/material.dart';
import 'package:flutter_swiper/flutter_swiper.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

const APPBAR_SCROLL_OFFSET = 100;

class _HomePageState extends State<HomePage> {
  List<String> _imageUrls = [
    "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1054598549,1073845993&fm=11&gp=0.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556887233339&di=5cfd6fca316145b15772174465c04f76&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F1a221dcf350a657e91a23643f69985947f38e1bd390e4-1dOD2z_fw658",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556887233339&di=90d470dc5fadd2ae483b61a79ce20216&imgtype=0&src=http%3A%2F%2Fs3.sinaimg.cn%2Fmw690%2F003YlLWpty6El1U0Cye82%26690"
  ];

  var appBarAlpha = 1.0;

  @override
  Widget build(BuildContext context) {
    _onScroll(offset) {
      double alpha = offset / APPBAR_SCROLL_OFFSET;
      if (alpha < 0) {
        alpha = 0;
      } else if (alpha > 1) {
        alpha = 1;
      }
      setState(() {
        appBarAlpha = alpha;
      });
      print(appBarAlpha);
    }

    return Scaffold(
      body: Stack(
        children: <Widget>[
          //ScrollContent
          //MediaQuery 移除顶部安全区域，比如 StatusBar
          MediaQuery.removePadding(
            context: context,
            removeTop: true,
            //NotificationListener 监听滚动，在回调函数  onNotification 中处理监听。NotificationListener 会监听所有子组件的滚顶信息
            child: NotificationListener(
              onNotification: (scrollNotification) {
                //通过过滤 onNotification 方法的实参类型来过滤事件，比如 `notificationEvent is ScrollUpdateNotification`
                // 表示只关心真正触发滑动事才生的事件(引入 Widget 初始化时 onNotification 也会被框架调用)。
                //scrollNotification.depth 控制监听滚动的深度，比如 `scrollNotification.depth==0` 表示当第 0 个元素滚动时才进行处理(过滤Swiper的滑动事件)
                if (scrollNotification is ScrollUpdateNotification &&
                    scrollNotification.depth == 0) {
                  print('${scrollNotification.metrics.pixels}');
                  _onScroll(scrollNotification.metrics.pixels);
                }
              },
              child: ListView(
                children: [

                  //banner
                  Container(
                    height: 160,
                    //轮播图开源组件：[flutter_swiper](https://github.com/best-flutter/flutter_swiper)
                    child: Swiper(
                      itemCount: _imageUrls.length,
                      autoplay: true,
                      pagination: SwiperPagination(),
                      control: new SwiperControl(),
                      itemBuilder: (BuildContext context, int index) {
                        return Image.network(
                          _imageUrls[index],
                          fit: BoxFit.fill,
                        );
                      },
                    ),
                  ),
                  //content
                  Container(
                    height: 800,
                    child: Text("haha"),
                  )
                ],
              ),
            ),
          ),

          //AppBar
          Opacity(
            opacity: appBarAlpha,
            child: Container(
              height: 80,
              decoration: BoxDecoration(color: Colors.white),
              child: Center(
                child: Padding(
                  padding: EdgeInsets.only(top: 20),
                  child: Text("首页"),
                ),
              ),
            ),
          )
        ],
      ),
    );
  }
}

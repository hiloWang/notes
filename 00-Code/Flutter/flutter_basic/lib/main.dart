import 'package:flutter/material.dart';
import 'package:flutter_basic/widget/00_hello_world.dart';
import 'package:flutter_basic/widget/01_basic_widget.dart';
import 'package:flutter_basic/widget/02_material_widget.dart';
import 'package:flutter_basic/widget/03_process_gesture.dart';
import 'package:flutter_basic/widget/04_stateful_widget.dart';
import 'package:flutter_basic/widget/05_shopping_cart.dart';
import 'package:flutter_basic/widget/06_inherited_widget.dart';
import 'package:flutter_basic/image/01_display_images.dart';
import 'package:flutter_basic/list/01_simple_listview.dart';
import 'package:flutter_basic/list/02_horizontal_listview.dart';
import 'package:flutter_basic/list/03_long_listview.dart';
import 'package:flutter_basic/list/04_multi_type_listview.dart';
import 'package:flutter_basic/list/05_grid.dart';
import 'package:flutter_basic/list/06_load_more_list.dart';
import 'package:flutter_basic/list/07_sliver_scroll.dart';
import 'package:flutter_basic/list/08_notification_listener.dart';
import 'package:flutter_basic/gesture/01_tap.dart';
import 'package:flutter_basic/gesture/02_InkWell.dart';
import 'package:flutter_basic/gesture/03_dismissible.dart';
import 'package:flutter_basic/gesture/04_draw.dart';
import 'package:flutter_basic/navigator/01_simple_navigator.dart';
import 'package:flutter_basic/navigator/02_pass_values.dart';
import 'package:flutter_basic/navigator/03_return_values.dart';
import 'package:flutter_basic/network/01_http.dart';
import 'package:flutter_basic/network/02_web_socket.dart';
import 'package:flutter_basic/network/03_loading_status.dart';
import 'package:flutter_basic/layout/01_layout.dart';
import 'package:flutter_basic/layout/02_layout.dart';
import 'package:flutter_basic/layout/03_constrained_box.dart';
import 'package:flutter_basic/layout/04_material_pager1.dart';
import 'package:flutter_basic/layout/05_material_pager2.dart';
import 'package:flutter_basic/animation/01_zoomin_logo.dart';
import 'package:flutter_basic/animation/02_animated_widget.dart';

void main() => runApp(new FlutterBasicWidget());

List<Page> _buildRoutes() {
  return [
    //basic widget
    Page("HelloWorld", (BuildContext context) => buildHelloWorldApp()),
    Page("BasicWidget", (BuildContext context) => buildBasicWidgetApp()),
    Page("MaterialWidget", (BuildContext context) => buildMaterialWidget()),
    Page("TapAbleButton", (BuildContext context) => buildTapAbleButton()),
    Page("StatefulWidget", (BuildContext context) => buildStatefulWidget()),
    Page("ShoppingCartList", (BuildContext context) => buildShoppingCartList()),
    Page("InheritedWidget", (BuildContext context) => buildInheritedWidget()),
    //basic image
    Page("ImageList", (BuildContext context) => buildImageList()),
    //basic list
    Page("SimpleList", (BuildContext context) => buildSimpleList()),
    Page("HorizontalListView",
        (BuildContext context) => buildHorizontalListView()),
    Page("LongListView", (BuildContext context) => buildLongListView()),
    Page("MultiListView", (BuildContext context) => buildMultiListView()),
    Page("GridViewWidget", (BuildContext context) => buildGridViewWidget()),
    Page("InfiniteListView",
            (BuildContext context) => buildInfiniteListView()),
    Page("CustomScrollView",
            (BuildContext context) => buildCustomScrollView()),
    Page("ScrollNotificationWidget",
            (BuildContext context) => buildScrollNotificationWidget()),
    //basic gesture
    Page("TapAbleWidget", (BuildContext context) => buildTapAbleWidget()),
    Page("InkWellWidget", (BuildContext context) => buildInkWellWidget()),
    Page("DismissibleWidget",
        (BuildContext context) => buildDismissibleWidget()),
    Page("DrawableWidget", (BuildContext context) => buildDrawableWidget()),
    //basic navigator
    Page("SimpleNavigatorWidget",
        (BuildContext context) => buildSimpleNavigatorWidget()),
    Page("PassValuesNavigatorWidget",
        (BuildContext context) => buildPassValuesNavigatorWidget()),
    Page("ReturningValuesWidget",
        (BuildContext context) => buildReturningValuesWidget()),
    //net work
    Page("HttpRequestingWidget",
        (BuildContext context) => buildHttpRequestingWidget()),
    Page("WebSocketWidget", (BuildContext context) => buildWebSocketWidget()),
    Page("LoadingStatusWidget",
        (BuildContext context) => buildLoadingStatusWidget()),
    //layout
    Page("LayoutDemoWidget", (BuildContext context) => buildLayoutWidget()),
    Page("LayoutDemoInteractiveWidget",
        (BuildContext context) => buildLayoutInteractiveWidget()),
    Page("ConstrainedBoxWidget",
        (BuildContext context) => buildConstrainedBoxWidget()),
    Page("MaterialPagerWidge1t",
        (BuildContext context) => buildMaterialPagerWidget1()),
    Page("MaterialPagerWidget2",
        (BuildContext context) => buildMaterialPagerWidget2()),
    //Animation
    Page("ZoomInLogoWidget", (BuildContext context) => buildZoomInLogoWidget()),
    Page("ZoomInLogoAnimatedWidget",
        (BuildContext context) => buildZoomInLogoAnimatedWidget()),
  ];
}

class FlutterBasicWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: "Flutter Basic",
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text("Flutter Basic"),
        ),
        body: _buildBody(_buildRoutes()),
      ),
    );
  }
}

class Page {
  String name;
  WidgetBuilder builder;

  Page(this.name, this.builder);
}

_buildBody(List<Page> routes) {
  return ListView.builder(
      itemCount: routes.length,
      itemBuilder: (BuildContext context, int index) {
        return new ListTile(
          title: new Text(routes[index].name),
          trailing: new RaisedButton(
              child: new Text("Go"),
              onPressed: () {
                Navigator.push(context,
                    new MaterialPageRoute(builder: routes[index].builder));
              }),
        );
      });
}

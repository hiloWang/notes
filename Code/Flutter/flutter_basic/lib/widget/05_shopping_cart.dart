import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

Widget buildShoppingCartList() => new MaterialApp(
    title: "Shopping Center",
    home: new ShoppingCartList(
      products: [
        new Product(name: 'Eggs'),
        new Product(name: 'Flour'),
        new Product(name: 'Shoes'),
        new Product(name: 'Car'),
        new Product(name: 'Gun'),
        new Product(name: 'Clothes'),
        new Product(name: 'Trouser'),
        new Product(name: 'Water'),
        new Product(name: 'Phone'),
        new Product(name: 'Computer'),
        new Product(name: 'Card'),
        new Product(name: 'Screnn'),
        new Product(name: 'Game'),
        new Product(name: 'Chocolate chips'),
      ],
    ));

class ShoppingCartList extends StatefulWidget {
  final List<Product> products;

  /*
  key：https://docs.flutter.io/flutter/foundation/Key-class.html
  GlobalKey：https://docs.flutter.io/flutter/widgets/GlobalKey-class.html

   key：可以使用key来控制框架将在widget重建时与哪些其他widget匹配。默认情况下，框架根据它们的runtimeType和它们的显示顺序来匹配。
   使用key时，框架要求两个widget具有相同的key和runtimeType。

   Key在构建相同类型widget的多个实例时很有用。例如，ShoppingList构建足够的ShoppingListItem实例以填充其可见区域：
   1. 如果没有key，当前构建中的第一个条目将始终与前一个构建中的第一个条目同步，即使在语义上，列表中的第一个条目如果滚动出屏幕，
   那么它将不会再在窗口中可见。
   2. 通过给列表中的每个条目分配为“语义” key，无限列表可以更高效，因为框架将同步条目与匹配的语义key并因此具有相似（或相同）的可视外观。
   此外，语义上同步条目意味着在有状态子widget中，保留的状态将附加到相同的语义条目上，而不是附加到相同数字位置上的条目。


   全局 Key：可以使用全局key来唯一标识子widget。全局key在整个widget层次结构中必须是全局唯一的，这与局部key不同，后者只需要在同级中唯一。
   由于它们是全局唯一的，因此可以使用全局key来检索与widget关联的状态。
   */
  ShoppingCartList({Key key, this.products}) : super(key: key);

  @override
  State<StatefulWidget> createState() => new _ShoppingCartListStatus();
}

class Product {
  const Product({this.name});

  final String name;
}

class _ShoppingCartListStatus extends State<ShoppingCartList> {
  //用来记录是否在购物车中
  Set<Product> _shoppingCart = new Set<Product>();

  /*
  在StatefulWidget调用createState之后，框架将新的状态对象插入树中，然后调用状态对象的initState。
  子类化State可以重写initState，以完成仅需要执行一次的工作。
   */
  @override
  void initState() {
    super.initState();
    print('initState');
  }

  void _handleCartChanged(Product product, bool inCart) {
    setState(() {
      if (inCart)
        _shoppingCart.add(product);
      else
        _shoppingCart.remove(product);
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("List"),
        ),
        body: new ListView(
            padding: new EdgeInsets.symmetric(vertical: 8.0),
            children: widget.products.map((Product product) {
              return new ShoppingListItem(
                product: product,
                inCart: _shoppingCart.contains(product),
                onCartChanged: _handleCartChanged,
              );
            }).toList()));
  }
}

typedef void CartChangedCallback(Product product, bool inCart);

class ShoppingListItem extends StatelessWidget {
  final Product product;
  final bool inCart;
  final CartChangedCallback onCartChanged;

  ShoppingListItem({Product product, this.inCart, this.onCartChanged})
      : product = product,
        super(key: new ObjectKey(product));

  @override
  Widget build(BuildContext context) {
    return new ListTile(
      onTap: () {
        onCartChanged(product, !inCart);
      },
      leading: new CircleAvatar(
        backgroundColor: _getColor(context),
        child: new Text(product.name[0]),
      ),
      title: new Text(product.name, style: _getTextStyle(context)),
    );
  }

  Color _getColor(BuildContext context) {
    return inCart ? Colors.black54 : Theme.of(context).primaryColor;
  }

  TextStyle _getTextStyle(BuildContext context) {
    if (!inCart) {
      return null;
    }
    return new TextStyle(
      color: Colors.black54,
      decoration: TextDecoration.lineThrough,
    );
  }
}

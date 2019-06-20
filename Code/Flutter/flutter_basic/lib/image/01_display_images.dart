import 'package:flutter/material.dart';
import 'package:transparent_image/transparent_image.dart';
import 'package:cached_network_image/cached_network_image.dart';

Widget buildImageList() {
  return new MaterialApp(
    title: "Show Image",
    home: new Scaffold(
      appBar: new AppBar(
        title: new Text("Imges"),
      ),
      body: new ListView(
        children: _createDifferentImages(),
      ),
    ),
  );
}

_createDifferentImages() {
  return [
    new Image.network(
      'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555862751146&di=0fe9c2c945415ca091cb94c72892ea87&imgtype=0&src=http%3A%2F%2Fp1.meituan.net%2Fbeautyimgout%2F09b80448b9174a87408bf70658de2c13220571.jpg%2540700w_700h_0e_1l%257Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20',
    ),

    new Divider(),
    new FadeInImage.memoryNetwork(
      placeholder: kTransparentImage,
      image:
          'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555862751146&di=bfe1c78a30f0ca6614dd5bf6dba8cc77&imgtype=0&src=http%3A%2F%2Fp1.meituan.net%2Fdpdeal%2Fb8e9f763f8b2a99a4e1cab4826d55b3b812941.jpg%2540640w_1024h_1e_1l%257Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20',
    ),

    new Divider(),
    new CachedNetworkImage(
      imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555862751146&di=bfe1c78a30f0ca6614dd5bf6dba8cc77&imgtype=0&src=http%3A%2F%2Fp1.meituan.net%2Fdpdeal%2Fb8e9f763f8b2a99a4e1cab4826d55b3b812941.jpg%2540640w_1024h_1e_1l%257Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20",
      placeholder: (context, url) => new CircularProgressIndicator(),
      errorWidget: (context, url, error) => new Icon(Icons.error),
    ),
  ];
}

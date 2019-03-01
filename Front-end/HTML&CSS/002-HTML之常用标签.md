# HTML之常用标签

---
## 1 基本语法

- HTML 对换行不敏感，对 tab 不敏感
- **空白折叠现象**：HTML中所有的文字之间，如果有空格、换行、tab都将被折叠为一个空格显示。
- 标签要严格封闭

---
## 2 常用标签

具体参考[W3 HTML 参考手册](http://www.w3school.com.cn/tags/index.asp)，HTML 标签可以分为 **容器级的标签** 和 **文本级的标签**。

- 常见容器级的标签: `div h ul ol dl li dt dd ...`
- 常见文本级的标签:`span p buis strong em ins del ...`

它们的区别在于：

- 容器级的标签中可以嵌套其它所有的标签
- 文本级的标签中只能 **嵌套文字/图片/超链接**

### head 标签

头标签都放在`<head></head>`头部分之间，该标签中的内容不会在网页中直接显示，该标签用于帮助浏览器解析页面，头标签包括以下子标签：

- `<title>`：用来设置网页的标题，默认会在浏览器的标题栏中显示，搜索引擎检索网页时，会主要检索title中的内容，它会影响到页面在搜索引擎中的排名。
- `<base>`：为页面上的所有链接规标题栏显示的内容定默认地址或默认目标。
- `<meta>`：用来设置网页的元数据，比如网页使用的字符集。
- `<link>`：定义文档与外部资源的关系。

### h 系列

在html中一共有六级标题，六级标题中，h1 最重要，h6 最不重要，一般页面中只会使用 h1~h3，h1 的重要性仅次于 title，浏览器也会主要检索 h1 中的内容，以判断页面的主要内容，一般一个页面中只能写一个h1。

### p 标签

段落，是英语 paragraph “段落” 缩写。HTML标签是分等级的.

HTML将所有的标签分为两种：**容器级、文本级。**

- 容器级的标签，里面可以放置任何东西；
- 文本级的标签里面，只能放置**文字、图片、表单元素**。

**p标签是一个文本级标签**，里面只能放文字、图片、表单元素。其他的一律不能放。

### img 标签

- 图片标签为 `img`，img 即 image，img 标签式自封闭的，仅代表图片。
- alt属性：alt是英语 `alternate`“ 替代” 的意思，就表示不管因为什么原因，当这个图片无法被显示的时候，出现的替代文字，搜索引擎主要通过该属性来识别图片的内容，如果不写该属性则搜索引擎会对图片进行收录。

### 超链接

- a是英语anchor“锚”的意思
- href是英语hypertext reference超文本地址的缩写
- 页面内的锚点`<a name="wdzp">我的作品</a>`，`<a href="#wdzp">`
- a是一个文本级的标签

### 列表

无序列表

 - ul就是英语unordered list，“无序列表”的意思
 - li 就是英语list item ， “列表项”的意思。
  - 所有的li不能单独存在，必须包裹在ul里面；反过来说，ul的“儿子”不能是别的东西，只能有li。
 - ul的作用，并不是给文字增加小圆点的，而是增加无序列表的“语义”的
 - li是一个容器级标签，li里面什么都能放

有序列表

- ordered list  有序列表，用ol表示

定义列表

- dl表示definition list 定义列表
- dt表示definition title    定义标题
- dd表示definition description 定义表述词儿
- dt、dd只能在dl里面；dl里面只能有dt、dd
- dt、dd都是容器级标签，想放什么都可以。

### div 和 span

div和span是非常重要的标签，div的语义是division“分割”； span的语义就是span“范围、跨度”，这两个东西，都是最重要的**“盒子”**

- div在浏览器中，默认是不会增加任何的效果改变的，但是语义变了，div中的所有元素是一个小区域
- div标签是一个容器级标签，里面什么都能放，甚至可以放div自己
- span也是表达“小区域、小跨度”的标签，但是是一个“文本级”的标签。
- span里面只能放置文字、图片、表单元素。 span里面不能放`p、h、ul、dl、ol、div。`
- span里面是放置小元素的，div里面放置大东西的

### 表单

表单标签：`<form>`用于与服务器端的交互表单标签：`<form>`用于与服务器端的交互，`<input>`：输入标签 ,接收用户输入`<input>`：输入标签 ，接收用户输入。

input包括以下类型：

- 文本框：`text`
- 密码框：`password`
- 单选按钮：`radio`
- 复选框：`checkbox`
- 下拉列表：`select`
- 多行文本框：`textarea`
- 三种按钮和 label：
    - button
    - submit
    - reset
    - label

### 表格

- table标签常用属性：`border、width、height、align、bgcolor、cellpadding、cellspacing`
- caption 用于设置标题
- 一般情况下 thead、 tbody、 tfoot 不会被用到
- thead、 元素应该与 tbody 和 tfoot 元素结合起来使用。
- tbody 元素用于对 HTML 表格中的主体内容进行分组，而 tfoot 元素用于对 HTML 表格中的表注（页脚）内容进行分组。
- 如使用 thead、tfoot 以及 tbody 元素，就必须使用全部的元素。它们的出现次序是：thead、tfoot、tbody，这样浏览器就可以在收到所有数据前呈现页脚了。必须在 table 元素内部使用这些标签。

### 文本标签

- `<em>`：表示语气上的强调
- `<strong>`：表示内容的重要性
- `<i>`：表示单纯的斜体
- `<b>`：表示单纯的加粗
- `<small>`：表示细则一类的内容
- `<cite>`：表示参考的内容，凡是加书名号的都可以使用cite
- `<q>`：短引用，行内引用
- `<blockquote>`：长引用，块级引用
- `<sup>`：上标
- `<sub>`：下标
- `<del>`：删除的内容
- `<ins>`：插入的内容
- `<pre>`：预格式标签，可以保留代码中空格换行这些格式
- `<code>   `：表示程序代码

------

## 3 meta data

meta 是 head 的子标签，meta表示**元**。**元配置**，就是表示基本的配置项目

### 3.1 字符集

字符集用 meta 标签定义：

```html
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
```

中文能够使用的字符集两种:

```html
    第一种：UTF-8
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    第二种：gb2312
        <meta http-equiv="Content-Type" content="text/html;charset=gb2312">
    也可以写成gbk(不推荐)
        <meta http-equiv="Content-Type" content="text/html;charset=gbk">
```

> http-equiv，相当于http的文件头作用，它可以向浏览器传回一些有用的信息，以帮助正确和精确地显示网页内容

**我们用meta标签可以声明当前这个html文档的字库，但是一定要和保存的类型一样，否则乱码！**。

两种字符集的优缺点

- UTF-8 字多，有各种国家的语言，但是保存尺寸大，文件臃肿；
- gb2312字少，只用中文和少数外语和符号，但是尺寸小，文件小巧。

### 3.2 关键字和页面描述

meta 除了可以设置字符集，还可以设置关键字和页面描述。

#### 设置页面描述

```html
    <meta name="Description" content="网易是中国领先的互联网技术公司，为用户提供免费邮箱、游戏、搜索引擎服务，开设新闻、娱乐、体育等30多个内容频道，及博客、视频、论坛等互动交流，网聚人的力量。" />
```

只要设置的 Description 页面面熟，那么百度搜索结果，就能够显示这些语句，**这个技术叫做SEO，search engine optimization**，搜索引擎优化。

#### 定义关键词

```html
    <meta name="Keywords" content="网易,邮箱,游戏,新闻,体育,娱乐,女性,亚运,论坛,短信" />
```

这些关键词，就是告诉搜索引擎，这个网页是干嘛的，能够提高搜索命中率。让别人能够找到你，搜索到你。Keywords就是“关键词”的意思。

#### 请求的重定向

```html
<meta http-equiv="refresh" content="秒数;url=地址"  />
```

---

## 4 HTML杂项

### HTML标签属性

HTML中的标签有许多属性，用于控制标签的展示样式 或 提供有关 HTML 元素的更多的信息

### HTML注释

`<!-- 注释内容 -->`

### 特殊字符实体

用于展示一些特殊的字符 或 转义html语法使用的字符，具体参考 [W3 HTML ISO-8859-1 参考手册](http://www.w3school.com.cn/tags/html_ref_entities.html)

### HTML废弃标签介绍

HTML 现在只负责语义，不负责样式，但是 HTML 初期，也负责标签的样式。经过 WEB 技术的发展与规范，现在这些样式的标签，都已经被废弃。推荐使用标准的 `div+css` 构建页面，常用的标签种类 `div p h1 span a img ul ol dl input`。

不推荐使用的标签如下：

- 表现性元素：`basefont、big、center、font、s、strike、tt、u`，建议用语义正确的元素代替，并使用CSS 来确保渲染后的效果。
- 框架类元素：`frame、frameset、noframes`，内联框架中的内容不会被搜索引擎所检索，因框架有很多可用性及可访问性问题，HTML5 规范将这些元素移除，但 HTML5 支持 iframe。
- 属性类：
    - align
    - body标签上的 link、vlink、alink、text 属性
    - bgcolor
    - height 和 width
    - iframe 元素上的 scrolling 属性
    - valign
    - hspace 和 vspace
    - table 标签上的 cellpadding、cellspacing 和 border 属性
    - header 标签上的 profile 属性
    - 链接标签 a 上的 target 属性
    - img 和 iframe 元素的 longdesc 属性
- 其他：`em 、br 、hr 、font 、i 、b`

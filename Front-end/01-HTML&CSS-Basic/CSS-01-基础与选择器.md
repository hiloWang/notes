# CSS 基础与选择器

现在的互联网前端三层：

- `HTML`    超文本标记语言    从语义的角度描述页面结构。
- `CSS`        层叠式样式表    从审美的角度负责页面样式。
- `JS`        JavaScript        从交互的角度描述页面行为。

**css**就是`cascading style sheet(层叠式样式表)`的简写。css的最新版本是`css3`我们写css的地方是style标签，就是“样式”的意思，写在head里面。当然css也可以写在单独的文件里面。

------------
## 1 常见的属性

> css 的样式不需要死记，需要的时候查文档即可

### 1.1 字符颜色

```
    color:red;
    color属性的值，可以是英语单词，比如red、blue、yellow等等；也可以是rgb、十六进制。color就是前景色。
```

### 1.2 字号大小

```css
font-size:40px;
font就是“字体”，size就是“尺寸”。px是“像素”，必须加单位。
```

### 1.3 背景颜色

```css
background-color: blue;
background就是“背景”。
```

### 1.4 字体样式

通过 font-family 可以指定标签中文字使用 的字体，一般来说只有用户计算机中安装了我们指 定的字体时，它才会显示，否则这行代码 是没有意义的，通过font-family可以同时指定多个字体。 例如：

```css
p{
	font-family:Arial , Helvetica , sans-serif
}
```

浏览器会优先使用第一 个，如果没有找到则使用第二个，以此类推。 这里面 sans-serif 并不是指的具体某一个字 体。而是一类字体。字体分类有很多，比如 serif（衬线字体）、sans-serif（非衬线字体）、monospace （等宽字体）、cursive （草书字体）、fantasy （虚幻字体） 等，这些分类都是一些大的分类，并没有 涉及具体的类型，如果将字体指定为这些 格式，浏览器会自己选择指定类型的字体。

其他文本样式：

```css
//weight，weight 是“重量”的意思，bold 加粗；normal 不加粗。
font-weight: bold;

//style，italic “斜体”；normal 正常。
font-style: italic;

//decoration就是“装饰”的意思，可选值：underline、 overline、line-through、none 
text-decoration: underline;

//font-variant 属性可以将字母类型设置为小型大写，在该样式中，字母看起来像是稍 微缩小了尺寸的大写字母。
font-variant:small-caps

//字体属性的简写，font可以一次性同时设置多个字体的样式，但是大小和字体 必须写且必须写到后两个。
font:加粗 斜体 小型大写 大小/行高 字体 

//大小写 
text-transform:uppercase 
text-tansform:lowercase 
text-transform:capitalize //首字母大
text-transform:none
```

- 行间距： 在 CSS 并没有为我们提供一个直接设置行间距的方式，我们只能通过设置行高来间接的设置行间距，line-height 用于设置行高，行高越大则行 间距越大，行间距 = `line-height – font-size`
- 字母间距和单词间距 ：
  - letter-spacing 用来设置字符之间的间距。 
  -  word-spacing 用来设置单词之间的间距。 
  - 这两个属性都可以直接指定一个长度或百 分数作为值。正数代表的是增加距离，而 负数代表减少距离。
- 对齐文本：text-align 用于设置文本的对齐方式，可选值： 
  - left：左对齐 
  - right：右对齐
  - justify：两边对齐 
  -  center：居中对齐
- 首行缩进：text-indent 用来设置首行缩进，该样式需要指定一个长度，并且只对第一 行生效。

### 1.5 单位 

- px：如果我们将一个图片放大的话，我们会发现一个图片 是有一个一个的小色块构成的，这一个小色块就是一 个像素，也就是 1px，对于不同的显示器来说一个像素 的大小是不同的。 
-  百分比 ：也可以使用一个百分数来表示一个大小，百分比是相 对于父元素来说的，如果父元素使用的大小是 16px， 则 100% 就是 16px，200%就是 32px。 
- em：em和百分比类似，也是相对于父元素说的，1em 就相 当于100%，2em 相当于200%，1.5em 相当于150%。

### 1.6 颜色 

在 CSS 中可以直接使用颜色的关键字来代表 一种颜色。 

- 17 种颜色

```
aqua、black、blue、fuchsia、gray、green、lime、maroon、navy、olive、orange、 purple、red、silver、teal、white、yellow
```

- 有 147 种 svg 颜色。
- 十六进制颜色：用的最多的颜色是十六进制符号。比如`#6600FF`。
- RGB 值：也可以使用计算机中常用的 RGB 值来表示 颜色。可以使用 0~255 的数值，也可以使 用 0%~100% 的百分比数。 ` RGB(100%,0%,0%) ` 或 ` RGB(0,255,0) ` 。
- RGBA 值：RGBA 表示一个颜色和 RGB 类似，只不过比 RGB 多了一个A（alpha）来表示透明度， 透明度需要一个0-1的值。0表示完全透明，1表示完全不透明。比如： `RGBA(255,100,5,0.5)`。

------------
## 2 基础选择器

css 怎么学？很简单，有两个知识部分：

- 选择器，怎么选？
- 属性，样式是什么？

选择器（selector）用于告诉浏览器：网页上的哪些元素需要设置什么样的样式。 比如 p 这个选择器就表示选择页面中的所 有的 p 元素，在选择器之后所设置的样式会 应用到所有的 p 元素上。

### 2.1 标签选择器：`标签名`

1. 标签选择器就是标签的名字
2. 所有的标签，都可以是选择器。比如 `ul、li、label、dt、dl、input`
3. 标签选择器，选择的是页面上所有这种类型的标签，所以经常描述“共性”，无法描述某一个元素的“个性”的

```css
    <style type="text/css">
        span {
            color:red;
        }
    </style>
```

### 2.2 id 选择器：`#`

html
```html
    <p>我是段落1</p>
    <p id="para2">我是段落2</p>
    <p>我是段落3</p>
```
css：
```css
    <style type="text/css">
        #para2{
            color:red;
        }
    </style>
```
1. id 选择器的选择符是 `#`。
2. 任何的 HTML 标签都可以有 id 属性。表示这个标签的名字。
3. 这个标签的名字，可以任取，但是：
   1. 只能有字母、数字、下划线
   2. 必须以字母开头
   3. 不能和标签同名。比如id不能叫做`body、img、a`
   4. 大小写严格区别，也就是说mm和MM是两个不同的id。

**一个HTML页面，不能出现相同的id，哪怕他们不是一个类型。比如页面上有一个 id 为 pp 的 p，一个 id 为 pp 的div，是非法的！**


###  2.3 class 选择器：`.`

点号——**`.`**就是类选择器的符号。类的英语叫做class。所谓的类，就是class属性，class属性和id非常相似，**任何的标签都可以携带class属性。**

- class属性可以重复
- 同一个标签，可能同时属于多个类，用空格隔开

**类的使用，能够决定一个人的css水平。**:

- 不要去试图用一个类名，把某个标签的所有样式写完。这个标签要多携带几个类，共同造成这个标签的样式。
- 每一个类要尽可能小，有“公共”的概念，能够让更多的标签使用。

**到底用id还是用class？**:尽可能的用class，除非极特殊的情况可以用id。因为id是js用的。也就是说，js要通过id属性得到标签，所以我们css层面尽量不用id，要不然js就很别扭。另一层面，我们会认为一个有id的元素，有动态效果。

<font color="red">**一个标签，可以同时被多种选择器选择，标签选择器、id选择器、类选择器。这些选择器都可以选择上同一个标签，从而影响样式，这就是 css 的 cascading “层叠式”的第一层含义。**</font>

------------
## 3  CSS 高级选择器

### 3.1  后代选择器：`空格`

**空格**就表示后代，`.div1 p `就是`.div1`的后代所有的`p`,注意**选择的是后代，不一定是儿子**，空格可以多次出现。

后代选择器，就是一种平衡：**共性、特性的平衡**。当要把某一个部分的所有的 xx 进行样式改变，就要想到后代选择器。**后代选择器，描述的是祖先结构**。


### 3.2 交集选择器：`s1.s2.s3`

```css
    h3.special{
      color:red;3
    }
```
上面css选择的元素是同时满足两个条件：1，必须是h3标签；2，必须是special标签。

交集选择器可以连续交（一般不要这么写）
```css
    h3.special.zhongyao{
       color:red;3
    }
```
交集选择器，我们一般都是以标签名开头，比如`div.haha ` 比如`p.special。`


### 3.3 并集选择器：`,`

也叫分组选择器，用逗号就表示并集：

```css
    h3,li{
       color:red;3
    }
```

### 3.4 通配符：`*`

`*`就表示所有元素。但是效率不高，如果页面上的标签越多，效率越低，**所以页面上不能出现这个选择器。**

------------
### 3.5 标签之间的关系

- 祖先元素：直接或间接包含后代元素的元素。 
- 后代元素：直接或间接被祖先元素包含的元素。
- 父元素 ：直接包含子元素的元素。 
- 子元素：直接被父元素包含的元素。 
- 兄弟元素 ：拥有相同父元素的元素。


### 3.6 子元素选择器：`>`

IE7 开始兼容，IE6 不兼容。
```css
    div>p{
     color:red;3
    }
```
div 的儿子 p。和 div 的后代 p 的截然不同。

### 3.7  序选择器

包括：

- `x:first-child`：选择第一个子标签 
- ``x:last-child`：选择最后一个子标签 
- `x:nth-child `：选择指定类型的子元素
- `x:first-of-type `：选择指定类型的子元素
- `x:last-of-type`：选择指定类型的子元素

> IE8 开始兼容；IE6、7都不兼容

选择第 1 个 li：
```css
    <style type="text/css">
        ul li:first-child{
            color:red;
        }
    </style>
```
选择最后一个 li：
```css
    ul li:last-child{
        color:blue;
    }
```
但是由于浏览器的更新需要过程，所以现在如果公司还要求兼容IE6、7，那么就要自己写类名：

```css
<ul>
    <li class="first">项目</li>
    <li>项目</li>
    <li>项目</li>
    <li>项目</li>
    <li>项目</li>
    <li>项目</li>
    <li>项目</li>
    <li>项目</li>
    <li>项目</li>
    <li class="last">项目</li>
</ul>

//选择器
ul li.first{
    color:red;
}
ul li.last{
    color:blue;
}
```

### 3.8  兄弟选择器：`+` 和 `~`

- `+` 表示选择下一个兄弟
- `~` 表示选择后边所有的兄弟元素 

> IE7开始兼容，IE6不兼容。

```css
<style type="text/css">
   h3+p{
     color:red;
   }
</style>
```

选择上的是 h3 元素后面紧挨着的第一个兄弟。
```html
    <h3>我是一个标题</h3>
    <p>我是一个段落</p> //被选中
    <p>我是一个段落</p>
    <p>我是一个段落</p>
    <h3>我是一个标题</h3>
    <p>我是一个段落</p> //被选中
    <p>我是一个段落</p>
```

### 3.9 伪类和伪元素 

有时候，你需要选择本身没有标签，但是 仍然易于识别的网页部位，比如段落首行或鼠标滑过的连接。CSS为他们提供一些选 择器：伪类和伪元素。

```
正常链接 – a:link
访问过的链接 – a:visited（只能定义字体颜色）
鼠标滑过的链接 – a:hover 
正在点击的链接 – a:active
获取焦点 – input:focus 
选中的元素 – ::selection
指定元素前 – :before
指定元素后 – :after
首字母 – :first-letter
首行 – :first-line
```

### 3.10 属性选择器 

属性选择器可以挑选带有特殊属性的标签。 

```css
//语法
[属性名] 
[属性名="属性值"] 
[属性名~="属性值"] 
[属性名|="属性值"] 
[属性名^="属性值"] 
[属性名$="属性值"] 
[属性名*="属性值"]

//示例
p[title*="c"] {
	background-color: yellow;
}
```

### 3.11 否定伪类 

否定伪类可以帮助我们选择不是其他东西 的某件东西，比如 `p:not(.hello)` 表示选择所有的 p 元素但是 class 为 hello 的除外。 

```css
:not(选择器){
    
}
```

### 3.12 `!important`标记
```css
<style type="text/css">
    p{
    	color:red !important;
    }
    #para1{
    	color:blue;
    }
    .spec{
    	color:green;
    }
</style>
```
important是英语里面的“重要的”的意思。我们可以通过语法：`k:v !important;`
来给一个属性提高权重。这个属性的权重就是无穷大。

- `!important`提升的是一个属性，而不是一个选择器
- `!important`无法提升继承的权重，该是0还是0
- `!important`不影响就近原则
- `!important`做站的时候，不允许使用。因为会让css写的很乱。

---
## 4 在 HTML中引入 CSS 的方式

### 4.1 行内样式

```html
<div style="font-size:18cm">hello</div>
```

可以直接将样式写到标签内部的style属性 中，这种样式不用填写选择器，直接编写 声明即可。这种方式编写简单，定位准确。但是由于 直接将css代码写到了html标签的内部，导 致结构与表现耦合，同时导致样式不能够 复用，所以这种方式我们不使用。 

### 4. 2 内部样式表 

```css
<style> 
	p{color:red; font-size: 30px;} 
</style>
```

这样使css独立于html代码，而且可以同时为 多个元素设置样式，这是我们使用的比较多的 一种方式。 但是这种方式，样式只能在一个页面中使用， 不能在多个页面中重复使用。

###  4.3 外部样式表 

可以将所有的样式保存到一个外部的 css 文 件中，然后通过 `<link>` 标签将样式表引入 到文件中。

1. 链接方式，通过head标签中link标签来实现：`<link rel="stylesheet" type="text/css" href="css_3.css" />`
1. 导入方式：
```css
<style type="text/css">
    @import url(css_3.css);
    div { color:#FF0000;}
</style>
```

这种方式将样式表放入到了页面的外部， 可以在多个页面中引入，同时浏览器加载 文件时可以使用缓存，这是我们开发中使 用的最多的方式。

---

## 5 兼容问题介绍

**IE**： 微软的浏览器，随着操作系统安装的。所以每个 windows 都有 IE 浏览器。

```
windows xp    操作系统安装的IE6
windows vista 操作系统安装的IE7
windows 7     操作系统安装的IE8
windows 8     操作系统安装的IE9
windows10     操作系统安装的edge
```

浏览器兼容问题，要出就基本上就是出在 IE6、IE7 身上，这两个浏览器是非常低级的浏览器。

使用不同的浏览器打开链接 [css3-doraemon](http://www1.pconline.com.cn/pcedu/specialtopic/css3-doraemon/)。

![](index_files/a8fe2f7f-7209-474b-9856-5ac5ce220336.jpg)

浏览器的市场占有率：[百度统计]( http://tongji.baidu.com/data/)

```
IE9 5.94%
IE8 21.19%
IE7 4.79%
IE6 4.11%
```

**HTML5 浏览器打分：**

打开链接 [html5test](http://html5test.com/results/desktop.html)：

![](index_files/e24e9e67-3b2c-4c6e-9ce4-7add966e0e27.jpg)

**项目一般兼容到 IE8，但是典型的 IE6 兼容问题需要知道**
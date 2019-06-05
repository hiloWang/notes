package com.ztiany.xml.dom4j.demo;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class Dom4JDemo {

    public static void main(String... args) throws Exception {
        Dom4JDemo dom4JDemo = new Dom4JDemo();
        //dom4JDemo.test1();
        //dom4JDemo.test2();
        //dom4JDemo.test3();
        //dom4JDemo.test4();
        //dom4JDemo.test5();
        //dom4JDemo.test6();
        //dom4JDemo.test7();
        dom4JDemo.xpathTest1();
        //dom4JDemo.xpathTest2();
    }

    //1、得到某个具体的节点内容:第2本书的作者
    public void test1() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read("xml/book.xml");
        //首先得到根元素
        Element root = document.getRootElement();
        List<Element> list = root.elements("书");
        Element secondAuthor = list.get(1).element("作者");
        System.out.println(secondAuthor.getText());
    }

    //2、遍历所有元素节点：打印元素的名称
    public void test2() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read("xml/book.xml");
        //首先得到根元素
        Element root = document.getRootElement();
        treeWalk(root);
    }

    private void treeWalk(Element root) {
        System.out.println(root.getName());
        List<Element> es = root.elements();
        for (Element e : es) {
            treeWalk(e);
        }
    }

    //3、修改某个元素节点的主体内容:把第2本书的价格改为15
    public void test3() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read("xml/book.xml");
        //首先得到根元素
        Element root = document.getRootElement();
        //找到第2本书的售价
        List<Element> books = root.elements("书");
        Element secondPrice = books.get(1).element("售价");
        //设置内容
        secondPrice.setText("15");

        //保存数据到xml中
        OutputStream out = new FileOutputStream("xml/book.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        //format.setEncoding("UTF-8");//默认是UTF-8
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }

    //4、向指定元素节点中增加子元素节点:新加的都在最后。第2本书添加<批发价>20</批发价>
    public void test4() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read("xml/book.xml");
        //首先得到根元素
        Element root = document.getRootElement();
        //得到第2本书
        List<Element> es = root.elements("书");
        Element secondBook = es.get(1);
        //add内容
        secondBook.addElement("批发价").addText("" + 20);

        //保存数据到xml中
        OutputStream out = new FileOutputStream("xml/book.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        //format.setEncoding("UTF-8");//默认是UTF-8
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }

    //5、向指定元素节点上增加同级元素节点:第2本书售价前面添加一个内部价
    public void test5() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read("xml/book.xml");
        //首先得到根元素
        Element root = document.getRootElement();
        //得到第2本书
        List<Element> es = root.elements("书");
        //创建一个新元素
        Element e = DocumentHelper.createElement("内部价");
        e.setText("30");
        //建立关系
        Element secondBook = es.get(1);
        secondBook.elements().add(2, e);//List是有索引的

        //保存数据到xml中
        OutputStream out = new FileOutputStream("xml/book.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        //format.setEncoding("UTF-8");//默认是UTF-8
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }

    //6、删除指定元素节点:删除内部价
    public void test6() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read("xml/book.xml");
        //首先得到根元素
        Element root = document.getRootElement();
        //得到第2本书
        List<Element> es = root.elements("书");
        Element secondBook = es.get(1);
        secondBook.remove(secondBook.element("内部价"));

        //保存数据到xml中
        OutputStream out = new FileOutputStream("xml/book.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        //format.setEncoding("UTF-8");//默认是UTF-8
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }

    //7、操作XML文件属性:添加属性。给第1本书添加属性  出版社="黑马深圳"
    public void test7() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read("xml/book.xml");
        //首先得到根元素
        Element root = document.getRootElement();
        //得到第1本书
        root.element("书").addAttribute("出版社", "黑马深圳");

        //保存数据到xml中
        OutputStream out = new FileOutputStream("xml/book.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        //format.setEncoding("UTF-8");//默认是UTF-8
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }

    //第2本书的作者
    public void xpathTest1() throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = reader.read("xml/book.xml");
        String xpath = "//书[2]/作者";
        Node node = doc.selectSingleNode(xpath);
        System.out.println(node.getText());
    }

    //取第一本书的出版社
    public void xpathTest2() throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = reader.read("xml/book.xml");
        String xpath = "//书[1]";
        Node node = doc.selectSingleNode(xpath);
        if (node != null) {
            System.out.println(node.valueOf("@出版社"));
        }
    }

}

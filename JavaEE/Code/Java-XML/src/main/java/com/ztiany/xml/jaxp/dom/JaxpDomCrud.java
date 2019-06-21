package com.ztiany.xml.jaxp.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 利用JAXP开发包进行xmlDOM解析
 */
public class JaxpDomCrud {

    public static void main(String[] args) throws Exception {
        //得到具体的解析器（DOM方式的）
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //System.out.println(factory.getClass().getName());
        DocumentBuilder builder = factory.newDocumentBuilder();
        //解析XML文件
        Document document = builder.parse("xml/book.xml");
        //必须得到代表整棵树的Document对象

        test1(document);
        //test2(document);
        //test3(document);
        //test4(document);
        //test5(document);
        //test6(document);
        //test7(document);
    }

    //1、得到某个具体的节点内容:第2本书的作者
    private static void test1(Document document) {
        NodeList nodeList = document.getElementsByTagName("作者");//向上转型：多态
        System.out.println("得到的作者的数量：" + nodeList.getLength());
        //取第2个作者
        Node authorNode = nodeList.item(1);
        System.out.println(authorNode.getTextContent());//获取节点中的文本内容
    }

    //2、遍历所有元素节点：打印元素的名称
    private static void test2(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            //是元素节点
            System.out.println(node.getNodeName());
        }
        //遍历孩子
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            test2(n);
        }
    }

    //3、修改某个元素节点的主体内容:把第2本书的价格改为15
    private static void test3(Document document) throws Exception {
        //得到第2个售价
        Node node = document.getElementsByTagName("售价").item(1);
        //改变主体内容
        node.setTextContent("15");
        //把内存中的Document写回xml文件中
        Transformer ts = TransformerFactory.newInstance().newTransformer();
        //第一个参数：数据源 （内存中的Document） 第二个参数：目标,xml/book.xml
        ts.transform(new DOMSource(document), new StreamResult("xml/book.xml"));
    }

    //4、向指定元素节点中增加子元素节点:新加的都在最后。第2本书添加<批发价>20</批发价>
    private static void test4(Document document) throws Exception {
        //得到第2个书
        Node node = document.getElementsByTagName("书").item(1);

        //创建新的元素，并设置其主体内容
        Element newElement = document.createElement("批发价");
        newElement.setTextContent("" + 20);
        //挂接到第2本书上
        node.appendChild(newElement);

        //把内存中的Document写回xml文件中
        Transformer ts = TransformerFactory.newInstance().newTransformer();
        //第一个参数：数据源 （内存中的Document） 第二个参数：目标,xml/book.xml
        ts.transform(new DOMSource(document), new StreamResult("xml/book.xml"));
    }

    //5、向指定元素节点上增加同级元素节点:第2本书售价前面添加一个内部价
    private static void test5(Document document) throws Exception {
        //得到第2个书
        Node node = document.getElementsByTagName("书").item(1);

        //创建新的元素，并设置其主体内容
        Element newElement = document.createElement("内部价");
        newElement.setTextContent("" + 30);
        //调用insertBefore(新,参考);
        Node refNode = document.getElementsByTagName("售价").item(1);
        //父节点加子节点
        node.insertBefore(newElement, refNode);

        //把内存中的Document写回xml文件中
        Transformer ts = TransformerFactory.newInstance().newTransformer();
        //第一个参数：数据源 （内存中的Document） 第二个参数：目标,xml/book.xml
        ts.transform(new DOMSource(document), new StreamResult("xml/book.xml"));
    }

    //6、删除指定元素节点:删除内部价
    private static void test6(Document document) throws Exception {
        //找到要删除的节点
        Node node = document.getElementsByTagName("内部价").item(0);
        //删除一定要用父节点删
        node.getParentNode().removeChild(node);
        //把内存中的Document写回xml文件中
        Transformer ts = TransformerFactory.newInstance().newTransformer();
        //第一个参数：数据源 （内存中的Document） 第二个参数：目标,xml/book.xml
        ts.transform(new DOMSource(document), new StreamResult("xml/book.xml"));
    }

    //7、操作XML文件属性:添加属性。给第1本书添加属性  出版社="深圳出版社"
    private static void test7(Document document) throws Exception {

        //得到第1本书
        Node node = document.getElementsByTagName("书").item(0);
        //向下转型：Element
        //if(node.getNodeType()==Node.ELEMENT_NODE){
        if (node instanceof Element) {
            Element e = (Element) node;
            e.setAttribute("出版社", "深圳出版社");
        }

        //把内存中的Document写回xml文件中
        Transformer ts = TransformerFactory.newInstance().newTransformer();
        //第一个参数：数据源 （内存中的Document） 第二个参数：目标,xml/book.xml
        ts.transform(new DOMSource(document), new StreamResult("xml/book.xml"));
    }

}

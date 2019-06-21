package com.ztiany.xml.jaxp.sax;

import com.ztiany.xml.jaxp.Book;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 实际开发中：应该吧读到的数据封装到JavaBean
 */
public class SAXDemo2 {

    public static void main(String[] args) throws Exception {
        final List<Book> books = new ArrayList<Book>();
        //利用Jaxp的SAX解析方式封装数据
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        XMLReader reader = parser.getXMLReader();

        reader.setContentHandler(new DefaultHandler() {
            //读到开始元素：书---->创建一个book对象
            Book book = null;
            String currentTagName = null;//书   书名

            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if ("书".equals(qName)) {
                    book = new Book();
                }
                currentTagName = qName;
            }

            public void characters(char[] ch, int start, int length) {
                if ("书名".equals(currentTagName)) {
                    book.setName(new String(ch, start, length));
                }
                if ("作者".equals(currentTagName)) {
                    book.setAuthor(new String(ch, start, length));
                }
                if ("售价".equals(currentTagName)) {
                    book.setPrice(new String(ch, start, length));
                }
            }

            //读到结束元素:书----->放到List中
            public void endElement(String uri, String localName, String qName) {
                if ("书".equals(qName)) {
                    books.add(book);
                }
                currentTagName = null;
            }
        });

        reader.parse("xml/book.xml");

        //解析完毕：验证结果
        for (Book b : books) {
            System.out.println(b);
        }
    }

}

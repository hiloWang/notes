package com.ztiany.xml.pull;

import com.ztiany.xml.jaxp.Book;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PULL解析，把数据封装到JavaBean中
 */
public class PULLDemo2 {

    public static void main(String[] args) throws Exception {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        //获取要解析的内容

        List<Book> books = new ArrayList<>();

        parser.setInput(new FileInputStream("xml/book.xml"), "UTF-8");
        int eventType = parser.getEventType();
        Book book = null;
        String currentTagName = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if ("书".equals(parser.getName())) {
                    book = new Book();
                }
                currentTagName = parser.getName();
            }
            if (eventType == XmlPullParser.TEXT) {
                if ("书名".equals(currentTagName)) {
                    book.setName(parser.getText());
                }
                if ("作者".equals(currentTagName)) {
                    book.setAuthor(parser.getText());
                }
                if ("售价".equals(currentTagName)) {
                    book.setPrice(parser.getText());
                }
            }
            if (eventType == XmlPullParser.END_TAG) {
                if ("书".equals(parser.getName())) {
                    books.add(book);
                }
                currentTagName = null;
            }

            //读下一个，得到事件类型
            eventType = parser.next();
        }

        for (Book b : books) {
            System.out.println(b);
        }
    }

}

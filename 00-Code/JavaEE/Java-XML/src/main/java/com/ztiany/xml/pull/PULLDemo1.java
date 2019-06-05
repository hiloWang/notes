package com.ztiany.xml.pull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;

//明白PULL解析的过程和原理
public class PULLDemo1 {

    public static void main(String[] args) throws Exception {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        //获取要解析的内容
        parser.setInput(new FileInputStream("xml/book.xml"), "UTF-8");
        //事件类型：START DOCUMENT  STARTTAG TEXT ENDTAG ENDDOCUMENT
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {//不是读到了文档结尾
            if (eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("读到了文档的开始");
            }
            if (eventType == XmlPullParser.START_TAG) {
                System.out.println("读到了元素的开始：" + parser.getName());
            }
            if (eventType == XmlPullParser.TEXT) {
                System.out.println("读到了文本内容：" + parser.getText());
            }
            if (eventType == XmlPullParser.END_TAG) {
                System.out.println("读到了元素的结束：" + parser.getName());
            }
            if (eventType == XmlPullParser.END_DOCUMENT) {
                System.out.println("读到了文档的结束");
            }
            //读下一个，得到事件类型
            eventType = parser.next();
        }
    }

}

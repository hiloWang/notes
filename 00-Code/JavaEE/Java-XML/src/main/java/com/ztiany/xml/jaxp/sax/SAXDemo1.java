package com.ztiany.xml.jaxp.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//明白SAX解析过程和原理
public class SAXDemo1 {

    public static void main(String[] args) throws Exception {
        //得到SAX解析器
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        //得到读取器
        XMLReader reader = parser.getXMLReader();
        //注册处理器（事件）
        reader.setContentHandler(new DemoContentHandler());
        //读取xml文件
        reader.parse("xml/book.xml");
    }

    private static class DemoContentHandler implements ContentHandler {

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            System.out.println("读到了开始元素：" + qName);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            System.out.println("读到了结束元素：" + qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            System.out.println("读到了主体内容：" + new String(ch, start, length));
        }

        @Override
        public void startDocument() {
            System.out.println("读到了XML文档的开始");
        }

        @Override
        public void endDocument() {
            System.out.println("读到了XML文档的结束");
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) {
        }

        @Override
        public void endPrefixMapping(String prefix) {

        }


        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) {

        }

        @Override
        public void processingInstruction(String target, String data) {

        }

        @Override
        public void skippedEntity(String name) {

        }

        @Override
        public void setDocumentLocator(Locator locator) {

        }
    }

}

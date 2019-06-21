package com.ztiany.xml.dom4j.sample.util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * JAXP DOM工具类
 */
public class JaxpUtil {
    /**
     * 获取内存中的DOM树
     */
    public static Document getDocument() {
        Document document = null;
        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.parse("src/exam.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 把内存中的DOM写到xml文件中
     */
    public static void write2xml(Document document) {
        try {
            Transformer ts = TransformerFactory.newInstance().newTransformer();
            ts.transform(new DOMSource(document), new StreamResult("xml/exam.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.ztiany.xml.jaxp.sample;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * JAXP DOM工具类
 *
 * @author wzhting
 */
public class JaxpUtil {

    /**
     * 获取内存中的DOM树
     *
     * @return
     */
    public static Document getDocument() {
        Document document = null;
        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.parse("xml/exam.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 把内存中的DOM写到xml文件中
     *
     * @param document
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

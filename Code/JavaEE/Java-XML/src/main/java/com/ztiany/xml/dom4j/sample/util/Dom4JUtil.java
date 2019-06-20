package com.ztiany.xml.dom4j.sample.util;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class Dom4JUtil {
    public static Document getDocument() throws Exception {
        SAXReader reader = new SAXReader();
        return reader.read("xml/exam.xml");
    }

    public static void write2xml(Document document) throws Exception {
        OutputStream out = new FileOutputStream("xml/exam.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }
}

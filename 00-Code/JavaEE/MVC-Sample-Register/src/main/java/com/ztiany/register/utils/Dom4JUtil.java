package com.ztiany.register.utils;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

public class Dom4JUtil {

    private static String dbFilePath;

    static {
        ClassLoader cl = Dom4JUtil.class.getClassLoader();
        URL url = cl.getResource("users.xml");
        System.out.println("Dom4JUtil uri: " + url);
        if (url == null) {
            throw new IllegalStateException("user.xml不存在");
        }
        dbFilePath = url.getPath();
    }

    public static Document getDocument() throws Exception {
        SAXReader sr = new SAXReader();
        return sr.read(dbFilePath);
    }

    public static void write2xml(Document document) throws Exception {
        OutputStream out = new FileOutputStream(dbFilePath);
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }
}

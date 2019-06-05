package com.ztiany.xml.dom4j.sample.dao;

import com.ztiany.xml.dom4j.sample.domain.Student;
import com.ztiany.xml.dom4j.sample.util.Dom4JUtil;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;

public class StudentDaoDom4JImpl implements StudentDao {

    public boolean addStudent(Student s) {
        try {
            Document document = Dom4JUtil.getDocument();
            Element root = document.getRootElement();
            Element studentE = root.addElement("student")
                    .addAttribute("examid", s.getExamid())
                    .addAttribute("idcard", s.getIdcard());
            studentE.addElement("name").addText(s.getName());
            studentE.addElement("location").addText(s.getLocation());
            studentE.addElement("grade").addText("" + s.getGrade());
            Dom4JUtil.write2xml(document);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);//异常转义
        }
    }

    public Student findStudentByExamId(String examid) {
        System.out.println("DOM4J");
        try {
            Document document = Dom4JUtil.getDocument();
            Node node = document.selectSingleNode("//student[@examid='" + examid + "']");
            if (node == null)
                return null;

            Student s = new Student();
            s.setExamid(node.valueOf("@examid"));
            s.setIdcard(node.valueOf("@idcard"));

            Element e = (Element) node;
            //s.setName(e.elementText("name"));//直接取子元素name的主体内容
            s.setName(e.element("name").getText());
            s.setLocation(e.elementText("location"));
            s.setGrade(Float.parseFloat(e.elementText("grade")));
            return s;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delStudentByName(String name) {
        try {
            Document document = Dom4JUtil.getDocument();
            List<Node> nodes = document.selectNodes("//name");
            for (Node n : nodes) {
                if (n.getText().equals(name)) {
                    n.getParent().getParent().remove(n.getParent());
                    Dom4JUtil.write2xml(document);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

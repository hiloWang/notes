package com.ztiany.xml.jaxp.sample.dao;

import com.ztiany.xml.jaxp.sample.JaxpUtil;
import com.ztiany.xml.jaxp.sample.domain.Student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class StudentDaoJaxpDomImpl implements StudentDao {

    /*
     <student idcard="333" examid="444">
        <name>李四</name>
        <location>大连</location>
        <grade>97</grade>
    </student>
     */
    public boolean addStudent(Student s) {
        boolean result;
        //获取Document对象
        Document document = JaxpUtil.getDocument();
        //获取exam根节点
        Node rootNode = document.getElementsByTagName("exam").item(0);
        //创建student元素，设置属性
        Element studentE = document.createElement("student");//<studnet/>
        studentE.setAttribute("idcard", s.getIdcard());//<student idcard=""/>
        studentE.setAttribute("examid", s.getExamid());//<student idcard="" examid=""/>
        //创建name、locaiton、grade元素，设置主体内容
        Element nameE = document.createElement("name");//<name/>
        nameE.setTextContent(s.getName());//<name>xxx</name>
        Element locationE = document.createElement("location");
        locationE.setTextContent(s.getLocation());
        Element gradeE = document.createElement("grade");
        gradeE.setTextContent("" + s.getGrade());
        //把name等挂接到student元素上
        studentE.appendChild(nameE);
        studentE.appendChild(locationE);
        studentE.appendChild(gradeE);
        //把student挂接到根节点
        rootNode.appendChild(studentE);
        //写回到xml文件中
        JaxpUtil.write2xml(document);
        result = true;
        return result;
    }

    public Student findStudentByExamId(String examid) {

        Student s = null;

        //得到document对象
        Document document = JaxpUtil.getDocument();
        //获取所有的student元素
        NodeList nodeList = document.getElementsByTagName("student");
        //遍历：判断examid属性的值是否和参数一致
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (!(node instanceof Element))
                throw new RuntimeException("遍历节点出错了，请检查您的xml文件");
            Element studentE = (Element) node;
            if (studentE.getAttribute("examid").equals(examid)) {
                //如果一致：创建Student对象
                s = new Student();
                //设置Student的属性
                s.setIdcard(studentE.getAttribute("idcard"));
                s.setExamid(examid);
                s.setName(studentE.getElementsByTagName("name").item(0).getTextContent());
                s.setLocation(studentE.getElementsByTagName("location").item(0).getTextContent());
                s.setGrade(Float.parseFloat(studentE.getElementsByTagName("grade").item(0).getTextContent()));
                break;
            }
        }
        return s;
    }

    public boolean delStudentByName(String name) {
        boolean result = false;
        //得到Document
        Document document = JaxpUtil.getDocument();
        //获取所有的name
        NodeList nodeList = document.getElementsByTagName("name");
        //遍历：比对name标签的主体内容那个是否与参数一致
        for (int i = 0; i < nodeList.getLength(); i++) {
            //如果一致:爷爷节点干掉父节点
            Node node = nodeList.item(i);
            if (node.getTextContent().equals(name)) {
                node.getParentNode().getParentNode().removeChild(node.getParentNode());
                //写到XML文件中
                JaxpUtil.write2xml(document);
                result = true;
                break;
            }
        }
        return result;
    }

}

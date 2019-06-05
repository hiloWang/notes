package com.ztiany.xml.dom4j.sample.ui;

import com.ztiany.xml.dom4j.sample.dao.StudentDao;
import com.ztiany.xml.dom4j.sample.domain.Student;
import com.ztiany.xml.dom4j.sample.util.DaoObjectFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainView {

    public static void main(String[] args) throws Exception {
        StudentDao dao = DaoObjectFactory.getStudentDao();

        System.out.println("a、添加学生\tb、删除学生\tc、查询成绩");
        System.out.println("请输入操作类型(a|b|c)：");

        //接收用户的输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String op = br.readLine();//a|b|c
        if ("a".equals(op)) {
            add(dao, br);
        } else if ("b".equals(op)) {
            delete(dao, br);
        } else if ("c".equals(op)) {
            query(dao, br);
        } else {
            System.out.println("输入错误");
        }
    }

    @SuppressWarnings("all")
    private static void query(StudentDao dao, BufferedReader br) throws IOException {
        //查询
        System.out.println("请输入要查询的学生准考证号：");
        String examid = br.readLine();
        Student s = dao.findStudentByExamId(examid);
        if (s != null) {
            System.out.println(s.toString());
        } else {
            System.out.println("查询的学生不存在");
        }
    }

    @SuppressWarnings("all")
    private static void delete(StudentDao dao, BufferedReader br) throws IOException {
        //删除
        System.out.println("请输入删除的学生姓名：");
        String name = br.readLine();
        boolean b = dao.delStudentByName(name);
        if (b) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败或者学生不存在");
        }
    }

    @SuppressWarnings("all")
    private static void add(StudentDao dao, BufferedReader br) throws IOException {
        //添加
        System.out.println("请输入学生姓名：");
        String name = br.readLine();
        System.out.println("请输入准考证号:");
        String examid = br.readLine();
        System.out.println("请输入身份证号:");
        String idcard = br.readLine();
        System.out.println("请输入所在地：");
        String location = br.readLine();
        System.out.println("请输入成绩");
        String sgrade = br.readLine();
        float grade;
        try {
            grade = Float.parseFloat(sgrade);
        } catch (Exception e) {
            throw new RuntimeException("成绩输入有误");
        }
        Student s = new Student(idcard, examid, name, location, grade);
        boolean b = dao.addStudent(s);
        if (b) {
            System.out.println("添加成功");
        } else {
            System.out.println("服务器忙");
        }
    }


}

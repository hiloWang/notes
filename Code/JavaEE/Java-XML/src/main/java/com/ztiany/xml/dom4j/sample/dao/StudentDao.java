package com.ztiany.xml.dom4j.sample.dao;


import com.ztiany.xml.dom4j.sample.domain.Student;

public interface StudentDao {

    /**
     * 添加新的学生信息
     *
     * @param s 数据
     * @return 失败返回false
     */
    boolean addStudent(Student s);

    /**
     * 根据examid查询学生信息
     *
     * @return 没有查到返回null
     */
    Student findStudentByExamId(String examid);

    /**
     * 根据学生姓名删除学生
     *
     * @param name
     * @return 如果删除失败，返回false
     */
    boolean delStudentByName(String name);
}

package com.ztiany.xml.jaxp.sample.dao;


import com.ztiany.xml.jaxp.sample.domain.Student;

public interface StudentDao {

    /**
     * 添加新的学生信息
     *
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
     * @return 如果删除失败，返回false
     */
    boolean delStudentByName(String name);

}

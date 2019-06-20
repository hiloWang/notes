package com.ztiany.mysql.multi_table.many_to_many;


import com.ztiany.mysql.dbcp.DBCPUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class TeacherDao {

    private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());

    public void addTeacher(Teacher t) {
        try {
            //存教师的基本信息
            qr.update("insert into multi_teacher (id,name,salary) values(?,?,?)", t.getId(), t.getName(), t.getSalary());
            //查看有没有学员：
            List<Student> stus = t.getStus();
            if (stus != null && stus.size() > 0) {

                for (Student s : stus) {
                    //查询学员信息是否已经存在，不存在 存学员的基本信息
                    Student student = qr.query("select * from multi_student where id=?", new BeanHandler<Student>(Student.class), s.getId());
                    if (student == null) {
                        qr.update("insert into multi_student (id,name,grade) values(?,?,?)", s.getId(), s.getName(), s.getGrade());
                    }
                    //在第三方表中存关系
                    qr.update("insert into multi_teacher_student (t_id,s_id) values(?,?)", t.getId(), s.getId());
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Teacher findTeacherById(int teacherId) {
        try {
            //查询老师的基本信息
            Teacher t = qr.query("select * from multi_teacher where id=?", new BeanHandler<>(Teacher.class), teacherId);
            //查询学生的基本信息
            if (t != null) {
                //String sql = "select * from student where id in (select s_id from teacher_student where t_id=?)";//子查询
                //String sql = "select s.* from student s,teacher_student ts where s.id=ts.s_id and ts.t_id=?";//隐式内连接查询，并且只需要student的字段
                //String sql = "select * form student s, teacher_student ts where s.id = ts.s_id and ts.t_id =?";//隐式内连接查询
                String sql = "select s.* from multi_student s inner join multi_teacher_student ts on s.id=ts.s_id where ts.t_id=?";//内连接查询
                List<Student> stus = qr.query(sql, new BeanListHandler<>(Student.class), t.getId());
                t.setStus(stus);
            }

            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

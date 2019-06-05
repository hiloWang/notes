package com.ztiany.mysql.multi_table.many_to_many;


public class TeacherDaoImplTest {

    public static void main(String... args) {
        TeacherDaoImplTest teacherDaoImplTest = new TeacherDaoImplTest();
        teacherDaoImplTest.testAdd();
        teacherDaoImplTest.testFind();
    }

    private TeacherDao dao = new TeacherDao();

    public void testAdd() {
        Teacher t1 = new Teacher();
        t1.setId(1);
        t1.setName("BXD");
        t1.setSalary(100000);

        Teacher t2 = new Teacher();
        t2.setId(2);
        t2.setName("WZT");
        t2.setSalary(1000);

        Student s1 = new Student();
        s1.setId(1);
        s1.setName("WJ");
        s1.setGrade("A");

        Student s2 = new Student();
        s2.setId(2);
        s2.setName("XX");
        s2.setGrade("A");

        //建立关系
        t1.getStus().add(s1);
        t1.getStus().add(s2);

        t2.getStus().add(s1);
        t2.getStus().add(s2);

        dao.addTeacher(t1);
        dao.addTeacher(t2);
    }

    public void testFind() {
        Teacher t = dao.findTeacherById(2);
        System.out.println(t);
        for (Student s : t.getStus()) {
            System.out.println(s);
        }
    }
}

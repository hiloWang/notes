package com.ztiany.mysql.multi_table.one_to_many;


public class DepartmentDaoImplTest {

    public static void main(String... args) {
        DepartmentDaoImplTest test = new DepartmentDaoImplTest();
        test.testAddDepartment();
        test.testFindAllDepartment();
    }


    private DepartmentDao dao = new DepartmentDao();

    public void testAddDepartment() {
        Department d = new Department();
        d.setId(1);
        d.setName("开发部");
        d.setFloor("18层");

        Employee e1 = new Employee();
        e1.setId(1);
        e1.setName("沙悟净");
        e1.setSalary(100000);

        Employee e2 = new Employee();
        e2.setId(2);
        e2.setName("小熊");
        e2.setSalary(100000);

        d.getEmps().add(e1);
        d.getEmps().add(e2);
        dao.addDepartment(d);

    }

    public void testFindAllDepartment() {
        Department d = dao.findDepartmentById(1);
        System.out.println(d);
        for (Employee e : d.getEmps())
            System.out.println(e);
    }

}

package com.ztiany.mysql.multi_table.one_to_many;


import com.ztiany.mysql.dbcp.DBCPUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class DepartmentDao {

    private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());

    public void addDepartment(Department dept) {
        try {
            //先存部门的基本信息
            qr.update("insert into multi_department (id,name,floor) values(?,?,?)", dept.getId(), dept.getName(), dept.getFloor());
            //判断部门中有没有关联的员工，有，保存员工信息
            List<Employee> employeeList = dept.getEmps();
            if (employeeList != null && employeeList.size() > 0) {
                for (Employee e : employeeList) {
                    qr.update("insert into multi_employee (id,name,salary,depart_id) values(?,?,?,?)", e.getId(), e.getName(), e.getSalary(), dept.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //部门关联的员工要不要查？ 看需求
    public Department findDepartmentById(int departId) {
        try {
            Department d = qr.query("select * from multi_department where id=?", new BeanHandler<>(Department.class), departId);
            if (d != null) {
                List<Employee> employees = qr.query("select * from multi_employee where depart_id=?", new BeanListHandler<>(Employee.class), departId);
                d.setEmps(employees);
            }
            return d;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

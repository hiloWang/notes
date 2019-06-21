package com.ztiany.mysql.multi_table.one_to_one;


import com.ztiany.mysql.dbcp.DBCPUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class PersonDao {

    private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());

    public void addPerson(Person p) {
        try {
            qr.update("insert into multi_person(id,name) values(?,?)", p.getId(), p.getName());
            IdCard idcard = p.getIdCard();
            if (idcard != null) {
                qr.update("insert into multi_idcard(id,num) values(?,?)", p.getId(), idcard.getNum());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Person findById(int personId) {
        try {
            Person p = qr.query("select * from multi_person where id=?", new BeanHandler<>(Person.class), personId);
            if (p != null) {
                IdCard idcard = qr.query("select * from multi_idcard where id=?", new BeanHandler<>(IdCard.class), personId);
                p.setIdCard(idcard);
            }
            return p;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

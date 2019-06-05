package com.ztiany.mysql.multi_table.one_to_one;


public class PersonDaoImplTest {

    public static void main(String... args) {
        PersonDaoImplTest test = new PersonDaoImplTest();
        test.testAddPerson();
        test.testFindById();
    }

    private PersonDao dao = new PersonDao();

    public void testAddPerson() {
        Person p = new Person();
        p.setId(1);
        p.setName("WJ");

        IdCard idcard = new IdCard();
        idcard.setNum("42XXXX");

        p.setIdCard(idcard);
        dao.addPerson(p);
    }

    public void testFindById() {
        Person p = dao.findById(1);
        System.out.println(p);
        System.out.println(p.getIdCard());
    }

}

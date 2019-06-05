package com.ztiany.basic.generic.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class GenericSample {


    ///////////////////////////////////////////////////////////////////////////
    // 泛型与异常
    ///////////////////////////////////////////////////////////////////////////
    public static <T extends Exception> void testCatchException() throws T {
       /* try {

        } catch (T e) {

        } finally {

        }*/
    }

    public static <T extends Exception> void testThrowException() throws T {
        try {

        } catch (Exception e) {
            throw (T) e;
        } finally {

        }
    }

    private static void testThrowAs(Exception e) {
        throwAs(e);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwAs(Throwable throwable) throws T {
        throw (T) throwable;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 泛型与数组
    ///////////////////////////////////////////////////////////////////////////

    public static void testGenericArray() {
        Pair<String>[] pairs = new Pair[4];
        Object[] objects = pairs;
        //数组会记住它的元素类型，如果试图存入其他类型的元素，就会抛出一个ArrayStoreException异常
        objects[0] = "Hello";//运行时错误，component type is Pair
        objects[0] = new Pair<Employee>();

        addAll(new ArrayList<Pair<String>>(), new Pair<String>());

    }

    @SafeVarargs
    public static <T> void addAll(Collection<T> collection, T... ts) {
        for (int i = 0; i < ts.length; i++) {
            collection.add(ts[i]);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 泛型推断
    ///////////////////////////////////////////////////////////////////////////

    private void testAdd() {
        Number add = add(1, 1.2);
    }

    static <T> T add(T a, T b) {
        return a;
    }
    ///////////////////////////////////////////////////////////////////////////
    // 翻译泛型方法：桥方法
    ///////////////////////////////////////////////////////////////////////////

    public void testDateInterval() {
        DateInterval dateInterval = new DateInterval();
        Pair<Date> pair = dateInterval;
        pair.setSecond(new Date());
    }

    class DateInterval extends Pair<Date> {
        @Override
        public void setSecond(Date second) {
            if (second.compareTo(getFirst()) >= 0) {
                super.setSecond(second);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 通配符上限
    ///////////////////////////////////////////////////////////////////////////
    private static void printManagerList(List<Manager> managers) {
        for (Manager manager : managers) {
            System.out.println(manager.getName());
        }
    }

    private static void printEmployeeList(List<? extends Employee> employees) {
        for (Employee employee : employees) {
            System.out.println(employee.getName());
        }
    }

    private static void wildcardCeiling() {
        List<? extends Employee> list;
        List<Manager> managerList = new ArrayList<>();
        list = managerList;
        //list.add(new Employee()); 编译错误
        managerList.add(new Manager());//ok
    }

    ///////////////////////////////////////////////////////////////////////////
    // 通配符下限
    ///////////////////////////////////////////////////////////////////////////
    private static void wildcardFloor() {
        Pair<? super Manager> pair = new Pair<>();
        Object first = pair.getFirst();
        pair.setFirst(new Manager());
        pair.setSecond(new Manager() {
        });

        minMaxBonus(null, new Pair<>());
    }

    private static void minMaxBonus(Manager[] a, Pair<? super Manager> pair) {
        if (a == null || a.length == 0) {
            return;
        }
        Manager min = a[0];
        Manager max = a[0];
        for (int i = 0; i < a.length; i++) {
            if (min.getBonus() > a[i].getBonus()) {
                min = a[i];
            }
            if (max.getBonus() < a[i].getBonus()) {
                max = a[i];
            }
        }
        pair.setFirst(min);
        pair.setFirst(max);


    }

    private static void useComparator() {
        TreeSet<Manager> managers = new TreeSet<>(mManagerComparator);
        TreeSet<Manager> managers1 = new TreeSet<>(sEmployeeComparator);
    }

    private static Comparator<Manager> mManagerComparator = new Comparator<Manager>() {
        @Override
        public int compare(Manager o1, Manager o2) {
            return 0;
        }
    };

    private static Comparator<Employee> sEmployeeComparator = new Comparator<Employee>() {
        @Override
        public int compare(Employee o1, Employee o2) {
            return 0;
        }
    };

}

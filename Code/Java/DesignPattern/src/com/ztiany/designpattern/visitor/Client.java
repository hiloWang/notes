package com.ztiany.designpattern.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 19:58
 *         Email: ztiany3@gmail.com
 */
public class Client {


    public static void main(String... args) {
        CEOVisitor ceoVisitor = new CEOVisitor();
        CTOVisitor ctoVisitor = new CTOVisitor();
        BusinessReport businessReport = new BusinessReport();
        System.out.println("ceo 查看报表");
        businessReport.showReport(ceoVisitor);
        System.out.println("cto 查看报表");
        businessReport.showReport(ctoVisitor);
    }


    public static class BusinessReport {
        private List<Staff> staffList = new ArrayList<>();

        BusinessReport() {
            staffList.add(new Manager("张三"));
            staffList.add(new Engineer("李四"));
            staffList.add(new Engineer("王五"));
            staffList.add(new Engineer("赵六"));
        }

        public void showReport(Visitor visitor) {
            for (Staff staff : staffList) {
                staff.accept(visitor);
            }
        }
    }
}

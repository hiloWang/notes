package com.ztiany.principle.lkp;

/**
 * <br/>    author      Ztiany
 * <br/>    Date        2016-06-16-0016
 * <br/>    描述
 */
public class InstallWizardImpl implements InstallWizard{


    @Override
    public void runInstallWizard() {
        first();
        second();
        third();
    }

    private int third() {
        return 1;

    }

    private int second() {
        return 1;
    }

    private int first() {
        return 1;
    }
}

package com.ztiany.filter.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.4 0:31
 */
public class AllRequesterListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        System.out.println("AllRequesterListener.requestDestroyed " + sre.getServletRequest());
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        System.out.println("AllRequesterListener.requestInitialized " + sre.getServletRequest());
    }

}

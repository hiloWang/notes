package com.mwqi.server;

import com.mwqi.servlet.AppServlet;
import com.mwqi.servlet.CategoryServlet;
import com.mwqi.servlet.DetailServlet;
import com.mwqi.servlet.DownloadServlet;
import com.mwqi.servlet.GameServlet;
import com.mwqi.servlet.HomeServlet;
import com.mwqi.servlet.HotServlet;
import com.mwqi.servlet.ImageServlet;
import com.mwqi.servlet.RecommendServlet;
import com.mwqi.servlet.SubjectServlet;
import com.mwqi.servlet.UserServlet;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServlertConfig {

    public static void config(ServletContextHandler handler) {
        handler.addServlet(new ServletHolder(new CategoryServlet()), "/category");
        handler.addServlet(new ServletHolder(new ImageServlet()), "/image");
        handler.addServlet(new ServletHolder(new RecommendServlet()), "/recommend");
        handler.addServlet(new ServletHolder(new SubjectServlet()), "/subject");
        handler.addServlet(new ServletHolder(new DetailServlet()), "/detail");
        handler.addServlet(new ServletHolder(new HomeServlet()), "/home");
        handler.addServlet(new ServletHolder(new AppServlet()), "/app");
        handler.addServlet(new ServletHolder(new GameServlet()), "/game");
        handler.addServlet(new ServletHolder(new DownloadServlet()), "/download");
        handler.addServlet(new ServletHolder(new UserServlet()), "/user");
        handler.addServlet(new ServletHolder(new HotServlet()), "/hot");
    }
}

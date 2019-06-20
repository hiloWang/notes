package com.ztiany.serbase.servlets.response;

import com.ztiany.serbase.utils.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 23:34
 */
public class CodeImageServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utils.setNoCache(response);
        //图片宽高
        int width = 110;
        int height = 25;
        //BufferedImage：代表内存图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Graphics:画笔
        Graphics g = image.getGraphics();
        //画边线
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, width, height);
        //填充背景色
        g.setColor(Color.YELLOW);
        g.fillRect(1, 1, width - 2, height - 2);
        //干扰线
        Random r = new Random();
        g.setColor(Color.GRAY);
        for (int i = 0; i < 15; i++) {
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        }
        //验证码颜色
        g.setColor(Color.RED);
        //字体
        g.setFont(new Font("宋体", Font.BOLD | Font.ITALIC, 18));
        //开始画
        int x = 19;
        for (int i = 0; i < 4; i++) {
            g.drawString(r.nextInt(10) + "", x, 20);
            x += 20;
        }
        //ImageIO:输出图片给指定的流
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }
}

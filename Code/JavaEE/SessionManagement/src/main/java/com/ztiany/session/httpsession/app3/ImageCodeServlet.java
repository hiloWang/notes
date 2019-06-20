package com.ztiany.session.httpsession.app3;

import com.ztiany.session.Constants;
import com.ztiany.session.utils.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageCodeServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //通知浏览器不要缓存
        Utils.setNoCache(response);

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
        for (int i = 0; i < 15; i++)
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        //验证码
        g.setColor(Color.RED);
        g.setFont(new Font("宋体", Font.BOLD | Font.ITALIC, 18));

        int x = 19;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String code = r.nextInt(10) + "";
            sb.append(code);
            g.drawString(code, x, 20);
            x += 20;
        }

        //放到HttpSession中
        request.getSession().setAttribute(Constants.IMAGE_CODE, sb.toString());
        //ImageIO:输出图片给指定的流
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}

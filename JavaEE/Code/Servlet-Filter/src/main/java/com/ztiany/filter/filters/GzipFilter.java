package com.ztiany.filter.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 全站GZIP压缩，包装设计模式的应用
 */
public class GzipFilter implements Filter {

    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("GzipFilter.doFilter "+chain);


        HttpServletRequest request;
        HttpServletResponse response;

        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) resp;
        } catch (Exception e) {
            throw new RuntimeException("non http request");
        }

        //包装一下原有的Response，让下游的数据写入到GzipHttpServletResponse内部的缓存中
        GzipHttpServletResponse gzipHttpServletResponse = new GzipHttpServletResponse(response);

        chain.doFilter(request, gzipHttpServletResponse);//直接放行，需要的是下游的输出数据

        String gzipHeader = request.getHeader("Accept-Encoding");

        byte b[] = gzipHttpServletResponse.getBytes();//获取原始的输出数据（关键点）


        if (gzipHeader.contains("gzip")) {  //如果浏览器支持压缩

            System.out.println("压缩资源："+request.getRequestURI());
            System.out.println("压缩前大小：" + b.length);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gout = new GZIPOutputStream(out);
            gout.write(b);
            gout.close();

            b = out.toByteArray();//压缩后的数据
            System.out.println("压缩后大小：" + b.length);

            //通知浏览器，是压缩数据和压缩数据的长度
            response.setHeader("Content-Encoding", "gzip");
            response.setHeader("Content-Length", b.length + "");
        }

        response.getOutputStream().write(b);
    }

    public void destroy() {

    }


    /**
     * hook掉原有Response的字节正文输出方法，获取下游所有的正文输出
     */
    private class GzipHttpServletResponse extends HttpServletResponseWrapper {

        private ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
        private PrintWriter pw;

        GzipHttpServletResponse(HttpServletResponse response) {
            super(response);
        }

        //把数据写到一个缓存中:字节流
        public ServletOutputStream getOutputStream() {
            return new MemoryServletOutputStream(mByteArrayOutputStream);
        }

        //把数据写到一个缓存中:字符流
        public PrintWriter getWriter() throws IOException {
            pw = new PrintWriter(new OutputStreamWriter(mByteArrayOutputStream, /*设置为上游全局字符编码过滤器设置的编码*/super.getCharacterEncoding()));
            return pw;
        }

        //从mByteArrayOutputStream缓存流中获取原始的字节数据
        byte[] getBytes() {
            try {

                if (pw != null) {
                    pw.close();
                }

                mByteArrayOutputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mByteArrayOutputStream.toByteArray();
        }

    }

    private class MemoryServletOutputStream extends ServletOutputStream {

        private ByteArrayOutputStream mByteArrayOutputStream;

        MemoryServletOutputStream(ByteArrayOutputStream mByteArrayOutputStream) {
            this.mByteArrayOutputStream = mByteArrayOutputStream;
        }

        public void write(int b) {
            mByteArrayOutputStream.write(b);
        }

    }

}


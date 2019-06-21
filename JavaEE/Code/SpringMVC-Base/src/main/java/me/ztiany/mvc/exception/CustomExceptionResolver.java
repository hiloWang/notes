package me.ztiany.mvc.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器的自定义的实现类
 */
public class CustomExceptionResolver implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) {

        //通用应该用文件日志记录异常，然后邮件通知开发者

        ModelAndView mav = new ModelAndView();
        //判断异常为类型
        if (e instanceof MessageException) {
            //预期异常
            MessageException me = (MessageException) e;
            mav.addObject("error", me.getMsg());
        } else {
            mav.addObject("error", "未知异常");
        }
        mav.setViewName("error");
        return mav;
    }

}

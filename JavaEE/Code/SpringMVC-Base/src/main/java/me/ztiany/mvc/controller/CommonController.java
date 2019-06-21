package me.ztiany.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.6 23:35
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    @RequestMapping("/success.action")
    public String toSuccess() {
        return "success";
    }

}

package com.virgil.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Virgil on 2017/1/30.
 */
@Controller
@RequestMapping("/sys")
public class BaseController {

    @RequestMapping("/test")
    public String getTest() {
        System.out.println("request main...");
        return "/sys/test";
    }

    @RequestMapping("/main.html")
    public ModelAndView getMain() {
        System.out.println("fuck me");
        return new ModelAndView("/sys/main");
    }

    @RequestMapping("/main")
    public String getMain1() {
        return "/sys/main";
    }

    @RequestMapping("/user.html")
    public String getUser() {
        return "/sys/user";
    }
}

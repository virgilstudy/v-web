package com.virgil.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Virgil on 2017/1/30.
 */
@Controller
public class BaseController {

    @RequestMapping("/sys/{url}.html")
    public String getTest(@PathVariable("url") String url) {
        System.out.println("request url: " + url);
        return "sys/" + url + ".html";
    }

}

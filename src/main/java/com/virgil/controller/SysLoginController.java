package com.virgil.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.virgil.shiro.ShiroUtils;
import com.virgil.utils.R;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Virgil on 2017/1/31.
 */
@Controller
public class SysLoginController {
    @Autowired
    private Producer producer;

    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store,no-cache");
        response.setContentType("image/jpeg");
        //create captcha
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
    }

    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String username, String password, String captcha) {
        String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        System.out.println(kaptcha);
        if (!kaptcha.equalsIgnoreCase(captcha)) {
            return R.error("验证码不正确");
        }
        try {
            Subject subject = ShiroUtils.getSubject();
            password = new Sha256Hash(password).toHex();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        } catch (LockedAccountException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return R.error("账户验证失败");
        }
        System.out.println("login ok");
        return R.ok();
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();
        return "redirect:login.html";
    }

}

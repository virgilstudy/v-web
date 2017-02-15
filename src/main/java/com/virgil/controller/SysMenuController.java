package com.virgil.controller;

import com.virgil.entity.SysMenuEntity;
import com.virgil.service.SysMenuService;
import com.virgil.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Virgil on 2017/1/31.
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {

    @Autowired
    private SysMenuService sysMenuService;

    @RequestMapping("/user")
    public R user() {
        List<SysMenuEntity> userMenuList = sysMenuService.getUserMenuList(getUserId());
        return R.ok().put("menuList", userMenuList);
    }
}

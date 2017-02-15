package com.virgil.controller;

import com.virgil.entity.SysMenuEntity;
import com.virgil.entity.SysUserEntity;
import com.virgil.service.SysUserService;
import com.virgil.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Virgil on 2017/2/14.
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(Integer page, Integer limit){
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        //查询列表数据
        List<SysUserEntity> userList = sysUserService.queryList(map);
        int total = sysUserService.queryTotal(map);

        //PageUtils pageUtil = new PageUtils(userList, total, limit, page);

        return R.ok().put("page", "");
    }
}

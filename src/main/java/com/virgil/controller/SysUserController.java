package com.virgil.controller;

import com.virgil.common.persistance.Page;
import com.virgil.entity.SysMenuEntity;
import com.virgil.entity.SysRoleEntity;
import com.virgil.entity.SysUserEntity;
import com.virgil.service.SysUserRoleService;
import com.virgil.service.SysUserService;
import com.virgil.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(Integer page, Integer limit) {
        System.out.println("page: " + page + " limit: " + limit);
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        //查询列表数据
        List<SysUserEntity> userList = sysUserService.queryList(map);
        int total = sysUserService.queryTotal(map);

        Page pageResult = new Page(userList, total, limit, page);

        return R.ok().put("page", pageResult);
    }

    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId){
        SysUserEntity user = sysUserService.queryObject(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        return R.ok().put("user", user);
    }
}

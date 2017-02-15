package com.virgil.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virgil.dao.SysMenuEntityDao;
import com.virgil.entity.SysMenuEntity;
import com.virgil.service.SysMenuService;
import com.virgil.service.SysUserService;
import com.virgil.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Virgil on 2017/1/31.
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuEntityDao sysMenuEntityDao;
    @Autowired
    private SysUserService sysUserService;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = sysMenuEntityDao.simpleFindAll("parentId=?", parentId);
        if (menuIdList == null) {
            return menuList;
        }
        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return null;
    }

    @Override
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if (userId == 1) {
            return getAllMenuList(null);
        }
        //用户菜单列表
        String hql = "select m.menuId from SysUserRoleEntity ur LEFT JOIN SysRoleMenuEntity rm on ur.roleId=rm.roleId" +
                "LEFT JOIN SysMenuEntity m where ur.userId=?";
        List<Long> menuIdList = sysMenuEntityDao.find(hql, userId);
        return getAllMenuList(menuIdList);
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
        System.out.println("*******");
        try {
            System.out.println(mapper.writeValueAsString(menuList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        for (SysMenuEntity entity : menuList) {
            System.out.println(entity.getName());

            if (entity.getType() == Constant.MenuType.CATALOG.getValue()) {//目录
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

    @Override
    public SysMenuEntity queryObject(Long menuId) {
        return null;
    }

    @Override
    public List<SysMenuEntity> queryList(Map<String, Object> map) {
        return null;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return 0;
    }

    @Override
    public void save(SysMenuEntity menu) {

    }

    @Override
    public void update(SysMenuEntity menu) {

    }

    @Override
    public void deleteBatch(Long[] menuIds) {

    }
}

package com.virgil.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virgil.common.persistance.Page;
import com.virgil.dao.SysUserEntityDao;
import com.virgil.daoold.SysUserDao;
import com.virgil.entity.SysRoleEntity;
import com.virgil.entity.SysUserEntity;
import com.virgil.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Virgil on 2017/1/30.
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    //@Autowired
    private SysUserDao sysUserDao;

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SysUserEntityDao sysUserEntityDao;

    public List<String> queryAllPerms(Long userId) {
        return sysUserDao.queryAllPerms(userId);
    }

    public List<Long> queryAllMenuId(Long userId) {

        return sysUserDao.queryAllMenuId(userId);
    }

    public SysUserEntity queryByUserName(String username) {
        SysUserEntity userEntity = sysUserEntityDao.findOne("username=?", username);
        try {
            System.out.println(mapper.writeValueAsString(userEntity));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userEntity;
    }

    public SysUserEntity queryObject(Long userId) {
        return sysUserEntityDao.find(userId);
    }

    public List<SysUserEntity> queryList(Map<String, Object> map) {
        try {
            System.out.println(mapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Page page = new Page((int) map.get("offset"), (int) map.get("limit"), "userId", "DESC");
        String hql = "select a from SysUserEntity a order by " + page.getOrderBy() + " " + page.getOrder();
        System.out.println(hql);
        Page<SysUserEntity> result = sysUserEntityDao.findPage(page, hql);
        try {
            System.out.println("***res***");
            System.out.println(mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result.getList();
    }

    public int queryTotal(Map<String, Object> map) {
        return sysUserEntityDao.findAll().size();
    }

    public void save(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        sysUserDao.save(user);
        //保存用户与角色关系
    }

    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        }
        sysUserDao.update(user);

        //保存用户与角色关系

    }

    public void deleteBatch(Long[] userIds) {
        sysUserDao.deleteBatch(userIds);
    }

    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        return sysUserDao.updatePassword(map);
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {

        return null;
    }
}

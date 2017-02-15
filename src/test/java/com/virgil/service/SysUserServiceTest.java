package com.virgil.service;

import com.virgil.common.persistance.HibernateDao;
import com.virgil.dao.SysMenuEntityDao;
import com.virgil.entity.SysMenuEntity;
import com.virgil.entity.SysUserEntity;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Virgil on 2017/1/31.
 */
public class SysUserServiceTest {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuEntityDao sysMenuEntityDao;


    @Test
    public void queryObject() throws Exception {
//        String admin = new Sha256Hash("admin").toHex();
//        System.out.println(admin);



    }

}
package com.virgil.controller;

import com.virgil.entity.SysUserEntity;
import com.virgil.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Virgil on 2017/1/31.
 */
public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SysUserEntity getUser() {
        return ShiroUtils.getUserEntity();
    }

    protected Long getUserId() {
        return ShiroUtils.getUserId();
    }
}

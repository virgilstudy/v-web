package com.virgil.dao;

import com.virgil.common.persistance.HibernateDao;
import com.virgil.entity.SysUserRoleEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Virgil on 2017/1/31.
 */
@Repository("sysUserRoleEntityDao")
public class SysUserRoleEntityDao extends HibernateDao<SysUserRoleEntity, Long> {
}

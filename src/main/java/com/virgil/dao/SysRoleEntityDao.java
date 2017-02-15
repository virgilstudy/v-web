package com.virgil.dao;

import com.virgil.common.persistance.HibernateDao;
import com.virgil.entity.SysRoleEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Virgil on 2017/1/31.
 */
@Repository("sysRoleEntityDao")
public class SysRoleEntityDao extends HibernateDao<SysRoleEntity, Long> {
}

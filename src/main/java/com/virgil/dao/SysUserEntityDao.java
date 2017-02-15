package com.virgil.dao;

import com.virgil.common.persistance.HibernateDao;
import com.virgil.entity.SysUserEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Virgil on 2017/1/31.
 */
@Repository("sysUserEntityDao")
public class SysUserEntityDao extends HibernateDao<SysUserEntity, Long> {
}

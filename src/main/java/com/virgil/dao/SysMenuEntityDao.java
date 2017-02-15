package com.virgil.dao;

import com.virgil.common.persistance.HibernateDao;
import com.virgil.entity.SysMenuEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Virgil on 2017/1/31.
 */
@Repository("sysMenuEntityDao")
public class SysMenuEntityDao extends HibernateDao<SysMenuEntity, Long> {
}

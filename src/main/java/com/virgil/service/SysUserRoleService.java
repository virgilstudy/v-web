package com.virgil.service;

import java.util.List;

/**
 * Created by Virgil on 2017/2/16.
 */
public interface SysUserRoleService {
    void saveOrUpdate(Long userId, List<Long> roleIdList);

    List<Long> queryRoleIdList(Long userId);

    void delete(Long userId);
}

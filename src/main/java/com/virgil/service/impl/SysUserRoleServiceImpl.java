package com.virgil.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virgil.dao.SysUserRoleEntityDao;
import com.virgil.entity.SysUserRoleEntity;
import com.virgil.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Virgil on 2017/2/16.
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleEntityDao sysUserRoleEntityDao;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {

    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        List<SysUserRoleEntity> entities = sysUserRoleEntityDao.simpleFindAll("userId=?", userId);
        List<Long> list = new ArrayList<>();
        if (entities != null && entities.size() > 0) {
            for (SysUserRoleEntity userRoleEntity : entities) {
                try {
                    System.out.println(mapper.writeValueAsString(userRoleEntity));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                list.add(userRoleEntity.getId());
            }
        }
        return list;
    }

    @Override
    public void delete(Long userId) {

    }
}

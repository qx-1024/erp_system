package com.qiu.user.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.user.client.model.entity.Role;
import com.qiu.user.server.service.RoleService;
import com.qiu.user.server.mapper.RoleMapper;
import com.qiu.user.server.utils.SnowflakeIdGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author Qiu
* @createDate 2025-08-15 18:58:12
*/
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService{

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public boolean addRole(String code, String name, String description) {
        Role role = Role.builder()
                .id(snowflakeIdGenerator.nextId())
                .roleCode(code)
                .roleName(name)
                .description(description)
                .build();

        return roleMapper.insert(role) > 0;
    }
}





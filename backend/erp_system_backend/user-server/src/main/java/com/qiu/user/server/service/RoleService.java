package com.qiu.user.server.service;

import com.qiu.user.client.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Qiu
* @description 针对表【t_role(角色表)】的数据库操作Service
* @createDate 2025-08-15 18:58:12
*/
public interface RoleService extends IService<Role> {

    boolean addRole(String code, String name, String description);
}

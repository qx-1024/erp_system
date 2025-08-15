package com.qiu.erp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.erp.model.entity.UserRole;
import com.qiu.erp.service.UserRoleService;
import com.qiu.erp.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author Qiu
* @description 针对表【t_user_role(角色表)】的数据库操作Service实现
* @createDate 2025-08-15 20:06:42
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}





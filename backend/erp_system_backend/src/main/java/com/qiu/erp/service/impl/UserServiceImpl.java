package com.qiu.erp.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.erp.constants.RoleConstants;
import com.qiu.erp.exception.EncryptionException;
import com.qiu.erp.mapper.RoleMapper;
import com.qiu.erp.mapper.UserRoleMapper;
import com.qiu.erp.model.dto.UserDTO;
import com.qiu.erp.model.entity.Role;
import com.qiu.erp.model.entity.User;
import com.qiu.erp.model.entity.UserRole;
import com.qiu.erp.model.vo.UserVO;
import com.qiu.erp.service.UserService;
import com.qiu.erp.mapper.UserMapper;
import com.qiu.erp.utils.EncryptionUtil;
import com.qiu.erp.utils.SnowflakeIdGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:39
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(UserDTO dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);

        // 密码加密
        try {
            user.setPassword(EncryptionUtil.encrypt(dto.getPassword()));
        } catch (EncryptionException e) {
            // 捕获加密工具类抛出的具体异常
            log.error("密码加密失败 [错误码: {}]，原因: {}", e.getCode(), e.getMessage(), e);

            // 可根据错误码进行不同处理
            if (EncryptionException.KEY_INVALID.equals(e.getCode())) {
                // 密钥无效的特殊处理（如提示管理员检查配置）
                log.warn("加密密钥配置错误，请联系管理员检查配置文件");
            } else if (EncryptionException.ENCRYPT_ERROR.equals(e.getCode())) {
                // 加密过程失败的处理
                log.error("加密过程失败，请检查加密工具类实现");
            }

            return false;
        } catch (Exception e) {
            // 捕获其他未预料的异常
            log.error("密码处理时发生未知异常", e);
            return false;
        }

        user.setId(snowflakeIdGenerator.nextId());

        int insertUserRole = 0;
        UserRole userRole = new UserRole();
        if (StringUtils.isEmpty(dto.getRoleCode())){
            userRole.setUserId(user.getId());

            Role role = new LambdaQueryChainWrapper<>(roleMapper)
                    .eq(Role::getRoleCode, RoleConstants.GENERAL_STAFF)
                    .one();

            userRole.setRoleId(role.getId());

            insertUserRole = userRoleMapper.insert(userRole);
        } else {
            userRole.setUserId(user.getId());

            Role role = new LambdaQueryChainWrapper<>(roleMapper)
                    .eq(Role::getRoleCode, dto.getRoleCode().toUpperCase())
                    .one();

            userRole.setRoleId(role.getId());

            insertUserRole = userRoleMapper.insert(userRole);
        }

        int insertUser = userMapper.insert(user);

        return insertUserRole > 0 && insertUser > 0;
    }

    @Override
    public List<UserVO> getPage() {
        List<User> userList = new LambdaQueryChainWrapper<>(userMapper)
                .list();

        return userList.stream()
                .map(user -> {
                    UserVO vo = new UserVO();
                    BeanUtils.copyProperties(user, vo);

                    vo.setPassword("*****************");
                    String phone = user.getPhone();
                    if (phone != null && phone.length() == 11) {
                        String maskedPhone = phone.substring(0, 3) + "****" + phone.substring(7);
                        vo.setPhone(maskedPhone);
                    } else {
                        vo.setPhone("未知号码");
                    }

                    List<UserRole> userRoles = new LambdaQueryChainWrapper<>(userRoleMapper)
                            .eq(UserRole::getUserId, user.getId())
                            .list();

                    List<Long> roleIds = userRoles.stream()
                            .map(UserRole::getRoleId)
                            .collect(Collectors.toList());

                    if (!roleIds.isEmpty()) {
                        List<String> roleList = new LambdaQueryChainWrapper<>(roleMapper)
                                .in(Role::getId, roleIds)
                                .list()
                                .stream()
                                .map(Role::getRoleCode)
                                .collect((Collectors.toList()));
                        vo.setRole(roleList);
                    } else {
                        vo.setRole(Collections.emptyList());
                    }

                    return vo;
                })
                .collect(Collectors.toList());
    }
}





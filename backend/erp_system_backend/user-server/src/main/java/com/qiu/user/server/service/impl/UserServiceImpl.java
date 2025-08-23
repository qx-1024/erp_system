package com.qiu.user.server.service.impl;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.user.client.constants.RoleConstants;
import com.qiu.user.server.mapper.RoleMapper;
import com.qiu.user.server.mapper.UserRoleMapper;
import com.qiu.user.client.model.dto.UserDTO;
import com.qiu.user.client.model.entity.Role;
import com.qiu.user.client.model.entity.User;
import com.qiu.user.client.model.entity.UserRole;
import com.qiu.user.client.model.vo.UserVO;
import com.qiu.user.server.service.UserService;
import com.qiu.user.server.mapper.UserMapper;
import com.qiu.user.server.utils.EncryptionUtil;
import com.qiu.user.server.utils.SnowflakeIdGenerator;
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

        // 加密密码
        String encryptedPassword = EncryptionUtil.encrypt(dto.getPassword());
        if (encryptedPassword == null) {
            return false;
        }
        user.setPassword(encryptedPassword);

        // 加密手机号（非必填字段，允许为空）
        if (StringUtils.isNotEmpty(dto.getPhone())) {
            String encryptedPhone = EncryptionUtil.encrypt(dto.getPhone());
            user.setPhone(encryptedPhone);
        }

        // 加密邮箱（非必填字段，允许为空）
        if (StringUtils.isNotEmpty(dto.getEmail())) {
            String encryptedEmail = EncryptionUtil.encrypt(dto.getEmail());
            user.setEmail(encryptedEmail);
        }

        user.setId(snowflakeIdGenerator.nextId());

        int insertUserRole = 0;
        UserRole userRole = new UserRole();
        userRole.setId(snowflakeIdGenerator.nextId());
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

                    if (user.getPhone() != null ) {
                        String phone = EncryptionUtil.decrypt(user.getPhone());
                        String maskedPhone = phone.substring(0, 3) + "****" + phone.substring(7);
                        vo.setPhone(maskedPhone);
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

    @Override
    public UserVO getUserDetail(String id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            log.info("未查询到该用户=====>{}", id);
            return null;
        }

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

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
    }

    @Override
    public UserVO getUserDetailDecrypt(String id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            log.info("未查询到该用户=====>{}", id);
            return null;
        }

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        vo.setPassword(EncryptionUtil.decrypt(user.getPassword()));
        vo.setPhone(EncryptionUtil.decrypt(user.getPhone()));
        vo.setEmail(EncryptionUtil.decrypt(user.getEmail()));

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
    }

    @Override
    public String login(String username, String password) {
        User user = new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getUsername, username)
                .one();
        Long userId = user.getId();

        // 校验指定账号是否已被封禁（临时不可用，封禁时间过了就可用了）
        try {
            StpUtil.checkDisable(userId);
        } catch (DisableServiceException e){
            log.info("该用户已被封禁=====>{},封禁时长：{}", username, StpUtil.getDisableTime(userId));
            throw new RuntimeException("该用户已被封禁");
        }

        // 校验指定账号是否已被禁用（永久不可用）
        if (user.getStatus() == 1){
            log.info("该用户已被禁用=====>{}", username);
            throw new RuntimeException("该用户已被禁用");
        }

        String encryptPassword = EncryptionUtil.encrypt(password);
        if (!encryptPassword.equals(user.getPassword())) {
            log.info("密码错误======>userid：{}，登录密码：{}", user.getId(), password);
            throw new RuntimeException("密码错误");
        }


        StpUtil.login(userId);

        SaTokenInfo token = StpUtil.getTokenInfo();
        log.info("用户登录成功=====>{}", token.getTokenValue());

        return token.getTokenValue();
    }
}





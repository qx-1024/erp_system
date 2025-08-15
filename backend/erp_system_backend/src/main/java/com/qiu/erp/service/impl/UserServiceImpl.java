package com.qiu.erp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.erp.exception.EncryptionException;
import com.qiu.erp.model.dto.UserDTO;
import com.qiu.erp.model.entity.User;
import com.qiu.erp.service.UserService;
import com.qiu.erp.mapper.UserMapper;
import com.qiu.erp.utils.EncryptionUtil;
import com.qiu.erp.utils.SnowflakeIdGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(UserDTO dto) {
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

        return userMapper.insert(user) > 0;
    }
}





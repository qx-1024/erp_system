package com.qiu.erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qiu.erp.model.dto.UserDTO;
import com.qiu.erp.model.entity.User;
import com.qiu.erp.model.vo.UserVO;

import java.util.List;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:39
 */
public interface UserService extends IService<User> {

    boolean addUser(UserDTO dto);

    List<UserVO> getPage();

    UserVO getUserDetail(String id);

    UserVO getUserDetailDecrypt(String id);

}

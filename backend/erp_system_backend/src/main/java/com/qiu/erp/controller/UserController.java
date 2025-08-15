package com.qiu.erp.controller;

import com.qiu.erp.model.dto.UserDTO;
import com.qiu.erp.model.result.ResponseBody;
import com.qiu.erp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 04:32
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增用户
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "添加新用户信息，用户名不可重复")
    public ResponseBody<?> add(@RequestBody UserDTO dto) {
        return userService.add(dto) ? ResponseBody.success() : ResponseBody.failed();
    }


}

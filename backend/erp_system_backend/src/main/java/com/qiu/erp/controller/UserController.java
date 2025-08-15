package com.qiu.erp.controller;

import com.qiu.erp.model.dto.UserDTO;
import com.qiu.erp.model.result.ResponseBody;
import com.qiu.erp.model.vo.UserVO;
import com.qiu.erp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(
            summary = "新增用户",
            description = "添加新用户信息，用户名不可重复。必填字段：username（用户名）、password（密码）"
    )
    public ResponseBody<?> addUser(
            @Parameter(
                    description = "用户信息",
                    required = true,
                    schema = @Schema(implementation = UserDTO.class)
            )
            @RequestBody UserDTO dto
    ) {
        return userService.addUser(dto) ? ResponseBody.success() : ResponseBody.failed();
    }

    /**
     * 查询用户信息详情【未解密】
     */
    @GetMapping("/detail/encrypt/{id}")
    @Operation(
            summary = "查询用户信息详情【未解密】",
            description = "根据用户ID查询用户详细信息，包括用户名、手机号、邮箱、角色信息等"
            )
    public ResponseBody<UserVO> getUserDetail(
            @Parameter(description = "用户ID", required = true) @PathVariable String id) {
        return ResponseBody.success(userService.getUserDetail(id));
    }

    /**
     * 查询用户信息详情【已解密】
     */
    @GetMapping("/detail/decrypt/{id}")
    @Operation(
            summary = "查询用户信息详情【已解密】",
            description = "根据用户ID查询用户详细信息，包括用户名、手机号、邮箱、角色信息等，手机号和邮箱"
            )
    public ResponseBody<UserVO> getUserDetailDecrypt(
            @Parameter(description = "用户ID", required = true) @PathVariable String id) {
        return ResponseBody.success(userService.getUserDetailDecrypt(id));
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询用户列表", description = "查询所有用户信息")
    public ResponseBody<List<UserVO>> listUser() {
        return ResponseBody.success(userService.getPage());
    }


}

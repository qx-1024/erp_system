package com.qiu.user.server.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.qiu.user.client.model.dto.UserDTO;
import com.qiu.user.client.model.enums.UserStatusEnum;
import com.qiu.user.client.model.result.ResponseBody;
import com.qiu.user.client.model.vo.UserVO;
import com.qiu.user.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 登录
     */
    @PostMapping("/login")
    @Operation(summary = "登录", description = "用户登录，返回token")
    public ResponseBody<?> login(
            @Parameter(description = "用户名", required = true)
            @RequestParam String username,

            @Parameter(description = "密码", required = true)
            @Schema(type = "string", format = "password")
            @RequestParam(name = "password") String password
    ) {
        String token = userService.login(username, password);
        return StringUtils.isNotBlank(token) ? ResponseBody.success(token) : ResponseBody.failed("登录失败");
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    @Operation(summary = "登出", description = "用户登出，销毁token")
    public ResponseBody<?> logout() {
        StpUtil.logout();
        return ResponseBody.success();
    }

    /**
     * 永久禁用账号（永久封禁）
     */
    @PostMapping("/disable/{id}")
    @Operation(summary = "禁用账号（永久封禁）", description = "根据用户ID禁用账号，禁用后用户不可用")
    public ResponseBody<?> disableUserForever(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String id
    ) {
        return userService.changeUserStatus(id, UserStatusEnum.DISABLED.getCode()) ? ResponseBody.success() : ResponseBody.failed();
    }

    /**
     * 解除账号禁用
     */
    @PostMapping("/enable/{id}")
    @Operation(summary = "解除账号禁用", description = "根据用户ID解除账号禁用，解除后用户可正常使用")
    public ResponseBody<?> enableUserForever(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String id
    ) {
        return userService.changeUserStatus(id, UserStatusEnum.NORMAL.getCode()) ? ResponseBody.success() : ResponseBody.failed();
    }

    /**
     * 手动封号
     */
    @PostMapping("/ban/{id}/{duration}")
    @Operation(summary = "手动封号", description = "根据用户ID封号（短时间），封号后用户不可用")
    public ResponseBody<?> disableUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String id,

            @Parameter(description = "封号时长（单位：秒）")
            @PathVariable Integer duration
    ) {
        return userService.disableUser(id, duration) ? ResponseBody.success() : ResponseBody.failed();
    }


    /**
     * 手动解封
     */
    @PostMapping("/unban/{id}")
    @Operation(summary = "手动解封", description = "根据用户ID解封（短时间封号的情况），解封后用户可正常使用")
    public ResponseBody<?> enableUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable String id
    ) {
        return userService.enableUser(id) ? ResponseBody.success() : ResponseBody.failed();
    }

}

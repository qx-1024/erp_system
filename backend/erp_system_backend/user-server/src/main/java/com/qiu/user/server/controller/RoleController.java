package com.qiu.user.server.controller;

import com.qiu.user.client.model.result.ResponseBody;
import com.qiu.user.server.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 07:22
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色", description = "添加新角色信息，角色名不可重复")
    private ResponseBody<?> addRole(
            @Parameter(description = "角色编码", required = true) @RequestParam String code,
            @Parameter(description = "角色名称", required = true) @RequestParam String name,
            @Parameter(description = "角色描述") @RequestParam(required = false) String description) {
        return roleService.addRole(code, name, description) ? ResponseBody.success() : ResponseBody.failed();
    }


}

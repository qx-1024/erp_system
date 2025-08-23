package com.qiu.user.client.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HuangHaoBin
 * @date 2025-08-23, 星期六 下午 01:52
 */
@Getter
@AllArgsConstructor
@Schema(description = "用户状态枚举")
public enum UserStatusEnum {

    @Schema(description = "正常状态")
    NORMAL(0, "正常"),

    @Schema(description = "禁用状态")
    DISABLED(1, "禁用");

    /**
     * 状态编码
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 根据编码获取枚举实例
     * @param code 状态编码
     * @return 对应的枚举实例，若未找到则返回null
     */
    public static UserStatusEnum getByCode(Integer code) {
        for (UserStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}

package com.qiu.user.client.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 07:19
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    SYSTEM_ADMIN("1", "系统管理员"),
    FINANCIAL_MANAGER("2", "财务主管"),
    PURCHASE_MANAGER("3", "采购主管"),
    SALES_MANAGER("4", "销售主管"),
    FINANCIAL_STAFF("5", "财务专员"),
    PURCHASE_STAFF("6", "采购专员"),
    SALES_STAFF("7", "销售专员"),
    WAREHOUSE_MANAGER("8", "库管员"),
    DATA_ANALYST("9", "数据分析师"),
    GENERAL_STAFF("10", "普通员工");

    private final String code;
    private final String name;

    /**
     * 根据编码获取角色描述
     * @param code 角色编码
     * @return 角色描述，若编码不存在则返回null
     */
    public static String getDescByCode(String code) {
        for (RoleEnum role : values()) {
            if (code.equals(role.code)) {
                return role.name;
            }
        }
        return null;
    }

}

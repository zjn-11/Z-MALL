package com.zjn.mall.constants;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 张健宁
 * @ClassName BusinessEnum
 * @Description 业务类型状态响应码枚举类
 * @createTime 2024年09月03日 18:08:00
 */

public enum BusinessEnum {
    OPERATION_FAIL(-1, "操作失败"),
    SERVER_INNER_ERROR(9999, "服务器内部异常"),
    UN_AUTHORIZATION(401, "未授权"),
    access_deny_fail(403, "权限不足，请联系管理员")
    ;
    private Integer code;
    private String desc;

    BusinessEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

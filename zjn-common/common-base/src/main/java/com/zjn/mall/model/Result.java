package com.zjn.mall.model;

import com.zjn.mall.constants.BusinessEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 张健宁
 * @ClassName Result
 * @Description 项目统一响应结果对象
 * @createTime 2024年09月03日 17:55:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "项目统一响应结果对象")
public class Result<T> implements Serializable {
    @Schema(description = "状态码")
    private Integer code = 200;

    @Schema(description = "响应信息")
    private String msg = "ok";

    @Schema(description = "数据")
    private T data;

    public static <T> Result<T> success(T data) {
        Result result = new Result<>();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> fail(BusinessEnum businessEnum) {
        Result result = new Result<>();
        result.setCode(businessEnum.getCode());
        result.setMsg(businessEnum.getDesc());
        result.setData(null);
        return result;
    }
}

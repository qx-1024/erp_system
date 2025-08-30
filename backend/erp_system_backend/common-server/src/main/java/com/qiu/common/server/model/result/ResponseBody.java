package com.qiu.common.server.model.result;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 04:41
 */
@Data
public class ResponseBody<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    /**
     * 响应编码
     */
    private int code = 200;
    /**
     * 提示消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;


    /**
     * 附加数据
     */
    private Map<String, Object> extra;

    /**
     * 响应时间
     */
    private long timestamp;

    // 构造方法私有化，通过静态方法创建实例
    private ResponseBody() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 无参成功响应（默认状态码200，消息"操作成功"）
     */
    public static <T> ResponseBody<T> success() {
        ResponseBody<T> response = new ResponseBody<>();
        response.code = 200;
        response.message = "操作成功";
        return response;
    }

    /**
     * 带数据的成功响应（默认消息"操作成功"）
     */
    public static <T> ResponseBody<T> success(T data) {
        ResponseBody<T> response = success();
        response.data = data;
        return response;
    }

    /**
     * 创建失败响应（默认错误消息）
     */
    public static <T> ResponseBody<T> failed() {
        ResponseBody<T> response = new ResponseBody<>();
        response.code = 500;
        response.message = "操作失败";
        return response;
    }

    /**
     * 创建带错误消息的失败响应
     */
    public static <T> ResponseBody<T> failed(String message) {
        ResponseBody<T> response = failed();
        response.message = message;
        return response;
    }

    /**
     * 创建带错误码和消息的失败响应
     */
    public static <T> ResponseBody<T> failed(int code, String message) {
        ResponseBody<T> response = new ResponseBody<>();
        response.code = code;
        response.message = message;
        return response;
    }

    /**
     * 添加附加数据
     */
    public ResponseBody<T> addExtra(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        this.extra.put(key, value);
        return this;
    }

}

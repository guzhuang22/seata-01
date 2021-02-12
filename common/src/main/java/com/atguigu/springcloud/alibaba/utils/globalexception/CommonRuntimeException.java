package com.atguigu.springcloud.alibaba.utils.globalexception;


import com.atguigu.springcloud.alibaba.utils.entity.ResultCode;

/**
 * @program: ict_projectspace_server2.0
 * @description: 自定义异常类除了
 * @author: guzhuang
 * @create: 2020-11-18 11:04
 **/
public class CommonRuntimeException extends RuntimeException {

    private Integer code;

    /**
     * 使用已有的错误类型
     *
     * @param type 枚举类中的错误类型
     */
    public CommonRuntimeException(ResultCode type) {
        super(type.getMsg());
        this.code = type.getCode();
    }

    /**
     * 自定义错误类型
     *
     * @param code 自定义的错误码
     * @param msg  自定义的错误提示
     */
    public CommonRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

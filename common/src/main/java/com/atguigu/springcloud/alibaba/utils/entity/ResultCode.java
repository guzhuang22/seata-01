package com.atguigu.springcloud.alibaba.utils.entity;

/**
 * 系统中出现的错误码
 * 
 * @author XHT
 *
 */
public enum ResultCode {
    // 成功
    SUCCESS(20000, "SUCCESS."),
    // token无效或过期
    TOKEN_ERR(30001, "token无效或过期."),
    // token无对应菜单
    TOKEN_NO_MENU(30002, "token无对应菜单."),
    // 用户没有权限
    AUTH_FAIL(30003, "用户没有权限."),
    // 用户或密码为空
    PASSWD_NULL(30004, "用户或密码为空."),
    // 用户密码错误
    PASSWD_ERR(30005, "用户密码错误."),
    // 用户已登录
    USER_ALREAD_LOGIN(30006, "用户已登录."),
    // 用户名为空
    USER_NULL(30007, "用户名为空."),
    // 用户被禁用
    USER_DISABLED(30008, "用户已被禁用."),
    // redis操作失败
    REDIS_OPT_ERR(50003, "redis操作失败."),
    // 其他错误
    ERROR(50000, "其他错误."),
    // 参数错误
    PARAM_ERR(50001, "参数错误."),
    FAIL(50002, "操作失败.");

    private int code;

    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
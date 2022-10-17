package com.dykj.rpg.common.consts;

public enum UcCodeEnum {
    OK(0, "成功"),

    ADDRESS_NOT_EXIST(100, "地址不存在"),


    ACCOUNT_NOT_EXIST(200, "账号不存在"),

    CHANNEL_NOT_EXIST(300, "渠道不存在"),

    SERVER_NOT_OPEN(301, "服务器未开启"),


    SERVER_NOT_REGISTER(302, "服务器未注册"),

    PARAM_ERROR(303, "参数错误"),

    SERVER_NOT_EXIST(304, "区服不存在"),

    SYS_ERROR(305, "服务器异常");

    private int code;

    private String desc;

    UcCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

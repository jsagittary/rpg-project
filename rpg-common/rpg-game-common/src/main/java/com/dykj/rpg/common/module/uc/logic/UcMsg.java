package com.dykj.rpg.common.module.uc.logic;

import com.dykj.rpg.common.consts.UcCodeEnum;
import com.dykj.rpg.util.JsonUtil;

/**
 * @Author: jyb
 * @Date: 2020/9/4 11:33
 * @Description:
 */
public class UcMsg {
    /**
     * 状态码 0是成功
     */
    private int code;

    /**
     * 状态码描述
     */
    private String desc;

    /**
     * 信息的内容 json字符串
     */
    private String data;


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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



    public UcMsg(String data) {
        this.code= UcCodeEnum.OK.getCode();
        this.desc =UcCodeEnum.OK.getDesc();
        this.data = data;
    }

    public UcMsg(UcCodeEnum ucCodeEnum) {
        this.code= ucCodeEnum.getCode();
        this.desc =ucCodeEnum.getDesc();
    }

    public UcMsg() {
    }

    @Override
    public String toString()
    {
        return "UcMsg{" + "code=" + code + ", desc='" + desc + '\'' + ", data='" + data + '\'' + '}';
    }
}


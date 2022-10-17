package com.dykj.rpg.game.consts;

/**
 * @author jyb
 * @date 2020/12/29 15:40
 * @Description
 */
public enum  LoginTypeEnum{
    LOGIN(1),//登录

    RECONNECT(2),//重连

    REGISTER(3),//注册
    ;


    private int type;


    LoginTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
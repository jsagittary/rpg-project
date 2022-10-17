package com.dykj.rpg.common.consts.cmd;

/**
 * @Author: jyb
 * @Date: 2020/9/3 16:02
 * @Description:
 */
public interface LoginCmdConsts {
    /**
     * 登录游戏
     */
    short C2S_PLAYER_LOGIN = ModuleCodeConsts.LOGIN + 1;

    /**
     * 登录游戏结果
     */
    short S2C_LOGIN_RESULT = ModuleCodeConsts.LOGIN + 2;
    /**
     * 踢玩家下线
     */
    short S2C_OFFLINE_PLAYER = ModuleCodeConsts.LOGIN + 3;
    /**
     * 玩家注册请求
     */
    short C2S_PLAYER_REGISTER = ModuleCodeConsts.LOGIN + 4;
    /**
     * 玩家注册回复
     */
    short S2C_PLAYER_REGISTER = ModuleCodeConsts.LOGIN + 5;

}

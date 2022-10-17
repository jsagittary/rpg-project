package com.dykj.rpg.common.consts;

public enum RedisKeyEnum {
    /**
     * 游戏服务器
     */
    game_server("game_server");


//    /**
//     * 玩家账号信息
//     */
//    account_info("account_info");


    private String type;

    RedisKeyEnum(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }
}

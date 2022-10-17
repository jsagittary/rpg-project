package com.dykj.rpg.common.consts.cmd;

public enum RedisDBKeyEnum {
    /**
     * player_info
     */
    player_info("player_info");


    private String type;

    RedisDBKeyEnum(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }
}

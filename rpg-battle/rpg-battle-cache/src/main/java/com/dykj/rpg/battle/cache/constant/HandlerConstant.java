package com.dykj.rpg.battle.cache.constant;

public class HandlerConstant {

    /**
     * --------------------------------BattleCache to GameServer--------------------------------
     */
    /**
     * 组队结果
     */
    public final static byte GAMESERVER_ENTER_CACHE_RESULT = 1;

    /**
     * 进入战斗成功
     */
    public final static byte GAMESERVER_ENTER_BATTLE_SUCCESS = 2;

    /**
     * 战斗结算结果
     */
    public final static byte GAMESERVER_BATTLE_FINISH_RESULT = 3;


    /**
     * --------------------------------BattleCache to BattleServer--------------------------------
     */
    /**
     * 进入战斗请求
     */
    public final static byte BATTLESERVER_ENTER_BATTLE_REQUEST = 1;

    /**
     * 进入战斗成功
     */
    public final static byte BATTLESERVER_ENTER_BATTLE_SUCCESS = 2;


}

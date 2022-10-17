package com.dykj.rpg.battle.cache.constant;

public enum BattleTypeEnum {

    BATTLE_TYPE_GENERAL((byte)1,1);

    public byte battleType;

    public int playerNum;

    private BattleTypeEnum(byte battleType,int playerNum){
        this.battleType = battleType;
        this.playerNum = playerNum;
    }

    public static BattleTypeEnum getEnumByBattleType(byte battleType){
        BattleTypeEnum[] enums = BattleTypeEnum.values();
        for(BattleTypeEnum battleTypeEnum : enums){
            if(battleType == battleTypeEnum.battleType){
                return battleTypeEnum;
            }
        }
        return null;
    }
}
